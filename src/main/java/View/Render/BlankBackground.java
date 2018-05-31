package View.Render;

import java.awt.Graphics;

import Model.Graph;
import Model.ReferenceColor;
import View.PanZoomHelper;

public class BlankBackground extends BaseRenderDecorator {

    private ReferenceColor mBackcolor;
    
    public BlankBackground(ReferenceColor c, IRenderDecorator wrapped) {
	super(wrapped);
	mBackcolor = c;
    }

    @Override
    public void render(Graphics gx, Graph g, PanZoomHelper helper) {
	super.render(gx, g, helper);
	gx.setColor(mBackcolor.getColor());
	gx.fillRect(0, 0, helper.WindowWidth(), helper.WindowHeight());
	//gx.setColor(Color.black);
	//gx.drawOval(helper.adjustX(0), helper.adjustY(0), 5, 5);
    }
}
