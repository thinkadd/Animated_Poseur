/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package events.poseList;

import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedSpriteFileManager;
import animatedPoseur.gui.AnimatedPoseurGUI;
import animatedPoseur.state.AnimatedPoseurStateManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JOptionPane;
import sprite_renderer.AnimationState;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;
import sprite_renderer.SpriteType;

/**
 * This handler responds to when the user wants to set a duration to the
 * selected pose.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class SetDurationHandler implements ActionListener {

    /**
     * Called when the user requests to set a duration to the selected pose.
     *
     * @param e The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int duration = Integer.parseInt(JOptionPane.showInputDialog("Set new duration in frames"));
            AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
            AnimatedPoseurGUI gui = singleton.getGUI();
            AnimatedPoseurStateManager stateManager = singleton.getStateManager();
            SpriteType currentType = stateManager.getSpriteType();
            PoseList poseList = currentType.getPoseList(AnimationState.valueOf((String) (gui.getAnimationStateSelection().getSelectedItem())));
            Iterator<Pose> poses = poseList.getPoseIterator();
            int poseID = 0;
            int index = gui.getPoseList().getSelectedIndex();
            Pose poseToChange = poses.next();
            while(poseID != index) {
                poseToChange = poses.next();
                poseID++;
            }
            poseToChange.setDurationInFrames(duration);
            AnimatedSpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
            spriteFileManager.setSaved(false);
            gui.updateMode();
        } catch (Exception ce) {
            JOptionPane.showMessageDialog(null, "Invalid input, integer needed");
        }

    }
}
