package lse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LseApp {
	
	static Scanner stdin = new Scanner(System.in);
	
	public static void main(String[] args) 
	throws IOException {
		/*
		ArrayList<Occurrence> occs = new ArrayList<>();
		
		occs.add(new Occurrence(null, 14));
		occs.add(new Occurrence(null,10));
		occs.add(new Occurrence(null,9));
		occs.add(new Occurrence(null,7));
		occs.add(new Occurrence(null,6));
		occs.add(new Occurrence(null,4));
		
		
		//test getKeyWord Method
		String word = "";
		LittleSearchEngine lse = new LittleSearchEngine();
		lse.noiseWords.add("through");
		lse.noiseWords.add("and");
		lse.noiseWords.add("before");
		
		while (true) {
			System.out.print("Enter word to test or quit => ");
			word = stdin.nextLine();
			if (word.equals("quit")) {
				break;
			}
			lse.getKeyword(word);
		}
		
		int count = 0;
		while (true) {
			System.out.print("Insert last occurrence => ");
			word = stdin.nextLine();
			if (word.equals("quit")) {
				break;
			}
			
			int freq = 0;
			try{
				freq = Integer.parseInt(word);
			} catch(NumberFormatException e) {
				System.out.println("Not an integer");
			}
			occs.add(new Occurrence(Integer.toString(count),freq));
			lse.insertLastOccurrence(occs);
			count++;
		}
		*/
		
		LittleSearchEngine lse = new LittleSearchEngine();
		lse.makeIndex("docs.txt", "noisewords.txt");
		System.out.print("keywordsIndex now contains: => ");
		printMap(lse.keywordsIndex);
		
		while (true) {
			System.out.print("Find top5 searches: => ");
			System.out.print("keyword 1: => ");
			String word1 = stdin.nextLine();
			System.out.print("keyword 2: => ");
			String word2 = stdin.nextLine();
			if (word1.equals("x")) {
				break;
			}
			ArrayList<String> answer = lse.top5search(word1, word2);
			printArrStr(answer);
		}
	}
	
	private static void printMap(HashMap<String, ArrayList<Occurrence>> map) {
		System.out.println("Printing HashMap now: ");
		for (String key: map.keySet()) {
			System.out.print(key + ": ");
			printOcc(map.get(key));
		}
		
	}
	
	private static void printArrStr(ArrayList<String> arr) {
		System.out.println("Printing String ArrayList: ");
		String answer = "";
		for (int i = 0; i < arr.size(); i++) {
			answer += arr.get(i) + ", ";
		}
		System.out.println(answer.substring(0, answer.length()-2));
	}
	
	private static void printOcc(ArrayList<Occurrence> arr) {
		String answer = "";
		for (int i = 0; i < arr.size(); i++) {
			answer += arr.get(i) + ", ";
		}
		System.out.println(answer.substring(0, answer.length()-2));
	}

}
