package rs.logic.algorithms.steps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.logic.algorithms.general.Normalize0to1;
import rs.logic.staticvariables.Coefficients;
import rs.logic.staticvariables.Dataset;

/**
 * Contains methods to calculate similarities
 *
 * @author georgia
 */
public class CalculateSimilarities
{
	private int U; //Number of users
	private int R;  //Number of multi-criteria ratings
	private int[][][] dataset;
	private double[][] coefficients;   //Coefficients are R+1
	//    private double[][] similarities_weighted;     //Square matrix with similarities_weighted and diagonal has aces(1)
	Map<Integer,Map<Integer,Double>> similarities_weighted;
	//	private double[][] similarities_overall;     //Square matrix with similarities_overall and diagonal has aces(1)
	Map<Integer,Map<Integer,Double>> similarities_overall;     //Square matrix with similarities_overall and diagonal has aces(1)
	//	private double[][] similarities_aggregated;     //Square matrix with similarities_aggregated and diagonal has aces(1)
	Map<Integer,Map<Integer,Double>> similarities_aggregated;     //Square matrix with similarities_aggregated and diagonal has aces(1)

	/**
	 *
	 * @return Returns the 2D matrix containing the similarities calculated from {@link #CalculateSimilarities_Weighted(int, int, double[][])}
	 */
	//    public double[][] getSimilarities_weighted()
	//    {
	//        return similarities_weighted;
	//    }

	public Map<Integer,Map<Integer,Double>> getSimilarities_weighted()
	{
		return similarities_weighted;
	}

	/**
	 *
	 * @return Returns the 2D matrix containing the similarities calculated from {@link #CalculateSimilarities_Overall_Cosine(int, double[][])} or {@link #CalculateSimilarities_Overall_Pearson(int, double[][]) }
	 */
	public Map<Integer,Map<Integer,Double>> getSimilarities_overall()
	{
		return similarities_overall;
	}

	/**
	 *
	 * @return Returns the 2D matrix containing the similarities calculated from {@link #CalculateSimilarities_Aggregated_Multiply(double[][], double[][])} or {@link #CalculateSimilarities_Aggregated_Intersection(double[][], double[][], java.lang.String) }
	 */
	public Map<Integer,Map<Integer,Double>> getSimilarities_aggregated()
	{
		return similarities_aggregated;
	}

	public boolean CalculateSimilarities_Weighted(int u, int r, Map<Integer,List<double[]>> neighboursByCoefsMap,Map<double[],Integer> userCoefToId)
	{
		U = u;
		R = (r - 1);              //We just need the multi-criteria ratings not the overall

		similarities_weighted = new HashMap<Integer,Map<Integer,Double>>();

		double coefs[][] = Coefficients.getCoefficients();

		double userCoef[] = new double[coefs[0].length];

		for (int i = 0; i < U; i++)
		{

			System.arraycopy(coefs[i], 0, userCoef, 0, coefs[i].length);

			Map<Integer,Double> similarities =  new HashMap<Integer,Double>();

			coefficients = new double[neighboursByCoefsMap.get(i).size()][r];

			// vres ta similarities gia ka8e geitona tou user i
			for (double[] neighBhourCoef : neighboursByCoefsMap.get(i) ) {

				double sum1 = 0;
				double sum2 = 0;
				double sum3 = 0;
				double similarity = 0;

				for (int k = 1; k < 1 + R; k++)     // From k = 1 cause we don't use r0
				{
					sum1 = sum1 + userCoef[k] * neighBhourCoef[k];
					sum2 = sum2 + userCoef[k] * userCoef[k];
					sum3 = sum3 + neighBhourCoef[k] * neighBhourCoef[k];
				}

				if (sum1 == 0 && (sum2 == 0 || sum3 == 0))
				{
					similarity = -1;
				}
				else
				{
					similarity = sum1 / (Math.sqrt(sum2) * Math.sqrt(sum3));
				}
				Normalize0to1 normal = new Normalize0to1();
				similarity = normal.Normalize_value0to1(similarity, -1, 1);     //Normalize value to range [0,1] cause we got before [-1,1]

				similarities.put(userCoefToId.get(neighBhourCoef),similarity);

			}

			similarities_weighted.put(i, similarities);
		}

		//		System.out.println("similarities CalculateSimilarities_Weighted : ");
		//
		//		for (Map.Entry<Integer, List<Double>> entry : similarities_weighted.entrySet()) 
		//		{
		//			System.out.println(" i = " + entry.getKey());
		//
		//			for (double sim : entry.getValue()) {
		//				System.out.print(sim + " ");
		//			}
		//			System.out.println();
		//		}

		return true;
	}

