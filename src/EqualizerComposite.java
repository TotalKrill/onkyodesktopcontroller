
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;


public class EqualizerComposite extends Composite {

	private static Scale subwooferScale;
	private static Scale centerScale;
	private static Scale trebleScale;
	private static Scale subScale;
	private Label lblSubwoofer;
	private Label lblCenter;
	private Label lblSub;
	private Label lblTreble;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public EqualizerComposite(Composite parent, int style) {
		super(parent, style);
		final int ms = 75;
		
		subwooferScale = new Scale(this, SWT.CENTER);
		subwooferScale.setDragDetect(false);
		subwooferScale.setPageIncrement(1);
		subwooferScale.addListener(SWT.Selection, new Listener() {
		      @Override
			public void handleEvent(Event event) {
		        int perspectiveValue = subwooferScale.getSelection()-15;
		        System.out.println("Subwoofer: " + perspectiveValue+"dB");
		        //Status.tempSubwooferLvl = perspectiveValue;		        
		        lblSubwoofer.setText("Subwoofer: "+perspectiveValue+"dB");
		        Eiscp.sendCommand("SWL"+getVolumeSendData(perspectiveValue));
		        try {
					Thread.sleep(ms);
				} catch (InterruptedException e) {
					System.out.println("wait Interrupted");
					e.printStackTrace();
				}
		      }
		});
		subwooferScale.setSelection(15);
		subwooferScale.setMaximum(27);
		subwooferScale.setMinimum(0);
		subwooferScale.setBounds(10, 33, 230, 18);
		
						
		lblSubwoofer = new Label(this, SWT.NONE);
		lblSubwoofer.setBounds(10, 10, 230, 17);
		lblSubwoofer.setText("SubwooferLevel: ");
		
		lblCenter = new Label(this, SWT.NONE);
		lblCenter.setBounds(10, 57, 230, 17);
		lblCenter.setText("CenterLevel: ");
		
		centerScale = new Scale(this, SWT.NONE);
		centerScale.setPageIncrement(1);
		centerScale.setMaximum(24);
		centerScale.setSelection(10);
		centerScale.addListener(SWT.Selection, new Listener() {
		      @Override
			public void handleEvent(Event event) {
			        int perspectiveValue = centerScale.getSelection()-12;
			        System.out.println("Center: " + perspectiveValue+"dB");
			        Status.tempCenterLvl = perspectiveValue;		        
			        lblCenter.setText("Center: "+perspectiveValue+"dB");
			        Eiscp.sendCommand("CTL"+getVolumeSendData(perspectiveValue));
			        try {
						Thread.sleep(ms);
					} catch (InterruptedException e) {
						System.out.println("wait Interrupted");
						e.printStackTrace();
					}
			      }
			});
		centerScale.setBounds(10, 80, 230, 18);
		
		lblSub = new Label(this, SWT.NONE);
		
		lblSub.setBounds(10, 104, 230, 17);
		lblSub.setText("Sub: ");
		
		subScale = new Scale(this, SWT.NONE);
		subScale.setDragDetect(false);
		subScale.setPageIncrement(2);
		subScale.setMaximum(20);
		subScale.setSelection(10);
		subScale.setIncrement(2);
		subScale.addListener(SWT.Selection, new Listener() {
		      @Override
			public void handleEvent(Event event) {
		        int perspectiveValue = subScale.getSelection()-10;
		        if(perspectiveValue%2 != 0)
		        	perspectiveValue++;
		        System.out.println("Sub: " + perspectiveValue+"dB");
		        Status.subLevel = perspectiveValue;    
		        lblSub.setText("Sub: "+perspectiveValue+"dB");
		        
		        Eiscp.sendCommand("TFRB"+getVolumeSendData(perspectiveValue));
		        try {
					Thread.sleep(ms);
				} catch (InterruptedException e) {
					System.out.println("wait Interrupted");
					e.printStackTrace();
				}
		      }
		});
		subScale.setBounds(10, 127, 230, 18);
		
		lblTreble = new Label(this, SWT.NONE);
		lblTreble.setBounds(10, 151, 230, 17);
		lblTreble.setText("Treble: ");
		
		trebleScale = new Scale(this, SWT.NONE);
		trebleScale.setPageIncrement(2);
		trebleScale.setIncrement(2);
		trebleScale.setMaximum(20);
		trebleScale.setSelection(10);
		trebleScale.addListener(SWT.Selection, new Listener() {
		      @Override
			public void handleEvent(Event event) {
			        int perspectiveValue = trebleScale.getSelection()-10;
			        if(perspectiveValue%2 !=0)
			        	perspectiveValue++;
			        System.out.println("Treble: " + perspectiveValue+"dB");
			        Status.trebleLevel = perspectiveValue;		        
			        lblTreble.setText("Treble: "+perspectiveValue+"dB");
			        
			        Eiscp.sendCommand("TFRT"+getVolumeSendData(perspectiveValue));
			        try {
						Thread.sleep(ms);
					} catch (InterruptedException e) {
						System.out.println("wait Interrupted");
						e.printStackTrace();
					}
			      }
			});
		trebleScale.setBounds(10, 174, 230, 18);
		

	}

	protected String getVolumeSendData(int vol) {
		String sendData="";
		if(vol<0){
			vol *= -1;
			sendData="-"+Integer.toHexString(vol);
		}
		else if(vol==0){
			sendData ="00";
		}
		else{
			sendData="+"+Integer.toHexString(vol);
		}
			
		// TODO Auto-generated method stub
		return sendData.toUpperCase();
	}
	public void checkvalues(){
		int ms=0;
		Eiscp.sendCommand("TFRQSTN");
		wait(ms);
		Eiscp.sendCommand("CTLQSTN");
		wait(ms);
		Eiscp.sendCommand("SWLQSTN");
		wait(ms);
		trebleScale.setSelection(Status.trebleLevel+10);
		subScale.setSelection(Status.subLevel+10);
		centerScale.setSelection(Status.tempCenterLvl+12);
		subwooferScale.setSelection(Status.tempSubwooferLvl+15);
	}
	
	@Override
	public void update(){
		//subwooferScale.setSelection(Status.tempSubwooferLvl+15);
		lblSubwoofer.setText("Subwoofer: " + Status.tempSubwooferLvl+"dB");
		//centerScale.setSelection(Status.tempCenterLvl+12);
		lblCenter.setText("Center: " + Status.tempCenterLvl+"dB");
		//subScale.setSelection(Status.subLevel+10);
		lblSub.setText("Sub: " + Status.subLevel+"dB");
		//trebleScale.setSelection(Status.trebleLevel+10);
		lblTreble.setText("Treble: " + Status.trebleLevel+"dB");		
		}
	
	protected static void wait(int millis)
	{
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
}
