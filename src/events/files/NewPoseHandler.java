package events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedPoseurFileManager;

/**
 * This handler responds to when the user wants to make a new Pose file. It will
 * have to make sure any file being edited is not accidentally lost.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class NewPoseHandler implements ActionListener {

    /**
     * Called when the user requests to make a new pose.
     *
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurFileManager poseurFileManager = singleton.getFileManager();
        poseurFileManager.requestNewPose();
    }
}