package Controller.UserController;

import Model.GraphPackage;
import View.GraphView;

public class HoverUpdater implements IUserController{

    @Override
    public void update(GraphPackage gPack) {
	GraphView view = gPack.getView();
	view.notifyNodeHover();
	view.notifyRelationHover();
    }
}
