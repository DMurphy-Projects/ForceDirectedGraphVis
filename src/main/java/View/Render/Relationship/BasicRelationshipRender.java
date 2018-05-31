package View.Render.Relationship;

import java.awt.Graphics;
import java.awt.Point;

import Model.ReferenceColor;
import View.PanZoomHelper;
import View.Render.IRenderDecorator;

public class BasicRelationshipRender extends BaseRelationshipDecorator {

    private ReferenceColor mColor;
    
    public BasicRelationshipRender(ReferenceColor c, IRenderDecorator wrapped) {
	super(wrapped);
	mColor = c;
    }

    @Override
    public void renderRelationship(Graphics gx, Point[] rel,
	    PanZoomHelper helper) {
	gx.setColor(mColor.getColor());
	gx.drawLine(rel[0].x, rel[0].y, rel[1].x, rel[1].y);
    }

}
