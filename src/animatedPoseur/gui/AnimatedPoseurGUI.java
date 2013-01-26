/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animatedPoseur.gui;

import animated.poseur.AnimatedPoseur;
import static animated.poseur.AnimatedPoseurSettings.*;
import animatedPoseur.files.AnimatedPoseurFileManager;
import animatedPoseur.files.AnimatedSpriteFileManager;
import animatedPoseur.files.ColorPalletLoader;
import animatedPoseur.shapes.PoseurShape;
import animatedPoseur.state.AnimatedPoseurStateManager;
import animatedPoseur.state.ColorPalletState;
import animatedPoseur.state.PoseCanvasState;
import animatedPoseur.state.PoseurState;
import events.canvas.PoseCanvasComponentHandler;
import events.canvas.PoseCanvasMouseHandler;
import events.colors.ChangeShapeTransparencyHandler;
import events.colors.ColorPalletHandler;
import events.colors.CustomColorHandler;
import events.colors.FillColorHandler;
import events.colors.OutlineColorHandler;
import events.edit.CopyHandler;
import events.edit.CutHandler;
import events.edit.MoveBackHandler;
import events.edit.MoveFrontHandler;
import events.edit.PasteHandler;
import events.edit.StartSelectionHandler;
import events.files.ExitHandler;
import events.files.ExportPoseHandler;
import events.files.NewPoseHandler;
import events.files.NewSpriteHandler;
import events.files.OpenPoseHandler;
import events.files.OpenSpriteHandler;
import events.files.SaveAsPoseHandler;
import events.files.SavePoseHandler;
import events.files.SaveSpriteAsHandler;
import events.files.SaveSpriteHandler;
import events.poseList.DeletePoseHandler;
import events.poseList.DuplicatePoseHandler;
import events.poseList.EditPoseHandler;
import events.poseList.MovePoseDownHandler;
import events.poseList.MovePoseUpHandler;
import events.poseList.PoseListHandler;
import events.poseList.SetDurationHandler;
import events.shapes.EllipseSelectionHandler;
import events.shapes.LineSelectionHandler;
import events.shapes.RectangleSelectionHandler;
import events.shapes.ShapeOutlineThicknessHandler;
import events.states.AnimationStateChooseHandler;
import events.states.DeleteStateHandler;
import events.states.DuplicateStateHandler;
import events.states.NewStateHandler;
import events.states.RenameStateHandler;
import events.viewer.SlowDownAnimationHandler;
import events.viewer.SpeedUpAnimationHandler;
import events.viewer.StartAnimationHandler;
import events.viewer.StopAnimationHandler;
import events.window.PoseurWindowHandler;
import events.zoom.ChangePoseDimensionsHandler;
import events.zoom.ZoomInHandler;
import events.zoom.ZoomOutHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import sprite_renderer.AnimationState;
import sprite_renderer.SceneRenderer;
import sprite_renderer.SpriteType;

/**
 * This class provides the full Graphical User Interface for the Animated Poseur
 * application. It contains references to all GUI components, including the
 * rendering surfaces, and has service methods for updating them
 *
 * @author Xiufeng Yang
 */
public class AnimatedPoseurGUI extends JFrame {

    // THE NAME OF THE APPLICATION, WE'LL PUT THIS IN THE 
    // WINDOW'S TITLE BAR, BUT MIGHT ALSO INCLUDE THE 
    // FILE NAME THERE
    protected String appName;
    // NORTH PANEL - EVERYTHING ELSE GOES IN HERE
    private JPanel northPanel;
    private JPanel northOfNorthPanel;
    private JPanel southOfNorthPanel;
    private JPanel southPanel;
    private JPanel westOfSouthPanel;
    // WE'LL HAVE TWO CANVASES IN THE CENTER, THE
    // ONE ON THE LEFT IS THE TRUE ONE, AND WILL NEVER
    // ZOOM THE VIEW. THE ONE ON THE RIGHT WILL ALLOW 
    // FOR ZOOMED IN AND OUT VIEWS. NOTE THAT WE'LL PUT
    // THEM INTO A SPLIT PANE
    private JSplitPane outerSplitPane;
    private JSplitPane canvasSplitPane;
    private PoseCanvas zoomableCanvas;
    private PoseCanvas trueCanvas;
    // THIS PANEL WILL RENDER OUR SPRITE
    private SceneRenderer sceneRenderingPanel;
    // FILE CONTROLS
    private JToolBar fileToolbar;
    private JButton newPoseurButton;
    private JButton openPoseurButton;
    private JButton savePoseurButton;
    private JButton savePoseurAsButton;
    private JButton exitButton;
    // Animation State Control
    private JComboBox animationStateSelection;
    private DefaultComboBoxModel animationStateModel;
    private JToolBar animationStateControl;
    private JButton newStateButton;
    private JButton renameStateButton;
    private JButton deleteStateButton;
    private JButton duplicateStateButton;
    //Pose File Control
    private JToolBar poseFileToolBar;
    private JButton newPoseButton;
    private JButton openPoseButton;
    private JButton savePoseButton;
    private JButton savePoseAsButton;
    private JButton exportImgButton;
    // Pose EDIT CONTROLS
    private JToolBar editToolBar;
    private ButtonGroup editButtonGroup;
    private JButton selectionButton;
    private JButton cutButton;
    private JButton copyButton;
    private JButton pasteButton;
    private JButton moveToBackButton;
    private JButton moveToFrontButton;
    // SHAPE SELECTION CONTROLS
    private JToolBar shapeToolbar;
    private JToggleButton lineToggleButton;
    private JToggleButton rectToggleButton;
    private JToggleButton ellipseToggleButton;
    private ButtonGroup shapeButtonGroup;
    private JComboBox lineStrokeSelectionComboBox;
    // Pose List and control
    private JScrollPane listPane;
    private JList poseList;
    private JPanel listPanel;
    private JToolBar poseControlToolBar;
    private JButton editPoseButton;
    private JButton deletePoseButton;
    private JButton duplicatePoseButton;
    private JToolBar poseSequenceToolBar;
    private JButton movePoseUpButton;
    private JButton movePoseDownButton;
    private JButton changeDurationButton;
    // ZOOM CONTROLS
    private JToolBar zoomToolbar;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JButton dimensionsButton;
    private JLabel zoomLabel;
    // COLOR SELECTION CONTROLS
    private JToolBar colorSelectionToolbar;
    private ColorToggleButton outlineColorSelectionButton;
    private ColorToggleButton fillColorSelectionButton;
    private ButtonGroup colorButtonGroup;
    private ColorPallet colorPallet;
    private JButton customColorSelectorButton;
    private JLabel alphaLabel;
    private JSlider transparencySlider;
    // SPRITE VIEWER CONTROLS
    private JToolBar viewerControlToolbar;
    private JButton startButton;
    private JButton stopButton;
    private JButton speedUpButton;
    private JButton slowDownButton;

