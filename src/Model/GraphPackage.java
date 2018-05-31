package Model;

import Model.Force.IForceModel;
import View.GraphView;

public class GraphPackage {

    private Graph mGraph;

    private GraphView mView;

    private IForceModel mForceModel;

    private ThemePair mTheme;

    private IScaler mScaler;

    public GraphPackage(Graph g, GraphView view, ThemePair thm,
	    IForceModel mod, IScaler scaler) {
	mGraph = g;
	mView = view;
	mTheme = thm;
	mForceModel = mod;
	mScaler = scaler;
    }

    public IForceModel getModel() {
	return mForceModel;
    }

    public void setModel(IForceModel mod) {
	mForceModel = mod;
    }

    public GraphView getView() {
	return mView;
    }

    public Graph getGraph() {
	return mGraph;
    }

    public ThemePair getTheme() {
	return mTheme;
    }

    public void setTheme(ThemePair thm) {
	mTheme = thm;
    }

    public IScaler getScaler() {
	return mScaler;
    }

    public void setScaler(IScaler scaler) {
	mScaler = scaler;
    }
}
