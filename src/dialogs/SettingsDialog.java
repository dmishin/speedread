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
	
	private void applyParameters(){
		PhraseDisplay disp = app.getDisplay();
		disp.setForeground(phraseDisplay.getForeground());
		disp.setBackground(phraseDisplay.getBackground());
		disp.setAlignKeyColor(phraseDisplay.getAlignKeyColor());
		disp.setFont(phraseDisplay.getFont());
		
		ProportionalTimeEstimator estimator = (ProportionalTimeEstimator)app.getTimeEstimator();
		estimator.wordDelay = parseDouble("word weight", fldWordWeight.getText(), 0, Double.MAX_VALUE, "Value must be positive");
		estimator.characterWeight = parseDouble("character weight", fldLetterWeight.getText(), 0, Double.MAX_VALUE, "Value must be positive");
		
		estimator.bigWordTreshold = parseInteger( "long word treshold", fldLongWordTreshold.getText(), 0, 10000, "Value is out of range");
		estimator.bigWordPenalty = parseDouble("Big word letter penalty", fldLongWordLetterWeight.getText(), 0, Double.MAX_VALUE, "Value must be positive");

		String strLangDefPath = fldLangDefinitionPath.getText();
		if (cbUseInternal.isSelected())
			strLangDefPath = "";
		app.reloadLanguageDefinition(strLangDefPath);
		fldLangDefinitionPath.setText(app.getLangDefinitionFilePath());
		
	}

	private double parseInteger(String what, String strDouble, int min,
			int max, String errorMessag) {
		try{
			int x = Integer.parseInt(strDouble);
			if (! (x>=min && x <= max) )
				throw new RuntimeException("Failed to parse "+what+": "+errorMessag);
			return x;
		}catch(NumberFormatException e){
			throw new RuntimeException("Failed to parse "+what+", error: "+e.getMessage());
		}
	}

	private double parseDouble(String what, String strDouble, double min,
			double max, String errorMessag) {
		try{
			double x = Double.parseDouble(strDouble);
			if (! (x>=min && x <= max) )
				throw new RuntimeException("Failed to parse "+what+": "+errorMessag);
			return x;
		}catch(NumberFormatException e){
			throw new RuntimeException("Failed to parse "+what+", error: "+e.getMessage());
		}
	}

}
