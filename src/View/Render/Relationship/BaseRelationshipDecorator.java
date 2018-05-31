package View.Render.Relationship;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import org.neo4j.graphdb.Transaction;

import Model.Graph;
import Model.Force.ForceNode;
import Model.Force.ForceRelationship;
import View.PanZoomHelper;
import View.Render.BaseRenderDecorator;
import View.Render.IRenderDecorator;

public abstract class BaseRelationshipDecorator extends BaseRenderDecorator {

    public BaseRelationshipDecorator(IRenderDecorator wrapped) {
	super(wrapped);
    }

    @Override
    public void render(Graphics gx, Graph g, PanZoomHelper helper) {
	super.render(gx, g, helper);
	Point node1;
	Point node2;
	ArrayList<ForceNode> r1;

	try (Transaction tx = g.startTx()) {
	    for (ForceRelationship r : g.getRelationships()) {
		r1 = r.getRelationship();
		node1 = helper.adjustPoint(r1.get(0).getPoint());
		node2 = helper.adjustPoint(r1.get(1).getPoint());
		renderRelationship(gx, new Point[] { node1, node2 }, helper);
	    }
	    tx.success();
	}
    }

    public abstract void renderRelationship(Graphics gx, Point[] rel,
	    PanZoomHelper helper);
}
