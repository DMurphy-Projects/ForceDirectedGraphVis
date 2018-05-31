package Model;

import java.awt.Point;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import Controller.DistanceCalculator;
import Model.Force.ForceNode;
import Model.Force.ForceRelationship;

public class Graph {
    
    private GraphDatabaseService mDatabase;

    private DistanceCalculator mDistCalc;

    //handles holding the nodes and relations
    private ForceNodeGrouper mGrouper;
    
    public Graph(GraphDatabaseService db, ForceNodeGrouper g) {
	mDatabase = db;
	mDistCalc = new DistanceCalculator();
	mGrouper = g;
    }

    public ForceNode[] Nodes() {
	//return mNodes;
	return mGrouper.getNodes();
    }

    public Transaction startTx() {
	return mDatabase.beginTx();
    }

    /*
    public void addRelationship(ForceRelationship rel) {
	mRels[relIndex++] = rel;
    }
    */

    public ForceRelationship[] getRelationships() {
	// System.out.println("here");
	/*if (mRels == null) {
	    Iterable<Relationship> list;
	    list = GlobalGraphOperations.at(mDatabase).getAllRelationships();
	    ArrayList<Relationship> rTemp = new ArrayList<Relationship>();
	    for (Relationship r : list) {
		rTemp.add(r);
	    }
	    // mRels = new Relationship[rTemp.size()];
	    mRels = rTemp.toArray(mRels);
	}*/
	//return mRels;
	return mGrouper.getRels();
    }

    public ForceNode findNode(long id) {
	ForceNode[] mNodes = mGrouper.getNodes();
	for (ForceNode fNode : mNodes) {
	    if (fNode.NodeInfo().getId() == id) {
		return fNode;
	    }
	}
	return null;
    }

    public ForceNode findNode(Point p, int rad) {
	ForceNode[] mNodes = mGrouper.getNodes();
	rad *= rad;// avoid sqrt
	float dist = 0;
	for (ForceNode f : mNodes) {
	    dist = mDistCalc.distanceNoRoot(f.getPoint(), p);
	    if (dist < rad) {
		return f;
	    }
	}
	return null;
    }

    public GraphDatabaseService getService() {
	return mDatabase;
    }

    public Point findCenter() {
	ForceNode[] mNodes = mGrouper.getNodes();
	Point avg = new Point(0, 0);
	for (ForceNode f : mNodes) {
	    Point node = f.getPoint();
	    avg.x += node.x;
	    avg.y += node.y;
	}
	// avg.x /= mNodes.size();
	// avg.y /= mNodes.size();
	avg.x /= mNodes.length;
	avg.y /= mNodes.length;
	return avg;
    }

    public int findWidth() {
	ForceNode[] mNodes = mGrouper.getNodes();
	Float scoreLow = null;
	Float scoreHigh = null;
	float s;
	for (ForceNode f : mNodes) {
	    s = f.getPosition().x;
	    if (scoreLow == null || scoreLow > s) {
		scoreLow = s;
	    }
	    if (scoreHigh == null || scoreHigh < s) {
		scoreHigh = s;
	    }
	}
	return (int) (scoreHigh - scoreLow);
    }

    public int findHeight() {
	ForceNode[] mNodes = mGrouper.getNodes();
	Float scoreLow = null;
	Float scoreHigh = null;
	float s;
	for (ForceNode f : mNodes) {
	    s = f.getPosition().y;
	    if (scoreLow == null || scoreLow > s) {
		scoreLow = s;
	    }
	    if (scoreHigh == null || scoreHigh < s) {
		scoreHigh = s;
	    }
	}
	return (int) (scoreHigh - scoreLow);
    }
}
