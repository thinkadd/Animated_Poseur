package events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedPoseurFileManager;
import animatedPoseur.state.AnimatedPoseurStateManager;

/**
 * This handler responds to when the user wants to export the current pose to an
 * image file.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class ExportPoseHandler implements ActionListener {

    /**
     * Called when the user requests to export the pose currently being edited
     * to an image file.
     *
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurFileManager poseurFileManager = singleton.getFileManager();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        poseurFileManager.requestExportPose();
        stateManager.setEditing(false);
    }
}