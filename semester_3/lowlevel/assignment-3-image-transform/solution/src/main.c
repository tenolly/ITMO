#include "bmp.h"
#include "image.h"
#include "transform.h"
#include <errno.h>
#include <inttypes.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


int main(int argc, char** argv) {
    const int EXPECTED_ARGS_COUNT = 4;
    if (argc != EXPECTED_ARGS_COUNT) {
        fprintf(stderr, "Unexpected count of arguments: %d (expected %d) \n", argc - 1, EXPECTED_ARGS_COUNT - 1);
        fprintf(stderr, "Check you use following arguments: <source-image> <transformed-image> <tranformation>");
        return EPERM;
    }

    const char* source_filename = argv[1];
    const char* target_filename = argv[2];
    const char* operation = argv[3];

    // Source file

    FILE *source_file = fopen(source_filename, "rb");
    if (source_file == NULL) {
        fprintf(stderr, "Unable to open source file \"%s\"", source_filename);
        return ENOENT;
    }

    struct image source_image = {0};
    enum bmp_read_status read_status = from_bmp(source_file, &source_image);
    fclose(source_file);

    if (read_status != READ_OK) {
        fprintf(stderr, "Some error occurred during reading file \"%s\" (exit code: %d)", source_filename, read_status);
        free_image(&source_image);
        return read_status;
    }
    
    // Target file

    FILE *target_file = fopen(target_filename, "wb");
    if (target_file == NULL) {
        fprintf(stderr, "Unable to open target file \"%s\"", target_filename);
        free_image(&source_image);
        return ENOENT;
    }

    struct image target_image = {0};
    if (strcmp(operation, "none") == 0) copy_image(&target_image, &source_image);
    else if (strcmp(operation, "cw90") == 0) target_image = rotate_90_clockwise(source_image);
    else if (strcmp(operation, "ccw90") == 0) target_image = rotate_90_counterclockwise(source_image);
    else if (strcmp(operation, "fliph") == 0) target_image = flip_horizontal(source_image);
    else if (strcmp(operation, "flipv") == 0) target_image = flip_vertical(source_image);
    else {
        fprintf(stderr, "Undefined transformation: \"%s\"", operation);
        free_image(&source_image);
        free_image(&target_image);
        fclose(target_file);
        return EPERM;
    }

    enum bmp_write_status write_status = to_bmp(target_file, &target_image);
    fclose(target_file);

    free_image(&source_image);
    free_image(&target_image);

    if (write_status != WRITE_OK) {
        fprintf(stderr, "Some error occurred during writing into file \"%s\" (exit code: %d)", target_filename, write_status);
        return write_status;
    }

    return 0;
}
