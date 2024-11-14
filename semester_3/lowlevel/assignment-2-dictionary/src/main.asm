%include "io-library/lib.inc"
%include "src/dict.inc"

%define SYS_WRITE 1

%define STDIN 0
%define STDOUT 1
%define STDERR 2

section .bss
buffer db 255 dup(?)

section .rodata
%include "src/words.inc"

not_found: db "word not found"
.end:

too_big: db "key is too big"
.end:


section .text
global _start

_start:
    mov rdi, buffer
    mov rsi, 256
    xor rdx, rdx                ; do not skip spaces flag
    call read_word

    test rax, rax
    je print_too_big_error

    mov rdi, buffer
    mov rsi, pointer
    call find_word

    test rax, rax
    je print_not_found_error

    mov rdi, rax
    call print_string
    call exit
    

print_not_found_error:
    mov rdx, not_found.end - not_found
    mov rax, SYS_WRITE
    mov rsi, not_found
    mov rdi, STDERR
    syscall

    call exit

print_too_big_error:
    mov rdx, too_big.end - too_big
    mov rax, SYS_WRITE
    mov rsi, too_big
    mov rdi, STDERR
    syscall

    call exit
