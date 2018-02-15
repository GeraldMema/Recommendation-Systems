package rs.gui.functionality;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import rs.logic.algorithms.Worker;

/**
 * Contains static variables for all the contents of the GUI options
 *
 * @author georgia
 */
public class Options {
   //Database Connection Info

   final private static String username = "root";
   final private static String password = "3110";
   //Thread
   public static Worker w;
   public static Thread th = null;
   //Database info
   final public static TextField database_url = new TextField("mysql://localhost:3306/trip_advisor_hotels");
   final public static TextField database_username = new TextField(username);
   final public static TextField database_password = new TextField(password);
   public static String db_url_prev = "";
   public static String db_url;
   public static String db_username;
   public static String db_password;
   //Combinations info
   final public static TextField combinations_of_n = new TextField("9");
   final public static TextField max_combinations = new TextField("50");
   public static int comb_of_n;
   public static int max_comb;
   //Step 1
   final public static CheckBox cb_linear_aggregation = new CheckBox("Linear Regression");
   final public static List<String> list_step1 = new ArrayList<String>();
   //Step 2
   final public static CheckBox cb_weighted_cosine_similarity = new CheckBox("Weighted Cosine-based similarity");
   final public static CheckBox cb_weighted_pearson_similarity = new CheckBox("Weigthed Pearson similarity");
   final public static List<String> list_step2 = new ArrayList<String>();
   //Step 3
   final public static CheckBox cb_overall_cosine_similarity = new CheckBox("Overall Cosine-based similarity");
   final public static CheckBox cb_pearson_similarity = new CheckBox("Overall Pearson similarity");
   final public static List<String> list_step3 = new ArrayList<String>();
   //Step 4
   final public static CheckBox cb_multiplication_similarity = new CheckBox("Multiplication similarities: simF = simW * simO");
   final public static CheckBox cb_intersection_connectives_infinity = new CheckBox("Intersection Connectives using: p = infinity");
   final public static CheckBox cb_intersection_connectives_1 = new CheckBox("Intersection Connectives using: p = 1");
   final public static CheckBox cb_intersection_connectives_2 = new CheckBox("Intersection Connectives using: p = 2");
   final public static List<String> list_step4 = new ArrayList<String>();
   //Step 5
   final public static CheckBox cb_weighted_sum_approach = new CheckBox("Weighted Sum Approach");
   final public static CheckBox cb_adjusted_weighted_sum_approach = new CheckBox("Adjusted Weighted Sum Approach");
   final public static List<String> list_step5 = new ArrayList<String>();
   public static volatile Text actiontarget = new Text();
   //Footer
   final public static ProgressBar pb = new ProgressBar();
   final public static ProgressIndicator pi = new ProgressIndicator();
   final public static Text pbt = new Text("0" + "%");

   public Options() throws FileNotFoundException, UnsupportedEncodingException {
      w = new Worker();
   }

   /**
    *
    * @return Returns a boolean value that indicated if options specified in the
    * GUI are fine.
    */
   public static boolean setOptions() {
      Options.list_step1.clear();
      Options.list_step2.clear();
      Options.list_step3.clear();
      Options.list_step4.clear();
      Options.list_step5.clear();

      //Step 1
      if (Options.cb_linear_aggregation.isSelected()) {
         Options.list_step1.add("Linear Regression");
      }

      //Step 2
      if (Options.cb_weighted_cosine_similarity.isSelected()) {
         Options.list_step2.add("Weighted Cosine-based similarity");
      }
      if (Options.cb_weighted_pearson_similarity.isSelected()) {
         Options.list_step2.add("Weighted Pearson similarity");
      }

      //Step 3
      if (Options.cb_overall_cosine_similarity.isSelected()) {
         Options.list_step3.add("Overall Cosine-based similarity");
      }
      if (Options.cb_pearson_similarity.isSelected()) {
         Options.list_step3.add("Overall Pearson similarity");
      }

      //Step 4
      if (Options.cb_multiplication_similarity.isSelected()) {
         Options.list_step4.add("Multiplication similarities: simF = simW * simO");
      }
      if (Options.cb_intersection_connectives_infinity.isSelected()) {
         Options.list_step4.add("Intersection connectives infinity");
      }
      if (Options.cb_intersection_connectives_1.isSelected()) {
         Options.list_step4.add("Intersection connectives 1");
      }
      if (Options.cb_intersection_connectives_2.isSelected()) {
         Options.list_step4.add("Intersection connectives 2");
      }

      //Step 5
      if (Options.cb_weighted_sum_approach.isSelected()) {
         Options.list_step5.add("Weighted Sum Approach");
      }
      if (Options.cb_adjusted_weighted_sum_approach.isSelected()) {
         Options.list_step5.add("Adjusted Weighted Sum Approach");
      }

      //Checks
      if (Options.database_url.getText().equals("")) {
         Options.actiontarget.setText("Address to sql databse must not be empty..");
         return false;
      } else {
         Options.db_url = Options.database_url.getText();
         Options.db_username = Options.database_username.getText();
         Options.db_password = Options.database_password.getText();
      }

      if (Options.combinations_of_n.getText().equals("") || Options.max_combinations.getText().equals("")) {
         Options.actiontarget.setText("Combination options must not be empty..");
         return false;
      } else {
         try {
            comb_of_n = Integer.parseInt(Options.combinations_of_n.getText());
            max_comb = Integer.parseInt(Options.max_combinations.getText());
         } catch (NumberFormatException ex) {
            Options.actiontarget.setText("Combination options must not be other than integers..\n" + ex);
            return false;
         }

         if (comb_of_n == 0 || max_comb == 0) {
            Options.actiontarget.setText("Combination options must not be 0(zero)..");
            return false;
         } else if (comb_of_n < 0) {
            Options.actiontarget.setText("Combination of n cannot be less than 0(zero)..");
            return false;
         }
         if (max_comb < 0) {
            max_comb = Integer.MAX_VALUE;
         }
      }

      if (Options.list_step1.isEmpty() || Options.list_step2.isEmpty() || Options.list_step3.isEmpty() || Options.list_step4.isEmpty() || Options.list_step5.isEmpty()) {
         Options.actiontarget.setText("All steps need to have at least one option selected..");
         return false;
      }

      return true;
   }
}
