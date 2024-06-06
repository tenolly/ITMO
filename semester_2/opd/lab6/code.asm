ORG     0x0
V0:     WORD    $DEFAULT,   0X180
V1:     WORD    $DEFAULT,   0X180
V2:     WORD    $INT2,      0X180
V3:     WORD    $INT3,      0x180
V4:     WORD    $DEFAULT,   0X180
V5:     WORD    $DEFAULT,   0X180
V6:     WORD    $DEFAULT,   0X180
V7:     WORD    $DEFAULT,   0X180

ORG     0x028
X:      WORD    0x0014
MAX_X:  WORD    0x0028
MIN_X:  WORD    0xFFD4

DEFAULT:    IRET
START:      DI
            CLA
            OUT 0x1
            OUT 0x3
            OUT 0xB
            OUT 0xD
            OUT 0x11
            OUT 0x15
            OUT 0x19
            OUT 0x1D
            LD #0xA 	; разрешение прерывания для ВУ-2
            OUT 5
            LD #0xB     ; разрешение прерывания для ВУ-3
            OUT 7
            EI

MAIN:   	DI 		   	; Запрет прерываний чтобы обеспечить атомарность операции
   			LD X
    		DEC
   			CALL CHECK
    		ST X
    		EI
    		JUMP MAIN

CHECK:      
CHECK_MAX:  CMP MAX_X
            BLT CHECK_MIN
            CMP MAX_X
            BEQ EXIT
            JUMP SET_MAX
CHECK_MIN:  CMP MIN_X
            BGE EXIT
SET_MAX:    LD MAX_X
EXIT:       RET

INT2:       DI
            PUSH
            IN 0x4
            NEG
            ADD X
            CALL CHECK
            ST X
            HLT
            POP
            EI
            IRET

INT3:       DI
            PUSH
            LD X
            ASL
            ADD X
            ADD #6
            OUT 0x6
            NOP
            POP
            EI
            IRET