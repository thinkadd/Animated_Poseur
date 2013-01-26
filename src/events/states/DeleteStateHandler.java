/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package events.states;

import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedSpriteFileManager;
import animatedPoseur.gui.AnimatedPoseurGUI;
import animatedPoseur.state.AnimatedPoseurStateManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import sprite_renderer.AnimationState;

/**
 * This handler responds to when the user wants to delete the current selected
 * animated state.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class DeleteStateHandler implements ActionListener {

    /**
     * Called when the user presses the delete state button to delete the
     * selected state from the state list.
     *
     * @param e The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        AnimatedSpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
        String oldStateName = (String)gui.getAnimationStateModel().getSelectedItem();
        stateManager.getSpriteType().getAnimations().remove(AnimationState.valueOf(oldStateName));
        gui.getAnimationStateModel().removeElement(oldStateName);
        spriteFileManager.setSaved(false);
        gui.updateMode();
    }
}
