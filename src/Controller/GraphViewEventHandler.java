package Controller;

import java.util.ArrayList;

import Model.Force.ForceNode;
import View.IGraphNodeHoverEvent;
import View.IGraphNodeHoverListener;
import View.IGraphRelationHoverEvent;
import View.IGraphRelationHoverListener;

public class GraphViewEventHandler implements IGraphNodeHoverListener,
	IGraphRelationHoverListener {

    private ArrayList<IGraphNodeHoverListener> mObjectsNodeHandle;
    private ArrayList<IGraphRelationHoverListener> mObjectsRelationHandle;

    public GraphViewEventHandler() {
	mObjectsNodeHandle = new ArrayList<IGraphNodeHoverListener>();
	mObjectsRelationHandle = new ArrayList<IGraphRelationHoverListener>();
    }

    public void addSource(IGraphNodeHoverEvent e) {
	e.addNodeHoverListener(this);
    }

    public void removeSource(IGraphNodeHoverEvent e) {
	e.removeNodeHoverListener(this);
    }

    public void addSource(IGraphRelationHoverEvent e) {
	e.addRelationHoverListener(this);
    }

    public void removeSource(IGraphRelationHoverEvent e) {
	e.addRelationHoverListener(this);
    }

    public void addListener(IGraphNodeHoverListener l) {
	mObjectsNodeHandle.add(l);
    }

    public void removeListener(IGraphNodeHoverListener l) {
	mObjectsNodeHandle.remove(l);
    }

    public void addListener(IGraphRelationHoverListener l) {
	mObjectsRelationHandle.add(l);
    }

    public void removeListener(IGraphRelationHoverListener l) {
	mObjectsRelationHandle.remove(l);
    }

    @Override
    public void onNodeHover(ForceNode f) {
	// System.out.println("Hover on " + f.X() + " " + f.Y());
	for (IGraphNodeHoverListener l : mObjectsNodeHandle) {
	    l.onNodeHover(f);
	}
    }

    @Override
    public void onRelationHover(ForceNode[] rel) {
	for (IGraphRelationHoverListener l : mObjectsRelationHandle) {
	    l.onRelationHover(rel);
	}
    }
}
