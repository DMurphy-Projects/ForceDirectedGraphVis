package Model.Force;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import Controller.ForceVectorCalculator;


public class ForceNode {

    private Node mNodeInfo;
    private Relationship[] mRelationshipInfo;

    private float mX;

    private float mY;

    private ArrayList<ForceVector> mActingForces;

    private int mMass = 2;

    private float mFriction = 0.15f;

    private ForceVector mLastDirection;

    public ForceNode(int x, int y, Node n, Relationship[] rel) {
	mX = x;
	mY = y;
	mNodeInfo = n;
	mRelationshipInfo = rel;
	mActingForces = new ArrayList<ForceVector>();
	mLastDirection = new ForceVector(0, 0);
    }

    // change class to work with this
    public Point2D.Float getPosition() {
	return new Point2D.Float(mX, mY);
    }

    public Point getPoint() {
	return new Point((int) mX, (int) mY);
    }

    public void setX(float x) {
	mX = x;
    }

    public void setY(float y) {
	mY = y;
    }

    public Node NodeInfo() {
	return mNodeInfo;
    }

    public Relationship[] RelationshipInfo() {
	return mRelationshipInfo;
    }

    public void setMass(int mass) {
	mMass = mass;
    }

    public int Mass() {
	return mMass;
    }

    // Synchronised fixes null pointer exception that occurs - needs explain
    public synchronized void addForceVector(ForceVector fVec) {
	// cull some meaningless force vectors
	if (fVec.getForceValue() > 1) {
	    mActingForces.add(fVec);
	}
    }

    public ForceVector getFinalVector() {
	return mLastDirection;
    }

    // change to interface if other vector math calculations are possible
    public ForceVector getFinalVector(ForceVectorCalculator calc) {
	addForceVector(mLastDirection);
	ForceVector fVec = calc.getResultantVector(mActingForces
		.toArray(new ForceVector[mActingForces.size()]));
	//float dir = fVec.getDirection();
	//float force = fVec.getForceValue() * (1f - mFriction);

	mActingForces.clear();
	float n = mMass * 9.8f * mFriction;
	fVec = new ForceVector(fVec.getDirection(), fVec.getForceValue()-n);
	// Mathematically similar to adding initial velocity
	//mActingForces.add(fVec); 
	// for easy render stuffs
	mLastDirection = fVec;
	return fVec;
    }
    
    @Override
    public String toString() {
        return mX + " "+mY;
    }

}