	/**
	 *
	 * @param u Length of the items
	 * @param r Length of the weigths
	 * @param coef Coefficients matrix to calculate similaries from
	 * @return Returns true
	 */
	//    public boolean CalculateSimilarities_Weighted(int u, int r, double[][] coef)
	//    {
	//        U = u;
	//        R = (r - 1);              //We just need the multi-criteria ratings not the overall
	//        coefficients = coef;
	//
	//        similarities_weighted = new double[U][U];
	//
	//        for (int i = 0; i < U; i++)
	//        {
	//            int counter = 0;    //Used for faster iteration because of the symmetric squarematrix that we have                        
	//            for (int j = counter; j < U; j++)
	//            {
	//                double sum1 = 0;
	//                double sum2 = 0;
	//                double sum3 = 0;
	//                double similarity = 0;
	//                for (int k = 1; k < 1 + R; k++)     // From k = 1 cause we don't use r0
	//                {
	//                    sum1 = sum1 + coefficients[i][k] * coefficients[j][k];
	//                    sum2 = sum2 + coefficients[i][k] * coefficients[i][k];
	//                    sum3 = sum3 + coefficients[j][k] * coefficients[j][k];
	//                }
	//                if (sum1 == 0 && (sum2 == 0 || sum3 == 0))
	//                {
	//                    similarity = -1;
	//                }
	//                else
	//                {
	//                    similarity = sum1 / (Math.sqrt(sum2) * Math.sqrt(sum3));
	//                }
	//                Normalize0to1 normal = new Normalize0to1();
	//                similarity = normal.Normalize_value0to1(similarity, -1, 1);     //Normalize value to range [0,1] cause we got before [-1,1]
	//                similarities_weighted[i][j] = similarity;
	//                similarities_weighted[j][i] = similarity;
	//            }
	//            counter++;
	//        }
	//        return true;
	//    }

	public boolean CalculateSimilarities_Weighted_Pearson(int users, int rating, Map<Integer,List<double[]>> neighboursByCoefsMap , Map<double[],Integer> userCoefToId )
	{
		int i,j,k,counter=0;
		double Ru = 0, Rv = 0, sum1 = 0, sum2 = 0, sum3 = 0, similarities = 0;

		//        similarities_weighted = new double[users][users];

		similarities_weighted = new HashMap<Integer,Map<Integer,Double>>();

		double coefs[][] = Coefficients.getCoefficients();

		double userCoef[] = new double[coefs[0].length];

		for (i=0; i< users; i++){    //loop for all the users     

			System.arraycopy(coefs[i], 0, userCoef, 0, coefs[i].length);

			Map<Integer,Double> similaritiesList =  new HashMap<Integer,Double>();

			counter = 0;

			// vres meso oro twn coefficients tou xrhsth i = Ru
			for (k=1; k<rating; k++){
				if(userCoef[k]!=-1){
					counter++;
					Ru+=userCoef[k];
				}
			} 

			Ru=Ru/counter;            //calculate the average of similarities for the current user
			//			similarities_weighted[i][i] = 1;     //fill the diagonal

			List<double[]> neighbourCoefs = neighboursByCoefsMap.get(i);

			for (double[] neighbourCoef : neighbourCoefs ) 
			{

				// vres meso oro twn coefficients tou geitona tou xrhsth i (Rv)
				counter = 0;

				//pros erevna
				sum1 = 0;
				sum2 = 0;
				sum3 = 0;

				for (k=1; k<rating; k++){

					if(neighbourCoef[k]!=-1){
						counter++;
						Rv+=neighbourCoef[k];
					}
				} 

				Rv=Rv/counter;            //calculate the average of similarities for the inner current user

				for(k=1; k<rating; k++){  //loop for all the multiple criteria(without the overal)
					sum1+=(userCoef[k]-Ru)*(neighbourCoef[k]-Rv);
					sum2+=(userCoef[k]-Ru)*(userCoef[k]-Ru);
					sum3+=(neighbourCoef[k]-Rv)*(neighbourCoef[k]-Rv);
				}

				if(sum1 == 0 && (sum2 == 0 || sum3 == 0)) {
					similarities = -1;
				}
				else{
					similarities = sum1 /(Math.sqrt(sum2) * Math.sqrt(sum3));
				}
				Normalize0to1 normal = new Normalize0to1();
				similarities = normal.Normalize_value0to1(similarities, -1, 1);   //normalize the result

				similaritiesList.put(userCoefToId.get(neighbourCoef),similarities);

			}

			similarities_weighted.put(i, similaritiesList);

		}

		//		System.out.println("similarities CalculateSimilarities_Weighted_Pearson : ");
		//
		//		for (Map.Entry<Integer, List<Double>> entry : similarities_weighted.entrySet()) 
		//		{
		//			System.out.println(" i = " + entry.getKey());
		//
		//			for (double sim : entry.getValue()) {
		//				System.out.print(sim + " ");
		//			}
		//			System.out.println();
		//		}

		return true;        
	}

