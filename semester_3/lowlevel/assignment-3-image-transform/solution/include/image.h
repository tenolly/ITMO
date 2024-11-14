#ifndef IMAGE_H
#define IMAGE_H

#include <stdint.h>

struct pixel { uint8_t b, g, r; };

struct image {
    uint64_t width, height;
    struct pixel* data;
};

void copy_image(struct image* target, struct image* source);
void free_image(struct image* img);

uint8_t calc_image_padding(uint64_t width, uint8_t pixel_size);

#endif
