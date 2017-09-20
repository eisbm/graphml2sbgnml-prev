package org.eisbm.graphmlsbfc;

import org.eisbm.graphml.*;
import org.eisbm.graphmlsbfc.configuration.CannotFindPropertyException;
import org.eisbm.graphmlsbfc.configuration.Configuration;
import org.eisbm.graphmlsbfc.configuration.ConfigurationValue;
import org.sbgn.ArcClazz;
import org.sbgn.GlyphClazz;
import org.sbgn.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class InfoLookup {
    Logger logger = LoggerFactory.getLogger(InfoLookup.class);
    Configuration conf;
    Guesser guesser;

    public InfoLookup(Configuration conf, Guesser guesser) {
        this.conf = conf;
        this.guesser = guesser;
    }

    public boolean hasConfiguration() {
        return this.conf != null;
    }

    public boolean canGuess() {
        return this.guesser != null;
    }

    public Language getMapLanguage(GMLRoot graphml) {
        // first try to use configuration to infer value
        try {
            if(!this.hasConfiguration()) {
                throw new CannotFindPropertyException("No configuration provided");
            }
            ConfigurationValue mapLang = this.getConfiguration().getMapLanguage();
            // trivial case
            switch(mapLang.getType()){
                case PLAIN_VALUE:
                    return (Language) mapLang.getValue();
                case MAP_TO_ATTR:
                    String attrName = mapLang.getMappedLocation();
                    GMLGraph firstGraph = graphml.graphList.get(0);
                    return getLanguageFromData(attrName, graphml.getDataNameMap(), firstGraph.getDataNameMap());
                case MAP_TO_ID:
                    String attrId = mapLang.getMappedLocation();
                    firstGraph = graphml.graphList.get(0);
                    return getLanguageFromData(attrId, graphml.getDataMap(), firstGraph.getDataMap());
            }

        }
        // configuration failed to provide correct map language, try to guess it
        catch (CannotFindPropertyException | CannotMapPropertyException e) {
            try {
                if(!this.canGuess()) {
                    throw new CannotGuessException("Guesser not set, impossible to guess map language.");
                }
                return this.getGuesser().guessMapType();
            } catch (CannotGuessException e1) { // everything failed, impossible to know map language
                throw new UnsupportedOperationException ("Map language was not provided and could not be inferred.");
            }
        }
        throw new UnsupportedOperationException ();
    }

    public static Language getLanguageFromData(String mappedLocation, Map<String, GMLProperty> rootMap,
                                               Map<String, GMLProperty> graphMap) throws CannotMapPropertyException {
        // first try to get value from root org.eisbm.graphml element
        if(rootMap.containsKey(mappedLocation)) {
            GMLSimpleProperty prop = (GMLSimpleProperty) rootMap.get(mappedLocation);
            String dataValue = prop.getValue();
            return getLanguageFromString(dataValue);
        }
        // try to get value from the first graph element
        else if(graphMap.containsKey(mappedLocation)) {
            GMLSimpleProperty prop = (GMLSimpleProperty) graphMap.get(mappedLocation);
            String dataValue = prop.getValue();
            return getLanguageFromString(dataValue);
        }
        else {
            throw new CannotMapPropertyException("map.language property could not be mapped to '"+ mappedLocation+
                    "' attribute of <org.eisbm.graphml> or first <graph> data. Check your input file " +
                    "and configuration.");
        }
    }

    public static Language getLanguageFromString(String lang) throws CannotMapPropertyException {
        switch (lang) {
            case "AF":
            case "activity flow":
                return Language.AF;
            case "PD":
            case "process description":
                return Language.PD;
            case "ER":
            case "entity relationship":
                throw new CannotMapPropertyException("ER language isn't supported by this library");
            default:
                throw new CannotMapPropertyException("Map language value cannot be recognized." +
                        "Please check your input file.");
        }
    }

    public String getMapVersion(GMLRoot graphml) {
        return null;
    }

    public GlyphClazz getGlyphClass(GMLNode node) {
        // first try to use configuration to infer value
        try {
            // bypass everything if node is from SBGN palette
            // here we don't need configuration or guess, we know for sure
            if(node.isSBGNPalette()) {
                return getGlyphClassFromString(node.getShapeType());
            }

            if(!this.hasConfiguration()) {
                throw new CannotFindPropertyException("No configuration provided");
            }
            ConfigurationValue glyphClass = this.getConfiguration().getGlyphClass();
            // trivial case
            switch(glyphClass.getType()){
                case PLAIN_VALUE:
                    return (GlyphClazz) glyphClass.getValue();
                case MAP_TO_ATTR:
                    String attrName = glyphClass.getMappedLocation();
                    return getGlyphClassFromString(getGlyphClassFromData(attrName, node.getDataNameMap()));
                case MAP_TO_ID:
                    String attrId = glyphClass.getMappedLocation();
                    return getGlyphClassFromString(getGlyphClassFromData(attrId, node.getDataMap()));
                case CUSTOM_MAP_TO_ATTR:
                    attrName =  glyphClass.getMappedLocation();
                    String attrValue = getGlyphClassFromData(attrName, node.getDataNameMap());
                    HashMap<String, String> customMap = (HashMap<String, String>) glyphClass.getValue();
                    if(customMap.containsKey(attrValue)) {
                        return getGlyphClassFromString(customMap.get(attrValue));
                    }
                    else {
                        throw new CannotMapPropertyException("Glyph class found in file: "+ attrValue +" doesn't have " +
                                "any mapped value defined in the configuration");
                    }
                case CUSTOM_MAP_TO_ID:
                    attrId =  glyphClass.getMappedLocation();
                    attrValue = getGlyphClassFromData(attrId, node.getDataMap());
                    customMap = (HashMap<String, String>) glyphClass.getValue();
                    if(customMap.containsKey(attrValue)) {
                        return getGlyphClassFromString(customMap.get(attrValue));
                    }
                    else {
                        throw new CannotMapPropertyException("Glyph class found in file: "+ attrValue +" doesn't have " +
                                "any mapped value defined in the configuration");
                    }
                case CUSTOM_MAP_TO_SHAPE:
                    String shape = node.getShapeType();
                    customMap = (HashMap<String, String>) glyphClass.getValue();
                    if(customMap.containsKey(shape)) {
                        return getGlyphClassFromString(customMap.get(shape));
                    }
                    else {
                        throw new CannotMapPropertyException("Glyph shape found in file: "+ shape +" doesn't have " +
                                "any mapped value defined in the configuration");
                    }
            }

        }
        // configuration failed to provide correct map language, try to guess it
        catch (CannotFindPropertyException | CannotMapPropertyException e) {
            try {
                if(!this.canGuess()) {
                    throw new CannotGuessException("Guesser not set, impossible to guess map language.");
                }
                return this.getGuesser().guessClass(node);
            } catch (CannotGuessException e1) { // everything failed, impossible to know map language
                throw new UnsupportedOperationException ("Glyph class was not provided and could not be inferred.");
            }
        }
        throw new UnsupportedOperationException ();
    }

    public static String getGlyphClassFromData(String mappedLocation, Map<String, GMLProperty> map)
            throws CannotMapPropertyException {
        if(map.containsKey(mappedLocation)) {
            GMLSimpleProperty prop = (GMLSimpleProperty) map.get(mappedLocation);
            return prop.getValue();
        }
        else {
            throw new CannotMapPropertyException("glyph class property could not be mapped to '"+ mappedLocation+
                    "' attribute of <node> data. Check your input file and configuration.");
        }
    }

    public static GlyphClazz getGlyphClassFromString(String class_) throws CannotMapPropertyException {
        HashSet<String> hash = new HashSet<>();
        for(GlyphClazz g: GlyphClazz.values()) {
            hash.add(g.toString());
        }

        if(hash.contains(class_)){
            return GlyphClazz.fromClazz(class_);
        }
        else {
            throw new CannotMapPropertyException("Glyph class value cannot be recognized." +
                        "Please check your input file.");
        }
    }

    public ArcClazz getArcClass(GMLEdge edge) {
        logger.debug("getArcClass from edge"+edge);
        // first try to use configuration to infer value
        try {
            if(!this.hasConfiguration()) {
                logger.warn("No configuration provided");
                throw new CannotFindPropertyException("No configuration provided");
            }
            ConfigurationValue arcClass = this.getConfiguration().getArcClass();
            // trivial case
            switch(arcClass.getType()){
                case PLAIN_VALUE:
                    return (ArcClazz) arcClass.getValue();
                case MAP_TO_ATTR:
                    String attrName = arcClass.getMappedLocation();
                    return getArcClassFromString(getArcClassFromData(attrName, edge.getDataNameMap()));
                case MAP_TO_ID:
                    String attrId = arcClass.getMappedLocation();
                    return getArcClassFromString(getArcClassFromData(attrId, edge.getDataMap()));
                case CUSTOM_MAP_TO_ATTR:
                    attrName =  arcClass.getMappedLocation();
                    String attrValue = getArcClassFromData(attrName, edge.getDataNameMap());
                    HashMap<String, String> customMap = (HashMap<String, String>) arcClass.getValue();
                    if(customMap.containsKey(attrValue)) {
                        return getArcClassFromString(customMap.get(attrValue));
                    }
                    else {
                        throw new CannotMapPropertyException("Arc class found in file: "+ attrValue +" doesn't have " +
                                "any mapped value defined in the configuration");
                    }
                case CUSTOM_MAP_TO_ID:
                    attrId =  arcClass.getMappedLocation();
                    attrValue = getGlyphClassFromData(attrId, edge.getDataMap());
                    customMap = (HashMap<String, String>) arcClass.getValue();
                    if(customMap.containsKey(attrValue)) {
                        return getArcClassFromString(customMap.get(attrValue));
                    }
                    else {
                        throw new CannotMapPropertyException("Arc class found in file: "+ attrValue +" doesn't have " +
                                "any mapped value defined in the configuration");
                    }
                case CUSTOM_MAP_TO_SHAPE:
                    logger.debug("custom map to shape found in configuration");
                    if(edge.hasSingleArrow()) {
                        String arrowShape = edge.getArrowShape();
                        customMap = (HashMap<String, String>) arcClass.getValue();
                        System.out.println("custom map "+arrowShape+" "+customMap);
                        if(customMap.containsKey(arrowShape)) {
                            return getArcClassFromString(customMap.get(arrowShape));
                        }
                        else {
                            logger.error("Could not map arrow shape: "+arrowShape+" for edge: "+edge);
                            throw new CannotMapPropertyException("Edge arrow found in file: "+ arrowShape +" doesn't " +
                                    "have any mapped value defined in the configuration");
                        }
                    }
                    else {
                        logger.error("Edge has double arrow");
                        throw new CannotMapPropertyException("Edge has 2 arrows, mapping arcs only works with edges " +
                                "with single arrows. Please check your input file.");
                    }


            }

        }
        // configuration failed to provide correct map language, try to guess it
        catch (CannotFindPropertyException | CannotMapPropertyException e) {
            logger.debug("Try to guess edge class");
            try {
                if(!this.canGuess()) {
                    logger.warn("No guesser provided");
                    throw new CannotGuessException("Guesser not set, impossible to guess map language.");
                }
                return this.getGuesser().guessClass(edge);
            } catch (CannotGuessException e1) { // everything failed, impossible to know map language
                logger.error("Impossible to get or guess arc class from edge: "+edge);
                throw new UnsupportedOperationException ("Arc class was not provided and could not be inferred.");
            }
        }
        throw new UnsupportedOperationException ();
    }

    public static String getArcClassFromData(String mappedLocation, Map<String, GMLProperty> map)
            throws CannotMapPropertyException {
        if(map.containsKey(mappedLocation)) {
            GMLSimpleProperty prop = (GMLSimpleProperty) map.get(mappedLocation);
            return prop.getValue();
        }
        else {
            throw new CannotMapPropertyException("arc class property could not be mapped to '"+ mappedLocation+
                    "' attribute of <edge> data. Check your input file and configuration.");
        }
    }

    public static ArcClazz getArcClassFromString(String class_) throws CannotMapPropertyException {
        HashSet<String> hash = new HashSet<>();
        for(ArcClazz g: ArcClazz.values()) {
            hash.add(g.toString());
        }

        if(hash.contains(class_)){
            return ArcClazz.fromClazz(class_);
        }
        else {
            throw new CannotMapPropertyException("Arc class value cannot be recognized." +
                    "Please check your input file.");
        }
    }



    public Configuration getConfiguration() {
        return conf;
    }

    public void setConfiguration(Configuration conf) {
        this.conf = conf;
    }

    public Guesser getGuesser() {
        return guesser;
    }

    public void setGuesser(Guesser guesser) {
        this.guesser = guesser;
    }
}
