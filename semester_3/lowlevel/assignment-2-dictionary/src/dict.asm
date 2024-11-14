%include "io-library/lib.inc"

%define LABLE_LENGTH 8

section .text
global find_word

 
find_word:
    sub rsp, 8              ; align stack
    push r15                ; will store rdi (buffer pointer) here
    push r14                ; will store rsi (dict pointer) here
    
    mov r15, rdi
    mov r14, rsi

    .skip_keys:
        test r14, r14
        je .return_0

        lea rsi, [r14 + 8]
        mov rdi, r15            ; restore rdi
        call string_equals

        test rax, rax
        jne .return
        
        mov r14, [r14]          ; go to next items

        jmp .skip_keys

.return_0:
    xor rax, rax
    jmp .restore

.return:
    lea rax, [r14 + LABLE_LENGTH]              ; set pointer to key (skip label)
    call string_length
    lea rax, [r14 + rax + LABLE_LENGTH + 1]    ; skip key + addr and set pointer to value

.restore:
    mov rdi, r15
    pop r14
    pop r15
    add rsp, 8

    ret
