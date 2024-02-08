import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Main {
    private static final int COLOR_MAX = 255;
    private static final int COLOR_THRESHOLD_MAX = 255;
    private static final int zeroColorArray[] = {0, 0, 0};
    private static AtomicIntegerArray colorThreshold = new AtomicIntegerArray(zeroColorArray);


    private static final String imagePath = "images/pixels.jpg";
    private static Mat image;
    private static JFrame frame;
    private static JLabel imageLabel;
    private static int anInt;

    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        image = loadImage(imagePath);
        showImage(image);
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

    private static void showImage(Mat imageMat) {
        frame = new JFrame("Linear Blend");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image img = HighGui.toBufferedImage(imageMat);
        addComponentsToPane(frame.getContentPane(), img, "Colors", new AtomicInteger(colorThreshold.get(0)));

        frame.pack();
        frame.setVisible(true);
    }

    private static void updateImage() {
        double alpha = colorThreshold.get(0) / (double) COLOR_THRESHOLD_MAX;
        System.out.println(colorThreshold.get(0));
        Mat imageMat = new Mat();
        Core.addWeighted(image, alpha, image, alpha, 0, imageMat);
        Image img = HighGui.toBufferedImage(imageMat);
        imageLabel.setIcon(new ImageIcon(img));
        frame.repaint();
    }

    private static void addComponentsToPane(Container pane, Image img, String title, AtomicInteger toModify) {
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }

        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
        sliderPanel.add(new JLabel(title)); // Use the title

        // Initialize the slider with the initial value of colorThreshold
        JSlider slider = new JSlider(0, COLOR_THRESHOLD_MAX, toModify.intValue());
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        AtomicInteger temp = new AtomicInteger(0);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                System.out.println("Slider " + source.getValue());
                toModify.set(source.getValue());
                System.out.println("Temp " + toModify.intValue());
                updateImage();
            }
        });

        sliderPanel.add(slider);
        pane.add(sliderPanel, BorderLayout.PAGE_START);
        imageLabel = new JLabel(new ImageIcon(img));
        pane.add(imageLabel, BorderLayout.CENTER);
    }
}