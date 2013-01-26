package events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.state.AnimatedPoseurStateManager;

/**
 * This event handler responds to when the user has clicked on the arrow button,
 * denoting they want to select a shape to edit in some way.
 *
 * @author Xiufeng Yang
 */
public class StartSelectionHandler implements ActionListener {

    /**
     * This method relays this event to the data manager, which will update its
     * state and the gui.
     *
     * @param ae The event object for this button press.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();
        poseurStateManager.startShapeSelection();
    }
}