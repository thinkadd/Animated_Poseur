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
import javax.swing.JOptionPane;
import sprite_renderer.AnimationState;
import sprite_renderer.PoseList;

/**
 * This handler responds to when the user wants to rename the current state
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class RenameStateHandler implements ActionListener {

    /**
     * Called when the user presses the rename state button to rename the
     * current state
     *
     * @param e The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        AnimatedSpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
        String newStateName = JOptionPane.showInputDialog("Give a new name of the current state:", null);
        String oldStateName = (String)gui.getAnimationStateModel().getSelectedItem();
        PoseList list = stateManager.getSpriteType().getAnimations().get(AnimationState.valueOf(oldStateName));
        stateManager.getSpriteType().getAnimations().remove(AnimationState.valueOf(oldStateName));
        stateManager.getSpriteType().getAnimations().put(AnimationState.valueOf(newStateName), list);
        gui.getAnimationStateModel().addElement(newStateName);
        gui.getAnimationStateModel().setSelectedItem(newStateName);
        gui.getAnimationStateModel().removeElement(oldStateName);
        spriteFileManager.setSaved(false);
        gui.updateMode();
    }
}
