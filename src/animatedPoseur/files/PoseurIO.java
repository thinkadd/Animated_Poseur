/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animatedPoseur.files;

import animated.poseur.AnimatedPoseur;
import static animated.poseur.AnimatedPoseurSettings.*;
import animatedPoseur.gui.AnimatedPoseurGUI;
import animatedPoseur.state.AnimatedPoseurStateManager;
import animatedPoseur.viewer.WhitespaceFreeXMLDoc;
import animatedPoseur.viewer.WhitespaceFreeXMLNode;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import sprite_renderer.AnimationState;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;
import sprite_renderer.SpriteType;

/**
 *
 * @author anderson
 */
public class PoseurIO {

    private Element makeElement(Document doc, Element parent,
            String elementName, String textContent) {
        Element element = doc.createElement(elementName);
        element.setTextContent(textContent);
        parent.appendChild(element);
        return element;
    }

    public WhitespaceFreeXMLDoc loadXMLDocument(String xmlFile,
            String schemaFile)
            throws animatedPoseur.viewer.InvalidXMLFileFormatException {
        // FIRST LET'S VALIDATE IT
        boolean validDoc = validateXMLDoc(xmlFile, schemaFile);
        if (!validDoc) {
            // FAIL
            throw new animatedPoseur.viewer.InvalidXMLFileFormatException(xmlFile, schemaFile);
        }

        // THIS IS JAVA API STUFF
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // FIRST RETRIEVE AND LOAD THE FILE INTO A TREE
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmlDoc = db.parse(xmlFile);

            // THEN PUT IT INTO A FORMAT WE LIKE
            WhitespaceFreeXMLDoc cleanDoc = new WhitespaceFreeXMLDoc();
            cleanDoc.loadDoc(xmlDoc);
            return cleanDoc;
        } // THESE ARE XML-RELATED ERRORS THAT COULD HAPPEN DURING
        // LOADING AND PARSING IF THE XML FILE IS NOT WELL FORMED
        // OR IS NOW WHERE AND WHAT WE SAY IT IS
        catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            return null;
        } catch (SAXException se) {
            se.printStackTrace();
            return null;
        } catch (IOException io) {
            io.printStackTrace();
            return null;
        }
    }

    public boolean validateXMLDoc(String xmlDocNameAndPath,
            String xmlSchemaNameAndPath) {
        try {
            // 1. Lookup a factory for the W3C XML Schema language
            SchemaFactory factory =
                    SchemaFactory.newInstance(SCHEMA_STANDARD_SPEC_URL);

            // 2. Compile the schema. 
            // Here the schema is loaded from a java.io.File, but you could use 
            // a java.net.URL or a javax.xml.transform.Source instead.
            File schemaLocation = new File(xmlSchemaNameAndPath);
            Schema schema = factory.newSchema(schemaLocation);

            // 3. Get a validator from the schema.
            Validator validator = schema.newValidator();

            // 4. Parse the document you want to check.
            Source source = new StreamSource(xmlDocNameAndPath);

            // 5. Check the document
            validator.validate(source);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean saveSpriteTypeAs(File spriteFile, String spriteName) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();
        SpriteType spriteTypeToSave = poseurStateManager.getSpriteType();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("sprite_type");
            doc.appendChild(rootElement);

            Element widthElement = makeElement(doc, rootElement, "width", "" + spriteTypeToSave.getWidth());
            Element heightElement = makeElement(doc, rootElement, "height", "" + spriteTypeToSave.getHeight());
            Element imageListElement = makeElement(doc, rootElement, "images_list", "");
            Iterator<AnimationState> states = spriteTypeToSave.getAnimationStates();
            HashMap<Integer, Image> images = spriteTypeToSave.getSpriteImages();
            HashMap<Integer, String> fileNames = spriteTypeToSave.getImageAddr();
            Iterator<Integer> ids = images.keySet().iterator();
            Integer temp = new Integer(0);
            String newFileName = "";
            while (ids.hasNext()) {
                temp = ids.next();
                Element imageNode = doc.createElement("image_file");
                imageNode.setAttribute("id", "" + temp.intValue());
                newFileName = fileNames.get(temp).replace(poseurStateManager.getCurrentTypeName(), spriteName);
                imageNode.setAttribute("file_name", newFileName.substring(fileNames.get(temp).lastIndexOf("/")+1));
                imageListElement.appendChild(imageNode);
            }
            Element animationListElement = makeElement(doc, rootElement, "animations_list", "");
            HashMap<AnimationState, PoseList> listMap = spriteTypeToSave.getAnimations();
            while (states.hasNext()) {
                AnimationState state = states.next();
                Element animationStateElement = makeElement(doc, animationListElement, "animation_state", "");
                Element stateElement = makeElement(doc, animationStateElement, "state", state.name());
                Element sequenceElement = makeElement(doc, animationStateElement, "animation_sequence", "");
                Iterator<Pose> poseList = listMap.get(state).getPoseEndIterator();
                while (poseList.hasNext()) {
                    Pose tempPose = poseList.next();
                    Element poseNode = doc.createElement("pose");  
                    poseNode.setAttribute("image_id", "" + tempPose.getImageID());
                    poseNode.setAttribute("duration", "" + tempPose.getDurationInFrames());                    
                    sequenceElement.appendChild(poseNode);
                }
            }
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(
                    XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(spriteFile);

            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result);

            // WE MADE IT THIS FAR WITH NO ERRORS
            AnimatedPoseurGUI gui = singleton.getGUI();
            
            JOptionPane.showMessageDialog(
                    gui,
                    "SpriteType .xml file has been saved!",
                    "SpriteType File Saved!",
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
    
    public boolean saveSpriteType(File spriteFile, SpriteType type) {
        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        //AnimatedPoseurStateManager poseurStateManager = singleton.getStateManager();
        SpriteType spriteTypeToSave = type;

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("sprite_type");
            doc.appendChild(rootElement);

            Element widthElement = makeElement(doc, rootElement, "width", "" + spriteTypeToSave.getWidth());
            Element heightElement = makeElement(doc, rootElement, "height", "" + spriteTypeToSave.getHeight());
            Element imageListElement = makeElement(doc, rootElement, "images_list", "");
            Iterator<AnimationState> states = spriteTypeToSave.getAnimationStates();
            HashMap<Integer, Image> images = spriteTypeToSave.getSpriteImages();
            HashMap<Integer, String> fileNames = spriteTypeToSave.getImageAddr();
            Iterator<Integer> ids = images.keySet().iterator();
            Integer temp = new Integer(0);
            while (ids.hasNext()) {
                temp = ids.next();
                Element imageNode = doc.createElement("image_file");
                imageNode.setAttribute("id", "" + temp.intValue());
                imageNode.setAttribute("file_name", fileNames.get(temp).substring(fileNames.get(temp).lastIndexOf("/")+1));
                imageListElement.appendChild(imageNode);
            }
            Element animationListElement = makeElement(doc, rootElement, "animations_list", "");
            HashMap<AnimationState, PoseList> listMap = spriteTypeToSave.getAnimations();
            while (states.hasNext()) {
                AnimationState state = states.next();
                Element animationStateElement = makeElement(doc, animationListElement, "animation_state", "");
                Element stateElement = makeElement(doc, animationStateElement, "state", state.name());
                Element sequenceElement = makeElement(doc, animationStateElement, "animation_sequence", "");
                Iterator<Pose> poseList = listMap.get(state).getPoseEndIterator();
                while (poseList.hasNext()) {
                    Pose tempPose = poseList.next();
                    Element poseNode = doc.createElement("pose");  
                    poseNode.setAttribute("image_id", "" + tempPose.getImageID());
                    poseNode.setAttribute("duration", "" + tempPose.getDurationInFrames());                    
                    sequenceElement.appendChild(poseNode);
                }
            }
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(
                    XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(spriteFile);

            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result);

            // WE MADE IT THIS FAR WITH NO ERRORS
            AnimatedPoseurGUI gui = singleton.getGUI();
            
            JOptionPane.showMessageDialog(
                    gui,
                    "SpriteType .xml file has been saved!",
                    "SpriteType File Saved!",
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

    public SpriteType loadSpriteType(String spriteTypeName) {

        AnimatedPoseur singleton = AnimatedPoseur.getAnimatedPoseur();
        AnimatedPoseurGUI view = singleton.getGUI();
        // BUILD THE PATH WHERE ITS XML FILE AND IMAGES SHOUDL BE
        String spriteTypeXMLFile = SPRITETYPE_PATH + spriteTypeName + "/" + spriteTypeName + ".xml";
        String spriteTypeXSDFile = SPRITETYPE_PATH + SPRITE_TYPE_NODE_NAME + ".xsd";

        // FIRST RETRIEVE AND LOAD THE FILE INTO A TREE
        WhitespaceFreeXMLDoc cleanDoc;

        try {
            cleanDoc = loadXMLDocument(spriteTypeXMLFile, spriteTypeXSDFile);
        } catch (animatedPoseur.viewer.InvalidXMLFileFormatException ixffe) {
            // COULD NOT LOAD THE SPRITE TYPE BECAUSE OF A FAULTY
            // XML DOC, SO WE'LL JUST SKIP IT
            return null;
        }

        // GET THE ROOT NODE
        WhitespaceFreeXMLNode spriteTypeNode = cleanDoc.getRoot();

        // GET THE WIDTH
        WhitespaceFreeXMLNode widthNode = spriteTypeNode.getChildOfType(WIDTH_NODE_NAME);
        String widthAsText = widthNode.getData();
        int width = Integer.parseInt(widthAsText);

        // GET THE HEIGHT
        WhitespaceFreeXMLNode heightNode = spriteTypeNode.getChildOfType(HEIGHT_NODE_NAME);
        String heightAsText = heightNode.getData();
        int height = Integer.parseInt(heightAsText);

        // NOW LET'S USE THE LOADED DATA TO BUILD OUR SPRITE TPYE
        SpriteType spriteTypeToLoad = new SpriteType(width, height);

        // NOW LET'S GET THE IMAGES
        // FIRST LET'S GET THE IMAGES LIST
        WhitespaceFreeXMLNode imageListNode = spriteTypeNode.getChildOfType(IMAGES_LIST_NODE_NAME);

        // NEXT THE INDIVIDUAL IMAGES
        ArrayList<WhitespaceFreeXMLNode> imageFileNodes = imageListNode.getChildrenOfType(IMAGE_FILE_NODE_NAME);
        MediaTracker tracker = new MediaTracker(view);
        Toolkit tk = Toolkit.getDefaultToolkit();
        for (WhitespaceFreeXMLNode imageFileNode : imageFileNodes) {
            String idAsText = imageFileNode.getAttributeValue(ID_ATTRIBUTE_NAME);
            int id = Integer.parseInt(idAsText);
            String fileName = imageFileNode.getAttributeValue(FILE_NAME_ATTRIBUTE_NAME);
            String imageFileNameAndPath = SPRITETYPE_PATH
                    + spriteTypeName
                    + "/"
                    + fileName;
            Image loadedImage = tk.getImage(imageFileNameAndPath);
            tracker.addImage(loadedImage, id);
            spriteTypeToLoad.addImage(id, loadedImage);
            spriteTypeToLoad.getImageAddr().put(id, fileName);
        }

        // MAKER SURE ALL THE IMAGES ARE FULLY LOADED BEFORE GOING ON
        try {
            tracker.waitForAll();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        // AND NOW LOAD ALL THE ANIMATIONS
        WhitespaceFreeXMLNode animationListNode = spriteTypeNode.getChildOfType(ANIMATIONS_LIST_NODE_NAME);
        ArrayList<WhitespaceFreeXMLNode> animationStateNodes = animationListNode.getChildrenOfType(ANIMATION_STATE_NODE_NAME);
        for (WhitespaceFreeXMLNode animationState : animationStateNodes) {
            // FIRST GET THE STATE NAME
            WhitespaceFreeXMLNode stateNode = animationState.getChildOfType(STATE_NODE_NAME);
            String state = stateNode.getData();

            // AND NOW ALL THE POSES
            AnimationState animState = AnimationState.valueOf(state);
            PoseList poseList = spriteTypeToLoad.addPoseList(animState);
            WhitespaceFreeXMLNode animationSequenceNode = animationState.getChildOfType(ANIMATION_SEQUENCE_NODE_NAME);
            ArrayList<WhitespaceFreeXMLNode> poseNodes = animationSequenceNode.getChildrenOfType(POSE_NODE_NAME);
            for (WhitespaceFreeXMLNode poseNode : poseNodes) {
                String imageIDText = poseNode.getAttributeValue(IMAGE_ID_ATTRIBUTE_NAME);
                int imageID = Integer.parseInt(imageIDText);
                String durationText = poseNode.getAttributeValue(DURATION_ATTRIBUTE_NAME);
                int duration = Integer.parseInt(durationText);
                poseList.addPose(imageID, duration);
            }
        }
        // AND SINCE EVERYTHING LOADED PROPERLY LET'S NOW
        // SAVE THE SPRITE TYPE WE JUST CREATED
        AnimatedPoseurStateManager stateManager = singleton.getStateManager();
        stateManager.setSpriteType(spriteTypeToLoad);
        return spriteTypeToLoad;
    }
}
