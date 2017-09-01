package org.eisbm.graphmlsbfc.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.eisbm.graphml.GMLRoot;
import org.eisbm.graphmlsbfc.GraphMLModel;
import org.eisbm.graphmlsbfc.SBGNML2GraphML;
import org.sbfc.converter.exceptions.WriteModelException;
import org.sbgn.SbgnUtil;
import org.sbgn.bindings.Sbgn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.File;

public class Sbgnml2graphml {
    @Parameter(names = { "-i", "--input"}, required = true)
    String inputFileName;

    @Parameter(names = { "-o", "--output" }, required = true)
    String outputFileName;

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Sbgnml2graphml.class);

        Sbgnml2graphml app = new Sbgnml2graphml();
        JCommander.newBuilder()
                .addObject(app)
                .build()
                .parse(args);

        convert(app.inputFileName, app.outputFileName);
    }

    public static void convert(String inputFileName, String outputFileName) {
        SBGNML2GraphML toGMLConverter = new SBGNML2GraphML();

        Sbgn sbgn = null;
        try {
            sbgn = SbgnUtil.readFromFile(new File(inputFileName));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        try {
            GMLRoot root = toGMLConverter.toGraphml(sbgn);
            GraphMLModel gModelOut = new GraphMLModel(root);
            gModelOut.modelToFile(outputFileName);
        } catch (WriteModelException e) {
            e.printStackTrace();
        }
    }
}
