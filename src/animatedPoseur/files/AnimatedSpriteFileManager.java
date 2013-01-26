/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animatedPoseur.files;

import animated.poseur.AnimatedPoseur;
import static animated.poseur.AnimatedPoseurSettings.*;
import animatedPoseur.gui.AnimatedPoseurGUI;
import animatedPoseur.state.AnimatedPoseurStateManager;
import animatedPoseur.state.PoseurState;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import sprite_renderer.SpriteType;

/**
 *
 * @author Anderson Yang
 */
public class AnimatedSpriteFileManager {

    private PoseurIO poseurIO;
    private String currentSpriteTypeName;
    private File currentFile;
    private String currentFileName;
    private boolean saved;

    public AnimatedSpriteFileManager() {
        this.poseurIO = new PoseurIO();
        this.saved = true;
    }

    public String getCurrentSpriteTypeName() {
        return currentSpriteTypeName;
    }

    public void setCurrentSpriteTypeName(String currentSpriteTypeName) {
        this.currentSpriteTypeName = currentSpriteTypeName;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public void requestOpenSpriteType() {
        boolean continueToOpenSpriteType = true;
        if (!saved) {
            continueToOpenSpriteType = promptToSave();
        }

        if (continueToOpenSpriteType) {
            promptToOpenSpriteType();
            AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
            AnimatedPoseurStateManager stateManager = singleton.getStateManager();
            stateManager.resetState();
            stateManager.setState(PoseurState.SELECT_ANIMATION_STATE);
        }

    }

    public void requestNewSprite() {
        boolean continueToMakeNew = true;
        if (!saved) {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSave();
        }

        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew) {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptForNew();

            if (continueToMakeNew) {
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                AnimatedPoseurStateManager poseurStateManager =
                        AnimatedPoseur.getAnimatedPoseur().getStateManager();
                //poseurStateManager.setSpriteType(new SpriteType());
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_ANIMATION_STATE);
            }
        }
    }

    private boolean promptToSave() {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        AnimatedPoseurGUI gui = AnimatedPoseur.getAnimatedPoseur().getGUI();
        SpriteType type = AnimatedPoseur.getAnimatedPoseur().getStateManager().getSpriteType();
        int selection = JOptionPane.showOptionDialog(gui,
                PROMPT_TO_SAVE_TEXT, PROMPT_TO_SAVE_TITLE_TEXT,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, null, null);

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection == JOptionPane.YES_OPTION) {
            poseurIO.saveSpriteType(currentFile, type);
            saved = true;
        } // IF THE USER SAID CANCEL, THEN WE'LL TELL WHOEVER
        // CALLED THIS THAT THE USER IS NOT INTERESTED ANYMORE
        else if (selection == JOptionPane.CANCEL_OPTION) {
            return false;
        }
        // IF THE USER SAID NO, WE JUST GO ON WITHOUT SAVING
        // BUT FOR BOTH YES AND NO WE DO WHATEVER THE USER
        // HAD IN MIND IN THE FIRST PLACE
        return true;
    }

    public void requestSaveSprite() {
        SpriteType type = AnimatedPoseur.getAnimatedPoseur().getStateManager().getSpriteType();
        boolean savedSuccessfully = poseurIO.saveSpriteType(currentFile, type);
        if (savedSuccessfully) {
            // MARK IT AS SAVED
            saved = true;

            // AND REFRESH THE GUI
            AnimatedPoseur.getAnimatedPoseur().getGUI().updateMode();
        }
    }

    public boolean requestSaveAsSpriteType() {
        AnimatedPoseurGUI gui = AnimatedPoseur.getAnimatedPoseur().getGUI();
        String fileName = JOptionPane.showInputDialog(
                gui,
                "Save the current sprite type as:",
                "Enter Sprite Type File Name",
                JOptionPane.QUESTION_MESSAGE);
        if ((fileName != null)
                && (fileName.length() > 0)) {
            currentSpriteTypeName = fileName;
            currentFileName = fileName + ".xml";
            File dir = new File("./data/sprite_types/" + currentSpriteTypeName + "/");
            dir.mkdirs();
            currentFile = new File(dir, currentFileName);
            poseurIO.saveSpriteTypeAs(currentFile, fileName);
            AnimatedPoseurStateManager state = AnimatedPoseur.getAnimatedPoseur().getStateManager();
            SpriteType currentType = AnimatedPoseur.getAnimatedPoseur().getStateManager().getSpriteType();
            HashMap images = currentType.getImageAddr();
            Iterator imgIterator = images.values().iterator();
            String srcName = "";
            while (imgIterator.hasNext()) {
                srcName = (String) imgIterator.next();
                try {
                    BufferedImage temp = ImageIO.read(new File("./data/sprite_types/"
                            + state.getCurrentTypeName() + "/" + srcName));
                    ImageIO.write(temp, "png", new File("./data/sprite_types/" + fileName + "/"
                            + srcName.replace(state.getCurrentTypeName(), fileName)));
                    File sourceFile = new File("./data/sprite_types/" + state.getCurrentTypeName()
                            + "/" + srcName.substring(0, srcName.lastIndexOf(".")) + ".pose");
                    File destFile = new File("./data/sprite_types/" + fileName + "/"
                            + srcName.replace(state.getCurrentTypeName(), fileName).
                            substring(0, srcName.replace(state.getCurrentTypeName(), fileName).
                            lastIndexOf(".")) + ".pose");
                    if (!destFile.exists()) {
                        destFile.createNewFile();
                    }

                    FileChannel source = null;
                    FileChannel destination = null;
                    try {
                        source = new RandomAccessFile(sourceFile, "rw").getChannel();
                        destination = new RandomAccessFile(destFile, "rw").getChannel();

                        long position = 0;
                        long count = source.size();

                        source.transferTo(position, count, destination);
                    } finally {
                        if (source != null) {
                            source.close();
                        }
                        if (destination != null) {
                            destination.close();
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(AnimatedSpriteFileManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            saved = true;
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile);
            // WE DID IT!
            return true;
        }
        // USER DECIDED AGAINST IT
        return false;
    }

    private boolean promptForNew() {
        // SO NOW ASK THE USER FOR A POSE NAME
        AnimatedPoseurGUI gui = AnimatedPoseur.getAnimatedPoseur().getGUI();
        //gui.getAnimationStateModel().removeAllElements();
        //gui.getAnimationStateSelection().removeAllItems();
        String fileName = JOptionPane.showInputDialog(
                gui,
                "What do you want to name your new sprite type?",
                "Enter Sprite Type File Name",
                JOptionPane.QUESTION_MESSAGE);
        //AnimatedPoseur.getAnimatedPoseur().getStateManager().setCurrentTypeName(fileName);
        // IF THE USER CANCELLED, THEN WE'LL GET A fileName
        // OF NULL, SO LET'S MAKE SURE THE USER REALLY
        // WANTS TO DO THIS ACTION BEFORE MOVING ON
        if ((fileName != null)
                && (fileName.length() > 0)) {
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteTypeName = fileName;
            currentFileName = fileName + ".xml";
            File dir = new File("./data/sprite_types/" + currentSpriteTypeName);
            dir.mkdirs();
            currentFile = new File(dir, currentFileName);
            SpriteType type = AnimatedPoseur.getAnimatedPoseur().getStateManager().getSpriteType();
//            SpriteType type = poseurIO.loadSpriteType(currentSpriteTypeName);


            poseurIO.saveSpriteType(currentFile, type);
            // SAVE OUR NEW FILE            
            saved = true;
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile);

            // WE DID IT!
            return true;
        }
        // USER DECIDED AGAINST IT
        return false;
    }

    private void promptToOpenSpriteType() {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        //gui.getAnimationStateModel().removeAllElements();
        JFileChooser spriteTypeFileChooser = new JFileChooser(SPRITETYPE_PATH);
        int buttonPressed = spriteTypeFileChooser.showOpenDialog(gui);

        if (buttonPressed == JFileChooser.APPROVE_OPTION) {

            currentFile = spriteTypeFileChooser.getSelectedFile();
            currentFileName = currentFile.getName();
            currentSpriteTypeName = currentFileName.substring(0, currentFileName.indexOf("."));
            AnimatedPoseur.getAnimatedPoseur().getStateManager().setCurrentTypeName(currentSpriteTypeName);
            saved = true;
            //String appName = gui.getAppName();
            gui.setTitle(currentFileName);
            poseurIO.loadSpriteType(currentSpriteTypeName);
            gui.refreshStateSelection();
        }
    }
}
