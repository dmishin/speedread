package org.ratson.speedread;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import org.ratson.speedread.core.AlignmentRule;
import org.ratson.speedread.core.EuropeanAlignmentRule;
import org.ratson.speedread.core.ProportionalTimeEstimator;
import org.ratson.speedread.core.SpaceWordBreaker;
import org.ratson.speedread.core.TimeEstimator;
import org.ratson.speedread.core.WordBreaker;
import org.ratson.speedread.core.WordGrouper;

import configuration.Configurable;

import dialogs.HelpDialog;
import dialogs.JFontChooser;
import dialogs.SettingsDialog;

@SuppressWarnings("serial")
public class SpeedReadFrame extends JFrame implements Configurable{

	private static class AlignedWord{
		public int align;
		public String word;
		public AlignedWord(String s, int a){ word=s; align=a; };
	}
	private static final String KEY_CPM = "CharsPerMinute";
	private static final String KEY_FONT_FAMILY = "FontName";
	private static final String KEY_FONT_SIZE = "FontSize";
	private static final String KEY_FONT_STYLE = "FontStyle";
	private static final String KEY_COLOR_BG = "BgColor";
	private static final String KEY_COLOR_FONT = "WordColor";
	private static final String KEY_COLOR_ALIGN = "KeyColor";
	private static final String KEY_LAST_DIR = "LastDirectory";
	
	private static final String KEY_WORDBREAK_DICTIONARY = "Dictionary";
			
	private static final String CMDLINE_HELP_TEXT = 
			"java -jar speedread.jar [ARGS] [FILE]\n"+
			"   ARGS is one of:\n"+
			"   -h --help  Show this help\n"+
			"   -c --clipboard  Load clipboard contents on start\n"+
			"   FILE must be text file. For now, only plain text files are supported.";
	private String wordBreakerDictionary;


