package org.ratson.speedread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**Transform list of words into groups.
 * Each group is a several words and integer, pointing to the "main" word in the group
 * @author dmishin
 *
 */
public class WordGrouper {
	public enum WordClass{
		GeneralWord, PrefixSmallWord, PostfixSmallWord, SmallWord, Punctuation 
	}
	
	public class Group{
		public String[] words;
		public int mainWord;
		public Group(String[] ws, int mainIdx){
			assert mainIdx >=0 && mainIdx < ws.length;
			assert ws.length > 0;
			words=ws; mainWord = mainIdx;
		}
		@Override
		public String toString() {
			StringBuffer buf = new StringBuffer("[");
			for(int i=0; i<words.length; ++i){
				boolean showMain = (i==mainWord && words.length>1);
				if (i>0) buf.append(' ');
				if (showMain) buf.append('*');
				buf.append(words[i]);
				if (showMain) buf.append('*');
			}
			buf.append(']');
			return buf.toString();
		}
		public String join(){
			StringBuffer buf = new StringBuffer();
			for(int i=0; i<words.length; ++i){
				if (i>0) buf.append(' ');
				buf.append(words[i]);
			}
			return buf.toString();			
		}
		public int calcAlignment(AlignmentRule rule){
			int prefixLen = 0;
			for (int i=0; i<mainWord; ++i)
				prefixLen += (words[i].length() + 1); //add length of each of the words and space.
			
			//First skip all pre-words
			//    then all punctuation in the main word
			//    then calculate alignment in the main word by the given rule
			return 
				prefixLen +
				firstNonPunctuation(words[mainWord]) +
				rule.alignPosition(normalize(words[mainWord]));
				
		}
	}
	
	ArrayList<Group> groups;
	ArrayList<String> currentGroup ;
	int currentMainWord;
	boolean currentGroupHasMainWord;
	
	public ArrayList<Group> groupWords(ArrayList<String> words){
		groups = new ArrayList<WordGrouper.Group>();
		currentGroup = new ArrayList<String>();
		currentMainWord=-1;
		currentGroupHasMainWord=false;
		
		for(String word: words){
			String nword = normalize(word);
			WordClass c = classify(nword);
			if (endsWithPunctuation(word)){
				//No matter what this word is, if it ends with punctuation, it either finishes the group
				if (currentGroupHasMainWord){
					if (c == WordClass.PostfixSmallWord || c == WordClass.SmallWord){
						appendWord(word);
						emitGroup();
					}else{
						emitGroup();
						appendWord(word);
						emitGroup();
					}
				}else{
					//there is not main word in the current group
					emitGroup();
					appendWord(word);
					emitGroup();
				}
				continue;
			}
			//not a punctuation
			if (currentGroupHasMainWord){
				if (c == WordClass.PostfixSmallWord || c == WordClass.SmallWord){
					//it can go after the main word
					appendWord(word);
				}else{
					//it can't go after the main word. start a new grop
					emitGroup();
					appendWord(word);
					if (c == WordClass.GeneralWord){
						setMainWord();
					}
				}
			}else{
				//current group yet has no main, and word is not a terminating one.
				appendWord(word);
				if (c == WordClass.GeneralWord){
					setMainWord();
				}
			}
				
		}
		emitGroup(); //dont' forget the last group, if present.
		//clear the internal state to release memory
		currentGroup = null;
		ArrayList<Group> rval = groups;
		groups = null;
		return rval;
	}
	
	private void setMainWord() {
		assert currentGroup.size() > 0;
		currentMainWord = currentGroup.size()-1;
		currentGroupHasMainWord = true;
	}

	private void emitGroup() {
		if (currentGroup.isEmpty()) return;
		
		if (! currentGroupHasMainWord){
			currentMainWord = 0;
		}
		Group g = new Group(currentGroup.toArray(new String[currentGroup.size()]),
							currentMainWord);
		groups.add(g);
		currentGroupHasMainWord = false;
		currentMainWord = -1;
		currentGroup.clear();
	}

	private void appendWord(String word) {
		currentGroup.add(word);
	}

	private WordClass classify(String word){
		WordClass klass = dictionary.get(word);
		if (klass == null)
			return WordClass.GeneralWord;
		else
			return klass;
	}
	private HashMap<String, WordClass> dictionary = new HashMap<String, WordGrouper.WordClass>();
	private HashSet<Character> punctuation = new HashSet<Character>();
	
	public void loadDictionary(Reader stream){
		BufferedReader br = new BufferedReader(stream);
		HashMap<String, WordClass> str2class = new HashMap<String, WordGrouper.WordClass>();
		str2class.put("L", WordClass.PrefixSmallWord);
		str2class.put("R", WordClass.PostfixSmallWord);
		str2class.put("S", WordClass.SmallWord);
		str2class.put("G", WordClass.GeneralWord);
		str2class.put("P", WordClass.Punctuation);
		try {
			do{
				String line = br.readLine();
				if (line == null) break;
				if (line.isEmpty() || line.startsWith("#"))
					continue;
				int separatorPos = line.lastIndexOf('\t');
				if (separatorPos == -1) throw new RuntimeException("Dictionary format error: no tab");
				String word = line.substring(0, separatorPos);
				WordClass klass = str2class.get(line.substring(separatorPos+1));
				assert klass != null;
				if (klass != WordClass.Punctuation){
					dictionary.put(word, klass);
				}else{
					assert word.length() == 1;
					punctuation.add(word.charAt(0));
				}
			}while(true);
			br.close();
		}catch (IOException e) {
			throw new RuntimeException(e);
		} 
	}
	private boolean endsWithPunctuation(String word){
		return punctuation.contains(word.charAt(word.length()-1));
	}
	
	public int firstNonPunctuation(String word){
		for( int i=0; i<word.length(); ++i){
			if (! punctuation.contains(word.charAt(i)))
				return i;
		}
		return 0; //punctuation-only word: return valid default.
	}
	
	private String normalize(String word){
		StringBuffer buff = new StringBuffer();
		for(int i=0; i<word.length();++i){
			char c = word.charAt(i);
			if (punctuation.contains(c)) continue;
			buff.append(Character.toLowerCase(c));
		}
		if (buff.length() == 0) 
			return word;
		return buff.toString();
	}
}
