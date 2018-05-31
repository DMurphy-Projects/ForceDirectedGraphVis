package View.Render.Node;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import Model.ReferenceColor;
import Model.Force.ForceNode;
import View.PanZoomHelper;
import View.Render.IRenderDecorator;

public class BasicNodeRender extends BaseNodeDecorator {

    private ReferenceColor mColor;

    public BasicNodeRender(ReferenceColor c, IRenderDecorator wrapped) {
	super(wrapped);
	mColor = c;
    }

    @Override
    public void renderNode(Graphics gx, ForceNode fNode, Point node,
	    PanZoomHelper helper) {
	//System.out.println("Basic " + node + " "+mColor);
	
	int outerOffset = mRadius / 2;
	int innerOffset = (int) (mRadius * 0.35);

	Point outerCircle = new Point((int) (node.x - outerOffset
		* helper.Zoom()), (int) (node.y - outerOffset * helper.Zoom()));

	Point innerCircle = new Point((int) (node.x - innerOffset
		* helper.Zoom()), (int) (node.y - innerOffset * helper.Zoom()));

	int outerRad = (int) (outerOffset * 2 * helper.Zoom());
	int innerRad = (int) (innerOffset * 2 * helper.Zoom());

	Color c = mColor.getColor().darker();

	gx.setColor(c);
	gx.fillOval(outerCircle.x, outerCircle.y, outerRad, outerRad);

	gx.setColor(c.brighter());
	gx.fillOval(innerCircle.x, innerCircle.y, innerRad, innerRad);

	gx.setColor(Color.BLACK);
	gx.drawOval(outerCircle.x, outerCircle.y, outerRad, outerRad);
    }
}
