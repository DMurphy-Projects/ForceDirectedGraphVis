package Model;

import java.util.ArrayList;

import Model.Force.ForceNode;
import Model.Force.ForceRelationship;

public class ForceNodeGrouper {

    //all
    private ForceNode[] mNodes;
    private ForceRelationship[] mRels;

    //grouped
    private ForceNode[] mActualNodes;
    private ForceRelationship[] mActualRels;
    
    private int mGroup = 1;

    public ForceNodeGrouper(ForceNode[] nodes, ForceRelationship[] rels) {
	mNodes = nodes;
	mRels = rels;
	doGroup();
    }

    public void doGroup() {
	if (mGroup > 1) {
	    groupNodes();
	} else {
	    mActualNodes = mNodes;
	    mActualRels = mRels;
	}
    }

    //needs some intelligence
    public void groupNodes() {
	ArrayList<GroupedForceNode> groups = new ArrayList<GroupedForceNode>();
	GroupedForceNode group = new GroupedForceNode();
	int cur = 0;
	for (ForceNode f : mNodes) {
	    if (cur > mGroup - 1) {
		groups.add(group);
		group = new GroupedForceNode();
		cur = 0;
	    }
	    group.addNode(f);
	    cur++;
	}
	groups.add(group);

	mActualNodes = new ForceNode[groups.size()];
	int i = 0;
	for (GroupedForceNode f : groups) {
	    mActualNodes[i++] = f.getForceNode();
	}
	groupRelations(groups);
    }

    public ForceNode[] getNodes() {
	return mActualNodes;
    }

    public void groupRelations(ArrayList<GroupedForceNode> groups) {
	ArrayList<ForceRelationship> groupedRels = new ArrayList<ForceRelationship>();
	for (GroupedForceNode g1 : groups) {
	    for (GroupedForceNode g2 : groups) {
		if (g1 == g2)
		    continue;
		boolean b = false;
		for (ForceNode f1 : g1.getUngrouped()) {
		    for (ForceNode f2 : g2.getUngrouped()) {
			for (ForceRelationship r : mRels) {
			    ForceNode rNode1 = r.getRelationship().get(0);
			    ForceNode rNode2 = r.getRelationship().get(1);
			    if (rNode1 == f1 && rNode2 == f2) {
				// the groups are related
				ForceRelationship newRel = new ForceRelationship();
				newRel.addForceNode(g1.getForceNode());
				newRel.addForceNode(g2.getForceNode());
				groupedRels.add(newRel);
				b = true;
				break;
			    }
			}
			if (b)
			    break;
		    }
		    if (b)
			break;
		}
	    }
	}
	ForceRelationship[] r = new ForceRelationship[groupedRels.size()];
	mActualRels = groupedRels.toArray(r);
	//System.out.println(actualNodes.length + " "+actualRels.length);
    }

    public ForceRelationship[] getRels() {
	return mActualRels;
    }

}