	/**
	 *
	 * @param users number of users
	 * @param rating number_of_ratings
	 * @param coef Coefficients matrix to calculate similaries from
	 * @return Returns true
	 */
	//    public boolean CalculateSimilarities_Weighted_Pearson(int users, int rating, double [][] coef )
	//    {
	//        int i,j,k,counter=0;
	//        double Ru = 0, Rv = 0, sum1 = 0, sum2 = 0, sum3 = 0, similarities = 0;
	//   
	//        similarities_weighted = new double[users][users];
	//        
	//       for (i=0; i< users; i++){    //loop for all the users            
	//           counter = 0;
	//            for (k=1; k<rating; k++){
	//                if(coef[i][k]!=-1){
	//                    counter++;
	//                    Ru+=coef[i][k];
	//                }
	//           } 
	//           Ru=Ru/counter;            //calculate the average of similarities for the current user
	//           similarities_weighted[i][i] = 1;     //fill the diagonal
	//           
	//           for (j=i+1; j<users; j++){   //loop for the rest of the users             
	//               counter = 0;
	//              for (k=1; k<rating; k++){
	//                  
	//                  if(coef[j][k]!=-1){
	//                      counter++;
	//                      Rv+=coef[j][k];
	//                  }
	//              } 
	//              Rv=Rv/counter;            //calculate the average of similarities for the inner current user
	//              
	//              for(k=1; k<rating; k++){  //loop for all the multiple criteria(without the overal)
	//                    sum1+=(coef[i][k]-Ru)*(coef[j][k]-Rv);
	//                    sum2+=(coef[i][k]-Ru)*(coef[i][k]-Ru);
	//                    sum3+=(coef[j][k]-Rv)*(coef[j][k]-Rv);
	//              }
	//              if(sum1 == 0 && (sum2 == 0 || sum3 == 0)) {
	//                   similarities = -1;
	//              }
	//              else{
	//                  similarities = sum1 /(Math.sqrt(sum2) * Math.sqrt(sum3));
	//              }
	//              Normalize0to1 normal = new Normalize0to1();
	//              similarities = normal.Normalize_value0to1(similarities, -1, 1);   //normalize the result
	//              similarities_weighted[i][j]=similarities; //fill the upper diagonal
	//              similarities_weighted[j][i]=similarities; //fill the below diagonal(because of the symmetry)
	//           }
	//       }
	//       return true;        
	//    }


