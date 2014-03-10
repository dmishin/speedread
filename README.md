SpeedRead
=========

Speed reading application for desctop, with smart alignment rules and adjustable scheduling. Java+Swing.

![Screenshot](/screenshot.png)


Installation
------------
The application does not reqires installation, unpack it to any appropriate folder. However, you need fresh Java Runtime intalled on your computer. You can get it frm the [Oracle site] (http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html). Linux ysers may prefer to install JRE from the repository.

Running
-------
* Windows: 
  execute `speedread.exe`
* Any OS (Linux): 
  execute command: `java -jar speedread.jar`

Reading Texts
-------------
The application supprots plain text files. You can open the text from GUI: hit `O`; from command line: run `java -jar speedread.jar book.txt`; from clipboard: hit `Ctrl+V`.

Hit `Space` to start or pause playback; use arrow keys to adjust playback speed. Hit `H` to see other keyboard shortcuts, or `S` for settings.

Compiling
---------
Requirements: jdk, apache-ant.

Run `ant -p` to see the list of available targets.



Used Software
-------------
1. JFontCHooser :  http://jfontchooser.sourceforge.net/
2. WinRun4J: http://winrun4j.sourceforge.net/
