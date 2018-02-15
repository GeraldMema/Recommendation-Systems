package rs.gui.functionality;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * The class that controls the Cancel button
 *
 * @author Simon Tzanakis
 */
public class CancelButtonAction implements EventHandler<ActionEvent>
{
    /**
     * Method that is run when the Cancel button is pressed
     *
     * @param e The event that activated the method
     */
    @Override
    public void handle(ActionEvent e)
    {
        if(Options.th.isAlive())                        //Check if already running on the background
        {
            Options.actiontarget.setText(Options.actiontarget.getText().concat("Canceling algorithm.. Please wait..\n\n"));
            Options.w.cancel();
        }
        
    }
}
