package rs.logic.algorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import rs.gui.functionality.Options;
import rs.logic.staticvariables.Predicted;

/**
 * Class that extends Task, used from the JavaFX application running
 * concurrently with the interaction of the user
 *
 * @author georgia
 * @see RecommenderSystem
 */
public class Worker extends Task {

   private String statisticsFile = "statistics_file.txt";
   private String encoding = "UTF-8";
   private PrintWriter writer;

   public Worker() throws FileNotFoundException, UnsupportedEncodingException {
      writer = new PrintWriter(statisticsFile, encoding);
   }

   /**
    *
    * @return Reruns null as a Void Object
    */
   @Override
   protected Void call() {
      double i = 0.0, loops;
      int numberOfLoops = 1;
      boolean multicriteria = false, slopeOne = false;
      int size = Options.list_step1.size() * Options.list_step2.size() * Options.list_step3.size()
              * Options.list_step4.size() * Options.list_step5.size() + 2;
      double step = 1.0 / (size * 3);
      boolean firstTime = true;

      try {
         //Create directory to write results
         File dir = createDirectory("Result");
         //Create file to write results
         File tagFileResults = createFileUnderDirectory(dir, "average_errors");

         FileWriter fstreamResults = new FileWriter(tagFileResults);
         BufferedWriter outResults = new BufferedWriter(fstreamResults);

         Options.actiontarget.setText("Program is running..\n\n");
         Options.pi.setVisible(true);
         updateProgress((long) 0, (long) 1.0);
         int counter = 0;

         //Get all the combinations of the options specified
         for (int a1 = 0; a1 < Options.list_step1.size(); a1++) {
            for (int a2 = 0; a2 < Options.list_step2.size(); a2++) {
               for (int a3 = 0; a3 < Options.list_step3.size(); a3++) {
                  for (int a4 = 0; a4 < Options.list_step4.size(); a4++) {
                     for (int a5 = 0; a5 < Options.list_step5.size(); a5++) {

                        if ((a1 == Options.list_step1.size() - 1) && (a2 == Options.list_step2.size() - 1) && (a3 == Options.list_step3.size() - 1)
                                && (a4 == Options.list_step4.size() - 1) && (a5 == Options.list_step5.size() - 1)) {
                           numberOfLoops = 3;
                        }

                        for (loops = 0; loops < numberOfLoops; loops++) {

                           if (isCancelled()) {
                              Options.actiontarget.setText(Options.actiontarget.getText().concat("Algorithm was canceled!\n"));
                              return null;
                           }

                           counter++;
                           if (loops == 0) {
                              if (firstTime) {
                                 Options.actiontarget.setText(Options.actiontarget.getText().concat("1. Multi-step multi-criteria Algorithm was started!\n\n"));
                                 writer.println("1. Multi-step multi-criteria Algorithm was started!\n\n");
                                 firstTime = false;
                              }
                           } else if (loops == 1) {  //Multicriteria algorithm
                              multicriteria = true;
                              slopeOne = false;
                              Options.actiontarget.setText(Options.actiontarget.getText().concat("2. Decomposing multi-criteria Algorithm was started!\n\n"));
                              writer.println("2. Decomposing multi-criteria Algorithm was started!\n\n");
                           } else {                   //slope one algorithm
                              slopeOne = true;
                              multicriteria = false;
                              Options.actiontarget.setText(Options.actiontarget.getText().concat("3. Weighted Slope One Algorithm was started!\n\n"));
                              writer.println("3. Weighted Slope One Algorithm was started!\n\n");
                           }


                           //Progress bar update
                           i += step;
                           if (i > 1.0) {
                              i = 1.0;
                           }
                           updateProgress((long) i, (long) 1.0);
                           Options.pbt.setText(Math.round(i * 100) + "%");
                           //End of progress bar update

                           long startTime = System.currentTimeMillis();
                           
                           boolean sameDb = Options.db_url.equals(Options.db_url_prev);
                           Results result = RecommenderSystem.RunAlgorithm(!sameDb, Options.db_url,
                                   Options.db_username, Options.db_password,
                                   Options.list_step1.get(a1), Options.list_step2.get(a2), Options.list_step3.get(a3),
                                   Options.list_step4.get(a4), Options.list_step5.get(a5), Options.comb_of_n, Options.max_comb,
                                   multicriteria, slopeOne);

                           long endTime = System.currentTimeMillis();
                           long totalTime = endTime - startTime;
                           
                           if(slopeOne){
                           System.out.println("duration slope one: " + totalTime);
                           }else if(multicriteria){
                              System.out.println("duration multiciteria: " + totalTime);
                           }else{
                              System.out.println("duration xamo: " + totalTime);
                           }


                           if (result.getError() != true) {
                              if (multicriteria == false && slopeOne == false) {
                                 Options.actiontarget.setText(Options.actiontarget.getText().concat("1." + counter + ". Algortihm run with options: \n"));
                                 Options.actiontarget.setText(Options.actiontarget.getText().concat("\tStep1. " + Options.list_step1.get(a1) + "\n"));
                                 Options.actiontarget.setText(Options.actiontarget.getText().concat("\tStep2. " + Options.list_step2.get(a2) + "\n"));
                                 Options.actiontarget.setText(Options.actiontarget.getText().concat("\tStep3. " + Options.list_step3.get(a3) + "\n"));
                                 Options.actiontarget.setText(Options.actiontarget.getText().concat("\tStep4. " + Options.list_step4.get(a4) + "\n"));
                                 Options.actiontarget.setText(Options.actiontarget.getText().concat("\tStep5. " + Options.list_step5.get(a5) + "\n"));

                                 writer.println("1." + counter + ". Algortihm run with options:");
                                 writer.println("\tStep1. " + Options.list_step1.get(a1) + "\n");
                                 writer.println("\tStep2. " + Options.list_step2.get(a2) + "\n");
                                 writer.println("\tStep3. " + Options.list_step3.get(a3) + "\n");
                                 writer.println("\tStep4. " + Options.list_step4.get(a4) + "\n");
                                 writer.println("\tStep5. " + Options.list_step5.get(a5) + "\n");
                              }
                              Options.actiontarget.setText(Options.actiontarget.getText().concat("\n\tResults:\n"));
                              Options.actiontarget.setText(Options.actiontarget.getText().concat("\t\t " + "Average error\u0020\u0020\u0020\u0020\u0020\u0020\u0020: " + result.getAverageError() + "\n "));
                              Options.actiontarget.setText(Options.actiontarget.getText().concat("\t\t " + "Average precision : " + result.getAveragePrecision() + "\n "));
                              Options.actiontarget.setText(Options.actiontarget.getText().concat("\t\t " + "Average recall\u0020\u0020\u0020\u0020\u0020\u0020: " + result.getAverageRecall() + "\n"));
                              Options.actiontarget.setText(Options.actiontarget.getText().concat("\t\t " + "FMeasure\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020: " + result.getFMeasure() + "\n"));
                              Options.actiontarget.setText(Options.actiontarget.getText().concat("\t\t " + "Average accuracy\u0020: " + result.getAverageAccuracy() + "\n\n"));

                              writer.println("\tResults:");
                              writer.println("\t\t " + result.getRoundedAverageError(3));
                              writer.println("\t\t " + result.getRoundedAveragePrecision(3));
                              writer.println("\t\t " + result.getRoundedAverageRecall(3));
                              writer.println("\t\t " + result.getRoundedFMeasure(3));
                              writer.println("\t\t " + result.getRoundedAverageAccuracy(3) + "\n");
                           } else {
                              Options.actiontarget.setText(Options.actiontarget.getText().concat("An error occured. Check your specified options.\n"));
                              writer.println("An error occured. Check your specified options.\n");
                              return null;
                           }

                           //Progress bar update
                           i += step;
                           if (i > 1.0) {
                              i = 1.0;
                           }
                           updateProgress((long) i, (long) 1.0);
                           Options.pbt.setText(Math.round(i * 100) + "%");
                           //End of progress bar update

                           //Create files to write the predictions
                           String filename = (a1 + 1) + "_" + (a2 + 1) + "_" + (a3 + 1) + "_" + (a4 + 1) + "_" + (a5 + 1) + "_";
                           File tagFile = createFileUnderDirectory(dir, filename);

                           // Create filewriter 
                           //Write prediction results to file
                           FileWriter fstream = new FileWriter(tagFile);
                           BufferedWriter out = new BufferedWriter(fstream);
                           double[][] pr;
                           pr = Predicted.getPredicted();
                           out.write("=======================================================================================\n");
                           out.write("File containing predicted results while running algorthim with options:\n");
                           out.write("\tStep1. " + Options.list_step1.get(a1) + "\n");
                           out.write("\tStep2. " + Options.list_step2.get(a2) + "\n");
                           out.write("\tStep3. " + Options.list_step3.get(a3) + "\n");
                           out.write("\tStep4. " + Options.list_step4.get(a4) + "\n");
                           out.write("\tStep5. " + Options.list_step5.get(a5) + "\n");
                           out.write("=======================================================================================\n\n");
                           for (int u = 0; u < pr.length; u++) {
                              for (int j = 0; j < pr[0].length; j++) {
                                 out.write("Predicted rating of user " + u + " and item " + j + " :  " + pr[u][j] + "\r\n");
                              }
                           }
                           //Close the output stream
                           out.close();
                           //End of writting to file

                           //Write to file final average error for this combination of algorithms
                           outResults.write(counter + ". Algortihm run with options: \r\n");
                           outResults.write("\tStep1. " + Options.list_step1.get(a1) + "\r\n");
                           outResults.write("\tStep2. " + Options.list_step2.get(a2) + "\r\n");
                           outResults.write("\tStep3. " + Options.list_step3.get(a3) + "\r\n");
                           outResults.write("\tStep4. " + Options.list_step4.get(a4) + "\r\n");
                           outResults.write("\tStep5. " + Options.list_step5.get(a5) + "\r\n");
                           outResults.write("\tResult: " + result + "\r\n");

                           //Progress bar update
                           i += step;
                           if (i > 1.0) {
                              i = 1.0;
                           }
                           updateProgress((long) i, (long) 1.0);
                           Options.pbt.setText(Math.round(i * 100) + "%");
                           //End of progress bar update

                           if (isCancelled()) {
                              Options.actiontarget.setText(Options.actiontarget.getText().concat("Algorithm was canceled!\n\n"));
                              return null;
                           }
                        }
                     }
                  }
               }
            }
         }


         writer.close();
         //Close output stream of results
         outResults.close();

         //Progress bar update
         i = 1.0;
         updateProgress((long) i, (long) 1.0);
         Options.pbt.setText(Math.round(i * 100) + "%");
         //End of progress bar update

         Options.actiontarget.setText(Options.actiontarget.getText().concat("Finished!\n\n"));
         Options.db_url_prev = Options.db_url;
      } catch (SQLException ex) {
         Options.actiontarget.setText(Options.actiontarget.getText().concat("SQL error: " + ex + "\n"));
         Logger.getLogger(RecommenderSystem.class.getName()).log(Level.SEVERE, null, ex);
      } catch (InstantiationException ex) //JDBC driver exception thrown from (Class.forName("com.mysql.jdbc.Driver").newInstance();)
      {
         Options.actiontarget.setText(Options.actiontarget.getText().concat("JDBC driver error: " + ex + "\n"));
         Logger.getLogger(RecommenderSystem.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalAccessException ex) //JDBC driver exception thrown from (Class.forName("com.mysql.jdbc.Driver").newInstance();)
      {
         Options.actiontarget.setText(Options.actiontarget.getText().concat("JDBC driver error: " + ex + "\n"));
         Logger.getLogger(RecommenderSystem.class.getName()).log(Level.SEVERE, null, ex);
      } catch (ClassNotFoundException ex) //JDBC driver exception thrown from (Class.forName("com.mysql.jdbc.Driver").newInstance();)
      {
         Options.actiontarget.setText(Options.actiontarget.getText().concat("JDBC driver error: " + ex + "\n"));
         Logger.getLogger(RecommenderSystem.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Options.actiontarget.setText(Options.actiontarget.getText().concat("IO exception error: " + ex + "\n"));
         Logger.getLogger(RecommenderSystem.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         Options.pi.setVisible(false);
      }

      return null;
   }

   private File createDirectory(String path) {
      File dir = new File(path);
      if (!dir.exists()) {
         dir.mkdir();
      }
      return dir;
   }

   private File createFileUnderDirectory(File dir_path, String filename) throws IOException {
      File tagFile = new File(dir_path, filename + ".txt");
      if (!tagFile.exists()) {
         tagFile.createNewFile();
      } else {
         tagFile.delete();
         tagFile.createNewFile();
      }

      return tagFile;
   }
}
