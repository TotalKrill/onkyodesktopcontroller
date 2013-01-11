


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;


public class NetPlayComposite extends Composite {
	private static Label lblSongName;
	private static Label lblAlbumName;
	private static Label lblTimeInfo;
	private static Label lblArtistName;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private static int shuffleIndex = 0;
	private static int repeatIndex = 0;
	private Button btnShuffle;
	private Button btnRepeat;
	private Label lblRepeat;
	private Label lblShuffle;
	static org.eclipse.swt.graphics.Image image = SWTResourceManager.getImage(mainForm.class, "/resources/note.png");
	private Label lblJacketArt;
	public boolean changePicture;
	private static String lastAlbumName="";
	
	
	public static byte[] jacketBytes;
	public static  BufferedImage jacketBuffered;
	static ByteArrayOutputStream byteStream;
	public static Image jacketImageSWT= SWTResourceManager.getImage(mainForm.class, "/resources/note.png");
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public NetPlayComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(null);
		
		Button btnPause = new Button(this, SWT.NONE);
		btnPause.setBounds(70, 290, 52, 30);
		btnPause.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("NTCPAUSE");
			}
		});
		btnPause.setText("Pause");
		
		Button btnPlay = new Button(this, SWT.NONE);
		btnPlay.setBounds(128, 290, 52, 30);
		btnPlay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Eiscp.sendCommand("NTCPLAY");
				
			}
		});
		btnPlay.setText("Play");
		
		Button btnNext = new Button(this, SWT.ARROW | SWT.RIGHT);
		btnNext.setBounds(185, 290, 30, 30);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("NTCRIGHT");
			}
		});
		btnNext.setText("Next");
		
		Button btnPrevious = new Button(this, SWT.ARROW|SWT.LEFT);
		btnPrevious.setBounds(35, 290, 30, 30);
		btnPrevious.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("NTCLEFT");
				
			}
		});
		btnPrevious.setText("Previous");
		
		btnShuffle = new Button(this, SWT.NONE);
		btnShuffle.setBounds(132, 335, 50, 24);
		btnShuffle.setFont(SWTResourceManager.getFont("Sans", 8, SWT.NORMAL));
		btnShuffle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(shuffleIndex)
				{
				case 0: Eiscp.sendCommand("NST"+Status.playStatus+Status.repeatStatus+'-'); shuffleIndex=1; break;
				case 1: Eiscp.sendCommand("NST"+Status.playStatus+Status.repeatStatus+'S'); shuffleIndex=2; break;
				case 2: Eiscp.sendCommand("NST"+Status.playStatus+Status.repeatStatus+'A'); shuffleIndex=3; break;
				case 3: Eiscp.sendCommand("NST"+Status.playStatus+Status.repeatStatus+'F'); shuffleIndex=0; break;
				}
				
			}
		});
		
		btnShuffle.setText("Shuffle");
		
		lblArtistName = new Label(this, SWT.CENTER);
		lblArtistName.setBounds(10, 190, 240, 20);
		lblArtistName.setText("Artist Name");
		
		lblAlbumName = new Label(this, SWT.CENTER);
		lblAlbumName.setBounds(10, 215, 240, 20);
		lblAlbumName.setText("Album");
		
		lblSongName = new Label(this, SWT.CENTER);
		lblSongName.setBounds(10, 240, 240, 17);
		lblSongName.setText("SongName");
		
		lblTimeInfo = new Label(this, SWT.CENTER);
		lblTimeInfo.setBounds(10, 265, 240, 17);
		lblTimeInfo.setText("TrackTime");
		
		
		btnRepeat = new Button(this, SWT.NONE);
		btnRepeat.setBounds(40, 335, 50, 24);
		btnRepeat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(repeatIndex)
				{
				case 0: Eiscp.sendCommand("NST"+Status.playStatus+'-'+Status.shuffleStatus); repeatIndex=1; break;
				case 1: Eiscp.sendCommand("NST"+Status.playStatus+'R'+Status.shuffleStatus); repeatIndex=2; break;
				case 2: Eiscp.sendCommand("NST"+Status.playStatus+'F'+Status.shuffleStatus); repeatIndex=3; break;
				case 3: Eiscp.sendCommand("NST"+Status.playStatus+'1'+Status.shuffleStatus); repeatIndex=0; break;
				}
			}
		});
		btnRepeat.setFont(SWTResourceManager.getFont("Sans", 8, SWT.NORMAL));
		btnRepeat.setText("Repeat");
		
		lblRepeat = new Label(this, SWT.HORIZONTAL | SWT.CENTER);
		lblRepeat.setBounds(98, 335, 25, 25);
		lblRepeat.setToolTipText("Repeat Status: \n\"-\": Off\n\"R\": All\n\"F\": Folder\n\"1\": Repeat 1");
		lblRepeat.setFont(SWTResourceManager.getFont("Sans", 12, SWT.BOLD));
		lblRepeat.setText("Repeat");
		
		lblShuffle = new Label(this, SWT.HORIZONTAL | SWT.CENTER);
		lblShuffle.setBounds(185, 335, 25, 25);
		lblShuffle.setToolTipText("Shuffle Status: \n\"-\": Off\n\"S\": All \n\"A\": Album\n\"F\": Folder");
		lblShuffle.setFont(SWTResourceManager.getFont("Sans", 12, SWT.BOLD));
		lblShuffle.setText("Shuffle");
		
		lblJacketArt = new Label(this, SWT.NONE);
		lblJacketArt.setBackgroundImage(image);
		lblJacketArt.setBounds(50, 30, 150, 150);
		lblJacketArt.setToolTipText("ListSize");
		formToolkit.adapt(lblJacketArt, true, true);
		
	
		
		

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	public void initialize(){
		// ask for artist name
		Eiscp.sendCommand("NATQSTN");
		// ask for song album
		Eiscp.sendCommand("NALQSTN");
		// ask for song title
		Eiscp.sendCommand("NTIQSTN");
	}
	void makeJacketArt(byte[] bytes) {
		String string = Convert.byteArrayToString(bytes);
		System.out.println(Convert.byteArrayToString(Arrays.copyOfRange(bytes, 2, bytes.length)));
		if(string.startsWith("00")){jacketBytes = Arrays.copyOfRange(bytes, 2, bytes.length);}			//jacketBytes = Arrays.copyOfRange(bytes, 2,bytes.length);}
		if(string.startsWith("01")){jacketBytes = concatenateBytes(jacketBytes,Arrays.copyOfRange(bytes, 2, bytes.length));}
		if(string.startsWith("02")){
			jacketBytes = concatenateBytes(jacketBytes,Arrays.copyOfRange(bytes, 2, bytes.length));
			//converts the received ASCII string in byte[] to a HEX String and then back to byte array again
			String hexStringJacketArt = Convert.byteToHexString(jacketBytes);
			byte[] jacketArtBytes = Convert.hexStringToByteArray(hexStringJacketArt);
			InputStream in = new ByteArrayInputStream(jacketArtBytes);
			try {
				jacketBuffered = ImageIO.read(in);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			jacketImageSWT = new Image(Display.getDefault(), Convert.bufferedImageToSWT(jacketBuffered));
			changePicture = true;		
		}
	}
	static byte[] concatenateBytes(byte[] A, byte[] B) {
		   byte[] C= new byte[A.length+B.length];
		   System.arraycopy(A, 0, C, 0, A.length);
		   System.arraycopy(B, 0, C, A.length, B.length);
		   return C;
		}
	@Override
	public void update()
	{
		lblSongName.setText(Status.songName);
		lblAlbumName.setText(Status.album);
		lblTimeInfo.setText(Status.timeInfo);
		lblArtistName.setText(Status.artist);
		lblShuffle.setText(Character.toString(Status.shuffleStatus));
		lblRepeat.setText(Character.toString(Status.repeatStatus));
//		image = new Image(Display.getDefault(),Status.jacketArtSWT);
		if(!lastAlbumName.equals(lblAlbumName.getText())){
			jacketImageSWT = SWTResourceManager.getImage(mainForm.class, "/resources/note.png");
			lastAlbumName = lblAlbumName.getText();
			changePicture = true;
		}
		if(changePicture){
			int jacketWidth = lblJacketArt.getSize().x;
			int jacketHeight = lblJacketArt.getSize().y;
			ImageData jacketTemp = jacketImageSWT.getImageData().scaledTo(jacketWidth,jacketHeight);
			Image tempImage = new Image(Display.getDefault(),jacketTemp);
			lblJacketArt.setImage(tempImage);
			lblJacketArt.setToolTipText(Status.currentInList+"/"+Status.totalInList);			
			tempImage.dispose();
			changePicture = false;
		}
		
	}


	
	public Label getLblJacketart() {
		return lblJacketArt;
	}
}
