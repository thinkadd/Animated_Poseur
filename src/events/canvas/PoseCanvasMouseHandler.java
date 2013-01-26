package events.canvas;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import animated.poseur.AnimatedPoseur;
import animatedPoseur.state.AnimatedPoseurStateManager;

/**
 * This is the event handler for when the user interacts with the editing canvas
 * using the mouse. Note that this class implements both MouseListener and
 * MouseMotionListener, and so is obliged to provide services for both types of
 * interactions.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class PoseCanvasMouseHandler implements MouseListener, MouseMotionListener {

    /**
     * This method responds to when the user presses the mouse on the pose
     * editing canvas. Note that it forwards the response to the poseur state
     * manager, since that object manages the data it intends to change.
     *
     * @param e The event object.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager state = singleton.getStateManager();
        state.processMousePress(e.getX(), e.getY());
    }

    /**
     * This method responds to when the user releases the mouse on the pose
     * editing canvas. Note that it forwards the response to the poseur state
     * manager, since that object manages the data it intends to change.
     *
     * @param e The event object.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager state = singleton.getStateManager();
        state.processMouseReleased(e.getX(), e.getY());
    }

    /**
     * This method responds to when the user drags the mouse on the pose editing
     * canvas. Note that it forwards the response to the poseur state manager,
     * since that object manages the data it intends to change.
     *
     * @param e The event object.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();
        poseurStateManager.processMouseDragged(e.getX(), e.getY());
    }

    // WE ARE NOT USING THE REST OF THESE INTERACTIONS
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
