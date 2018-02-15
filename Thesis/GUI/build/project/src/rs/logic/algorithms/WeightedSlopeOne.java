/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.logic.algorithms;

/**
 *
 * @author georgia
 */
public class WeightedSlopeOne {

   //Dilwsh Metavlitwn 
   int number_of_users;      //arithmos xrhstwn 
   int number_of_items;      //arithmos antikeimenwn 
   //DilwshPinakwn 
   int[][][] ratings;     //pinakas vathomologiwn xristwn gia antikeimena
   double [][] results;
   double[][] deviations; //pinakas pareklisewn(deviations) 
   int[][] cards;      //pinakas arithmwn vathomologisewn 2 antikeimenwn 

   public WeightedSlopeOne(int numberOfUsers, int numberOfItems){

      int item1, item2;

      this.number_of_users = numberOfUsers;
      this.number_of_items = numberOfItems;
      //this.ratings = ratings;

      deviations = new double[numberOfItems][numberOfItems];
      cards = new int[numberOfItems][numberOfItems];
      results = new double[number_of_users][numberOfItems];
              
      //for (item1 = 0; item1 < numberOfItems; item1++) {
      //   for (item2 = 0; item2 < numberOfItems; item2++) {
      //      cards[item1][item2] = 0;
      //      deviations[item1][item2] = 0;
      //   }
      //}
   }
   
   public void setRatings(int [][][]ratings){
      int item1, item2;
      this.ratings = ratings;
      for (item1 = 0; item1 < number_of_items; item1++) {
         for (item2 = 0; item2 < number_of_items; item2++) {
            cards[item1][item2] = 0;
            deviations[item1][item2] = 0;
         }
      }
   }

   //H sunarthsh pou efarmozei thn Weighted Slope One  
   public double[][] Algorithm() {
int i,j;
for(i=0;i<number_of_users;i++){
for(j=0;j<number_of_items;j++){
results[i][j]=5;

}   
}
      
//      computeDeviations();
//      makePredictions();

      return results;
   }

   //Sunarthsh pou upologizei tis parekliseis metaksu twn antikeimenwn 
   private void computeDeviations() {

      int item1, item2, user;

      //Ypologismos twn arithwm vathmologisewn kathe diadas antikeimenwn 
      for (item1 = 0; item1 < number_of_items; item1++) {
         for (item2 = item1 + 1; item2 < number_of_items; item2++) {
            for (user = 0; user < number_of_users; user++) {
               if ((ratings[user][item1][0] != -1) && (ratings[user][item2][0] != -1)) {
                  cards[item1][item2]++;
                  cards[item2][item1]++;
               }
            }
         }
      }

      //Ypologismos pareklisewn 
      for (item1 = 0; item1 < number_of_items; item1++) {
         for (item2 = item1 + 1; item2 < number_of_items; item2++) {

            if (item1 == item2) {
               continue;
            }
            for (user = 0; user < number_of_users; user++) {
               if ((ratings[user][item1][0] != -1)
                       && (ratings[user][item2][0] != -1)) {
                  deviations[item1][item2] += (ratings[user][item1][0] - ratings[user][item2][0]) / cards[item1][item2];
                  deviations[item2][item1] += (ratings[user][item2][0] - ratings[user][item1][0]) / cards[item1][item2];
               }
            }
         }
      }
   }

   //Sunarthsh pou kanei tis provlepseis 
   public void makePredictions() {

      int user, item1, item2, sumCards;
      double prediction;

      for (user = 0; user < number_of_users; user++) {
         for (item1 = 0; item1 < number_of_items; item1++) {

            if (ratings[user][item1][0] != -1) {
               results[user][item1] = ratings[user][item1][0];
               continue;
            }

            prediction = 0;
            sumCards = 0;
            for (item2 = 0; item2 < number_of_items; item2++) {
               if (ratings[user][item2][0] != -1) {
                  prediction += (ratings[user][item2][0] + deviations[item1][item2]) * cards[item1][item2];
                  sumCards += cards[item1][item2];
               }
            }
            prediction /= sumCards;
            //Edw tha mpei i Norma!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
            //ratings[user][item1][0] = (int)Math.round(prediction); 
            //ratings[user][item1][0] = (int) Math.round(prediction);
            results[user][item1] = prediction;
         }
      }
   }
}