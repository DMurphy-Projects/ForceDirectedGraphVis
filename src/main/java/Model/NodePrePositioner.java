package Model;

import java.awt.Point;

public class NodePrePositioner {

    int x = 0;
    int y = 0;
    
    int dist = 20;
    
    float angle = 0;
    
    public Point getPosition()
    {
	x = (int) (Math.cos(angle)*dist);
	y = (int) (Math.sin(angle)*dist);
	angle += Math.toRadians(5);
	if (angle > Math.toRadians(360))
	{
	    angle = 0;
	}
	//dist += 1;
	return new Point(x, y);
    }
    
}
