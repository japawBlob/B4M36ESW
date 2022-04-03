
#ifndef ELLIPSE_H
#define ELLIPSE_H

#include <opencv2/core/core.hpp>
#include <vector>

using namespace cv;

class Ellipse : public cv::RotatedRect {
public:
    //  ax²+2bxy+cy²+2dx+2fy+g=0
    Ellipse(double a, double b, double c, double d, double f, double g);
    Ellipse(cv::Point2f center, cv::Size2f size, float angle);

    static Ellipse fit(const std::vector<cv::Point> &p);
    static Ellipse invalid() { return Ellipse(); }

    cv::Point pointAtAngle(float a) const;

    cv::Point2f f1, f2;

    cv::Matx22f rot;


    void operator*= (float scale) { center *= scale; size = size * scale; }
    Ellipse operator* (float scale) const { Ellipse e(*this); e *= scale; return e; }

    bool operator< (const Ellipse &other) const { return weight < other.weight; }
    bool operator> (const Ellipse &other) const { return weight > other.weight; }
    bool operator<= (const Ellipse &other) const { return weight <= other.weight; }
    bool operator>= (const Ellipse &other) const { return weight >= other.weight; }

    bool isInvalid() const { return center.x == NAN; }
    bool hasPoint(cv::Point &p) const;
    void calcWeight(const cv::Mat1b &bw, cv::Mat3b *dbg = nullptr, std::vector<cv::Point> *pointsOnEllipse = nullptr);
    float getWeight() const { return weight; }
    std::vector<cv::Point> getPointsOnEllipse(cv::Mat &img) const;




private:
    float weight = 0.0;
    static const int nWeightPoints = 360;

    // Construct invalid ellipse
    Ellipse() { center.x = NAN; }

};




#endif // ELLIPSE_H
