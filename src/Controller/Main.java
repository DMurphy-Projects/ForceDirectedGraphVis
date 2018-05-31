package Controller;

import java.awt.Color;
import java.io.File;

import Component.ColorComponent;
import Model.ColorScheme;

public class Main {

    public static void main(String[] args) {

		String path = System.getProperty("user.dir")
			+ System.getProperty("file.separator");
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		MainWindow window = new MainWindow(path);
		window.createGui();

    }
}