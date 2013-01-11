import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import org.eclipse.swt.widgets.Display;
 


class EiscpListener extends Eiscp implements Runnable {
	public volatile static boolean run;
	public static byte[] eiscpListen()
	  {
		    int dataSize;
		    byte [] responseBytes = new byte[16];
	        int numBytesReceived = 0;
	        byte [] dataBytes = null;
		    // now listen for the response
		    
		    if(connected_){
		      try{
		    	  	// get the Header + DataSize bytes (first 16 bytes, datasize is the 12th byte)
		    	  	numBytesReceived = in_.read(responseBytes);        
		  	  	
		        	// gets the data size from the 12th bit, negative value means 256 - the value
		        	dataSize = responseBytes[11];
		        	if (dataSize <= 0){
		        		dataSize = 256 + dataSize;
		        	}
		        	dataBytes = new byte[dataSize];
		        	in_.read(dataBytes);		        	
		        	System.out.println("received " + numBytesReceived);
		        
		      }		      		      
		      catch(EOFException  eofException){		      
		        
		        return new byte[2];
		      }
		      catch(SocketTimeoutException Timeout){
		      System.out.println("No command recieved in the last 2 seconds...");
		      return new byte[2];
		      }
		      catch(IOException ioException){		      
		    	System.out.println("ioException");
		        ioException.printStackTrace();
		        return new byte[2];
		      }
		      catch (NumberFormatException numberFormatException){	        	
	        		System.out.println("NumberFormatException");
	        		numberFormatException.printStackTrace();
	        		return new byte[2];
	        	}		      
		    }
		    else 
		      System.out.println(" Not Connected to Receive ");
		    //returns the byte array excluding the !1 part and the last 3 bytes containing nothing.
		    return Arrays.copyOfRange(dataBytes,2,dataBytes.length-3);
		    
	  }

	@Override
	public void run() {
		int i =0;
		run = true;
		try {
			in_ = new DataInputStream(eiscpSocket_.getInputStream());
			Eiscp.eiscpSocket_.setSoTimeout(2000);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		while (run) {
	      // Running big task
			final byte[] statusBytes = eiscpListen();			
			// This will update the UI asynchronously, e.g. the calling thread will continue
			Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run(){
						// ... do any work that updates the screen ...
						//System.out.println("status message sent to be filtered: " +status);
						Status.filterMessage(statusBytes);		
					}
				});

			if(i == 10){			
				System.out.println("Every " + i + " loop");
				i = 0;
			}
			else{			
				i++;
			}
		}
		//eiscp.closeSocket();
	}
}
	