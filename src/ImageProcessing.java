import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ImageProcessing {
    public static void approximateShape(Image image) {
        // Find contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(image.getModifiedImage(), contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
            double contourLength = Imgproc.arcLength(contour2f, true);
            Imgproc.approxPolyDP(contour2f, approxCurve, 0.02 * contourLength, true);

            if (approxCurve.total() > 4 && approxCurve.total() < 10) {
                Point[] points = approxCurve.toArray();
                for (int i = 0; i < points.length; i++) {
                    Imgproc.line(image.getImage(), points[i], points[(i + 1) % points.length], new Scalar(0, 255, 0), 3);
                }
            }
        }
    }
}