    /**
     * Default constructor for initializing the GUI. Note that the Animated
     * Poseur application's state manager must have already been constructed and
     * setup when this method is called because we'll use some of the values
     * found there to initializer the GUI. The color pallet, for example, must
     * have already been loaded into the state manager before this constructor
     * is called.
     */
    public AnimatedPoseurGUI() {
        // IN CASE THE PARENT DOES ANYTHING, I USUALLY LIKE TO EXPLICITY INCLUDE THIS
        super();

        // CONSTRUCT AND LAYOUT THE COMPONENTS
        initGUI();

        // AND SETUP THE HANDLERS
        initHandlers();

        // ENABLE AND DISABLE ALL CONTROLS AS NEEDED
        updateMode();
    }
    
    public void setPoseList(JList poseList) {
        this.poseList = poseList;
    }

    public JList getPoseList() {
        return poseList;
    }

    public SceneRenderer getSceneRenderingPanel() {
        return sceneRenderingPanel;
    }

    public void setSceneRenderingPanel(SceneRenderer sceneRenderingPanel) {
        this.sceneRenderingPanel = sceneRenderingPanel;
    }

    public JComboBox getAnimationStateSelection() {
        return animationStateSelection;
    }

    public DefaultComboBoxModel getAnimationStateModel() {
        return animationStateModel;
    }

    /**
     * This helper method constructs and lays out all GUI components,
     * initializing them to their default startup state.
     */
    private void initGUI() {
        // MAKE THE COMPONENTS
        constructGUIControls();

        // AND ARRANGE THEM
        layoutGUIControls();
        sceneRenderingPanel.startScene();
//        initHandlers();
//        setLocation(10,10);
//        setSize(1152,640);
//        setResizable(false);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //updateMode();
    }

