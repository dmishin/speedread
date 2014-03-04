package org.ratson.speedread;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;


@SuppressWarnings("serial")
public class HelpDialog extends JDialog {
	private JLabel helpLabel;

	public HelpDialog(JFrame owner){
		super(owner, "Help", true);
		initGUI();
		setLocationRelativeTo(owner);
	}

	private void initGUI() {
		InputStream istrm = getClass().getResourceAsStream("/help.html");
		String helpText = "Failed to load help";
		
		if (istrm != null){
			BufferedReader br = new BufferedReader(new InputStreamReader(istrm));
			StringBuilder text = new StringBuilder();
			try{
				while(true){
					String l = br.readLine();
					if (l == null) break;
					text.append(l);
					text.append('\n');
				}
			} catch (IOException e) {
				System.err.println("Failed to load help text");
				e.printStackTrace();
			}finally{
				try{
					br.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			helpText = text.toString();
		}
				
		helpLabel = new JLabel(helpText);
		Box vBox = Box.createVerticalBox();
		vBox.add(helpLabel);
		Box buttons = Box.createHorizontalBox();
		AbstractAction actionOK = new AbstractAction("OK") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		};
		JButton btnOK = new JButton(actionOK );
		buttons.add(Box.createHorizontalGlue());
		buttons.add(btnOK);
		Container cp = getContentPane();
		cp.add(vBox, BorderLayout.CENTER);
		cp.add(buttons, BorderLayout.SOUTH);
		pack();
	}
}
