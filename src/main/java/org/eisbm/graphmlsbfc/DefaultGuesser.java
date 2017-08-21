package org.eisbm.graphmlsbfc;

import org.eisbm.graphml.GMLEdge;
import org.eisbm.graphml.GMLNode;
import org.eisbm.graphml.GMLRoot;
import org.sbgn.ArcClazz;
import org.sbgn.GlyphClazz;
import org.sbgn.Language;

public class DefaultGuesser implements Guesser {

    GMLRoot graphml;

    public DefaultGuesser(GMLRoot root) {
        this.graphml = root;
    }

    @Override
    public Language guessMapType() throws CannotGuessException {
        return Language.AF;
    }

    public GlyphClazz guessClass(GMLNode node) throws CannotGuessException {
        return GlyphClazz.BIOLOGICAL_ACTIVITY;
    }

    public ArcClazz guessClass(GMLEdge edge) throws CannotGuessException {
        throw new CannotGuessException();
    }
}