    /**
     * GUI setup method can be quite lengthy and repetitive so it helps to
     * create helper methods that can do a bunch of things at once. This method
     * creates a button with a bunch of premade values. Note that we are using
     * Java reflection here, to make an object based on what class type it has.
     *
     * @param imageFile The image to use for the button.
     *
     * @param parent The container inside which to put the button.
     *
     * @param tracker This makes sure our button fully loads.
     *
     * @param id A unique id for the button so the tracker knows it's there.
     *
     * @param buttonType The type of button, we'll use reflection for making it.
     *
     * @param bg Some buttons will go into groups where only one may be selected
     * at a time.
     *
     * @param tooltip The mouse-over text for the button.
     *
     * @return A fully constructed and initialized button with all the data
     * provided to it as arguments.
     */
    private AbstractButton initButton(String imageFile,
            Container parent,
            MediaTracker tracker,
            int id,
            Class buttonType,
            ButtonGroup bg,
            String tooltip) {
        try {
            // LOAD THE IMAGE AND MAKE AN ICON
            Image img = batchLoadImage(imageFile, tracker, id);
            ImageIcon ii = new ImageIcon(img);

            // HERE'S REFLECTION MAKING OUR OBJECT USING IT'S CLASS
            // NOTE THAT DOING IT LIKE THIS CALLS THE buttonType
            // CLASS' DEFAULT CONSTRUCTOR, SO WE MUST MAKE SURE IT HAS ONE
            AbstractButton createdButton;
            createdButton = (AbstractButton) buttonType.newInstance();

            // NOW SETUP OUR BUTTON FOR USE
            createdButton.setIcon(ii);
            createdButton.setToolTipText(tooltip);
            parent.add(createdButton);

            // INSETS ARE SPACING INSIDE THE BUTTON,
            // TOP LEFT RIGHT BOTTOM
            Insets buttonMargin = new Insets(
                    BUTTON_INSET, BUTTON_INSET, BUTTON_INSET, BUTTON_INSET);
            createdButton.setMargin(buttonMargin);

            // ADD IT TO ITS BUTTON GROUP IF IT'S IN ONE
            if (bg != null) {
                bg.add(createdButton);
            }
            // AND RETURN THE SETUP BUTTON
            return createdButton;
        } catch (InstantiationException | IllegalAccessException ex) {
            // WE SHOULD NEVER GET THIS ERROR, BUT WE HAVE TO PUT
            // A TRY CATCH BECAUSE WE'RE USING REFLECTION TO DYNAMICALLY
            // CONSTRUCT OUR BUTTONS BY CLASS NAME
            Logger.getLogger(AnimatedPoseurGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        // THIS WOULD MEAN A FAILURE OF SOME SORT OCCURED
        return null;
    }

    /**
     * Helper method that constructs all the GUI controls and loads them with
     * their necessary art and data.
     */
    private void constructGUIControls() {
        // SOME COMPONENTS MAY NEED THE STATE MANAGER
        // FOR INITIALIZATION, SO LET'S GET IT
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();

        // LET'S START BY INITIALIZING THE CENTER AREA,
        // WHERE WE'LL RENDER EVERYTHING. WE'LL HAVE TWO
        // CANVASES AND PUT THEM INTO DIFFERENT SIDES
        // OF A JSplitPane
        outerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        canvasSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // LET'S MAKE THE CANVAS ON THE LEFT SIDE, WHICH
        // WILL NEVER ZOOM
        PoseCanvasState trueCanvasState = poseurStateManager.getTrueCanvasState();
        trueCanvas = new PoseCanvas(trueCanvasState);
        trueCanvasState.setPoseCanvas(trueCanvas);
        trueCanvas.setBackground(TRUE_CANVAS_COLOR);
        //trueCanvas.setVisible(false);

        // AND NOW THE CANVAS ON THE RIGHT SIDE, WHICH
        // WILL BE ZOOMABLE
        PoseCanvasState zoomableCanvasState = poseurStateManager.getZoomableCanvasState();
        zoomableCanvas = new PoseCanvas(zoomableCanvasState);
        zoomableCanvasState.setPoseCanvas(zoomableCanvas);
        zoomableCanvas.setBackground(ZOOMABLE_CANVAS_COLOR);

        sceneRenderingPanel = new SceneRenderer(singleton.getStateManager().getSpriteToRender());
        sceneRenderingPanel.setPreferredSize(new Dimension(400, 400));


        // ULTIMATELY EVERYTHING IN THE NORTH GOES IN HERE, INCLUDING
        // TWO PANELS FULL OF JToolBars
        northPanel = new JPanel();
        northOfNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southOfNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // WE'LL BATCH LOAD THE IMAGES
        MediaTracker tracker = new MediaTracker(this);
        int idCounter = 0;

        //Application File control
        fileToolbar = new JToolBar();
        newPoseurButton = (JButton) initButton(NEW_IMAGE_FILE, fileToolbar, tracker, idCounter++, JButton.class, null, NEW_POSEUR_TOOLTIP);
        openPoseurButton = (JButton) initButton(OPEN_IMAGE_FILE, fileToolbar, tracker, idCounter++, JButton.class, null, OPEN_POSEUR_TOOLTIP);
        savePoseurButton = (JButton) initButton(SAVE_IMAGE_FILE, fileToolbar, tracker, idCounter++, JButton.class, null, SAVE_POSEUR_TOOLTIP);
        savePoseurAsButton = (JButton) initButton(SAVE_AS_IMAGE_FILE, fileToolbar, tracker, idCounter++, JButton.class, null, SAVE_POSEUR_AS_TOOLTIP);
        exitButton = (JButton) initButton(EXIT_IMAGE_FILE, fileToolbar, tracker, idCounter++, JButton.class, null, EXIT_TOOLTIP);

        // View Controls
        viewerControlToolbar = new JToolBar();
        startButton = (JButton) initButton(START_IMAGE_FILE, viewerControlToolbar, tracker, idCounter++, JButton.class, null, START_TOOLTIP);
        stopButton = (JButton) initButton(STOP_IMAGE_FILE, viewerControlToolbar, tracker, idCounter++, JButton.class, null, STOP_TOOLTIP);
        speedUpButton = (JButton) initButton(SPEED_UP_IMAGE_FILE, viewerControlToolbar, tracker, idCounter++, JButton.class, null, SPEED_UP_TOOLTIP);
        slowDownButton = (JButton) initButton(SLOW_DOWN_IMAGE_FILE, viewerControlToolbar, tracker, idCounter++, JButton.class, null, SLOW_DOWN_TOOLTIP);

        // Animation State Controls
        animationStateControl = new JToolBar();
        animationStateSelection = new JComboBox();
        animationStateModel = new DefaultComboBoxModel();
        animationStateSelection.setModel(animationStateModel);
        animationStateModel.addElement("Select Animation State");
        JPanel stateChoosePanel = new JPanel(new GridLayout(2, 1));
        stateChoosePanel.setMaximumSize(new Dimension(180, 55));
        stateChoosePanel.setAlignmentY((float) 0.53);
        JLabel stateChooseLabel = new JLabel("Choose Animation State: ");
        stateChoosePanel.add(stateChooseLabel);
        stateChoosePanel.add(animationStateSelection);
        animationStateControl.add(stateChoosePanel);
        newStateButton = (JButton) initButton(NEW_IMAGE_FILE, animationStateControl, tracker, idCounter++, JButton.class, null, NEW_STATE_TOOLTIP);
        renameStateButton = (JButton) initButton(EDIT_IMAGE_FILE, animationStateControl, tracker, idCounter++, JButton.class, null, RENAME_STATE_TOOLTIP);
        deleteStateButton = (JButton) initButton(DELETE_IMAGE_FILE, animationStateControl, tracker, idCounter++, JButton.class, null, DELETE_STATE_TOOLTIP);
        duplicateStateButton = (JButton) initButton(COPY_IMAGE_FILE, animationStateControl, tracker, idCounter++, JButton.class, null, DUPLICATE_STATE_TOOLTIP);


        // Pose Editor File Controls
        poseFileToolBar = new JToolBar();
        newPoseButton = (JButton) initButton(NEW_IMAGE_FILE, poseFileToolBar, tracker, idCounter++, JButton.class, null, NEW_POSE_TOOLTIP);
        openPoseButton = (JButton) initButton(OPEN_IMAGE_FILE, poseFileToolBar, tracker, idCounter++, JButton.class, null, OPEN_POSE_TOOLTIP);
        savePoseButton = (JButton) initButton(SAVE_IMAGE_FILE, poseFileToolBar, tracker, idCounter++, JButton.class, null, SAVE_POSE_TOOLTIP);
        savePoseAsButton = (JButton) initButton(SAVE_AS_IMAGE_FILE, poseFileToolBar, tracker, idCounter++, JButton.class, null, SAVE_POSE_AS_TOOLTIP);
        exportImgButton = (JButton) initButton(EXPORT_IMAGE_FILE, poseFileToolBar, tracker, idCounter++, JButton.class, null, EXPORT_TOOLTIP);

        // EDITING CONTROLS
        editToolBar = new JToolBar();
        selectionButton = (JButton) initButton(SELECTION_IMAGE_FILE, editToolBar, tracker, idCounter++, JButton.class, null, SELECT_TOOLTIP);
        cutButton = (JButton) initButton(CUT_IMAGE_FILE, editToolBar, tracker, idCounter++, JButton.class, null, CUT_TOOLTIP);
        copyButton = (JButton) initButton(COPY_IMAGE_FILE, editToolBar, tracker, idCounter++, JButton.class, null, COPY_TOOLTIP);
        pasteButton = (JButton) initButton(PASTE_IMAGE_FILE, editToolBar, tracker, idCounter++, JButton.class, null, PASTE_TOOLTIP);
        moveToBackButton = (JButton) initButton(MOVE_TO_BACK_IMAGE_FILE, editToolBar, tracker, idCounter++, JButton.class, null, MOVE_TO_BACK_TOOLTIP);
        moveToFrontButton = (JButton) initButton(MOVE_TO_FRONT_IMAGE_FILE, editToolBar, tracker, idCounter++, JButton.class, null, MOVE_TO_FRONT_TOOLTIP);

        // HERE ARE OUR SHAPE SELECTION CONTROLS
        shapeToolbar = new JToolBar();
        shapeButtonGroup = new ButtonGroup();
        lineToggleButton = (JToggleButton) initButton(LINE_SELECTION_IMAGE_FILE, shapeToolbar, tracker, idCounter++, JToggleButton.class, shapeButtonGroup, LINE_TOOLTIP);
        rectToggleButton = (JToggleButton) initButton(RECT_SELECTION_IMAGE_FILE, shapeToolbar, tracker, idCounter++, JToggleButton.class, shapeButtonGroup, RECT_TOOLTIP);
        ellipseToggleButton = (JToggleButton) initButton(ELLIPSE_SELECTION_IMAGE_FILE, shapeToolbar, tracker, idCounter++, JToggleButton.class, shapeButtonGroup, ELLIPSE_TOOLTIP);

        // THE LINE THICKNESS SELECTION COMBO BOX WILL GO WITH THE SHAPE CONTROLS
        DefaultComboBoxModel lineThicknessModel = new DefaultComboBoxModel();
        for (int i = 0; i < NUM_STROKES_TO_CHOOSE_FROM; i++) {
            String imageFileName = STROKE_SELECTION_FILE_PREFIX
                    + (i + 1)
                    + PNG_FILE_EXTENSION;
            Image img = batchLoadImage(imageFileName, tracker, idCounter++);
            ImageIcon ii = new ImageIcon(img);
            lineThicknessModel.addElement(ii);
        }
        lineStrokeSelectionComboBox = new JComboBox(lineThicknessModel);

        //The Pose list
        westOfSouthPanel = new JPanel();
        westOfSouthPanel.setPreferredSize(new Dimension(180, 400));
        westOfSouthPanel.setLayout(new BorderLayout());
        poseList = new JList(new DefaultListModel());
        listPane = new JScrollPane(poseList);
        poseControlToolBar = new JToolBar();
        poseSequenceToolBar = new JToolBar();
        changeDurationButton = (JButton) initButton(CHANGE_DURATION_IMAGE_FILE, poseSequenceToolBar, tracker, idCounter++, JButton.class, null, CHANGE_POSE_DURATION_TOOLTIP);
        movePoseDownButton = (JButton) initButton(MOVE_POSE_DOWN_IMAGE_FILE, poseSequenceToolBar, tracker, idCounter++, JButton.class, null, MOVE_POSE_DOWN_TOOLTIP);
        movePoseUpButton = (JButton) initButton(MOVE_POSE_UP_IMAGE_FILE, poseSequenceToolBar, tracker, idCounter++, JButton.class, null, MOVE_POSE_UP_TOOLTIP);
        editPoseButton = (JButton) initButton(EDIT_IMAGE_FILE, poseControlToolBar, tracker, idCounter++, JButton.class, null, EDIT_TOOLTIP);
        deletePoseButton = (JButton) initButton(DELETE_IMAGE_FILE, poseControlToolBar, tracker, idCounter++, JButton.class, null, DELETE_TOOLTIP);
        duplicatePoseButton = (JButton) initButton(COPY_IMAGE_FILE, poseControlToolBar, tracker, idCounter++, JButton.class, null, DUPLICATE_TOOLTIP);
        westOfSouthPanel.add(poseControlToolBar, BorderLayout.NORTH);
        westOfSouthPanel.add(poseSequenceToolBar, BorderLayout.SOUTH);
        westOfSouthPanel.add(listPane, BorderLayout.CENTER);


        // NOW THE ZOOM TOOLBAR
        zoomToolbar = new JToolBar();
        zoomOutButton = (JButton) initButton(ZOOM_OUT_IMAGE_FILE, zoomToolbar, tracker, idCounter++, JButton.class, null, ZOOM_OUT_TOOLTIP);
        zoomInButton = (JButton) initButton(ZOOM_IN_IMAGE_FILE, zoomToolbar, tracker, idCounter++, JButton.class, null, ZOOM_IN_TOOLTIP);
        zoomLabel = new JLabel();
        zoomLabel.setFont(ZOOM_LABEL_FONT);
        updateZoomLabel();
        dimensionsButton = (JButton) initButton(POSE_DIMENSIONS_IMAGE_FILE, zoomToolbar, tracker, idCounter++, JButton.class, null, CHANGE_POSE_DIMENSIONS_TOOLTIP);

        // COLOR SELECTION CONTROLS
        colorSelectionToolbar = new JToolBar();
        colorButtonGroup = new ButtonGroup();
        outlineColorSelectionButton = (ColorToggleButton) initButton(OUTLINE_COLOR_IMAGE_FILE, colorSelectionToolbar, tracker, idCounter++, ColorToggleButton.class, colorButtonGroup, OUTLINE_TOOLTIP);
        outlineColorSelectionButton.setBackground(Color.BLACK);
        fillColorSelectionButton = (ColorToggleButton) initButton(FILL_COLOR_IMAGE_FILE, colorSelectionToolbar, tracker, idCounter++, ColorToggleButton.class, colorButtonGroup, FILL_TOOLTIP);
        fillColorSelectionButton.setBackground(Color.WHITE);
        outlineColorSelectionButton.setSelected(true);

        // AND LET'S LOAD THE COLOR PALLET FROM AN XML FILE
        ColorPalletLoader cpl = new ColorPalletLoader();
        ColorPalletState cps = new ColorPalletState();
        cpl.initColorPallet(COLOR_PALLET_SETTINGS_XML, cps);

        // NOW LET'S SETUP THE COLOR PALLET. USING THE
        // STATE WE JUST LOADED. NOW MAKE OUR COLOR PALLET
        // AND MAKE SURE THEY KNOW ABOUT ONE ANOTHER
        colorPallet = new ColorPallet(cps);
        cps.setView(colorPallet);

        // THIS CONTROL WILL LET US CHANGE THE COLORS IN THE COLOR PALLET
        customColorSelectorButton = (JButton) initButton(CUSTOM_COLOR_SELECTOR_IMAGE_FILE, colorSelectionToolbar, tracker, idCounter++, JButton.class, null, CUSTOM_COLOR_TOOLTIP);

        // AND THE TRANSPARENCY SLIDER AND LABEL
        alphaLabel = new JLabel(ALPHA_LABEL_TEXT);
        alphaLabel.setFont(ALPHA_LABEL_FONT);
        alphaLabel.setBackground(ALPHA_BACKGROUND_COLOR);
        transparencySlider = new JSlider(JSlider.HORIZONTAL, TRANSPARENT, OPAQUE, OPAQUE);
        transparencySlider.setBackground(ALPHA_BACKGROUND_COLOR);
        transparencySlider.setMajorTickSpacing(ALPHA_MAJOR_TICK_SPACING);
        transparencySlider.setMinorTickSpacing(ALPHA_MINOR_TICK_SPACING);
        transparencySlider.setPaintLabels(true);
        transparencySlider.setPaintTicks(true);
        transparencySlider.setPaintTrack(true);
        transparencySlider.setToolTipText(ALPHA_TOOLTIP);
        transparencySlider.setSnapToTicks(false);

        // NOW WE NEED TO WAIT FOR ALL THE IMAGES THE
        // MEDIA TRACKER HAS BEEN GIVEN TO FULLY LOAD
        try {
            tracker.waitForAll();
        } catch (InterruptedException ie) {
            // LOG THE ERROR
            Logger.getLogger(AnimatedPoseurGUI.class.getName()).log(Level.SEVERE, null, ie);
        }
    }

    /**
     * This helper method locates all the components inside the frame. Note that
     * it does not put most buttons into their proper toolbars because that was
     * already done for most when they were initialized by initButton.
     */
    private void layoutGUIControls() {
        // LET'S PUT THE TWO CANVASES INSIDE 
        // THE SPLIT PANE. WE'LL PUT THE DIVIDER
        // RIGHT IN THE MIDDLE AND WON'T LET
        // THE USER MOVE IT - FOOLPROOF DESIGN!
        zoomableCanvas.add(colorSelectionToolbar);
        //zoomableCanvas.add(customColorSelectorButton);
        //canvasSplitPane.setRightComponent(sceneRenderingPanel);
        canvasSplitPane.setLeftComponent(trueCanvas);
        canvasSplitPane.setRightComponent(zoomableCanvas);
        canvasSplitPane.setResizeWeight(0);
        canvasSplitPane.setDividerLocation(0);
        canvasSplitPane.setDividerSize(0);
        canvasSplitPane.setEnabled(false);
        outerSplitPane.setLeftComponent(canvasSplitPane);
        outerSplitPane.setRightComponent(sceneRenderingPanel);
        outerSplitPane.setResizeWeight(0.2);
        outerSplitPane.setEnabled(false);

        // PUT THE COMBO BOX IN THE SHAPE TOOLBAR
        shapeToolbar.add(lineStrokeSelectionComboBox);


        // ARRANGE THE COLOR SELECTION TOOLBAR
        colorSelectionToolbar.add(colorPallet);
        colorSelectionToolbar.add(customColorSelectorButton);
        //colorSelectionToolbar.add(alphaLabel);
        //colorSelectionToolbar.add(transparencySlider);

        zoomToolbar.add(zoomLabel);
        // NOW ARRANGE THE TOOLBARS
        northOfNorthPanel.add(fileToolbar);
        northOfNorthPanel.add(animationStateControl);
        //northOfNorthPanel.add(colorSelectionToolbar);
        northOfNorthPanel.add(alphaLabel);
        northOfNorthPanel.add(transparencySlider);
        northOfNorthPanel.add(viewerControlToolbar);
        southOfNorthPanel.add(poseFileToolBar);
        southOfNorthPanel.add(editToolBar);
        southOfNorthPanel.add(shapeToolbar);
        southOfNorthPanel.add(zoomToolbar);

        // NOW PUT ALL THE CONTROLS IN THE NORTH
        northPanel.setLayout(new BorderLayout());
        northPanel.add(northOfNorthPanel, BorderLayout.NORTH);
        northPanel.add(southOfNorthPanel, BorderLayout.SOUTH);

        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(westOfSouthPanel, BorderLayout.WEST);
        southPanel.add(outerSplitPane, BorderLayout.CENTER);

        // AND NOW PUT EVERYTHING INSIDE THE FRAME
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.CENTER);
    }

    /**
     * This method constructs and registers all the event handlers for all the
     * GUI controls.
     */
    private void initHandlers() {
        // THIS WILL HANDLE THE SCENARIO WHEN THE USER CLICKS ON
        // THE X BUTTON, WE'LL WANT A CUSTOM RESPONSE
        PoseurWindowHandler pwh = new PoseurWindowHandler();
        this.addWindowListener(pwh);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //Animated Poseur file handler
        NewSpriteHandler nsh = new NewSpriteHandler();
        newPoseurButton.addActionListener(nsh);
        OpenSpriteHandler osh = new OpenSpriteHandler();
        openPoseurButton.addActionListener(osh);
        SaveSpriteHandler ssh = new SaveSpriteHandler();
        savePoseurButton.addActionListener(ssh);
        SaveSpriteAsHandler ssah = new SaveSpriteAsHandler();
        savePoseurAsButton.addActionListener(ssah);

        //Animated State toolbar handler
        AnimationStateChooseHandler asch = new AnimationStateChooseHandler();
        animationStateSelection.addItemListener(asch);
        NewStateHandler nsth = new NewStateHandler();
        newStateButton.addActionListener(nsth);
        RenameStateHandler rnsh = new RenameStateHandler();
        renameStateButton.addActionListener(rnsh);
        DeleteStateHandler dsh = new DeleteStateHandler();
        deleteStateButton.addActionListener(dsh);
        DuplicateStateHandler dpsh = new DuplicateStateHandler();
        duplicateStateButton.addActionListener(dpsh);

        // Animation viewer toolbar handler
        StartAnimationHandler starth = new StartAnimationHandler(sceneRenderingPanel);
        startButton.addActionListener(starth);
        StopAnimationHandler stoph = new StopAnimationHandler(sceneRenderingPanel);
        stopButton.addActionListener(stoph);
        SpeedUpAnimationHandler spduh = new SpeedUpAnimationHandler(sceneRenderingPanel);
        speedUpButton.addActionListener(spduh);
        SlowDownAnimationHandler slowdh = new SlowDownAnimationHandler(sceneRenderingPanel);
        slowDownButton.addActionListener(slowdh);

        // FILE TOOLBAR HANDLER
        NewPoseHandler nph = new NewPoseHandler();
        newPoseButton.addActionListener(nph);
        OpenPoseHandler oph = new OpenPoseHandler();
        openPoseButton.addActionListener(oph);
        SavePoseHandler sph = new SavePoseHandler();
        savePoseButton.addActionListener(sph);
        SaveAsPoseHandler saph = new SaveAsPoseHandler();
        savePoseAsButton.addActionListener(saph);
        ExportPoseHandler eph = new ExportPoseHandler();
        exportImgButton.addActionListener(eph);
        ExitHandler eh = new ExitHandler();
        exitButton.addActionListener(eh);

        // EDIT TOOLBAR HANDLER
        StartSelectionHandler startSH = new StartSelectionHandler();
        selectionButton.addActionListener(startSH);
        CutHandler cutEh = new CutHandler();
        cutButton.addActionListener(cutEh);
        CopyHandler copyEh = new CopyHandler();
        copyButton.addActionListener(copyEh);
        PasteHandler pasteEh = new PasteHandler();
        pasteButton.addActionListener(pasteEh);
        MoveBackHandler mvBackEh = new MoveBackHandler();
        moveToBackButton.addActionListener(mvBackEh);
        MoveFrontHandler mvFrontEh = new MoveFrontHandler();
        moveToFrontButton.addActionListener(mvFrontEh);

        // SHAPE SELECTION HANDLERS
        LineSelectionHandler lsh = new LineSelectionHandler();
        lineToggleButton.addActionListener(lsh);
        RectangleSelectionHandler rsh = new RectangleSelectionHandler();
        rectToggleButton.addActionListener(rsh);
        EllipseSelectionHandler esh = new EllipseSelectionHandler();
        ellipseToggleButton.addActionListener(esh);
        ShapeOutlineThicknessHandler otesh = new ShapeOutlineThicknessHandler();
        lineStrokeSelectionComboBox.addItemListener(otesh);


        // ZOOM HANDLERS
        ZoomInHandler zih = new ZoomInHandler();
        zoomInButton.addActionListener(zih);
        ZoomOutHandler zoh = new ZoomOutHandler();
        zoomOutButton.addActionListener(zoh);
        ChangePoseDimensionsHandler cpdh = new ChangePoseDimensionsHandler();
        dimensionsButton.addActionListener(cpdh);

        //POSE LIST HANDLERS
        PoseListHandler plh = new PoseListHandler();
        poseList.addListSelectionListener(plh);
        EditPoseHandler editph = new EditPoseHandler();
        editPoseButton.addActionListener(editph);
        DeletePoseHandler delph = new DeletePoseHandler();
        deletePoseButton.addActionListener(delph);
        DuplicatePoseHandler dupph = new DuplicatePoseHandler();
        duplicatePoseButton.addActionListener(dupph);
        MovePoseUpHandler mvuph = new MovePoseUpHandler();
        movePoseUpButton.addActionListener(mvuph);
        MovePoseDownHandler mvdph = new MovePoseDownHandler();
        movePoseDownButton.addActionListener(mvdph);
        SetDurationHandler sdh = new SetDurationHandler();
        changeDurationButton.addActionListener(sdh);

        // COLOR CONTROL HANDLERS
        OutlineColorHandler acal = new OutlineColorHandler();
        outlineColorSelectionButton.addActionListener(acal);
        FillColorHandler fcal = new FillColorHandler();
        fillColorSelectionButton.addActionListener(fcal);
        ColorPalletHandler cph = new ColorPalletHandler();
        colorPallet.registerColorPalletHandler(cph);
        CustomColorHandler cch = new CustomColorHandler();
        customColorSelectorButton.addActionListener(cch);
        ChangeShapeTransparencyHandler cth = new ChangeShapeTransparencyHandler();
        transparencySlider.addChangeListener(cth);

        // CANVAS MOUSE HANDLERS
        PoseCanvasMouseHandler rsmh = new PoseCanvasMouseHandler();
        zoomableCanvas.addMouseListener(rsmh);
        zoomableCanvas.addMouseMotionListener(rsmh);

        // THIS HANDLER IS CALLED WHEN THE COMPONENT IS 
        // FIRST SIZED TO BE DISPLAYED. WE WISH TO CALCULATE
        // THE POSE AREA AT THAT TIME, SO WE'LL DO IT FOR
        // BOTH CANVASES
        PoseCanvasComponentHandler pcch = new PoseCanvasComponentHandler();
        trueCanvas.addComponentListener(pcch);
        zoomableCanvas.addComponentListener(pcch);
    }

    /**
     * This method helps us load a bunch of images and ensure they are fully
     * loaded when we want to use them.
     *
     * @param imageFile The path and name of the image file to load.
     *
     * @param tracker This will help ensure all the images are loaded.
     *
     * @param id A unique identifier for each image in the tracker. It will only
     * wait for ids it knows about.
     *
     * @return A constructed image that has been registered with the tracker.
     * Note that the image's data has not necessarily been fully loaded when
     * this method ends.
     */
    private Image batchLoadImage(String imageFile, MediaTracker tracker, int id) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage(imageFile);
        tracker.addImage(img, id);
        return img;
    }

