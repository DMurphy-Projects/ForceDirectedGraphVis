package Model;

import java.awt.Point;
import java.util.ArrayList;

import Model.Force.ForceNode;

public class GroupedForceNode {

    ArrayList<ForceNode> mNodes;

    ForceNode mNode;

    public GroupedForceNode() {
	mNodes = new ArrayList<ForceNode>();
    }

    public void addNode(ForceNode node) {
	mNodes.add(node);
    }

    public ForceNode getForceNode() {
	if (mNode == null) {
	    int x = 0;
	    int y = 0;
	    for (ForceNode n : mNodes) {
		Point p = n.getPoint();
		x += p.x;
		y += p.y;
	    }
	    mNode = new ForceNode(x/mNodes.size(), y/mNodes.size(), null, null);
	}
	return mNode;
    }

    public ArrayList<ForceNode> getUngrouped() {
	return mNodes;
    }
}
