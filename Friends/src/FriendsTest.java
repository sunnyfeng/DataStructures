import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import friends.Friends;
import friends.Graph;

public class FriendsTest {
	public static void main(String[] args) {
		String docFile = "test5.txt";
		Graph g = null;
		try(Scanner sc = new Scanner(new File(docFile))){
			g = new Graph(sc);	
		} catch (FileNotFoundException e) {
			System.out.println("Input file not found");
			return;
		}		
		
		//test shortest path
		ArrayList<String> shortest = Friends.shortestChain(g, "rich", "rachel");
		if (shortest == null || shortest.size() == 0) {
			System.out.println("Cannot be reached or bad input!");
		} else {
			for (int i = 0; i < shortest.size(); i++){
				System.out.print(shortest.get(i) + " - > ");
			}
			System.out.println();
		}
		
		
		//test cliques
		Scanner sc = new Scanner(System.in);
		System.out.println("School: ");
		String school = sc.nextLine();
		ArrayList<ArrayList<String>> cliques = Friends.cliques(g, school);
		if (cliques == null || cliques.size() == 0) {
			System.out.println("None found!");
		} else {
			print(cliques);
		}
		 
		//test connectors
		ArrayList<String> connectors = Friends.connectors(g);
		if (connectors == null || connectors.size() == 0) {
			System.out.println("No connectors found");
 		} else {
 			System.out.print("(");
 			for (String c : connectors) {
 				System.out.println(c + ", ");
 			}
 			System.out.println(")");
 		}
		
	}
	
	private static void print(ArrayList<ArrayList<String>> cliques) {
		if (cliques.size() == 0 || cliques == null) {
			System.out.println("Not found!");
			return;
		}
		String answer = "[";
		for (ArrayList<String> clique : cliques) {
			answer+= "[";
			for (String s : clique) {
				answer+=s;
				answer+=",";
			}
			answer = answer.substring(0, answer.length()-1);
			answer+="],";
		}
		answer = answer.substring(0, answer.length()-1);
		answer+= "]";
		System.out.println(answer);
	}
	
	
}