    /**
     * This method updates the zoom label display with the current zoom level.
     */
    public void updateZoomLabel() {
        // GET THE RIGHT CANVAS STATE, SINCE IT ZOOMS
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();
        PoseCanvasState zoomableCanvasState = poseurStateManager.getZoomableCanvasState();

        // GET THE ZOOM LEVEL
        float zoomLevel = zoomableCanvasState.getZoomLevel();

        // MAKE IT LOOK NICE
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(1);
        String zoomText = ZOOM_LABEL_TEXT_PREFIX
                + nf.format(zoomLevel)
                + ZOOM_LABEL_TEXT_POSTFIX;

        // AND PUT IT IN THE LABEL
        zoomLabel.setText(zoomText);
    }

    /**
     * Called each time the application's state changes, this method is
     * responsible for enabling, disabling, and generally updating all the GUI
     * control based on what the current application state (i.e. the PoseurMode)
     * is in.
     */
    public final void updateMode() {
        // WE'LL NEED THESE GUYS
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager state = singleton.getStateManager();
        PoseurState mode = state.getMode();
        AnimatedPoseurFileManager fileManager = singleton.getFileManager();
        AnimatedSpriteFileManager spriteManager = singleton.getSpriteFileManager();

        if (mode == PoseurState.POSE_SELECTED_STATE) {
            setEnabledPoseListControls(true);
        }
        // IN THIS MODE THE USER IS DRAGGING THE MOUSE TO
        // COMPLETE THE DRAWING OF A SINGLE SHAPE
        else if (mode == PoseurState.SELECT_ANIMATION_STATE){
            savePoseurAsButton.setEnabled(true);
            animationStateSelection.setEnabled(true);
            newStateButton.setEnabled(true);
            setEnabledEditControls(false);
            setEnabledPoseListControls(false);
        }
        else if (mode == PoseurState.STATE_SELECTED_STATE) {
            setEnabledAnimationStateControl(true);
            setEnabledViewerControls(true);
            enableStartupFileControls();
            setEnabledPoseListControls(false);
            selectCursor(Cursor.DEFAULT_CURSOR);
        }
        else if (mode == PoseurState.COMPLETE_SHAPE_STATE) {
            // THIS USES THE CROSSHAIR
            selectCursor(Cursor.CROSSHAIR_CURSOR);
            setEnabledPoseListControls(false);
        } // IN THIS MODE THE USER IS ABOUT TO START DRAGGING
        // THE MOUSE TO CREATE A SHAPE
        else if (mode == PoseurState.CREATE_SHAPE_STATE) {
            // THIS USES THE CROSSHAIR
            selectCursor(Cursor.CROSSHAIR_CURSOR);

            // TURN THE APPROPRIATE CONTROLS ON/OFF
            setEnabledEditControls(false);
            selectionButton.setEnabled(true);
            setEnabledPoseListControls(false);
        } // IN THIS STATE THE USER HAS SELECTED A SHAPE
        // ON THE CANVAS AND IS DRAGGING IT
        else if (mode == PoseurState.DRAG_SHAPE_STATE) {
            // THIS USES THE MOVE 
            selectCursor(Cursor.MOVE_CURSOR);
            setEnabledPoseListControls(false);
        } // IN THIS STATE THE USER IS ABLE TO CLICK ON
        // A SHAPE TO SELECT IT. THIS IS THE MOST COMMON
        // STATE AND IS THE DEFAULT AT THE START OF THE APP
        else if (mode == PoseurState.SELECT_SHAPE_STATE) {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            setEnabledPoseListControls(false);

            // THERE IS NO SHAPE SELECTED, SO WE CAN'T
            // USE THE EDIT CONTROLS
            enableSaveAsAndExport();
            setEnabledEditControls(false);
            selectionButton.setEnabled(false);
            setEnabledColorControls(true);
            setEnabledShapeControls(true);
            setEnabledZoomControls(true);
        } // IN THIS STATE A SHAPE HAS BEEN SELECTED AND SO WE
        // MAY EDIT IT, LIKE CHANGE IT'S COLORS OR TRANSPARENCY
        else if (mode == PoseurState.SHAPE_SELECTED_STATE) {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            setEnabledPoseListControls(false);

            // THE EDIT CONTROLS CAN NOW BE USED
            setEnabledEditControls(true);
        } // THIS IS THE STATE WHEN THE Poseur APP FIRST
        // STARTS. THERE IS NO Pose YET, SO MOST CONTROLS
        // ARE DISABLED
        else if (mode == PoseurState.STARTUP_STATE) {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            setEnabledPoseListControls(false);
            setEnabledAnimationStateControl(false);
            // NOTHING IS SELECTED SO WE CAN'T EDIT YET
            enableStartupFileControls();
            setEnabledEditControls(false);
            selectionButton.setEnabled(false);
            setEnabledColorControls(false);
            toggleOutlineColorButton();
            setEnabledZoomControls(false);
            setEnabledShapeControls(false);
            setEnabledViewerControls(false);
            animationStateSelection.setEnabled(false);
            savePoseurAsButton.setEnabled(false);
            newPoseButton.setEnabled(false);
            openPoseButton.setEnabled(false);
        }
        savePoseButton.setEnabled(!fileManager.isSaved());
        savePoseurButton.setEnabled(!spriteManager.isSaved());

        // AND UPDATE THE SLIDER
        PoseurShape selectedShape = state.getSelectedShape();
        if (selectedShape != null) {
            // UPDATE THE SLIDER ACCORDING TO THE SELECTED
            // SHAPE'S ALPHA (TRANSPARENCY) VALUE, IF THERE
            // EVEN IS A SELECTED SHAPE
            transparencySlider.setValue(selectedShape.getAlpha());
        }

        // REDRAW EVERYTHING
        trueCanvas.repaint();
        zoomableCanvas.repaint();
    }

