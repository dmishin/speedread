package org.ratson.speedread.core;

public interface TimeEstimator {
	int estimateDisplayTimeMs(String word, int charactersPerMinute);
}