	private void loadPrefs(){
		try{
			Preferences prefs = Preferences.userNodeForPackage(SpeedReadFrame.class);
			
			prefs.putInt(KEY_CPM, charactersPerMinute);
			Font font = display.getFont();
			String fontFam = prefs.get(KEY_FONT_FAMILY,font.getName());
			int fontSz = prefs.getInt(KEY_FONT_SIZE, font.getSize());
			int fontStyle = prefs.getInt(KEY_FONT_STYLE, font.getStyle());
			
			Font newFont = new Font(fontFam, fontStyle, fontSz);
			display.setFont(newFont);
			
			//save colors
			String clr = prefs.get(KEY_COLOR_BG, null);
			if (clr!=null)
				display.setBackground(Color.decode(clr));
			clr = prefs.get(KEY_COLOR_FONT, null);
			if (clr!=null)
				display.setForeground(Color.decode(clr.toUpperCase()));
			clr = prefs.get(KEY_COLOR_ALIGN, null);
			if (clr!=null)
				display.setAlignKeyColor(Color.decode(clr.toUpperCase()));
			lastDirectory = new File(prefs.get(KEY_LAST_DIR, "./"));
			if (!lastDirectory.exists() || !lastDirectory.isDirectory())
				lastDirectory = new File("./");
			
			//load word breaker settings
			wordBreakerDictionary = prefs.get(KEY_WORDBREAK_DICTIONARY, "");
			
			//load scheduler settings
			
		}catch(Exception e){
			System.err.println("Failed to load preferences:"+e.getMessage());
			e.printStackTrace();
		}

	}
	private static String hexCOlor(Color c){
		return "#"+Integer.toHexString(c.getRGB() & 0xffffff);
	}
	private void savePrefs(){
		Preferences prefs = Preferences.userNodeForPackage(SpeedReadFrame.class);
		
		prefs.putInt(KEY_CPM, charactersPerMinute);
		Font font = display.getFont();
		prefs.put(KEY_FONT_FAMILY, font.getName());
		prefs.putInt(KEY_FONT_SIZE, font.getSize());
		prefs.putInt(KEY_FONT_STYLE, font.getStyle());
		
		//save colors
		prefs.put(KEY_COLOR_BG, hexCOlor(display.getBackground()));
		prefs.put(KEY_COLOR_FONT, hexCOlor(display.getForeground()));
		prefs.put(KEY_COLOR_ALIGN, hexCOlor(display.getAlignKeyColor()));

		prefs.put(KEY_LAST_DIR, lastDirectory.getAbsolutePath());
	}
	private static void createAndShowGUI(boolean loadClipboard, String fileToLoad)
	{
		SpeedReadFrame mainFrame = new SpeedReadFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try{
			if (fileToLoad!=null)
				mainFrame.loadFile(new File(fileToLoad));
			else if (loadClipboard)
				mainFrame.loadClipboard();
			else
				mainFrame.loadText("Hello.");
		}catch (Exception e){
			System.err.println(e.getMessage());
			System.exit(1);
		}
		mainFrame.setVisible(true);
	}
	private void doExit(int errCode){
		savePrefs();
		System.exit(errCode);
	}
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
		String fileToOpen = null;
		boolean isOpeningClipboard = false;
		try{			
			for(int i=0;i<args.length;++i){
				String arg = args[i];
				if (arg.startsWith("-")){
					if (arg.equals("-c") || arg.equals("--clipboard")){
						isOpeningClipboard = true;
					}else if(arg.equals("-h") || arg.equals("--help")){
						System.out.println(CMDLINE_HELP_TEXT);
						System.exit(0);
					}else
						throw new RuntimeException("Unexpected argument: "+arg);
				}else{
					if (fileToOpen != null){
						throw new RuntimeException("Unexpected argument: "+arg);
					}else{
						fileToOpen = arg;
					}
				}
			}
		}catch (Exception e){
			System.err.println("Error: "+e.getMessage());
			System.exit(1);
		}
		final String fFileToOpen = fileToOpen;
		final boolean fOpenClipboard = isOpeningClipboard;
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(fOpenClipboard, fFileToOpen);
            }
        });
	}
	//////// Algorithm-specific settings /////////
	private AlignmentRule aligner;
	private WordGrouper grouper;
	private WordBreaker breaker;
	private TimeEstimator timeEstimator;
	private ArrayList<AlignedWord> words = new ArrayList<SpeedReadFrame.AlignedWord>();
	private boolean isPlaying = false;
	

	private Timer timer = new Timer();
	private int charactersPerMinute = 3000;
	private int speedIncreaseStep=50;
	
	private int currentWord = 0;
	//////// GUI controls ////////////
	private PhraseDisplay display;
	private JProgressBar progressBar;
	private File lastDirectory = new File ("./");
	private JLabel labelCPM;
	private String langDefinitionFile="";
	
	
	public SpeedReadFrame() {
		super("SpeedRead");
		createUI();
		initLanguage();
		loadPrefs();
	}
	
	private void createUI() {
		ImageIcon icon = new ImageIcon(getClass().getResource("/sr-icon.png"));
		setIconImage(icon.getImage());
		Container cp = getContentPane();
		
		display = new PhraseDisplay();
		display.setFont(new Font("Helvetica", 0, 24));
		display.setMinimumSize(new Dimension(480, 64));
		display.setPreferredSize(new Dimension(480, 64));
		cp.add(display);
		
		progressBar = new JProgressBar();
		

		Box buttons = Box.createHorizontalBox();
		
		JLabel helpLabel = new JLabel("[Space] - play/pause, [B] - back, [F] - forward, [A] - again, [Ctrl+V] - paste");
		labelCPM = new JLabel("--- words/min");
		
		//helpLabel.setBorder(new LineBorder(Color.RED));
		helpLabel.setForeground(Color.GRAY);

		Box vBox = Box.createVerticalBox();
		//vBox.setBorder(new LineBorder(Color.BLUE));
		helpLabel.setHorizontalAlignment(SwingConstants.LEFT);
		helpLabel.setAlignmentX(0.0f);
		vBox.add(helpLabel);
		
		Box statusBox = Box.createHorizontalBox();
		statusBox.setAlignmentX(0.0f);

		statusBox.add(progressBar);
		statusBox.add(labelCPM);
		vBox.add(statusBox);
		
		vBox.add(buttons);
		cp.add(vBox, BorderLayout.SOUTH);

		AbstractAction aNext = new AbstractAction("Next") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nextWord();
			}
		};		
		AbstractAction aPrev= new AbstractAction("Prev") {
			@Override
			public void actionPerformed(ActionEvent e) {
				prevWord();
			}
		};
		AbstractAction aPlay= new AbstractAction("Play") {
			@Override
			public void actionPerformed(ActionEvent e) {
				togglePlay();
			}
		};
		AbstractAction aAgain = new AbstractAction("Again") {
			@Override
			public void actionPerformed(ActionEvent e) {
				setWord(0);
				if (! isPlaying)
					togglePlay();
			}
		};
		AbstractAction aPaste= new AbstractAction("Paste") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doStop();
				loadClipboard();
			}
		};
		AbstractAction aQuit = new AbstractAction("Quit") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doExit(0);
			}
		};
		AbstractAction aOpenFile= new AbstractAction("Open") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doStop();
				queryOpenFile();
			}
		};		
		AbstractAction aSpeedUp= new AbstractAction("Faster") {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateCPS(speedIncreaseStep);
			}
		};		
		AbstractAction aSpeedDOwn= new AbstractAction("Slower") {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateCPS(-speedIncreaseStep);
			}
		};
		AbstractAction aChangeFont =  new AbstractAction("Font") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doStop();
				queryFont();
			}
		};
		AbstractAction aHelp=  new AbstractAction("Help") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doStop();
				showHelp();
			}
		};
		AbstractAction aConfig=  new AbstractAction("Settings") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doShowSettings();
			}
		};
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				doStop();
			}
			
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				savePrefs();
			}
		});
		InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = getRootPane().getActionMap();
		
        im.put(KeyStroke.getKeyStroke("P"), "playText");
        im.put(KeyStroke.getKeyStroke("SPACE"), "playText");
        am.put("playText", aPlay); 

        im.put(KeyStroke.getKeyStroke("B"), "wordBack");
        am.put("wordBack", aPrev);
        
        im.put(KeyStroke.getKeyStroke("F"), "wordForward");
        am.put("wordForward", aNext); 
        
        im.put(KeyStroke.getKeyStroke("control V"), "fromClipboard");
        am.put("fromClipboard", aPaste); 
        
        im.put(KeyStroke.getKeyStroke("A"), "again");
        am.put("again", aAgain); 

        im.put(KeyStroke.getKeyStroke("Q"), "quit");
        am.put("quit", aQuit);
        
        im.put(KeyStroke.getKeyStroke("O"), "openFile");
        am.put("openFile", aOpenFile); 

        im.put(KeyStroke.getKeyStroke("UP"), "faster");
        am.put("faster", aSpeedUp); 

        im.put(KeyStroke.getKeyStroke("DOWN"), "slower");
        am.put("slower", aSpeedDOwn); 

        im.put(KeyStroke.getKeyStroke("alt F"), "font");
        am.put("font", aChangeFont); 

        im.put(KeyStroke.getKeyStroke("S"), "settings");
        am.put("settings", aConfig); 

        im.put(KeyStroke.getKeyStroke("F1"), "help");
        im.put(KeyStroke.getKeyStroke("H"), "help");
        am.put("help", aHelp); 

		pack();
	}
	
	protected void doShowSettings() {
		SettingsDialog dlg = new SettingsDialog(this); 
		dlg.setVisible(true);
	}
	private void showHelp() {
		HelpDialog dlg = new HelpDialog(this);
		dlg.setVisible(true);
	}
	private void queryFont() {
		JFontChooser fontChooser = new JFontChooser();
		fontChooser.setSelectedFont(display.getFont());
		if (fontChooser.showDialog(this)==JFontChooser.OK_OPTION){
			display.setFont(fontChooser.getSelectedFont());
		}
	}
	private double estimateWordsPerMinute(int nextNWords){
		int i0 = currentWord;
		int i1 = i0 + nextNWords;
		if (i1 >= words.size()){
			int di =  i1 - words.size() ;
			i1 -= di;
			i0 =Math.max(0, i0 - di);
		}
		if (i1 - i0 <= 0) return 0;
		double totalTimeMs = 0;
		for(int i=i0; i<i1;++i){
			totalTimeMs += timeEstimator.estimateDisplayTimeMs(words.get(i).word, charactersPerMinute);

		}
		return (i1-i0) / (totalTimeMs) * 1000.0 * 60.0;
	}
	
	protected void updateCPS(int deltaCPM) {
		int newCPM = Math.max(0, charactersPerMinute + deltaCPM);
		setCharactersPerMinute(newCPM);
	}
	void updateWordsPerMinuteEstimation(){
		labelCPM.setText((int)estimateWordsPerMinute(100) + " words/min");
	}
	public void setCharactersPerMinute(int cpm) {
		charactersPerMinute = cpm;
		updateWordsPerMinuteEstimation();
	}
	protected void queryOpenFile() {
		JFileChooser fc = new JFileChooser( lastDirectory );
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			lastDirectory = fc.getCurrentDirectory();
			try {
				loadFile(fc.getSelectedFile());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Error operning file:"+e.getMessage());
			}
		}
	}
	void initLanguage(){
		breaker = new SpaceWordBreaker();
		grouper = new WordGrouper();
		aligner = new EuropeanAlignmentRule();
		reloadLanguageDefinition("");
		timeEstimator = new ProportionalTimeEstimator();
	}

	public void loadClipboard(){
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    try {
			loadText( (String)clipboard.getData(DataFlavor.stringFlavor) );
		} catch (UnsupportedFlavorException e) {
			loadText("unsupported_flawor");
		} catch (IOException e) {
			loadText("IO_error");
		}
	}
	public void loadFile(File f) throws IOException{
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		try{
			words.clear();
			while (true){
				String line = br.readLine();
				if (line == null) break;
				loadLine(line);
			}
		}finally{
			br.close();
			fr.close();
		}
		setWord(0);
	}
	private void loadLine(String text){
		for(WordGrouper.Group g : grouper.groupWords(breaker.words(text))){
			words.add(new AlignedWord(g.join(), g.calcAlignment(aligner)));
		}		
	}
	public void loadText(String text){
		words.clear();
		loadLine(text);
		setWord(0);
	}
	
	private boolean nextWord(){
		int nw = currentWord + 1;
		if (nw < words.size()){
			setWord(nw);
			return true;
		}else{
			return false;
		}
	}

	private void prevWord() {
		if (currentWord > 0)
			setWord(currentWord - 1);
	}

	protected void scheduleNextWord() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				nextWord(); 
				if (isPlaying)
					if (currentWord < words.size()-1)
						scheduleNextWord();
					else
						isPlaying = false;
				}
		}, timeEstimator.estimateDisplayTimeMs(words.get(currentWord).word, 
				charactersPerMinute));		
	}

	private void setWord(int i) {
		if (words.isEmpty()){
			display.setWord("",0);
			progressBar.setValue(100);
		}else{
			display.setWord(words.get(i).word, words.get(i).align);
			currentWord = i;
			int progress;
			if (words.size() == 1) progress = 100;
			else progress = 100* i / (words.size()-1);
			progressBar.setValue(progress);
			updateWordsPerMinuteEstimation();
		}
	}
	private void doStop(){
		isPlaying = false;
	}
	protected void togglePlay() {
		if (isPlaying){
			doStop();
		}else{
			isPlaying = true;
			scheduleNextWord();
		}
	}
	public PhraseDisplay getDisplay() {
		return display;
	}
	public TimeEstimator getTimeEstimator() {
		return timeEstimator;
	}
	public String getLangDefinitionFilePath() {
		return langDefinitionFile;
	}
	public void reloadLanguageDefinition(String strLangDefPath) {
		InputStream istrm;
		try {
			if (langDefinitionFile.isEmpty()){
				istrm =	getClass().getResourceAsStream("/language_data/word-classes-en-ru.txt");	
			}else{
				istrm =	new FileInputStream(langDefinitionFile);
			}
			grouper.clearDictionary();
			grouper.loadDictionary(new InputStreamReader(istrm));
			istrm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	@Override
	public HashMap<String, Object> getConfiguration() {
		HashMap<String, Object> config = new HashMap<String, Object>();
		config.put(KEY_CPM, charactersPerMinute);
		
		Font font = display.getFont();
		config.put(KEY_FONT_FAMILY, font.getName());
		config.put(KEY_FONT_SIZE, font.getSize());
		config.put(KEY_FONT_STYLE, font.getStyle());
		
		//save colors
		config.put(KEY_COLOR_BG, hexCOlor(display.getBackground()));
		config.put(KEY_COLOR_FONT, hexCOlor(display.getForeground()));
		config.put(KEY_COLOR_ALIGN, hexCOlor(display.getAlignKeyColor()));

		config.put(KEY_LAST_DIR, lastDirectory.getAbsolutePath());
		return config;
	}
	@Override
	public HashMap<String, Object> getDefaultConfiguration() {
		HashMap<String, Object> config = new HashMap<String, Object>();
		config.put(KEY_CPM, 2000);
		
		config.put(KEY_FONT_FAMILY, "Arial");
		config.put(KEY_FONT_SIZE, "24");
		config.put(KEY_FONT_STYLE, 0);
		
		//save colors
		config.put(KEY_COLOR_BG, "");
		config.put(KEY_COLOR_FONT, "");
		config.put(KEY_COLOR_ALIGN, "");

		config.put(KEY_LAST_DIR, "");
		return config;
	}
	@Override
	public void applyConfiguration(HashMap<String, Object> prefs) {
		// TODO Auto-generated method stub
		prefs.putInt(KEY_CPM, charactersPerMinute);
		
		Font font = display.getFont();
		String fontFam = prefs.get(KEY_FONT_FAMILY,font.getName());
		int fontSz = prefs.getInt(KEY_FONT_SIZE, font.getSize());
		int fontStyle = prefs.getInt(KEY_FONT_STYLE, font.getStyle());
		
		Font newFont = new Font(fontFam, fontStyle, fontSz);
		display.setFont(newFont);
		
		//save colors
		String clr = prefs.get(KEY_COLOR_BG, null);
		if (clr!=null)
			display.setBackground(Color.decode(clr));
		clr = prefs.get(KEY_COLOR_FONT, null);
		if (clr!=null)
			display.setForeground(Color.decode(clr.toUpperCase()));
		clr = prefs.get(KEY_COLOR_ALIGN, null);
		if (clr!=null)
			display.setAlignKeyColor(Color.decode(clr.toUpperCase()));
		lastDirectory = new File(prefs.get(KEY_LAST_DIR, "./"));
		if (!lastDirectory.exists() || !lastDirectory.isDirectory())
			lastDirectory = new File("./");
		
	}
}