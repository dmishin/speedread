package org.ratson.speedread;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class PhraseDisplay extends JComponent {
	private static final long serialVersionUID = 1L;
	private String wordLeft="";
	private String wordRight="";
	private String wordCenter="";
	private Color clrJustify=Color.RED;
	private double alignAt = 0.3;
	private int guideGap = 15;
	private int guideSeparation = 5;
	private Color clrGuide = Color.GRAY;

	public PhraseDisplay() {
	}
	
	public void setWord(String w, int justify){
		wordLeft = w.substring(0, justify);
		wordRight = w.substring(justify+1);
		wordCenter = w.substring(justify, justify+1);
		if (isDisplayable()) repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension( 120, 64);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension( 120, 64 );
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;	
		g2d.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		FontMetrics fm = g.getFontMetrics(getFont());
	
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		//Drawing the word
		int wPre = fm.stringWidth(wordLeft);
		int wCenter = fm.stringWidth(wordCenter);
		int justifyX = (int)(getWidth() * alignAt);
		int yPos = (int)(getHeight()*0.5);
		int wordBeginX = justifyX-wPre-wCenter/2;
		if (wordBeginX < 0){
			justifyX -= wordBeginX;
			wordBeginX = 0;
		}
		g.setColor(getForeground());
		g.drawString(wordLeft, wordBeginX, yPos);
		g.drawString(wordRight, justifyX-wCenter/2 + wCenter, yPos);
		Color clrOld = g.getColor();
		g.setColor(clrJustify);
		g.drawString(wordCenter, justifyX-wCenter/2, yPos);

		//Drawing the guides
		g.setColor(clrGuide);
		g.drawLine(0, yPos+guideGap, getWidth(), yPos+guideGap );
		g.drawLine(justifyX, yPos+guideGap, justifyX, yPos+guideSeparation );
		
		g.setColor(clrOld);
	}

	public Color getAlignKeyColor() {
		return clrJustify;
	}

	public void setAlignKeyColor(Color c) {
		clrJustify = c;
		if (isDisplayable()) repaint();
	}

	
}
