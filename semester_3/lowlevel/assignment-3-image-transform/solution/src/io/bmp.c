#include "bmp.h"
#include "image.h"
#include <errno.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

enum bmp_read_status from_bmp(FILE* in, struct image* img) {
    if (in == NULL || img == NULL) return READ_ERROR;

    struct bmp_header* header = malloc(sizeof(struct bmp_header));
    if (header == NULL) exit(ENOMEM);

    if (fread(header, sizeof(*header), 1, in) != 1) {
        free(header);
        return READ_INVALID_HEADER;
    }

    img->width = header->biWidth;
    img->height = header->biHeight;
    img->data = malloc(sizeof(struct pixel) * img->width * img->height);
    if (img->data == NULL) {
        free(header);
        exit(ENOMEM);
    }

    if (fseek(in, (long) header->bfOffBits, SEEK_SET) == -1) {
        free(header);
        free_image(img);
        return READ_INVALID_SIGNATURE;
    };

    const uint8_t padding = calc_image_padding(img->width, sizeof(struct pixel));
    for (size_t i = 0; i < img->height; ++i) {
        if (
            img->data + i * img->width == NULL ||
            fread(img->data + i * img->width, sizeof(struct pixel), img->width, in) != img->width ||
            fseek(in, padding, SEEK_CUR) == -1
        ) {
            free(header);
            free_image(img);
            return READ_INVALID_BITS;
        }
    }

    free(header);

    return READ_OK;
}

enum bmp_write_status to_bmp(FILE* out, struct image const* img) {
    if (out == NULL || img == NULL) return WRITE_ERROR;

    const uint8_t padding = calc_image_padding(img->width, sizeof(struct pixel));

    const uint32_t header_size = sizeof(struct bmp_header);
    const uint32_t image_size = (img->width * sizeof(struct pixel) + padding) * img->height;

    struct bmp_header header = {
        .bfType = 0x4D42,
        .bfileSize = header_size + image_size,
        .bfReserved = 0,
        .bfOffBits = header_size,
        .biSize = 40,
        .biWidth = img->width,
        .biHeight = img->height,
        .biPlanes = 1,
        .biBitCount = sizeof(struct pixel) * 8,
        .biCompression = 0,
        .biSizeImage = image_size,
        .biXPelsPerMeter = 0,
        .biYPelsPerMeter = 0,
        .biClrUsed = 0,
        .biClrImportant = 0
    };

    if (fwrite(&header, sizeof(header), 1, out) != 1 || fseek(out, (long) header.bfOffBits, SEEK_SET) == -1) {
        return WRITE_ERROR;
    }
    
    const uint8_t padding_bytes[3] = {0};
    for (size_t i = 0; i < img->height; ++i) {
        if (
            fwrite(img->data + i * img->width, sizeof(struct pixel), img->width, out) != img->width ||
            fwrite(padding_bytes, 1, padding, out) != padding
        ) {
            return WRITE_ERROR;
        }
    }

    return WRITE_OK;
}
