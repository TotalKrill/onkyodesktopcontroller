import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;


public class SoundSettingsComposite extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SoundSettingsComposite(Composite parent, int style) {
		super(parent, style);
		
		Button btnMovie = new Button(this, SWT.NONE);
		btnMovie.setBounds(10, 10, 70, 25);
		btnMovie.setText("Movie");
		
		Button btnMusic = new Button(this, SWT.NONE);
		btnMusic.setBounds(90, 10, 70, 25);
		btnMusic.setText("Music");
		
		Button btnGame = new Button(this, SWT.NONE);
		btnGame.setBounds(170, 10, 70, 25);
		btnGame.setText("Game");
		
		Label lblSoundmode = new Label(this, SWT.NONE);
		lblSoundmode.setBounds(10, 41, 230, 15);
		lblSoundmode.setText("SoundMode");
		
		Button btnMusicOptimizer = new Button(this, SWT.RADIO);
		btnMusicOptimizer.setBounds(10, 90, 106, 16);
		btnMusicOptimizer.setText("Music optimizer");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
