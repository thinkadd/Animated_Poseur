package events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedPoseurFileManager;

/**
 * This handler responds to when the user wants to save the Pose currently being
 * edited.
 *
 * @author Xiufeng Yang
 *
 * @version 1.0
 */
public class SavePoseHandler implements ActionListener {

    /**
     * Called when the user requests save the current pose to file.
     *
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurFileManager poseurFileManager = singleton.getFileManager();
        poseurFileManager.requestSavePose();
    }
}