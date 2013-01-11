
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class mainForm {

	protected static Shell shell;

	//declares the new thread containing the listener.
	static EiscpListener listener = new EiscpListener();
	static Thread threadListener = new Thread(listener);

	//composites
	static Composite compositeStack;
	static NetPlayComposite netPlay;
	static StackLayout sl_contentPane;
	static NetListComposite netList;
	static EqualizerComposite equalizer;
	static SoundSettingsComposite soundSettings;
	static InputSelectorComposite inSelect;

	//buttons
	private static Button btnPower;
	private static Button btnMute;
	private static Button btnDisplay;
	private static Button btnRight;
	private static Button btnOk;
	private static Button btnReturn;
	private static Button btnInputSelect;
	private static Button btnRIHD;
	private static Button btnEqualizer;
	private static Button hiddenButton;
	
	//TODO temporary button!
	private static Button btnMOT;

	//labels
	private static Label lblInput;
	private static Label lblVolume;
	static Group grpButtonGroup;

	static XStream xstream = new XStream(new DomDriver());
	static Settings settings = (Settings)xstream.fromXML(FileReadWrite.readSettings());
	
	static int previousTopComposite;
	static int currentTopComposite;
	

	protected static boolean closeApp = true;
	//change this to create log messages in file log.0
	private static boolean log = false;
	private static Logger logger;
	private static LogManager logManager;
	private static Handler fileHandler;
	static LoggingOutputStream los;
	
	
	static boolean indevelopment = false;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {

		if(log)
			startlog();

		try {
			PreStart preStart = new PreStart(settings);		
			preStart.open();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (!closeApp){

			try {

				mainForm window = new mainForm();
				window.open();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static void startlog() {
		// initialize logging to go to rolling log file
		//LogManager logManager = LogManager.getLogManager();
		 logManager = LogManager.getLogManager();
		logManager.reset();

		// log file max size 10K, 3 rolling files, append-on-open
		//Handler fileHandler;
		try {
			fileHandler = new FileHandler("log", 10000, 3, true);
			fileHandler.setFormatter(new SimpleFormatter());
			Logger.getLogger("").addHandler(fileHandler);
		} catch (SecurityException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		// preserve old stdout/stderr streams in case they might be useful      
		PrintStream stdout = System.out;                                        
		PrintStream stderr = System.err;                                        

		// now rebind stdout/stderr to logger                                   
//		Logger logger;                                                          
//		LoggingOutputStream los;                                                

		logger = Logger.getLogger("stdout");                                    
		los = new LoggingOutputStream(logger, StdOutErrLevel.STDOUT);           
		System.setOut(new PrintStream(los, true));                              

		logger = Logger.getLogger("stderr");                                    
		los= new LoggingOutputStream(logger, StdOutErrLevel.STDERR);            
		System.setErr(new PrintStream(los, true));
		// show stdout going to logger
		logger.info("TotalKrills Onkyo Remote Started");

		// now log a message using a normal logger
		//logger = Logger.getLogger("test");
		// logger.info("This is a test log message");

		// now show stderr stack trace going to logger
		//        try {
		//            throw new RuntimeException("Test");
		//        } catch (Exception e) {
		//            e.printStackTrace();
		//        }

		// and output on the original stdout
		stdout.println("");

	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();		
		createContents();
		shell.open();
		shell.layout();
		shell.getDisplay().addFilter(SWT.KeyDown, new Listener()
		{
			@Override
			public void handleEvent(Event e)
			{
				System.out.println("Filter-key: " + e.keyCode);
				Keyboard.keyResponse(e.keyCode,btnRIHD.getSelection());
			}


		});



		//		//TODO moved to Prestart connect button
		//		System.out.println("Trying to connect to IP and port: " +settings.receiverIP + ", " +settings.receiverPort);
		//		if(eiscp.connectSocket(settings.receiverIP,settings.receiverPort)){
		//		// starts the Listener in a new thread
		//		System.out.println("succeded, starting listening services");
		//		threadListener.start();
		//		Status.getInitialStatus();
		//		}
		//		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}





	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected static void createContents() {
		shell = new Shell(Display.getDefault(), SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.TITLE);		
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		shell.setImage(SWTResourceManager.getImage(mainForm.class, "/resources/note.png"));
		shell.setModified(true);
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		shell.setBackgroundImage(SWTResourceManager.getImage(mainForm.class, "/resources/test.jpg"));		
		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e){
				//runs on closing!
				
				EiscpListener.run=false;
				logger.info("Application Closed");
			}
		});
		shell.setSize(450, 550);
		shell.setText("TotalKrills TX-NR609 Remote");
		shell.setLayout(null);
		
		createCompositesInStack();
		
		compositeStack = new Composite(shell, SWT.NONE);
		compositeStack.setBounds(100, 10, 250, 400);
		compositeStack.setBackgroundMode(SWT.INHERIT_FORCE);
		sl_contentPane = new StackLayout();
		compositeStack.setLayout(sl_contentPane);

		netPlay = new NetPlayComposite(compositeStack,SWT.NONE);
		netList = new NetListComposite(compositeStack,SWT.NONE);
		inSelect = new InputSelectorComposite(compositeStack,SWT.NONE);
		equalizer = new EqualizerComposite(compositeStack,SWT.NONE);
		soundSettings = new SoundSettingsComposite(compositeStack,SWT.NONE);

		sl_contentPane.topControl = netPlay;
		
		//remove enter/space button pressing gui components!
		KeyboardFocusManager kfm =   
				KeyboardFocusManager.getCurrentKeyboardFocusManager();  
		kfm.addKeyEventDispatcher( new KeyEventDispatcher() {  
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {  
				if(e.getKeyCode() == KeyEvent.VK_ENTER||e.getKeyCode() == KeyEvent.VK_SPACE) {  
					e.consume();  
					return true;  
				}  
				return false;
			}  
		});  



		Button btnDown = new Button(shell, SWT.ARROW | SWT.DOWN);
		btnDown.setBounds(40, 160, 25, 25);
		btnDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {if(btnRIHD.getSelection()){
				Eiscp.sendCommand("CDVDOWN");
			}
			else Eiscp.sendCommand("NTCDOWN");
			}
		});
		btnDown.setText("Down");

		btnMute = new Button(shell, SWT.NONE);
		btnMute.setBounds(10, 337, 80, 29);
		btnMute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){							
				if (Status.muted){
					Eiscp.sendCommand("AMT00");
				}
				else{
					Eiscp.sendCommand("AMT01");
				}								
			}
		});
		btnMute.setText("Mute/Unmute");
		if(Status.muted){
			btnMute.setText("UnMute");
		}
		else{
			btnMute.setText("Mute");
		}		

		btnPower = new Button(shell, SWT.NONE);
		btnPower.setBounds(10, 10, 80, 25);
		btnPower.setText("power");
		btnPower.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {if(btnRIHD.getSelection()){
				Eiscp.sendCommand("CDVPOWER");
			}
			else {				
				if(Status.power){
					Eiscp.sendCommand("PWR00");
				}	
				else{
					Eiscp.sendCommand("PWR01");
				}
			}
			}
		});
		if(Status.power){
			btnPower.setText("Power Off");
		}
		else{
			btnPower.setText("Power On");
		}

		Button btnVolumeup = new Button(shell, SWT.ARROW);
		btnVolumeup.setBounds(10, 243, 80, 29);
		btnVolumeup.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Eiscp.sendCommand("MVLUP");
			}
		});
		btnVolumeup.setText("VolumeUp");

		Button btnVolumedown = new Button(shell, SWT.ARROW | SWT.DOWN);
		btnVolumedown.setBounds(10, 301, 80, 29);
		btnVolumedown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Eiscp.sendCommand("MVLDOWN");
			}
		});
		btnVolumedown.setText("VolumeDown");

		lblVolume = new Label(shell, SWT.NONE);
		lblVolume.setBounds(10, 278, 80, 17);
		lblVolume.setText("Volume");
		// setting netPlay at the top of the stack.

		

		btnDisplay = new Button(shell, SWT.NONE);
		btnDisplay.setBounds(360, 10, 80, 25);
		btnDisplay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Eiscp.sendCommand("NTCDISPLAY");
				setTopComposite(0);
			}
		});
		btnDisplay.setText("Display");

		Button btnUp = new Button(shell, SWT.ARROW);
		btnUp.setBounds(40, 100, 25, 25);
		btnUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnRIHD.getSelection()){
					Eiscp.sendCommand("CDVUP");
				}
				else {
					Eiscp.sendCommand("NTCUP");}
			}
		});
		btnUp.setText("Up");

		Button btnLeft = new Button(shell, SWT.ARROW | SWT.LEFT);
		btnLeft.setBounds(10, 129, 25, 25);

		btnLeft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnRIHD.getSelection()){
					Eiscp.sendCommand("CDVLEFT");
				}
				else {Eiscp.sendCommand("NTCLEFT");}

			}
		});
		btnLeft.setText("Left");

		btnRight = new Button(shell, SWT.ARROW | SWT.RIGHT);
		btnRight.setBounds(70, 130, 25, 25);		
		btnRight.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {if(btnRIHD.getSelection()){
				Eiscp.sendCommand("CDVRIGHT");
			}
			else {Eiscp.sendCommand("NTCRIGHT");}
			}
		});
		btnRight.setText("right");

		btnOk = new Button(shell, SWT.NONE);
		btnOk.setBounds(40, 130, 25, 25);
		btnOk.setFont(SWTResourceManager.getFont("Sans", 8, SWT.NORMAL));
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnRIHD.getSelection()){
					Eiscp.sendCommand("CDVENTER");
				}
				else {Eiscp.sendCommand("NTCSELECT");}
			}
		});
		btnOk.setText("Ok");

		btnReturn = new Button(shell, SWT.NONE);
		btnReturn.setBounds(71, 160, 25, 25);
		btnReturn.setImage(SWTResourceManager.getImage(mainForm.class, "/resources/arrow-back-1.png"));
		btnReturn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnRIHD.getSelection()){
					Eiscp.sendCommand("CDVRETURN");
				}
				else {Eiscp.sendCommand("NTCRETURN");}
			}
		});

		btnRIHD = new Button(shell, SWT.CHECK);		
		btnRIHD.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateGUI();
			}
		});
		btnRIHD.setBounds(329, 486, 111, 22);
		btnRIHD.setText("RIHD");

		grpButtonGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
		grpButtonGroup.setBackgroundMode(SWT.INHERIT_FORCE);
		grpButtonGroup.setBounds(10, 420, 310, 90);

		btnInputSelect = new Button(grpButtonGroup, SWT.TOGGLE);
		btnInputSelect.setBounds(10, 50, 80, 30);
		btnInputSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setTopComposite(2);
				updateGUI();
			}
		});
		btnInputSelect.setText("InputSelect");

		btnEqualizer = new Button(grpButtonGroup, SWT.TOGGLE);
		btnEqualizer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				equalizer.checkvalues();
				setTopComposite(3);
				updateGUI();

			}
		});
		btnEqualizer.setBounds(10, 10, 80, 30);
		btnEqualizer.setText("Equalizer");

		lblInput = new Label(shell, SWT.SHADOW_NONE);
		lblInput.setAlignment(SWT.CENTER);
		lblInput.setBounds(10, 41, 80, 30);
		lblInput.setText("INPUT");

		// TODO temporary button and label, fix later
		btnMOT = new Button(shell, SWT.NONE);
		btnMOT.setBounds(360, 385, 80, 25);
		btnMOT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				Eiscp.sendCommand("MOTUP");

			}
		});

		Label lblMot = new Label(shell, SWT.NONE);
		lblMot.setBounds(356, 364, 84, 15);
		lblMot.setText("Music optimizer");
		
		Button btnBugtest = new Button(shell, SWT.NONE);
		btnBugtest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateGUI();
			}
		});
		btnBugtest.setBounds(365, 426, 75, 25);
		btnBugtest.setText("BugTest");
		btnBugtest.setVisible(indevelopment);


		setTopComposite(0);

	}

	private static void createCompositesInStack() {

		
	}

	public static void updateGUI()
	{
		//TODO temporary fix
		if(Status.musicOptimizer)
			btnMOT.setText("enabled");
		else
			btnMOT.setText("disabled");
		
		//		if(!btnInputSelect.getSelection()){
		//			setTopComposite(2);
		//		}
		//		
		//		else if(btnEqualizer.getSelection()){
		//			
		//			equalizer.update();
		//			setTopComposite(3);
		//		}

		if(Status.power)
		{
			btnPower.setText("Power Off");
		}
		else
		{
			btnPower.setText("Power On");
		}
		
		
		
		if(btnRIHD.getSelection())
			btnPower.setText("Power");
		if(!sl_contentPane.topControl.equals(inSelect))
			btnInputSelect.setSelection(false);
		
		netPlay.update();		
		
		equalizer.update();
		
		if(Status.muted)
		{
			btnMute.setText("UnMute");
		}
		else
		{
			btnMute.setText("Mute");
		}
		
		//textFields
		lblVolume.setText("Volume: " + Status.masterVolume);
		//shell.setFocus();
		shell.getShell().setDefaultButton(hiddenButton);
		lblInput.setText(getlblInput(Status.inputSelected));
		System.out.println("SLI NUMBER = "+Status.inputSelected);

	}
	/** 	netPlay = 0
	 * 	netList = 1
	 *	inSelect = 2
	 *   equalizer = 3
	 *   soundSettings = 4
	 */
	public static void setTopComposite(int set) {
		switch(set){
		case 0: sl_contentPane.topControl=netPlay;compositeStack.layout();break;
		case 1: sl_contentPane.topControl=netList;compositeStack.layout();break;
		case 2: sl_contentPane.topControl=inSelect;compositeStack.layout();break;
		case 3: sl_contentPane.topControl=equalizer;compositeStack.layout();break;
		case 4: sl_contentPane.topControl=soundSettings;compositeStack.layout();break;
		}
		compositeStack.layout();
		

	}
	public static void setOnlyActiveButton(Group buttonGroup,Button button){
		Control[] children =buttonGroup.getChildren();
		for(Control c : children){
			((Button)c).setSelection(false);
			button.setSelection(true);

		}			
	}
	public static void setNoActiveButton(Group buttonGroup){
		Control[] children = buttonGroup.getChildren();
		for(Control c : children){
			((Button)c).setSelection(false);
		}

	}
	public static String getlblInput(int input){
		String text ="";
		try{
			switch(input){
			case 0: text = "VCR/DVD";break;
			case 1: text = "CBL/SAT";break;
			case 2: text = "GAME";break;
			case 3: text = "AUX1";break;
			case 4: text = "AUX2";break;
			case 5: text = "PC";break;
			case 6: text = "VIDEO7";break;
			case 7: text = "Hidden1";break;
			case 8: text = "Hidden2";break;
			case 16: text = "BD/DVD";break;
			case 32: text = "TV/TAPE";break;
			case 33: text = "TAPE2";break;
			case 34: text = "PHONO";break;
			case 35: text = "TV/CD";break;
			case 36: text = "FM";break;
			case 37: text = "AM";break;
			case 38: text = "TUNER";break;
			case 39: text = "DLNA";break;
			case 40: text = "NET-RADIO";break;
			case 41: text = "FRONT-USB";break;
			case 42: text = "REAR-USB";break;
			case 43: text = "NET";break;
			case 44: text = "USB";break;
			case 64: text = "UPORT";break;
			case 48: text = "MULTICH";break;
			case 49: text = "XM1";break;
			case 50: text = "SIRIUS1";break;
			}
		}
		catch(Exception e){
			text = "Error";
			e.printStackTrace();
		}

		return text;			
	}
}
