import java.util.NoSuchElementException;

public class Stack<T> {
	Node<T> front;
	int size;
	
	Stack(){
		front = null;
		size = 0;
	}
	
	public void push(T item) {
		front = new Node<T>(item, front);
		size++;
	}
	
	public T pop() {
		if (front == null) {
			throw new NoSuchElementException("Stack is empty");
		} else {
			T tmp = front.data;
			front = front.next;
			size--;
			return tmp;
		}
	}
	
	public T peek() {
		if (front == null) {
			return null;
		} else {
			return front.data;
		}
	}
	
	public boolean isEmpty() {
		if (front == null) {
			return true;
		} else {
			return false;
		}
	}

}
