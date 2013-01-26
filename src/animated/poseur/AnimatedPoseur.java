
package animated.poseur;

import static animated.poseur.AnimatedPoseurSettings.*;
import animatedPoseur.files.AnimatedPoseurFileManager;
import animatedPoseur.files.AnimatedPoseurGUILoader;
import animatedPoseur.files.AnimatedSpriteFileManager;
import animatedPoseur.gui.AnimatedPoseurGUI;
import animatedPoseur.state.AnimatedPoseurStateManager;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This project will be an application that can be used to create Animated
 * Sprites. If you've never heard of an Animated Sprite, then the first thing
 * you'll need to do is move out of your cave and into the 1980s. Why the 1980s?
 * Because in the 1980s, 2D Video Games like Super Mario Brothers pioneered the
 * use of this technology, employing charming little pixelated characters who
 * endeared themselves to hundreds of millions of gamers.
 *
 *
 * @author Xiufeng Yang
 */
public class AnimatedPoseur {

    private static AnimatedPoseur singleton = null;
    private AnimatedPoseurStateManager stateManager;
    private AnimatedPoseurFileManager fileManager;
    private AnimatedSpriteFileManager spriteFileManager;
    private boolean debugTextEnabled;
    private ArrayList<String> debugText;
    private AnimatedPoseurGUI gui;

    /**
     * This method initialize the singleton object, making sure it is only done
     * the first time this method is called. From there on out, anyone can
     * access this Poseur by calling this method.
     *
     * @return The singleton AnimatedPoseur used for this application.
     */
    public static AnimatedPoseur getAnimatedPoseur() {

        if (singleton == null) {
            singleton = new AnimatedPoseur();
        }
        return singleton;
    }

    /**
     * When called, this init method will fully initialize the application,
     * including loading the application settings from an xml file and using
     * that to setup the window. Notice that the constructor is a singleton
     * object, so we make the constructor private to prevent misuse. This method
     * has the obligation of initialization of the app.
     */
    public void init() {
        // INITIALIZE THE DATA MANAGER
        stateManager = new AnimatedPoseurStateManager();

        // INITIALIZE THE FILE MANAGER
        fileManager = new AnimatedPoseurFileManager();
        
        spriteFileManager = new AnimatedSpriteFileManager();

        // INITALIZE THE GUI
        gui = new AnimatedPoseurGUI();

        // WE'LL USE THIS FOR DEBUGGING
        debugText = new ArrayList();
        debugTextEnabled = DEFAULT_DEBUG_TEXT_ENABLED;

        // AND LOAD THE WINDOW SETTINGS FROM AN XML FILE
        AnimatedPoseurGUILoader pgl = new AnimatedPoseurGUILoader();
        pgl.initWindow(gui, WINDOW_SETINGS_XML);
    }

    /**
     * Accessor method for getting the application's GUI. Note that since this
     * is a singleton, anyone can get access to the GUI.
     *
     * @return The AnimatedPoseurFrame for this application, which is the window
     * for this application and contains references to all components and event
     * handlers in use.
     *
     * @return The GUI of this app
     */
    public AnimatedPoseurGUI getGUI() {
        return gui;
    }

    public AnimatedSpriteFileManager getSpriteFileManager() {
        return spriteFileManager;
    }
    
    

    /**
     * Accessor method for getting this application's state manager. Note that
     * since this is a singleton, anyone can get access to the state.
     *
     * @return The AnimatedPoseurDataManager for this application, which
     * contains all the data for the sprite being edited.
     */
    public AnimatedPoseurStateManager getStateManager() {
        return stateManager;
    }

    /**
     * Accessor method for getting this application's file manager. Note that
     * since this is a singleton, anyone can get access to this object.
     *
     * @return The AnimatedPoseurFileManager for this application, which
     * contains all the IO for the sprite being edited.
     */
    public AnimatedPoseurFileManager getFileManager() {
        return fileManager;
    }

    /**
     * Accessor method for testing if the debug text is enabled. If true, the
     * text will be rendered to the canvas, if false, it will not.
     *
     * @return true if the debug text rendering is enabled, false otherwise.
     */
    public boolean isDebugTextEnabled() {
        return debugTextEnabled;
    }

    /**
     * This accessor method gets an iterator for going through all the current
     * debug text sequentially.
     *
     * @return An iterator for the debug text list.
     */
    public Iterator<String> getDebugTextIterator() {
        return debugText.iterator();
    }

    /**
     * This method removes all debug text, and will typically be done after each
     * time we clear the canvases.
     */
    public void clearDebugText() {
        debugText.clear();
    }

    /**
     * Entry point for the AnimatedPoseur application, code starts its execution
     * here. This method simply inits the AnimatedPoseur singleton and starts up
     * the GUI.
     *
     * @param args the command line arguments (not used)
     */
    public static void main(String[] args) {
        AnimatedPoseur app = AnimatedPoseur.getAnimatedPoseur();
        app.init();
        AnimatedPoseurGUI animatedPoseurGUI = app.getGUI();
        animatedPoseurGUI.setVisible(true);
    }
}