	public boolean CalculateSimilarities_Overall_Cosine(int u, double[][] coef,Map<Integer,int[]> indexOfNeigboursMap )
	{
		U = u;
		R = 0;              //We just need the overall rating not the multi-criteria
		coefficients = coef;

		similarities_overall = new HashMap<Integer, Map<Integer,Double>>();
		int[][][] dt = Dataset.getNewdataset();


		for (int i = 0; i < U; i++)
		{
			Map<Integer,Double> simMap = new HashMap<Integer,Double>();


			int[] indexOfNeighboursArray = indexOfNeigboursMap.get(i);

			for (int p = 0; p < indexOfNeighboursArray.length; p++)
			{
				int j = indexOfNeighboursArray[p];
				double sum1 = 0;
				double sum2 = 0;
				double sum3 = 0;
				double similarity = 0;

				if( j != -1)
				{
					for (int k = 0; k < dt[i].length; k++)   // Iterate all the movies
					{
						if (dt[i][k][0] != -1 && dt[j][k][0] != -1)    // -1 are unrated movies
						{
							sum1 = sum1 + (coefficients[i][0] * (double)dt[i][k][0]) * (coefficients[j][0] * (double)dt[j][k][0]);
							sum2 = sum2 + (coefficients[i][0] * (double)dt[i][k][0]) * (coefficients[i][0] * (double)dt[i][k][0]);
							sum3 = sum3 + (coefficients[j][0] * (double)dt[j][k][0]) * (coefficients[j][0] * (double)dt[j][k][0]);
						}

					}
				}

				if (sum1 == 0 && (sum2 == 0 || sum3 == 0))
				{
					similarity = -1;
				}
				else
				{
					similarity = sum1 / (Math.sqrt(sum2) * Math.sqrt(sum3));
				}
				Normalize0to1 normal = new Normalize0to1();
				similarity = normal.Normalize_value0to1(similarity, -1, 1);     //Normalize value to range [0,1] cause we got before [-1,1]

				// Na krathsw mia lista apo ta similarities me tous geitones
				simMap.put(j,similarity);

			}

			similarities_overall.put(new Integer(i), simMap);

		}
		return true;
	}

	/**
	 *
	 * @param u Length of the items
	 * @param coef Coefficients matrix to calculate similaries from
	 * @return Returns true
	 */
	//	public boolean CalculateSimilarities_Overall_Cosine(int u, double[][] coef)
	//	{
	//		U = u;
	//		R = 0;              //We just need the overall rating not the multi-criteria
	//		coefficients = coef;
	//
	//		similarities_overall = new double[U][U];
	//		int[][][] dt = Dataset.getNewdataset();
	//
	//		//        System.out.println("coefficients = ");
	//		//        for (int[][] is : dt) {
	//		//			for (double[] is2 : coefficients) {
	//		//				System.out.println(Arrays.toString(is2));
	//		//			}
	//		//			System.out.println();
	//		//		}
	//
	//		for (int i = 0; i < U; i++)
	//		{
	//			int counter = 0;    //Used for faster iteration because of the symmetric squarematrix that we have                        
	//			for (int j = counter; j < U; j++)
	//			{
	//				double sum1 = 0;
	//				double sum2 = 0;
	//				double sum3 = 0;
	//				double similarity = 0;
	//
	//				for (int k = 0; k < Math.min(dt[i].length, dt[j].length); k++)   // Iterate all the movies
	//				{
	//					if (dt[i][k][0] != -1 && dt[j][k][0] != -1)    // -1 are unrated movies
	//					{
	//						sum1 = sum1 + (coefficients[i][0] * dt[i][k][0]) * (coefficients[j][0] * dt[j][k][0]);
	//						sum2 = sum2 + (coefficients[i][0] * dt[i][k][0]) * (coefficients[i][0] * dt[i][k][0]);
	//						sum3 = sum3 + (coefficients[j][0] * dt[j][k][0]) * (coefficients[j][0] * dt[j][k][0]);
	//					}
	//
	//				}
	//				if (sum1 == 0 && (sum2 == 0 || sum3 == 0))
	//				{
	//					similarity = -1;
	//				}
	//				else
	//				{
	//					similarity = sum1 / (Math.sqrt(sum2) * Math.sqrt(sum3));
	//				}
	//				Normalize0to1 normal = new Normalize0to1();
	//				similarity = normal.Normalize_value0to1(similarity, -1, 1);     //Normalize value to range [0,1] cause we got before [-1,1]
	//				similarities_overall[i][j] = similarity;
	//				similarities_overall[j][i] = similarity;
	//			}
	//
	//			counter++;
	//		}
	//		return true;
	//	}

