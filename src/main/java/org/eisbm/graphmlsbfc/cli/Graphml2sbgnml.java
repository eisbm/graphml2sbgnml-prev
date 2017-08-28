package org.eisbm.graphmlsbfc.cli;

import org.eisbm.graphmlsbfc.GraphML2SBGNML;
import org.eisbm.graphmlsbfc.GraphMLModel;

import org.sbfc.converter.exceptions.ConversionException;
import org.sbfc.converter.exceptions.ReadModelException;
import org.sbfc.converter.exceptions.WriteModelException;
import org.sbfc.converter.models.SBGNModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;

public class Graphml2sbgnml {

    @Parameter(names = { "-i", "--input"}, required = true)
    String inputFileName;

    @Parameter(names = { "-o", "--output" }, required = true)
    String outputFileName;

    @Parameter(names = { "-c", "--conf", "--configuration" }, required = true)
    String configFileName;

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Graphml2sbgnml.class);

        Graphml2sbgnml app = new Graphml2sbgnml();
        JCommander.newBuilder()
                .addObject(app)
                .build()
                .parse(args);


        convert(app.inputFileName, app.outputFileName, app.configFileName);
    }

    public static void convert(String inputFileName, String outputFileName, String configFileName) {
        GraphMLModel gModel = new GraphMLModel();
        try {
            gModel.setModelFromFile(inputFileName);
        } catch (ReadModelException e) {
            e.printStackTrace();
        }

        GraphML2SBGNML converter = new GraphML2SBGNML(configFileName);
        SBGNModel sbgnModel = new SBGNModel();
        try {
            sbgnModel = (SBGNModel) converter.convert(gModel);
        } catch (ConversionException | ReadModelException e) {
            e.printStackTrace();
        }

        try {
            sbgnModel.modelToFile(outputFileName);
        } catch (WriteModelException e) {
            e.printStackTrace();
        }
    }
}
