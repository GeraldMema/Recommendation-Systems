package rs.logic.staticvariables;

/**
 * Class containing the predicted values
 *
 * @author Simon Tzanakis
 */
public class Predicted
{
    private static double[][] predicted;

    public static double[][] getPredicted()
    {
        return predicted;
    }

    public static void setPredicted(double[][] predicted)
    {
        Predicted.predicted = predicted;
    }
}
