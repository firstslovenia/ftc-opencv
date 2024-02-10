import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static JFrame frame;
    public static Box sliderBox;

    public static Mat imageMat;
    private static final ImageLoader imageLoader = new ImageLoader();//"images/pixels.jpg");
    private static JLabel imageLabel;

    private static Slider colorSliderMinR;
    private static Slider colorSliderMaxR;
    private static Slider colorSliderMinG;
    private static Slider colorSliderMaxG;
    private static Slider colorSliderMinB;
    private static Slider colorSliderMaxB;

    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        frame = new JFrame("Linear Blend");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sliderBox = new Box(BoxLayout.Y_AXIS);

        imageMat = imageLoader.getCameraImage();
        Image img = HighGui.toBufferedImage(imageMat);
        imageLabel = new JLabel(new ImageIcon(img));
        frame.getContentPane().add(imageLabel, BorderLayout.CENTER);
        frame.getContentPane().add(sliderBox, BorderLayout.NORTH);

        colorSliderMinR = new Slider("Color Slider R min", 0, 255, 0);
        colorSliderMaxR = new Slider("Color Slider R max", 0, 255, 255);
        colorSliderMinG = new Slider("Color Slider G min", 0, 255, 0);
        colorSliderMaxG = new Slider("Color Slider G max", 0, 255, 255);
        colorSliderMinB = new Slider("Color Slider B min", 0, 255, 0);
        colorSliderMaxB = new Slider("Color Slider B max", 0, 255, 255);

        frame.pack();
        frame.setVisible(true);

        while (true) {
            imageMat = imageLoader.getCameraImage();
            updateImage();
        }
    }

    public static void updateImage() {
        Mat imageMatHsv = new Mat();
        Imgproc.cvtColor(imageMat, imageMatHsv, Imgproc.COLOR_BGR2HSV);

        Mat imageMatColorFilter = new Mat();
        Core.inRange(imageMat, new Scalar(colorSliderMinR.getSliderValue(),colorSliderMinG.getSliderValue(), colorSliderMinB.getSliderValue()), new Scalar(colorSliderMaxR.getSliderValue(),colorSliderMaxG.getSliderValue(), colorSliderMaxB.getSliderValue()), imageMatColorFilter);

        Mat imageMatFiltered = new Mat();
        Core.bitwise_and(imageMat, imageMat, imageMatFiltered, imageMatColorFilter);

        Mat imageMatFinal = imageMatFiltered;

        // Hexagon detection
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(imageMatColorFilter, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            double contourArea = Imgproc.contourArea(contour);
            if (contourArea > 500) {
                System.out.println("Contour with area " + contourArea + " detected.");
                Imgproc.drawContours(imageMatFiltered, contours, contours.indexOf(contour), new Scalar(0, 255, 0), 2);
            }
        }

        Image img = HighGui.toBufferedImage(imageMatFinal);
        imageLabel.setIcon(new ImageIcon(img));
        frame.repaint();
    }
}