package org.eisbm.graphmlsbfc;

import org.eisbm.graphml.GMLRoot;
import org.eisbm.graphml.GraphMLReader;
import org.eisbm.graphml.GraphMLWriter;
import org.sbfc.converter.exceptions.ReadModelException;
import org.sbfc.converter.exceptions.WriteModelException;
import org.sbfc.converter.models.GeneralModel;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public class GraphMLModel implements GeneralModel{
    public GMLRoot graphml;

    public GraphMLModel() {
        super();
    }

    public GraphMLModel(GMLRoot graphml) {
        super();
        this.graphml = graphml;
    }

    @Override
    public void setModelFromFile(String s) throws ReadModelException {
        try {
            this.graphml = GraphMLReader.read(new File(s));
        } catch (IOException e) {
            throw new ReadModelException();
        } catch (SAXException e) {
            throw new ReadModelException();
        }
    }

    @Override
    public void setModelFromString(String s) throws ReadModelException {
        try {
            this.graphml = GraphMLReader.read(s);
        } catch (IOException e) {
            throw new ReadModelException();
        } catch (SAXException e) {
            throw new ReadModelException();
        }
    }

    @Override
    public void modelToFile(String s) throws WriteModelException {
        try {
            GraphMLWriter.writeToFile(this.graphml, new File(s));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String modelToString() throws WriteModelException {
        try {
            return GraphMLWriter.writeToString(this.graphml);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String[] getExtensions() {
        return new String[0];
    }

    @Override
    public boolean isCorrectType(File file) {
        return false;
    }

    @Override
    public String getURI() {
        return null;
    }
}
