package Model;

import View.Render.IRenderDecorator;

public class ThemePair {

    private IRenderDecorator mDecoration;
    private ColorScheme mColors;

    public ThemePair(IRenderDecorator dec, ColorScheme c) {
	mDecoration = dec;
	mColors = c;
    }
    
    public IRenderDecorator getDecoration()
    {
	return mDecoration;
    }
    public ColorScheme getColors()
    {
	return mColors;
    }
}
