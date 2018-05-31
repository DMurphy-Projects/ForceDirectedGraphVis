package Controller;

import javax.swing.JFrame;

public class DEBUG {

    static JFrame mF;
    public DEBUG(JFrame f)
    {
	mF = f;
    }
    
    public static void write(String s)
    {
	mF.setTitle(mF.getTitle()+s);
    }
}
