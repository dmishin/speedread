package org.ratson.speedread.core;


public class ProportionalTimeEstimator implements TimeEstimator{
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


}
