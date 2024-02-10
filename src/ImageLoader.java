import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

public class ImageLoader {
    private Mat imageMat;
    private final VideoCapture capture;

    public ImageLoader() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        capture = new VideoCapture(0);
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