	public boolean CalculateSimilarities_Overall_Pearson(int u, double[][] coef,Map<Integer,int[]> indexOfNeigboursMap)
	{
		U = u;
		R = 0;              //We just need the overall rating not the multi-criteria
		coefficients = coef;

		similarities_overall = new HashMap<Integer,Map<Integer,Double>>();

		int[][][] dt = Dataset.getNewdataset();

		for (int i = 0; i < U; i++)
		{

			Map<Integer,Double> simMap = new HashMap<Integer,Double>();

			int[] indexOfNeighboursArray = indexOfNeigboursMap.get(i);

			for (int p = 0; p < indexOfNeighboursArray.length; p++)
			{
				int j = indexOfNeighboursArray[p];

				double sum1 = 0;
				double sum2 = 0;
				double sum3 = 0;
				double similarity = 0;
				double co_averageU = 0;
				double co_averageV = 0;
				int items_counter = 0;

				if( j != -1)
				{
					for (int k = 0; k < Dataset.getYSize(); k++)     // Iterate all the movies
					{
						if (dt[i][k][0] != -1 && dt[j][k][0] != -1)    // -1 are unrated movies
						{
							items_counter++;
							co_averageU += dt[i][k][0];
							co_averageV += dt[j][k][0];
						}
					}
					co_averageU = co_averageU / items_counter;
					co_averageV = co_averageV / items_counter;

					for (int k = 0; k < Dataset.getYSize(); k++)     // Iterate all the movies
					{
						if (dt[i][k][0] != -1 && dt[j][k][0] != -1)    // -1 are unrated movies
						{
							sum1 = sum1 + (dt[i][k][0] - co_averageU) * (dt[j][k][0] - co_averageV);
							sum2 = sum2 + (dt[i][k][0] - co_averageU) * (dt[i][k][0] - co_averageU);
							sum3 = sum3 + (dt[j][k][0] - co_averageV) * (dt[j][k][0] - co_averageV);
						}
					}
				}
				if (sum1 == 0 && (sum2 == 0 || sum3 == 0))
				{
					similarity = -1;
				}
				else
				{
					similarity = sum1 / (Math.sqrt(sum2) * Math.sqrt(sum3));
				}
				Normalize0to1 normal = new Normalize0to1();
				similarity = normal.Normalize_value0to1(similarity, -1, 1);     //Normalize value to range [0,1] cause we got before [-1,1]

				simMap.put(j,similarity);

			}

			similarities_overall.put(new Integer(i), simMap);

		}
		return true;
	}

	/**
	 *
	 * @param u Length of the items
	 * @param coef Coefficients matrix to calculate similaries from
	 * @return Returns true
	 */
	//	public boolean CalculateSimilarities_Overall_Pearson(int u, double[][] coef)
	//	{
	//		U = u;
	//		R = 0;              //We just need the overall rating not the multi-criteria
	//		coefficients = coef;
	//
	//		similarities_overall = new double[U][U];
	//		int[][][] dt = Dataset.getNewdataset();
	//
	//		for (int i = 0; i < U; i++)
	//		{
	//			int counter = 0;    //Used for faster iteration because of the symmetric squarematrix that we have                        
	//			for (int j = counter; j < U; j++)
	//			{
	//				double sum1 = 0;
	//				double sum2 = 0;
	//				double sum3 = 0;
	//				double similarity = 0;
	//				double co_averageU = 0;
	//				double co_averageV = 0;
	//				int items_counter = 0;
	//
	//				for (int k = 0; k < Dataset.getYSize(); k++)     // Iterate all the movies
	//				{
	//					if (dt[i][k][0] != -1 && dt[j][k][0] != -1)    // -1 are unrated movies
	//					{
	//						items_counter++;
	//						co_averageU += dt[i][k][0];
	//						co_averageV += dt[j][k][0];
	//					}
	//				}
	//				co_averageU = co_averageU / items_counter;
	//				co_averageV = co_averageV / items_counter;
	//
	//				for (int k = 0; k < Dataset.getYSize(); k++)     // Iterate all the movies
	//				{
	//					if (dt[i][k][0] != -1 && dt[j][k][0] != -1)    // -1 are unrated movies
	//					{
	//						sum1 = sum1 + (dt[i][k][0] - co_averageU) * (dt[j][k][0] - co_averageV);
	//						sum2 = sum2 + (dt[i][k][0] - co_averageU) * (dt[i][k][0] - co_averageU);
	//						sum3 = sum3 + (dt[j][k][0] - co_averageV) * (dt[j][k][0] - co_averageV);
	//					}
	//				}
	//				if (sum1 == 0 && (sum2 == 0 || sum3 == 0))
	//				{
	//					similarity = -1;
	//				}
	//				else
	//				{
	//					similarity = sum1 / (Math.sqrt(sum2) * Math.sqrt(sum3));
	//				}
	//				Normalize0to1 normal = new Normalize0to1();
	//				similarity = normal.Normalize_value0to1(similarity, -1, 1);     //Normalize value to range [0,1] cause we got before [-1,1]
	//				similarities_overall[i][j] = similarity;
	//				similarities_overall[j][i] = similarity;
	//			}
	//			counter++;
	//		}
	//		return true;
	//	}


