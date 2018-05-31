package Model.Force;

import java.util.ArrayList;

public class ForceRelationship {

    ArrayList<ForceNode> mRelation;
    
    public ForceRelationship()
    {
	mRelation = new ArrayList<ForceNode>();
    }
    
    public void addForceNode(ForceNode f)
    {
	mRelation.add(f);
    }
    
    public ArrayList<ForceNode> getRelationship()
    {
	return mRelation;
    }
}
