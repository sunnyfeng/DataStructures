
public class StringLL {
	//fields
	StringNode front; //just a reference if no "new" declaration
	int size;
	
	StringLL(){
		//initialize
		front = null;
		size = 0;
	}
	
	public void addToFront(String data) {
		//added one item, increase size
		front = new StringNode(data, front);
		size++;
	}
	
	public void traverse(){
		System.out.printf("The LinkedList has %d nodes\n", size);
		
			//initialize				//end		//increment
		for (StringNode ptr = front; ptr != null; ptr = ptr.next) {
			System.out.printf("%s -> ", ptr.data);
		}
		System.out.println("\\ \n"); //end slash
	}
	
	public StringNode search(String target){
		for (StringNode ptr = front; ptr != null; ptr = ptr.next) {
			if(ptr.data.equals(target)) {
				return ptr;
			}
		}
		return null;
	}
}

