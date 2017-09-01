package org.eisbm.graphmlsbfc;

import org.eisbm.graphml.GMLEdge;
import org.eisbm.graphml.GMLNode;
import org.sbgn.ArcClazz;
import org.sbgn.GlyphClazz;
import org.sbgn.Language;

public interface Guesser {
    public Language guessMapType() throws CannotGuessException;
    public GlyphClazz guessClass(GMLNode node) throws CannotGuessException;
    public ArcClazz guessClass(GMLEdge edge) throws CannotGuessException;
}
