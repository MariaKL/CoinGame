package storage;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class ReadXMLFile {

	 /**
     * Opens the specified file and creates an XML document object from it which
     * can then be parsed/traversed.
     *
     * @param fileName
     *            the file to parse
     * @return the XML document corresponding to the specified XML file
     *
     */
    public Document createDocument(String filename){

        try {
        	File xmlFile = new File(filename);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            return doc;

            } catch (Exception e) {
            	e.printStackTrace();
            }
        return null;
    }

}