    /**
     * Accessor method for getting the color currently set for filling shapes.
     *
     * @return The fill color currently in use for making shapes.
     */
    public Color getFillColor() {
        return fillColorSelectionButton.getBackground();
    }

    /**
     * Accessor method for getting the color currently set four outlining
     * shapes.
     *
     * @return The outline color currently in use for making shapes.
     */
    public Color getOutlineColor() {
        return outlineColorSelectionButton.getBackground();
    }

    /**
     * Accessor method to test if the outline color toggle button is selected.
     * Note that either the outline or fill button must be selected at all
     * times.
     *
     * @return true if the outline toggle button is selected, false if the fill
     * button is selected.
     */
    public boolean isOutlineColorSelectionButtonToggled() {
        return outlineColorSelectionButton.isSelected();
    }

    /**
     * Accessor method for getting the line thickness currently set for drawing
     * shape outlines and lines.
     *
     * @return The line thickness currently in use for making shapes.
     */
    public int getLineThickness() {
        return lineStrokeSelectionComboBox.getSelectedIndex() + 1;
    }

    /**
     * Accessor method for getting the current transparency value of the slider.
     *
     * @return The alpha value, between 0 (fully transparent) and 255 (fully
     * opaque) as currently set by the transparency slider.
     */
    public int getAlphaTransparency() {
        return transparencySlider.getValue();
    }

