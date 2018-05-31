package Model.Force;

import java.awt.Point;

public class StringSpring extends Spring{

    private int maxLength;
    
    public StringSpring(int minLen, int maxLen) {
	super(minLen);
	maxLength = maxLen*maxLen;
    }

    @Override
    public ForceVector getForceOf(Point n1, Point n2) {
	double distanceBetween = Math.pow(n1.x - n2.x, 2)
		+ Math.pow(n1.y - n2.y, 2);
	
	float angle = getDirection(n1, n2, distanceBetween);
	
	double differenceFactor = getDifferenceFactor(distanceBetween, maxLength);
	double force = mForce * (differenceFactor * differenceFactor);
	
	if (distanceBetween < mSpringLength)
	{
	    return new ForceVector(angle, force);
	}
	else if (distanceBetween > maxLength)
	{
	    return new ForceVector(angle - Math.PI, force);
	}
	else
	{
	    return new ForceVector(0, 0);
	}
    }
    
}
