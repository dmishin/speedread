package org.ratson.speedread;

import java.util.ArrayList;

public interface WordBreaker {
	//feed a character. Return true, if word is ready.
	public ArrayList<String> words(String text); 
}
