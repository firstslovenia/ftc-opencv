import java.util.ArrayList;
import java.util.Arrays;

public class ColorPreset {
    public String name;
    public ArrayList<ArrayList<Integer>> colorValues = new ArrayList<>(3);

    public ColorPreset (String name, Integer rMin, Integer rMax, Integer gMin, Integer gMax, Integer bMin, Integer bMax) {
        this.name = name;
        colorValues.add(new ArrayList<> (Arrays.asList(rMin, rMax)));
        colorValues.add(new ArrayList<> (Arrays.asList(gMin, gMax)));
        colorValues.add(new ArrayList<> (Arrays.asList(bMin, bMax)));
    }
}