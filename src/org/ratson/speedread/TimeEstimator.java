package org.ratson.speedread;

public interface TimeEstimator {
	int estimateDisplayTimeMs(String word, int charactersPerMinute);
}
