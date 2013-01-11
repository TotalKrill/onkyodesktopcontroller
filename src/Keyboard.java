import org.eclipse.swt.SWT;


public class Keyboard {
	
	static void keyResponse(int keyCode,boolean RIHDstate) {
		String RHID="";
		System.out.println("keyEvent detected, Keycode: "+keyCode);
		if(keyCode == SWT.KEYPAD_ADD)Eiscp.sendCommand("MVLUP");
		if(keyCode == SWT.KEYPAD_SUBTRACT)Eiscp.sendCommand("MVLDOWN");
		if (!RIHDstate){RHID = "NTC";}
		else{RHID = "CDV";}	
			if (keyCode == 13||keyCode == SWT.KEYPAD_5){
				if (RHID.equals("NTC"))Eiscp.sendCommand("NTCSELECT");
				else Eiscp.sendCommand("CDVENTER");
				}
			if (keyCode == SWT.ARROW_UP||keyCode == SWT.KEYPAD_8){Eiscp.sendCommand(RHID+"UP");}
			if (keyCode == SWT.ARROW_DOWN||keyCode == SWT.KEYPAD_2){Eiscp.sendCommand(RHID+"DOWN");}
			if (keyCode == SWT.ARROW_LEFT||keyCode == SWT.KEYPAD_4){Eiscp.sendCommand(RHID+"LEFT");}
			if (keyCode == SWT.ARROW_RIGHT||keyCode == SWT.KEYPAD_6){Eiscp.sendCommand(RHID+"RIGHT");}
			if (keyCode == SWT.BS||keyCode == SWT.KEYPAD_3){Eiscp.sendCommand(RHID+"RETURN");}
		
		
	}
}
