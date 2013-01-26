package animatedPoseur.files;

import animated.poseur.AnimatedPoseur;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static animated.poseur.AnimatedPoseurSettings.*;
import animatedPoseur.gui.AnimatedPoseurGUI;
import animatedPoseur.state.AnimatedPoseurStateManager;
import animatedPoseur.state.PoseurState;
import java.util.Iterator;
import sprite_renderer.AnimationState;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;
import sprite_renderer.SpriteType;

/**
 * This class provides all the file servicing for the AnimatedPoseur
 * application. This means it directs all operations regarding loading,
 * exporting, creating, and saving files, this class manages when to actually
 * read and write from/to files, prompting the user when necessary for file
 * names and validation on actions.
 *
 * @author Xiufeng Yang
 * @version 1.0
 */
public class AnimatedPoseurFileManager {
    // WE'LL STORE THE FILE CURRENTLY BEING WORKED ON
    // AND THE NAME OF THE FILE

    private File currentFile;
    private String currentPoseName;
    private String currentFileName;
    private String currentSpriteTypeName;
    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    private boolean saved;
    // THIS GUY KNOWS HOW TO READ, WRITE, AND EXPORT POESS
    private PoseIO poseIO;
    private PoseurIO poseurIO;

    /**
     * This default constructor starts the program without a pose file being
     * edited.
     */
    public AnimatedPoseurFileManager() {
        // NOTHING YET
        currentFile = null;
        currentFileName = null;
        saved = true;
        poseIO = new PoseIO();
        poseurIO = new PoseurIO();
    }

    public String getCurrentPoseName() {
        return currentPoseName;
    }

