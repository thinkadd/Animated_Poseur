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
 * This handler responds to when the user wants to duplicate the current
 * selected animated state.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class DuplicateStateHandler implements ActionListener {

    /**
     * Called when the user presses the duplicate state button to duplicate the
     * selected state from the state list, and ask the user for a name.
     *
     * @param e The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        AnimatedSpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
        String newStateName = JOptionPane.showInputDialog("Give a name of the duplicated state:", null);
        String oldStateName = (String)gui.getAnimationStateModel().getSelectedItem();
        PoseList list = stateManager.getSpriteType().getAnimations().get(AnimationState.valueOf(oldStateName));
        stateManager.getSpriteType().getAnimations().put(AnimationState.valueOf(newStateName), list);
        gui.getAnimationStateModel().addElement(newStateName);
        gui.getAnimationStateModel().setSelectedItem(newStateName);
        spriteFileManager.setSaved(false);
        gui.updateMode();
    }
}
