package View.Render;

import java.awt.Graphics;

import Model.Graph;
import View.PanZoomHelper;

public abstract class BaseHoverEffect extends BaseRenderDecorator {

    public BaseHoverEffect(IRenderDecorator wrapped) {
	super(wrapped);
    }

    @Override
    public void render(Graphics gx, Graph g, PanZoomHelper helper) {
	super.render(gx, g, helper);
	renderEffect(gx, g, helper);
    }

    protected abstract void renderEffect(Graphics gx, Graph g,
	    PanZoomHelper helper);
}
