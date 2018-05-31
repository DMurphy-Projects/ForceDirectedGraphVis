package View.Render.Node;

import java.awt.Graphics;
import java.awt.Point;

import Model.ReferenceColor;
import Model.Force.ForceNode;
import View.PanZoomHelper;
import View.Render.IRenderDecorator;

public class BasicNodeSizeBasedRender extends BasicNodeRender {

    public BasicNodeSizeBasedRender(ReferenceColor c, IRenderDecorator wrapped) {
	super(c, wrapped);
    }

    @Override
    public void renderNode(Graphics gx, ForceNode fNode, Point node,
	    PanZoomHelper helper) {
	mRadius = (fNode.RelationshipInfo().length + 3) * 2;
	super.renderNode(gx, fNode, node, helper);
    }

}
