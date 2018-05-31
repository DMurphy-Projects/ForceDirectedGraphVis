package Model;

import java.util.ArrayList;

public class LoadScreenModel {

    private static ArrayList<String> mMessages;

    private static int mLimit = 1;

    public LoadScreenModel(int limit) {
	mLimit = limit;
	mMessages = new ArrayList<String>();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getStrings() {
	return (ArrayList<String>) mMessages.clone();
    }

    public static void addMessage(String msg) {
	if (mMessages.size() >= mLimit) {
	    mMessages.remove(0);
	}
	mMessages.add(msg);
    }

}
