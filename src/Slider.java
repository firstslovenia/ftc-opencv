import javax.swing.*;

public class Slider extends JSlider {
    private final int maxValue;
    private int velju;

    /**
     * Naredi drsnik
     *
     * @param title Ime drsnika
     * @param minValue Minimalna vrednost
     * @param maxValue Maximalna vrednost
     * @param startValue Začetna vrednost
     */
    public Slider (String title, int minValue, int maxValue, int startValue) {
        this.maxValue = maxValue;

        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
        sliderPanel.add(new JLabel(title));

        // Initialize the slider with the initial value of colorThreshold
        JSlider slider = new JSlider(minValue, maxValue, startValue);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            velju = source.getValue();
            Main.updateImage();
        });

        sliderPanel.add(slider);
        Main.sliderBox.add(sliderPanel);
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setSliderValue(int newValue) {
        velju = newValue;
        this.setValue(newValue);
    }

    public int getSliderValue() {
        return velju;
    }
}
