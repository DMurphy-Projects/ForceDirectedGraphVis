package View;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JComponent;

import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.TransactionFailureException;

import Controller.DistanceCalculator;
import Model.Graph;
import Model.Force.ForceNode;
import Model.Force.ForceRelationship;
import View.Render.IRenderDecorator;

public class GraphView extends JComponent implements IGraphNodeHoverEvent, IGraphRelationHoverEvent,
	MouseMotionListener {

    private static final long serialVersionUID = 1L;

    private Graph mGraphModel;

    private IRenderDecorator mDecorations;

    private PanZoomHelper mHelper;

    private DistanceCalculator mDistCalc;

    private ArrayList<IGraphNodeHoverListener> mNodeListeners;
    private ArrayList<IGraphRelationHoverListener> mRelationListeners;

    private int mNotifyRadius = 10;

    private Point mMousePosition;

    public GraphView(Graph model) {
	mGraphModel = model;
	mHelper = new PanZoomHelper();
	mNodeListeners = new ArrayList<IGraphNodeHoverListener>();
	mRelationListeners = new ArrayList<IGraphRelationHoverListener>();
	mMousePosition = new Point(0, 0);
	mDistCalc = new DistanceCalculator();

	mNotifyRadius *= mNotifyRadius;// avoid sqrt

	this.addMouseMotionListener(this);
    }

    public Graph getGraph() {
	return mGraphModel;
    }

    public PanZoomHelper getHelper() {
	return mHelper;
    }

    public void setDecorations(IRenderDecorator dec) {
	mDecorations = dec;
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	mHelper.setWindowSize(getWidth(), getHeight());

	if (mDecorations != null) {
	    //System.out.println("Start");
	    mDecorations.render(g, mGraphModel, mHelper);
	    //System.out.println("End");
	}
    }

    public Dimension getPreferredSize() {
	return new Dimension(400, 400);
    }

    public Dimension getMinimumSize() {
	return getPreferredSize();
    }

    @Override
    public void notifyNodeHover() {
	for (ForceNode f : mGraphModel.Nodes()) {
	    if (mDistCalc.distanceNoRoot(f.getPoint(),
		    mHelper.reversePoint(mMousePosition)) < mNotifyRadius) {
		for (IGraphNodeHoverListener l : mNodeListeners) {
		    l.onNodeHover(f);
		}
	    }
	}
    }

    @Override
    public void notifyRelationHover() {
	ForceNode a;
	ForceNode b;
	Point c = mHelper.reversePoint(mMousePosition);

	ArrayList<ForceNode> r1;
	
	try (Transaction tx = mGraphModel.startTx()) {
	    for (ForceRelationship r : mGraphModel.getRelationships()) {
		r1 =r.getRelationship(); 
		a = r1.get(0);
		b = r1.get(1);

		if (mDistCalc.distanceToLineSegment(c, a.getPoint(),
			b.getPoint()) < mNotifyRadius) {
		    for (IGraphRelationHoverListener l : mRelationListeners) {
			l.onRelationHover(new ForceNode[] { a, b });
		    }
		}
	    }
	    tx.success();
	} catch (TransactionFailureException e) {

	}
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
	mouseMoved(arg0);
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
	mMousePosition.x = arg0.getX();
	mMousePosition.y = arg0.getY();
    }

    @Override
    public void addRelationHoverListener(IGraphRelationHoverListener l) {
	mRelationListeners.add(l);
    }

    @Override
    public void removeRelationHoverListener(IGraphRelationHoverListener l) {
	mRelationListeners.remove(l);	
    }

    @Override
    public void addNodeHoverListener(IGraphNodeHoverListener l) {
	mNodeListeners.add(l);	
    }

    @Override
    public void removeNodeHoverListener(IGraphNodeHoverListener l) {
	mNodeListeners.remove(l);	
    }
}