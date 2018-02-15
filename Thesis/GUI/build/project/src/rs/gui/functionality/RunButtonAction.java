package rs.gui.functionality;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import rs.logic.algorithms.Worker;

/**
 * The class that controls the Run button
 *
 * @author Simon Tzanakis
 */
public class RunButtonAction implements EventHandler<ActionEvent> {

   /**
    * Method that is run when the Run button is pressed
    *
    * @param e The event that activated the method
    */
   @Override
   public void handle(ActionEvent e) {
      if (Options.th != null && Options.th.isAlive()) //Check if already running on the background
      {
         Options.actiontarget.setText(Options.actiontarget.getText().concat("Algorithm is already running.. Please wait..\n\n"));
      } else {
         if (Options.setOptions()) {
            try {
               Options.w = new Worker();
            } catch (FileNotFoundException ex) {
               Logger.getLogger(RunButtonAction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
               Logger.getLogger(RunButtonAction.class.getName()).log(Level.SEVERE, null, ex);
            }
            Options.th = new Thread(Options.w);
            Options.pb.progressProperty().bind(Options.w.progressProperty());
            Options.th.start();
         }
      }
   }
}
