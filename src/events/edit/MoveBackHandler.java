package events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.state.AnimatedPoseurStateManager;

/**
 * This event handler responds to when the user has selected an item on the
 * canvas and has asked to move it to the bottom of the canvases.
 *
 * @author Xiufeng Yang
 */
public class MoveBackHandler implements ActionListener {

    /**
     * This method relays this event to the state manager, which will change the
     * sequence of the shapes to make them overlap in a certain way.
     *
     * @param ae The event object for this button press.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();
        poseurStateManager.moveBackSelectedItem();
    }
}