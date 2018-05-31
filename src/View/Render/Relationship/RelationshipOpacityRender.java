package View.Render.Relationship;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import Model.ReferenceColor;
import View.PanZoomHelper;
import View.Render.IRenderDecorator;

public class RelationshipOpacityRender extends BaseRelationshipDecorator {

    private ReferenceColor mColor;
    
    public RelationshipOpacityRender(ReferenceColor c, IRenderDecorator wrapped) {
	super(wrapped);
	mColor = c;
    }

    @Override
    public void renderRelationship(Graphics gx, Point[] rel,
	    PanZoomHelper helper) {
	Color c = mColor.getColor();
	gx.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 50));
	gx.drawLine(rel[0].x, rel[0].y, rel[1].x, rel[1].y);
    }
}
