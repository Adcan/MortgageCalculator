package lecho.lib.hellocharts.model;

import java.util.Arrays;

import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

/**
 * Model representing single slice on PieChart.
 */
public class SliceValue {
    private static final int DEFAULT_SLICE_SPACING_DP = 50;
    @Deprecated
    /** Spacing between this slice and its neighbors. */
    private int sliceSpacing = DEFAULT_SLICE_SPACING_DP;
    /**
     * Current value of this slice.
     */
    private double value;
    /**
     * Origin value of this slice, used during value animation.
     */
    private double originValue;
    /**
     * Difference between originValue and targetValue.
     */
    private double diff;
    /**
     * Color of this slice.
     */
    private int color = ChartUtils.DEFAULT_COLOR;
    /**
     * Darken color used to draw label background and give touch feedback.
     */
    private int darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
    /**
     * Custom label for this slice, if not set number formatting will be used.
     */
    private char[] label;

    public SliceValue() {
        setValue(0);
    }

    public SliceValue(double value) {
        setValue(value);
    }

    public SliceValue(double value, int color) {
        setValue(value);
        setColor(color);
    }

    public SliceValue(double value, int color, int sliceSpacing) {
        setValue(value);
        setColor(color);
        this.sliceSpacing = sliceSpacing;
    }

    public SliceValue(SliceValue sliceValue) {
        setValue(sliceValue.value);
        setColor(sliceValue.color);
        this.sliceSpacing = sliceValue.sliceSpacing;
        this.label = sliceValue.label;
    }

    public void update(double scale) {
        value = originValue + diff * scale;
    }

    public void finish() {
        setValue(originValue + diff);
    }

    public double getValue() {
        return value;
    }

    public lecho.lib.hellocharts.model.SliceValue setValue(double value) {
        this.value = value;
        this.originValue = value;
        this.diff = 0;
        return this;
    }

    /**
     * Set target value that should be reached when data animation finish then call {@link Chart#startDataAnimation()}
     *
     * @param target
     * @return
     */
    public SliceValue setTarget(double target) {
        setValue(value);
        this.diff = target - originValue;
        return this;
    }

    public int getColor() {
        return color;
    }

    public SliceValue setColor(int color) {
        this.color = color;
        this.darkenColor = ChartUtils.darkenColor(color);
        return this;
    }

    public int getDarkenColor() {
        return darkenColor;
    }

    @Deprecated
    public int getSliceSpacing() {
        return sliceSpacing;
    }

    @Deprecated
    public SliceValue setSliceSpacing(int sliceSpacing) {
        this.sliceSpacing = sliceSpacing;
        return this;
    }

    @Deprecated
    public char[] getLabel() {
        return label;
    }

    @Deprecated
    public SliceValue setLabel(char[] label) {
        this.label = label;
        return this;
    }

    public SliceValue setLabel(String label) {
        this.label = label.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return label;
    }

    @Override
    public String toString() {
        return "SliceValue =" + value + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SliceValue that = (SliceValue) o;

        if (color != that.color) return false;
        if (darkenColor != that.darkenColor) return false;
        if (Double.compare(that.diff, diff) != 0) return false;
        if (Double.compare(that.originValue, originValue) != 0) return false;
        if (sliceSpacing != that.sliceSpacing) return false;
        if (Double.compare(that.value, value) != 0) return false;
        if (!Arrays.equals(label, that.label)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        long result = (value != +0.0f ? Double.doubleToLongBits(value) : 0);
        result = 31 * result + (originValue != +0.0f ? Double.doubleToLongBits(originValue) : 0);
        result = 31 * result + (diff != +0.0f ? Double.doubleToLongBits(diff) : 0);
        result = 31 * result + color;
        result = 31 * result + darkenColor;
        result = 31 * result + sliceSpacing;
        result = 31 * result + (label != null ? Arrays.hashCode(label) : 0);
        return (int) result;
    }
}
