import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import javax.swing.JOptionPane;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

class FileReadWrite 
{
	static XStream xstream = new XStream(new DomDriver());
	
	static String settingsFileName = "Settings.txt";
	/**reads the config file and returns ip and port in an String surronded with <ip>ip</ip> and likewise for port. the port needs to be parsed to an int before using
	 * 
	 * @param
	 * @return String 
	 * 
	 */
 public static String readSettings()
  {
	 //increase this if increasing the numbers of config items!!!
	 String returnString = "";
	 boolean exists = (new File(System.getProperty("user.dir")+"/"+settingsFileName)).exists();
	 if(exists){				
		try{
			// Open the file that is the first 
			// command line parameter
		 	FileInputStream fstream = new FileInputStream(System.getProperty("user.dir")+"/"+settingsFileName);
		 	// Get the object of DataInputStream
		 	DataInputStream in = new DataInputStream(fstream);
		 	BufferedReader br = new BufferedReader(new InputStreamReader(in));
		 	String strLine;
		 	
		 	//Read File Line By Line
		 	while ((strLine = br.readLine()) != null){ 
		 			returnString = returnString+strLine;
		 			// Print the content on the console
		 			//System.out.println (strLine);
		 			
		 		}
		 	//Close the input stream
		 	in.close();
		 	br.close();
		 	fstream.close();
	 		}
	 	catch (Exception e)
	 	{
		 //Catch exception if any
		 System.err.println("Error: " + e.getMessage());
	 	}
	 }
	 else
	 {		
    	createNewSettingsFile(false);  
    	returnString=readSettings();
	 }
	 //mainForm.settings = (Settings)xstream.fromXML(returnString);
	 
	return returnString;
  }
 public static void createNewSettingsFile(boolean ask)
 {
	  // does not require XPP3 library
	    Settings setup = new Settings();
	    if(ask){
		String receiverIP = JOptionPane.showInputDialog(null,"What ip adress does your reciever have?");  
		String receiverPort = JOptionPane.showInputDialog(null,"What port does your reciever use? Standard: 60128");
	    setup.receiverIP = receiverIP;
		setup.receiverPort = Integer.parseInt(receiverPort);
		}
		//create xml file
		String text = xstream.toXML(setup);
		createFile(settingsFileName, text, true);
		System.out.println("Made new "+settingsFileName);
	
	 
//		//Pop up an input box with text
//	 String receiverIP = JOptionPane.showInputDialog(null,"What ip adress does your reciever have?");  
//	 String receiverPort = JOptionPane.showInputDialog(null,"What port does your reciever use? Standard: 60128");  
//	 //Print into command prompt what you put into input dialog box
//	 String[] temp = new String[3];
//	 temp[0] = "Ip Adress:      <ip>"+receiverIP+"</ip>";
//	 temp[1] = "Port Number:    <port>"+receiverPort+"</port>";
//	 temp[2] = "AlphaChannel:   <shellAlpha>255</shellAlpha>";
//	 createFile("Config.txt", temp,true);
 }
 public static void createSettingsFile(Settings settings)
 {
	  // does not require XPP3 library
	   	String text = xstream.toXML(settings);
		createFile(settingsFileName, text, true);
		System.out.println("Created "+settingsFileName);
 }
 
 public static void createFile(String fileName,String[] fileContents,boolean inStartUpPath)
 
 {
	 String tempName;
	  Writer output = null;
	  if(inStartUpPath)
	  {
		  tempName = System.getProperty("user.dir")+"/"+fileName;
	  }
	  else
	  {
		  tempName = fileName;
	  }
	  File file = new File(tempName);
	  
	  try 
	  {
		output = new BufferedWriter(new FileWriter(file));
		
	  for(int i = 0; i < fileContents.length; i++)
	  {
		  output.write(fileContents[i]);
		  output.write("\r\n");
	  }
	  	output.close();
	  }
	  catch (IOException e) {
		
		e.printStackTrace();
	  }
	  System.out.println("Your file has been written"); 
	  }
public static void createFile(String fileName,String fileContents,boolean inStartUpPath)
 
 {
	  String tempName;
	  Writer output = null;
	  if(inStartUpPath)
	  {
		  tempName = System.getProperty("user.dir")+"/"+fileName;
	  }
	  else
	  {
		  tempName = fileName;
	  }
	  File file = new File(tempName);
	  
	  try 
	  {
		output = new BufferedWriter(new FileWriter(file));
		output.write(fileContents);
		output.close();
	  }
	  catch (IOException e) {
		
		e.printStackTrace();
	  }
	  System.out.println("Your file has been written"); 
	  }

public static void WriteToFile(String fileName, String fileContents,boolean append, boolean inStartUpPath)
{
	
	  if(inStartUpPath)
	  {
		  fileName = System.getProperty("user.dir")+"/"+fileName;
	  }
	  if (!new File(fileName).exists()){
		  createFile(fileName,"",false);
	  }
	  if(append){
		  String TextBefore = ReadFile(fileName);
		  fileContents = TextBefore + fileContents;
		  
	  }
	  createFile(fileName,fileContents,false);
}
private static String ReadFile(String filePath) {
	String returnString="";
	boolean exists = (new File(filePath).exists());
	 if(exists){				
		try{
			// Open the file that is the first 
			// command line parameter
		 	FileInputStream fstream = new FileInputStream(filePath);
		 	// Get the object of DataInputStream
		 	DataInputStream in = new DataInputStream(fstream);
		 	BufferedReader br = new BufferedReader(new InputStreamReader(in));
		 	String strLine;
		 	
		 	//Read File Line By Line
		 	while ((strLine = br.readLine()) != null){ 
		 			returnString = returnString+strLine;
		 			// Print the content on the console
		 			//System.out.println (strLine);
		 			
		 		}
		 	//Close the input stream
		 	in.close();
		 	br.close();
		 	fstream.close();
	 		}
	 	catch (Exception e)
	 	{
		 //Catch exception if any
		 System.err.println("Error: " + e.getMessage());
	 	}
	 }
	
	return returnString;
	}

 }