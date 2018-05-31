package View.Render;

import java.awt.Graphics;

import Model.Graph;
import View.PanZoomHelper;

public class BaseRenderDecorator implements IRenderDecorator {

    protected IRenderDecorator mDecoration;

    public BaseRenderDecorator(IRenderDecorator wrapped) {
	mDecoration = wrapped;
    }

    @Override
    public void render(Graphics gx, Graph g, PanZoomHelper helper) {
	if (mDecoration != null) {
	    mDecoration.render(gx, g, helper);
	}
    }
}
