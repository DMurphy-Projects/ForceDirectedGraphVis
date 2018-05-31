package Model;

import java.awt.Color;

public class ReferenceColor{

    private Color mColor;
    
    public ReferenceColor(Color c)
    {
	mColor = c;
    }
    public Color getColor()
    {
	return mColor;
    }
    public void setColor(Color c)
    {
	mColor = c;
    }
    @Override
    public String toString() {
	return mColor.toString();
    }
    
}
