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
		
		//if the first number is not a digit
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
		
		//traverse - DELETE THIS LATER: FOR TESTING ONLY
		System.out.println("Parsing result");
		System.out.println("Number of Digits: " + num.numDigits);
		for (DigitNode ptr = num.front; ptr != null; ptr = ptr.next) {
			System.out.print(ptr.digit + " - > "); //can't use printf because don't know the type
		}
		System.out.println();
		
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
			//both positive = add and make result positive
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
			
			while (ptrBig != null || (ptrBig == null && carry != 0)) {
				//if ptrSmall is at the end already, pretend there's a 0
				if (ptrSmall == null) {
					smallDigit = 0;
				} else {
					smallDigit = ptrSmall.digit;
				}
				
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
					//if items inside already
					DigitNode ptr = addedInt.front;
					while (ptr.next != null) {
						ptr = ptr.next;
					}
					ptr.next = new DigitNode(addedDigit, null); //add to end
				}
				addedInt.numDigits++; //increment number of digits
				
				//don't move pointer if at the end already
				if (ptrSmall!= null) {
					ptrSmall = ptrSmall.next;
				}
				
				if (ptrBig!=null) {
					ptrBig = ptrBig.next;
				}
			}
			
			//DELETE LATER
			System.out.println("Adding result: ");
			System.out.println("Number of Digits: " + addedInt.numDigits);
			for (DigitNode ptr = addedInt.front; ptr != null; ptr = ptr.next) {
				System.out.print(ptr.digit + " - > ");
			}
			System.out.println();
			
			return addedInt;
		} else {
			//one neg and one pos = subtract
			
			System.out.println("HEY I GOT TO POINT 1");
			
			//copy over this and other since it will be modified during borrowing in subtraction
			///////****THIS IS NOT COPYING OVER****///////
			BigInteger thisInt = new BigInteger();
			DigitNode end = thisInt.front;
			for (DigitNode ptrThis = this.front; ptrThis != null; ptrThis = ptrThis.next) {
				//add new digits to back, not front!
				if (thisInt.numDigits == 0) {
					//if nothing there, just add front
					end = new DigitNode(ptrThis.digit, end);
				} else {
					//if items inside already
					end.next = new DigitNode(ptrThis.digit, null); //add to end
					end = end.next;
				}
				thisInt.numDigits++; //increment number of digits
			}
			System.out.println("THIS IS: ");
			for (DigitNode ptrThis = thisInt.front; ptrThis != null; ptrThis = ptrThis.next) {
				System.out.print(ptrThis.digit + " -> ");
			}
			System.out.println();
			
			BigInteger otherInt = new BigInteger();
			for (DigitNode ptrOther = other.front; ptrOther != null; ptrOther = ptrOther.next) {
				//add new digits to back, not front!
				if (otherInt.numDigits == 0) {
					//if nothing there, just add front
					otherInt.front = new DigitNode(ptrOther.digit, this.front);
				} else {
					//if items inside already
					DigitNode ptr = otherInt.front;
					while (ptr.next != null) {
						ptr = ptr.next;
					}
					ptr.next = new DigitNode(ptrOther.digit, null); //add to end
				}
				otherInt.numDigits++; //increment number of digits
			}
			
			System.out.println("HEY I GOT TO POINT 2");
			
			DigitNode ptrPos = thisInt.front;
			DigitNode ptrNeg = otherInt.front;
			boolean posBigger = true;
			boolean thisIntIsPositive = true;
			
			//find the pos and neg one and determine which is bigger
			if (this.negative == true) {
				ptrNeg = this.front;
				ptrPos = other.front;
				thisIntIsPositive = false;
				
				//determine whether pos or negative is bigger
				if (this.numDigits > other.numDigits) {
					posBigger = false;
				} else if (this.numDigits < other.numDigits) {
					posBigger = true;
				} else {
					while(ptrNeg.digit == ptrPos.digit && ptrNeg != null) {
						ptrNeg = ptrNeg.next;
						ptrPos = ptrPos.next;
					}
					if (ptrNeg == null) {
						//they are the same number, order does not matter
						posBigger = true;
					} else {
						if (ptrNeg.digit > ptrPos.digit) {
							posBigger = false;
						} else {
							posBigger = true;
						}
					}
				}
				
				//reset pointers
				ptrNeg = this.front;
				ptrPos = other.front;
				
			} else {
				//determine whether pos or negative is bigger
				if (this.numDigits > other.numDigits) {
					posBigger = true;
				} else if (this.numDigits < other.numDigits) {
					posBigger = false;
				} else {
					while(ptrNeg.digit == ptrPos.digit && ptrNeg != null) {
						ptrNeg = ptrNeg.next;
						ptrPos = ptrPos.next;
					}
					if (ptrNeg == null) {
						//they are the same number, order does not matter
						posBigger = true;
					} else {
						if (ptrNeg.digit > ptrPos.digit) {
							posBigger = false;
						} else {
							posBigger = true;
						}
					}
				}
				
				//reset pointers
				ptrPos = thisInt.front;
				ptrNeg = otherInt.front;
				thisIntIsPositive = true;
			}
			
			System.out.println("HEY I GOT TO POINT 3");
			
			//set up subtraction
			if (!posBigger) {
				//the negative is bigger, so flip the values, subtract as normal, and make answer negative in the end
				DigitNode temp = ptrNeg;
				ptrNeg = ptrPos;
				ptrPos = temp; //swapped places - ptrPos always the bigger number
				if (thisIntIsPositive) {
					thisIntIsPositive = false;
				} else {
					thisIntIsPositive = true;
				}
			} else {
				//do a normal subtraction, answer will be positive in the end
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
					subtracted = ptrPos.digit + 10 - bottomDigit;
					ptrPos = ptrPosOriginal;
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
			
			if (!posBigger) {
				//make answer negative because it was flipped earlier
				answer.negative = true;
			} else {
				answer.negative = false;
			}
			
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
