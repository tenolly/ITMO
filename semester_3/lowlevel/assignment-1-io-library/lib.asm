section .text

%define SYS_READ 0
%define SYS_WRITE 1
%define SYS_EXIT 60

%define STDIN 0
%define STDOUT 1
 

exit: 
    mov rax, SYS_EXIT
    syscall 


string_length:
    mov rcx, -1             ; store string length in rcx

    .count_chars:               ; start of the loop
        inc rcx                 ; every iteration increment string length

        mov rax, [rdi + rcx]    ; write current character to rax
        test al, al             ; compare with null
        jnz .count_chars        ; ax != 0 => string hasn't ended

    mov rax, rcx            ; write string length to rax

    ret


print_string:
    push rdi                        ; push string to stack
    call string_length              ; get length of string
    pop rdi                         ; pop string from stack

    mov rdx, rax                    ; and move it in rdx (buffer size)

    mov rax, SYS_WRITE              ; "write" syscall
    mov rsi, rdi                    ; string address
    mov rdi, STDOUT                 ; stdout descriptor
    syscall

    ret

print_newline:
    mov rdi, `\n`

print_char:
    push di                 ; push char address to the stack

    mov rax, SYS_WRITE      ; "write" syscall
    mov rdi, STDOUT         ; stdout descriptor
    mov rsi, rsp            ; string address (the head of stack)
    mov rdx, 1              ; string length in bytes
    syscall

    pop di                  ; pop char address from the stack
    ret


print_int:
    test rdi, rdi           ; compare with 0
    jge print_uint          ; if rdi >= 0, make no changes

    push rdi
    mov rdi, `-`            ; otherwise output minus
    call print_char
    pop rdi

    neg rdi                 ; make it positive to right output

print_uint:
    push r15                ; save regs
    push r14                

    sub rsp, 24             ; allocate memory (log10(2^64) = 20) and align stack
    mov r15, rsp            ; store string pointer
    add r15, 24             ; move pointer to the end of stack

    mov r14, 10             ; delimiter

    mov rax, rdi            ; move number to rax
    .parse_int:
        xor rdx, rdx        ; clear rdx
        div r14             ; quotient -> rax, remainder -> rdx
        add dl, `0`         ; convert remainder to ASCII
        dec r15             ; move pointer back
        mov [r15], dl       ; write to stack
        test rax, rax       ; check if rax == 0
        jnz .parse_int      ; if not zero, continue

    mov rdi, r15            ; rdi points to start of the string
    
    call print_string       ; print the string

    add rsp, 24             ; free memory and align stack

    pop r14                 ; restore regs
    pop r15
    ret


string_equals:
    push r15                ; save regs before changing

    mov rcx, -1             ; store current index in rcx

    .cmp_chars_loop:        ; start of the loop
        inc rcx                 ; every iteration increment string length

        mov r15, [rdi + rcx]    ; write current str1 characters to r15
        mov rax, [rsi + rcx]    ; write current str2 characters to rax

        test al, al             ; compare first character with null (the end of string)
        jnz .cmp_chars          ; if not null, jump to comparing chars.
        test r15b, r15b         ; otherwise compare second character with null
        jnz .break_with_0       ; if second is not null, string are not equals
        jmp .break_with_1       ; otherwise both of them has ended and equals

        .cmp_chars:
        cmp r15b, al            ; compare characters
        jz .cmp_chars_loop      ; and continue if equals

        .break_with_0:
        xor rax, rax            ; "not equal"
        jmp .exit

    .break_with_1:
    mov rax, 1

    .exit:      
    pop r15                     ; restore regs after changing
    ret


read_char:
    xor eax, eax            ; "read" syscall, is equivalent of mov rax, SYS_READ
    push rax                ; allocate memory in stack (also 0 is default value if no char)

    mov rdi, STDIN          ; stdin descriptor
    mov rsi, rsp            ; string address (the head of stack)
    mov rdx, 1              ; string length in bytes
    syscall

    pop rax                 ; put char to rax

    ret 


