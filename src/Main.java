import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static JFrame frame;
    public static Box sliderBox;

    private static Mat imageMat;
    private static JLabel imageLabel;

    private static Slider colorSliderMinR;
    private static Slider colorSliderMaxR;
    private static Slider colorSliderMinG;
    private static Slider colorSliderMaxG;
    private static Slider colorSliderMinB;
    private static Slider colorSliderMaxB;

    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        imageMat = loadImage("images/pixels.jpg");


        frame = new JFrame("Linear Blend");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sliderBox = new Box(BoxLayout.Y_AXIS);

        Image img = HighGui.toBufferedImage(imageMat);
        imageLabel = new JLabel(new ImageIcon(img));
        frame.getContentPane().add(imageLabel, BorderLayout.CENTER);
        frame.getContentPane().add(sliderBox, BorderLayout.NORTH);

        colorSliderMinR = new Slider("Color Slider R min", 0, 255, 100);
        colorSliderMaxR = new Slider("Color Slider R max", 0, 255, 100);
        colorSliderMinG = new Slider("Color Slider G min", 0, 255, 100);
        colorSliderMaxG = new Slider("Color Slider G max", 0, 255, 100);
        colorSliderMinB = new Slider("Color Slider B min", 0, 255, 100);
        colorSliderMaxB = new Slider("Color Slider B max", 0, 255, 100);

        frame.pack();
        frame.setVisible(true);
    }

    private static Mat loadImage(String path) {
        Mat img = Imgcodecs.imread(path);

        if (img.empty()) {
            System.out.println("Empty image: " + path);
            System.exit(0);
        }

        System.out.println(img.width() + "x" + img.height());
        return img;
    }

    public static void updateImage() {
        double alpha = (double) colorSliderMinR.getSliderValue() / colorSliderMinR.getMaxValue();

        Mat tempImageMat = new Mat();

        //Core.addWeighted(imageMat, alpha, imageMat, alpha, 0, tempImageMat);
        Core.inRange(imageMat, new Scalar(colorSliderMinR.getSliderValue(),colorSliderMinG.getSliderValue(), colorSliderMinB.getSliderValue()), new Scalar(colorSliderMaxR.getSliderValue(),colorSliderMaxG.getSliderValue(), colorSliderMaxB.getSliderValue()), tempImageMat);
        Image img = HighGui.toBufferedImage(tempImageMat);
        imageLabel.setIcon(new ImageIcon(img));
        frame.repaint();
    }
}