package dialogs;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.border.LineBorder;

import org.ratson.speedread.PhraseDisplay;
import org.ratson.speedread.SpeedReadFrame;
import org.ratson.speedread.core.ProportionalTimeEstimator;
import org.ratson.speedread.core.TimeEstimator;

@SuppressWarnings("serial")
public class SettingsDialog extends SettingsDialogBase {

	private SpeedReadFrame app;

	public SettingsDialog(SpeedReadFrame speedReadFrame) {
		super(speedReadFrame);
		app = speedReadFrame;
		loadParameters();
		setLocationRelativeTo(speedReadFrame);
	}

	private void loadParameters() {
		PhraseDisplay disp = app.getDisplay();
		fldFgColor.setText(Integer.toHexString(0xffffff & disp.getForeground().getRGB()));
		phraseDisplay.setForeground(disp.getForeground());
		fldBgColor.setText(Integer.toHexString(0xffffff & disp.getBackground().getRGB()));
		phraseDisplay.setBackground(disp.getBackground());
		fldKeyColor.setText(Integer.toHexString(0xffffff & disp.getAlignKeyColor().getRGB()));
		phraseDisplay.setAlignKeyColor(disp.getAlignKeyColor());
		
		Font f = disp.getFont();
		phraseDisplay.setFont(f);
		
		fldFontName.setText(encodeFont(f));
		
		ProportionalTimeEstimator estimator = (ProportionalTimeEstimator)app.getTimeEstimator();
		DecimalFormat fmt = new DecimalFormat("#.#");
		fldWordWeight.setText(fmt.format(estimator.wordDelay));
		fldLetterWeight.setText(fmt.format(estimator.characterWeight));
		fldLongWordTreshold.setText(fmt.format(estimator.bigWordTreshold));
		fldLongWordLetterWeight.setText(fmt.format(estimator.bigWordPenalty));
		
		fldLangDefinitionPath.setText(app.getLangDefinitionFilePath());
		
	}
	private String encodeFont(Font f){
		return f.getName()+" "+f.getSize()+" "+f.getStyle();
	}

}
