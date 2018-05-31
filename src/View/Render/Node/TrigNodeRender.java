package View.Render.Node;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import Model.Force.ForceNode;
import View.PanZoomHelper;
import View.Render.IRenderDecorator;

public class TrigNodeRender extends BaseNodeDecorator {

	private float mRadius = 40;
	private float mApplitude = 1;
	private float mFrequency = 8;
	private float mOffset = 0;

	public TrigNodeRender(IRenderDecorator wrapped) {
		super(wrapped);
		mRadius = (float) Math.sqrt(mRadius);
	}

	@Override
	public void renderNode(Graphics gx, ForceNode fNode, Point node,
			PanZoomHelper helper) {
		Polygon p = new Polygon();
		for (double i = 0; i < 360; i += 1) {
			int dist = (int) mRadius;
			dist += (mApplitude * Math.sin(Math.toRadians((mOffset + i)
					* mFrequency)));
			// dist += (mApplitude * Math.cos(Math.toRadians((mOffset + i) *
			// mFrequency)));
			dist *= helper.Zoom();

			int x = (int) (node.x + (dist * Math.cos(Math.toRadians(i))));
			int y = (int) (node.y + (dist * Math.sin(Math.toRadians(i))));
			p.addPoint(x, y);
			/*
			 * if (first) { first = false; } else { gx.drawLine(xprev, yprev, x,
			 * y); }
			 * 
			 * xprev = x; yprev = y;
			 */
		}
		gx.setColor(Color.white);
		gx.fillPolygon(p);
		gx.setColor(Color.black);
		gx.drawPolygon(p);
		mOffset += 0.1f;
		if (mOffset > 360) {
			mOffset = 0;
		}
	}

}
