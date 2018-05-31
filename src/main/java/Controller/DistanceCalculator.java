package Controller;

import java.awt.Point;
import java.awt.geom.Point2D;

public class DistanceCalculator {

    public int distance(Point p1, Point p2) {
	return (int) Math.sqrt(distanceNoRoot(p1, p2));
    }

    public float distanceNoRoot(Point2D.Float p1, Point2D.Float p2) {
	float xDif = p1.x - p2.x;
	float yDif = p1.y - p2.y;
	float dist = (xDif*xDif) + (yDif*yDif);
	return dist;
    }
    
    public float distanceNoRoot(Point p1, Point p2) {
	int xDif = p1.x - p2.x;
	int yDif = p1.y - p2.y;
	int dist = (xDif*xDif) + (yDif*yDif);
	return dist;
    }

    public float distanceToLineSegment(Point p, Point v, Point w) {
	float l2 = distanceNoRoot(v, w);
	if (l2 == 0)
	    return distanceNoRoot(p, v);
	float t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2;
	if (t < 0)
	    return distanceNoRoot(p, v);
	if (t > 1)
	    return distanceNoRoot(p, w);
	return distanceNoRoot(p, new Point((int) (v.x + t * (w.x - v.x)),
		(int) (v.y + t * (w.y - v.y))));
    }
}
