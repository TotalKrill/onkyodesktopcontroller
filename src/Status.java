

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JOptionPane;



public class Status {
	public static boolean muted = false;
	public static boolean power = false;
	public static boolean musicOptimizer = false;
	public static String mode = "";
	public static int masterVolume = 0;
	public static String imageString = "";
	public static String timeInfo = "";
	public static String artist = "";
	public static String album = "";
	public static String songName = "";
	public static int inputSelected = 0;
	public static char repeatStatus = '-';
	public static char shuffleStatus = '-';
	public static char playStatus = '-';
	
	
	
	
	public static int currentInList= 0;
	public static int totalInList = 0;
	public static Integer tempSubwooferLvl=0;
	protected static int tempCenterLvl =0;
	protected static int subLevel  = 0;
	protected static int trebleLevel = 0;
	
	public static int listCursor = 0;
	public static String[] listData = new String[]{"-","-","-","-","-","-","-","-","-","-"};
	static PlayList wholePlayList = new PlayList("--", 0, 0,0);
	static int currentPlaylist=0;
	private static int playListSelected=0;
	static String lastListName = "";
	
	
	public static void Main(String status)
   {
     
      
   }
public static void getInitialStatus() 
{
	//ask for power info	
	Eiscp.sendCommand("PWRQSTN");
	
	//ask for volume
	Eiscp.sendCommand("MVLQSTN");
	//ask for input selected
	Eiscp.sendCommand("SLIQSTN");
	//ask for muted
	Eiscp.sendCommand("AMTQSTN");
	//send input nothing to get N/A response
	Eiscp.sendCommand("SLI");
	//ask for music optimizer
	Eiscp.sendCommand("MOTQSTN");
//	// ask for artist name
//	eiscp.sendCommand("NATQSTN");
//	// ask for song album
//	eiscp.sendCommand("NALQSTN");
//	// ask for song title
//	eiscp.sendCommand("NTIQSTN");
	
	
		
	System.out.println("Sent status querys");
}
protected static void wait(int millis)
{
	try {
		Thread.sleep(millis);
	} catch (InterruptedException e) {
		
		e.printStackTrace();
	}
	}
public static void filterMessage(byte[] statusBytes)
{
	try{
		String statusString = Convert.byteArrayToString(statusBytes);
		System.out.println(statusString);
		//System.out.println(statusBytes.length);
		if (statusString.length()<=3){}
		else if (statusString.endsWith("N/A")){}
		else if (statusString.startsWith("PWR01")){power = true;}
		else if (statusString.startsWith("PWR00")){power = false;}
		else if (statusString.startsWith("AMT00")){muted = false;}
		else if (statusString.startsWith("AMT01")){muted = true;}
		else if (statusString.startsWith("MOT")){musicOptimizer = getBool(statusString);}
		else if (statusString.startsWith("MVL")){masterVolume = Integer.valueOf(statusString.split("MVL")[1], 16).intValue();}
		else if (statusString.startsWith("NTM")){timeInfo = statusString.split("NTM")[1];}
		else if (statusString.startsWith("NAT")){artist = statusString.split("NAT")[1];}
		else if (statusString.startsWith("NAL")){album = statusString.split("NAL")[1];}
		else if (statusString.startsWith("NAT")){artist = statusString.split("NAT")[1];}
		else if (statusString.startsWith("NTI")){songName = statusString.split("NTI")[1];}
		else if (statusString.startsWith("SLI")){inputSelected = Integer.valueOf(statusString.split("SLI")[1], 16);}
		else if (statusString.startsWith("NST")){playSettings(statusString.split("NST")[1]);}
		else if (statusString.startsWith("NLS")){handleList(statusString.split("NLS")[1]);}
		else if (statusString.startsWith("NKY")){waitInput(statusString.split("NKY")[1]);}
		else if (statusString.startsWith("NJA")){mainForm.netPlay.makeJacketArt(Arrays.copyOfRange(statusBytes, 3, statusBytes.length));}
		else if (statusString.startsWith("NTR")){listSizeData(statusString.split("NTR")[1]);}
		else if (statusString.startsWith("SWL")){tempSubwooferLvl = getVolumeData(statusString.split("SWL")[1]);}
		else if (statusString.startsWith("CTL")){tempCenterLvl = getVolumeData(statusString.split("CTL")[1]);}
		else if (statusString.startsWith("TFR")){trebleLevel = getTrebleData(statusString.split("TFR")[1]);
		subLevel = getSubData(statusString.split("TFR")[1]);
		}
		else if (statusString.startsWith("NLT")){
			handlePlayList(statusString.split("NLT")[1]);
		}
		mainForm.updateGUI();
	}
	catch(Exception e){
		e.printStackTrace();	
	}

}

private static boolean getBool(String string) {
	boolean bool = false;
	if (string.endsWith("01"))
		bool = true;
	return bool;
}
private static int getSubData(String string) {
	return getVolumeData(string.substring(1,3));
}
private static int getTrebleData(String string) {
	return getVolumeData(string.substring(4));
}
private static int getVolumeData(String string){
	int volume = 0;
	if(string.startsWith("+"))
		volume = Integer.parseInt(string.substring(1), 16);
	if(string.startsWith("-"))
		volume =Integer.parseInt(string.split("-")[1], 16)*-1;
	return volume;
}
private static void listSizeData(String string) {
	currentInList=Integer.parseInt(string.split("/")[0]);
	totalInList=Integer.parseInt(string.split("/")[1]);
	}


static byte[] concatenateBytes(byte[] A, byte[] B) {
	   byte[] C= new byte[A.length+B.length];
	   System.arraycopy(A, 0, C, 0, A.length);
	   System.arraycopy(B, 0, C, A.length, B.length);
	   return C;
	}
static String[] concatenateString(String[] A, String[] B) {
	   String[] C= new String[A.length+B.length];
	   System.arraycopy(A, 0, C, 0, A.length);
	   System.arraycopy(B, 0, C, A.length, B.length);
	   return C;
	}
/**
 * 
 * @param A Array to add to
 * @param B Array to be added
 * @param From Starts Adding from this position
 * @return
 */
static String[] concatenateStringFrom(String[] A, String[] B,int From) {
	String[] C;
	if (From+B.length>A.length){
		C = new String[From+B.length];
	}
	else{
		C= new String[A.length];
	}
	
	   System.arraycopy(A, 0, C, 0, A.length);
	   System.arraycopy(B, 0, C,From , B.length);
	   return C;
	}
private static void waitInput(String string) {
	String question="";
	if(string.equals("01")){question = "Enter Username";}
	if(string.equals("02")){question = "Enter Password";}
	if(string.equals("03")){question = "Enter Artist";}
	if(string.equals("04")){question = "Enter Album";}
	if(string.equals("05")){question = "Enter Song";}
	if(string.equals("06")){question = "Enter Station";}
	if(string.equals("07")){question = "Enter Tag Name";}
	if(string.equals("08")){question = "Enter Artist or Song";}
	if(string.equals("09")){question = "Enter Episode";}
	if(string.equals("0A")){question = "Enter Pin Code (some digit Number [0-9])";}
		
	String input;
	try {
		input = JOptionPane.showInputDialog(null, question);
		if (input.equals("")||input == null){
			Eiscp.sendCommand("NTCRETURN");
		}
		else{
			Eiscp.sendCommand("NKY"+input.trim());

		}
	}
	catch (Exception e) {

	}
}
// 0 = netPlay
// 1 = netList
private static void handleList(String listInfo) {
	
	if (listInfo.startsWith("A")||listInfo.startsWith("U")){
		int position = Integer.parseInt(listInfo.substring(1,2));
	    String listText =listInfo.substring(3);
		if (listText.contains("ģÃû")){
			listText = listText.replaceAll("ģÃû", "-");
		}
		listData[position] = listText;	
		wholePlayList.SongTitles[wholePlayList.currentTenStartsAt+position]= (wholePlayList.currentTenStartsAt+position+1)+". "+listText;
//		playList = concatenateStringFrom(playList, listData, playListSelected);
		mainForm.setTopComposite(1);
		
		}
	else if (listInfo.startsWith("C")){
		if (listInfo.toCharArray()[1] != '-'){
		listCursor = Integer.parseInt(listInfo.substring(1,2));
		}
		else{listCursor = 0;}
		if (listInfo.endsWith("P") ){
			
			listData = new String[]{"-","-","-","-","-","-","-","-","-","-"};
						
			}
		}
	mainForm.netList.update();
	}
private static void handlePlayList(String string) {
	//numbers 0,1 is network source type in ascii (Byte 1)
	//numbers 2,3 is menu depth in hex (byte 2)
	//numbers 4,5,6,7 are the cursor position in hex (byte3,4)
	//numbers 8,9,10,11 are the total items in the list in hex(byte 5,6)
		if(string.length() > 22){
			String name = string.substring(22);		
			int listlength = Integer.parseInt(string.substring(8,12), 16);
			if(listlength == 0);

			int listcursorpos = Integer.parseInt(string.substring(4,8), 16);
			int listStartAt = listcursorpos/10*10;
			int menuDepth = Integer.parseInt(string.substring(2,3));

			System.out.println("list name: "+name);	

			//System.out.println(string.substring(3, 22));
			System.out.println("list cursor  : "+playListSelected+" ("+string.substring(8,12)+")");
			System.out.println("list lenght  : "+listlength);
			//checks that it actually is the same list.
			System.out.println("Checking if list is known!");
			

			if (wholePlayList.title.equals(name) && wholePlayList.menuDepth == menuDepth && wholePlayList.length == listlength){
				// if at end of list say it is looped and go back to list start.
				System.out.println("list is known");
				wholePlayList.currentTenStartsAt = listStartAt;
				if(listStartAt == wholePlayList.loopStartFlag && wholePlayList.looped ==false){
					System.out.println("list has been looped succesfully!");
					wholePlayList.looped = true;


				}

				if(wholePlayList.looped == false){
					Eiscp.sendCommand("NTCRIGHT");
				}


			}
			else{
				System.out.println("List is NOT known! Creating new list and setting list start flag.");

				wholePlayList = new PlayList(name,listlength,menuDepth,listStartAt);
				Eiscp.sendCommand("NTCRIGHT");
			}

		}

	if(wholePlayList.looped)
		for(int i=0;i<wholePlayList.SongTitles.length; i++)
			System.out.println(i+" - "+wholePlayList.SongTitles[i]);
	
	//FileRead.WriteToFile("NLT", string, true,true);
	}


	

private static void playSettings(String string) {
	if (string.toCharArray()[0] == 'S')
	{
		playStatus = 'S';
	}
	else if (string.toCharArray()[0] == 'P')
	{
		playStatus = 'P';
	}
	else if (string.toCharArray()[0] == 'p')
	{
		playStatus = 'p';
	}
	else if (string.toCharArray()[0] == 'F')
	{
		playStatus = 'F';
	}
	else if (string.toCharArray()[0] == 'R')
	{
		playStatus = 'R';
	}
	if (string.toCharArray()[1] == '-')
	{
		repeatStatus = '-';
	}
	else if (string.toCharArray()[1] == 'R')
	{
		repeatStatus = 'R';
	}
	else if (string.toCharArray()[1] == 'F')
	{
		repeatStatus = 'F';
	}
	else if (string.toCharArray()[1] == '1')
	{
		repeatStatus = '1';
	}
	if (string.toCharArray()[2] == '-')
	{
		shuffleStatus = '-';
	}
	else if (string.toCharArray()[2] == 'S')
	{
		shuffleStatus = 'S';
	}
	else if (string.toCharArray()[2] == 'A')
	{
		shuffleStatus = 'A';
	}
	else if (string.toCharArray()[2] == 'F')
	{
		shuffleStatus = 'F';
	}
}

public static void writeByteArrayToFile(String strFilePath,byte[] bytes) {
	
	     try
	     {
	       FileOutputStream fos = new FileOutputStream(strFilePath);
	       fos.write(bytes);
	     	     
	       fos.close();
	     
	     }
	     catch(FileNotFoundException ex)
	     {
	      System.out.println("FileNotFoundException : " + ex);
	     }
	     catch(IOException ioe)
	     {
	      System.out.println("IOException : " + ioe);
	     }
	   
	  
	}
/**Converts a boolean to either 00 or 01
 * 
 * @param bool
 * @return 01(true) or 00(false) 
 */
public static String boolToString(boolean bool){
	String string ="00";
	if(bool)
		string = "01";
	else
		string = "00";
	return string;
}



}
