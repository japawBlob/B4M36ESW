/*
 * Copyright (C) 2017, 2022 Michal Sojka <sojkam1@fel.cvut.cz>
 * Copyright (C) 2017 Joel Matějka <matejjoe@fel.cvut.cz>
 *
 * This file is part of find_ellipse.
 *
 * Find_ellipse is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Find_ellipse is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Find_ellipse. If not, see
 * <http://www.gnu.org/licenses/>.
 */

#define _USE_MATH_DEFINES // Needed by MSVC++
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <vector>
#include <algorithm>
#include <math.h>
#include <queue>

// debug
#include <opencv2/highgui/highgui.hpp>

#include "imgproc.h"

#include "table3x3.hpp"

using namespace cv;

typedef std::vector<Point> Points;
typedef std::vector<Points> Components;

static bool isNiceEllipse(const Ellipse e, const ImgProcParameters &param)
{
    return !e.isInvalid() &&
           e.size.width >= param.min_width &&
           e.size.height >= param.min_height &&
           e.size.width / e.size.height <= param.max_axis_ratio;
}

Points findContigComponentAt(Mat1b &bw, Point point, uint8_t newVal = 0)
{
    std::vector<Point> open;
    std::vector<Point> comp;

    if (!bw(point))
            return comp;

    open.push_back(point);

    while (open.size() > 0) {
        Point p = open.back();
        open.pop_back();
        comp.push_back(p);
        bw(p) = newVal;

        for (int yy = -1; yy <= +1; yy++) {
            for (int xx = -1; xx <= +1; xx++) {
                Point pp(p.x + xx, p.y + yy);
                if (pp.x < 0 || pp.x >= bw.size().width ||
                        pp.y < 0 || pp.y >= bw.size().height)
                    continue;
                if (bw(pp))
                    open.push_back(pp);
            }
        }
    }
    return comp;
}

static void findContiguousComponents(Mat1b bw, Components &c)
{
    bw = bw.clone();
    std::vector<Point> comp;

    for (int y = 0; y < bw.size().height; y++) {
        for (int x = 0; x < bw.size().width; x++) {
            Point point(x, y);
            comp = findContigComponentAt(bw, point);
            if (comp.size() > 0)
                c.push_back(comp);
        }
    }
}

static Ellipse findEllipseRansac(const Mat1b &bw, std::vector<Point> &points, const ImgProcParameters &param)
{
    Ellipse best = Ellipse::invalid();
    std::vector<Point> p(5);
    unsigned iterations = param.iterations;
    unsigned timeout = 10*iterations;

    while (iterations > 0 && (--timeout > 0)
           // Random selection would be slow if we have just idx.size()
           // points so we require 2x more.
           && points.size() >= 2*p.size()
           ) {

        for (unsigned i = 0; i < p.size(); i++) {
            bool dup;
            do {
                dup = false;
                p[i] = points[rand() % points.size()];

                for (unsigned j = 0; j < i; j++) {
                    if (p[i] == p[j]) {
                        dup = true;
                        break;
                    }
                }
            } while (dup);
        }

        Ellipse e = Ellipse::fit(p);
        if (!isNiceEllipse(e, param))
            continue; // Too small for a wheel or invalid

        std::vector<Point> pointsOnEllipse;
        e.calcWeight(bw, nullptr, &pointsOnEllipse);

        for (int i = 0; i < 2; i++) {
#ifdef DEBUG
            if (0 && e >= best && i > 0) {
                // Visualize RANSAC progress
                Mat3b vis = bw;
                for (auto p : points)
                    vis.at<Vec3b>(p) = Vec3b(0, 0, 255);
                try {
                    ellipse(vis, e, CV_RGB(0, 0, 255));
                    ellipse(vis, best, CV_RGB(255, 0, 255));
                }
                catch (cv::Exception) {}
                imshow("RANSAC", vis);
                try {
                    displayStatusBar("RANSAC", "Iter: "+std::to_string(i));
                } catch (...) {}
                if ((waitKey(0) & 0xff) == 'q')
                    exit(0);
            }
#endif
            Ellipse e2 = Ellipse::fit(pointsOnEllipse);
            if (!isNiceEllipse(e2, param))
                break;
            e2.calcWeight(bw, nullptr, &pointsOnEllipse);
            if (e2 > e) {
                e = e2;
            } else
                break;

        }
        if (best < e)
            best = e;

#ifdef DEBUG
        if (0) {
            Mat3b vis = bw;
            for (auto p : points)
                vis(p) = Vec3b(0, 0, 255);
            try {
                ellipse(vis, e, CV_RGB(0, 0, 255));
                ellipse(vis, best, CV_RGB(255, 0, 255));
            }
            catch (cv::Exception) {}
            imshow("RANSAC", vis);
            if ((waitKey(1) & 0xff) == 'q')
                exit(0);
        }
#endif

        iterations--;
    }

    return best;
}