read_word:
    test rsi, rsi           ; if buffer size is 0 we can't write into
    jz .exit

    xor rax, rax 

    push r15                ; save regs
    push r14
    push r13

    mov r14, rdi            ; save buffer address
    mov r13, rsi            ; save buffer size
    
    .skip_spaces:
    call read_char
    cmp rax, ` `            ; compare with space
    jz .skip_spaces
    cmp rax, `\n`           ; compare with \n
    jz .skip_spaces
    cmp rax, `\t`           ; compare with \t
    jz .skip_spaces

    xor r15, r15            ; counter

    .read:
        mov [r14 + r15], rax    ; save char
        inc r15                 ; increment counter
        cmp r15, r13
        jz .make_result
        test rax, rax           ; compare with null
        jz .make_result

        call read_char
        cmp rax, ` `            ; compare with space
        jz .break
        cmp rax, `\n`           ; compare with \n
        jz .break
        cmp rax, `\t`           ; compare with \t
        jz .break

        jmp .read

    .break:                 ; use it if has read space-symbol
    xor rax, rax
    mov [r14 + r15], rax
    inc r15

    .make_result:           ; check last read char and make result 
        test rax, rax           ; depends on it
        jz .good
        jmp .bad

        .good:                  ; word fit buffer
            mov rax, r14
            mov rdx, r15
            dec rdx
            jmp .restore
            
        .bad:                   ; word doesn't fit buffer
            xor rax, rax
            jmp .restore

    .restore:
    pop r13                 ; restore regs
    pop r14
    pop r15
    
    .exit:
    ret


parse_uint:
    push r14                ; save regs
    push r13
    push r12

    xor rax, rax            ; result
    xor r12, r12            ; length
    xor r13, r13            ; current number
    mov r14, 10             ; delimiter

    .parse_int:
        mov r13b, [rdi + r12]       ; get current number
        test r13b, r13b             ; check if r13b == 0
        jz .return

        sub r13b, `0`               ; convert from ASCII
        test r13b, r13b             ; and check 0 <= r13b <= 9
        jl .return
        cmp r13b, 9
        jg .return

        mul r14                     ; result -> ax
        add rax, r13                ; update number

        inc r12

        jmp .parse_int

    .return:
    mov rdx, r12

    pop r12
    pop r13                 ; restore regs
    pop r14
    ret


parse_int:
    push r15                ; save reg

    mov r15, [rdi]          ; sign (can be)

    cmp r15b, `+`           ; if number has sign, remove it
    jz .remove_sign
    cmp r15b, `-`
    jz .remove_sign
    jmp .parse              ; otherwise do nothing
    
    .remove_sign:
    inc rdi                 ; remove sign from number
    .parse:
    call parse_uint
    test rdx, rdx           ; failed to parse int
    jz .return

    cmp r15b, `-`           ; process signs
    jnz .return
    neg rax                 ; make number negative
    inc rdx                 ; add 1 to length because of sign
    jmp .return

    .return:
    pop r15                 ; restore reg

    ret 


string_copy:
    push rsi            ; save regs
    push rdi
    push rdx

    call string_length  ; get string length

    pop rdx             ; restore regs
    pop rdi             ; restore regs
    pop rsi

    cmp rax, rdx        ; compare string and buffer length
    ja .exit_with_0     ; if string length > buffer length, return 0

    push r15            ; save r15

    mov rcx, rax        ; count of iterations 
    inc rcx
    .copy_loop:
        mov r15b, [rdi]     ; copy char from string
        mov [rsi], r15b     ; and put it into buffer
        inc rsi             ; next char
        inc rdi             ; next char
        loop .copy_loop

    pop r15            ; restore r15

    ret
    .exit_with_0:
    xor rax, rax
    ret
