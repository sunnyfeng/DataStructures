package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		
		//initialize root
		TrieNode root = new TrieNode(null, null, null);
		int index = 0;
		
		//insert each word one at a time
		for (String word: allWords) {
			root = insert(allWords, word, index, root);
			index++;
		}

		return root;
		
	}
	
	/**
	 * Inserts each word from word list into Trie.
	 * @param allWords word list
	 * @param word word to be added
	 * @param index index the word is in the word list
	 * @param root root of Trie
	 * @return root root of Trie 
	 */
	private static TrieNode insert(String[] allWords, String word, int index, TrieNode root) {
		
		//first thing to add = just add first child of the whole tree
		if (root.firstChild == null) {
			Indexes ind = new Indexes(index, (short)0, (short)(word.length()-1));
			TrieNode node = new TrieNode(ind, null, null);
			root.firstChild = node;
			return root;
		}
		
		//for adding that second word, add sibling - no relations
		TrieNode ptr = root.firstChild;
		String part = word; //will be decremented in size when prefix match found
		int startInd = 0;
		int endInd = part.length()-1;
		TrieNode prev = null;
		
		//check if in common
		String common = commonPrefix(allWords, part, ptr);
		
		//for the case where it has kids and not a full match - ex: be and bid matches partially - must make b
		if (!common.equals("") && common.length() != (ptr.substr.endIndex-ptr.substr.startIndex+1) && ptr.firstChild != null) {
			prev = ptr;
			return specificCase(root, index, startInd, endInd,common, prev);
		}
		
		//no prefix match for the first node = go across siblings at that level
		if (common.equals("")) {
			boolean prefixFound = false;
			
			//look for the prefix match through siblings
			while (ptr.sibling != null) {
				common = commonPrefix(allWords, part, ptr);
				if (!common.equals("")) {
					//found prefix, start looking into it
					prefixFound = true;
					break;
				}
				ptr = ptr.sibling;
			}
			
			//get prefix match for the last sibling
			common = commonPrefix(allWords, part,ptr);
			
			//for the case where has kids and not a full match - ex: be and bid matches partially - must make b
			if (!common.equals("") && common.length() != (ptr.substr.endIndex-ptr.substr.startIndex+1) && ptr.firstChild != null) {
				return specificCase(root, index, startInd, endInd,common, ptr);
			}
			
			//if no prefixes before matched, check last sibling and then stop there
			if (!prefixFound) {
				
				//check last sibling
				if (common.equals("")) {
					//no match = make new sibling
					Indexes newInd = new Indexes(index, (short)startInd, (short)endInd);
					ptr.sibling = new TrieNode(newInd, null, null); //set ptr to new sibling created
					return root;
				} else {
					//purposefully left empty so that it continues to the main while loop
					//go through loops to dive deeper
				}
			}
		}
		
		//part of word that is remaining still needs to be matched
		startInd = common.length();
		part = word.substring(startInd);
		prev = ptr;
		ptr = ptr.firstChild;
		boolean endedAtSibling = false;
		
		while (ptr != null) {
			common = commonPrefix(allWords, part,ptr);
			
			//for the case where has kids and not a full match - ex: be and bid wants to match - must make b
			if (!common.equals("") && common.length() != (ptr.substr.endIndex-ptr.substr.startIndex+1) && ptr.firstChild != null) {
				return specificCase(root, index, startInd, endInd,common, ptr);
			}
			
			//doesn't match
			if (common.equals("")) {
				//go to siblings to check equality
				prev = ptr;
				ptr = ptr.sibling;
				endedAtSibling = true; //if loop stops after this, indicated that it stopped at a sibling
			//matches
			} else {
				startInd += common.length();
				part = word.substring(startInd);
				prev = ptr;
				ptr = ptr.firstChild;
				endedAtSibling = false; //if loop stops, indicated that stopped at going to firstChild
			}
		}
		
		//if unmatched because traversed through siblings, add new sibling at end
		if (endedAtSibling) {
			//add as another sibling - easy
			Indexes newInd = new Indexes(index, (short)startInd, (short)endInd);
			prev.sibling = new TrieNode(newInd, null, null); //set ptr to new sibling created
			return root;
		} else {
			//ended at the child - must go through below steps
			
			//1. change above parent to new substring --> prev = parent
			prev.substr.endIndex = (short) (prev.substr.startIndex + common.length()-1);
			
			//2. make unique part of the word the first child
			Indexes truncInd = new Indexes((prev.substr.wordIndex),(short)(prev.substr.endIndex+1), (short)(allWords[prev.substr.wordIndex].length()-1));
			TrieNode trunc = new TrieNode(truncInd, null, null);
			prev.firstChild = trunc;
			
			//3. make new one a sibling of the first child (maintain order of indexes - lowest first)
			Indexes newInd = new Indexes(index, (short)startInd, (short)endInd);
			trunc.sibling = new TrieNode(newInd, null, null);
			return root;
		}
		
	}
	
	/**
	 * Helper method to handle the specific case where there's a partial match so a new node must be created
	 * Ex: if be has children and then adding bid, need to make node b that goes to e and id while children of be are now children of e
	 * @return root of TrieNode
	 */
	private static TrieNode specificCase(TrieNode root, int index, int startInd, int endInd, String common, TrieNode prev) {
		//save child of the node to be changed and the old ending index
		TrieNode oldChild = prev.firstChild;
		int oldEndInd = prev.substr.endIndex;
		
		//change above parent to new substring --> prev = parent
		prev.substr.endIndex = (short) (prev.substr.startIndex + common.length()-1);

		//change the original child
		TrieNode intermed = new TrieNode(new Indexes(prev.substr.wordIndex,(short)(prev.substr.endIndex+1), (short)(oldEndInd)), null, null);
		prev.firstChild = intermed;
		intermed.firstChild = oldChild;

		//make new one a sibling of the first child (maintain order of indexes - lowest first)
		Indexes newInd = new Indexes(index, (short)(startInd+common.length()), (short)endInd);
		intermed.sibling = new TrieNode(newInd, null, null);
		return root;
	}
	
	/**
	 * Helper method to find common prefixes between word part and TrieNode
	 * @param allWords list of words
	 * @param part part of word to be matched
	 * @param ptr TrieNode to be matched
	 * @return the common matched String (ex: be and bid will return "b")
	 */
	private static String commonPrefix(String[] allWords, String part, TrieNode ptr) {
		//find word of the TrieNode
		Indexes ptrInd = ptr.substr; //current node to be compared
		String ptrWord = allWords[ptrInd.wordIndex].substring(ptrInd.startIndex, ptrInd.endIndex+1);
		
		//match the two words for a common prefix
		String common = "";
		int count = 0;
		while (count < ptrWord.length() && count < part.length()) {
			if (part.charAt(count) == ptrWord.charAt(count)) {
				common+= part.charAt(count);
			} else {
				break;
			}
			count++;
		}
		return common;
	}
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		
		ArrayList<TrieNode> arr = new ArrayList<>();
		
		//if no root or root has no child, return null
		if (root == null || root.firstChild == null) {
			return null; //nothing there
		}

		//find the point where whole prefix is matched
		TrieNode prefixNode = null;
		TrieNode ptr = root.firstChild;
		String part = prefix;
		String common = "";
		
		//while we have not found the prefix yet
		while (ptr != null) {
			common = commonPrefix(allWords, part, ptr);
			if (common.equals("")) {
				//if no match, go to siblings
				ptr = ptr.sibling;
			} else {
				//matched at least partially
				if (common.length() == part.length()) {
					//whole prefix matched, prefixNode found
					prefixNode = ptr;
					break;
				} else {
					//check case where dr and doo are partial matches but not fully == not a match, will not find a match anywhere else
					if (common.length() < allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1).length()) {
						break;
					}
					
					//matched but not finished matching prefix - traverse down child
					part = part.substring(common.length());
					ptr = ptr.firstChild;
				}
			}
		}
		
		//if after the loop, no prefixNode found, return null - no matches
		if (prefixNode == null) {
			//nothing found
			return null;
		}
		
		//if prefixNode found, recursively traverse through all leafs under that node
		addAll(allWords, prefix, arr,prefixNode);
		return arr;
		
	}
	
	/**
	 * Recursively finds all leaf nodes under the root and adds to ArrayList
	 * @param allWords list of words
	 * @param prefix prefix to be matched
	 * @param arr arr of prefix-matched TrieNodes
	 * @param root starting point of where prefix matches
	 */
	private static void addAll(String[] allWords, String prefix, ArrayList<TrieNode> arr, TrieNode root) {
		
		//base case: if the root is null or when traversing siblings, prefix no longer matches, end
		if (root == null || !allWords[root.substr.wordIndex].startsWith(prefix)) {
			return;
		}
		
		//go to children, add, go to their siblings
		addAll(allWords, prefix, arr,root.firstChild);
		
		//if a leaf and double check if it starts with prefix, add it
		if (root.firstChild == null && allWords[root.substr.wordIndex].startsWith(prefix)) {
			arr.add(root);
		}
		
		//go to the siblings as well
		addAll(allWords, prefix, arr,root.sibling);

	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
