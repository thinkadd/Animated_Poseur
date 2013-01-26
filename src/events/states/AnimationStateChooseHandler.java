/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package events.states;

import animated.poseur.AnimatedPoseur;
import animatedPoseur.gui.AnimatedPoseurGUI;
import animatedPoseur.state.AnimatedPoseurStateManager;
import animatedPoseur.state.PoseurState;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import sprite_renderer.AnimationState;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;
import sprite_renderer.SceneRenderer;
import sprite_renderer.Sprite;
import sprite_renderer.SpriteType;

/**
 * This handler responds to when the user selects a animation state from the
 * animation state choose combobox.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class AnimationStateChooseHandler implements ItemListener {

    /**
     * this is the method called when the user click one item on the
     * <code>JComboBox</code> will resulting the generating and load,
     * as well as rendering of a sprite
     * 
     * @param e contains the information about the event
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        AnimatedPoseurGUI gui = singleton.getGUI();
        JComboBox temp = (JComboBox) e.getSource();
        AnimationState state=null;
        //we neglect the default meaningless "state" , that is, "Select Animation State"
        if(temp.getSelectedItem()!=null && !((String) temp.getSelectedItem()).equals("Select Animation State")) {
//        //conver strings to AnimationStates
            try {
                state = AnimationState.valueOf((String)temp.getSelectedItem());
            } catch (IllegalArgumentException ie) {
                JOptionPane.showMessageDialog(null, "This is not a legal state");
            }
        PoseList poseList = stateManager.getSpriteType().getPoseList(state);
        Iterator<Pose> poseIterator = poseList.getPoseEndIterator();
        int imgID;
        DefaultListModel model = (DefaultListModel)gui.getPoseList().getModel();
        model.clear();
        SpriteType currentType = stateManager.getSpriteType();
        while(poseIterator.hasNext()) {
            imgID = poseIterator.next().getImageID();            
            model.addElement(new ImageIcon(currentType.getImage(imgID).getScaledInstance(160, 160, Image.SCALE_SMOOTH)));
        }
        //start to generate a new sprite to be rendered
        Sprite newSprite = new Sprite(stateManager.getSpriteType(), state);
        newSprite.setPositionX(220);
        newSprite.setPositionY(180);
        //remove the sprite that used to be rendered and add the new one to the
        //render list
        ArrayList<Sprite> spriteToRender = stateManager.getSpriteToRender();
        spriteToRender.removeAll(spriteToRender);
        spriteToRender.add(newSprite);
        stateManager.setState(PoseurState.STATE_SELECTED_STATE);
        //SceneRenderer renderer = singleton.getGUI().getSceneRenderingPanel();
        //renderer.setTimeScaler((float)2.0);
        //renderer.unpauseScene();
        //renderer.pauseScene();
      }
    }   
}
