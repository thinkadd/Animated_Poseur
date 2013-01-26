/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package events.files;

import animated.poseur.AnimatedPoseur;
import animatedPoseur.files.AnimatedPoseurFileManager;
import animatedPoseur.files.AnimatedSpriteFileManager;
import animatedPoseur.gui.AnimatedPoseurGUI;
import animatedPoseur.state.AnimatedPoseurStateManager;
import animatedPoseur.state.PoseurState;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import sprite_renderer.SpriteType;

/**
 * This handler responds to when the user wants to make a new Sprite file. It
 * will have to make sure any file being edited is not accidentally lost.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class NewSpriteHandler implements ActionListener {

    /**
     * Called when the user make a new sprite, this will make sure no work is
     * lost and then close the application.
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
            fileManager.requestNewSprite();
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
            DefaultListModel model = (DefaultListModel) gui.getPoseList().getModel();
            model.clear();
            stateManager.setSpriteType(new SpriteType());
            stateManager.getSpriteToRender().clear();
            gui.getSceneRenderingPanel().unpauseScene();
            gui.getSceneRenderingPanel().pauseScene();
            fileManager.requestNewSprite();
            fileManager.setSaved(true);
        } else {
            //gui.getAnimationStateSelection().removeAllItems();
            gui.getAnimationStateModel().removeAllElements();
            DefaultListModel model = (DefaultListModel) gui.getPoseList().getModel();
            model.clear();
            stateManager.setSpriteType(new SpriteType());
            stateManager.getSpriteToRender().clear();
            gui.getSceneRenderingPanel().unpauseScene();
            gui.getSceneRenderingPanel().pauseScene();
            fileManager.requestNewSprite();
            fileManager.setSaved(true);
        }
    }
}
