package events.shapes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.shapes.PoseurShapeType;
import animatedPoseur.state.AnimatedPoseurStateManager;

/**
 * This handler responds to when the user requests to start drawing a rectangle.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class RectangleSelectionHandler implements ActionListener {

    /**
     * When the user requests to draw a rectangle, we'll need to notify the data
     * manager, since it managers the shape in progress. It will update the gui
     * as needed as well.
     *
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // RELAY THE REQUEST TO THE DATA MANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();
        poseurStateManager.selectShapeToDraw(PoseurShapeType.RECTANGLE);
    }
}