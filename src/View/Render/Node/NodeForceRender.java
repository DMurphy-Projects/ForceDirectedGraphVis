package View.Render.Node;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import Model.Force.ForceNode;
import Model.Force.ForceVector;
import View.PanZoomHelper;
import View.Render.IRenderDecorator;

public class NodeForceRender extends BaseNodeDecorator {

    public NodeForceRender(IRenderDecorator wrapped) {
	super(wrapped);
    }

    @Override
    public void renderNode(Graphics gx, ForceNode fNode, Point node,
	    PanZoomHelper helper) {
	ForceVector vec = fNode.getFinalVector();

	int x = (int) (50 * Math.cos(vec.getDirection())) + node.x;
	int y = (int) (50 * Math.sin(vec.getDirection())) + node.y;

	int alpha = (int) (vec.getForceValue() / 100);
	if (alpha < 100) {
	    alpha = 100;
	} else if (alpha > 255) {
	    alpha = 255;
	}

	Color base = Color.green;
	gx.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(),
		alpha));
	gx.drawLine(node.x, node.y, x, y);
	gx.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(),
		255));
	gx.drawString("F: "+vec.getForceValue(), node.x, node.y);
    }

}
