/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package events.poseList;

import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedSpriteFileManager;
import animatedPoseur.gui.AnimatedPoseurGUI;
import animatedPoseur.state.AnimatedPoseurStateManager;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import sprite_renderer.AnimationState;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;
import sprite_renderer.SpriteType;

/**
 * This handler responds to when the user wants to move a selected pose up the
 * pose sequence, i.e., to be rendered earlier.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class MovePoseDownHandler implements ActionListener {

    /**
     * Called when the user requests to move a selected pose up the pose
     * sequence.
     *
     * @param e The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        SpriteType currentType = stateManager.getSpriteType();
        PoseList poseList = currentType.getPoseList(AnimationState.valueOf((String) (gui.getAnimationStateSelection().getSelectedItem())));
        Iterator<Pose> poses = poseList.getPoseIterator();
        int poseID = 0;
        int index = gui.getPoseList().getSelectedIndex();
        Pose poseToChange = poses.next();
        while (poseID != index) {
            poseToChange = poses.next();
            poseID++;
        }
        if(poseList.movePoseDown(poseToChange)) {
            DefaultListModel model = (DefaultListModel) gui.getPoseList().getModel();
            model.clear();
            int imgID;
            poses = poseList.getPoseEndIterator();
            while (poses.hasNext()) {
                imgID = poses.next().getImageID();
                model.addElement(new ImageIcon(currentType.getImage(imgID).getScaledInstance(160, 160, Image.SCALE_SMOOTH)));
            }
            gui.getPoseList().setSelectedIndex(index+1);
            AnimatedSpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
            spriteFileManager.setSaved(false);
            gui.updateMode();
        }
    }
}
