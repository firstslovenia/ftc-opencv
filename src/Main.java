import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class Main {
    public static JFrame frame;
    public static Box sliderBox;
    public static Box presetBox;

    public static Mat imageMat;
    private static final ImageLoader imageLoader = new ImageLoader();//"images/pixels.jpg");
    private static JLabel imageLabel;

    private static Slider colorSliderMinR;
    private static Slider colorSliderMaxR;
    private static Slider colorSliderMinG;
    private static Slider colorSliderMaxG;
    private static Slider colorSliderMinB;
    private static Slider colorSliderMaxB;

    private static ArrayList<ColorPreset> colorPresets = new ArrayList<>();
    private static Image[] images = new Image[4];

    final static int[] imageIndex = {0};

    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        colorPresets.add(new ColorPreset("New Preset", 0, 255, 0, 255, 0, 255));
        colorPresets.add(new ColorPreset("White", 144, 255, 162, 255, 149, 255));
        colorPresets.add(new ColorPreset("Purple", 75, 207, 0, 153, 25, 175));
        colorPresets.add(new ColorPreset("Blue Cub", 156, 255, 83, 220, 29, 147));
        colorPresets.add(new ColorPreset("Blue Cub 2", 190, 255, 171, 255, 96, 177));
        colorPresets.add(new ColorPreset("Blue Cub 3 (Works)", 155, 255, 80, 255, 0, 157));
        colorPresets.add(new ColorPreset("Blue Cub 4 (Works)", 155, 255, 80, 255, 0, 156));
        colorPresets.add(new ColorPreset("Blu Cub Works 100% HSV ", 4, 40, 75, 194, 141, 255));
        colorPresets.add(new ColorPreset("Red Cub Works 101% HSV", 101, 138, 108, 255, 102, 255));
        colorPresets.add(new ColorPreset("Gren Blob HSV", 38, 82, 43, 169, 106, 255));

        frame = new JFrame("Linear Blend");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sliderBox = new Box(BoxLayout.Y_AXIS);
        presetBox = new Box(BoxLayout.Y_AXIS);

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

        JButton cycleColorButton = new JButton("Cycle color preset");
        cycleColorButton.setBounds(0,0,100,30);
        sliderBox.add(cycleColorButton);

        JButton exportButton = new JButton("Print settings");
        exportButton.setBounds(0,0,100,30);
        sliderBox.add(exportButton);

        final JTextField presetName = new JTextField();
        presetName.setBounds(50,50, 150,20);
        sliderBox.add(presetName);

        JButton gumbButton = new JButton("Gumb button");
        gumbButton.setBounds(0,0,100,30);
        sliderBox.add(gumbButton);

        final int[] colorPresetIndex = {-1};
        cycleColorButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                colorPresetIndex[0]++;
                cycleColorButton.setText(String.valueOf(colorPresetIndex[0] % colorPresets.size()));
                System.out.println("Preset " + colorPresets.get(colorPresetIndex[0]%colorPresets.size()).name + " (" + (colorPresetIndex[0] % colorPresets.size()) + ") active");
                colorSliderMinR.setSliderValue(colorPresets.get(colorPresetIndex[0]%colorPresets.size()).colorValues.get(0).get(0));
                colorSliderMaxR.setSliderValue(colorPresets.get(colorPresetIndex[0]%colorPresets.size()).colorValues.get(0).get(1));
                colorSliderMinG.setSliderValue(colorPresets.get(colorPresetIndex[0]%colorPresets.size()).colorValues.get(1).get(0));
                colorSliderMaxG.setSliderValue(colorPresets.get(colorPresetIndex[0]%colorPresets.size()).colorValues.get(1).get(1));
                colorSliderMinB.setSliderValue(colorPresets.get(colorPresetIndex[0]%colorPresets.size()).colorValues.get(2).get(0));
                colorSliderMaxB.setSliderValue(colorPresets.get(colorPresetIndex[0]%colorPresets.size()).colorValues.get(2).get(1));

                System.out.println("R value: " + colorSliderMinR.getSliderValue() + " - " + colorSliderMaxR.getSliderValue());
                System.out.println("G value: " + colorSliderMinG.getSliderValue() + " - " + colorSliderMaxG.getSliderValue());
                System.out.println("B value: " + colorSliderMinB.getSliderValue() + " - " + colorSliderMaxB.getSliderValue());

                presetName.setText(colorPresets.get(colorPresetIndex[0]%colorPresets.size()).name);
            }
        });

        gumbButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                imageIndex[0]++;
            }
        });

        exportButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                colorPresets.add(new ColorPreset(presetName.getText(), colorSliderMinR.getSliderValue(), colorSliderMaxR.getSliderValue(), colorSliderMinG.getSliderValue(), colorSliderMaxG.getSliderValue(), colorSliderMinB.getSliderValue(), colorSliderMaxB.getSliderValue()));
                System.out.println("colorPresets.add(new ColorPreset(\"" + presetName.getText() + "\", " + colorSliderMinR.getSliderValue() + ", " +  colorSliderMaxR.getSliderValue() + ", " +  colorSliderMinG.getSliderValue() + ", " +  colorSliderMaxG.getSliderValue() + ", " + colorSliderMinB.getSliderValue() + ", " + colorSliderMaxB.getSliderValue() + "));");
            }
        });

        frame.pack();
        frame.setVisible(true);

        while (true) {
            imageMat = imageLoader.getCameraImage();
            updateImage();
        }
    }

    public static void updateImage() {
        int i = 0;
        Mat imageMatHsv = new Mat();
        Imgproc.cvtColor(imageMat, imageMatHsv, Imgproc.COLOR_RGB2HSV);
        images[i++] = HighGui.toBufferedImage(imageMatHsv);;

        Mat imageMatColorFilter = new Mat();
        Core.inRange(imageMatHsv, new Scalar(colorSliderMinR.getSliderValue(),colorSliderMinG.getSliderValue(), colorSliderMinB.getSliderValue()), new Scalar(colorSliderMaxR.getSliderValue(),colorSliderMaxG.getSliderValue(), colorSliderMaxB.getSliderValue()), imageMatColorFilter);
        images[i++] = HighGui.toBufferedImage(imageMatColorFilter);

        Mat imageMatFiltered = new Mat();
        Core.bitwise_and(imageMat, imageMat, imageMatFiltered, imageMatColorFilter);
        images[i++] = HighGui.toBufferedImage(imageMatFiltered);

        Mat imageMatFinal = imageMatFiltered;

        // Hexagon detection
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(imageMatColorFilter, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            double contourArea = Imgproc.contourArea(contour);
            if (contourArea > 1500) {
                //System.out.println("Contour with area " + contourArea + " detected.");
                Imgproc.drawContours(imageMatFiltered, contours, contours.indexOf(contour), new Scalar(0, 255, 0), 2);
            }
        }

        Image imageFinal = HighGui.toBufferedImage(imageMatFinal);
        images[i++] = imageFinal;
        imageLabel.setIcon(new ImageIcon(images[imageIndex[0]%images.length]));
        frame.repaint();
    }
}