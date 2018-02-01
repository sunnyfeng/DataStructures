
public class IntLL {
	
	public static void main (String[] args) {
		IntNode L = null;
		L = addToFront(L,5); //adding new value 
		L = addToFront(L,4);
		L = addToFront(L,3);
		/*
		 * Looks like L -> 3 -> 4 -> 5
		 */
		traverse(L);
		L = deleteFront(L); //delete front
		traverse(L);
		L = addToFront(L,2); //add 2 to the front
		traverse(L);
		addAfter(L,5,6);
		addAfter(L,2,3);
		traverse(L);
		L = delete(L,5);
		traverse(L);
	}
	
	/**
	 * Adding new value to the front of the linkedlist and changing handle.
	 * @param front the address of the old first unit
	 * @param data the new value to be added
	 * @return the address of the new first unit
	 */
	public static IntNode addToFront(IntNode front, int data) {
		//puts node in front and links to the old first unit
		return new IntNode(data, front);
	}
	
	/**
	 * Travel through and print linked list nodes
	 * @param front starting pointer (handle)
	 */
	public static void traverse(IntNode front) {
		System.out.println("Traversing...");
		
		/* while loop
		IntNode ptr = front; //reference to traverse the linked list
		//ends when at the last object and the next link = null
		while (ptr != null) {
			System.out.print(ptr.data + " -> "); //print out the data
			ptr = ptr.next; //put pointer at next object
		}
		*/
		
		//for loop
		for (IntNode ptr = front; ptr != null; ptr=ptr.next) {
			System.out.println(ptr.data);
		}
	}
	
	/**
	 * Makes the next node the first node
	 * @param front the previous pointer (handle)
	 * @return the new pointer (handle)
	 */
	public static IntNode deleteFront(IntNode front) {
		System.out.println("Deleting " + front.data + 
				" and moving first node to " + front.next.data + "...");
		return front.next; //updated to the address of second node
	}
	
	public static void addAfter(IntNode front, int target, int data) {
		IntNode ptr = front;
		while (ptr!= null && ptr.data != target) { //get out if either one happens
			ptr = ptr.next;
		}
		//find target number to go after or the end of the linkedlist
		if (ptr != null) {
			//found target
			IntNode node = new IntNode(data, ptr.next);
			ptr.next = node;
		} else {
			System.out.println(target + " not found");
		}
	} 
	
	public static IntNode delete(IntNode front, int target) {
		
		//if target is the front
		IntNode ptr = front;
		IntNode prev = null;
		
		//way number 1 = with two pointers
		while (ptr != null && ptr.data != target) {
			prev = ptr;
			ptr = ptr.next;
		}
			
		if (ptr == null){
			return front;
		} else if (ptr == front) {
			return front.next;
		} else {
			prev.next = ptr.next;
			return front;
		}
		
	}
}
