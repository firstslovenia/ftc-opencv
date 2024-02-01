import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Image {
    private final String imagePath;
    private Mat image;
    private Mat modifiedImage;

    public Image(String imagePath) {
        this.imagePath = imagePath;
        image = new Mat();
    }

    public void loadImage () {
        image = Imgcodecs.imread(imagePath);
        modifiedImage = Imgcodecs.imread(imagePath);
    }

    public void displayImage() {
        HighGui.imshow("Image: " + imagePath, image);
    }

    public void displayModifiedImage() {
        HighGui.imshow("Modified Image: " + imagePath, modifiedImage);
    }

    public void resizeImage(int xScale, int yScale) {
        Size newSize = new Size(image.width() * xScale, image.height() * yScale);
        Imgproc.resize(image, image, newSize);
    }

    public Mat getImage() {
        return image;
    }

    public Mat getModifiedImage() {
        return modifiedImage;
    }
}