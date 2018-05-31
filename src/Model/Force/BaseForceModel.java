package Model.Force;

import java.util.ArrayList;

import Controller.CoordinateMathCalculator;
import Controller.ForceVectorCalculator;
import Controller.UserController.KneelingCalc;
import Model.Graph;
import Model.IScaler;
import Model.ThreadPool;
import Model.Task.ResultantTask;

public abstract class BaseForceModel implements IForceModel {

    private ForceVectorCalculator mVectorCalc;
    private CoordinateMathCalculator mCoordCalc;
    private KneelingCalc mKneelCalc;

    protected ArrayList<Thread> mLoopers;

    private ThreadPool mPool;

    public BaseForceModel(ForceVectorCalculator vectorCalc,
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

	int length = graph.Nodes().length;
	int end = length;
	int totalActions = (length * (length - 1)) / 2;// (n*n-1)/2
	int group = Runtime.getRuntime().availableProcessors();
	// System.out.println("Start");
	// balances the no relation task more accurately on available processors
	for (int l = 0; l < length; l++) {
	    int total = 0;
	    ArrayList<Integer> toList = new ArrayList<Integer>();
	    do {
		Integer i;
		int actions;
		if (total == 0) {
		    actions = (end - 1) - l;
		    i = l;
		} else {
		    actions = (end - 1) - (length - 1);
		    length--;
		    i = length;
		}
		if (actions == 0 || l == length) {
		    break;
		}
		toList.add(i);
		total += actions;
	    } while (total < (totalActions / group) && length > l);
	    addTask(new NoRelationTaskAlt(graph, toList));
	}

	// addTask(new NoRelationTask(graph, 0, graph.Nodes().length));

	// int split = Runtime.getRuntime().availableProcessors();
	// int split = (int) FastMath.sqrt(length);

	/*
	 * int size = (length / split); if (size == 0) { size = 1; } int end;
	 * System.out.println("Start"); /*for (int i = 0; i < length; i += size)
	 * { end = i + size; // System.out.println("Start "+i+" End "+end); if
	 * (end > length) { addTask(new NoRelationTask(graph, i, length)); }
	 * else { addTask(new NoRelationTask(graph, i, end)); } }
	 */

	// addTask(new NoRelationTask(graph, 0, length));
	mPool.waitUntilDone();
	// System.out.println("End");
	/*
	 * // new problem /* for (ForceNode fNode : graph.Nodes()) { addTask(new
	 * SingleResultantTask(fNode, rate.getValue())); }
	 * mPool.waitUntilDone();
	 */
	int left = graph.Nodes().length;
	group = 1;
	int i = 0;

	// is not properly balanced -need better method
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
    @SuppressWarnings("unused")
    private class NoRelationTask extends Thread {
	private Graph mGraph;

	private int mStart;
	private int mEnd;

	public NoRelationTask(Graph g, int start, int end) {
	    mGraph = g;
	    mStart = start;
	    mEnd = end;
	}

	public void run() {
	    // nodes without relationships
	    ForceNode[] g = mGraph.Nodes();
	    int limit = g.length;
	    // long start = System.currentTimeMillis();
	    for (int i = mStart; i < mEnd; i++) {
		for (int ii = i + 1; ii < limit; ii++) {
		    // System.out.println(i+">"+ii);
		    nodesWithoutRelationship(g[i], g[ii]);
		}
	    }
	    // long taken = System.currentTimeMillis() - start;
	    // System.out.println(taken);
	}
    }

    private class NoRelationTaskAlt extends Thread {
	private Graph mGraph;

	private ArrayList<Integer> mToDo;

	public NoRelationTaskAlt(Graph g, ArrayList<Integer> toDo) {
	    mGraph = g;
	    mToDo = toDo;
	}

	public void run() {
	    // nodes without relationships
	    ForceNode[] g = mGraph.Nodes();
	    int limit = g.length;
	    for (int i : mToDo) {
		// System.out.println(i);
		for (int ii = i + 1; ii < limit; ii++) {
		    // System.out.println(i+">"+ii);
		    nodesWithoutRelationship(g[i], g[ii]);
		}
	    }
	    // System.out.println("Count> "+count);
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

    protected abstract void nodesWithoutRelationship(ForceNode n1, ForceNode n2);
}
