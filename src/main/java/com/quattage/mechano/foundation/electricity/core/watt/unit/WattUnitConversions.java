
package com.quattage.mechano.foundation.electricity.core.watt.unit;

import com.quattage.mechano.MechanoSettings;

public class WattUnitConversions {
    
    /**
     * Converts a standard integer voltage input into a stored voltage value such that 
     * <code>(input - 32,768) / 4 = output</code><p>
     * 
     * @param input Any int value - will be cut off if it is less than <code>zero</code> 
     * or greater than <code>262141</code>.
     * @return A short representing the input voltage value.
     * @see WattUnitConversions#toRealVolts(short) performing the inverse operation.
     */
    public static short toStoredVolts(int input) {
        int rI = toNearestFour(input);
        return (short)(rI > 0 ? (rI < 262141 ? (((int)(rI / 4)) - 32768f) : 32767) : -32768);
    }

    private static int toNearestFour(int in) {
        int rV = in % 4;
        return rV < 2 ? in - rV : (in + 4) - rV;
    }

    /**
     * Converts a compressed short voltage value into a legible integer
     * <code>(output - 32,768) / 4 = input</code><p>
     * 
     * @param input Any short value between 0 and 65355
     * @return An int value directly representing voltage
     * @see WattUnitConversions#toStoredVolts(int) performing the inverse operation.
     */
    public static int toRealVolts(short input) {
        return (int)(input + 32768) * 4;
    }

    /**
     * Makes a new Watt from an FE value and scales the resulting WattUnit object's current and voltage 
     * based on the volume of FE provided. Assumes a <strong>maximum</strong> voltage of {@link MechanoSettings#FE2W_VOLTAGE this constant}
     * @param fe FE value to convert from. 
     * @param scale <code>TRUE</code> if the resulting WattUnit's voltage should be scaled based on the volume of FE. This makes handling large or
     * small amounts of FE more convenient, as lower FE values will convey at a lower voltage.
     * @return a new Watt object whose power value is representative of the given FE value
     */
    public static WattUnit toWatts(int fe) {
        return toWatts(fe, true);
    }

    /**
     * Makes a new Watt from an FE value and scales the resulting WattUnit object's current and voltage 
     * based on the volume of FE provided. Assumes a <strong>maximum</strong> voltage of {@link MechanoSettings#FE2W_VOLTAGE this constant}
     * @param fe FE value to convert from. 
     * @param scale <code>TRUE</code> if the resulting WattUnit's voltage should be scaled based on the volume of FE. This makes handling large or
     * small amounts of FE more convenient, as lower FE values will convey at a lower voltage.
     * @return a new Watt object whose power value is representative of the given FE value
     */
    public static WattUnit toWatts(int fe, boolean scale) {
        if(fe < 1) return new WattUnit(0, 0);

        WattUnit out = new WattUnit(4, ((float)fe / (float)MechanoSettings.FE2W_RATE) * 0.25f);
        if(!scale) return out.adjustVoltage(MechanoSettings.FE2W_VOLTAGE);

        int volts = (int)((MechanoSettings.FE2W_VOLTAGE * fe) * 0.001);
        return out.adjustVoltage(volts < MechanoSettings.FE2W_VOLTAGE ? (volts < 4 ? 4 : volts) : MechanoSettings.FE2W_VOLTAGE);
    }

    /**
     * Converts total watts into ForgeEnergy
     * based on the conversion rate defined in {@link MechanoSettings#FE2W_RATE this constant}
     * @param watts Accepts float, int or WattUnit
     * @return rounded int value for FE
     */
    public static int toFE(float watts) {
        return Math.round((watts * (float)MechanoSettings.FE2W_RATE));
    } 

    /**
     * Converts total watts into ForgeEnergy
     * based on the conversion rate defined in {@link MechanoSettings#FE2W_RATE this constant}
     * @param watts Accepts float, int or WattUnit
     * @return rounded int value for FE
     */
    public static int toFE(WattUnit watts) {
        return Math.round((watts.getWatts() * (float)MechanoSettings.FE2W_RATE));
    } 

    /**
     * Converts total watts into ForgeEnergy
     * based on the conversion rate defined in {@link MechanoSettings#FE2W_RATE this constant}
     * @param watts Accepts float, int or WattUnit
     * @return rounded int value for FE
     */
    public static int toFE(int watts) {
        return watts * MechanoSettings.FE2W_RATE;
    } 

    public static float toWattsSimple(int fe) {
        return (float)fe / (float)MechanoSettings.FE2W_RATE;
    }
}