    /**
     * This mutator method sets the background color for the outline toggle
     * button, which can then be used for the outline of new shapes.
     *
     * @param initColor The color to use for shape outlines.
     */
    public void setOutlineToggleButtonColor(Color initColor) {
        outlineColorSelectionButton.setBackground(initColor);
    }

    /**
     * This mutator method sets the background color for the fill toggle button,
     * which can then be used for the fill color of new shapes.
     *
     * @param initColor The color to use for shape filling.
     */
    public void setFillToggleButtonColor(Color initColor) {
        fillColorSelectionButton.setBackground(initColor);
    }

    /**
     * Accessor method for getting the application's name.
     *
     * @return The name of the application this window is being used for.
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Accessor method for getting the color pallet.
     *
     * @return The color pallet component.
     */
    public ColorPallet getColorPallet() {
        return colorPallet;
    }

    /**
     * The fill and outline toggle buttons are connected, only one can be
     * toggled on a any time. This method turns the fill toggle button on.
     */
    public void toggleFillColorButton() {
        fillColorSelectionButton.select();
        outlineColorSelectionButton.deselect();
    }

    /**
     * The fill and outline toggle buttons are connected, only one can be
     * toggled on a any time. This method turns the outline toggle button on.
     */
    public void toggleOutlineColorButton() {
        fillColorSelectionButton.deselect();
        outlineColorSelectionButton.select();
    }

