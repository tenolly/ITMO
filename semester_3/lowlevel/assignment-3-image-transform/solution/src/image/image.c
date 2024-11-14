#include "image.h"
#include <errno.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

uint8_t calc_image_padding(uint64_t width, uint8_t pixel_size) {
    return 4 - (width * pixel_size) % 4;
}

void copy_image(struct image* target, struct image* source) {
    target->width = source->width;
    target->height = source->height;

    target->data = malloc(source->width * source->height * sizeof(struct pixel));
    if (target->data == NULL) exit(ENOMEM);
    
    memcpy(target->data, source->data, source->width * source->height * sizeof(struct pixel));
}

void free_image(struct image* img) {
    if (img != NULL) {
        if (img->data != NULL) {
            free(img->data);
            img->data = NULL;
        }
    }
}
