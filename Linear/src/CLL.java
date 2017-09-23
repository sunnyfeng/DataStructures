
public class CLL<T> {
	
	//all fields should be private
	private Node<T> tail; // points to last element of CLL
	private int size;
	
	//initialize CLL to an empty one
	public CLL() {
		tail = null;
		size = 0;
	}
	
	public void addToFront(T data) {
		
		Node<T> node = new Node<T>(data, null);
		if (tail == null) {
			//empty CLL
			tail = node;
			node.next = node;
		} else {
			//not empty CLL
			node.next = tail.next;
			tail.next = node;
		}
		size++;
	}
	
	/**
	 * Best case: target is first node: O(1)
	 * Worst case: target not found/ last element: O(n)
	 * 
	 * @param target target to find
	 * @return target node
	 */
	public Node<T> search (T target) {
		//start at tail.next and go up to tail
		Node<T> ptr = tail.next;
		do {
			if (ptr.data.equals(target)) {
				return ptr; 
			} else {
				ptr = ptr.next;
			}
		} while(ptr != tail.next);
		
		System.out.println("Target " + target + " not found");
		return null;
	}
	
	/**
	 * Efficiency: O(1) -- constant
	 */
	public void deleteFront() {
		//only one element - delete that element
		if(tail == null || tail == tail.next) {
			tail = null;
			size = 0;
		} else {
			//tail point to second element
			tail.next = tail.next.next;
			size--;
		}
		
	}
	

}
