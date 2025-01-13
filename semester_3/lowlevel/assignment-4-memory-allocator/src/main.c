#include "mem.h"
#include "tests.h"

int main() {
    init_tests();
    test_allocate_one_block();
    test_free_one_block_of_free();
    test_free_two_blocks_of_free();
    test_expand_region_from_old_one();
    test_expand_region_to_another_place();
    destroy_tests();
}
