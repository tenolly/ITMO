#ifndef MY_OWN_TESTS

#define MY_OWN_TESTS

void init_tests();
void destroy_tests();
void test_allocate_one_block();
void test_free_one_block_of_free();
void test_free_two_blocks_of_free();
void test_expand_region_from_old_one();
void test_expand_region_to_another_place();

#endif