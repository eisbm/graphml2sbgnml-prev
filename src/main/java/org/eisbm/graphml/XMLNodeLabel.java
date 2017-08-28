package org.eisbm.graphml;

import org.w3c.dom.Element;

public class XMLNodeLabel {
    Element nodeLabelELement;

    public enum LabelType {
        SIMPLE,
        SMART
    }

    public static class XMLNodeLabelBuilder {
        LabelType type;
        NodeLabelAttributes attributes;

        public XMLNodeLabelBuilder() {

        }
    }

    public static class NodeLabelAttributes {
        String alignment = "center";
        String autoSizePolicy = "content";
        String fontFamily = "Dialog";
        int fontSize = 12;
        String fontStyle = "plain";
        boolean hasBackgroundColor = false;
        boolean hasLineColor = false;
        boolean hasText = false;
        String horizontalTextPosition = "center";
        int iconTextGap = 4;
        String modelName = "custom";
        String textColor = "#000000";
        String verticalTextPosition = "bottom";
        boolean visible = true;

        public String getAlignment() {
            return alignment;
        }

        public void setAlignment(String alignment) {
            this.alignment = alignment;
        }

        public String getAutoSizePolicy() {
            return autoSizePolicy;
        }

        public void setAutoSizePolicy(String autoSizePolicy) {
            this.autoSizePolicy = autoSizePolicy;
        }

        public String getFontFamily() {
            return fontFamily;
        }

        public void setFontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
        }

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }

        public String getFontStyle() {
            return fontStyle;
        }

        public void setFontStyle(String fontStyle) {
            this.fontStyle = fontStyle;
        }

        public boolean hasBackgroundColor() {
            return hasBackgroundColor;
        }

        public void setHasBackgroundColor(boolean hasBackgroundColor) {
            this.hasBackgroundColor = hasBackgroundColor;
        }

        public boolean hasLineColor() {
            return hasLineColor;
        }

        public void setHasLineColor(boolean hasLineColor) {
            this.hasLineColor = hasLineColor;
        }

        public boolean hasText() {
            return hasText;
        }

        public void setHasText(boolean hasText) {
            this.hasText = hasText;
        }

        public String getHorizontalTextPosition() {
            return horizontalTextPosition;
        }

        public void setHorizontalTextPosition(String horizontalTextPosition) {
            this.horizontalTextPosition = horizontalTextPosition;
        }

        public int getIconTextGap() {
            return iconTextGap;
        }

        public void setIconTextGap(int iconTextGap) {
            this.iconTextGap = iconTextGap;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public String getVerticalTextPosition() {
            return verticalTextPosition;
        }

        public void setVerticalTextPosition(String verticalTextPosition) {
            this.verticalTextPosition = verticalTextPosition;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }
    }
}
