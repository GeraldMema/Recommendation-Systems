package rs.logic.algorithms;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.logic.algorithms.WeightedSlopeOne;
import rs.logic.algorithms.general.Combinations;
import rs.logic.algorithms.steps.CalculateSimilarities;
import rs.logic.algorithms.steps.LinearRegression;
import rs.logic.algorithms.steps.PredictedRatings;
import rs.logic.staticvariables.Coefficients;
import rs.logic.staticvariables.Dataset;
import rs.logic.staticvariables.Predicted;
import rs.logic.staticvariables.Similarities;

import ann_decision_version.LSH;
import Database.Users;

/**
 * Contains RunAlgorithm() Method that executes the steps of the Recommender
 * System
 *
 * @author georgia
 * @see LinearRegression
 * @see CalculateSimilarities
 * @see PredictedRatings
 */
public class RecommenderSystem {

	private static List<List<int[]>> list = null;       //List of lists of the reviews of users on hotels
	private static double recommending_percentage = 0.70;
	private static WeightedSlopeOne weightedSlopeOne;

	/**
	 * @param newurl Used for checking if a new database was inserted
	 * @param db_url Contains the database url
	 * @param db_username Contains the username of the database
	 * @param db_password Contains the password of the database
	 * @param regression_algorithm Contains the option value of the regression
	 * algorithm to be used
	 * @param similarity_computation_weighted Contains the option value of the
	 * similarity computation weighted to be used
	 * @param similarity_computation_overall Contains the option value of the
	 * similarity computation overall to be used
	 * @param similarity_computation_aggregated Contains the option value of the
	 * similarity computation aggregated to be used
	 * @param prediction_computation Contains the option value of the prediction
	 * computation to be used
	 * @param combinations_of_n Contains the value of the combinations of n items
	 * out of a set bigger that n items
	 * @param max_combinations Contains the maximum combinations to be calculated
	 * @return Returns the average error of the algorithm, or NaN if there was
	 * any problem
	 * @throws SQLException
	 * @throws InstantiationException Exception thrown from the jdbc driver
	 * @throws IllegalAccessException Exception thrown from the jdbc driver
	 * @throws ClassNotFoundException Exception thrown from the jdbc driver
	 */
	public static Results RunAlgorithm(boolean newurl, String db_url, String db_username, String db_password,
			String regression_algorithm, String similarity_computation_weighted, String similarity_computation_overall,
			String similarity_computation_aggregated, String prediction_computation, int combinations_of_n, int max_combinations,
			boolean multicriteria, boolean slopeOne) throws SQLException, InstantiationException, IllegalAccessException,
	ClassNotFoundException {

		int Nrs, Nis, Nrn, Nin;
		double sumP, averageP, finalAverageP, sumR, averageR, finalAverageR;
		double sumAccuracy, averageAccuracy, finalAverageAccuracy, FMeasure;
		double[][] predicted;

		if (db_url.equals("") || regression_algorithm.equals("") || similarity_computation_weighted.equals("") || similarity_computation_overall.equals("") || similarity_computation_aggregated.equals("") || prediction_computation.equals("") || combinations_of_n <= 0 || max_combinations <= 0) {
			Results results = new Results();
			return results;
		}
		//Create static variable of a Dataset class that contains our dataset values
		if (!Dataset.datasetExists() || (Dataset.datasetExists() && newurl == true)) {
			Dataset.CreateDataset(db_url, db_username, db_password);
			Dataset.setNewdatasetWithDataset();
		}
		int[][][] dt = Dataset.getDataset();

		//pinaka me aferemenes kapoies grammes gia na epistrepsei kala apotelesmata h grammikh palindromish
		int[][][] upgradedDt = new int[dt.length][dt[0].length][dt[0][0].length];

		for (int i = 0; i < dt.length; i++) {
			for (int j = 0; j < dt[i].length; j++) {
				System.arraycopy(dt[i][j], 0, upgradedDt[i][j], 0, dt[i][j].length);
			}
		}

		int users = Dataset.getXSize();     //Number of users in the dataset
		int items = Dataset.getYSize();    //Number of items in the dataset
		int ratings = Dataset.getZSize();    //Number of ratings should be N = OVERALL + CRITERIA RATINGS
		int max_rating = Dataset.getMAX_R();    //Maximum rating that can be given on an item
		predicted = new double[users][items];

		if (slopeOne == true) {
			weightedSlopeOne = new WeightedSlopeOne(users, items);
		}

		double[] sum_error = new double[users];                 //Matrix with sum of errors of each user
		double[] average_error = new double[users];             //Matrix with average errors of each user
		double final_average_error = 0.0;                       //Final total error of te algorithm

		for (int i = 0; i < users; i++) //Inititalize errors to 0
		{
			sum_error[i] = 0;
			average_error[i] = 0;
		}


		if (newurl == true) //Check if there is new database checked
		{
			//Create a list of lists of the reviews of users on hotels
			list = new ArrayList<List<int[]>>();
			for (int i = 0; i < users; i++) {

				upgradedDt[i] = Users.PetakseIdiesGrammes(upgradedDt[i]);

				//Each user has a list of his reviews to the hotels
				List<int[]> l = new ArrayList<int[]>();
				for (int j = 0; j < dt[i].length; j++) {
					if (dt[i][j][0] != -1) {
						int[] t = new int[2];                  //We add an matrix with the value of the hotel and one more value needed if we use it for the pediction or not
						t[0] = j;
						t[1] = 0;                       //0(zero) indicates that it's not yet used, -1 indicates used
						l.add(t);
					}
				}
				list.add(l);
			}
		}

		Double averageOverallArray[] = new Double[users];

		for (int i = 0; i < users; i++) {

			double overallSum = 0;
			double itemsRatedCounter = 0;
			for (int j = 0; j < items; j++)
			{			
				if( dt[i][j][0] != -1 )
				{
					overallSum += dt[i][j][0];
					itemsRatedCounter ++;
				}
			}
			averageOverallArray[i] = overallSum/itemsRatedCounter; //ypologismos avg_overal gia kathe user
		}

		LSH lsh = new LSH();

		Map<Integer,int[]> users_common_items = new HashMap<Integer, int[]>();
		users_common_items = lsh.find_neigh_overal(users, items, dt, averageOverallArray);


		//		double averageOverallArray[] = new double[users];
		//		
		//		for (int i = 0; i < users; i++) 
		//		{
		//			double overallSum = 0;
		//			double itemsRatedCounter = 0;
		//			for (int j = 0; j < items; j++) 
		//			{
		//				if( dt[i][j][0] != -1 )
		//				{
		//					overallSum += dt[i][j][0];
		//					itemsRatedCounter++;
		//				}
		//			}
		//			
		//			averageOverallArray[i] = overallSum/itemsRatedCounter;
		//		}

		//		LSH lsh = new LSH();

		//initialize ton lsh me 4 hashTables
		lsh.initialize(4);
		lsh.initializeOveralls(4);

		//Now we have the list with the reviewed hotels
		//We need to choose randomly 8 of the reviews
		averageP = 0;
		averageR = 0;
		averageAccuracy = 0;
		for (int i = 0; i < users; i++) {

			List<int[]> l = list.get(i);
			int length = l.size();

			int[][][] newdataset = Dataset.getNewdataset();

			int[][][] upgradedNewdataset = new int[newdataset.length][][];//[newdataset[0].length][newdataset[0][0].length];

			for (int q=0; q<users; q++) {
				upgradedNewdataset[q] = Users.PetakseIdiesGrammes(newdataset[q]);
			}


			int c = combinations_of_n;
			int n = length;
			int a[] = new int[c];
			List<int[]> comb_list = new ArrayList<int[]>();

			//Find all the combinations
			Combinations com = new Combinations();
			com.setCount(0);                     //We need to make it 0(zero) on each loop
			com.findCombinations(n, c, 0, a, c, comb_list, max_combinations);

			int counter_null = 0;
			int comb_list_length = comb_list.size();
			sumP = 0;
			sumR = 0;
			sumAccuracy = 0;

			for (int m = 0; m < comb_list_length; m++) 
			{
				//Make specific user ratings -1 so we take later only what we need
				for (int j = 0; j < dt[i].length; j++) 
				{
					for (int k = 0; k < ratings; k++) 
					{
						newdataset[i][j][k] = -1;
					}
				}

				//Set the combination we found to the newdataset
				for (int v = 0; v < c; v++) {
					for (int k = 0; k < ratings; k++) {
						newdataset[i][l.get(comb_list.get(m)[v])[0]][k] = dt[i][l.get(comb_list.get(m)[v])[0]][k];
					}
				}

				for(int user = 0; user < users ; user++ )
				{
					lsh.generateCentroids(upgradedNewdataset[user], Dataset.getUserId(user),null);
				}

				//Set lists items to -1 so that we know they are used
				int[] tt;
				for (int v = 0; v < c; v++) {
					tt = l.get(comb_list.get(m)[v]);
					tt[1] = -1;
					l.set(comb_list.get(m)[v], tt); //set it to -1 so that we know it's used
				}

				//Now we have the newdata matrix with the data we need only
				//So we can calculate the predicted values of the "unrated" hotels of the specific user in this section
				//Step1
				//Create matrices needed to be passed to the linearregression function
				if (newurl == true) //Check if new database, so we don't reallocate memory for no reason
				{
					Coefficients.Create_Coefficients_Matrix(users, ratings);
				}

				long startTime = System.currentTimeMillis();

				if (regression_algorithm.equals("Linear Regression")) {
					double[] coef = null;
					for (int w = 0; w < users; w++) //We pass the matrices for each user every time
					{

						int rated_counter = 0;
						for (int j = 0; j < upgradedDt[w].length; j++) {
							if (upgradedNewdataset[w][j][0] != -1) {
								rated_counter++;
							}
						}

						if (rated_counter != 0) {

							//							System.out.println("rated_counter = " + rated_counter + " userID = " + Dataset.getUserId(w));

							int N = rated_counter;
							double[] y = new double[N];             //Are the overall ratings
							double[][] x = new double[ratings][N];        //x[>0][i] are the multi-ratings except the overall
							double[] weight = new double[N];
							int u = 0;

							if(w == i)
							{
								int rated_counter2 = 0;
								for (int j = 0; j < dt[w].length; j++) {
									if (newdataset[w][j][0] != -1) {
										rated_counter2++;
									}
								}

								N = rated_counter2;
								y = new double[N];
								x = new double[ratings][N];
								weight = new double[N];
								u = 0;

								for (int j = 0; j < dt[w].length; j++) {
									if (newdataset[w][j][0] != -1) {
										y[u] = newdataset[w][j][0];
										x[0][u] = 1;
										for (int k = 1; k < ratings; k++) {
											x[k][u] = newdataset[w][j][k];
										}
										weight[u] = 1;
										u++;
									}
								}
							}
							else
							{
								for (int j = 0; j < upgradedDt[w].length; j++) {
									if (upgradedNewdataset[w][j][0] != -1) {
										y[u] = upgradedNewdataset[w][j][0];
										x[0][u] = 1;
										for (int k = 1; k < ratings; k++) {
											x[k][u] = upgradedNewdataset[w][j][k];
										}
										weight[u] = 1;
										u++;
									}
								}
							}

							LinearRegression lr = new LinearRegression();
							lr.regress(y, x, weight);
							coef = lr.getC();      //Get the Coefficients matrix from the LinearRegression Class

							Integer userId = Dataset.getUserId(w);

							if(coef != null)
							{
								double sum = 0;
								for (int j = 0; j < coef.length; j++) {
									sum += coef[j];
								}
								if(sum == 0)
								{
									coef = null;
								}
							}
							
							if (coef == null) {
								
								int[][] kainourgiesVa8mologies = lsh.getRatingsFromNeighbour(lsh.getCentroid(userId), upgradedNewdataset[w], userId);

								while((kainourgiesVa8mologies.length < 1.5* kainourgiesVa8mologies[0].length))
								{

									lsh.initialize(4);		

									for(int user = 0; user < users ; user++ )
									{
										lsh.generateCentroids(upgradedNewdataset[user], Dataset.getUserId(user),lsh.getCentroid(user));
									}

									kainourgiesVa8mologies = lsh.getRatingsFromNeighbour(lsh.getCentroid(userId),kainourgiesVa8mologies,userId);

								}

								N = kainourgiesVa8mologies.length;
								y = new double[N];             //Are the overall ratings
								x = new double[ratings][N];        //x[>0][i] are the multi-ratings except the overall
								weight = new double[N];
								u = 0;
								for (int j = 0; j < N; j++) {
									if (kainourgiesVa8mologies[j][0] != -1) {
										y[u] = kainourgiesVa8mologies[j][0];
										x[0][u] = 1;
										for (int k = 1; k < ratings; k++) {
											x[k][u] = kainourgiesVa8mologies[j][k];
										}
										weight[u] = 1;
										u++;
									}
								}

								lr.regress(y, x, weight);
								coef = lr.getC();      //Get the Coefficients matrix from the LinearRegression Class

								if( coef == null)
									break;
							}

							Coefficients.setCoefficientsAtIndex(w, coef);

//							lsh.addOverall(averageOverallArray[w]*coef[0], w);
						}
					}
					if (coef == null) {
						System.out.println("coef = NULL");

						//If the was a problem we have to set the user's data that we changed back to normal
						//So we can calculate for the next user
						for (int j = 0; j < items; j++) {
							System.arraycopy(dt[i][j], 0, newdataset[i][j], 0, ratings);
						}

						//Set the values of the list back to 0's
						for (int v = 0; v < c; v++) {
							tt = l.get(comb_list.get(m)[v]);
							tt[1] = 0;
							l.set(comb_list.get(m)[v], tt); //set it to -1 so that we know it's not used anymore
						}
						counter_null++;
						continue;
					}

				}

				dt = Dataset.getDataset();
				newdataset = Dataset.getNewdataset();

				long endTime = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				//            System.out.println("duration: " + totalTime);

				double[][] coe = Coefficients.getCoefficients();
				if (slopeOne == true) 
				{
					weightedSlopeOne.setRatings(newdataset);
					predicted = weightedSlopeOne.Algorithm();
				} 
				else if (multicriteria == true) 
				{
					predicted = MultiCriteriaAlgorithm.Algorithm(newdataset, coe);
				} 
				else 
				{
					// map pou antistoixei coef  me to userId tou
					Map<double[] , Integer > userIdToCoef = new HashMap<double[], Integer >();

					lsh.initializeCoef(4);

					int r = 0;
					for (double[] ds : coe) {
						lsh.addCoef(ds,r);
						userIdToCoef.put( ds ,r);
						r++;
					}

					Map<Integer,List<double[]>> neighboursByCoefsMap = new HashMap<Integer,List<double[]>>();

					for (int j = 0; j < users; j++) {

						neighboursByCoefsMap.put(j, lsh.getNeighboursByCoefsById(userIdToCoef, j,coe[j]));

					}

					//Step2
					//Create similarities table(three tables are created also for next steps) ,//Square matrix with similarities and diagonal has aces(1)
					CalculateSimilarities cl = new CalculateSimilarities();
					if (similarity_computation_weighted.equals("Weighted Cosine-based similarity")) 
					{
						cl.CalculateSimilarities_Weighted(users, ratings, neighboursByCoefsMap,userIdToCoef);
					} 
					else if (similarity_computation_weighted.equals("Weighted Pearson similarity"))//ean exei epilegei h pearson sto step2
					{
						cl.CalculateSimilarities_Weighted_Pearson(users, ratings, neighboursByCoefsMap,userIdToCoef);
					}

					Similarities.setSimilarities_weighted(cl.getSimilarities_weighted());


					Map<Integer,Map<Integer,Double>> sim_weighted = Similarities.getSimilarities_weighted();

					//					for (Map.Entry<Integer, Map<Integer,Double>> simWeight : sim_weighted.entrySet()) 
					//					{
					//						System.out.println("user " + simWeight.getKey() + " with neighbours");
					//						for (Map.Entry<Integer,Double> entry : simWeight.getValue().entrySet()) 
					//						{
					//							System.out.println("neighbourI: " + entry.getKey() + " with sim = " + entry.getValue());
					//						}
					//					}
					//					lsh.printCoefs();


					//Map apo userI->int[neighbourIndex,...,neighbourIndex]
//					Map<Integer,int[]> indexOfNeigboursMap = lsh.getNeighbourOverall();

					//					for (Integer index : indexOfNeigboursMap.keySet()) 
					//					{
					//						System.out.println("User : " + index);
					//						System.out.println(Arrays.toString(indexOfNeigboursMap.get(index)));
					//					}
					//					
					//					lsh.printOveralls();

					//Step3
					if (similarity_computation_overall.equals("Overall Cosine-based similarity")) {
						//						cl.CalculateSimilarities_Overall_Cosine(users, coe);
						cl.CalculateSimilarities_Overall_Cosine(users, coe,users_common_items);
					} else if (similarity_computation_overall.equals("Overall Pearson similarity")) {
						cl.CalculateSimilarities_Overall_Pearson(users, coe, users_common_items);
					}
					Similarities.setSimilarities_overall(cl.getSimilarities_overall());

					Map<Integer,Map<Integer,Double>> sim_overall = Similarities.getSimilarities_overall();

					//					for (Map.Entry<Integer, Map<Integer,Double>> entry : sim_overall.entrySet()) 
					//					{
					//						System.out.print("user : " + entry.getKey() + "with simOverall: ");
					//						for (Map.Entry<Integer, Double> geitonas : entry.getValue().entrySet()) 
					//						{
					//							System.out.println("geitonasI = " + geitonas.getKey() + "  sim = " + geitonas.getValue());
					//						}
					//						System.out.println();
					//					}

					//Step4
					if (similarity_computation_aggregated.equals("Multiplication similarities: simF = simW * simO")) 
					{
						cl.CalculateSimilarities_Aggregated_Multiply(sim_weighted, sim_overall);
					} 
					else if (similarity_computation_aggregated.equals("Intersection connectives infinity")) 
					{
						cl.CalculateSimilarities_Aggregated_Intersection(sim_weighted, sim_overall, "infinity");
					} 
					else if (similarity_computation_aggregated.equals("Intersection connectives 1")) 
					{
						cl.CalculateSimilarities_Aggregated_Intersection(sim_weighted, sim_overall, "1");
					} 
					else if (similarity_computation_aggregated.equals("Intersection connectives 2")) 
					{
						cl.CalculateSimilarities_Aggregated_Intersection(sim_weighted, sim_overall, "2");
					}
					Similarities.setSimilarities_aggregated(cl.getSimilarities_aggregated());

					Map<Integer,Map<Integer,Double>> sim_aggr = Similarities.getSimilarities_aggregated();

					//					for (Map.Entry<Integer, Map<Integer,Double>> simWeight : sim_aggr.entrySet()) 
					//					{
					//						System.out.println("user " + simWeight.getKey() + " with neighbours");
					//						for (Map.Entry<Integer,Double> entry : simWeight.getValue().entrySet()) 
					//						{
					//							System.out.println("neighbourI: " + entry.getKey() + " with sim = " + entry.getValue());
					//						}
					//					}

					//Step5
					PredictedRatings pr = new PredictedRatings();

					if (prediction_computation.equals("Weighted Sum Approach")) {
						pr.PredictRatings_Weighted_Sum_Approach(users, items, sim_aggr);
					} else if (prediction_computation.equals("Adjusted Weighted Sum Approach")) {
						pr.PredictRatings_Adjusted_Weighted_Sum_Approach(users, dt[i].length, sim_aggr);
					}
					Predicted.setPredicted(pr.getPredicted_values());

					predicted = Predicted.getPredicted();

				}
				//Calculate the error
				int unrated_counter = 0;
				double error;
				double P, R, Accuracy;
				double sum = 0.0;
				Nrs = 0;
				Nis = 0;
				Nrn = 0;
				Nin = 0;
				P = 0;
				R = 0;
				Accuracy = 0;

				for (int z = 0; z < length; z++) {

					if (l.get(z)[1] == 0 && dt[i][l.get(z)[0]][0] != -1) //Review not used
					{
						if (predicted[i][l.get(z)[0]] >= recommending_percentage * max_rating) {
							if (dt[i][l.get(z)[0]][0] >= recommending_percentage * max_rating) {
								Nrs++;
							} else {
								Nis++;
							}
						} else {
							if (dt[i][l.get(z)[0]][0] < recommending_percentage * max_rating) {
								Nrn++;
							} else {
								Nin++;
							}
						}
						sum += Math.abs(predicted[i][l.get(z)[0]] - dt[i][l.get(z)[0]][0]);
						unrated_counter++;
					}
				}
				if ((Nrs == 0) && (Nis == 0)) {           //means that all instances were predicted as negative
					P = 1;
				} else {
					P = Nrs / (Nrs + Nis);                      //Ypologismos precision ana user kai item combinationn
				}

				if ((Nrs == 0) && (Nrn == 0)) {           //means that there were no positive cases in the input data
					R = 1;
				} else {
					R = Nrs / (Nrs + Nrn);
				}
				Accuracy = (Nrs + Nin) / (Nrs + Nrn + Nis + Nin); //ypologismos accuracy ana user kai item combination
				sumAccuracy += Accuracy;                  //ypologismos accuracy ana user(ola ta combinations)
				sumP += P;                                //Ypologismos precision ana user(ola ta combination)
				sumR += R;                                //Ypologismos recall ana user(ola ta combination)
				error = sum / unrated_counter;          //Error of each combination
				sum_error[i] += error;                  //Sum of errors of each combination of the specific user
				//End

				//After the calculation we have to set the user's data that we changed back to normal
				//So we can calculate for the next user
				for (int j = 0; j < items; j++) {
					System.arraycopy(dt[i][j], 0, newdataset[i][j], 0, ratings);
				}

				//Set the values of the list back to 0's
				for (int v = 0; v < c; v++) {
					tt = l.get(comb_list.get(m)[v]);
					tt[1] = 0;
					l.set(comb_list.get(m)[v], tt); //set it to -1 so that we know it's not used anymore
				}
			}

			//Check if all combinations do not return any coefficients
			if (counter_null == comb_list_length) {
				return new Results();
			}
			average_error[i] = sum_error[i] / comb_list_length;                 //Average error of user i of all combinations errors
			averageP += sumP / comb_list_length;                                    //Athroisma tou mesou Precision gia olous tou xrhstes 
			averageR += sumR / comb_list_length;
			averageAccuracy += sumAccuracy / comb_list_length;
		}
		double sum = 0.0;
		for (int i = 0; i < users; i++) {
			sum += average_error[i];
		}
		final_average_error = sum / users;                                      //Final error of all average errors of all users
		finalAverageP = averageP / users;                                         //Ypologismos mesou presicion
		finalAverageR = averageR / users;
		finalAverageAccuracy = averageAccuracy / users;
		FMeasure = 2 * (finalAverageP * finalAverageR) / (finalAverageP + finalAverageR);
		Results results;
		results = new Results(final_average_error, finalAverageP, finalAverageR, finalAverageAccuracy, FMeasure);
		return results;
	}
}
