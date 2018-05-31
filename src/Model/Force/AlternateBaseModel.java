package Model.Force;

import java.awt.Point;
import java.util.ArrayList;

import Controller.CoordinateMathCalculator;
import Controller.ForceVectorCalculator;
import Controller.UserController.KneelingCalc;
import Model.Graph;
import Model.IScaler;
import Model.ThreadPool;
import Model.Task.ResultantTask;

public abstract class AlternateBaseModel implements IForceModel {

    protected ForceVectorCalculator mVectorCalc;
    private CoordinateMathCalculator mCoordCalc;
    private KneelingCalc mKneelCalc;

    protected ArrayList<Thread> mLoopers;

    private ThreadPool mPool;

    public AlternateBaseModel(ForceVectorCalculator vectorCalc,
	    CoordinateMathCalculator coordCalc, KneelingCalc kc) {
	mVectorCalc = vectorCalc;
	mCoordCalc = coordCalc;
	mKneelCalc = kc;
	mLoopers = new ArrayList<Thread>();
	mPool = ThreadPool.getInstance();
    }

    protected void addTask(Runnable r) {
	mPool.execute(r);
    }

    public void manipulate(final Graph graph, IScaler rate) {
	addTask(new RelationTask(graph));
	addTask(new AllNodesTask(graph));
	mPool.waitUntilDone();

	int left = graph.Nodes().length;
	int group = 1;
	int i = 0;

	while (left > 0) { // group bigger than amount of nodes
	    if (left < group) {
		addTask(new ResultantTask(graph.Nodes(), mCoordCalc,
			mVectorCalc, rate.getValue(), i, i += left));
	    } // there are still some nodesleft
	    else if (left > 0) {
		addTask(new ResultantTask(graph.Nodes(), mCoordCalc,
			mVectorCalc, rate.getValue(), i, i += group));
	    }
	    left -= group;
	}
	float force;
	float lgForce = -1;
	for (ForceNode f : graph.Nodes()) {
	    force = f.getFinalVector().getForceValue();
	    if (force > lgForce || lgForce == -1) {
		lgForce = force;
	    }
	}
	mKneelCalc.setKneel(lgForce);
	mPool.waitUntilDone();
    }

    // generally there are more non relations than relations
    // this one one thread is slow

    private class AllNodesTask extends Thread {
	private Graph mGraph;

	public AllNodesTask(Graph g) {
	    mGraph = g;
	}

	public void run() {
	    // nodes without relationships
	    ForceNode[] g = mGraph.Nodes();
	    Point centre = mGraph.findCenter();
	    for (ForceNode fNode : g) {
		allNodes(fNode, centre);
	    }
	}
    }

    private class RelationTask extends Thread {
	private Graph mGraph;

	public RelationTask(Graph g) {
	    mGraph = g;
	}

	public void run() {
	    // nodes with relationship
	    ArrayList<ForceNode> r1;
	    for (ForceRelationship r : mGraph.getRelationships()) {
		r1 = r.getRelationship();
		nodesWithRelationship(r1.get(0), r1.get(1));
	    }
	}
    }

    protected abstract void nodesWithRelationship(ForceNode n1, ForceNode n2);

    protected abstract void allNodes(ForceNode n1, Point p);
}
