package events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedPoseurFileManager;

/**
 * This handler responds to when the user wants to save as another Pose with the
 * Pose currently being edited.
 *
 * @author Xiufeng Yang
 *
 * @version 1.0
 */
public class SaveAsPoseHandler implements ActionListener {

    /**
     * Called when the user requests to save the current pose to a new file.
     *
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurFileManager poseurFileManager = singleton.getFileManager();
        poseurFileManager.requestSaveAsPose();
    }
}