package org.ratson.speedread;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class SettingsDialogBase extends JDialog {

	private final JPanel contentPanel = new JPanel();
	protected JTextField fldFontName;
	protected JTextField textField;
	protected JTextField textField_1;
	protected JTextField textField_2;
	protected JTextField textField_3;
	protected JTextField textField_4;
	protected JTextField textField_5;
	protected JTextField textField_6;
	protected JTextField textField_7;
	protected PhraseDisplay phraseDisplay;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SettingsDialogBase dialog = new SettingsDialogBase(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createGUI(){
		setTitle("Settings");
		setBounds(100, 100, 450, 728);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JPanel panel = new JPanel();
			panel.setAlignmentY(Component.TOP_ALIGNMENT);
			panel.setBorder(new TitledBorder(null, "View", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
			gbl_panel.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
			panel.setLayout(gbl_panel);
			{
				JLabel lblFont = new JLabel("Font:");
				GridBagConstraints gbc_lblFont = new GridBagConstraints();
				gbc_lblFont.anchor = GridBagConstraints.EAST;
				gbc_lblFont.insets = new Insets(0, 0, 5, 5);
				gbc_lblFont.gridx = 0;
				gbc_lblFont.gridy = 0;
				panel.add(lblFont, gbc_lblFont);
			}
			{
				fldFontName = new JTextField();
				fldFontName.setEditable(false);
				GridBagConstraints gbc_fldFontName = new GridBagConstraints();
				gbc_fldFontName.insets = new Insets(0, 0, 5, 5);
				gbc_fldFontName.fill = GridBagConstraints.HORIZONTAL;
				gbc_fldFontName.gridx = 1;
				gbc_fldFontName.gridy = 0;
				panel.add(fldFontName, gbc_fldFontName);
				fldFontName.setColumns(10);
			}
			{
				JButton btnEditFont = new JButton("...");
				GridBagConstraints gbc_btnEditFont = new GridBagConstraints();
				gbc_btnEditFont.insets = new Insets(0, 0, 5, 0);
				gbc_btnEditFont.gridx = 2;
				gbc_btnEditFont.gridy = 0;
				panel.add(btnEditFont, gbc_btnEditFont);
			}
			{
				JLabel lblBackground = new JLabel("Background:");
				GridBagConstraints gbc_lblBackground = new GridBagConstraints();
				gbc_lblBackground.anchor = GridBagConstraints.EAST;
				gbc_lblBackground.insets = new Insets(0, 0, 5, 5);
				gbc_lblBackground.gridx = 0;
				gbc_lblBackground.gridy = 1;
				panel.add(lblBackground, gbc_lblBackground);
			}
			{
				textField = new JTextField();
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 1;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JButton button = new JButton("...");
				GridBagConstraints gbc_button = new GridBagConstraints();
				gbc_button.insets = new Insets(0, 0, 5, 0);
				gbc_button.gridx = 2;
				gbc_button.gridy = 1;
				panel.add(button, gbc_button);
			}
			{
				JLabel lblForeground = new JLabel("Foreground:");
				GridBagConstraints gbc_lblForeground = new GridBagConstraints();
				gbc_lblForeground.anchor = GridBagConstraints.EAST;
				gbc_lblForeground.insets = new Insets(0, 0, 5, 5);
				gbc_lblForeground.gridx = 0;
				gbc_lblForeground.gridy = 2;
				panel.add(lblForeground, gbc_lblForeground);
			}
			{
				textField_1 = new JTextField();
				GridBagConstraints gbc_textField_1 = new GridBagConstraints();
				gbc_textField_1.insets = new Insets(0, 0, 5, 5);
				gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField_1.gridx = 1;
				gbc_textField_1.gridy = 2;
				panel.add(textField_1, gbc_textField_1);
				textField_1.setColumns(10);
			}
			{
				JButton button = new JButton("...");
				GridBagConstraints gbc_button = new GridBagConstraints();
				gbc_button.insets = new Insets(0, 0, 5, 0);
				gbc_button.gridx = 2;
				gbc_button.gridy = 2;
				panel.add(button, gbc_button);
			}
			{
				JLabel lblKey = new JLabel("Key:");
				GridBagConstraints gbc_lblKey = new GridBagConstraints();
				gbc_lblKey.anchor = GridBagConstraints.EAST;
				gbc_lblKey.insets = new Insets(0, 0, 5, 5);
				gbc_lblKey.gridx = 0;
				gbc_lblKey.gridy = 3;
				panel.add(lblKey, gbc_lblKey);
			}
			{
				textField_2 = new JTextField();
				GridBagConstraints gbc_textField_2 = new GridBagConstraints();
				gbc_textField_2.insets = new Insets(0, 0, 5, 5);
				gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField_2.gridx = 1;
				gbc_textField_2.gridy = 3;
				panel.add(textField_2, gbc_textField_2);
				textField_2.setColumns(10);
			}
			{
				JButton button = new JButton("...");
				GridBagConstraints gbc_button = new GridBagConstraints();
				gbc_button.insets = new Insets(0, 0, 5, 0);
				gbc_button.gridx = 2;
				gbc_button.gridy = 3;
				panel.add(button, gbc_button);
			}
			{
				phraseDisplay = new PhraseDisplay();
				phraseDisplay.setBorder(new LineBorder(Color.BLACK));

				phraseDisplay.setWord("Sample", 2);
				GridBagConstraints gbc_phraseDisplay = new GridBagConstraints();
				gbc_phraseDisplay.fill = GridBagConstraints.BOTH;
				gbc_phraseDisplay.gridwidth = 3;
				gbc_phraseDisplay.insets = new Insets(0, 0, 0, 5);
				gbc_phraseDisplay.gridx = 0;
				gbc_phraseDisplay.gridy = 4;
				
				panel.add(phraseDisplay, gbc_phraseDisplay);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setAlignmentY(Component.TOP_ALIGNMENT);
			panel.setBorder(new TitledBorder(null, "Scheduler", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] {0, 0, 0};
			gbl_panel.rowHeights = new int[] {0, 0, 0, 0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
			panel.setLayout(gbl_panel);
			{
				JLabel lblPerWordWeight = new JLabel("Word weight:");
				GridBagConstraints gbc_lblPerWordWeight = new GridBagConstraints();
				gbc_lblPerWordWeight.anchor = GridBagConstraints.EAST;
				gbc_lblPerWordWeight.insets = new Insets(0, 0, 5, 5);
				gbc_lblPerWordWeight.gridx = 0;
				gbc_lblPerWordWeight.gridy = 0;
				panel.add(lblPerWordWeight, gbc_lblPerWordWeight);
			}
			{
				textField_6 = new JTextField();
				GridBagConstraints gbc_textField_6 = new GridBagConstraints();
				gbc_textField_6.insets = new Insets(0, 0, 5, 0);
				gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField_6.gridx = 1;
				gbc_textField_6.gridy = 0;
				panel.add(textField_6, gbc_textField_6);
				textField_6.setColumns(10);
			}
			{
				JLabel lblLetterWeight = new JLabel("Letter weight:");
				GridBagConstraints gbc_lblLetterWeight = new GridBagConstraints();
				gbc_lblLetterWeight.anchor = GridBagConstraints.EAST;
				gbc_lblLetterWeight.insets = new Insets(0, 0, 5, 5);
				gbc_lblLetterWeight.gridx = 0;
				gbc_lblLetterWeight.gridy = 1;
				panel.add(lblLetterWeight, gbc_lblLetterWeight);
			}
			{
				textField_3 = new JTextField();
				GridBagConstraints gbc_textField_3 = new GridBagConstraints();
				gbc_textField_3.insets = new Insets(0, 0, 5, 0);
				gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField_3.gridx = 1;
				gbc_textField_3.gridy = 1;
				panel.add(textField_3, gbc_textField_3);
				textField_3.setColumns(10);
			}
			{
				JLabel lblLongWordTreshold = new JLabel("Long word treshold:");
				GridBagConstraints gbc_lblLongWordTreshold = new GridBagConstraints();
				gbc_lblLongWordTreshold.anchor = GridBagConstraints.EAST;
				gbc_lblLongWordTreshold.insets = new Insets(0, 0, 5, 5);
				gbc_lblLongWordTreshold.gridx = 0;
				gbc_lblLongWordTreshold.gridy = 2;
				panel.add(lblLongWordTreshold, gbc_lblLongWordTreshold);
			}
			{
				textField_4 = new JTextField();
				GridBagConstraints gbc_textField_4 = new GridBagConstraints();
				gbc_textField_4.insets = new Insets(0, 0, 5, 0);
				gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField_4.gridx = 1;
				gbc_textField_4.gridy = 2;
				panel.add(textField_4, gbc_textField_4);
				textField_4.setColumns(10);
			}
			{
				JLabel lblLongWordLetter = new JLabel("Long word letter weight:");
				GridBagConstraints gbc_lblLongWordLetter = new GridBagConstraints();
				gbc_lblLongWordLetter.anchor = GridBagConstraints.EAST;
				gbc_lblLongWordLetter.insets = new Insets(0, 0, 0, 5);
				gbc_lblLongWordLetter.gridx = 0;
				gbc_lblLongWordLetter.gridy = 3;
				panel.add(lblLongWordLetter, gbc_lblLongWordLetter);
			}
			{
				textField_5 = new JTextField();
				GridBagConstraints gbc_textField_5 = new GridBagConstraints();
				gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField_5.gridx = 1;
				gbc_textField_5.gridy = 3;
				panel.add(textField_5, gbc_textField_5);
				textField_5.setColumns(10);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setAlignmentY(Component.TOP_ALIGNMENT);
			panel.setBorder(new TitledBorder(null, "Language", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
			gbl_panel.rowHeights = new int[] {0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 0.0};
			panel.setLayout(gbl_panel);
			{
				JLabel lblLanguageDefinitionFile = new JLabel("Language definition file:");
				GridBagConstraints gbc_lblLanguageDefinitionFile = new GridBagConstraints();
				gbc_lblLanguageDefinitionFile.anchor = GridBagConstraints.EAST;
				gbc_lblLanguageDefinitionFile.insets = new Insets(0, 0, 5, 5);
				gbc_lblLanguageDefinitionFile.gridx = 0;
				gbc_lblLanguageDefinitionFile.gridy = 0;
				panel.add(lblLanguageDefinitionFile, gbc_lblLanguageDefinitionFile);
			}
			{
				textField_7 = new JTextField();
				GridBagConstraints gbc_textField_7 = new GridBagConstraints();
				gbc_textField_7.insets = new Insets(0, 0, 5, 5);
				gbc_textField_7.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField_7.gridx = 1;
				gbc_textField_7.gridy = 0;
				panel.add(textField_7, gbc_textField_7);
				textField_7.setColumns(10);
			}
			{
				JButton button = new JButton("...");
				button.setToolTipText("Browse for file");
				GridBagConstraints gbc_button = new GridBagConstraints();
				gbc_button.insets = new Insets(0, 0, 5, 5);
				gbc_button.gridx = 2;
				gbc_button.gridy = 0;
				panel.add(button, gbc_button);
			}
			{
				JButton btnNew = new JButton("New");
				btnNew.setToolTipText("Create new empty file with comments");
				GridBagConstraints gbc_btnNew = new GridBagConstraints();
				gbc_btnNew.insets = new Insets(0, 0, 5, 0);
				gbc_btnNew.gridx = 3;
				gbc_btnNew.gridy = 0;
				panel.add(btnNew, gbc_btnNew);
			}
			{
				JCheckBox chckbxUseInternalen = new JCheckBox("Use internal (EN, RU)");
				GridBagConstraints gbc_chckbxUseInternalen = new GridBagConstraints();
				gbc_chckbxUseInternalen.anchor = GridBagConstraints.WEST;
				gbc_chckbxUseInternalen.gridwidth = 2;
				gbc_chckbxUseInternalen.insets = new Insets(0, 0, 0, 5);
				gbc_chckbxUseInternalen.gridx = 0;
				gbc_chckbxUseInternalen.gridy = 1;
				panel.add(chckbxUseInternalen, gbc_chckbxUseInternalen);
			}
		}
		{
			Component verticalGlue = Box.createVerticalGlue();
			contentPanel.add(verticalGlue);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public SettingsDialogBase(JFrame owner) {
		super(owner, "Settings", true);
		createGUI();
		pack();
	}

}
