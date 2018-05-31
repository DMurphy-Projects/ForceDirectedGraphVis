package Controller;

public class Limiter {

    private int mMaxAxtions = 0;
    private int mCurActions = 0;
    private long time;

    private int mPrevActionPerSec = 0;

    private boolean mWait = false;

    public Limiter(int actions) {
	mMaxAxtions = actions;
	time = System.currentTimeMillis();
	new Thread() {
	    @Override
	    public void run() {
		while (true)
		{
		    mPrevActionPerSec = mCurActions;
		    mCurActions = 0;
		    try {
			Thread.sleep(1000);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    }
	}.start();
    }

    public void doAction()
    {
	mCurActions++;
    }
    
    public boolean canAction() {
	long t = System.currentTimeMillis();
	// see if its been a second yet
	if (time + (1000 / mMaxAxtions) < t) {
	    //mCurActions++;
	    time = t;
	    return true;
	}
	return false;
	// System.out.println(mCurActions);
	// return (mCurActions < mMaxAxtions);
    }

    public int getActions() {
	return mPrevActionPerSec;
    }

}
