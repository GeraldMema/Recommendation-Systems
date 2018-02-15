package rs.logic.algorithms.steps;

import java.util.Map;

import rs.logic.staticvariables.Dataset;

/**
 * Contains methods to calculate predictions
 *
 * @author Simon Tzanakis
 */
public class PredictedRatings
{
    private int U;
    private int M;
    private double[][] predicted_values;

    /**
     *
     * @return Returns the 2D matrix containing the similarities calculated from {@link #PredictRatings_Weighted_Sum_Approach(int, int, double[][]) } or {@link #PredictRatings_Adjusted_Weighted_Sum_Approach(int, int, double[][]) }
     */
    public double[][] getPredicted_values()
    {
        return predicted_values;
    }
    
    public void PredictRatings_Weighted_Sum_Approach(int u, int m,Map<Integer,Map<Integer,Double>> sim)
    {
        U = u;
        M = m;
        predicted_values = new double[U][M];

        int[][][] dt = Dataset.getNewdataset();

        for (int i = 0; i < U; i++)
        {
            for (int j = 0; j < M; j++)
            {
                double sum1 = 0;
                double sum2 = 0;
                
                for (Map.Entry<Integer,Double> neighbour : sim.get(i).entrySet() ) 
                {

                	if( neighbour.getKey() != -1)
                		if (dt[neighbour.getKey()][j][0] != -1)
                		{
                			sum1 += neighbour.getValue() * dt[neighbour.getKey()][j][0];
                			sum2 += Math.abs(neighbour.getValue());
                		}

                }
                double z;
                if (sum2 == 0)
                {
                    z = 0;
                }
                else
                {
                    z = 1 / sum2;
                }
                predicted_values[i][j] = z * sum1;
            }
        }
    }

    /**
     *
     * @param u Length of the items on rows
     * @param m Length of the items on columns
     * @param sim Matrix with the similarities values
     */
    public void PredictRatings_Weighted_Sum_Approach(int u, int m, double[][] sim)
    {
        U = u;
        M = m;
        predicted_values = new double[U][M];

        int[][][] dt = Dataset.getNewdataset();

        for (int i = 0; i < U; i++)
        {
            for (int j = 0; j < dt[i].length; j++)
            {
                double sum1 = 0;
                double sum2 = 0;
                
                for (int k = 0; k < U; k++)
                {
                    if (i == k || j >= dt[k].length)
                    {
                        continue;
                    }
                    else
                    {
                        if (dt[k][j][0] != -1)
                        {
                            sum1 += sim[i][k] * dt[k][j][0];
                            sum2 += Math.abs(sim[i][k]);
                        }
                    }
                }
                double z;
                if (sum2 == 0)
                {
                    z = 0;
                }
                else
                {
                    z = 1 / sum2;
                }
                predicted_values[i][j] = z * sum1;
            }
        }
    }
    
    public void PredictRatings_Adjusted_Weighted_Sum_Approach(int u, int m, Map<Integer,Map<Integer,Double>> sim)
    {
        U = u;
        M = m;
        predicted_values = new double[U][M];
        

        int[][][] dt = Dataset.getNewdataset();
        double[] average = new double[U];
        int counter;

        for (int i = 0; i < U; i++)
        {
            double sum = 0;
            counter = 0;
            for (int j = 0; j < M; j++)
            {
                if (dt[i][j][0] != -1)
                {
                    sum += dt[i][j][0];
                    counter++;
                }
            }
            average[i] = (double)(sum / counter);
        }

        for (int i = 0; i < U; i++)
        {
            double sum1 = 0;
            double sum2 = 0;
            for (int j = 0; j < M; j++)
            {
            	for (Map.Entry<Integer,Double> neighbour : sim.get(i).entrySet() ) 
                {

                	if( neighbour.getKey() != -1)
                		if (dt[neighbour.getKey()][j][0] != -1)
                		{
                			sum1 += neighbour.getValue() * (dt[neighbour.getKey()][j][0] - average[neighbour.getKey()]);
                			sum2 += Math.abs(neighbour.getValue());
                		}

                }
                double z;
                if (sum2 == 0)
                {
                    z = 0;
                }
                else
                {
                    z = 1 / sum2;
                }
                predicted_values[i][j] = average[i] + z * sum1;
            }
        }
    }

    /**
     *
     * @param u Length of the items on rows
     * @param m Length of the items on columns
     * @param sim Matrix with the similarities values
     */
//    public void PredictRatings_Adjusted_Weighted_Sum_Approach(int u, int m, double[][] sim)
//    {
//        U = u;
//        M = m;
//        predicted_values = new double[U][M];
//
//        int[][][] dt = Dataset.getNewdataset();
//        double[] average = new double[U];
//        int counter;
//
//        for (int i = 0; i < U; i++)
//        {
//            double sum = 0;
//            counter = 0;
//            for (int j = 0; j < M; j++)
//            {
//                if (dt[i][j][0] != -1)
//                {
//                    sum += dt[i][j][0];
//                    counter++;
//                }
//            }
//            average[i] = (double)(sum / counter);
//        }
//
//        for (int i = 0; i < U; i++)
//        {
//            double sum1 = 0;
//            double sum2 = 0;
//            for (int j = 0; j < M; j++)
//            {
//                for (int k = 0; k < U; k++)
//                {
//                    if (i == k)
//                    {
//                        continue;
//                    }
//                    else
//                    {
//                        if (dt[k][j][0] != -1)
//                        {
//                            sum1 += sim[i][k] * (dt[k][j][0] - average[k]);
//                            sum2 += Math.abs(sim[i][k]);
//                        }
//                    }
//                }
//                double z;
//                if (sum2 == 0)
//                {
//                    z = 0;
//                }
//                else
//                {
//                    z = 1 / sum2;
//                }
//                predicted_values[i][j] = average[i] + z * sum1;
//            }
//        }
//    }
}
