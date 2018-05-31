package View.Render.Node;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import Model.ReferenceColor;
import Model.Force.ForceNode;
import View.PanZoomHelper;
import View.Render.IRenderDecorator;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class NodeInfoRender extends BaseNodeDecorator {

    private ReferenceColor mBackColor;
    private ReferenceColor mTextColor;
    
    public NodeInfoRender(ReferenceColor background, ReferenceColor text, IRenderDecorator wrapped) {
	super(wrapped);
	mBackColor = background;
	mTextColor = text;
    }

    @Override
    public void renderNode(Graphics gx, ForceNode fNode, Point node,
	    PanZoomHelper helper) {
	//System.out.println("Info");
	Node info = fNode.NodeInfo();
	try (Transaction tx = info.getGraphDatabase().beginTx()) {
	    ArrayList<String> details = new ArrayList<String>();
	    String lg = "";
	    for (String l : info.getPropertyKeys()) {
		String s = l + " : " + info.getProperty(l).toString();
		details.add(s);
		if (s.length() > lg.length()) {
		    lg = s;
		}
	    }
	    if (details.size() > 0) {
		FontMetrics metrics = gx.getFontMetrics();
		int height = details.size() * metrics.getHeight();
		int width = metrics.stringWidth(lg);

		int padding = 5;
		gx.setColor(mBackColor.getColor());
		gx.fillRect(node.x - padding, node.y - padding, width
			+ (padding * 2), height + (padding * 2));
		gx.setColor(mTextColor.getColor());
		gx.drawRect(node.x - padding, node.y - padding, width
			+ (padding * 2), height + (padding * 2));
		int y = node.y;
		for (String s : details) {
		    gx.drawString(s, node.x, y += metrics.getHeight());
		}
	    }
	    tx.success();
	}

    }

}
