import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;


public class PreStart {

	protected Shell shell;
	private Text txtIpAdress;
	private Text txtPort;
	private Settings settings;
	Label lblConnectfail;
	private Scale scale;
	private Button btnLongPlaylist;
	private Button btnBugTest;
	
	/**
	 * Settings
	 */
	public PreStart(Settings settings) {
		this.settings = settings;
	}
		/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
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
	protected void createContents() {
		
		shell = new Shell();
		shell.setSize(300, 400);
		shell.setText("Configurations");
		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e){
				//runs on closing!
				
				}
		});
		Label lblIpAdress = new Label(shell, SWT.NONE);
		lblIpAdress.setBounds(10, 10, 55, 15);
		lblIpAdress.setText("Ip adress");
		
		txtIpAdress = new Text(shell, SWT.BORDER);
		txtIpAdress.setText(settings.receiverIP);
		txtIpAdress.setBounds(10, 31, 162, 31);
		
		Label lblPort = new Label(shell, SWT.NONE);
		lblPort.setBounds(193, 10, 55, 15);
		lblPort.setText("Port");
		
		txtPort = new Text(shell, SWT.BORDER);
		txtPort.setText(Integer.toString(settings.receiverPort));
		txtPort.setBounds(193, 31, 70, 31);
		
		scale = new Scale(shell, SWT.NONE);
		scale.setPageIncrement(5);
		scale.setMaximum(255);
		scale.setBounds(10, 163, 264, 42);
		scale.setSelection(settings.AlphaChannel);
		
		Label lblOpacity = new Label(shell, SWT.NONE);
		lblOpacity.setBounds(10, 120, 122, 31);
		lblOpacity.setText("Opacity");
		
		btnLongPlaylist = new Button(shell, SWT.RADIO);
		btnLongPlaylist.setBounds(10, 79, 122, 35);
		btnLongPlaylist.setText("Long Playlist");
		btnLongPlaylist.setSelection(settings.longPlaylist);
		
		Button btnConnect = new Button(shell, SWT.NONE);
		btnConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblConnectfail.setVisible(false);
				System.out.println("Trying to connect to IP and port: " +txtIpAdress.getText() + ", " +Integer.parseInt(txtPort.getText()));
				
				//update all the settings and the settings file
				updateSettings();
								
				if(Eiscp.connectSocket(txtIpAdress.getText(),Integer.parseInt(txtPort.getText()))){
					
					System.out.println("succeded, starting listening services");
					// starts the Listener in a new thread
					mainForm.closeApp = false;
					mainForm.threadListener.start();
					Status.getInitialStatus();
					shell.dispose();
				}			
				else
				{
					lblConnectfail.setVisible(true);				
				}

			}
		});
		btnConnect.setBounds(170, 310, 104, 42);
		btnConnect.setText("Connect");
		
		lblConnectfail = new Label(shell, SWT.NONE);
		lblConnectfail.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblConnectfail.setBounds(20, 58, 264, 15);
		lblConnectfail.setText("Connection failed, check ip and port settings!");
		lblConnectfail.setVisible(false);
		
		btnBugTest = new Button(shell, SWT.NONE);
		btnBugTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mainForm.closeApp = false;
				shell.dispose();
			}
		});
		btnBugTest.setBounds(10, 337, 75, 25);
		btnBugTest.setText("dev.button");
		btnBugTest.setVisible(mainForm.indevelopment);

	}
	protected void updateSettings() {
		settings.receiverIP = txtIpAdress.getText();
		settings.receiverPort = Integer.parseInt(txtPort.getText());
		settings.AlphaChannel = scale.getSelection();
		settings.longPlaylist = btnLongPlaylist.getSelection();
		FileReadWrite.createSettingsFile(settings);
		
	}
	protected Scale getScale() {
		return scale;
	}
	protected Button getBtnLongPlaylist() {
		return btnLongPlaylist;
	}
}