	public boolean CalculateSimilarities_Aggregated_Multiply(Map<Integer,Map<Integer,Double>> sim_w, Map<Integer,Map<Integer,Double>> sim_o)
	{

		similarities_aggregated = new HashMap<Integer,Map<Integer,Double>>();

		for (Map.Entry<Integer, Map<Integer,Double>> user : sim_w.entrySet()) 
		{

			int userIndex = user.getKey();

			Map<Integer,Double> neighbours = new HashMap<Integer,Double>();

			Map<Integer,Double> simONeighbours = sim_o.get(userIndex);

			Map<Integer,Double> simWNeighbours = sim_w.get(userIndex);

			for (Map.Entry<Integer,Double> entry : simONeighbours.entrySet()) 
			{
				Double result = 0.0;

				Integer neighbourIndex = entry.getKey();

				if( neighbourIndex == -1 ) // den exei geitones
				{
					neighbours.put(-1, 0.0);
					break;
				}

				if( simWNeighbours.containsKey(neighbourIndex) )
				{

					result =  simWNeighbours.get(neighbourIndex) * simONeighbours.get(neighbourIndex);
				}
				else
					result = simONeighbours.get(neighbourIndex);

				neighbours.put(neighbourIndex, result);
			}

			for (Map.Entry<Integer,Double> entry : simWNeighbours.entrySet()) 
			{
				Double result = 0.0;

				Integer neighbourIndex = entry.getKey();

				if( neighbourIndex == -1 ) // den exei geitones
				{
					neighbours.put(-1, 0.0);
					break;
				}

				if( simONeighbours.containsKey(neighbourIndex) )
				{
					continue;
				}
				else
					result = simWNeighbours.get(neighbourIndex);

				neighbours.put(neighbourIndex, result);
			}

			similarities_aggregated.put(userIndex, neighbours);

		}

		return true;
	}

	/**
	 *
	 * @param sim_w Similarities weighted matrix to calculate similaries from
	 * @param sim_o Similarities overall matrix to calculate similaries from
	 * @return Returns true
	 */
	//	public boolean CalculateSimilarities_Aggregated_Multiply(double[][] sim_w, double[][] sim_o)
	//	{
	//
	//		int u = sim_w.length;
	//		similarities_aggregated = new double[u][u];
	//
	//		for (int i = 0; i < u; i++)
	//		{
	//			for (int j = 0; j < u; j++)
	//			{
	//				similarities_aggregated[i][j] = sim_w[i][j] * sim_o[i][j];
	//			}
	//		}
	//
	//		return true;
	//	}

