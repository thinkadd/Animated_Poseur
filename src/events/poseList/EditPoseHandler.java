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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import sprite_renderer.AnimationState;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;
import sprite_renderer.SpriteType;

/**
 * This handler responds to when the user wants to edit an existing animation
 * state of the current sprite.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class EditPoseHandler implements ActionListener {

    /**
     * Called when the user requests to edit an existing animation state of the
     * current sprite.
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
            int imgID = currentPose.getImageID();
            String imgAddr = currentType.getImageAddr().get(imgID);
            //String poseAddr = "data/sprite_types/"+stateManager.getCurrentTypeName()+"/"+imgAddr.substring(0,imgAddr.length()-4)+".pose";
            String poseAddr = "./data/sprite_types/"+stateManager.getCurrentTypeName()+"/"+ imgAddr.substring(0, imgAddr.lastIndexOf(".")) + ".pose";
            File file = new File(poseAddr);
            PoseIO io = new PoseIO();
            io.loadPose(file.getAbsolutePath());
            stateManager.setEditing(true);
            AnimatedSpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
            spriteFileManager.setSaved(false);
            gui.updateMode();
        }
    }
}
