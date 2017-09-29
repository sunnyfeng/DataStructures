package math;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}

	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		
		BigInteger num = new BigInteger();
		
		//trim integer
		integer = integer.trim();
		
		//if nothing entered, throw illegal argument exception
		if (integer.length() < 1) {
			throw new IllegalArgumentException();
		}
		
		//if the first number is not a digit, check signs
		if (!Character.isDigit(integer.charAt(0))) {
			if (integer.charAt(0) == '-') {
				num.negative = true;
			} else if (integer.charAt(0) == '+') {
				num.negative = false;
			} else {
				throw new IllegalArgumentException();
			}
			integer = integer.substring(1); //trim the sign off the number
		}
		
		//if after sign trimming there are no digits, throw exception
		if (integer.length() < 1) {
			throw new IllegalArgumentException();
		}
		
		//take care of '0's at the front
		while (integer.charAt(0) == '0' && integer.length() > 1) {
			integer = integer.substring(1);
		}
		
		//iterate through the digits backwards (754 -> 4 5 7)
		for (int i = 0 ; i < integer.length(); i++) {
			if (Character.isDigit(integer.charAt(i))) {
				int digit = integer.charAt(i) - '0'; //change to int from char
				num.front = new DigitNode(digit, num.front); //put the values in
				num.numDigits++; //increment number of digits
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		return num;
	}
	
	/**
	 * Adds an integer to this integer, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY this integer.
	 * NOTE that either or both of the integers involved could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param other Other integer to be added to this integer
	 * @return Result integer
	 */
	public BigInteger add(BigInteger other) {
		//add digit by digit
		//if addition of digits > 9, then add one to next addition
		//if one linked list ends and other does not, just keep remaining one's digit
		//three cases: both pos, both neg, and one pos/one neg
		
		//both positive or both negative
		if ((this.negative == false && other.negative == false)
				|| (this.negative == true && other.negative == true)) {
			
			
			//testing to make sure nothing changed
			System.out.println("**ORIGINAL");
			System.out.println("this BigInteger: " + this);
			System.out.println("this numDigits: " + this.numDigits);
			System.out.println("this negative: " + this.negative);
			System.out.println("other BigInteger: " + other);
			System.out.println("other numDigits: " + other.numDigits);
			System.out.println("other negative: " + other.negative);
			
			
			//both positive or both negative = add and make result positive
			DigitNode ptrSmall = this.front;
			DigitNode ptrBig = other.front;
			
			//find the one with more digits
			if (other.numDigits < this.numDigits) {
				ptrSmall = other.front;
				ptrBig = this.front;
			}

			BigInteger addedInt = new BigInteger();
			addedInt.negative = this.negative;
			int carry = 0;
			int addedDigit;
			int smallDigit, bigDigit;
			
			//while the bigger number is still going or there's a carry after the number ends
			while (ptrBig != null || (ptrBig == null && carry != 0)) {
				
				//if ptrSmall is at the end already, treat as 0
				if (ptrSmall == null) {
					smallDigit = 0;
				} else {
					smallDigit = ptrSmall.digit;
				}
				
				//if ptrBig is at end, treat as a 0
				if (ptrBig == null) {
					bigDigit = 0;
				} else {
					bigDigit = ptrBig.digit;
				}
				
				//add digits along with last add's carry
				addedDigit = bigDigit + smallDigit + carry;
				
				//reset carry and make carry 1 if the added digits exceed 9
				carry = 0;
				if (addedDigit > 9) {
					addedDigit = addedDigit%10; //get the remainder if bigger than 10
					carry = 1;
				}
				
				//add new digits to back, not front!
				if (addedInt.numDigits == 0) {
					//if nothing there, just add front
					addedInt.front = new DigitNode(addedDigit, addedInt.front);
				} else {
					//if items inside already, add to the end
					DigitNode ptr = addedInt.front;
					while (ptr.next != null) {
						ptr = ptr.next;
					}
					ptr.next = new DigitNode(addedDigit, null); //add to end
				}
				addedInt.numDigits++; //increment number of digits
				
				//stop moving pointers if at the end already
				if (ptrSmall!= null) {
					ptrSmall = ptrSmall.next;
				}
				
				if (ptrBig!=null) {
					ptrBig = ptrBig.next;
				}
			}
			
			if (addedInt.numDigits == 1 && addedInt.front.digit == 0) {
				addedInt.negative = false;
			}
			
			//testing to make sure nothing changed
			System.out.println("***AFTER");
			System.out.println("this BigInteger: " + this);
			System.out.println("this numDigits: " + this.numDigits);
			System.out.println("this negative: " + this.negative);
			System.out.println("other BigInteger: " + other);
			System.out.println("other numDigits: " + other.numDigits);
			System.out.println("other negative: " + other.negative);
			
			return addedInt;
			
		} else {
			
			
			//testing to make sure nothing changed
			System.out.println("**ORIGINAL");
			System.out.println("this BigInteger: " + this);
			System.out.println("this numDigits: " + this.numDigits);
			System.out.println("this negative: " + this.negative);
			System.out.println("other BigInteger: " + other);
			System.out.println("other numDigits: " + other.numDigits);
			System.out.println("other negative: " + other.negative);
			
			
			//one neg and one pos = subtract
			
			//copy over this BigInteger to prevent modification when borrowing in subtraction
			BigInteger thisInt = new BigInteger();
			thisInt.negative = this.negative;
			for (DigitNode ptrThis = this.front; ptrThis != null; ptrThis = ptrThis.next) {
				//add new digits to back, not front!
				if (thisInt.numDigits == 0) {
					//if nothing there, just add front
					thisInt.front = new DigitNode(ptrThis.digit, thisInt.front);
					thisInt.numDigits++;
				} else {
					//if items inside already, add to back
					DigitNode ptr = thisInt.front;
					while (ptr.next != null) {
						ptr = ptr.next;
					}
					ptr.next = new DigitNode(ptrThis.digit, null); //add to end
					thisInt.numDigits++;
				}
			}
			
			//copy over other BigInteger to prevent modification when borrowing in subtraction
			BigInteger otherInt = new BigInteger();
			otherInt.negative = other.negative;
			for (DigitNode ptrOther = other.front; ptrOther != null; ptrOther = ptrOther.next) {
				//add new digits to back, not front!
				if (otherInt.numDigits == 0) {
					//if nothing there, just add front
					otherInt.front = new DigitNode(ptrOther.digit, otherInt.front);
					otherInt.numDigits++;
				} else {
					//if items inside already
					DigitNode ptr = otherInt.front;
					while (ptr.next != null) {
						ptr = ptr.next;
					}
					ptr.next = new DigitNode(ptrOther.digit, null); //add to end
					otherInt.numDigits++;
				}
			}
			
			//initialize pointers
			DigitNode ptrPos = thisInt.front;
			DigitNode ptrNeg = otherInt.front;
			boolean posBigger = true;
			
			//find the pos and neg one and determine which is bigger
			if (thisInt.negative == true) {
				//if this int is the negative one
				ptrNeg = thisInt.front;
				ptrPos = otherInt.front;
				
				//determine whether positive or negative is bigger number
				if (thisInt.numDigits > otherInt.numDigits) {
					posBigger = false;
				} else if (thisInt.numDigits < otherInt.numDigits) {
					posBigger = true;
				} else {
					boolean sameNumber = true;
					while (ptrNeg != null && ptrPos != null) {
						if (ptrPos.digit > ptrNeg.digit) {
							posBigger = true;
							sameNumber = false;
						} else if (ptrPos.digit < ptrNeg.digit) {
							posBigger = false;
							sameNumber = false;
						} else {
							//do nothing
						}
						ptrNeg = ptrNeg.next;
						ptrPos = ptrPos.next;
					}
					
					if (sameNumber) {
						//they are the same number in the end, order does not matter
						posBigger = true;
					}
				}
				
				//reset pointers with this as negative
				ptrNeg = thisInt.front;
				ptrPos = otherInt.front;
				
			} else {
				//determine whether pos or negative is bigger
				//this is positive, other is negative
				if (thisInt.numDigits > otherInt.numDigits) {
					posBigger = true;
				} else if (thisInt.numDigits < otherInt.numDigits) {
					posBigger = false;
				} else {
					boolean sameNumber = true;
					while (ptrNeg != null && ptrPos != null) {
						if (ptrPos.digit > ptrNeg.digit) {
							posBigger = true;
							sameNumber = false;
						} else if (ptrPos.digit < ptrNeg.digit) {
							posBigger = false;
							sameNumber = false;
						} else {
							//do nothing
						}
						ptrNeg = ptrNeg.next;
						ptrPos = ptrPos.next;
					}
					
					if (sameNumber) {
						//they are the same number, order does not matter
						posBigger = true;
					}
				}
				
				//reset pointers with this being positive
				ptrPos = thisInt.front;
				ptrNeg = otherInt.front;
			}
			
			boolean makeAnswerNegative;
			//set up subtraction
			if (!posBigger) {
				//the negative is bigger, so flip the values, subtract as normal, and make answer negative in the end
				DigitNode temp = ptrNeg;
				ptrNeg = ptrPos;
				ptrPos = temp; //swapped places - ptrPos always the bigger number
				makeAnswerNegative = true;
			} else {
				//do a normal subtraction, answer will be positive in the end
				makeAnswerNegative = false;
			}
			
			//subtract - pos - neg
			BigInteger answer = new BigInteger();
			int bottomDigit = 0;
			int subtracted = 0;
			while (ptrPos != null) { //ptrPos is the bigger number on top
				//when smaller number runs out, assume it is a 0
				if (ptrNeg == null) {
					bottomDigit = 0;
				} else {
					bottomDigit = ptrNeg.digit;
				}
				subtracted = ptrPos.digit - bottomDigit; //top - bottom
				
				if (subtracted < 0) {
					//positive one on top = the one to borrow from
					DigitNode ptrPosOriginal = ptrPos;
					ptrPos = ptrPos.next;
					while (ptrPos.digit == 0 && ptrPos != null) {
						ptrPos.digit = 9;
						ptrPos = ptrPos.next;
					}
					ptrPos.digit--;
					ptrPos = ptrPosOriginal;
					subtracted = ptrPos.digit + 10 - bottomDigit;
				}
				
				//insert in back of linked list
				//add new digits to back, not front!
				if (answer.numDigits == 0) {
					//if nothing there, just add front
					answer.front = new DigitNode(subtracted, answer.front);
				} else {
					//if items inside already
					DigitNode ptr = answer.front;
					while (ptr.next != null) {
						ptr = ptr.next;
					}
					ptr.next = new DigitNode(subtracted, null); //add to end
				}
				answer.numDigits++; //increment number of digits
				
				ptrPos = ptrPos.next;
				if (ptrNeg != null) {
					ptrNeg = ptrNeg.next;
				}
			}
			
			if (makeAnswerNegative) {
				//make answer negative because it was flipped earlier
				answer.negative = true;
			} else {
				answer.negative = false;
			}
			
			//parse 0's out
			int numDigitsOfAnswer = answer.numDigits;
			if (numDigitsOfAnswer > 1) {
				for (int i = answer.numDigits; i > 0; i--) {
					DigitNode ptr = answer.front;
					if (numDigitsOfAnswer > 2) {
						for (int j = 0; j < i; j++) {
							if (ptr.next.next == null) {
								break;
							}
							ptr = ptr.next;
						}
						
						if (ptr.next.digit == 0) {
							ptr.next = null;
							numDigitsOfAnswer--;
						} else {
							break;
						}
					} else {
						//only two linked list items
						if (ptr.next.digit == 0) {
							ptr.next = null;
							numDigitsOfAnswer--;
						}
						break;
					}
				}
			}
			answer.numDigits = numDigitsOfAnswer;
			
			//testing to make sure nothing changed
			System.out.println("***AFTER");
			System.out.println("this BigInteger: " + this);
			System.out.println("this numDigits: " + this.numDigits);
			System.out.println("this negative: " + this.negative);
			System.out.println("other BigInteger: " + other);
			System.out.println("other numDigits: " + other.numDigits);
			System.out.println("other negative: " + other.negative);
			
			
			return answer;
		}
	}
	
	
	/**
	 * Returns the BigInteger obtained by multiplying the given BigInteger
	 * with this BigInteger - DOES NOT MODIFY this BigInteger
	 * 
	 * @param other BigInteger to be multiplied
	 * @return A new BigInteger which is the product of this BigInteger and other.
	 */
	public BigInteger multiply(BigInteger other) {

		BigInteger answer = new BigInteger();
		answer = parse("0"); //start with 0
		DigitNode thisPtr = this.front;
		DigitNode otherPtr = other.front;
		int place = 0;
		int carry = 0;
		
		while (thisPtr != null) {
			BigInteger lineStorage = new BigInteger();

			while (otherPtr != null) {
				//get the line
				int line = thisPtr.digit*otherPtr.digit+carry;
				if (line > 9) {
					carry = line/10;
					line = line%10;
				} else {
					carry = 0;
				}
				
				//add new digits to back, not front!
				if (lineStorage.numDigits == 0) {
					//if nothing there, just add front
					lineStorage.front = new DigitNode(line, lineStorage.front);
				} else {
					//if items inside already
					DigitNode ptr = lineStorage.front;
					while (ptr.next != null) {
						ptr = ptr.next;
					}
					ptr.next = new DigitNode(line, null); //add to end
				}
				lineStorage.numDigits++; //increment number of digits
				
				otherPtr = otherPtr.next;
			}
		
			
			//add extra 0 to front (LSB) for what line of multiplication is
			int count = place;
			while (count > 0) {
				//add to front
				lineStorage.front = new DigitNode(0, lineStorage.front);
				lineStorage.numDigits++; //increment number of digits
				count--;
			}
			
			//if carry still there, add to end (MSB)
			if (carry > 0) {
				//add to end
				DigitNode ptr = lineStorage.front;
				while (ptr.next != null) {
					ptr = ptr.next;
				}
				ptr.next = new DigitNode(carry, null); //add to end
				lineStorage.numDigits++;
			}
			
			lineStorage = BigInteger.parse(lineStorage.toString());
			answer = answer.add(lineStorage);
			
			//add stored line to answer
			place++;
			carry = 0;
			thisPtr = thisPtr.next;
			otherPtr = other.front;
		}
		
		if ((this.negative == true && other.negative == true)
				|| (this.negative == false && other.negative == false)) {
			 answer.negative = false;
		} else {
			answer.negative = true;
		}
		
		//make 0 positive
		if (answer.numDigits == 1 && answer.front.digit == 0) {
			answer.negative = false;
		}
		
		return answer;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		
		return retval;
	}
	
}
