#include <stdio.h>
#include <opencv2/opencv.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/highgui.hpp>
#include <boost/filesystem.hpp>
#include <boost/filesystem/path.hpp>
#include <iostream>
#include <vector>
#include <math.h>
#include <queue>
#include <getopt.h>

#include "ellipse.h"
#include "imgproc.h"

namespace fs = boost::filesystem;

using namespace cv;
using namespace std;

struct ui {
    Mat image;
    MatPhases phases;

    int phaseToView = MatPhases::VIS_EL;

    std::vector<Ellipse> ellipses;
} ui;

ImgProcParameters param;

static void drawResults(Mat &image)
{
    for (Ellipse &e: ui.ellipses) {
        Scalar col = CV_RGB(0, 255, 0);
        try {
            ellipse(image, e * image.size().width, col);
        } catch (cv::Exception&) {}
    }
}

static void printResults(Mat &image)
{
    for (Ellipse &e: ui.ellipses) {
        printf("c: [%f,%f] w: %f h: %f\n",
               e.center.x * image.size().width,
               e.center.y * image.size().width,
               e.size.width * image.size().width,
               e.size.height * image.size().width
               );
    }
}

static void onTrackbar(int, void*)
{
    size_t phase = std::min(static_cast<std::size_t>(ui.phaseToView), ui.phases.size()-1);
    Mat image(ui.phases[phase]);

    if (image.empty())
        image.create(100, 100, CV_8UC3);

    if (image.channels() == 1) {
#if (CV_VERSION_MAJOR >= 4)
        cvtColor(ui.phases[phase], image, cv::COLOR_GRAY2RGB);
#else
        cvtColor(ui.phases[phase], image, CV_GRAY2RGB);
#endif
    }

    switch (ui.phaseToView) {
    case MatPhases::VIS_EL:
        // Do not draw results
        break;
    default:
        drawResults(image);
    }

    imshow("Image", image);
}

class StringBuilder
{
public:
    template <typename T> inline StringBuilder& operator<<(const T& t) { mStream << t; return * this; }
    inline operator std::string () const { return mStream.str(); }
private:
    std::stringstream mStream;
};

static void onMouse(int event, int x, int y, int, void* )
{
    Mat1b bw = ui.phases[MatPhases::BW];

    switch (ui.phaseToView) {
    default:
        Ellipse e = Ellipse::invalid();
        Mat dbgImg = debugOnPixelEllipse(bw, Point(x, y), param, e);
        if (!dbgImg.empty()) {
            imshow("Image", dbgImg);
            try {
                displayStatusBar("Image", "Ellipse weight: " + to_string(e.getWeight()));
            } catch (cv::Exception&) {}
        }
    }
}

static bool handleKey(int key, int gui_enabled)
{
    if(key<0){
        return false;
    }
    printf("Key: %d %#x %c\n", key, key, key);
    switch (key & 0xff) {
    case '0'...'9':
        ui.phaseToView = (key & 0xff) - '0';
        break;
    case 'q':
        break;
    default:
        return false;
    }
    if (gui_enabled) {
        setTrackbarPos("Phase", "Image", ui.phaseToView);
    }
    return true;
}

static void print_usage()
{
    printf("usage: find_ellipse <camera number>|<image file name>|<stream URL> [<RANSAC iterations>] [no-gui]\n"
           "\n"
           "Examples:\n"
           "  ./find_ellipse 0\n"
           "  ./find_ellipse img.jpg\n"
           "  ./find_ellipse img.jpg 100 no-gui\n"
           "  ./find_ellipse http://192.168.1.1:8080/video  # With IP Webcam Android App\n"
           );
}

int main(int argc, char** argv )
{
    bool gui_enabled = true;
    bool video_input = false;
    VideoCapture videoCap;
    const char *arg = nullptr;
    int camera_num = -1;
    int iterations = 100;

    if (argc == 2 &&
        (string(argv[1]) == "-h" ||
         string(argv[1]) == "--help"))
    {
        print_usage();
        return -1;
    }

    arg = (argc >= 2) ? argv[1] : "0";

    if (argc > 3 && string(argv[3]) == "no-gui")
        gui_enabled = false;

    if (argc > 2) {
      try {
        iterations = stoi(argv[2]);
      } catch (...) {
        if (string(argv[2]) == "no-gui")
          gui_enabled = false;
      }
    }

    param.iterations = iterations;

    try {
        camera_num = stoi(arg);
        gui_enabled = true;
    } catch (...) {}

    srand(time(0));

    int key;

    if (camera_num < 0 && fs::exists(fs::path(arg))) {
        // Static image
#if (CV_VERSION_MAJOR >= 4)
        ui.image = imread( arg, cv::IMREAD_COLOR );
#else
        ui.image = imread( arg, CV_LOAD_IMAGE_COLOR );
#endif
        if (ui.image.data == NULL)
            throw std::runtime_error("Cannot load " + string(arg));

    } else {
        // Video stream
        gui_enabled = true;
        video_input = true;
        if (camera_num >= 0)
            videoCap.open(camera_num); // Local camera
        else
            videoCap.open(arg); // Remote stream
        if (!videoCap.isOpened()) {
            cerr << "Error openning " << ((camera_num >= 0) ? "camera " + to_string(camera_num) : arg) << endl;
            return 1;
        }
    }

    if (gui_enabled) {
#if (CV_VERSION_MAJOR >= 4)
        namedWindow("Image", cv::WindowFlags::WINDOW_NORMAL|cv::WindowFlags::WINDOW_KEEPRATIO|cv::WindowFlags::WINDOW_GUI_EXPANDED);
#else
        namedWindow("Image", CV_WINDOW_NORMAL|CV_WINDOW_KEEPRATIO|CV_GUI_EXPANDED);
#endif
        createTrackbar("Phase", "Image", &ui.phaseToView, MatPhases::__COUNT - 1, onTrackbar);
    }

    if (gui_enabled) {
        setMouseCallback("Image", onMouse);

        if (video_input) {
            for (int i = 0; i < 1; i++)
                videoCap.grab(); // Skip few frames to not lag behind real time
        }
        do {
            if (video_input) {
                videoCap.read(ui.image);
                if (ui.image.empty()) {
                    cerr << "video capture returned an empty image!" << endl;
                    return 1;
                }
            }
            processImage(ui.image, ui.phases, ui.ellipses, param);
            onTrackbar(0, 0); // Draw the result
            fflush(stdout);
            key = cv::waitKey(video_input ? 1 : 0);
            handleKey(key, gui_enabled);
        } while ((key & 0xff) != 'q' &&
                 (key & 0xff) != 27  /* escape */);
    } else {
        processImage(ui.image, ui.phases, ui.ellipses, param);
        printResults(ui.image);
    }
    return 0;
}
