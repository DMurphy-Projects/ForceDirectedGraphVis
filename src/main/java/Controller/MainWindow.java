package Controller;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileView;

import Component.ColorComponent;
import Controller.UserController.HoverUpdater;
import Controller.UserController.KneelingCalc;
import Controller.UserController.MouseController;
import Model.ColorScheme;
import Model.GenericOption;
import Model.Graph;
import Model.LoadScreenModel;
import Model.ThemePair;
import Model.Force.AlternateSpringModel;
import Model.Force.ExpSpring;
import Model.Force.GenericRepel;
import Model.Force.IForceModel;
import Model.Force.MagneticSpringForceModel;
import Model.Force.SimpleSpring;
import Model.Force.Spring;
import Model.Force.SpringForceModel;
import Model.Force.StringSpring;
import View.LoadScreenView;
import View.Render.BlankBackground;
import View.Render.FPSCounter;
import View.Render.FrameTimeCounter;
import View.Render.GraphBackground;
import View.Render.IRenderDecorator;
import View.Render.Node.BaseNodeDecorator;
import View.Render.Node.BasicNodeHoverEffect;
import View.Render.Node.BasicNodeRender;
import View.Render.Node.BasicNodeSizeBasedRender;
import View.Render.Node.GearNodeRender;
import View.Render.Node.NodeForceRender;
import View.Render.Node.NodeHighlighter;
import View.Render.Node.NodeInfoRender;
import View.Render.Node.NodeSphericalRender;
import View.Render.Node.NodeTagRender;
import View.Render.Relationship.BaseRelationshipDecorator;
import View.Render.Relationship.BasicRelationshipHoverEffect;
import View.Render.Relationship.BasicRelationshipRender;
import View.Render.Relationship.RelationshipOpacityRender;
import org.neo4j.cypher.internal.compiler.v2_1.ast.Null;

