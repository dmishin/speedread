package org.ratson.speedread.core;

import java.util.ArrayList;
import java.util.HashMap;

import configuration.Configurable;

/** Word breaker for the European languages, using spaces for word separation
 * */
public class SpaceWordBreaker implements WordBreaker{

	StringBuilder curWord = new StringBuilder();
	ArrayList<String> wordsList ;
	
	@Override
	public ArrayList<String> words(String text) {
		wordsList = new ArrayList<String>();
		for(int pos=0; pos < text.length(); ++pos){
			char c = text.charAt(pos);
			if(isWhitespace(c)){ 
				appendWord();
				curWord.setLength(0);
			}else{
				curWord.append(c);
			}
		}
		appendWord();
		return wordsList;
	}

	private void appendWord() {
		if (curWord.length() > 0){
			wordsList.add(curWord.toString());
			curWord.setLength(0);
		}
	}

	private boolean isWhitespace(char c) {
		return " \t\r\n".indexOf(c) >= 0;
	}

}
