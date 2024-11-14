#ifndef TRANSFORM_H
#define TRANSFORM_H

#include "image.h"
#include <stdint.h>

extern struct image rotate_90_clockwise(struct image const source);
extern struct image rotate_90_counterclockwise(struct image const source);
extern struct image flip_horizontal(struct image const source);
extern struct image flip_vertical(struct image const source);

#endif
