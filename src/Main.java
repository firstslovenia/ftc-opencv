import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Image pixelImage = new Image("images/pixels.jpg");
        pixelImage.loadImage();
        pixelImage.resizeImage(3, 3);
        pixelImage.displayImage();
        pixelImage.displayModifiedImage();

        // Image to grayscale
        Imgproc.cvtColor(pixelImage.getModifiedImage(), pixelImage.getModifiedImage(), Imgproc.COLOR_BGR2GRAY);

        // GaussianBlur
        Imgproc.GaussianBlur(pixelImage.getModifiedImage(), pixelImage.getModifiedImage(), new Size(3, 3), 0, 0);

        // Canny edge
        Imgproc.Canny(pixelImage.getModifiedImage(), pixelImage.getModifiedImage(), 100, 200);

        HighGui.waitKey();
    }
}
