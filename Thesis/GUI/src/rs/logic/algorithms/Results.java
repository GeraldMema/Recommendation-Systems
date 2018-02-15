/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.logic.algorithms;

/**
 * Class that extends Task, used from the JavaFX application running
 * concurrently with the interaction of the user
 *
 * @author georgia
 * @see RecommenderSystem
 */
public class Results {

   private double averageError;
   private double averagePrecision;
   private double averageRecall;
   private double averageAccuracy;
   private double FMeasure;
   private boolean Error;

   public double getAverageError() {
      return averageError;
   }

   public double getRoundedAverageError(int number_of_demicals) {
      double round = Math.pow(10, number_of_demicals);
      double roundNumber = Math.round(averageError * round);
      return Math.ceil(roundNumber) / round;
   }

   public double getAveragePrecision() {
      return averagePrecision;
   }

   public double getRoundedAveragePrecision(int number_of_demicals) {
      double round = Math.pow(10, number_of_demicals);
      double roundNumber = Math.round(averagePrecision * round);
      return Math.ceil(roundNumber) / round;
   }

   public double getAverageRecall() {
      return averageRecall;
   }

   public double getRoundedAverageRecall(int number_of_demicals) {
      double round = Math.pow(10, number_of_demicals);
      double roundNumber = Math.round(averageRecall * round);
      return Math.ceil(roundNumber) / round;
   }

   public double getAverageAccuracy() {
      return averageAccuracy;
   }

   public double getRoundedAverageAccuracy(int number_of_demicals) {
      double round = Math.pow(10, number_of_demicals);
      double roundNumber = Math.round(averageAccuracy * round);
      return Math.ceil(roundNumber) / round;
   }

   public double getFMeasure() {
      return FMeasure;
   }

   public double getRoundedFMeasure(int number_of_demicals) {
      double round = Math.pow(10, number_of_demicals);
      double roundNumber = Math.round(FMeasure * round);
      return Math.ceil(roundNumber) / round;
   }

   public boolean getError() {
      return Error;
   }

   public Results(double averageError, double averagePrecision,
           double averageRecall, double averageAccuracy, double FMeasure) {
      this.averageError = averageError;
      this.averagePrecision = averagePrecision;
      this.averageRecall = averageRecall;
      this.averageAccuracy = averageAccuracy;
      this.FMeasure = FMeasure;
      Error = false;
   }

   public Results() {
      Error = true;
   }

   public static void main(String[] args) {
      Results re = new Results(1.234, 1.235, 1.2355, 1.2355, 1.2354);
      System.out.println(re.getAverageAccuracy() + " " + re.getRoundedAverageAccuracy(3));
   }
}
