/*
 * Copyright (C) 2017 Michal Sojka <sojkam1@fel.cvut.cz>
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

#include "ellipse.h"
#include "table3x3.hpp"

using namespace cv;

static double acot_d(double val)
{
    double acot = atan(1/val);

    return acot*180/M_PI;
}

static double distance(const Point2d &p1, const Point2d &p2)
{
    Point2d p(p2.x - p1.x, p2.y - p1.y);
    return sqrt(p.x * p.x + p.y * p.y);
}


Ellipse Ellipse::fit(const std::vector<Point> &p)
{
    Mat_<double> A(p.size(), 5);

    if (p.size() < 5)
        return Ellipse::invalid();

    for (size_t i = 0; i < p.size(); i++) {
        A[i][0] = p[i].x*p[i].x;
        A[i][1] = 2*p[i].x*p[i].y;
        A[i][2] = p[i].y*p[i].y;
        A[i][3] = 2*p[i].x;
        A[i][4] = 2*p[i].y;
    }
    Mat B = -1 * Mat::ones(p.size(), 1, CV_64F);

    double a, b, c, d, f, g;
    Mat x;
    solve(A, B, x, DECOMP_SVD);
    a = x.at<double>(0);
    b = x.at<double>(1);
    c = x.at<double>(2);
    d = x.at<double>(3);
    f = x.at<double>(4);
    g = 1;

    Mat tmp = (Mat_<double>(3,3) <<
               a, b, d,
               b, c, f,
               d, f, g);
    double Delta = determinant(tmp);
    double J = a * c - b * b;
    double I = a + c;

    if (Delta == 0 || J <= 0 || Delta/I >= 0)
        return Ellipse::invalid(); // This is not an ellipse, but another conic curve
    else
        return Ellipse(a, b, c, d, f, g);
}

Ellipse::Ellipse(double a, double b, double c, double d, double f, double g)
{
    center.x = (c * d - b * f)/(b * b - a * c);
    center.y = (a * f - b * d)/(b * b - a * c);

    size.width  = 2*sqrt( (2*(a*f*f+c*d*d+g*b*b-2*b*d*f-a*c*g))/((b*b-a*c)*(+sqrt((a-c)*(a-c)+4*b*b)-(a+c))));
    size.height = 2*sqrt( (2*(a*f*f+c*d*d+g*b*b-2*b*d*f-a*c*g))/((b*b-a*c)*(-sqrt((a-c)*(a-c)+4*b*b)-(a+c))));

    angle=0;
    if (b == 0 && a < c) {
        angle = 0;
    }
    else if (b == 0 && a > c) {
        angle = 90;
    }
    else if (b != 0 && a < c) {
        angle = 0.5 * acot_d( (a-c)/(2*b) );
    }
    else if (b != 0 && a > c) {
        angle = 90 + 0.5 * acot_d( (a-c)/(2*b) );
    }
    if (size.height > size.width) {
        double temp = size.width;
        size.width = size.height;
        size.height = temp;
        angle += 90;
    }

    double temp_c = sqrt(size.width/2 * size.width/2 - size.height/2 * size.height/2);
    f1.x = center.x - temp_c * cos(angle*M_PI/180);
    f1.y = center.y - temp_c * sin(angle*M_PI/180);
    f2.x = center.x + temp_c * cos(angle*M_PI/180);
    f2.y = center.y + temp_c * sin(angle*M_PI/180);

    float s = sin(angle/180.0*M_PI);
    float h = cos(angle/180.0*M_PI);

    rot = cv::Matx22f(h, -s, s, h);
}

Ellipse::Ellipse(Point2f center, Size2f size, float angle)
    : cv::RotatedRect(center, size, angle)
{}

float precomp = 180.0*M_PI;

Point Ellipse::pointAtAngle(float a) const
{
    //double s = sin(angle/180.0*M_PI);
    //double c = cos(angle/180.0*M_PI);

    //printf("angle: %f", angle);

    Vec2f v(cosf(a/180.0*M_PI) * size.width/2.0,
            sinf(a/180.0*M_PI) * size.height/2.0);
    v = rot * v;

    /*if (normal) {
        Vec2f &n = *normal;
        n = Vec2f(+cos(a/180.0*M_PI) * size.height/2.0, +sin(a/180.0*M_PI) * size.width/2.0);
        n = rot * n;
    }*/
    return Point(center.x + v[0], center.y + v[1]);
}

bool Ellipse::hasPoint(Point &p) const
{
    double d1 = distance(f1, p);
    double d2 = distance(f2, p);

    return  (d1 + d2) < size.width + 1 && (d1 + d2) > size.width - 1;
}

static bool isEdgeAtPoint(const Mat1b &bw, const Point p, Point &realP)
{
    unsigned idx = 0;

    // Look at all pixels around p and lookup their center of gravity
    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {
            idx = (idx << 1) | (bw(p.y + y, p.x + x) != 0);
        }
    }
    realP = Point(p.x + centerOfGravity3x3[idx].dx,
                  p.y + centerOfGravity3x3[idx].dy);
//#warning TODO Return true only if the angle of the detected line is similar to the angle of ellipse edge
    return idx != 0;
}

void Ellipse::calcWeight(const Mat1b &bw, Mat3b *dbg, std::vector<Point> *pointsOnEllipse)
{

    unsigned ptsOnEllipse = 0;
    unsigned ptsInRoi = 0;
    Rect roi(Point(1, 1), bw.size() - Size(2, 2));

    for (int a = 0; a < nWeightPoints; a++) {
        //Vec2f normal;
        Point p = pointAtAngle(a);
        //normal /= cv::norm(normal);
        bool pOnEllipse = false;
        if (roi.contains(p)) {
            ptsInRoi++;

            Point realP;
            pOnEllipse = isEdgeAtPoint(bw, p, realP);
            ptsOnEllipse += pOnEllipse;
            if (pointsOnEllipse && pOnEllipse)
                pointsOnEllipse->push_back(realP);
            if (dbg) {
                //normal = normal / cv::norm(normal) * 10.0;
                //line(*dbg, p, p+Point(20*cos(tangentAng), 20*sin(tangentAng)), CV_RGB(255, 255, 0));
                if (pOnEllipse)
                    (*dbg)(realP) = Vec3b(0, 255, 255);
                (*dbg)(p) = pOnEllipse ? Vec3b(0, 255, 0) : Vec3b(200, 0, 100);
            }
        }
    }

    if (ptsInRoi >= nWeightPoints * 30 / 100)
        weight = static_cast<float>(ptsOnEllipse) / ptsInRoi;
    else
        weight = 0;
}

