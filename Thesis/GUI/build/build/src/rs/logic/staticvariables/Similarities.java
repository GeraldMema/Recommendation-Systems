package rs.logic.staticvariables;

import java.util.List;
import java.util.Map;

/**
 * Class containing the similarities values
 *
 * @author Simon Tzanakis
 */
public class Similarities
{
//    private static double[][] similarities_weighted;
	public static Map<Integer,Map<Integer,Double>> similarities_weighted;
//    private static double[][] similarities_overall;
    private static Map<Integer,Map<Integer,Double>> similarities_overall;
//    private static double[][] similarities_aggregated;
    private static Map<Integer,Map<Integer,Double>> similarities_aggregated;
    
    public static Map<Integer,Map<Integer,Double>> getSimilarities_aggregated()
    {
        return similarities_aggregated;
    }

    public static void setSimilarities_aggregated(Map<Integer,Map<Integer,Double>> similarities_aggregated)
    {
        Similarities.similarities_aggregated = similarities_aggregated;
    }
    
//    public static void setSimilarities_aggregatedAtIndex(int x, int y, double value)
//    {
//        Similarities.similarities_aggregated[x][y] = value;
//    }
    
    public static Map<Integer,Map<Integer,Double>> getSimilarities_overall()
    {
        return similarities_overall;
    }

    public static void setSimilarities_overall(Map<Integer,Map<Integer,Double>> similarities_overall)
    {
        Similarities.similarities_overall = similarities_overall;
    }
    
//    public static void setSimilarities_overallAtIndex(int x, int y, double value)
//    {
//        Similarities.similarities_overall[x][y] = value;
//    }

    
    public static void setSimilarities_weighted(Map<Integer,Map<Integer,Double>> similarities_weighted)
    {
        Similarities.similarities_weighted = similarities_weighted;
    }
    
//    public static void setSimilarities_weighted(double[][] similarities_weighted)
//    {
//        Similarities.similarities_weighted = similarities_weighted;
//    }
    
    public static Map<Integer,Map<Integer,Double>> getSimilarities_weighted()
    {
        return similarities_weighted;
    }

//    public static double[][] getSimilarities_weighted()
//    {
//        return similarities_weighted;
//    }
    
//    public static void setSimilarities_weightedAtIndex(int x, int y, double value)
//    {
//    	Similarities.similarities_weighted.get(x).set(y,value);
////        Similarities.similarities_weighted[x][y] = value;
//    }   
}
