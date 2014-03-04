package org.ratson.speedread;

public class EuropeanAlignmentRule implements AlignmentRule {

	@Override
	public int alignPosition(String word) {
		int len = word.length();
		assert len > 0;
		switch (len){
		case 1: return 0;
		case 2: return 0;
		case 3: return 1;
		case 4: return 1;
		default: return 2;
		}
	}

}
