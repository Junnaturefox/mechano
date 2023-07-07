package com.quattage.mechano.core.electricity.node.base;

import java.util.Locale;

import com.simibubi.create.foundation.utility.Color;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;

/***
 * Stores the insertion/extraction capabilities of a specific ElectricNode in a separate object for convenience.
 * Also stores the colors tied to these modes to be accessed by client-side rendering.
 */
public enum NodeMode implements StringRepresentable{
    NONE(false, false, new Color(98, 98, 98), new Color(229, 229, 229)),
    INSERT(true, false, new Color(65, 180, 235), new Color(9, 89, 127)),
    EXTRACT(false, true, new Color(235, 65, 180), new Color(127, 9, 89)),
    BOTH(true, true, new Color(180, 235, 65), new Color(89, 127, 9));

    private final boolean isInput;
    private final boolean isOutput;
    private final Color color;
    private final Color darkColor;

    private NodeMode(boolean isInput, boolean isOutput, Color color, Color darkColor) {
        this.isInput = isInput;
        this.isOutput = isOutput;
        this.color = color;
        this.darkColor = darkColor;
    }

    public String toString() {
        return "Mode: [" + getSerializedName() + "]";
    }

    public boolean isInput() {
        return isInput;
    }


    public boolean isOutput() {
        return isOutput;
    }

    /***
     * Swaps this NodeMode <p>
     * If this NodeMode is of the "NONE" type, using 
     * this method will return the unmodified input.
     * @param input NodeMode to cycle
     */
    public static NodeMode cycle(NodeMode input) {
        if(input.equals(NodeMode.NONE)) return input; // NONE can't be cycled
        int pos = input.ordinal();
        pos += 1;
        if(pos >= NodeMode.values().length) pos = 1;
        return NodeMode.values()[pos];
    }

    /***
     * Populates a given CompoundTag with this NodeMode
     * @param in CompoundTag to modify
     * @return The modified CompoundTag 
     */
    public CompoundTag writeTo(CompoundTag in) {
        in.putString("NodeMode", getSerializedName());
        return in;
    }

    /***
     * 
     * @param in
     * @return The relevent NodeMode from this CompoundTag
     */
    public static NodeMode fromTag(CompoundTag in) {
        if(in.contains("NodeMode")) {
            String mode = in.getString("NodeMode");
            if(mode.equals("insert")) return from(true, false);
            if(mode.equals("extract")) return from(false, true);
            if(mode.equals("both")) return from(true, true);
            if(mode.equals("none")) return from(false, false);
        }
        throw new IllegalArgumentException("CompoundTag " + in + " doesn't contain relevent values to read!");
    }


    /***
     * Compose a new NodeMode from two boolean values
     * @param isInput Can this node input?
     * @param isOutput Can this node output?
     * @return the appropriate NodeMode for the given values
     */
    public static NodeMode from(boolean isInput, boolean isOutput) {
        if(isInput && isOutput)
            return BOTH;
        else {
            if(isInput) return INSERT;
            else if (isOutput) return EXTRACT;
        }
        return NONE;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    /***
     * Gets the highlight color of this node.
     * @return
     */
    public Color getColor() {
        return color.copy();
    }

    /***
     * Gets the highlight color of this node mixed with its darker variant
     * @param percent Percent of color to mix (1.0 = fully bright, 0 = fully dark)
     * @return
     */
    public Color getColor(float percent) {
        return darkColor.copy().mixWith(color, percent);
    }
}