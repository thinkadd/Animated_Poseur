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
import java.util.HashMap;
import javax.swing.JOptionPane;
import sprite_renderer.AnimationState;
import sprite_renderer.PoseList;
import sprite_renderer.Sprite;
import sprite_renderer.SpriteType;

/**
 * This handler responds to when the user wants to create a new state
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class NewStateHandler implements ActionListener {

    /**
     * Called when the user presses the new state button to create a new state,
     * with a given name
     *
     * @param e The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        AnimatedSpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
        String newStateName = JOptionPane.showInputDialog("Input the name of new state", null);
        if (newStateName != null && newStateName.trim().length() != 0) {
            try {
                AnimationState newState = AnimationState.valueOf(newStateName);
                SpriteType currentType = stateManager.getSpriteType();
                currentType.addPoseList(newState);
                gui.getAnimationStateModel().addElement(newStateName);
                //gui.getAnimationStateModel().setSelectedItem(newStateName);
                gui.getAnimationStateSelection().setSelectedItem(newStateName);
                spriteFileManager.setSaved(false);
                gui.updateMode();
                //stateManager.getSpriteType().getAnimations().put(newState, new PoseList());                
                
//            stateManager.getSpriteToRender().clear();
//            stateManager.getSpriteToRender().add(new Sprite(currentType, newState));
            } catch (IllegalArgumentException ie) {
                JOptionPane.showMessageDialog(null, "This is not a legal state");
            }
        }
    }
}
