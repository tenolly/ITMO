#include <sys/mman.h>

#include "mem.h"
#include "mem_internals.h"

#define HEAP_SIZE 1024
#define MAP_ANONYMOUS 0x20


void init_tests() {
    heap_init(HEAP_SIZE);
}

void destroy_tests() {
    heap_term();
}

void test_allocate_one_block() {
    const char* test_name = "test_allocate_one_block";

    void* addr = _malloc(30);

    if (addr == NULL) {
        printf("Unable to allocate one block\n");
        printf("%s -- failed\n", test_name);
        return;
    }

    _free(addr);

    printf("%s -- passed\n", test_name);
}

void test_free_one_block_of_free() {
    const char* test_name = "test_free_one_block_of_free";

    void* addr1 = _malloc(30);
    void* addr2 = _malloc(30);
    void* addr3 = _malloc(30);
    
    if (addr1 == NULL) {
        printf("Unable to allocate first block\n");
        printf("%s -- failed\n", test_name);
        return;
    }

    if (addr2 == NULL) {
        printf("Unable to allocate second block\n");
        printf("%s -- failed\n", test_name);
        _free(addr1);
        return;
    }

    if (addr3 == NULL) {
        printf("Unable to allocate third block\n");
        printf("%s -- failed\n", test_name);
        _free(addr1);
        _free(addr2);
        return;
    }

    struct block_header* block1 = (struct block_header*)(addr1 - offsetof(struct block_header, contents));
    struct block_header* block2 = (struct block_header*)(addr2 - offsetof(struct block_header, contents));
    struct block_header* block3 = (struct block_header*)(addr3 - offsetof(struct block_header, contents));

    _free(addr2);
    if (block1->is_free || !block2->is_free || block3->is_free) {
        printf(
            "Incorrect memory free:\n" 
            "Expected:\n"
            " - first block is freed: false\n"
            " - second block is freed: true\n"
            " - third block is freed: false\n"
            "In result:\n"
            " - first block is freed: %s\n"
            " - second block is freed: %s\n"
            " - third block is freed: %s\n",
            block1->is_free ? "true" : "false",
            block2->is_free ? "true" : "false",
            block3->is_free ? "true" : "false"
        );
        printf("%s -- failed\n", test_name);
        _free(addr1);
        _free(addr3);
        return;
    }

    _free(addr1);
    _free(addr3);

    printf("%s -- passed\n", test_name);
}

void test_free_two_blocks_of_free() {
    const char* test_name = "test_free_two_blocks_of_free";

    void* addr1 = _malloc(30);
    void* addr2 = _malloc(30);
    void* addr3 = _malloc(30);

    if (addr1 == NULL) {
        printf("Unable to allocate first block\n");
        printf("%s -- failed\n", test_name);
        return;
    }

    if (addr2 == NULL) {
        printf("Unable to allocate second block\n");
        printf("%s -- failed\n", test_name);
        _free(addr1);
        return;
    }

    if (addr3 == NULL) {
        printf("Unable to allocate third block\n");
        printf("%s -- failed\n", test_name);
        _free(addr1);
        _free(addr2);
        return;
    }

    struct block_header* block1 = (struct block_header*)(addr1 - offsetof(struct block_header, contents));
    struct block_header* block2 = (struct block_header*)(addr2 - offsetof(struct block_header, contents));
    struct block_header* block3 = (struct block_header*)(addr3 - offsetof(struct block_header, contents));

    _free(addr1);
    _free(addr3);
    if (!block1->is_free || block2->is_free || !block3->is_free) {
        printf(
            "Incorrect memory free:\n" 
            "Expected:\n"
            " - first block is freed: false\n"
            " - second block is freed: true\n"
            " - third block is freed: false\n"
            "In result:\n"
            " - first block is freed: %s\n"
            " - second block is freed: %s\n"
            " - third block is freed: %s\n",
            block1->is_free ? "true" : "false",
            block2->is_free ? "true" : "false",
            block3->is_free ? "true" : "false"
        );
        printf("%s -- failed\n", test_name);
        _free(addr2);
        return;
    }

    _free(addr2);

    printf("%s -- passed\n", test_name);
}

void test_expand_region_from_old_one() {
    const char* test_name = "test_expand_region_from_old_one";

    void* addr1 = _malloc(HEAP_SIZE * 0.75);
    void* addr2 = _malloc(HEAP_SIZE * 0.75);

    if (addr1 == NULL) {
        printf("Unable to allocate first block\n");
        printf("%s -- failed\n", test_name);
        return;
    }

    if (addr2 == NULL) {
        printf("Unable to allocate second block\n");
        printf("%s -- failed\n", test_name);
        _free(addr1);
        return;
    }

    struct block_header* block1 = (struct block_header*)(addr1 - offsetof(struct block_header, contents));
    struct block_header* block2 = (struct block_header*)(addr2 - offsetof(struct block_header, contents));

    if (block1->next != block2) {
        printf("Unable to expand new region from old region\n");
        printf("%s -- failed\n", test_name);
        _free(addr1);
        _free(addr2);
        return;
    }

    _free(addr1);
    _free(addr2);
    printf("%s -- passed\n", test_name);
}

void test_expand_region_to_another_place() {
    const char* test_name = "test_expand_region_to_another_place";

    if (mmap(HEAP_START + HEAP_SIZE, HEAP_SIZE, 0, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0) == MAP_FAILED) {
        printf("Unable to map pages\n");
        printf("%s -- failed\n", test_name);
        return;
    };

    void* addr1 = _malloc(HEAP_SIZE);

    munmap(HEAP_START + HEAP_SIZE, HEAP_SIZE);

    if (addr1 == NULL) {
        printf("Unable to allocate block\n");
        printf("%s -- failed\n", test_name);
        return;
    }

    _free(addr1);
    printf("%s -- passed\n", test_name);
}