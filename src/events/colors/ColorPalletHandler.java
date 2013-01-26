package events.colors;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.state.AnimatedPoseurStateManager;

/**
 * This handler responds to when the user selects a color from the color pallet.
 * Note that this may affect either the fill or outline color for rendering and
 * that it may update the colors used for a selected shape.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class ColorPalletHandler implements ActionListener {

    /**
     * This method responds to when the user clicks on a color from the color
     * pallet, which could change the color of the currently selected shape, or
     * load a color into one of the color controls (outline or fill).
     *
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // WE CAN GET THE COLOR FROM THE PRESSED BUTTON'S BACKGROUND
        JButton source = (JButton) ae.getSource();
        Color selectedColor = source.getBackground();

        // AND SEND IT OFF TO THE STATE MANAGER TO UPDATE THE APP
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();
        poseurStateManager.selectPalletColor(selectedColor);
    }
}