/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package events.poseList;

import animated.poseur.AnimatedPoseur;
import animatedPoseur.state.AnimatedPoseurStateManager;
import animatedPoseur.state.PoseurState;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This handler responds to when the user selects a pose from the pose sequence
 * list.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class PoseListHandler implements ListSelectionListener {

    /**
     * Called when the user selects a pose from the pose sequence list.
     *
     * @param e The Event Object.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList poseList = (JList) e.getSource();
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        if (poseList.getSelectedIndex() != -1) {
            stateManager.setState(PoseurState.POSE_SELECTED_STATE);
        } else {
            stateManager.setState(PoseurState.STATE_SELECTED_STATE);
        }
    }
}