    /**
     * Mutator method for setting the app name.
     *
     * @param initAppName The name of the application, which will be put in the
     * window title bar.
     */
    public void setAppName(String initAppName) {
        appName = initAppName;
    }

    /**
     * To enable the new, open, and exit button from start
     */
    private void enableStartupFileControls() {
        // THESE BUTTONS ARE ALWAYS ENABLED
        newPoseButton.setEnabled(true);
        openPoseButton.setEnabled(true);
        exitButton.setEnabled(true);

        // THESE BUTTONS START OFF AS DISABLED
        savePoseButton.setEnabled(false);
        savePoseAsButton.setEnabled(false);
        exportImgButton.setEnabled(false);
    }

    /**
     * To enable the saveAs and export button as we edit a pose
     */
    private void enableSaveAsAndExport() {
        // THESE ARE ENABLED AS SOON AS WE START EDITING
        savePoseAsButton.setEnabled(true);
        exportImgButton.setEnabled(true);
    }

    /**
     * To enable and disable the edit buttons
     *
     * @param isEnabled
     */
    private void setEnabledEditControls(boolean isEnabled) {
        // THE SELECTION BUTTON NEEDS TO BE CHECKED SEPARATELY

        // THESE ARE EASY, JUST DO AS THEY'RE TOLD
        cutButton.setEnabled(isEnabled);
        copyButton.setEnabled(isEnabled);
        moveToBackButton.setEnabled(isEnabled);
        moveToFrontButton.setEnabled(isEnabled);

        // WE ONLY WANT PASTE ENABLED IF THERE IS
        // SOMETHING ON THE CLIPBOARD
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager state = singleton.getStateManager();
        pasteButton.setEnabled(state.isShapeOnClipboard());
    }

