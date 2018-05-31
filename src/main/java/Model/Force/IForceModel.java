package Model.Force;

import Model.Graph;
import Model.IScaler;


public interface IForceModel {
	public void manipulate(Graph graph, IScaler rate);
}
