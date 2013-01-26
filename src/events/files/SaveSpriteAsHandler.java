/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package events.files;

import animated.poseur.AnimatedPoseur;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * This handler responds to when the user wants to save the current Sprite to
 * another xml file. It will have to make sure any file being edited is not
 * accidentally lost.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class SaveSpriteAsHandler implements ActionListener {

    /**
     * Called when the user requests to save an existing sprite, this will make
     * sure no work is lost.
     *
     * @param e The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        singleton.getSpriteFileManager().requestSaveAsSpriteType();
    }
}
