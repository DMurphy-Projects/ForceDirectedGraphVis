package Model.Force;

public class ForceVector {

    private float mXComp;
    private float mYComp;

    private float mDirection;
    
    private float mForceAmount;

    public ForceVector(float direction, float force) {
	mDirection = direction;
	mXComp = (float) Math.cos(direction);
	mYComp = (float) Math.sin(direction);
	mForceAmount = force;
	if (mForceAmount < 0)
	{
	    mForceAmount = 0;
	}
	else if(mForceAmount > 1000)
	{
	    mForceAmount = 1000;
	}
    }

    public ForceVector(double direction, double force) {
	mDirection = (float) direction;
	mXComp = (float) Math.cos(direction);
	mYComp = (float) Math.sin(direction);
	mForceAmount = (float) force;
    }

    public float getDirection()
    {
	return mDirection;
    }
    public float getXComp()
    {
	return mXComp;
    }
    public float getYComp()
    {
	return mYComp;
    }

    public float getForceValue() {
	return mForceAmount;
    }
}
