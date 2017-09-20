package org.eisbm.graphml;

public interface GMLNodeFeature {

    public float getX();

    public void setX(float x);

    public float getY();

    public void setY(float y);

    public float getWidth();

    public void setWidth(float width);

    public float getHeight();

    public void setHeight(float height);

    public String getShapeType();

    public void setShapeType(String shapeType);

    public String getLabel();

    public void setLabel(String label);

    public boolean isSBGNPalette();
}
