package rs.logic.algorithms.general;

/**
 *
 * @author Simon Tzanakis
 */
public class Normalize0to1
{
    /**
     * Normalizes a value to the range 0-1
     *
     * @param value The value to normalize
     * @param min The minimum acceptable value of the value to normalize before normalization
     * @param max The maximum acceptable value of the value to normalize before normalization
     * @return Returns the normalized value
     */
    public double Normalize_value0to1(double value, double min, double max)
    {
        double normalized = (value - min) / (max - min);
        return normalized;
    }
}
