package org.eisbm.graphmlsbfc.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.sbgn.Language;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Stack;

public class Configuration {
    public HashMap<String, Object> properties;

    public Configuration() {
        this.properties = new HashMap<>();
    }

    public static Configuration readFromFile(String s) throws IOException {
        return Configuration.readFromString(new String(Files.readAllBytes(Paths.get(s)), StandardCharsets.UTF_8));
    }

    public static Configuration readFromString(String s) {
        Configuration conf = new Configuration();

        YAMLFactory factory = new YAMLFactory();
        JsonParser parser = null; // don't be fooled by method name...
        try {
            parser = factory.createParser(s);
            Stack<HashMap<String, Object>> context = new Stack<>();
            context.push(conf.properties);
            String lastFieldName = "";
            while (parser.nextToken() != null) {
                System.out.println(parser.getCurrentToken()+" "+parser.getText());
                switch(parser.getCurrentToken()) {
                    case FIELD_NAME:
                        lastFieldName = parser.getText();
                        break;
                    case VALUE_STRING:
                        context.peek().put(lastFieldName, parser.getText());
                        break;
                    case START_OBJECT:
                        switch(lastFieldName){
                            case "class":
                                HashMap<String, Object> classMap = new HashMap<>();
                                context.peek().put("class", classMap);
                                context.push(classMap);
                                break;
                            case "glyph":
                                HashMap<String, Object> glyphMap = new HashMap<>();
                                context.peek().put("glyph", glyphMap);
                                context.push(glyphMap);
                                break;
                            case "arc":
                                HashMap<String, Object> edgeMap = new HashMap<>();
                                context.peek().put("arc", edgeMap);
                                context.push(edgeMap);
                                break;
                        }
                        break;
                    case END_OBJECT:
                        context.pop();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(conf.properties);
        return conf;
    }

    public ConfigurationValue getMapLanguage() throws CannotFindPropertyException {
        if(this.properties.containsKey("map.language")) {
            String mapLang = (String) this.properties.get("map.language");
            // map to attribute name
            if(mapLang.startsWith("attr.")) {
                String mappedLocation = mapLang.split("\\.")[1];
                return new ConfigurationValue(ValueType.MAP_TO_ATTR, mappedLocation, mappedLocation);
            }
            // map to key's id attribute
            else if(mapLang.startsWith("id.")) {
                String mappedLocation = mapLang.split("\\.")[1];
                return new ConfigurationValue(ValueType.MAP_TO_ID, mappedLocation, mappedLocation);
            }
            // plain value
            else {
                ValueType type = ValueType.PLAIN_VALUE;
                Language value = null;
                switch (mapLang) {
                    case "AF":
                    case "activity flow":
                        value = Language.AF;
                        break;
                    case "PD":
                    case "process description":
                        value = Language.PD;
                        break;
                    case "ER":
                    case "entity relationship":
                        throw new IllegalArgumentException("ER language isn't supported by this library");
                    default:
                        throw new IllegalArgumentException("map.language value cannot be recognized." +
                                "Please check your configuration file.");
                }
                return new ConfigurationValue(type, value, null);
            }
        } else {
            throw new CannotFindPropertyException("map.language isn't defined in configuration");
        }
    }

    public ConfigurationValue getGlyphClass() throws CannotFindPropertyException {
        if(this.properties.containsKey("glyph")) {
            HashMap<String, Object> glyphMap = (HashMap<String, Object>) this.properties.get("glyph");
            if(glyphMap.containsKey("class")){
                Object glyphClassValue = glyphMap.get("class");
                if(glyphClassValue instanceof String) {
                    String classString = (String) glyphClassValue;
                    // map to attribute name
                    if(classString.startsWith("attr.")) {
                        String mappedLocation = classString.split("\\.")[1];
                        return new ConfigurationValue(ValueType.MAP_TO_ATTR, mappedLocation, mappedLocation);
                    }
                    // map to key's id attribute
                    else if(classString.startsWith("id.")) {
                        String mappedLocation = classString.split("\\.")[1];
                        return new ConfigurationValue(ValueType.MAP_TO_ID, mappedLocation, mappedLocation);
                    }
                    // plain value doesn't make sense, return it anyway
                    else {
                        return new ConfigurationValue(ValueType.PLAIN_VALUE, classString, null);
                    }
                }
                // custom map, class is a dictionary
                else {
                    HashMap<String, String> classMap = (HashMap<String, String>) glyphClassValue;
                    if(!classMap.containsKey("__location__")) {
                        throw new CannotFindPropertyException("__location__ is missing from glyph class custom map, " +
                                "please check your configuration file");
                    }

                    HashMap<String, String> finalMap = new HashMap<>(classMap);
                    finalMap.remove("__location__");

                    String mappedLocation = classMap.get("__location__");
                    // map to attribute name
                    if(mappedLocation.startsWith("attr.")) {
                        mappedLocation = mappedLocation.split("\\.")[1];
                        return new ConfigurationValue(ValueType.CUSTOM_MAP_TO_ATTR, finalMap, mappedLocation);
                    }
                    // map to key's id attribute
                    else if(mappedLocation.startsWith("id.")) {
                        mappedLocation = mappedLocation.split("\\.")[1];
                        return new ConfigurationValue(ValueType.CUSTOM_MAP_TO_ID, finalMap, mappedLocation);
                    }
                    // map to shapes
                    else if(mappedLocation.equals("shape")) {
                        return new ConfigurationValue(ValueType.CUSTOM_MAP_TO_SHAPE, finalMap, mappedLocation);
                    }
                    else {
                        throw new IllegalArgumentException("glyph class __location__ property has an illegal value.");
                    }
                }
            }
            else {
                throw new CannotFindPropertyException("glyph class isn't defined in configuration");
            }
        }
        else {
            throw new CannotFindPropertyException("glyph section isn't defined in configuration");
        }
    }

    public ConfigurationValue getArcClass() throws CannotFindPropertyException {
        if(this.properties.containsKey("arc")) {
            HashMap<String, Object> arcMap = (HashMap<String, Object>) this.properties.get("arc");
            if(arcMap.containsKey("class")){
                Object arcClassValue = arcMap.get("class");
                if(arcClassValue instanceof String) {
                    String classString = (String) arcClassValue;
                    // map to attribute name
                    if(classString.startsWith("attr.")) {
                        String mappedLocation = classString.split("\\.")[1];
                        return new ConfigurationValue(ValueType.MAP_TO_ATTR, mappedLocation, mappedLocation);
                    }
                    // map to key's id attribute
                    else if(classString.startsWith("id.")) {
                        String mappedLocation = classString.split("\\.")[1];
                        return new ConfigurationValue(ValueType.MAP_TO_ID, mappedLocation, mappedLocation);
                    }
                    // plain value doesn't make sense, return it anyway
                    else {
                        return new ConfigurationValue(ValueType.PLAIN_VALUE, classString, null);
                    }
                }
                // custom map, class is a dictionary
                else {
                    HashMap<String, String> classMap = (HashMap<String, String>) arcClassValue;
                    if(!classMap.containsKey("__location__")) {
                        throw new CannotFindPropertyException("__location__ is missing from arc class custom map, " +
                                "please check your configuration file");
                    }

                    HashMap<String, String> finalMap = new HashMap<>(classMap);
                    finalMap.remove("__location__");

                    String mappedLocation = classMap.get("__location__");
                    // map to attribute name
                    if(mappedLocation.startsWith("attr.")) {
                        mappedLocation = mappedLocation.split("\\.")[1];
                        return new ConfigurationValue(ValueType.CUSTOM_MAP_TO_ATTR, finalMap, mappedLocation);
                    }
                    // map to key's id attribute
                    else if(mappedLocation.startsWith("id.")) {
                        mappedLocation = mappedLocation.split("\\.")[1];
                        return new ConfigurationValue(ValueType.CUSTOM_MAP_TO_ID, finalMap, mappedLocation);
                    }
                    // map to shapes
                    else if(mappedLocation.equals("shape")) {
                        return new ConfigurationValue(ValueType.CUSTOM_MAP_TO_SHAPE, finalMap, mappedLocation);
                    }
                    else {
                        throw new IllegalArgumentException("arc class __location__ property has an illegal value.");
                    }
                }
            }
            else {
                throw new CannotFindPropertyException("arc class isn't defined in configuration");
            }
        }
        else {
            throw new CannotFindPropertyException("arc section isn't defined in configuration");
        }
    }
}
