package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {	
		HashMap <String, Occurrence> keywords = new HashMap<>(500,2.0f);
		String line = "";
		
		//scan the document file
		try(Scanner sc = new Scanner(new File(docFile))){
			while (sc.hasNextLine()) {
				//read each line and split into each word by space
				line = sc.nextLine();
				String[] lineWords = line.split(" ");
				
				//viable word = add, else skip
				for (int i = 0; i < lineWords.length; i++) {
					String key = getKeyword(lineWords[i]);
					if (key != null) {
						//keyword found from getKeywords method
						if (keywords.containsKey(key)) {
							//update occurrence if exists already
							keywords.get(key).frequency++;
						} else {
							//add to keywords with frequency 1
							keywords.put(key, new Occurrence(docFile,1));
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			//throw exception if no document in that name
			throw new FileNotFoundException();
		}
		
		return keywords;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		//iterate through all keys
		for (String key : kws.keySet()) {
			//if this key is in master list already, add to end of arraylist
			if (keywordsIndex.containsKey(key)) {
				//get arraylist and then insert new occurrence
				ArrayList<Occurrence> occs = keywordsIndex.get(key);
				occs.add(kws.get(key));
				insertLastOccurrence(occs);
			} else {
				//key not in master list = make new occurrence and input
				ArrayList<Occurrence> occ = new ArrayList<>();
				occ.add(kws.get(key));
				keywordsIndex.put(key, occ);
			}
		}
		
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		//just for printing and error checking
		String unchanged = word + ": ";
		
		//trim trailing white space and lowercase it
		word = word.trim().toLowerCase();
		
		//if empty, null
		if (word.length() == 0) {
			System.out.println(unchanged + "Word length 0.");
			return null;
		}
		
		//find the last index that is the word
		int endInd = -1;
		for (int i = 0; i < word.length(); i++) {
			//when hit punctuation
			if (!Character.isLetter(word.charAt(i))){
				endInd = i;
				break;
			}
		}
		
		//if no punctuation or numbers found in word
		if (endInd == -1) {
			//if is noise word, null
			if (!noiseWords.contains(word)) {
				System.out.println(unchanged + "keyword: "  + word);
				return word;
			} else {
				System.out.println(unchanged + "Is noise word.");
				return null;
			}
		}
		
		//get the word and trim off punctuation after
		String restOfWord = word.substring(endInd);
		word = word.substring(0, endInd);
		
		//if after punctuation there are any letters, return null
		if (restOfWord.matches(".*[a-z].*")) {
			System.out.println(unchanged + "Wrong word format, not keyword");
			return null;
		//no letters and just trailing punctuation, good	
		} else {
			if (!noiseWords.contains(word)) {
				System.out.println(unchanged + "Only trailing, Return word: " + word);
				return word;
			} else {
				//if noise word, return null
				System.out.println(unchanged + "Is noise word.");
				return null;
			}
		}
	}
	
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		//error handling, shouldn't happen though
		if (occs.isEmpty()) {
			System.out.println("Occs is empty in insertLastOccurrence");
			return null;
		}
		
		//error checking
		System.out.println("Input: ");
		for (int i = 0; i < occs.size(); i++) {
			System.out.print(" " + occs.get(i));
		}
		System.out.println();
		
		ArrayList<Integer> check = new ArrayList<>();
		
		//binary search
		int low = 0;
		int high = occs.size()-2; //cut out the unsorted part
		int middle;
		
		Occurrence target = occs.get(occs.size()-1);
		int indexOfEqualFreq = -1;
		int lastIndex = -1;
		
		//descending order binary search
		while (low <= high) {
			middle = (low+high)/2;
			lastIndex = middle;
			check.add(middle); //just for debugging
			if (occs.get(middle).frequency == target.frequency) {
				indexOfEqualFreq = middle;
				break;
			} else {
				//middle is less than target, so need higher = first half
				if (occs.get(middle).frequency < target.frequency) {
					high = middle - 1;
				} else {
					//descending so if middle greater
					//need lower values = check second half
					low = middle + 1;
				}
			}
		}

		//if not failure and found equal freq, just enter
		if (indexOfEqualFreq != -1) {
			occs.add(indexOfEqualFreq,target);
		} else {
			//if not found the same freq
			//descending = closer to front is greater, farther = less freq
			if (target.frequency >= occs.get(lastIndex).frequency) {
				//descending, so closer to front
				occs.add(lastIndex, target);
			} else {
				//descending, so closer to back if smaller
				occs.add(lastIndex+1, target);
			}
		}
		
		//delete target at the end
		occs.remove(occs.size()-1);
		
		//error checking print
		System.out.println("Check: ");
		for (int i = 0; i < check.size(); i++) {
			System.out.print(" " + check.get(i));
		}
		System.out.println();
		
		//error checking occurrences - delete later
		System.out.println("Output: ");
		for (int i = 0; i < occs.size(); i++) {
			System.out.print(" " + occs.get(i));//.frequency);
		}
		System.out.println();
		
		return check;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		//initialize arraylists
		ArrayList<Occurrence> answer = new ArrayList<>();
		ArrayList<Occurrence> occ1 = null;
		ArrayList<Occurrence> occ2 = null;
		
		//get occurrences if key exists in the files -- stored descending order
		if (keywordsIndex.containsKey(kw1)) {
			occ1 = keywordsIndex.get(kw1);
		}
		if (keywordsIndex.containsKey(kw2)) {
			occ2 = keywordsIndex.get(kw2);
		}
		
		//if occ1 and occ2 null
		if (occ1 == null && occ2 == null) {
			return null;
		}
		
		//if occ1 null and occ2 not null
		if (occ1 == null && occ2 != null) {
			//copy occ2 into answer
			int count = 0;
			while (count < occ2.size()) {
				Occurrence occ = new Occurrence(occ2.get(count).document, occ2.get(count).frequency);
				answer.add(occ);
				count++;
			}
		}
		
		//if occ1 not null and occ2 null
		if (occ1 != null && occ2 == null) {
			//copy occ1 into answer
			int count = 0;
			while (count < occ1.size()) {
				Occurrence occ = new Occurrence(occ1.get(count).document, occ1.get(count).frequency);
				answer.add(occ);
				count++;
			}
		}
		
		//if both not null, merge them
		if (occ1 != null && occ2 != null) {
			//merge the two together into answer
			int oneCount = 0; //keyword 1 list has precedent over keyword 2 list
			int twoCount = 0;
			while (oneCount < occ1.size() && twoCount < occ2.size()) {
				//make new Occurrences to add
				Occurrence one = new Occurrence(occ1.get(oneCount).document, occ1.get(oneCount).frequency);
				Occurrence two = new Occurrence(occ2.get(twoCount).document, occ2.get(twoCount).frequency);
				
				//compare frequencies
				if (one.frequency > two.frequency) {
					answer.add(one);
					oneCount++;
				} else if (one.frequency < two.frequency) {
					answer.add(two);
					twoCount++;
				} else {
					//add both, but the first keyword one first - one has precedence
					answer.add(one);
					answer.add(two);
					oneCount++;
					twoCount++;
				}
			}
			
			//add leftovers to answers list
			if (oneCount < occ1.size()) {
				while (oneCount < occ1.size()) {
					Occurrence occ = new Occurrence(occ1.get(oneCount).document, occ1.get(oneCount).frequency);
					answer.add(occ);
					oneCount++;
				}
			}
			
			if (twoCount < occ2.size()) {
				while (twoCount < occ2.size()) {
					Occurrence occ = new Occurrence(occ2.get(twoCount).document, occ2.get(twoCount).frequency);
					answer.add(occ);
					twoCount++;
				}
			}
		}
		
		//error checking
		System.out.println("Answer ArrayList: ");
		for (int i = 0; i < answer.size(); i++) {
			System.out.print(answer.get(i) + " ");
		}
		System.out.println();
		
		//put answer into string arraylist with max limit of 5 documents
		ArrayList<String> docs = new ArrayList<>();
		int numDocs = 0;
		int index = 0;
		while (numDocs < 5 && index < answer.size()) {
			Occurrence doc = answer.get(index);
			if (!docs.contains(doc.document)) {
				//if not already in there, add
				docs.add(doc.document);
				numDocs++;
			}
			index++;
		}

		return docs;
	
	}
	
}
