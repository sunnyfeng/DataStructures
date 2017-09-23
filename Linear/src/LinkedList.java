import java.util.NoSuchElementException;

public class LinkedList <T> {
	Node<T> front;
	int size;
	
	LinkedList(){
		front = null;
		size = 0;
	}
	
	public void addToFront(T data) {
		front = new Node<T>(data, front);
		size++;
	}
	
	public void traverse(){
		System.out.printf("The LinkedList has %d nodes\n", this.size);
		
		//initialize				//end		//increment
		for (Node<T> ptr = front; ptr != null; ptr = ptr.next) {
			System.out.print(ptr.data + " - > "); //can't use printf because don't know the type
		}
		System.out.println("\\");
	}
	
	public T deleteFront() {
		
		if (front == null) {
			throw new NoSuchElementException("list is empty");
		}
		//get data to be deleted
		T temp = front.data;
		front = front.next;
		size--;
		return temp;
	}
}
	
