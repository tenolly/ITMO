#include "image.h"
#include "transform.h"
#include <errno.h>
#include <stdint.h>
#include <stdlib.h>

struct image rotate_90_clockwise(struct image const source) {
    struct image target = {.width = source.height, .height = source.width};
    target.data = malloc(target.width * target.height * sizeof(struct pixel));
    if (target.data == NULL) exit(ENOMEM);

    for (size_t i = 0; i < source.height; ++i) {
        for (size_t j = 0; j < source.width; ++j) {
            target.data[j * target.width + i] = source.data[i * source.width + (source.width - j - 1)];
        }
    }

    return target;
}

struct image rotate_90_counterclockwise(struct image const source) {
    struct image target = {.width = source.height, .height = source.width};
    target.data = malloc(target.width * target.height * sizeof(struct pixel));
    if (target.data == NULL) exit(ENOMEM);

    for (size_t i = 0; i < source.height; ++i) {
        for (size_t j = 0; j < source.width; ++j) {
            target.data[j * target.width + (target.width - i - 1)] = source.data[i * source.width + j];
        }
    }

    return target;
}

struct image flip_horizontal(struct image const source) {
    struct image target = {.width = source.width, .height = source.height};
    target.data = malloc(target.width * target.height * sizeof(struct pixel));
    if (target.data == NULL) exit(ENOMEM);

    for (size_t i = 0; i < target.height; ++i) {
        for (size_t j = 0; j < (target.width + 1) / 2; ++j) {
            target.data[i * target.width + j] = source.data[i * source.width + (source.width - j - 1)];
            target.data[i * target.width + (target.width - j - 1)] = source.data[i * source.width + j];
        }
    }

    return target;
}

struct image flip_vertical(struct image const source) {
    struct image target = {.width = source.width, .height = source.height};
    target.data = malloc(target.width * target.height * sizeof(struct pixel));
    if (target.data == NULL) exit(ENOMEM);

    for (size_t i = 0; i < (target.height + 1) / 2; ++i) {
        for (size_t j = 0; j < target.width; ++j) {
            target.data[i * target.width + j] = source.data[(source.height - i - 1) * source.width + j];
            target.data[(target.height - i - 1) * target.width + j] = source.data[i * source.width + j];
        }
    }

    return target;
}
