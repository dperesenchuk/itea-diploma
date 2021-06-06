package ua.itea.diploma;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ResourcesContainer {
    private HashMap<String, String> resourcesMap = new HashMap<>();

    public synchronized void addResource(String urlString, String data) {
        resourcesMap.put(urlString, data);
    }

    public void writeData() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.newDocument();
            Element root = dom.createElement("Data");
            for (String key: resourcesMap.keySet()) {
                Element item = dom.createElement("item");

                Element url = dom.createElement("url");
                url.appendChild(dom.createTextNode(key));
                item.appendChild(url);

                Element item_data = dom.createElement("item_data");
                item_data.appendChild(dom.createTextNode(resourcesMap.get(key)));
                item.appendChild(item_data);

                root.appendChild(item);
            }

            dom.appendChild(root);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream("output.xml")));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }

        } catch (ParserConfigurationException pce) {
            System.out.println("Error trying to instantiate DocumentBuilder " + pce);
        }
    }
}