public class MainWindow extends WindowAdapter implements ActionListener,
	ChangeListener {

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem menuItem;
    private JTextField textField;
    private JFrame mainFrame;

    private JDialog searchDialog;
    private JDialog tagDialog;
    private JDialog colorDialog;
    private JRadioButtonMenuItem toggleCam;
    private JRadioButtonMenuItem toggleDrag;

    private JTabbedPane tabs;

    private int graphNo = 0;

    private GraphController mController;

    private OptionSelector mDecSelector;
    private OptionSelector mModelSelector;

    private AutoGraphViewController mAutoCam;
    private MouseController mMouseControls;
    private KneelingCalc kCalc;

    private SliderListener mSliderListener;
    private ActionListener mTagListener;

    private JFileChooser mFileChooser;

    private String ROOT_PATH;
    private String DATABASE_PATH;

    private ColorComponent mColorPicker;

    public MainWindow(String path) {
	// file opener stuff
	ROOT_PATH = path;
	// path for random database folder not random directory
	DATABASE_PATH = ROOT_PATH + "Databases"
		+ System.getProperty("file.separator");
	File dbPath = new File(DATABASE_PATH);
	if (!dbPath.exists()) {
	    dbPath.mkdirs();
	}

	mFileChooser = new JFileChooser();

	mFileChooser.setCurrentDirectory(new File(DATABASE_PATH));

	mFileChooser.setAcceptAllFileFilterUsed(false);

	mFileChooser.setFileView(new FileView() {
	    @Override
	    public Boolean isTraversable(File f) {
		return f.getAbsolutePath().startsWith(DATABASE_PATH)
			&& !f.isFile();
	    }
	});

	mFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	// calculator used
	ForceVectorCalculator vCalc = new ForceVectorCalculator();
	CoordinateMathCalculator mCalc = new CoordinateMathCalculator();
	kCalc = new KneelingCalc();
	// handler to make hover effects possible/easier
	GraphViewEventHandler handler = new GraphViewEventHandler();

	// generic class to select some option and have a friendly name
	mDecSelector = new OptionSelector();

	BasicNodeHoverEffect hoverNode;
	BasicRelationshipHoverEffect hoverRel;

	ColorScheme colors;
	IRenderDecorator decorations;
	ThemePair theme;

	Limiter fpsLimiter = new Limiter(60);

	BaseNodeDecorator nodeHoverEffect;
	BaseNodeDecorator nodeHighlightEffect;
	BaseRelationshipDecorator relHoverEffect;
	BaseNodeDecorator nodeRender;
	// ----- Start Basic Theme
	colors = new ColorScheme();
	colors.addColor(ColorScheme.NODE_PRIMARY, Color.white);
	colors.addColor(ColorScheme.NODE_HIGHLIGHT, Color.yellow);
	colors.addColor(ColorScheme.LINK_PRIMARY, Color.black);
	colors.addColor(ColorScheme.TEXT_COLOR, Color.black);
	colors.addColor(ColorScheme.INFO_BACKGROUND, Color.white);
	colors.addColor(ColorScheme.BACKGROUND, Color.white);

	nodeHoverEffect = new NodeInfoRender(colors.getColor(ColorScheme.INFO_BACKGROUND),
		colors.getColor(ColorScheme.TEXT_COLOR), null);

	nodeHighlightEffect = new BasicNodeRender(
		colors.getColor(ColorScheme.NODE_HIGHLIGHT), null);

	nodeRender = new NodeHighlighter(nodeHighlightEffect,
		new BasicNodeRender(colors.getColor(ColorScheme.NODE_PRIMARY),
			null));

	decorations = hoverNode = new BasicNodeHoverEffect(nodeHoverEffect,
		new NodeTagRender(nodeRender, new BasicRelationshipRender(
			colors.getColor(ColorScheme.LINK_PRIMARY),
			new FPSCounter(fpsLimiter, new BlankBackground(colors
				.getColor(ColorScheme.BACKGROUND), null)))));
	theme = new ThemePair(decorations, colors);
	mDecSelector.addOption(new GenericOption<ThemePair>("Basic", theme));
	handler.addListener(hoverNode);
	// ----- End Basic Theme
	// ----- Start Size Based Basic Theme
	colors = new ColorScheme();
	colors.addColor(ColorScheme.NODE_PRIMARY, Color.white);
	colors.addColor(ColorScheme.LINK_PRIMARY, Color.black);
	colors.addColor(ColorScheme.NODE_HIGHLIGHT, Color.yellow);
	colors.addColor(ColorScheme.TEXT_COLOR, Color.black);
	colors.addColor(ColorScheme.INFO_BACKGROUND, Color.white);
	colors.addColor(ColorScheme.BACKGROUND, Color.white);

	nodeHighlightEffect = new BasicNodeSizeBasedRender(
		colors.getColor(ColorScheme.NODE_HIGHLIGHT), null);

	nodeRender = new NodeHighlighter(nodeHighlightEffect,
		new BasicNodeSizeBasedRender(
			colors.getColor(ColorScheme.NODE_PRIMARY), null));

	nodeHoverEffect = new NodeInfoRender(colors.getColor(ColorScheme.INFO_BACKGROUND),
		colors.getColor(ColorScheme.TEXT_COLOR), null);

	decorations = hoverNode = new BasicNodeHoverEffect(nodeHoverEffect,
		new NodeTagRender(nodeRender, new BasicRelationshipRender(
			colors.getColor(ColorScheme.LINK_PRIMARY),
			new BlankBackground(colors
				.getColor(ColorScheme.BACKGROUND), null))));
	theme = new ThemePair(decorations, colors);
	mDecSelector
		.addOption(new GenericOption<ThemePair>("Size Based", theme));
	handler.addListener(hoverNode);
	// ----- End Size Based Basic Theme
	// ----- Start Alchemy Theme
	colors = new ColorScheme();
	colors.addColor(ColorScheme.NODE_PRIMARY, Color.cyan);
	colors.addColor(ColorScheme.NODE_HIGHLIGHT, Color.yellow);
	colors.addColor(ColorScheme.LINK_PRIMARY, Color.black);
	colors.addColor(ColorScheme.NODE_HOVER, Color.white);
	colors.addColor(ColorScheme.LINK_HOVER, Color.black);
	colors.addColor(ColorScheme.TEXT_COLOR, Color.black);
	colors.addColor(ColorScheme.INFO_BACKGROUND, Color.white);
	colors.addColor(ColorScheme.GRID, Color.black);
	colors.addColor(ColorScheme.BACKGROUND, Color.white);

	nodeHoverEffect = new NodeInfoRender(colors.getColor(ColorScheme.INFO_BACKGROUND),
		colors.getColor(ColorScheme.TEXT_COLOR), new BasicNodeRender(
			colors.getColor(ColorScheme.NODE_HOVER), null));

	nodeHighlightEffect = new BasicNodeRender(
		colors.getColor(ColorScheme.NODE_HIGHLIGHT), null);

	relHoverEffect = new BasicRelationshipRender(
		colors.getColor(ColorScheme.LINK_HOVER), null);

	nodeRender = new NodeHighlighter(nodeHighlightEffect,
		new BasicNodeRender(colors.getColor(ColorScheme.NODE_PRIMARY),
			null));

	decorations = hoverNode = new BasicNodeHoverEffect(
		nodeHoverEffect,
		new NodeTagRender(
			nodeRender,
			hoverRel = new BasicRelationshipHoverEffect(
				relHoverEffect,
				new RelationshipOpacityRender(
					colors.getColor(ColorScheme.LINK_PRIMARY),
					new GraphBackground(
						colors.getColor(ColorScheme.GRID),
						new BlankBackground(
							colors.getColor(ColorScheme.BACKGROUND),
							null))))));
	handler.addListener(hoverNode);
	handler.addListener(hoverRel);
	theme = new ThemePair(decorations, colors);
	mDecSelector.addOption(new GenericOption<ThemePair>("Alchemy.js Basic",
		theme));
	// ----- End Alchemy Theme
	// ----- Start Alchemy Size Based
	colors = new ColorScheme();
	colors.addColor(ColorScheme.NODE_PRIMARY, Color.cyan);
	colors.addColor(ColorScheme.NODE_HIGHLIGHT, Color.yellow);
	colors.addColor(ColorScheme.LINK_PRIMARY, Color.black);
	colors.addColor(ColorScheme.NODE_HOVER, Color.white);
	colors.addColor(ColorScheme.LINK_HOVER, Color.black);
	colors.addColor(ColorScheme.TEXT_COLOR, Color.black);
	colors.addColor(ColorScheme.INFO_BACKGROUND, Color.white);
	colors.addColor(ColorScheme.GRID, Color.black);
	colors.addColor(ColorScheme.BACKGROUND, Color.white);

	nodeHoverEffect = new NodeInfoRender(colors.getColor(ColorScheme.INFO_BACKGROUND),
		colors.getColor(ColorScheme.TEXT_COLOR), new BasicNodeSizeBasedRender(
			colors.getColor(ColorScheme.NODE_HOVER), null));

	nodeHighlightEffect = new BasicNodeSizeBasedRender(
		colors.getColor(ColorScheme.NODE_HIGHLIGHT), null);

	relHoverEffect = new BasicRelationshipRender(
		colors.getColor(ColorScheme.LINK_HOVER), null);

	nodeRender = new NodeHighlighter(nodeHighlightEffect,
		new BasicNodeSizeBasedRender(
			colors.getColor(ColorScheme.NODE_PRIMARY), null));

	decorations = hoverNode = new BasicNodeHoverEffect(
		nodeHoverEffect,
		new NodeTagRender(
			nodeRender,
			hoverRel = new BasicRelationshipHoverEffect(
				relHoverEffect,
				new RelationshipOpacityRender(
					colors.getColor(ColorScheme.LINK_PRIMARY),
					new GraphBackground(
						colors.getColor(ColorScheme.GRID),
						new BlankBackground(
							colors.getColor(ColorScheme.BACKGROUND),
							null))))));
	handler.addListener(hoverNode);
	handler.addListener(hoverRel);
	theme = new ThemePair(decorations, colors);
	mDecSelector.addOption(new GenericOption<ThemePair>(
		"Alchemy.js Size Based", theme));
	// ----- End Alchemy Size Based
	// ----- Start Basic 3D
	colors = new ColorScheme();
	colors.addColor(ColorScheme.NODE_PRIMARY, Color.white);
	colors.addColor(ColorScheme.LINK_PRIMARY, Color.black);
	colors.addColor(ColorScheme.LINK_HOVER, Color.black);
	colors.addColor(ColorScheme.BACKGROUND, Color.white);

	relHoverEffect = new BasicRelationshipRender(
		colors.getColor(ColorScheme.LINK_HOVER), null);

	decorations = new NodeSphericalRender(
		colors.getColor(ColorScheme.NODE_PRIMARY),
		hoverRel = new BasicRelationshipHoverEffect(
			relHoverEffect,
			new RelationshipOpacityRender(
				colors.getColor(ColorScheme.LINK_PRIMARY),
				new BlankBackground(colors
					.getColor(ColorScheme.BACKGROUND), null))));
	handler.addListener(hoverRel);
	theme = new ThemePair(decorations, colors);
	mDecSelector.addOption(new GenericOption<ThemePair>("Basic 3D", theme));
	// ----- End Basic 3D
	// ----- Start Gears
	colors = new ColorScheme();
	colors.addColor(ColorScheme.NODE_PRIMARY, Color.gray);
	colors.addColor(ColorScheme.LINK_PRIMARY, Color.black);
	colors.addColor(ColorScheme.BACKGROUND, Color.white);

	decorations = new GearNodeRender(
		colors.getColor(ColorScheme.NODE_PRIMARY), true,
		new BasicRelationshipRender(
			colors.getColor(ColorScheme.LINK_PRIMARY),
			new BlankBackground(
				colors.getColor(ColorScheme.BACKGROUND), null)));
	theme = new ThemePair(decorations, colors);
	mDecSelector.addOption(new GenericOption<ThemePair>("Gears", theme));
	// ----- End Gears
	// ----- Start Debug Theme
	colors = new ColorScheme();
	colors.addColor(ColorScheme.NODE_PRIMARY, Color.white);
	colors.addColor(ColorScheme.LINK_PRIMARY, Color.black);
	colors.addColor(ColorScheme.BACKGROUND, Color.white);

	decorations = new BasicNodeRender(
		colors.getColor(ColorScheme.NODE_PRIMARY),
		new BasicRelationshipRender(
			colors.getColor(ColorScheme.LINK_PRIMARY),
			new NodeForceRender(
				new FPSCounter(
					fpsLimiter, new FrameTimeCounter(
					new BlankBackground(
						colors.getColor(ColorScheme.BACKGROUND),
						null))))));
	theme = new ThemePair(decorations, colors);
	mDecSelector.addOption(new GenericOption<ThemePair>("Force Visulised",
		theme));
	// ----- End Debug Theme
	// option selector for model options
	mModelSelector = new OptionSelector();

	mModelSelector.addOption(new GenericOption<IForceModel>(
		"Spring Force Model(Weak)", new SpringForceModel(vCalc, mCalc,
			kCalc, new Spring(30), new GenericRepel(8, 100))));
	mModelSelector.addOption(new GenericOption<IForceModel>(
		"Spring Force Model(Strong)", new SpringForceModel(vCalc,
			mCalc, kCalc, new ExpSpring(30), new GenericRepel(8))));
	//mModelSelector.addOption(new GenericOption<IForceModel>(
	//	"Spring Force Model(Close)", new SpringForceModel(vCalc, mCalc,
	//		kCalc, new ExpSpring(10), new GenericRepel(40))));
	mModelSelector.addOption(new GenericOption<IForceModel>(
		"Alt Spring Force Model", new AlternateSpringModel(vCalc,
			mCalc, kCalc, new Spring(20), new GenericRepel(8))));
	mModelSelector.addOption(new GenericOption<IForceModel>(
		"Alt Spring Force Model(Simple)", new AlternateSpringModel(
			vCalc, mCalc, kCalc, new SimpleSpring(30),
			new GenericRepel(8))));
	mModelSelector.addOption(new GenericOption<IForceModel>(
		"Alt Spring Force Model(String)", new AlternateSpringModel(
			vCalc, mCalc, kCalc, new StringSpring(0, 100),
			new GenericRepel(8))));
	mModelSelector.addOption(new GenericOption<IForceModel>(
		"Magnetic Spring Force Model(Weak)",
		new MagneticSpringForceModel(vCalc, mCalc, kCalc,
			new Spring(30), new GenericRepel(8, 100))));
	mModelSelector.addOption(new GenericOption<IForceModel>(
		"Magnetic Spring Force Model(Strong)",
		new MagneticSpringForceModel(vCalc, mCalc, kCalc,
			new ExpSpring(30), new GenericRepel(8))));

	mAutoCam = new AutoGraphViewController();
	mMouseControls = new MouseController();
	HoverUpdater hUpdater = new HoverUpdater();

	mController = new GraphController(DATABASE_PATH, mModelSelector,
		mDecSelector, handler, 0, fpsLimiter);
	// automatic camera control to track graph
	mController.addController(mAutoCam);
	// mouse controls
	mController.addController(mMouseControls);
	// graph scaler
	mController.addController(kCalc);
	// needs to update on hover on tick
	mController.addController(hUpdater);

	// for the sliders on every tab
	mSliderListener = new SliderListener();
	mSliderListener.set(true);
	mController.addController(mSliderListener);

	///SimulationFrameCounter sFC = new SimulationFrameCounter();
	///mController.addController(sFC);
	
	mTagListener = new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		NodeTagRender.toggle(arg0.getActionCommand());
	    }
	};
    }

    public void createGui() {
	Container c;

	mainFrame = new JFrame("MainWindow");
	mainFrame.addWindowListener(this);

	mColorPicker = new ColorComponent();
	colorDialog = new JDialog(mainFrame, Dialog.ModalityType.DOCUMENT_MODAL);
	colorDialog
		.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
	colorDialog.add(mColorPicker);
	colorDialog.pack();

	// DEBUG d = new DEBUG(mainFrame);

	c = mainFrame.getContentPane();

	tabs = new JTabbedPane();
	tabs.addChangeListener(this);
	c.add(tabs);

	// main window with menu bar
	menuBar = new JMenuBar();
	// start fill menu
	menu = new JMenu("File");
	menuBar.add(menu);
	menuItem = new JMenuItem("New Graph");
	menuItem.setActionCommand("newGraph");
	menuItem.addActionListener(this);
	menu.add(menuItem);

	menuItem = new JMenuItem("New Random Graph");
	menuItem.setActionCommand("newRandomGraph");
	menuItem.addActionListener(this);
	menu.add(menuItem);

	menuItem = new JMenuItem("Remove Current Graph");
	menuItem.setActionCommand("removeCurrentGraph");
	menuItem.addActionListener(this);
	menu.add(menuItem);
	// end file menu
	// start model
	menu = new JMenu("Model");
	menuBar.add(menu);

	int count = 0;
	for (GenericOption<IForceModel> m : mModelSelector.getOptions()) {
	    menuItem = new JMenuItem(m.getName());
	    menuItem.setSelected(false);
	    menuItem.setActionCommand("changemodel:" + count);
	    menuItem.addActionListener(this);
	    menu.add(menuItem);
	    count++;
	}
	// end model
	// start view menu
	menu = new JMenu("View");
	menuBar.add(menu);

	ButtonGroup group = new ButtonGroup();
	toggleCam = new JRadioButtonMenuItem("Auto Camera");
	toggleCam.setSelected(false);
	toggleCam.setActionCommand("toggleAutoCamera");
	toggleCam.addActionListener(this);
	group.add(toggleCam);
	menu.add(toggleCam);

	toggleDrag = new JRadioButtonMenuItem("Manual Camera");
	toggleDrag.setSelected(true);
	toggleDrag.setActionCommand("toggleDrag");
	toggleDrag.addActionListener(this);
	group.add(toggleDrag);
	menu.add(toggleDrag);

	menuItem = new JMenuItem("Center Graph");
	menuItem.setActionCommand("centerGraph");
	menuItem.addActionListener(this);
	menu.add(menuItem);

	// start sub menu
	JMenu submenu = new JMenu("Theme");

	count = 0;
	for (GenericOption<IRenderDecorator> o : mDecSelector.getOptions()) {
	    menuItem = new JMenuItem(o.getName());
	    menuItem.setActionCommand("changetheme:" + count);
	    menuItem.addActionListener(this);
	    submenu.add(menuItem);
	    count++;
	}

	menu.add(submenu);
	// end sub menu

	// end view menu
	// start tools menu
	menu = new JMenu("Tools");
	menuBar.add(menu);

	group = new ButtonGroup();
	toggleCam = new JRadioButtonMenuItem("Auto Scaling");
	toggleCam.setSelected(false);
	toggleCam.setActionCommand("toggleScalingAuto");
	toggleCam.addActionListener(this);
	group.add(toggleCam);
	menu.add(toggleCam);

	toggleDrag = new JRadioButtonMenuItem("Manual Scaling");
	toggleDrag.setSelected(true);
	toggleDrag.setActionCommand("toggleScalingUser");
	toggleDrag.addActionListener(this);
	group.add(toggleDrag);
	menu.add(toggleDrag);

	menuItem = new JMenuItem("Change Colors");
	menuItem.setActionCommand("colors");
	menuItem.addActionListener(this);
	menu.add(menuItem);

	// search input
	menuItem = new JMenuItem("Search");
	menuItem.setActionCommand("searchMenu");
	menuItem.addActionListener(this);
	menu.add(menuItem);

	searchDialog = new JDialog(mainFrame,
		Dialog.ModalityType.DOCUMENT_MODAL);
	c = searchDialog.getContentPane();
	c.setLayout(new BoxLayout(c, BoxLayout.X_AXIS));
	textField = new JTextField();
	textField.setActionCommand("searchBtn");
	textField.addActionListener(this);
	c.add(textField);
	Button searchBtn = new Button(">");
	searchBtn.setActionCommand("searchBtn");
	searchBtn.addActionListener(this);
	c.add(searchBtn);
	searchDialog
		.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
	searchDialog.pack();

	// tag menu
	menuItem = new JMenuItem("Tags");
	menuItem.setActionCommand("tagMenu");
	menuItem.addActionListener(this);
	menu.add(menuItem);

	tagDialog = new JDialog(mainFrame, Dialog.ModalityType.DOCUMENT_MODAL);
	c = tagDialog.getContentPane();

	tagDialog
		.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
	tagDialog.pack();
	// end tools menu
	mainFrame.setJMenuBar(menuBar);
	mainFrame.pack();
	mainFrame.setVisible(true);
    }

    private void addTabbedGraph(Graph graphToAdd) {
	JPanel tab = new JPanel(new BorderLayout());

	JPanel frame = new JPanel();
	frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));

	frame.add(mController.getView(mController.currentIndex()));
	JSlider simulationTicks = new JSlider(JSlider.HORIZONTAL, 0, 150, 40);
	mSliderListener.setValue(40);

	simulationTicks.addChangeListener(mSliderListener);
	frame.add(simulationTicks);

	tab.add(frame);

	tab.setName("Graph " + ++graphNo);
	tabs.add(tab);
	tabs.setSelectedIndex(mController.currentIndex());
	NodeTagRender.refreshProperties();
    }

    @Override
    public void stateChanged(ChangeEvent arg0) {
	mController.changeGraph(tabs.getSelectedIndex());
	// mMouseControls.changeView(mController.getView(mController.currentIndex()));
	NodeTagRender.refreshProperties();
    }

    private void refreshPropertiesDialog() {
	Container c = tagDialog.getContentPane();
	c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
	c.removeAll();
	Container line;
	JCheckBox box;
	for (String s : NodeTagRender.getProperties()) {
	    line = new Container();
	    line.setLayout(new BoxLayout(line, BoxLayout.X_AXIS));
	    line.add(new Label(s));
	    box = new JCheckBox();
	    box.addActionListener(mTagListener);
	    box.setActionCommand(s);
	    box.setSelected(true);
	    line.add(box);
	    c.add(line);
	}
    }

    public void windowClosing(WindowEvent e) {
	mController.stopTicks();
	System.exit(0);// window close doesnt stop process?
    }

    private void removeGraph(int i) {
	if (i > -1) {
	    mController.removeGraph(i);
	    tabs.remove(i);

	    tabs.setSelectedIndex(mController.currentIndex());
	}
    }

    private void newGraph() {
	newGraph(null);
    }

    private void newGraph(final String path) {
	Point loc = mainFrame.getLocation();
	loc = new Point(loc.x + (mainFrame.getWidth() / 2), loc.y
		+ (mainFrame.getHeight() / 2));
	final LoadScreenController con = new LoadScreenController(
		new LoadScreenView(new LoadScreenModel(3)), loc);
	(new Thread() {
	    public void run() {
		Graph g;
		if (path == null) {
		    g = mController.newRandomGraph(500);
		} else {
		    g = mController.newGraph(path);
		}
		addTabbedGraph(g);
		mainFrame.pack();
		con.hide();
	    }
	}).start();
    }

    public void actionPerformed(ActionEvent e) {
	String action = e.getActionCommand();
	int i;
	// System.out.println(e.getActionCommand());
	if (action.startsWith("changetheme")) {
	    i = action.indexOf(":");
	    int themeNumber = Integer.parseInt(action.substring(i + 1));
	    mDecSelector.select(themeNumber);
	    mController.updateDecoration();
	} else if (action.startsWith("changemodel")) {
	    i = action.indexOf(":");
	    int modelNumber = Integer.parseInt(action.substring(i + 1));
	    mModelSelector.select(modelNumber);
	    mController.updateModel();
	}
	switch (action) {
	case "newGraph":
	    // fileDialog.setVisible(true);
	    int returnVal = mFileChooser.showOpenDialog(mFileChooser);

	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		String file = mFileChooser.getSelectedFile().toString();
		if (file != ROOT_PATH) {
		    newGraph(mFileChooser.getSelectedFile().toString());
		}
	    }
	    break;
	case "searchMenu":
	    textField.setText(NodeHighlighter.getSearchString());
	    searchDialog.setVisible(true);
	    break;
	case "searchBtn":
	    searchDialog.setVisible(false);
	    NodeHighlighter.setSearchString(textField.getText());
	    break;
	case "newRandomGraph":
	    newGraph();
	    break;
	case "removeCurrentGraph":
	    removeGraph(mController.currentIndex());
	    break;
	case "toggleAutoCamera":
	    mAutoCam.setState(true);
	    mMouseControls.setState(false);
	    break;
	case "toggleDrag":
	    mAutoCam.setState(false);
	    mMouseControls.setState(true);
	    break;
	case "centerGraph":
	    i = mController.currentIndex();
	    if (i != -1) {
		mAutoCam.forceCenter(mController.getView(i));
	    }
	    break;
	case "toggleScalingAuto":
	    kCalc.set(true);
	    mSliderListener.set(false);
	    break;
	case "toggleScalingUser":
	    kCalc.set(false);
	    mSliderListener.set(true);
	    break;
	case "colors":
	    i = mController.currentIndex();
	    if (i != -1) {
		mColorPicker.setColorScheme(mController.getPackage(i)
			.getTheme().getColors());
		colorDialog.setVisible(true);
	    }
	    break;
	case "tagMenu":
	    refreshPropertiesDialog();
	    tagDialog.pack();
	    tagDialog.setVisible(true);
	    break;
	}
    }
}
