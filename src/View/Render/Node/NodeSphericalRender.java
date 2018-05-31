package View.Render.Node;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import Model.ReferenceColor;
import Model.Force.ForceNode;
import View.PanZoomHelper;
import View.Render.IRenderDecorator;

public class NodeSphericalRender extends BaseNodeDecorator {

    private int mRadius = 10;

    private ReferenceColor mColor;

    public NodeSphericalRender(ReferenceColor c, IRenderDecorator wrapped) {
	super(wrapped);
	mColor = c;
    }

    @Override
    public void renderNode(Graphics gx, ForceNode fNode, Point node,
	    PanZoomHelper helper) {
	int cOffset = mRadius / 2;
	int x = (int) (node.x - (cOffset * helper.Zoom()));
	int y = (int) (node.y - (cOffset * helper.Zoom()));
	int rad = (int) (mRadius * helper.Zoom());
	int hRad = (int) ((mRadius / 2) * helper.Zoom());
	int qaurterRad = (int) (((float) mRadius / 4) * helper.Zoom());
	int eighthRad = (int) (((float) mRadius / 8) * helper.Zoom());

	if (rad == 0 || hRad == 0 || qaurterRad == 0 || eighthRad == 0) {
	    return;
	}

	Graphics2D g2 = (Graphics2D) gx;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

	Color c = mColor.getColor();

	// Fills the circle with solid blue color
	g2.setColor(c);
	g2.fillOval(x, y, rad, rad);

	// Adds shadows at the top
	Paint p;
	p = new GradientPaint(x, y, new Color(0.0f, 0.0f, 0.0f, 0.4f), x, y
		+ rad, new Color(0.0f, 0.0f, 0.0f, 0.0f));
	g2.setPaint(p);
	g2.fillOval(x, y, rad, rad);

	// Adds highlights at the bottom
	p = new GradientPaint(x, y, new Color(1.0f, 1.0f, 1.0f, 0.0f), x, y
		+ rad, new Color(1.0f, 1.0f, 1.0f, 0.4f));
	g2.setPaint(p);
	g2.fillOval(x, y, rad, rad);

	Color dark = new Color(c.getRed(), c.getGreen(), c.getBlue(), 127);
	// Creates dark edges for 3D effect
	p = new RadialGradientPaint(new Point2D.Double(x + hRad, y + hRad),
		hRad, new float[] { 0.0f, 1f }, new Color[] { dark,
			new Color(0.0f, 0.0f, 0.0f, 0.8f) });
	g2.setPaint(p);
	g2.fillOval(x, y, rad, rad);

	// Adds oval specular highlight at the top left
	p = new RadialGradientPaint(new Point2D.Double(x + hRad, y + hRad),
		hRad, new Point2D.Double(x + qaurterRad, y + qaurterRad),
		new float[] { 0.0f, 0.8f }, new Color[] {
			new Color(1.0f, 1.0f, 1.0f, 0.4f),
			new Color(1.0f, 1.0f, 1.0f, 0.0f) },
		RadialGradientPaint.CycleMethod.NO_CYCLE);
	g2.setPaint(p);
	g2.fillOval(x, y, rad, rad);

	c = c.darker();
	// Adds oval inner highlight at the bottom p = new
	p = new RadialGradientPaint(new Point2D.Double(x + hRad,
		(y + hRad + qaurterRad) / 0.7), hRad - eighthRad,
		new Point2D.Double(x + hRad, (y + rad - eighthRad) / 0.7),
		new float[] { 0.0f, 0.8f }, new Color[] {
			new Color(c.getRed(), c.getGreen(), c.getBlue(), 255),
			new Color(c.getRed(), c.getGreen(), c.getBlue(), 0) },
		RadialGradientPaint.CycleMethod.NO_CYCLE,
		RadialGradientPaint.ColorSpaceType.SRGB,
		AffineTransform.getScaleInstance(1, 0.7));
	g2.setPaint(p);
	g2.fillOval(x, y, rad, rad);
    }
}
