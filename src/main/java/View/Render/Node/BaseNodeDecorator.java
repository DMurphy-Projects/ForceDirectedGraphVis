package View.Render.Node;

import java.awt.Graphics;
import java.awt.Point;

import Model.Graph;
import Model.Force.ForceNode;
import View.PanZoomHelper;
import View.Render.BaseRenderDecorator;
import View.Render.IRenderDecorator;

public abstract class BaseNodeDecorator extends BaseRenderDecorator {

    protected int mRadius = 10;

    public BaseNodeDecorator(IRenderDecorator wrapped) {
	super(wrapped);
    }

    @Override
    public void render(Graphics gx, Graph g, PanZoomHelper helper) {
	super.render(gx, g, helper);
	Point node;
	for (ForceNode fNode : g.Nodes()) {
	    node = helper.adjustPoint(fNode.getPoint());
	    renderNode(gx, fNode, node, helper);
	}
    }

    // relies on the effect being a series of BaseNodeDecorator
    // hard crash if not
    //this will render a single node - useful for hover effects where only one node is selected
    protected void recursiveRender(Graphics gx, PanZoomHelper helper,
	    BaseNodeDecorator effect, ForceNode fNode) {
	if (effect == null) {
	    return;
	}
	recursiveRender(gx, helper, (BaseNodeDecorator) effect.mDecoration,
		fNode);
	effect.renderNode(gx, fNode, helper.adjustPoint(fNode.getPoint()),
		helper);
    }

    public abstract void renderNode(Graphics gx, ForceNode fNode, Point node,
	    PanZoomHelper helper);
}
