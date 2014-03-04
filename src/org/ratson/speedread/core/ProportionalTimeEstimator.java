package org.ratson.speedread.core;

import java.util.HashMap;

import configuration.Configurable;

public class ProportionalTimeEstimator implements TimeEstimator, Configurable {
	public double bigWordPenalty = 1;
	public int bigWordTreshold = 9;
	public double wordDelay = 5;
	public double characterWeight = 0.2;
	public double endOfSentenceWeight = 10;


	@Override
	public int estimateDisplayTimeMs(String word, int charactersPerMinute) {
		boolean isEOS = word.endsWith(".");
		double virtualChars = 
			wordDelay + 
			word.length()*characterWeight + 
			(isEOS ? endOfSentenceWeight : 0) +
			Math.max(0, word.length() - bigWordTreshold) * bigWordPenalty;
		return (int)Math.round(virtualChars* 1000 * 60) / charactersPerMinute; //time in milliseconds 
	}


	//Configurable implementation
	@Override
	public HashMap<String, Object> getConfiguration() {
		HashMap<String, Object> cfg = new HashMap<String, Object>();
		cfg.put( "bigWordPenalty", bigWordPenalty);
		cfg.put( "bigWordTreshold", bigWordTreshold);
		cfg.put( "wordDelay", wordDelay);
		cfg.put( "characterWeight", characterWeight);
		cfg.put( "endOfSentenceWeight", endOfSentenceWeight);
		return cfg;
	}


	@Override
	public HashMap<String, Object> getDefaultConfiguration() {
		HashMap<String, Object> cfg = new HashMap<String, Object>();
		cfg.put( "bigWordPenalty", 1.0);
		cfg.put( "bigWordTreshold", 9);
		cfg.put( "wordDelay", 2.0);
		cfg.put( "characterWeight", 1.0);
		cfg.put( "endOfSentenceWeight", 5.0);
		return cfg;
	}


	@Override
	public void applyConfiguration(HashMap<String, Object> config) {
	}

}
