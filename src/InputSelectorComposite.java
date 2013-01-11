import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class InputSelectorComposite extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public InputSelectorComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(null);
		
		Button btnNet = new Button(this, SWT.NONE);
		btnNet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("SLI2B");
			mainForm.setNoActiveButton(mainForm.grpButtonGroup);
			}
		});
		btnNet.setBounds(30, 10, 80, 30);
		btnNet.setText("NET");
		
		Button btnGame = new Button(this, SWT.NONE);
		btnGame.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("SLI02");
			mainForm.setNoActiveButton(mainForm.grpButtonGroup);
			}
		});
		btnGame.setBounds(140, 10, 80, 30);
		btnGame.setText("GAME");
		
		Button btnAux = new Button(this, SWT.NONE);
		btnAux.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("SLI03");
			mainForm.setNoActiveButton(mainForm.grpButtonGroup);
			}
		});
		btnAux.setBounds(30, 46, 80, 30);
		btnAux.setText("AUX");
		
		Button btnUsb = new Button(this, SWT.NONE);
		btnUsb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("SLI29");
			mainForm.setNoActiveButton(mainForm.grpButtonGroup);
			}
		});
		btnUsb.setBounds(140, 46, 80, 30);
		btnUsb.setText("USB");
		
		Button btnPc = new Button(this, SWT.NONE);
		btnPc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("SLI05");
			mainForm.setNoActiveButton(mainForm.grpButtonGroup);
			}
		});
		btnPc.setBounds(30, 82, 80, 30);
		btnPc.setText("PC");
		
		Button btnBddvd = new Button(this, SWT.NONE);
		btnBddvd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("SLI10");
			mainForm.setNoActiveButton(mainForm.grpButtonGroup);
			}
		});
		btnBddvd.setBounds(140, 82, 80, 30);
		btnBddvd.setText("BD/DVD");
		
		Button btnVcr = new Button(this, SWT.NONE);
		btnVcr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("SLI00");
			mainForm.setNoActiveButton(mainForm.grpButtonGroup);
			}
		});
		btnVcr.setBounds(30, 118, 80, 30);
		btnVcr.setText("VCR/DVR");
		
		Button btnCbl = new Button(this, SWT.NONE);
		btnCbl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("SLI01");
			mainForm.setNoActiveButton(mainForm.grpButtonGroup);
			}
		});
		btnCbl.setBounds(140, 118, 80, 30);
		btnCbl.setText("CBL/SAT");
		
		Button btnTv = new Button(this, SWT.NONE);
		btnTv.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("SLI23");
			mainForm.setNoActiveButton(mainForm.grpButtonGroup);
			}
		});
		btnTv.setBounds(30, 154, 80, 30);
		btnTv.setText("TV/CD");
		
		Button btnPort = new Button(this, SWT.NONE);
		btnPort.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("SLI40");
			mainForm.setNoActiveButton(mainForm.grpButtonGroup);
			}
		});
		btnPort.setBounds(140, 154, 80, 30);
		btnPort.setText("PORT");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
