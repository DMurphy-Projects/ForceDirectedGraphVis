package View.Render.Node;

import java.awt.Graphics;
import java.awt.Point;

import Model.Force.ForceNode;
import View.IGraphNodeHoverListener;
import View.PanZoomHelper;

public class BasicNodeHoverEffect extends BaseNodeDecorator implements IGraphNodeHoverListener{

    private ForceNode mNodeToDraw;

    private BaseNodeDecorator mEffect;

    public BasicNodeHoverEffect(BaseNodeDecorator effect,
	    BaseNodeDecorator wrapped) {
	super(wrapped);
	mEffect = effect;
    }

    @Override
    public void onNodeHover(ForceNode f) {
	mNodeToDraw = f;
    }

    @Override
    public void renderNode(Graphics gx, ForceNode fNode, Point node,
	    PanZoomHelper helper) {
	if (mNodeToDraw != null) {
	    mEffect.recursiveRender(gx, helper, mEffect, mNodeToDraw);
	    mNodeToDraw = null;
	}
    }
   
}
