import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image = Imgcodecs.imread("images/pixels.jpg");

        // Image to grayscale
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Canny edge
        Mat edgesImage = new Mat();
        Imgproc.Canny(grayImage, edgesImage, 100, 200);

        // Find contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edgesImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Approximate shapes
        for (MatOfPoint contour : contours) {
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
            double contourLength = Imgproc.arcLength(contour2f, true);
            Imgproc.approxPolyDP(contour2f, approxCurve, 0.02 * contourLength, true);

            // Filter based on vertex count
            if (approxCurve.total() == 6) {
                // Draw overlay
                Point[] points = approxCurve.toArray();
                for (int i = 0; i < points.length; i++) {
                    Imgproc.line(image, points[i], points[(i + 1) % points.length], new Scalar(0, 255, 0), 3);
                }
            }
        }

        // Display images
        Mat resizedImage = new Mat();
        Size newSize = new Size(image.width() * 3, image.height() * 3);
        Imgproc.resize(image, image, newSize);
        Imgproc.resize(grayImage, grayImage, newSize);
        Imgproc.resize(edgesImage, edgesImage, newSize);

        System.out.println("New image size: " + newSize.width + "x" + newSize.height);

        HighGui.imshow("FTC Pixel OpenCV", image);
        HighGui.imshow("FTC Pixel OpenCV - Grayscale", grayImage);
        HighGui.imshow("FTC Pixel OpenCV - Edges", edgesImage);

        HighGui.waitKey();
    }
}
