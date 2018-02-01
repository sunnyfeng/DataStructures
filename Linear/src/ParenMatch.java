import java.util.NoSuchElementException;

public class ParenMatch {
	public static void main(String[] args) {
		String expr1 = "[(a-b)*c]*[(a-c)*(b+c)]";
		String expr2 = "((a-b)";
		String expr3 = "[a-b)";
		String expr4 = "a-b)";
		
		if (parenMatch(expr1)) {
			System.out.println(expr1 + " Matched!");
		} else {
			System.out.println(expr1 + " Not Matched!");
		}
		
		if (parenMatch(expr2)) {
			System.out.println(expr2 + " Matched!");
		} else {
			System.out.println(expr2 + " Not Matched!");
		}
		
		if (parenMatch(expr3)) {
			System.out.println(expr3 + " Matched!");
		} else {
			System.out.println(expr3 + " Not Matched!");
		}
		
		if (parenMatch(expr4)) {
			System.out.println(expr4 + " Matched!");
		} else {
			System.out.println(expr4 + " Not Matched!");
		}
	}
	
	//Returns true if parenthesis & square brackets matched in expression
	public static boolean parenMatch(String expr) {
		Stack<Character>  stack = new Stack<>();
		for (int i = 0; i < expr.length(); i++){
			char in = expr.charAt(i);
			if (in == '(' || in == '[') {
				//open = push in
				stack.push(in);
			} else if (in == ')' || in == ']') {
				//close = pop out
				try {
					char out = stack.pop();
					if (in == ')' && out == '(') {
						continue;
					} else if (in == ']' && out == '[') {
						continue;
					} else {
						return false;
					}
				} catch(NoSuchElementException e) {
					//if empty, mismatched
					return false;
				}
			}
		}
		if (stack.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}
