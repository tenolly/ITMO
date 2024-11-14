ASM=nasm
ASMFLAGS=-felf64

LD=ld

BUILD_FOLDER=build
OBJ_FOLDER=$(BUILD_FOLDER)/object
BIN_FOLDER=$(BUILD_FOLDER)/bin


all: main

main: lib.o dict.o build_dir
	$(ASM) src/main.asm $(ASMFLAGS) -o $(OBJ_FOLDER)/main.o
	$(LD) -o $(BIN_FOLDER)/main $(OBJ_FOLDER)/main.o $(OBJ_FOLDER)/lib.o $(OBJ_FOLDER)/dict.o

test: main test.py
	python3 test.py

lib.o: build_dir update_lib io-library/lib.asm
	$(ASM) io-library/lib.asm $(ASMFLAGS) -o $(OBJ_FOLDER)/lib.o

dict.o: build_dir src/dict.asm io-library/lib.asm
	$(ASM) src/dict.asm $(ASMFLAGS) -o $(OBJ_FOLDER)/dict.o

build_dir: 
	mkdir -p $(BUILD_FOLDER)
	mkdir -p $(OBJ_FOLDER)
	mkdir -p $(BIN_FOLDER)

update_lib:
	git submodule update --init --recursive
	git submodule update --remote --merge

.PHONY: all main test build_dir update_lib
