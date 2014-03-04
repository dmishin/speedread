package dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.ratson.speedread.PhraseDisplay;

import java.awt.Component;


/**config parameters
 * 
 * @author dim
 *
 */

@SuppressWarnings("serial")
public class ConfigDialog extends JDialog {
	public final static int RESULT_OK=0;
	public final static int RESULT_CANCEL=1;
	protected int dialogResult=RESULT_CANCEL; //if closed without button
	private PhraseDisplay sampleDisplay;

	public ConfigDialog(Frame owner) {
		super(owner, "Settings", /*modal=*/true);
		creatGUI();
		setLocationRelativeTo(owner);
	}

	private void creatGUI() {
		Container cp = getContentPane();
		Box vBox = Box.createVerticalBox();
		vBox.setBorder(new LineBorder(Color.RED));

		

		Box grpView = Box.createVerticalBox();
		
		grpView.add(new JLabel("Font:"));
		sampleDisplay = new PhraseDisplay();
		sampleDisplay.setWord("Hello", 2);
		sampleDisplay.setPreferredSize(new Dimension(100, 64));
		sampleDisplay.setBorder(new LineBorder(Color.BLACK));
		grpView.add(sampleDisplay);

		Box fontButtons= Box.createHorizontalBox();
		
		fontButtons.add(new JButton("Font..."));
		fontButtons.add(new JButton("Background..."));
		fontButtons.add(new JButton("Foreground..."));
		fontButtons.add(new JButton("Key Letter..."));
		grpView.add(fontButtons);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		fontButtons.add(horizontalGlue);
		
		grpView.setBorder(new TitledBorder("Visual"));
		vBox.add(grpView);
		vBox.add(Box.createVerticalGlue());

		cp.add(vBox, BorderLayout.CENTER);

		

		///Button pane
		Box buttons = Box.createHorizontalBox();
		buttons.add(Box.createHorizontalGlue());
		AbstractAction aOK=new AbstractAction("OK") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialogResult = RESULT_OK;
				setVisible(false);
			}
		};	
		AbstractAction aCancel=new AbstractAction("Cancel") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialogResult = RESULT_CANCEL;
				setVisible(false);
			}
		};	
		buttons.add(new JButton(aCancel));
		buttons.add(new JButton(aOK));
		cp.add(buttons, BorderLayout.SOUTH);
		
		pack();
	}

}
