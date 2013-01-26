package animatedPoseur.files;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import animated.poseur.AnimatedPoseur;
import static animated.poseur.AnimatedPoseurSettings.*;
import animatedPoseur.state.ColorPalletState;

/**
 * This class can be used to load color pallet data from an XML file into a
 * constructed ColorPalletState. Note that the XML file must validate against
 * the color_pallet_settings.xsd schema. This class demonstrates how application
 * settings can be loaded dynamically from a file.
 *
 * @author Xiufeng Yang
 *
 * @version 1.0
 */
public class ColorPalletLoader {

    /**
     * This method will extract the data found in the provided XML file argument
     * and use it to load the color pallet argument.
     *
     * @param colorPalletXMLFile Path and file name to an XML file containing
     * information about a custom color pallet. Note this XML file must validate
     * against the aforementioned schema.
     *
     * @param colorPalletState The state manager for the color pallet, we'll
     * load all the data found in the XML file inside here.
     */
    public void initColorPallet(String colorPalletXMLFile,
            ColorPalletState colorPalletState) {
        //The array that stores our colors
        Color[] colorPallet = new Color[20];
        try {
            //The XML operator
            XMLUtilities xmlUtil = new XMLUtilities();
            //Load the XML file
            Document colorDoc = xmlUtil.loadXMLDocument(
                    COLOR_PALLET_SETTINGS_XML, COLOR_PALLET_SETTINGS_SCHEMA);
            //Get all the color nodes
            NodeList colorList = colorDoc.getElementsByTagName("pallet_color");
            //Get all four attributes, and assemble them into a Color,
            //of every node one by one
            for (int i = 0; i < colorList.getLength(); i++) {
                Node node = colorList.item(i);
                NodeList list = node.getChildNodes();
                Color currentColor = new Color(Integer.parseInt(
                        list.item(0).getTextContent()), Integer.parseInt(
                        list.item(1).getTextContent()), Integer.parseInt(
                        list.item(2).getTextContent()));
                colorPallet[i] = currentColor;
            }
        } catch (InvalidXMLFileFormatException ixffe) {
            JOptionPane.showMessageDialog(AnimatedPoseur.getAnimatedPoseur().getGUI(),
                    ixffe.getMessage(),
                    LOADING_XML_ERROR_TEXT,
                    JOptionPane.ERROR_MESSAGE);

            // LOG THE ERROR
            Logger.getLogger(AnimatedPoseur.class.getName()).log(
                    Level.SEVERE, null, ixffe);

            // KILL THE APP
            System.exit(0);
        }

        /*
         for (int i = 0; i < 20; i++)
         {
         colorPallet[i] = Color.GRAY;
         }
         * */
        //Pass the colors to the gui
        colorPalletState.loadColorPalletState(colorPallet, 2, 12, Color.GRAY);
    }
}