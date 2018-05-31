package View.Render;

import java.awt.Color;
import java.awt.Graphics;

import Model.Graph;
import View.PanZoomHelper;

public class FrameTimeCounter extends BaseRenderDecorator {

    private long mTime;

    public FrameTimeCounter(IRenderDecorator wrapped) {
	super(wrapped);
	mTime = System.currentTimeMillis();
    }

    @Override
    public void render(Graphics gx, Graph g, PanZoomHelper helper) {
	super.render(gx, g, helper);
	long time = System.currentTimeMillis();
	gx.setColor(Color.black);
	gx.drawString("Frame: " + (time - mTime), 10, 20);
	mTime= time;
    }
}
