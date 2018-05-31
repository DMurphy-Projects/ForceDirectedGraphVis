package View.Render.Node;

import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

import org.neo4j.graphdb.Transaction;

import Model.Graph;
import Model.Force.ForceNode;
import View.PanZoomHelper;
import View.Render.IRenderDecorator;

//allows nodes to be toggled on and off based on the nodes tag data
public class NodeTagRender extends BaseNodeDecorator {

    private static Boolean mRefreshNeeded = false;

    private static HashMap<String, Boolean> mProperties;

    private BaseNodeDecorator mEffect;

    public NodeTagRender(BaseNodeDecorator effect, IRenderDecorator wrapped) {
	super(wrapped);
	mProperties = new HashMap<String, Boolean>();
	mEffect = effect;
    }

    public static void refreshProperties() {
	mRefreshNeeded = true;
    }
    
    public static Iterable<String> getProperties()
    {
	return mProperties.keySet();
    }
    public static void toggle(String prop)
    {
	Boolean b = mProperties.get(prop);
	b = !b;
	mProperties.put(prop, b);
    }

    @Override
    public void render(Graphics gx, Graph g, PanZoomHelper helper) {
	if (mRefreshNeeded) {
	    mProperties.clear();
	    // find all properties
	    try (Transaction tx = g.startTx()) {
		for (ForceNode n : g.Nodes()) {
		    for (String prop : n.NodeInfo().getPropertyKeys()) {
			if (!mProperties.containsKey(prop)) {
			    mProperties.put(prop, true);// everythings visible
							// to begin with
			}
		    }
		}
		tx.success();
		mRefreshNeeded = false;
	    }
	}
	super.render(gx, g, helper);
    }

    @Override
    public void renderNode(Graphics gx, ForceNode fNode, Point node,
	    PanZoomHelper helper) {
	try (Transaction tx = fNode.NodeInfo().getGraphDatabase().beginTx()) {
	    for (String prop : fNode.NodeInfo().getPropertyKeys()) {
		Boolean paint = mProperties.get(prop);
		if (paint != null)
		    if (!paint) {
			return;
		    }
	    }
	}
	// if it gets here it is allowed to be rendered
	recursiveRender(gx, helper, mEffect, fNode);
    }

}
