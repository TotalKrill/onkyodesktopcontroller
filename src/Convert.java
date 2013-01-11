import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;


public class Convert {
	
public static String byteArrayToHex(byte[] byteArray)
{
	  StringBuilder byteString = new StringBuilder(byteArray.length);
	  for(int i=0; i < byteArray.length;)
	  {
		  byteString.append(Integer.toHexString(byteArray[i]));
		  
		  // System.out.println(Integer.toHexString(byteArray[i]));
		  i++;
	  }
	  return byteString.toString();
}

	
	
	
	public static String byteArrayToString(byte[] byteArray){
		  StringBuilder byteString = new StringBuilder(byteArray.length);
		  for(int i=0; i < byteArray.length;)	  {
			  if (byteArray[i] < 0){
				  if (byteArray[i] == -61){}
				  else{
					  int charAscii = 320+byteArray[i];
					  //System.out.println(charAscii);
					  byteString.append((char)charAscii);
				  }
			  }
			  else{
				  byteString.append((char)byteArray[i]);
				  //System.out.println(byteArray[i]);
			  }
			  i++;
		  }
		 // System.out.println(byteString);
		  return byteString.toString();
	}
		/** Converts an ASCII String to a hex  String **/
	  public static String stringToHex(String str)
	  {
	     return stringToHex( str, false);
	  }


	  /** Converts an ASCII String to a hex  String **/
	  public static String stringToHex(String str,  boolean dumpOut)
	  {
	    char[] chars = str.toCharArray();
	    String out_put = "";

	    if (dumpOut) System.out.println("Ascii: "+str);
	    if (dumpOut) System.out.print("Hex: ");
	    StringBuffer hex = new StringBuffer();
	    for(int i = 0; i < chars.length; i++)
	    {
	      out_put = Integer.toHexString(chars[i]);
	      if (out_put.length()==1) hex.append("0");
	      hex.append(out_put);
	      if (dumpOut) System.out.print("0x"+(out_put.length()==1?"0":"")+ out_put+" ");
	    }
	    if (dumpOut) System.out.println("");

	    return hex.toString();
	  }


	  /** Converts a hex String to an ASCII String **/
	  public static String hexToString(String hex)
	  {
	    return hexToString( hex, false);
	  }


	  /** Converts a hex String to an ASCII String **/
	  public static String hexToString(String hex,  boolean dumpOut)
	  {

	    StringBuilder sb = new StringBuilder();
	    StringBuilder temp = new StringBuilder();
	    String out_put = "";

	    if (dumpOut) System.out.print("Hex: ");
	    //49204c6f7665204a617661 split into two characters 49, 20, 4c...
	    for( int i=0; i<hex.length()-1; i+=2 ){

	        //grab the hex in pairs
	        out_put = hex.substring(i, (i + 2));
	        if (dumpOut) System.out.print("0x"+out_put+" ");
	        if (out_put.equals("00")) { sb.append("00");}
	         //convert hex to decimal
	        else {
	        	try
	        	{
	        		int decimal = Integer.parseInt(out_put, 16);
	        		//convert the decimal to character
	        		
	        		sb.append((char)decimal); 
	        		temp.append(decimal);
	        	}
	        	catch (NumberFormatException e)
	        	{
	        		System.out.println("heres the fail");
	        	}
	        
	        }
	    }
	    if (dumpOut) System.out.println("Decimal : " + temp.toString());

	    return sb.toString();
	  }

	  public static String byteToHexString(byte[] bytes){
			StringBuilder sb = new StringBuilder(bytes.length);
			for(int i=0; i < bytes.length; i++){
				char c = (char)bytes[i];
				sb.append(c);
				}
			   return sb.toString();
			
			}
	  
	  /**Converts a BufferedImage to SWT ImageData
	   * 
	   * @param bufferedImage
	   * @return ImageData
	   */
	  public static ImageData bufferedImageToSWT(BufferedImage bufferedImage) {
	      if (bufferedImage.getColorModel() instanceof DirectColorModel) {
	          DirectColorModel colorModel
	                  = (DirectColorModel) bufferedImage.getColorModel();
	          PaletteData palette = new PaletteData(colorModel.getRedMask(),
	                  colorModel.getGreenMask(), colorModel.getBlueMask());
	          ImageData data = new ImageData(bufferedImage.getWidth(),
	                  bufferedImage.getHeight(), colorModel.getPixelSize(),
	                  palette);
	          WritableRaster raster = bufferedImage.getRaster();
	          int[] pixelArray = new int[3];
	          for (int y = 0; y < data.height; y++) {
	              for (int x = 0; x < data.width; x++) {
	                  raster.getPixel(x, y, pixelArray);
	                  int pixel = palette.getPixel(new RGB(pixelArray[0],
	                          pixelArray[1], pixelArray[2]));
	                  data.setPixel(x, y, pixel);
	              }
	          }
	          return data;
	      }
	      else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
	          IndexColorModel colorModel = (IndexColorModel)
	                  bufferedImage.getColorModel();
	          int size = colorModel.getMapSize();
	          byte[] reds = new byte[size];
	          byte[] greens = new byte[size];
	          byte[] blues = new byte[size];
	          colorModel.getReds(reds);
	          colorModel.getGreens(greens);
	          colorModel.getBlues(blues);
	          RGB[] rgbs = new RGB[size];
	          for (int i = 0; i < rgbs.length; i++) {
	              rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF,
	                      blues[i] & 0xFF);
	          }
	          PaletteData palette = new PaletteData(rgbs);
	          ImageData data = new ImageData(bufferedImage.getWidth(),
	                  bufferedImage.getHeight(), colorModel.getPixelSize(),
	                  palette);
	          data.transparentPixel = colorModel.getTransparentPixel();
	          WritableRaster raster = bufferedImage.getRaster();
	          int[] pixelArray = new int[1];
	          for (int y = 0; y < data.height; y++) {
	              for (int x = 0; x < data.width; x++) {
	                  raster.getPixel(x, y, pixelArray);
	                  data.setPixel(x, y, pixelArray[0]);
	              }
	          }
	          return data;
	      }
	      return null;
	  }

	  
	  
	  public static byte[] hexStringToByteArray(String s) {
		    int len = s.length();
		    byte[] data = new byte[len / 2];
		    for (int i = 0; i < len; i += 2) {
		        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
		                             + Character.digit(s.charAt(i+1), 16));
		    }
		    return data;
			}	
}
