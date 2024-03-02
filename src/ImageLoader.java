import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import static org.opencv.videoio.Videoio.*;

public class ImageLoader {
    private Mat imageMat;
    private VideoCapture capture;

    public ImageLoader() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Laptop has stupid face id cameras so use this to find correct one
        // For default use 0
        int index = 5;
        //capture = new VideoCapture(index + CAP_V4L);
        //capture.set(Videoio.CAP_PROP_AUTO_EXPOSURE, -1000.0);
        /*while (true) {
            capture = new VideoCapture(index + CAP_V4L);
            if (capture.isOpened()) {
                System.out.println("Camera found at index: " + index);
                capture.release();
                break;
            } else {
                System.out.println("No camera found at index: " + index);
            }
            index++;
            if (index > 10) {
                System.out.println("No external camera found. Please check connection.");
                break;
            }
        }*/
        capture = new VideoCapture(index + CAP_V4L);
    }

    public ImageLoader(String imagePath) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        capture = null;
        imageMat = Imgcodecs.imread(imagePath);

        if (imageMat.empty()) {
            System.out.println("Empty image: " + imagePath);
            System.exit(0);
        }

        System.out.println("Loaded image: " + imageMat.width() + "x" + imageMat.height());
    }

    public Mat getCameraImage () {
        if (capture != null) {
            imageMat = new Mat();
            capture.read(imageMat);
        }
        return imageMat;
    }
}