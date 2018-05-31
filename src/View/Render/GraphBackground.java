package View.Render;

import java.awt.Color;
import java.awt.Graphics;

import Model.Graph;
import Model.ReferenceColor;
import View.PanZoomHelper;

public class GraphBackground extends BaseRenderDecorator {

    private int mSize = 50;

    private ReferenceColor mGridColor;
    
    public GraphBackground(ReferenceColor c, IRenderDecorator wrapped) {
	super(wrapped);
	mGridColor = c;
    }

    @Override
    public void render(Graphics gx, Graph g, PanZoomHelper helper) {
	super.render(gx, g, helper);

	Color c = mGridColor.getColor();
	c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 10);
	gx.setColor(c);

	int step = (int) (mSize * helper.Zoom());
	if (step < 1)
	    step = 1;
	for (int i = 0; i < helper.WindowWidth(); i += step) {
	    gx.drawLine(i, 0, i, helper.WindowHeight());
	}
	for (int i = 0; i < helper.WindowHeight(); i += step) {
	    gx.drawLine(0, i, helper.WindowWidth(), i);
	}
    }
}
