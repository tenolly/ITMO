ORG		0x4CA		; адрес начала программы

STOP:	WORD	0x20		; стоп-символ
LEN:	WORD	0x0		; длина строки

D1:	WORD	0x3e8		; 1000
D2:	WORD	0x64		; 100
D3:	WORD	0xA		; 10

TEMP:	WORD	?		; делитель для подпрограммы
CNT:	WORD	?		; результат целочисленного деления

START:	CLA			; очистка аккумулятора

S1:	IN 0x19			; ожидание ввода символа
	AND #0x40		; проверка на наличие ввода
	BEQ S1			; нет - "спин-луп"
	IN 0x18			; вывод байта в аккумулятор
	CMP STOP		; проверяем на стоп-символ
	BEQ S2			; eсли стоп-символ - выход
	LD LEN			; берем длину строки
	INC			; увеличиваем счетчик длины
	ST LEN			; записываем обратно
	CLA			; очистка аккумулятора
	JUMP S1			; повторяем
        
S2:     LD D1                   ; считаем длину
        ST TEMP
        CALL P1
        LD D2
        ST TEMP
        CALL P1
        LD D3
        ST TEMP
        CALL P1
        CALL P4                 ; смотрим остаток

EXIT:   HLT

P1:     LD LEN
        CMP TEMP
        BLT P2
        SUB TEMP
        ST LEN
        LD CNT
        INC
        ST CNT
        JUMP P1
P2:     LD CNT
        BEQ P3
        ADD #0x30
        OUT 0xC
P3:     CLA
        RET

P4:     LD LEN
        ADD #0x30
        OUT 0xC
        CLA
        RET