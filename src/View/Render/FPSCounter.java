package View.Render;

import java.awt.Color;
import java.awt.Graphics;

import Controller.Limiter;
import Model.Graph;
import View.PanZoomHelper;

public class FPSCounter extends BaseRenderDecorator {

    int displayValue = 0;

    private Limiter mFpsLimiter;
    
    public FPSCounter(Limiter l, IRenderDecorator wrapped) {
	super(wrapped);
	mFpsLimiter = l;
    }

    @Override
    public void render(Graphics gx, Graph g, PanZoomHelper helper) {
	super.render(gx, g, helper);
	gx.setColor(Color.black);
	gx.drawString("FPS: " + mFpsLimiter.getActions(), 10, 10);
	mFpsLimiter.doAction();
    }

}
