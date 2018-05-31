package Controller;

import java.util.ArrayList;

import Model.GenericOption;

@SuppressWarnings("rawtypes")
public class OptionSelector {
    private ArrayList<GenericOption> mOptions;

    private int mSelected = 0;

    public OptionSelector() {
	mOptions = new ArrayList<GenericOption>();
    }

    public void addOption(GenericOption model) {
	mOptions.add(model);
    }

    public void select(int i) {
	if (i < mOptions.size()) {
	    mSelected = i;
	}
    }

    public GenericOption getOption() {
	return mOptions.get(mSelected);
    }

    public ArrayList<GenericOption> getOptions() {
	return mOptions;
    }
}
