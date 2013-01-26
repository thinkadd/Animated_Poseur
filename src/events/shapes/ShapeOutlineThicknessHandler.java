package events.shapes;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.state.AnimatedPoseurStateManager;

/**
 * This handler responds when the user requests to select or change the
 * thickness of the outline of a shape
 *
 * @author Xiufeng Yang
 */
public class ShapeOutlineThicknessHandler implements ItemListener {

    /**
     * When the user selects a line thickness in the JComboBox, we'll need to
     * notify the data manager, since it managers the shape in progress. It will
     * update the shape and gui as needed as well.
     *
     * @param e The Event Object.
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        //get the JCombobox we are currently using
        JComboBox temp = (JComboBox) e.getSource();
        //relay the request to the data manager
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();
        poseurStateManager.changeShapeOutlineThickness(temp.getSelectedIndex());
    }
}
