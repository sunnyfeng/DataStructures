package apps;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	/**
	 * Expression to be evaluated
	 */
	String expr;                
    
	/**
	 * Scalar symbols in the expression 
	 */
	ArrayList<ScalarSymbol> scalars;   
	
	/**
	 * Array symbols in the expression
	 */
	ArrayList<ArraySymbol> arrays;
    
    /**
     * String containing all delimiters (characters other than variables and constants), 
     * to be used with StringTokenizer
     */
    public static final String delims = " \t*+-/()[]";
    
    /**
     * Initializes this Expression object with an input expression. Sets all other
     * fields to null.
     * 
     * @param expr Expression
     */
    public Expression(String expr) {
        this.expr = expr;
    }

    /**
     * Populates the scalars and arrays lists with symbols for scalar and array
     * variables in the expression. For every variable, a SINGLE symbol is created and stored,
     * even if it appears more than once in the expression.
     * At this time, values for all variables are set to
     * zero - they will be loaded from a file in the loadSymbolValues method.
     */
    public void buildSymbols() {
    		//initialize
    		arrays = new ArrayList<>();
    		scalars = new ArrayList<>();
    		
    		//take out spaces
    		String exprNoSpaces = expr.replaceAll("\\s+","");
    		
    		//while loop to build symbols
    		int count = 0;
    		String temp = "";
    		while (count < exprNoSpaces.length()) {
    			
    			//if it is a letter
    			if (Character.isLetter(exprNoSpaces.charAt(count))){
    				temp = Character.toString(exprNoSpaces.charAt(count));
    				count++;
    				
    				//get all letters in a row and store in temp
    				while (count < exprNoSpaces.length() && Character.isLetter(exprNoSpaces.charAt(count))){
    					temp = temp + Character.toString(exprNoSpaces.charAt(count));
    					count++;
    				}
 
    				//if count at end of the expression, last variable must be a scalar variable
    				if (count >= exprNoSpaces.length()) {
    					//can't be an array, add last variable to scalar if hasn't already been added in
    					if (symbolIsUnique(temp, true)) {
    						scalars.add(new ScalarSymbol(temp));
    					}
    				} else if (exprNoSpaces.charAt(count) == '[') {
    					//is an array variable, add if not already inside
    					if (symbolIsUnique(temp, false)) {
    						arrays.add(new ArraySymbol(temp));
    					}
    				} else {
    					//is a normal variable, add if not already inside
    					if (symbolIsUnique(temp, true)) {
    						scalars.add(new ScalarSymbol(temp));
    					}
    				}
    			}
    			count++;
    		}
    		
    }
    
    /**
     * Make sure the symbol has not already been added.
     * 
     * @param symbol symbol to look for
     * @param scalars scalars ArrayList to look through
     * @param arrays arrays ArrayList to look through
     * @return true if symbol not in there, false if found
     */
    private boolean symbolIsUnique(String symbol, boolean isScalar) {
    		//if parsing through the scalar list
    		if (isScalar){
    			if (scalars.size() == 0) {
    				return true;
    			} else {
    				for (int i = 0; i < scalars.size(); i++) {
        				if (scalars.get(i).name.equals(symbol)) {
        					//if found, already there, don't add
        					return false;
        				}
        			}
    				return true;
    			}
    		} else {
    			//if parsing through array list
    			if (arrays.size() == 0) {
    				return true;
    			} else {
    				for (int i = 0; i < arrays.size(); i++) {
        				if (arrays.get(i).name.equals(symbol)) {
        					//found, return false
        					return false;
        				}
        			}
    				return true;
    			}
    		}
  
    }
    
    /**
     * Loads values for symbols in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     */
    public void loadSymbolValues(Scanner sc) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String sym = st.nextToken();
            ScalarSymbol ssymbol = new ScalarSymbol(sym);
            ArraySymbol asymbol = new ArraySymbol(sym);
            int ssi = scalars.indexOf(ssymbol);
            int asi = arrays.indexOf(asymbol);
            if (ssi == -1 && asi == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                scalars.get(ssi).value = num;
            } else { // array symbol
            	asymbol = arrays.get(asi);
            	asymbol.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    asymbol.values[index] = val;              
                }
            }
        }
    }
    
    
    /**
     * Evaluates the expression, using RECURSION to evaluate subexpressions and to evaluate array 
     * subscript expressions.
     * 
     * @return Result of evaluation
     */
    public float evaluate() {
    		//makes them tokens
    		StringTokenizer strToken = new StringTokenizer(expr.replaceAll("\\s+",""),delims,true);
    		
    		//make new array of size of number of tokens
    		String[] strArr = new String[strToken.countTokens()];
    		
    		//put tokens into string array
    		int count = 0;
    		while (strToken.hasMoreTokens()) {
    			strArr[count] = strToken.nextToken();
    			count++;
    		}
    		
    		//change string array into ArrayList
    		ArrayList<String> str = new ArrayList<String>(Arrays.asList(strArr));
    		return recurveEval(str);
    }
    
    /**
     * Recursive call to evaluate expression.
     * 
     * @param str ArrayList of tokens to be evaluated
     * @return expression result
     */
    	private float recurveEval(List<String> str) {
    		//create new stack each time
    		Stack<Float> values = new Stack<>();
    		Stack<String> operators = new Stack<>();
    		
    		int count = 0;
    		float num = -1;
    		String oper = "";
    		while (count < str.size()) {
    			try {
    				//is a number, so push it into values and continue
    				num = Float.parseFloat(str.get(count));
    				values.push(num);
    				count++;
    				continue;	
    			} catch (NumberFormatException e) {
    				//not a float number
    				   	
    				oper = str.get(count);	
    				if (oper.equals("*") || oper.equals("/") || oper.equals("-") || oper.equals("+")) {
    					//is an operator, push into stacks and continue
    					operators.push(oper);
    					count++;
    					continue;
    				} else if (!oper.equals("[") && !oper.equals("]") && !oper.equals("(") && !oper.equals(")")) {
    					//is variable or array

    					boolean isVariable = false;
    					
    					//check if is variable and if it is, push in the value
    					for (int i = 0; i < scalars.size(); i++) {
    						if (oper.equals(scalars.get(i).name)) {
    							values.push((float) (scalars.get(i).value)); //cast integer into float
    							isVariable = true;
    							break;
    						}
    					}
    					
    					//if it is a variable, continue the loop
    					if (isVariable) {
    						count++;
    						continue;
    					}	
    					
    					//not a variable, so check if array
    					for (int i = 0; i < arrays.size(); i++) {
    						if (oper.equals(arrays.get(i).name)) {
    							//get index of last "]" --- A[stuff] --> get "stuff" without brackets
    							int lastIndex = -1;
    							int countOfOpenBracket = 0;
    							for (int j = count+2; j < str.size(); j++) {
    								if (str.get(j).equals("[")) {
    									//nested arrays
    									countOfOpenBracket++;
    								} else if (str.get(j).equals("]")) {
    									if (countOfOpenBracket == 0) {
    										//if this is the corresponding one, break out
    										lastIndex = j;
    										break;
    									} else {
    										//decrement brackets needed
    										countOfOpenBracket--;
    									}
    								}
    							}
    							//get the value inside the array brackets through recursive call
    															       //inclusive //exclusive
    							int index = (int)(recurveEval(str.subList(count + 2,lastIndex)));
    							int[] arr = (arrays.get(i).values);
    							
    							//push array value in and move count to end of this expression
    							values.push((float)arr[index]);
    							count = lastIndex+1;
    							break;
    						}
    					}
    					
    					//do not iterate count
    					continue;
    					
    				} else {
    					//is parenthesis, send recursively and move count to end of that parenthesis
    					//push the resultant value into the values stack
    					//find end parenthesis and evaluate recursive
    					
    					if (oper.equals("(")) {
    						int lastIndex = -1;
    						int countOfOpenParen = 0;
    						
    						//find the corresponding closing parenthesis
    						for (int i = count+1; i < str.size(); i++) {
    							if (str.get(i).equals("(")) {
    								//nested
    								countOfOpenParen++;
    							} else if (str.get(i).equals(")")) {
    								if (countOfOpenParen == 0) {
    									lastIndex = i;
    									break;
    								} else {
    									countOfOpenParen--;
    								}
    							}
    						}
    						
    						//recursive send back the inside parenthesis content
    						float answerInside = recurveEval(str.subList(count + 1,lastIndex));
    						
    						//push answer into stack
    						values.push(answerInside);
    						count = lastIndex+1;
    						continue;
    						
    					}	
    				}
    			}
    		}
    		
    		//When out of while loop, have a stack of numbers and a stack of operations = BASE CASE

    		//if stack of operations is empty, should have one value -- just return the value
    		if (operators.isEmpty() && !values.isEmpty()) {
    			return values.pop();
    		} else {
    			//there are operations to handle
    			
    			//create new stack that will be ordered from left to right -- turned upside down
    			Stack<Float> orderedVal = new Stack<>();
			Stack<String> orderedOper = new Stack<>();
				
			//transfer values from the other stack
			while (!values.isEmpty()) {
				orderedVal.push(values.pop());
			}
			
			//transfer operators from the other stack
			while (!operators.isEmpty()) {
				orderedOper.push(operators.pop());
			}

    			//if there are operators, evaluate
    			while (!orderedVal.isEmpty()) {
    				
    				//when operator stack becomes empty during this while loop
    				if (orderedOper.isEmpty()){
    					//return the value
    					float answer = orderedVal.pop();
    					return answer;
    				}

    				//get the first value and the second
    				float firstVal = orderedVal.pop();
    				float secondVal = orderedVal.pop();
    				
    				//get the operator between those values
    				String betweenOper = orderedOper.pop();
    				String nextOper;
    				
    				//if there is another operator, peek at it
    				if (orderedOper.size() > 0) {
    					nextOper = orderedOper.peek();
    				} else {
    					//no operator to peek at means only one operator -- return answer
    					float answer = 0;
    					switch (betweenOper) {
						case "-":
							answer = firstVal - secondVal;
							break;
						case "+":
							answer = firstVal + secondVal;
							break;
						case "*":
							answer = firstVal * secondVal;
							break;
						case "/":
							answer = firstVal/secondVal; //not integer division
							break;
					}
    					return answer;
    				}
    				
    				//if the operator is - or +
    				if (betweenOper.equals("-") || betweenOper.equals("+")) {
    					//if the next one is also - or +, PEMDAS means do it left to right = just add/subtract
    					if (nextOper.equals("-") || nextOper.equals("+")) {
    						//push back the value
    						switch (betweenOper) {
    							case "-":
    								orderedVal.push(firstVal - secondVal);
    								break;
    							case "+":
    								orderedVal.push(firstVal - secondVal);
    								break;
    						}
    					} else {
    						//keep multiplying or dividing until it hits - or +
    						//then add or subtract back to the first number
    						
    						float overallAnswer = firstVal;
						float multDivAnswer = secondVal;
						
						//peak at nextOper first, then pop it
    						while(nextOper.equals("*") || nextOper.equals("/")) {
    							//getting next ones
    							float thirdVal = orderedVal.pop();
    							nextOper = orderedOper.pop();
    							
    							//multiply or add back to original multiplying answer
    							switch(nextOper) {
    								case "*":
    									multDivAnswer = multDivAnswer*thirdVal;
    									break;
    								case "/":
    									multDivAnswer = multDivAnswer/thirdVal;
    									break;
    							}
    							//only peek if there is a next operator
    							if (orderedOper.size() > 0) {
    								nextOper= orderedOper.peek();
    							} else {
    								//if there is no more operator, done with multiplying/dividing
    								break;
    							}
    						}
    						
    						//broken out of while, put before answer and multiplied answer together and push back
    						switch (betweenOper) {
							case "-":
								orderedVal.push(overallAnswer - multDivAnswer);
								break;
							case "+":
								orderedVal.push(overallAnswer + multDivAnswer);
								break;
						}
    						
    					}
    					
    				} else {
    					//if multiply or divide first, just do it from left to right
    					switch (betweenOper) {
						case "*":
							orderedVal.push(firstVal*secondVal);
							break;
						case "/":
							orderedVal.push(((float)firstVal)/secondVal);
							break;
					}
    				}
    			}
    		}
    		
    		//compiler complains, but with no errors in the expression, it should not reach here
    		return 0;
    	}
    	

    /**
     * Utility method, prints the symbols in the scalars list
     */
    public void printScalars() {
        for (ScalarSymbol ss: scalars) {
            System.out.println(ss);
        }
    }
    
    /**
     * Utility method, prints the symbols in the arrays list
     */
    public void printArrays() {
    		for (ArraySymbol as: arrays) {
    			System.out.println(as);
    		}
    }

}