	public boolean CalculateSimilarities_Aggregated_Intersection(Map<Integer,Map<Integer,Double>> sim_w, Map<Integer,Map<Integer,Double>> sim_o, String p)
	{
		
		similarities_aggregated = new HashMap<Integer,Map<Integer,Double>>();
		
		for (Map.Entry<Integer, Map<Integer,Double>> user : sim_w.entrySet()) 
		{

			int userIndex = user.getKey();

			Map<Integer,Double> neighbours = new HashMap<Integer,Double>();

			Map<Integer,Double> simONeighbours = sim_o.get(userIndex);

			Map<Integer,Double> simWNeighbours = sim_w.get(userIndex);

			for (Map.Entry<Integer,Double> entry : simONeighbours.entrySet()) 
			{
				Double result = 0.0;

				Integer neighbourIndex = entry.getKey();

				if( neighbourIndex == -1 ) // den exei geitones
				{
					neighbours.put(-1, 0.0);
					break;
				}

				if( simWNeighbours.containsKey(neighbourIndex) )
				{

					if (p.equals("infinity"))
						result =  Math.min(simWNeighbours.get(neighbourIndex),simONeighbours.get(neighbourIndex));
					else if (p.equals("1"))
						result =  Math.max(0,simWNeighbours.get(neighbourIndex) + simONeighbours.get(neighbourIndex) -1);
					else if (p.equals("2"))
						result = 1 - Math.min(1, Math.sqrt(Math.pow((1 - simWNeighbours.get(neighbourIndex)), 2) + Math.pow((1 - simONeighbours.get(neighbourIndex)), 2)));
				}
				else
					result = simONeighbours.get(neighbourIndex);

				neighbours.put(neighbourIndex, result);
			}

			for (Map.Entry<Integer,Double> entry : simWNeighbours.entrySet()) 
			{
				Double result = 0.0;

				Integer neighbourIndex = entry.getKey();

				if( neighbourIndex == -1 ) // den exei geitones
				{
					neighbours.put(-1, 0.0);
					break;
				}

				if( simONeighbours.containsKey(neighbourIndex) )
				{
					continue;
				}
				else
					result = simWNeighbours.get(neighbourIndex);

				neighbours.put(neighbourIndex, result);
			}

			similarities_aggregated.put(userIndex, neighbours);

		}

//		if (p.equals("infinity"))
//		{
//			for (int i = 0; i < u; i++)
//			{
//				for (int j = 0; j < u; j++)
//				{
//					similarities_aggregated[i][j] = Math.min(sim_w[i][j], sim_o[i][j]);
//				}
//			}
//		}
//		else if (p.equals("1"))
//		{
//			for (int i = 0; i < u; i++)
//			{
//				for (int j = 0; j < u; j++)
//				{
//					similarities_aggregated[i][j] = Math.max(0, (sim_w[i][j] + sim_o[i][j] - 1));
//				}
//			}
//		}
//		else if (p.equals("2"))
//		{
//			for (int i = 0; i < u; i++)
//			{
//				for (int j = 0; j < u; j++)
//				{
//					similarities_aggregated[i][j] = 1 - Math.min(1, Math.sqrt(Math.pow((1 - sim_w[i][j]), 2) + Math.pow((1 - sim_o[i][j]), 2)));
//				}
//			}
//		}
		return true;
	}
}

	/**
	 *
	 * @param sim_w Similarities weighted matrix to calculate similaries from
	 * @param sim_o Similarities overall matrix to calculate similaries from
	 * @param p Value "1", "2" or "infinity". Used to decide the intersection connectives
	 * @return Returns true
	 */
//	public boolean CalculateSimilarities_Aggregated_Intersection(double[][] sim_w, double[][] sim_o, String p)
//	{
//		int u = sim_w.length;
//		similarities_aggregated = new double[u][u];
//
//		if (p.equals("infinity"))
//		{
//			for (int i = 0; i < u; i++)
//			{
//				for (int j = 0; j < u; j++)
//				{
//					similarities_aggregated[i][j] = Math.min(sim_w[i][j], sim_o[i][j]);
//				}
//			}
//		}
//		else if (p.equals("1"))
//		{
//			for (int i = 0; i < u; i++)
//			{
//				for (int j = 0; j < u; j++)
//				{
//					similarities_aggregated[i][j] = Math.max(0, (sim_w[i][j] + sim_o[i][j] - 1));
//				}
//			}
//		}
//		else if (p.equals("2"))
//		{
//			for (int i = 0; i < u; i++)
//			{
//				for (int j = 0; j < u; j++)
//				{
//					similarities_aggregated[i][j] = 1 - Math.min(1, Math.sqrt(Math.pow((1 - sim_w[i][j]), 2) + Math.pow((1 - sim_o[i][j]), 2)));
//				}
//			}
//		}
//		return true;
//	}
//}

