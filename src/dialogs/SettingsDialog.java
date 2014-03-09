package dialogs;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import javax.management.RuntimeErrorException;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
		bindHandlers();
	}

	private void bindHandlers() {
		btnEditFont.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFontChooser chooser = new JFontChooser();
				chooser.setSelectedFont(phraseDisplay.getFont());
				if (chooser.showDialog(SettingsDialog.this)==JFontChooser.OK_OPTION){
					Font f = chooser.getSelectedFont();
					phraseDisplay.setFont(f);
					fldFontName.setText(encodeFont(f));
				}
			}
		});
		btnEditFgColor.addActionListener(new EditColorListener(fldFgColor, 0));
		btnEditBgColor.addActionListener(new EditColorListener(fldBgColor, 1));
		btnEditKeyCOlor.addActionListener(new EditColorListener(fldKeyColor, 2));
		
		{
			SetColorTextListener lst = new SetColorTextListener(1);
			fldBgColor.addActionListener(lst);
			fldBgColor.addFocusListener(lst);
		}
		{
			SetColorTextListener lst = new SetColorTextListener(0);
			fldFgColor.addActionListener(lst);
			fldFgColor.addFocusListener(lst);
		}
		{
			SetColorTextListener lst = new SetColorTextListener(2);
			fldKeyColor.addActionListener(lst);
			fldKeyColor.addFocusListener(lst);
		}
		btnBrowseLangDefinition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				if (fc.showOpenDialog(SettingsDialog.this) == JFileChooser.APPROVE_OPTION){
					fldLangDefinitionPath.setText(fc.getSelectedFile().getAbsolutePath());
					fldLangDefinitionPath.setEnabled(true);
					cbUseInternal.setSelected(false);
				}
			}
		});
		btnNewLangDefFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				if (fc.showSaveDialog(SettingsDialog.this) == JFileChooser.APPROVE_OPTION){
					//lastDirectory = fc.getCurrentDirectory();
					File f = fc.getSelectedFile();
					if( createNewLangDefFile(f)){
						fldLangDefinitionPath.setText(f.getAbsolutePath());
						fldLangDefinitionPath.setEnabled(true);
						cbUseInternal.setSelected(false);
					}
				}
			}
		});
		cbUseInternal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fldLangDefinitionPath.setEnabled(! cbUseInternal.isSelected());
			}
		});
		ActionListener okCancelListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("OK")){
					applySettings();
				}else if (e.getActionCommand().equals("Cancel")){
					System.out.println("Cancelled");
				}else{
					System.err.println("Unknown action command: "+e.getActionCommand());
					return;
				}
				setVisible(false);
			}
		};
		btnOk.addActionListener(okCancelListener);
		btnCancel.addActionListener(okCancelListener);
	}

	private boolean createNewLangDefFile(File file) {
		//copy template to the file
		try {
			InputStream istrm = getClass().getResourceAsStream("/language_data/dictionary-template.txt");	
			FileOutputStream ostrm = new FileOutputStream(file);
			byte[] buffer = new byte[12];
			
			while(true){
				int bytesRead = istrm.read(buffer);
				if (bytesRead <= 0) break;
				ostrm.write(buffer, 0, bytesRead);
			}
			ostrm.close();
			istrm.close();
			return true;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Error! File "+file.getPath()+" not found.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error! Failed to write file "+file.getPath());
		}
		return false;
	}
	private class SetColorTextListener implements ActionListener,FocusListener{
		private int index;
		SetColorTextListener (int colorIndex){
			index = colorIndex;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			applyColor((JTextField)arg0.getSource());
		}
		private void applyColor(JTextField src) {
			try{
				setColor(index, Color.decode("#"+src.getText()));
			}catch(Exception e){
				
			}
		}
		@Override
		public void focusGained(FocusEvent arg0) {}
		@Override
		public void focusLost(FocusEvent arg0) {
			applyColor((JTextField)arg0.getSource());
		}
		
	}
	private class EditColorListener implements ActionListener{
		private JTextField fld;
		private int fldIndex;

		public EditColorListener(JTextField fldText, int colorFieldIndex){
			fld = fldText;
			fldIndex = colorFieldIndex;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Color c = JColorChooser.showDialog(SettingsDialog.this, "Select color", getColor(fldIndex));
			if (c != null){
				setColor(fldIndex, c);
				fld.setText(encodeColor(c));
			}
		}
	}
	private Color getColor(int fldIndex){
		switch (fldIndex) {
		case 0: return phraseDisplay.getForeground();
		case 1: return phraseDisplay.getBackground();
		case 2: return phraseDisplay.getAlignKeyColor();
		default: throw new RuntimeException("Wrong color index");
		}
	}
	private void setColor(int fldIndex, Color c){
		switch (fldIndex) {
		case 0: phraseDisplay.setForeground(c); break;
		case 1: phraseDisplay.setBackground(c); break;
		case 2: phraseDisplay.setAlignKeyColor(c); break;
		default: throw new RuntimeException("Wrong color index");
		}			
	}

	private static String encodeColor(Color c){
		return Integer.toHexString(0xffffff & c.getRGB());
	}
	private void loadParameters() {
		PhraseDisplay disp = app.getDisplay();
		fldFgColor.setText(encodeColor(disp.getForeground()));
		phraseDisplay.setForeground(disp.getForeground());
		fldBgColor.setText(encodeColor(disp.getBackground()));
		phraseDisplay.setBackground(disp.getBackground());
		fldKeyColor.setText(encodeColor(disp.getAlignKeyColor()));
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
		
		String langPath = app.getLangDefinitionFilePath();
		fldLangDefinitionPath.setText(langPath);
		fldLangDefinitionPath.setEnabled(! langPath.isEmpty());
		cbUseInternal.setSelected(langPath.isEmpty());
		
	}
	private String encodeFont(Font f){
		return f.getName()+" "+f.getSize()+" "+f.getStyle();
	}
	
	public void applySettings(){
		app.setDisplayFont(phraseDisplay.getFont());
		app.setDisplayColors(phraseDisplay.getForeground(), 
					phraseDisplay.getBackground(),
					phraseDisplay.getAlignKeyColor());
		if (cbUseInternal.isSelected())
			app.setLangDefinitionFilePath("");
		else
			app.setLangDefinitionFilePath(fldLangDefinitionPath.getText());
		
		ProportionalTimeEstimator estimator = (ProportionalTimeEstimator)app.getTimeEstimator();
		
		try{
			estimator.wordDelay = Double.parseDouble(fldWordWeight.getText());
		
			estimator.characterWeight = Double.parseDouble(fldLetterWeight.getText());
			estimator.bigWordTreshold = Integer.parseInt(fldLongWordTreshold.getText());
			estimator.bigWordPenalty = Double.parseDouble(fldLongWordLetterWeight.getText());
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(this, "Bad number format: "+e.getMessage());
		}
	}

}
