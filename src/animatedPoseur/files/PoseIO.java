package animatedPoseur.files;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import animated.poseur.AnimatedPoseur;
import static animated.poseur.AnimatedPoseurSettings.*;
import animatedPoseur.gui.PoseCanvas;
import animatedPoseur.gui.AnimatedPoseurGUI;
import animatedPoseur.shapes.PoseurEllipse;
import animatedPoseur.shapes.PoseurLine;
import animatedPoseur.shapes.PoseurRectangle;
import animatedPoseur.shapes.PoseurShape;
import animatedPoseur.shapes.PoseurShapeType;
import animatedPoseur.state.PoseurPose;
import animatedPoseur.state.PoseurState;
import animatedPoseur.state.AnimatedPoseurStateManager;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import sprite_renderer.AnimationState;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;
import sprite_renderer.Sprite;
import sprite_renderer.SpriteType;

/**
 * This class performs Pose object saving to an XML formatted .pose file,
 * loading such files, and exporting Pose objects to image files.
 *
 * @author Xiufeng Yang
 *
 * @version 1.0
 */
public class PoseIO {

    /**
     * This method loads the contents of the poseFileName .pose file into the
     * Pose for editing. Note that this file must validate against the
     * poseur_pose.xsd schema.
     *
     * @param poseFileName The Pose to load for editing.
     */
    public void loadPose(String poseFileName) {
        // THIS WILL HELP US LOAD STUFF
        XMLUtilities xmlUtil = new XMLUtilities();

        // WE'RE GOING TO LOAD IT INTO THE POSE
        try {
            // LOAD THE XML FILE
            Document doc = xmlUtil.loadXMLDocument(poseFileName, POSE_SCHEMA);

            // AND THEN EXTRACT ALL THE DATA

            // LET'S START WITH THE POSE DIMENSIONS
            int poseWidth = xmlUtil.getIntData(doc, POSE_WIDTH_NODE);
            int poseHeight = xmlUtil.getIntData(doc, POSE_HEIGHT_NODE);

            // WE'RE USING A TEMP POSE IN CASE SOMETHING GOES WRONG
            PoseurPose tempPose = new PoseurPose(poseWidth, poseHeight);
            LinkedList<PoseurShape> shapesList = tempPose.getShapesList();

            // LET'S GET THE SHAPE LIST
            NodeList shapeNodes = doc.getElementsByTagName(POSEUR_SHAPE_NODE);
            for (int i = 0; i < shapeNodes.getLength(); i++) {
                // GET THE NODE, THEN WE'LL EXTRACT DATA FROM IT
                // TO FILL IN THE shapeToAdd SHAPE
                Node node = shapeNodes.item(i);
                PoseurShape shapeToAdd;

                //Get the shape Type
                Node geometryNode =
                        xmlUtil.getChildNodeWithName(node, GEOMETRY_NODE);
                NamedNodeMap attributes = geometryNode.getAttributes();
                String shapeTypeText = attributes.getNamedItem(
                        SHAPE_TYPE_ATTRIBUTE).getTextContent();
                PoseurShapeType shapeType =
                        PoseurShapeType.valueOf(shapeTypeText);
                //Extract the shape information and construct new shape 
                //accoring to its shape type
                if (shapeType == PoseurShapeType.RECTANGLE) {
                    double x = Double.parseDouble(attributes.
                            getNamedItem(X_ATTRIBUTE).getTextContent());
                    double y = Double.parseDouble(attributes.
                            getNamedItem(Y_ATTRIBUTE).getTextContent());
                    double width = Double.parseDouble(attributes.
                            getNamedItem(WIDTH_ATTRIBUTE).getTextContent());
                    double height = Double.parseDouble(attributes.
                            getNamedItem(HEIGHT_ATTRIBUTE).getTextContent());
                    Rectangle2D.Double geometry =
                            new Rectangle2D.Double(x, y, width, height);
                    shapeToAdd = new PoseurRectangle(geometry);
                } else if (shapeType == PoseurShapeType.LINE) {
                    double x1 = Double.parseDouble(attributes.
                            getNamedItem(X1_ATTRIBUTE).getTextContent());
                    double y1 = Double.parseDouble(attributes.
                            getNamedItem(Y1_ATTRIBUTE).getTextContent());
                    double x2 = Double.parseDouble(attributes.
                            getNamedItem(X2_ATTRIBUTE).getTextContent());
                    double y2 = Double.parseDouble(attributes.
                            getNamedItem(Y2_ATTRIBUTE).getTextContent());
                    Line2D.Double geometry = new Line2D.Double(x1, y1, x2, y2);
                    shapeToAdd = new PoseurLine(geometry);
                } else {
                    double x = Double.parseDouble(attributes.
                            getNamedItem(X_ATTRIBUTE).getTextContent());
                    double y = Double.parseDouble(attributes.
                            getNamedItem(Y_ATTRIBUTE).getTextContent());
                    double width = Double.parseDouble(attributes.
                            getNamedItem(WIDTH_ATTRIBUTE).getTextContent());
                    double height = Double.parseDouble(attributes.
                            getNamedItem(HEIGHT_ATTRIBUTE).getTextContent());
                    Ellipse2D.Double geometry = new Ellipse2D.Double(
                            x, y, width, height);
                    shapeToAdd = new PoseurEllipse(geometry);
                }


                // FIRST GET THE OUTLINE THICKNESS
                Node outlineNode = xmlUtil.getChildNodeWithName(
                        node, OUTLINE_THICKNESS_NODE);
                int outlineThickness = Integer.parseInt(
                        outlineNode.getTextContent());
                BasicStroke outlineStroke = new BasicStroke(outlineThickness);

                // THEN THE OUTLINE COLOR
                Color outlineColor = extractColor(
                        xmlUtil, node, OUTLINE_COLOR_NODE);

                // THEN THE FILL COLOR
                Color fillColor = extractColor(xmlUtil, node, FILL_COLOR_NODE);

                // AND THE TRANSPARENCY
                Node alphaNode = xmlUtil.getChildNodeWithName(node, ALPHA_NODE);
                int alpha = Integer.parseInt(alphaNode.getTextContent());

                // AND FILL IN THE REST OF THE SHAPE DATA
                shapeToAdd.setAlpha(alpha);
                shapeToAdd.setFillColor(fillColor);
                shapeToAdd.setOutlineColor(outlineColor);
                shapeToAdd.setOutlineThickness(outlineStroke);

                // WE'VE LOADED THE SHAPE, NOW GIVE IT TO THE POSE
                shapesList.add(shapeToAdd);
            }

            // EVERYTHING HAS LOADED WITHOUT FAILING, SO LET'S
            // FIRST LOAD THE DATA INTO THE REAL POSE
            AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
            AnimatedPoseurStateManager stateManager = singleton.getStateManager();
            PoseurPose actualPose = stateManager.getPose();
            actualPose.loadPoseData(tempPose);

            // TELL THE USER ABOUT OUR SUCCESS
            AnimatedPoseurGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                    gui,
                    POSE_LOADED_TEXT,
                    POSE_LOADED_TITLE_TEXT,
                    JOptionPane.INFORMATION_MESSAGE);

            // AND ASK THE GUI TO UPDATE
            singleton.getStateManager().setState(
                    PoseurState.SELECT_SHAPE_STATE);
        } catch (InvalidXMLFileFormatException |
                DOMException | HeadlessException ex) {
            // SOMETHING WENT WRONG LOADING THE .pose XML FILE
            AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
            AnimatedPoseurGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                    gui,
                    POSE_LOADING_ERROR_TEXT,
                    POSE_LOADING_ERROR_TITLE_TEXT,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This helper method will extract color information from the first found
     * child node of parentNode with color data and use this to build and return
     * a Color object.
     *
     * @param xmlUtil This helps perform some XML extraction.
     *
     * @param parentNode This node has a color child of some sort that will have
     * the color data.
     *
     * @param colorChildNodeName This is the name of the child node that has the
     * color data.
     *
     * @return A Color with the color data found in the color node.
     */
    private Color extractColor(XMLUtilities xmlUtil,
            Node parentNode, String colorChildNodeName) {
        // GET THE NODES WE'LL NEED 
        Node colorNode = xmlUtil.getChildNodeWithName(
                parentNode, colorChildNodeName);
        Node redNode = xmlUtil.getChildNodeWithName(colorNode, RED_NODE);
        Node greenNode = xmlUtil.getChildNodeWithName(colorNode, GREEN_NODE);
        Node blueNode = xmlUtil.getChildNodeWithName(colorNode, BLUE_NODE);

        // CONVERT THE COLOR DATA TO INTEGERS
        int red = Integer.parseInt(redNode.getTextContent());
        int green = Integer.parseInt(greenNode.getTextContent());
        int blue = Integer.parseInt(blueNode.getTextContent());

        // BUILD AND RETURN THE COLOR OBJECT
        Color extractedColor = new Color(red, green, blue);
        return extractedColor;
    }

    /**
     * This method saves the pose currently being edited to the poseFile. Note
     * that it will be saved as a .pose file, which is an XML-format that will
     * conform to the poseur_pose.xsd schema.
     *
     * @param poseFile The file to write the pose to.
     *
     * @return true if the file is successfully saved, false otherwise. It's
     * possible that another program could lock out ours from writing to it, so
     * we need to let the caller know when this happens.
     */
    public boolean savePose(File poseFile) {
        // GET THE POSE AND ITS DATA THAT WE HAVE TO SAVE
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();
        PoseurPose poseToSave = poseurStateManager.getPose();
        LinkedList<PoseurShape> shapesList = poseToSave.getShapesList();

        try {
            // THESE WILL US BUILD A DOC
            DocumentBuilderFactory docFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // FIRST MAKE THE DOCUMENT
            Document doc = docBuilder.newDocument();

            // THEN THE ROOT ELEMENT
            Element rootElement = doc.createElement(POSEUR_POSE_NODE);
            doc.appendChild(rootElement);

            // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
            Element poseWidthElement = makeElement(doc, rootElement,
                    POSE_WIDTH_NODE, "" + poseToSave.getPoseWidth());
            Element poseHeightElement = makeElement(doc, rootElement,
                    POSE_HEIGHT_NODE, "" + poseToSave.getPoseHeight());
            Element numShapesElement = makeElement(doc, rootElement,
                    NUM_SHAPES_NODE, "" + shapesList.size());

            // NOW LET'S MAKE THE SHAPES LIST AND ADD THAT TO THE ROOT AS WELL
            Element shapesListElement = makeElement(doc, rootElement,
                    SHAPES_LIST_NODE, "");

            // AND LET'S ADD ALL THE SHAPES TO THE SHAPES LIST
            for (PoseurShape shape : shapesList) {
                // MAKE THE SHAPE NODE AND ADD IT TO THE LIST
                Element shapeNodeElement = makeElement(doc, shapesListElement,
                        POSEUR_SHAPE_NODE, "");

                // NOW LET'S FILL IN THE SHAPE'S DATA

                // FIRST THE OUTLINE THICKNESS
                Element outlineThicknessNode =
                        makeElement(doc, shapeNodeElement,
                        OUTLINE_THICKNESS_NODE, ""
                        + (int) (shape.getOutlineThickness().getLineWidth()));

                // THEN THE OUTLINE COLOR
                Element outlineColorNode = makeColorNode(doc, shapeNodeElement,
                        OUTLINE_COLOR_NODE, shape.getOutlineColor());

                // AND THE FILL COLOR
                Element fillColorNode = makeColorNode(doc, shapeNodeElement,
                        FILL_COLOR_NODE, shape.getFillColor());

                // AND THE THE ALPHA VALUE
                Element alphaNode = makeElement(doc, shapeNodeElement,
                        ALPHA_NODE, "" + shape.getAlpha());

                // SHAPES HAVE BEEN GIVEN THE GIFT OF KNOWING HOW TO
                // ADD THEMESELVES AND THEIR DATA TO THE SHAPE ELEMENT
                Element geometryNode = doc.createElement(GEOMETRY_NODE);
                shape.addNodeData(geometryNode);
                shapeNodeElement.appendChild(geometryNode);
            }

            // THE TRANSFORMER KNOWS HOW TO WRITE A DOC TO
            // An XML FORMATTED FILE, SO LET'S MAKE ONE
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(
                    XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(poseFile);

            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result);

            // WE MADE IT THIS FAR WITH NO ERRORS
            AnimatedPoseurGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                    gui,
                    POSE_SAVED_TEXT,
                    POSE_SAVED_TITLE_TEXT,
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (TransformerException | ParserConfigurationException |
                DOMException | HeadlessException ex) {
            // SOMETHING WENT WRONG WRITING THE XML FILE
            AnimatedPoseurGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                    gui,
                    POSE_SAVING_ERROR_TEXT,
                    POSE_SAVING_ERROR_TITLE_TEXT,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * This helper method can be used to build a node to store color data. We'll
     * use this for building a Doc for the purpose of saving it to a file.
     *
     * @param doc The document where we'll put the node.
     *
     * @param parent The node where we'll add the color node as a child.
     *
     * @param elementName The name of the color node to make. Colors may be used
     * for different purposes, so may have different names.
     *
     * @param color The color data we'll use to build the node is in here.
     *
     * @return A Element (i.e. Node) with the color information inside.
     */
    private Element makeColorNode(Document doc, Element parent,
            String elementName, Color color) {
        // MAKE THE COLOR NODE
        Element colorNode = makeElement(doc, parent, elementName, "");

        // AND THE COLOR COMPONENTS
        Element redNode = makeElement(
                doc, colorNode, RED_NODE, "" + color.getRed());
        Element greenNode = makeElement(
                doc, colorNode, GREEN_NODE, "" + color.getGreen());
        Element blueNode = makeElement(
                doc, colorNode, BLUE_NODE, "" + color.getBlue());

        // AND RETURN OUR NEW ELEMENT (NODE)
        return colorNode;
    }

    /**
     * This helper method builds elements (nodes) for us to help with building a
     * Doc which we would then save to a file.
     *
     * @param doc The document we're building.
     *
     * @param parent The node we'll add our new node to.
     *
     * @param elementName The name of the node we're making.
     *
     * @param textContent The data associated with the node we're making.
     *
     * @return A node of name elementName, with textComponent as data, in the
     * doc document, with parent as its parent node.
     */
    private Element makeElement(Document doc, Element parent,
            String elementName, String textContent) {
        Element element = doc.createElement(elementName);
        element.setTextContent(textContent);
        parent.appendChild(element);
        return element;
    }

    /**
     * Exports the current pose to an image file of the same name in the
     * ./data/exported_images directory.
     *
     * @param currentPoseName Name of the pose to export.
     */
    public void exportPose(String currentPoseName, int imgNum) {
        // WE DON'T HAVE TO ASK THE USER, WE'LL JUST EXPORT IT
        // FIRST GET THE STUFF WE'LL NEED
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        AnimatedPoseurStateManager state = singleton.getStateManager();
        AnimatedSpriteFileManager fileManager = singleton.getSpriteFileManager();
        SpriteType currentType = state.getSpriteType();
        String currentTypeName = fileManager.getCurrentSpriteTypeName();
        String currentState = (String) gui.getAnimationStateSelection().getSelectedItem();
        String fileName = currentPoseName;
        PoseCanvas trueCanvas = gui.getTruePoseCanvas();
        PoseurPose pose = state.getPose();

        // THEN MAKE OUR IMAGE THE SAME DIMENSIONS AS THE POSE
        BufferedImage imageToExport = new BufferedImage(pose.getPoseWidth(),
                pose.getPoseHeight(),
                BufferedImage.TYPE_INT_ARGB);

        // AND ASK THE CANVAS TO FILL IN THE IMAGE,
        // SINCE IT ALREADY KNOWS HOW TO DRAW THE POSE
        trueCanvas.paintToImage(imageToExport);

        // AND NOW SAVE THE IMAGE TO A .png FILE
        File imageFile = new File(fileName + PNG_FILE_EXTENSION);
        // LET'S SAVE THE FILE
        try {
            ImageIO.write(imageToExport, PNG_FORMAT_NAME, imageFile);
            PoseList list = currentType.getPoseList(AnimationState.valueOf(currentState));
            if (!state.isEditing()) {
                ImageIcon newIcon = new ImageIcon(fileName + ".png");
                Image newImage = newIcon.getImage();
                HashMap hm = currentType.getImageAddr();
                int size = hm.size();
                currentType.addImage(size + 1, newImage);
                hm.put(size + 1, fileName.substring(fileName.lastIndexOf("/")+1) + ".png");
                int duration = Integer.parseInt(JOptionPane.showInputDialog("Set a duration for this pose"));
                list = currentType.getPoseList(AnimationState.valueOf(currentState));
                list.addPose(size + 1, duration);
            }
            else {
                ImageIcon newIcon = new ImageIcon(fileName + ".png");
                Image newImage = newIcon.getImage().getScaledInstance(currentType.getWidth(), currentType.getHeight(), Image.SCALE_SMOOTH);
                currentType.addImage(imgNum, newImage);
            }
            DefaultListModel model = (DefaultListModel) gui.getPoseList().getModel();
            model.clear();
            int imgID;
            Iterator<Pose> poses = list.getPoseEndIterator();
            while (poses.hasNext()) {
                imgID = poses.next().getImageID();
                Image i = currentType.getImage(imgID);
                ImageIcon ii = new ImageIcon(i.getScaledInstance(160, 160, Image.SCALE_SMOOTH));
                //model.addElement(new ImageIcon(currentType.getImage(imgID).getScaledInstance(160, 160, Image.SCALE_SMOOTH)));
                model.addElement(ii);
            }
            state.getSpriteToRender().clear();
            Sprite newSprite = new Sprite(currentType, AnimationState.valueOf(currentState));
            state.getSpriteToRender().add(newSprite);
            JOptionPane.showMessageDialog(
                    gui,
                    IMAGE_EXPORTED_TEXT + currentPoseName + PNG_FILE_EXTENSION,
                    IMAGE_EXPORTED_TITLE_TEXT,
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(
                    gui,
                    IMAGE_EXPORTING_ERROR_TEXT,
                    IMAGE_EXPORTING_ERROR_TITLE_TEXT,
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ie) {
            JOptionPane.showMessageDialog(null, "Invalid input, needs integer in frames!");
        }
    }
}