    public void setCurrentPoseName(String currentPoseName) {
        this.currentPoseName = currentPoseName;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

    public String getCurrentSpriteTypeName() {
        return currentSpriteTypeName;
    }

    public void setCurrentSpriteTypeName(String currentSpriteTypeName) {
        this.currentSpriteTypeName = currentSpriteTypeName;
    }

    /**
     * This method starts the process of editing a new pose. If a pose is
     * already being edited, it will prompt the user to save it first.
     */
    public void requestNewPose() {
        // WE MAY HAVE TO SAVE CURRENT WORK
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
                poseurStateManager.resetState();
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
            }
        }
    }

    /**
     * This method lets the user open a pose saved to a file. It will also make
     * sure data for the current pose is not lost.
     */
    public void requestOpenPose() {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToOpen = true;
        if (!saved) {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToOpen = promptToSave();
        }

        // IF THE USER REALLY WANTS TO OPEN A POSE
        if (continueToOpen) {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            promptToOpen();
        }
    }
    
    public void requestOpenSpriteType() {
        boolean continueToOpenSpriteType = true;
        if(!saved) {
            //continueToOpenSpriteType = promptToSaveSpriteType();
        }
        
        if(continueToOpenSpriteType) {
            promptToOpenSpriteType();
        }
    }

    /**
     * This method will save the current pose to a file. Note that we already
     * know the name of the file, so we won't need to prompt the user.
     */
    public void requestSavePose() {
        // DON'T ASK, JUST SAVE
        boolean savedSuccessfully = poseIO.savePose(currentFile);
        if (savedSuccessfully) {
            // MARK IT AS SAVED
            saved = true;

            // AND REFRESH THE GUI
            AnimatedPoseur.getAnimatedPoseur().getGUI().updateMode();
        }
    }

    /**
     * This method will save the current pose as a named file provided by the
     * user.
     */
    public void requestSaveAsPose() {
        // ASK THE USER FOR A FILE NAME
        promptForNew();
    }

    /**
     * This method will export the current pose to an image file.
     */
    public void requestExportPose() {
        // ASK THE USER TO MAKE SURE THEY WANT TO GO AHEAD WITH IT
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        int selection = JOptionPane.showOptionDialog(gui,
                EXPORT_POSE_TEXT + currentPoseName + PNG_FILE_EXTENSION,
                EXPORT_POSE_TITLE_TEXT,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, null, null);
        // IF THE USER CLICKED OK, THEN EXPORT THE POSE
        if (selection == JOptionPane.OK_OPTION) {
            int imgID = -1;
            if (stateManager.isEditing() == true) {
                SpriteType currentType = stateManager.getSpriteType();
                PoseList currentPoseList = currentType.getPoseList(AnimationState.valueOf((String)gui.getAnimationStateSelection().getSelectedItem()));
                Iterator<Pose> poseIterator = currentPoseList.getPoseEndIterator();
                int i = gui.getPoseList().getSelectedIndex();
                int index = 0;
                Pose poseInList = poseIterator.next();
                while(index!=i) {
                    poseInList= poseIterator.next();
                    index++;
                }
                imgID = poseInList.getImageID();
                String imgFileName = currentType.getImageAddr().get(imgID);
                currentFile = new File("./data/sprite_types/" + stateManager.getCurrentTypeName()
                        + "/" + imgFileName.substring(0, imgFileName.lastIndexOf(".")) + ".pose");
                if(imgFileName.contains("/")) {
                    currentPoseName = imgFileName.substring(0, imgFileName.lastIndexOf("."));
                } else {
                    currentPoseName = "./data/sprite_types/" + stateManager.getCurrentTypeName()
                        + "/" + imgFileName.substring(0, imgFileName.lastIndexOf("."));
                }
            }
            poseIO.savePose(currentFile);
            singleton.getFileManager().saved=true;
            poseIO.exportPose(currentPoseName, imgID);
            stateManager.setState(PoseurState.STATE_SELECTED_STATE);
        }
    }

    /**
     * This method will exit the application, making sure the user doesn't lose
     * any data first.
     */
    public void requestExit() {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToExit = true;
        if (!saved) {
            // THE USER CAN OPT OUT HERE
            continueToExit = promptToSave();
        }

        // IF THE USER REALLY WANTS TO EXIT THE APP
        if (continueToExit) {
            // EXIT THE APPLICATION
            System.exit(0);
        }
    }

    /**
     * This helper method asks the user for a name for the pose about to be
     * created. Note that when the pose is created, a corresponding .pose file
     * is also created.
     *
     * @return true if the user goes ahead and provides a good name false if
     * they cancel.
     */
    private boolean promptForNew() {
        // SO NOW ASK THE USER FOR A POSE NAME
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        AnimatedSpriteFileManager fileManager = singleton.getSpriteFileManager();
        SpriteType currentType = stateManager.getSpriteType();
        String currentTypeName = fileManager.getCurrentSpriteTypeName();
        String currentState = (String) gui.getAnimationStateSelection().getSelectedItem();
        String fileName = "./data/sprite_types/" + currentTypeName + "/"
                + currentTypeName + "_" + currentState
                + "_" + currentType.getAnimations().get(AnimationState.valueOf(currentState)).getNumPoses();

        // IF THE USER CANCELLED, THEN WE'LL GET A fileName
        // OF NULL, SO LET'S MAKE SURE THE USER REALLY
        // WANTS TO DO THIS ACTION BEFORE MOVING ON
        if ((fileName != null)
                && (fileName.length() > 0)) {
            // UPDATE THE FILE NAMES AND FILE
            currentPoseName = fileName;
            currentFileName = fileName + POSE_FILE_EXTENSION;
            currentFile = new File(currentFileName);

            // SAVE OUR NEW FILE
            poseIO.savePose(currentFile);
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

    /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. Note that it could be used
     * in multiple contexts before doing other actions, like creating a new
     * pose, or opening another pose, or exiting. Note that the user will be
     * presented with 3 options: YES, NO, and CANCEL. YES means the user wants
     * to save their work and continue the other action (we return true to
     * denote this), NO means don't save the work but continue with the other
     * action (true is returned), CANCEL means don't save the work and don't
     * continue with the other action (false is retuned).
     *
     * @return true if the user presses the YES option to save, true if the user
     * presses the NO option to not save, false if the user presses the CANCEL
     * option to not continue.
     */
    private boolean promptToSave() {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        AnimatedPoseurGUI gui = AnimatedPoseur.getAnimatedPoseur().getGUI();
        int selection = JOptionPane.showOptionDialog(gui,
                PROMPT_TO_SAVE_TEXT, PROMPT_TO_SAVE_TITLE_TEXT,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, null, null);

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection == JOptionPane.YES_OPTION) {
            poseIO.savePose(currentFile);
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

    /**
     * This helper method asks the user for a file to open. The user-selected
     * file is then loaded and the GUI updated. Note that if the user cancels
     * the open process, nothing is done. If an error occurs loading the file, a
     * message is displayed, but nothing changes.
     */
    private void promptToOpen() {
        // WE'LL NEED THE GUI
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();

        // AND NOW ASK THE USER FOR THE POSE TO OPEN
        JFileChooser poseFileChooser = new JFileChooser(POSES_PATH);
        int buttonPressed = poseFileChooser.showOpenDialog(gui);

        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (buttonPressed == JFileChooser.APPROVE_OPTION) {
            // GET THE FILE THE USER ENTERED
            currentFile = poseFileChooser.getSelectedFile();
            currentFileName = currentFile.getName();
            currentPoseName = currentFileName.substring(
                    0, currentFileName.indexOf("."));
            saved = true;

            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile);

            // AND LOAD THE .pose (XML FORMAT) FILE
            poseIO.loadPose(currentFile.getAbsolutePath());
        }
    }
    
    private void promptToOpenSpriteType() {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        
        JFileChooser spriteTypeFileChooser = new JFileChooser(SPRITETYPE_PATH);
        int buttonPressed = spriteTypeFileChooser.showOpenDialog(gui);
        
        if(buttonPressed == JFileChooser.APPROVE_OPTION) {
            
            currentFile = spriteTypeFileChooser.getSelectedFile();
            currentFileName = currentFile.getName();
            currentSpriteTypeName = currentFileName.substring(0, currentFileName.indexOf("."));
            saved = true;
            //String appName = gui.getAppName();
            gui.setTitle(currentFileName);
            poseurIO.loadSpriteType(currentSpriteTypeName);
            gui.refreshStateSelection();
        }
    }

    /**
     * This mutator method marks the file as not saved, which means that when
     * the user wants to do a file-type operation, we should prompt the user to
     * save current work first. Note that this method should be called any time
     * the pose is changed in some way.
     */
    public void markFileAsNotSaved() {
        saved = false;
    }

    /**
     * Accessor method for checking to see if the current pose has been saved
     * since it was last editing. If the current file matches the pose data,
     * we'll return true, otherwise false.
     *
     * @return true if the current pose is saved to the file, false otherwise.
     */
    public boolean isSaved() {
        return saved;
    }
}