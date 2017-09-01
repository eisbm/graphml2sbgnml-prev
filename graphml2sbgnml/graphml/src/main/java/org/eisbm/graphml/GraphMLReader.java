package org.eisbm.graphml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GraphMLReader {
    final static Logger logger = LoggerFactory.getLogger(GraphMLReader.class);


    public static GMLRoot read(InputStream is) throws IOException, SAXException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();
        logger.info("doc "+doc+" "+doc.getDocumentElement().getTagName());
        logger.info(String.valueOf(doc.getElementsByTagName("node").getLength()));
        GMLRoot root = new GMLRoot(doc.getDocumentElement());

        is.close();
        return root;
    }

    public static GMLRoot read(File f) throws IOException, SAXException {
        return GraphMLReader.read(new FileInputStream(f));
    }

    public static GMLRoot read(String s) throws IOException, SAXException {
        return GraphMLReader.read(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
    }

}