static void findEllipses(const Mat1b &bw, std::vector<Ellipse> &ellipses_out, Mat3b *vis, const ImgProcParameters &param)
{
    int w = bw.size().width;

    Components comp;
    findContiguousComponents(bw, comp);

    std::priority_queue<Ellipse> ellipses;

    for (auto p: comp) {
        if (p.size() < 30)
            continue;

        // First try fast (but inaccurate) fit
        Ellipse e = Ellipse::fit(p);

        if (e.isInvalid())
            continue;

#if DEBUG
        Mat3b vis = bw;
        for (auto pp : p)
            vis.at<Vec3b>(pp) = Vec3b(0, 0, 255);
        try {
            Scalar color = isNiceEllipse(e, param) ? CV_RGB(0, 255, 0) : CV_RGB(0, 0, 255);
            ellipse(vis, e, color);
        }
        catch (cv::Exception) {}
        imshow("Component", vis);
        if (waitKey(0) == 'q')
            exit(0);
#endif

        if (!isNiceEllipse(e, param))
            continue;

        e.calcWeight(bw);

        // If it looks like a good candidate for ellipse, try harder
        Ellipse er = findEllipseRansac(bw, p, param);
        if (!er.isInvalid() && er > e)
            e = er;

        ellipses.push(e);
    }

    for (int i = 0; i < 5 && ellipses.size() > 0; i++) {
        Ellipse e = ellipses.top();
        ellipses.pop();
        if (e.getWeight() > 0.2) {
            // Return ellipses scaled to image width
            ellipses_out.push_back(e * (1.0/w));
            if (vis)
                try {
                    Scalar col = CV_RGB(0, 180, 0);
                    ellipse(*vis, e, col);
                    circle(*vis, e.f1, 2, col);
                    circle(*vis, e.f2, 2, col);
                    circle(*vis, e.center, 3, col);
                } catch (cv::Exception&) {}
        }
    }
}


void calcGradient(Mat1b image_gray, Mat1b &grad)
{
    Mat1s grad_x, grad_y;
    Mat1b abs_grad_x, abs_grad_y;
    {
        int ddepth = CV_16S;
        int scale = 1;
        int delta = 0;

        /// Gradient X
        Sobel( image_gray, grad_x, ddepth, 1, 0, 3, scale, delta, BORDER_DEFAULT );
        convertScaleAbs( grad_x, abs_grad_x );

        /// Gradient Y
        Sobel( image_gray, grad_y, ddepth, 0, 1, 3, scale, delta, BORDER_DEFAULT );
        convertScaleAbs( grad_y, abs_grad_y );
    }

    /// Total Gradient (approximate)
    addWeighted( abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad );
    normalize(grad, grad, 0, 255, NORM_MINMAX);
}


void processImage(Mat &image, MatPhases &phases, std::vector<Ellipse> &ellipses, const ImgProcParameters &param)
{
    Mat image_small;
    Mat1b image_blur, image_gray, bw;

    ellipses.clear();

    phases[MatPhases::ORIG] = image;

    double scale;
    Size s = image.size(), fit(800, 600);
    if (s.width > s.height)
        scale = std::min((double)fit.width/s.width, (double)fit.height/s.height);
    else
        scale = std::min((double)fit.height/s.width, (double)fit.width/s.height);
    s.width *= scale;
    s.height *= scale;

    resize(image, image_small, s);
    phases[MatPhases::RESIZE] = image_small;

    // Convert it to gray
#if (CV_VERSION_MAJOR >= 4)
    cvtColor(image_small, image_gray, cv::COLOR_BGR2GRAY);
#else
    cvtColor(image_small, image_gray, CV_BGR2GRAY);
#endif
    phases[MatPhases::GRAY] = image_gray;

    GaussianBlur( image_gray, image_blur, Size(15,15), 0, 0, BORDER_DEFAULT );
    phases[MatPhases::BLUR] = image_blur;

    Mat1b grad;
    calcGradient(image_blur, grad);
    phases[MatPhases::GRAD] = grad;

    Canny(image_blur, bw, 10, 40);
    phases[MatPhases::BW] = bw;


    Mat3b visualization;

    visualization = image_small * 0.5;
    findEllipses(bw, ellipses, &visualization, param);
    phases[MatPhases::VIS_EL] = visualization.clone();

//#warning TODO Measure and print execution time of this function
}

Mat debugOnPixelEllipse(Mat1b &bw, Point p, const ImgProcParameters &param, Ellipse &e)
{
    Mat1b tmp;
    Mat3b vis;
    std::vector<Point> component;

    component = findContigComponentAt(tmp = bw.clone(), p);

    if (component.size() == 0)
        return vis;

#if (CV_VERSION_MAJOR >= 4)
    cvtColor(bw, vis, cv::COLOR_GRAY2RGB);
#else
    cvtColor(bw, vis, CV_GRAY2RGB);
#endif
    vis /= 2;

    for (auto p : component)
        vis(p) = Vec3b(0, 0, 255);

    Ellipse er = findEllipseRansac(bw, component, param);
    Ellipse ef = Ellipse::fit(component);
    if (!ef.isInvalid())
        ellipse(vis, ef, CV_RGB(0, 0, 255));
    if (!er.isInvalid()) {
        //ellipse(vis, er, CV_RGB(0, 128, 0));
        er.calcWeight(bw, &vis);
        e = er;
    }

    return vis;
}
