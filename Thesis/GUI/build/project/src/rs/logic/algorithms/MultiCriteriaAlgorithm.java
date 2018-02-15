/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.logic.algorithms;

import rs.logic.algorithms.general.Normalize0to1;
import rs.logic.staticvariables.Dataset;

/**
 * Class that extends Task, used from the JavaFX application running
 * concurrently with the interaction of the user
 *
 * @author georgia
 * @see RecommenderSystem
 */
public class MultiCriteriaAlgorithm {
   
   public static double[][] Algorithm(int[][][] newdataset, double[][] coefficients) {

      int rating, item, i, j, avg, sumZ, result;
      double avgrating1, sum1, sum2, sum3, similarity, z, sum;
      int ratings = Dataset.getZSize();
      int users = Dataset.getXSize();
      int items = Dataset.getYSize();
      double[][] similarities = new double[users][users]; // pinakas pou krataei omoiothtes xrhsth pros xrhsth
      double[] avgrating = new double[users]; // mesos oros twn va8mwn pou exei dwsei o xrhsths i sto item gia to epimerous krhthrio
      double [][] predictions = new double[users][items];
      
      //Step 1
      for (rating = 1; rating < ratings; rating++) { //loop gia ta epimerous kritiria
      	//dhladh gia ka8e epimerous krhthrio gia ka8e xrhsth
         for (i = 0; i < users; i++) {           
            avgrating1 = 0; // a8roisma twn va8mwn sto epimerous krhthrio tou xrhsth gia ta item pou exei va8mologhsei
            avg = 0; // plh8os twn item pou exei va8mologhsei se auto to epimerous krhthrio
            
            for (item = 0; item < items; item++) {
            
            	// an o xrhsths gia to antikeimeno exei dwsei va8mologeia
               if (newdataset[i][item][rating] != -1) // to i antiproswpeuei ton user 
               {             
                  avgrating1 += newdataset[i][item][rating]; 
                  avg++;               
               }
            }          
            avgrating1 = avgrating1 / avg;
            avgrating[i] = avgrating1;
         }

         //ypologismos similarities twn users
         for (i = 0; i < users; i++) {
            sum1 = 0;
            sum2 = 0;
            sum3 = 0;
            //me8odos Pearson correlation based similarity
            for (j = i + 1; j < users; j++) 
            {

               for (item = 0; item < items; item++) 
               {
                  if (newdataset[i][item][rating] != -1 && newdataset[j][item][rating] != -1) {
                  	//auroisma r_u,i - r_u meso
//                  	int r_u_i = newdataset[i][item][rating]; // to evala egw
//                  	int r_u_j = newdataset[j][item][rating]; // to evala egw
//                  	int r_u_meso_i = avgrating[i] ; // to evala egw
//                  	int r_u_meso_j = avgrating[j] ; // to evala egw
//                  	sum1 += (r_u_i - r_u_meso_i) * (r_u_j - r_u_meso_j); // to evala egw
//                  	sum2 += (r_u_i - r_u_meso_i) * (r_u_i - r_u_meso_i); // to evala egw
//                  	sum3 += (r_u_j - r_u_meso_j) * (r_u_j - r_u_meso_j); // to evala egw
					 sum1 += (newdataset[i][item][rating] - avgrating[i]) * (newdataset[j][item][rating] - avgrating[j]);
					 sum2 += (newdataset[i][item][rating] - avgrating[i]) * (newdataset[i][item][rating] - avgrating[i]);
					 sum3 += (newdataset[j][item][rating] - avgrating[j]) * (newdataset[j][item][rating] - avgrating[j]);
                  }
               }
	           
               if (sum1 == 0 && (sum2 == 0 || sum3 == 0)) {
                  similarity = -1;
               } else {
                  similarity = sum1 / (Math.sqrt(sum2) * Math.sqrt(sum3));
               }
               Normalize0to1 normal = new Normalize0to1();
               similarity = normal.Normalize_value0to1(similarity, -1, 1);     //Normalize value to range [0,1] cause we got before [-1,1]
               similarities[i][j] = similarity;
               similarities[j][i] = similarity;
            }
         }

         //Diadiakasia eureshs agnwstwn ratings
         for (i = 0; i < users; i++) 
         {         
            for (item = 0; item < items; item++) 
            {
               if (newdataset[i][item][rating] == -1) //aprosdioristo rating 
               {                  
                  sumZ = 0;
                  sum = 0;
                  for (j = 0; j < users; j++) {                 
                     if (newdataset[j][item][rating] != -1) {
                        sumZ += similarities[i][j];
                        sum += similarities[i][j] * (newdataset[j][item][rating] - avgrating[j]);
                     }
                  }
    
                  if(sumZ != 0)
                  {     
                     // System.out.println("sumZ = " + sumZ);            
                     z = 1 / sumZ;                  
                  }
                  else
                  {   
                     z = 0;
                  }                                    
                  newdataset[i][item][0] = (int)Math.round(avgrating[i] + z * sum); 
               }                                                                            
            }
         }      
      }
      //Step 2 Linear Regration
      //Step 3
      for (i = 0; i < users; i++) {
         for (item = 0; item < items; item++) {
            if (newdataset[i][item][0] == -1) {
               sum = coefficients[i][0];
               for (rating = 1; rating < ratings; rating++) {
                  sum += coefficients[i][rating] * newdataset[i][item][rating];
               }
               newdataset[i][item][0] = (int)Math.round(sum); 
            }
            predictions[i][item] =  newdataset[i][item][0]; // auto to evala egw edw ginotan apo katw 8/3/16
         }
      }
      
      // for(i=0;i<users;i++){
      //    for(item=0;item<items;item++){
      //       predictions[i][item] = newdataset[i][item][0];
      //    }
      // }    
      return predictions;
   }
}
