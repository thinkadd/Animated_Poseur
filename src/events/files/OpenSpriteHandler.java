/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package events.files;

import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedSpriteFileManager;
import animatedPoseur.gui.AnimatedPoseurGUI;
import animatedPoseur.state.AnimatedPoseurStateManager;
import animatedPoseur.state.PoseurState;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import sprite_renderer.SpriteType;

/**
 * This handler responds to when the user wants to open an existing Sprite xml
 * file. It will have to make sure any file being edited is not accidentally
 * lost.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class OpenSpriteHandler implements ActionListener {

    /**
     * Called when the user requests to open an existing sprite, this will make
     * sure no work is lost.
     *
     * @param e The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedSpriteFileManager fileManager = singleton.getSpriteFileManager();
        AnimatedPoseurGUI gui = singleton.getGUI();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        if (stateManager.getMode() == PoseurState.STARTUP_STATE) {
            fileManager.requestOpenSpriteType();
            fileManager.setSaved(true);
        } else if (!fileManager.isSaved()) {
            int selection = JOptionPane.showOptionDialog(gui,
                    "Save the current sprite type?", "Choose to Save",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, null, null);

            if (selection == JOptionPane.YES_OPTION) {
                fileManager.requestSaveSprite();
            }
            //gui.getAnimationStateSelection().removeAllItems();
            gui.getAnimationStateModel().removeAllElements();
            gui.getPoseList().removeAll();
            stateManager.setSpriteType(new SpriteType());
            fileManager.requestOpenSpriteType();
            fileManager.setSaved(true);
        } else {
            //gui.getAnimationStateSelection().removeAllItems();
            gui.getAnimationStateModel().removeAllElements();
            gui.getPoseList().removeAll();
            stateManager.setSpriteType(new SpriteType());
            fileManager.requestOpenSpriteType();
            fileManager.setSaved(true);
        }
    }
}
