package Controller.UserController;

import Model.GraphPackage;

public class SimulationFrameCounter implements IUserController {
    private long mTime;
    private boolean first = true;
    private long start;
    private long elapsed = 60 * 1000 * 2;// how long to track for

    @Override
    public void update(GraphPackage gPack) {
	long time = System.currentTimeMillis();
	if (first) {
	    first = false;
	    start = time;
	} else {
	    if (time > start + elapsed) {
		System.out.println("Time Elapsed");
	    }
	    System.out.println(time - mTime);
	}
	mTime = time;
    }

}
