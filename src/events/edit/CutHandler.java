package events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.state.AnimatedPoseurStateManager;

/**
 * This event handler responds to when the user has selected an item on the
 * canvas and has asked to cut it, which should place it on the clipboard and
 * make it disappear on canvases
 *
 * @author Xiufeng Yang
 */
public class CutHandler implements ActionListener {

    /**
     * This method relays this event to the state manager, which will update the
     * clipboard and canvases accordingly.
     *
     * @param ae The event object for this button press.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();
        poseurStateManager.cutSelectedItem();
    }
}