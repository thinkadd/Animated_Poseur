package events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedPoseurFileManager;
import animatedPoseur.files.AnimatedSpriteFileManager;
import animatedPoseur.gui.AnimatedPoseurGUI;
import javax.swing.JOptionPane;

/**
 * This handler responds to when the user wants to exit the application. We'll
 * have to be careful not to accidentally lose any of the user's work.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class ExitHandler implements ActionListener {

    /**
     * Called when the user requests to exit via the exit button, this will make
     * sure no work is lost and then close the application.
     *
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedSpriteFileManager spriteFileManager = singleton.getSpriteFileManager();
        if (!spriteFileManager.isSaved()) {
            AnimatedPoseurGUI gui = AnimatedPoseur.getAnimatedPoseur().getGUI();
            int selection = JOptionPane.showOptionDialog(gui,
                    "Save the current sprite type?", "Choose to Save",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, null, null);

            // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
            if (selection == JOptionPane.YES_OPTION) {
                spriteFileManager.requestSaveSprite();
                System.exit(0);
            }
        }
        else {
            System.exit(0);
        }
    }
}