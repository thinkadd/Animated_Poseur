package events.window;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedPoseurFileManager;

/**
 * This handler responds to interactions with the window. Note that we're only
 * interested in the case where the user is trying to close the window, so we'll
 * only provide a response for windowClosing.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class PoseurWindowHandler implements WindowListener {

    /**
     * This method responds when the user attempts to close the window. We'll
     * relay this request to the file manager, which will make sure any unsaved
     * work is not lost before exiting the application.
     *
     * @param we The Event Object.
     */
    @Override
    public void windowClosing(WindowEvent we) {
        // RELAY THE REQUEST TO THE FILE MANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurFileManager poseurFileManager = singleton.getFileManager();
        poseurFileManager.requestExit();
    }

    // WE WILL NOT BE USING THE REST OF THESE METHODS,
    // BUT I'LL PROVIDE EMPTY METHOD DEFINITIONS TO MAKE
    // THE COMPILER HAPPY, SINCE THIS CLASS implements
    // WindowListener. NOTE THAT AN ALTERNATIVE WOULD
    // BE extends WindowsAdapter AND THEN ONLY PROVIDE
    // THE METHODS WE'LL BE USING.
    @Override
    public void windowOpened(WindowEvent we) {
    }

    @Override
    public void windowClosed(WindowEvent we) {
    }

    @Override
    public void windowIconified(WindowEvent we) {
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
    }

    @Override
    public void windowActivated(WindowEvent we) {
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
    }
}