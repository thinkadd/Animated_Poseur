package events.colors;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.state.AnimatedPoseurStateManager;

/**
 * This handler responds to when the user moves the transparency glide. It will
 * change the transparency of the selected shape.
 *
 * @author Xiufeng Yang
 */
public class ChangeShapeTransparencyHandler implements ChangeListener {

    /**
     * This method responds to when the user moves the transparency glide, which
     * could change the transparency of the currently selected shape.
     *
     * @param e The Event Object.
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        //We get the JSlider that controls transparency
        JSlider temp = (JSlider) e.getSource();
        //We get the current program's poser state manager
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();
        //We call the corresponding method in PoseurStateManager class
        //with the value of the JSlider
        poseurStateManager.changeShapeTransparency(temp.getValue());
    }
}
