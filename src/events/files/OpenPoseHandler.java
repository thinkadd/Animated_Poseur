package events.files;

import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedPoseurFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This handler responds to when the user wants to open an existing Pose file.
 * It will have to make sure any file being edited is not accidentally lost.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class OpenPoseHandler implements ActionListener {

    /**
     * Called when the user requests to open an existing pose file, this will make
     * sure no work is lost and then start with opening a new pose
     *
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurFileManager poseurFileManager = singleton.getFileManager();
        poseurFileManager.requestOpenPose();
    }
}
