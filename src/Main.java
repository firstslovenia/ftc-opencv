import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    private static Slider luminositySlider;

    private static ArrayList<ColorPreset> colorPresets = new ArrayList<>();
    private static Image[] images = new Image[5];

    private static final int ROW_COUNT = 15, COL_COUNT = 20;
    private static int LUM_THRESHOLD = 100;
    private static Mat[][] imageSegments = new Mat[ROW_COUNT][COL_COUNT];
    private static boolean[][] validImageSegments = new boolean[ROW_COUNT][COL_COUNT];

    final static int[] displayImageIndex = {0};

    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        colorPresets.add(new ColorPreset("New Preset", 0, 255, 0, 255, 0, 255));
        colorPresets.add(new ColorPreset("Luminosity Blue", 0, 255, 0, 255, 0, 116));
        colorPresets.add(new ColorPreset("White", 144, 255, 162, 255, 149, 255));
        colorPresets.add(new ColorPreset("Purple", 75, 207, 0, 153, 25, 175));
        colorPresets.add(new ColorPreset("Blue Cub", 156, 255, 83, 220, 29, 147));
        colorPresets.add(new ColorPreset("Blue Cub 2", 190, 255, 171, 255, 96, 177));
        colorPresets.add(new ColorPreset("Blue Cub 3 (Works)", 155, 255, 80, 255, 0, 157));
        colorPresets.add(new ColorPreset("Blue Cub 4 (Works)", 155, 255, 80, 255, 0, 156));
        colorPresets.add(new ColorPreset("Blu Cub Works 100% HSV ", 4, 40, 75, 194, 141, 255));
        colorPresets.add(new ColorPreset("Red Cub Works 101% HSV", 101, 138, 108, 255, 102, 255));
        colorPresets.add(new ColorPreset("Green Blob HSV", 38, 82, 43, 169, 106, 255));

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
        luminositySlider = new Slider("Luminosity threshold", 0, 255, 120);

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
                displayImageIndex[0]++;
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
            // WASTEFUL FOR SETTING CONSTANT
            LUM_THRESHOLD = luminositySlider.getSliderValue();
            imageMat = imageLoader.getCameraImage();
            updateImage();
        }
    }

    public static void updateImage() {
        int imageIndex = 0;
        Mat imageMatColorspace = new Mat();
        Imgproc.cvtColor(imageMat, imageMatColorspace, Imgproc.COLOR_RGB2YCrCb); // convert rgb to chroma and luminosity
        // Imgproc.cvtColor(imageMat, imageMatColorspace, Imgproc.COLOR_RGB2HSV);

        Mat imageMatLuminosity = new Mat();
        Core.extractChannel(imageMatColorspace, imageMatLuminosity, 2);

        Mat imageMatColorFilter = new Mat();
        Core.inRange(imageMatColorspace, new Scalar(colorSliderMinR.getSliderValue(),colorSliderMinG.getSliderValue(), colorSliderMinB.getSliderValue()), new Scalar(colorSliderMaxR.getSliderValue(),colorSliderMaxG.getSliderValue(), colorSliderMaxB.getSliderValue()), imageMatColorFilter);

        Mat imageMatFiltered = new Mat();
        Core.bitwise_and(imageMat, imageMat, imageMatFiltered, imageMatColorFilter);

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

        int[] zones = new int[2];

        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                int topLeftX = col * (imageMat.width() / COL_COUNT);
                int topLeftY = row * (imageMat.height() / ROW_COUNT);

                int botRightX = (col + 1) * (imageMat.width() / COL_COUNT);
                int botRightY = (row + 1) * (imageMat.height() / ROW_COUNT);

                Point p1 = new Point(topLeftX, topLeftY);
                Point p2 = new Point(botRightX, botRightY);
                if (checkSegment(row, col, imageMatLuminosity)) {
                    Imgproc.rectangle(imageMatFinal, p1, p2, new Scalar(0, 0, (validImageSegments[row][col]) ? 255 : 0), 2);
                    Imgproc.rectangle(imageMatLuminosity, p1, p2, new Scalar(0, 0,(validImageSegments[row][col]) ? 255 : 0), 2);
                    zones[((topLeftX + botRightX) / 2 < imageMat.width() / 2)?0:1]++;
                }
            }
        }

        int pos = 0;
        if (Math.min(zones[0] + 1, zones[1] + 1) > 1) {
            pos = (zones[0] / zones[1] > 1)?1:2;
        }

        String zoneData = "Ratio  M " + Integer.toString(zones[0]) + "  :  R " + Integer.toString(zones[1]) +
                "  Zone" + Integer.toString(pos);
        Imgproc.putText(imageMatFinal, zoneData, new Point(100, 100)  ,Imgproc.FONT_ITALIC, 1, new Scalar(0, 0, 255),2);

        Image imageFinal = HighGui.toBufferedImage(imageMatFinal);

        images[imageIndex++] = HighGui.toBufferedImage(imageMatColorspace);;
        images[imageIndex++] = HighGui.toBufferedImage(imageMatLuminosity);;
        images[imageIndex++] = HighGui.toBufferedImage(imageMatColorFilter);
        images[imageIndex++] = HighGui.toBufferedImage(imageMatFiltered);
        images[imageIndex] = imageFinal;
        imageLabel.setIcon(new ImageIcon(images[displayImageIndex[0]%images.length]));
        frame.repaint();
    }

    public static boolean checkSegment (int row, int col, Mat imageMat) {
        int topLeftX = col * (imageMat.width() / COL_COUNT);
        int topLeftY = row * (imageMat.height() / ROW_COUNT);

        int botRightX = (col + 1) * (imageMat.width() / COL_COUNT);
        int botRightY = (row + 1) * (imageMat.height() / ROW_COUNT);

        Point p1 = new Point(topLeftX, topLeftY);
        Point p2 = new Point(botRightX, botRightY);

        imageSegments[row][col] = imageMat.submat(new Rect(p1, p2));

        validImageSegments[row][col] = Core.mean(imageSegments[row][col]).val[0] < LUM_THRESHOLD;

        return validImageSegments[row][col];
    }
}