package View.Render.Node;

import java.awt.Graphics;
import java.awt.Point;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import Model.Force.ForceNode;
import View.PanZoomHelper;
import View.Render.IRenderDecorator;

public class NodeHighlighter extends BaseNodeDecorator {

    private static String mSearchString = "Evan";

    private BaseNodeDecorator mHighlightEffect;

    public NodeHighlighter(BaseNodeDecorator effect, IRenderDecorator wrapped) {
	super(wrapped);
	mHighlightEffect = effect;
    }

    public static void setSearchString(String s)
    {
	mSearchString = s;
    }
    public static String getSearchString()
    {
	return mSearchString;
    }
    
    @Override
    public void renderNode(Graphics gx, ForceNode fNode, Point node,
	    PanZoomHelper helper) {
	Node info = fNode.NodeInfo();
	try (Transaction tx = info.getGraphDatabase().beginTx()) {
	    for (String l : info.getPropertyKeys()) {
		String s = l + " : " + info.getProperty(l).toString();
		if (s.toLowerCase().contains(mSearchString.toLowerCase())) {
		    recursiveRender(gx, helper, mHighlightEffect, fNode);
		    break;
		}
	    }
	    tx.success();
	}
    }
}
