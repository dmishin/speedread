package org.ratson.speedread;

public class ProportionalTimeEstimator implements TimeEstimator {
	private double bigWordPenalty = 1;
	private double bigWordTreshold = 9;
	private double wordDelay = 5;
	private double characterWeight = 0.2;
	private double endOfSentenceWeight = 10;


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
