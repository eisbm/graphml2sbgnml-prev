import org.eisbm.graphml.GMLRoot;
import org.eisbm.graphmlsbfc.SBGNML2GraphML;
import org.eisbm.graphmlsbfc.configuration.CannotFindPropertyException;
import org.eisbm.graphmlsbfc.configuration.Configuration;
import org.eisbm.graphmlsbfc.GraphML2SBGNML;
import org.eisbm.graphmlsbfc.GraphMLModel;
import org.sbfc.converter.exceptions.ConversionException;
import org.sbfc.converter.exceptions.ReadModelException;
import org.sbfc.converter.exceptions.WriteModelException;
import org.sbfc.converter.models.SBGNModel;
import org.sbgn.SbgnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.*;

public class Test {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Test.class);

        logger.info("test");
        /*Arc arc = new Arc();
        arc.setSource("source1");
        Arc.Start start = new Arc.Start();
        start.setX((float) 2.14);
        arc.setStart(start);

        TestVisitor tvisit = new TestVisitor();
        VisitableArc varc = new VisitableArc(arc);
        //varc.accept(tvisit);

        try {
            Sbgn sbgn = SbgnUtil.readFromFile(new File("../../Downloads/style_test (1).sbgnml"));
            VisitableSbgn vsbgn = new VisitableSbgn(sbgn);
            vsbgn.accept(tvisit);

        } catch (JAXBException e) {
            e.printStackTrace();
        }*/

        //String testFile = "src/main/resources/test_simple.graphml";
        //String testFile = "src/main/resources/test.graphml";
        String testFile = "src/main/resources/test2.graphml";
        //String testFile = "src/main/resources/out.graphml";

        GraphMLModel gModel = new GraphMLModel();
        try {
            gModel.setModelFromFile(testFile);
        } catch (ReadModelException e) {
            e.printStackTrace();
        }
        System.out.println(gModel.graphml.getDataNameMap());



        org.eisbm.graphml.TestVisitor gvisitor = new org.eisbm.graphml.TestVisitor();
        gModel.graphml.accept(gvisitor);
        System.out.println(gvisitor.types);
        System.out.println(gvisitor.edges);

        GraphML2SBGNML converter = new GraphML2SBGNML();
        SBGNModel sbgnModel = new SBGNModel();
        try {
            sbgnModel = (SBGNModel) converter.convert(gModel);
        } catch (ConversionException e) {
            e.printStackTrace();
        } catch (ReadModelException e) {
            e.printStackTrace();
        }

        String outFile = "src/main/resources/out.sbgnml";

        try {
            System.out.println("sbgn result written to file");
            sbgnModel.modelToFile(outFile);
        } catch (WriteModelException e) {
            e.printStackTrace();
        }

        //TestHierarchy h = new TestHierarchy();
        //gModel.org.eisbm.graphml.accept(h);

        if(false) {
            try {
                Configuration conf = Configuration.readFromFile("src/main/resources/default.yml");
                System.out.println(conf.getGlyphClass().getMappedLocation() + " " + conf.getGlyphClass().getValue());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CannotFindPropertyException e) {
                e.printStackTrace();
            }
        }

        SBGNML2GraphML toGMLConverter = new SBGNML2GraphML();
        String outFileGML = "src/main/resources/out.graphml";
        try {
            GMLRoot root = toGMLConverter.toGraphml(SbgnUtil.readFromFile(new File(outFile)));
            GraphMLModel gModelOut = new GraphMLModel(root);
            gModelOut.modelToFile(outFileGML);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (WriteModelException e) {
            e.printStackTrace();
        }

        /*try {
            GraphMLWriter.writeToFile(gModel.graphml, new File(outFileGML));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }*/


        logger.info("end test");
    }

}
