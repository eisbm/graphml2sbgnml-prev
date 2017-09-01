package org.eisbm.graphml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GraphMLWriter {
    final static Logger logger = LoggerFactory.getLogger(GraphMLWriter.class);

    public static void writeToFile(GMLRoot graphml, File f) throws IOException, SAXException {
        PrintWriter pw = new PrintWriter(f);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        StringWriter writer = new StringWriter();
        try {
            transformer.transform(new DOMSource(graphml.toXmlDoc()), new StreamResult(writer));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        //String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        pw.write(writer.getBuffer().toString());
        pw.close();
    }

    public static String writeToString(GMLRoot graphml) throws IOException, SAXException {
        return null;
    }
}