    /**
     * To enable and disable color control buttons
     *
     * @param isEnabled
     */
    private void setEnabledColorControls(boolean isEnabled) {
        outlineColorSelectionButton.setEnabled(isEnabled);
        fillColorSelectionButton.setEnabled(isEnabled);
        customColorSelectorButton.setEnabled(isEnabled);
        colorPallet.setEnabled(isEnabled);
        outlineColorSelectionButton.setSelected(isEnabled);
        alphaLabel.setEnabled(isEnabled);
        transparencySlider.setEnabled(isEnabled);
    }
    
    private void setEnabledPoseListControls(boolean isEnabled) {
        poseSequenceToolBar.setEnabled(isEnabled);
        changeDurationButton.setEnabled(isEnabled);
        movePoseUpButton.setEnabled(isEnabled);
        movePoseDownButton.setEnabled(isEnabled);
        poseControlToolBar.setEnabled(isEnabled);
        editPoseButton.setEnabled(isEnabled);
        deletePoseButton.setEnabled(isEnabled);
        duplicatePoseButton.setEnabled(isEnabled);
    }
    
    private void setEnabledAnimationStateControl(boolean isEnabled) {
        animationStateControl.setEnabled(isEnabled);
        newStateButton.setEnabled(isEnabled);
        renameStateButton.setEnabled(isEnabled);
        deleteStateButton.setEnabled(isEnabled);
        duplicateStateButton.setEnabled(isEnabled);
    }

    /**
     * To enable and disable zoom buttons
     *
     * @param isEnabled
     */
    private void setEnabledZoomControls(boolean isEnabled) {
        zoomOutButton.setEnabled(isEnabled);
        zoomInButton.setEnabled(isEnabled);
        zoomLabel.setEnabled(isEnabled);
        dimensionsButton.setEnabled(isEnabled);
    }
    
    private void setEnabledViewerControls (boolean isEnabled) {
        viewerControlToolbar.setEnabled(isEnabled);
        startButton.setEnabled(isEnabled);
        stopButton.setEnabled(isEnabled);
        speedUpButton.setEnabled(isEnabled);
        slowDownButton.setEnabled(isEnabled);
    }

    /**
     * To enable and disable shape buttons
     *
     * @param isEnabled
     */
    private void setEnabledShapeControls(boolean isEnabled) {
        // INIT THEM AS USABLE OR NOT
        lineToggleButton.setEnabled(isEnabled);
        rectToggleButton.setEnabled(isEnabled);
        ellipseToggleButton.setEnabled(isEnabled);
        lineStrokeSelectionComboBox.setEnabled(isEnabled);

        // IF THEY'RE USABLE, MAKE THE TOGGLES UNSELECTED
        if (isEnabled) {
            shapeButtonGroup.clearSelection();
        }
    }

    /**
     * To switch the type of cursor
     *
     * @param cursorToUse
     */
    private void selectCursor(int cursorToUse) {
        // AND NOW SWITCH TO A CROSSHAIRS CURSOR
        Cursor arrowCursor = Cursor.getPredefinedCursor(cursorToUse);
        setCursor(arrowCursor);
    }

    /**
     * Accessor method for getting the canvas that will zoom in and out,
     * rendering the pose accordingly.
     *
     * @return The zoomable canvas, which is on the right.
     */
    public PoseCanvas getZoomableCanvas() {
        return zoomableCanvas;
    }

    /**
     * Accessor method for getting the true canvas that will not zoom in and
     * out, rendering the pose accordingly.
     *
     * @return The true canvas, which is on the right.
     */
    public PoseCanvas getTruePoseCanvas() {
        return trueCanvas;
    }

    /**
     * Help method to help create ImageIcon that can be added into the pose list
     *
     * @param path The path of the pose image
     * @param description some description
     * @return a constructed ImageIcon object
     */
    public ImageIcon createImageIcon(String path,
            String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void refreshStateSelection() {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        SpriteType type = stateManager.getSpriteType();
        try {
            Iterator<AnimationState> states = type.getAnimationStates();
            while (states.hasNext()) {
                animationStateModel.addElement(states.next().name());
            }
        } catch (NullPointerException ne) {
            JOptionPane.showMessageDialog(null, "No animation state now, add one.");
        }
    }
}
