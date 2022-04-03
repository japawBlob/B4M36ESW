#ifndef IMGPROC_H
#define IMGPROC_H

#include <vector>
#include <opencv2/core/core.hpp>
#include "ellipse.h"

class MatPhases : public std::vector<cv::Mat> {
public:
    enum indexes {
        ORIG,                   /* Original image */
        RESIZE,                 /* Resized (smaller) image */
        GRAY,                   /* Converted to gray */
        BLUR,                   /* Blurred image */
        GRAD,                   /* Gradient: HSV→RGB visualization of gradient (angle and magnitude) */
        BW,                     /* Black&White (Canny) */
        VIS_EL,                 /* Visualization of detected ellipse */
        __COUNT
    };

    reference operator[](size_type n) {
        if (size() < n + 1)
            resize(n + 1);
        return std::vector<value_type>::operator [](n);
    }
};

struct ImgProcParameters {
    unsigned min_width = 70;
    unsigned min_height = 70;
    float max_axis_ratio = 7;
    unsigned iterations = 100;
};

void processImage(cv::Mat &image, MatPhases &phases, std::vector<Ellipse> &ellipses, const ImgProcParameters &param);

cv::Mat debugOnPixelEllipse(cv::Mat1b &bw, cv::Point p, const ImgProcParameters &param, Ellipse &e);

#endif
