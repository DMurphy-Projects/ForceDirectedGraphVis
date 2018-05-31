package Model;

import java.awt.Color;
import java.util.HashMap;
import java.util.Set;

public class ColorScheme {

    private HashMap<String, ReferenceColor> mColors;

    final public static String NODE_PRIMARY = "NodePrimary",
	    NODE_SECONDARY = "NodeSecondary", LINK_PRIMARY = "LinkPrimary",
	    LINK_SECONDARY = "LinkSecondary", NODE_HOVER = "NodeHover",
	    LINK_HOVER = "LinkHover", TEXT_COLOR = "TextColor",
	    INFO_BACKGROUND = "InfoBack", BACKGROUND = "Background",
	    GRID = "Grid", NODE_HIGHLIGHT = "NodeHighlight";

    public ColorScheme() {
	mColors = new HashMap<String, ReferenceColor>();
    }

    public void addColor(String name, Color c) {
	mColors.put(name, new ReferenceColor(c));
    }

    public ReferenceColor getColor(String name) {
	return mColors.get(name);
    }

    public Set<String> getColorNames() {
	return mColors.keySet();
    }

}
