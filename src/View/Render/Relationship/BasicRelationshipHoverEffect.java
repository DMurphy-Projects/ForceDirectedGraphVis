package View.Render.Relationship;

import java.awt.Graphics;
import java.awt.Point;

import Model.Force.ForceNode;
import View.IGraphRelationHoverListener;
import View.PanZoomHelper;

public class BasicRelationshipHoverEffect extends BaseRelationshipDecorator implements IGraphRelationHoverListener {

    private ForceNode[] mRelToRender;

    private BaseRelationshipDecorator mEffect;

    public BasicRelationshipHoverEffect(BaseRelationshipDecorator effect,
	    BaseRelationshipDecorator wrapped) {
	super(wrapped);
	mEffect = effect;
    }

    @Override
    public void onRelationHover(ForceNode[] rel) {
	mRelToRender = rel;
    }

    @Override
    public void renderRelationship(Graphics gx, Point[] rel,
	    PanZoomHelper helper) {
	if (mRelToRender != null) {
	    Point[] relToRender = new Point[mRelToRender.length];
	    for (int i = 0; i < mRelToRender.length; i++) {
		relToRender[i] = helper.adjustPoint(mRelToRender[i].getPoint());
	    }
	    mEffect.renderRelationship(gx, relToRender, helper);
	    mRelToRender = null;
	}
    }

}
