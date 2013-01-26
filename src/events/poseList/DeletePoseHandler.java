/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package events.poseList;

import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedSpriteFileManager;
import animatedPoseur.files.PoseIO;
import animatedPoseur.gui.AnimatedPoseurGUI;
import animatedPoseur.state.AnimatedPoseurStateManager;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import sprite_renderer.AnimationState;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;
import sprite_renderer.SpriteType;

/**
 * This handler responds to when the user wants to delete an existing animation
 * state of the current sprite.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class DeletePoseHandler implements ActionListener {

    /**
     * Called when the user requests to delete an existing animation state of
     * the current sprite.
     *
     * @param e The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        AnimatedPoseurGUI gui = singleton.getGUI();
        SpriteType currentType = stateManager.getSpriteType();
        PoseList list = currentType.getPoseList(AnimationState.valueOf((String) gui.getAnimationStateModel().getSelectedItem()));
        int index = gui.getPoseList().getSelectedIndex();
        if (index != -1) {
            Iterator<Pose> poses = list.getPoseEndIterator();
            int i = 0;
            Pose currentPose = poses.next();
            while (i != index) {
                currentPose = poses.next();
                i++;
            }
            list.deletePose(currentPose);
            DefaultListModel model = (DefaultListModel) gui.getPoseList().getModel();
            model.clear();
            int imgID;
            poses = list.getPoseEndIterator();
            while (poses.hasNext()) {
                imgID = poses.next().getImageID();
                model.addElement(new ImageIcon(currentType.getImage(imgID).getScaledInstance(160, 160, Image.SCALE_SMOOTH)));
            }
            AnimatedSpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
            spriteFileManager.setSaved(false);
            gui.updateMode();
        }
    }
}
