package Controller;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.TransactionFailureException;

import Controller.UserController.IUserController;
import Model.GUIScaler;
import Model.Graph;
import Model.GraphPackage;
import Model.IScaler;
import Model.ThemePair;
import Model.Force.IForceModel;
import View.GraphView;
import View.IGraphNodeHoverEvent;
import View.IGraphRelationHoverEvent;

public class GraphController {

    private Neo4jDatabaseHandler gParser;

    private List<GraphPackage> mGraphs;
    private int mSelectedGraph = -1;

    private int mTickIntervals;

    private ControllerTick mTicker;

    private GraphViewEventHandler mEventHandler;

    private OptionSelector mDecSelector;
    private OptionSelector mModelSelector;

    private Limiter mRenderLimiter;

    private ArrayList<IUserController> mUIControllers;

    public GraphController(String path, OptionSelector mSel,
	    OptionSelector dSelc, GraphViewEventHandler eHandler, int interval,
	    Limiter l) {
	mTickIntervals = interval;
	gParser = new Neo4jDatabaseHandler(path);
	mGraphs = new ArrayList<GraphPackage>();
	mEventHandler = eHandler;
	mDecSelector = dSelc;
	mModelSelector = mSel;

	mRenderLimiter = l;

	mUIControllers = new ArrayList<IUserController>();
    }

    public void addController(IUserController uic) {
	mUIControllers.add(uic);
    }

    private void addGraph(Graph g) {
	GraphView gView = new GraphView(g);

	// mMouseControls.changeView(gView);

	GraphPackage pack = new GraphPackage(g, gView,
		((ThemePair) mDecSelector.getOption().getOption()),
		(IForceModel) mModelSelector.getOption().getOption(),
		new GUIScaler(10f / 1000));
	mEventHandler.addSource((IGraphNodeHoverEvent) gView);
	mEventHandler.addSource((IGraphRelationHoverEvent) gView);

	mGraphs.add(pack);

	if (mSelectedGraph == -1) {
	    startTicker();
	}

	mSelectedGraph = mGraphs.size() - 1;

    }

    public void removeGraph(int i) {
	if (i > -1) {
	    stopTicks();
	    GraphPackage rem = mGraphs.get(i);
	    GraphView view = rem.getView();
	    mEventHandler.removeSource((IGraphNodeHoverEvent) view);
	    mEventHandler.removeSource((IGraphRelationHoverEvent) view);
	    mGraphs.remove(i);
	    // mMouseControls.removeView(view);

	    if (mGraphs.size() == 0) {
		mSelectedGraph = -1;
	    } else {
		changeGraph(0);
		startTicker();
	    }
	    mSelectedGraph = mGraphs.size() - 1;
	    // System.out.println(selectedGraph);
	}
    }

    public Graph newGraph(final String dbPath) {
	Graph g;
	g = gParser.parseGraph(dbPath);

	addGraph(g);
	return g;
    }

    public Graph newRandomGraph(int numOfNodes) {
	Graph g = gParser.newRandomGraph(numOfNodes);
	addGraph(g);
	return g;
    }

    public GraphPackage getPackage(int i) {
	if (i != -1) {
	    return mGraphs.get(i);
	}
	return null;
    }

    public void updateDecoration() {
	if (mSelectedGraph > -1) {
	    mGraphs.get(mSelectedGraph).setTheme(
		    ((ThemePair) mDecSelector.getOption().getOption()));
	}
    }

    public void updateModel() {
	if (mSelectedGraph > -1) {
	    mGraphs.get(mSelectedGraph).setModel(
		    (IForceModel) mModelSelector.getOption().getOption());
	}
    }

    public void updateScaler(IScaler scaler) {
	if (mSelectedGraph > -1) {
	    mGraphs.get(mSelectedGraph).setScaler(scaler);
	}
    }

    public int currentIndex() {
	return mSelectedGraph;
    }

    public Graph getGraph(int i) {
	return mGraphs.get(i).getGraph();
    }

    public GraphView getView(int i) {
	return mGraphs.get(i).getView();
    }

    public void changeGraph(int i) {
	if (i > -1) {
	    mSelectedGraph = i;
	}
    }

    private void startTicker() {
	mTicker = new ControllerTick();
	mTicker.start();
    }

    public void tickCurrentGraph() {
	// every tick manipulate current graph/view
	// System.out.println("Selected Graph " + selectedGraph);
	if (mSelectedGraph >= 0) {
	    GraphPackage curGraph = mGraphs.get(mSelectedGraph);

	    Graph g = curGraph.getGraph();
	    GraphView gView = curGraph.getView();
	    gView.setDecorations(curGraph.getTheme().getDecoration());

	    Boolean canAction = mRenderLimiter.canAction();
	    if (canAction) {
		for (IUserController controller : mUIControllers) {
		    controller.update(curGraph);
		}
	    }

	    // mAutoCamera.update(gView);
	    // mMouseControls.update();

	    try (Transaction tx = g.startTx()) {
		// System.out.println(mGraphs.get(selectedGraph).getGraph().Nodes().length);
		// System.out.println(mGraphs.get(selectedGraph).getGraph().findCenter());
		//System.out.println(curGraph.getScaler().getValue());
			if (curGraph.getScaler().getValue() != 0) {
				curGraph.getModel().manipulate(g, curGraph.getScaler());
			}
		tx.success();
	    } catch (TransactionFailureException e) {

	    }
	    if (canAction) {
		gView.repaint();
	    }
	    /*
	     * if (canAction) { gView.repaint(); // float echo; if
	     * (mAutoKneeling) { curGraph.getScaler().setValue(
	     * mKneelCalc.getKneelValue() / 1000); } //
	     * System.out.println("Value "+echo*1000); }
	     */
	    // stopTicks();
	    // System.out.println(allGraphs.get(selectedGraph).Nodes().size());
	}
    }

    public void stopTicks() {
	if (mTicker != null) {
	    mTicker.stopTicks();
	    if (mTicker.isAlive()) {
		try {
		    mTicker.join();
		} catch (InterruptedException e) {
		}
	    }
	}
    }

    class ControllerTick extends Thread {
	private boolean mEnd = false;

	public void stopTicks() {
	    mEnd = true;
	}

	public void run() {
	    try {
		while (!mEnd) {
		    tickCurrentGraph();
		    Thread.sleep(mTickIntervals);
		}
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}

    }
}
