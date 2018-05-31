package Model.Force;

import java.awt.Point;

import Model.Graph;
import Model.IScaler;


public class SimpleForceModel implements IForceModel{
	
	public void manipulate(Graph graph, IScaler rate)
	{
		for (ForceNode node:graph.Nodes())
		{
			Point n = node.getPoint();
			
			//force calculations
			n.x += 1;
			n.y += 1;
			
			node.setX(n.x);
			node.setY(n.y);
		}
	}
}
