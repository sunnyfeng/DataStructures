
public class DLL <T> {
	
	DLLNode<T> front;
	int size;
	
	public DLL() {
		front = null;
		size = 0;
	}
	
	public void addToFront(T data) {
		DLLNode<T> node = new DLLNode<T>(data, null, front);
		if (front != null) {
			front.previous = node;
		}
		front = node;
		size++;
	}
	
	public void addAfter(T data, T target) {
		DLLNode<T> ptr = front;
		while (ptr != null && !ptr.data.equals(target)) {
			ptr = ptr.next;
		}
		
		if (ptr == null) {
			System.out.println("Item not found");
			return;
		} else {
			DLLNode<T> node = new DLLNode<T>(data, ptr, ptr.next);
			if (ptr.next != null) {
				ptr.next.previous = node;
			}
			ptr.next = node;
		}
		size++;
		
	}
	
	public void traverse() {
		DLLNode<T> ptr = front;
		while (ptr != null) {
			System.out.print(ptr.data + " - > ");
			ptr = ptr.next;
		}
		System.out.println("");
	}
	
	public T delete(T target) {
		DLLNode<T> ptr = front;
		while (ptr != null && !ptr.data.equals(target)) {
			ptr = ptr.next;
		}
		if (ptr != null) {
			//found target
			T temp = (T) ptr.data;
			//what if one time
			if (ptr.previous == null && ptr.next == null) {
				front = null;
				size--;
				return temp;
			}
			
			if (ptr == front) {
				ptr.next.previous = null;
				front = ptr.next;
			}
			if (ptr.previous != null) {
				ptr.previous.next = ptr.next;
			}
			
			if (ptr.next != null) {
				ptr.next.previous = ptr.previous;
			}
			size--;
			return temp;
		} else {
			System.out.println("Item not found");
			return null;
		}
	}
	
	

}
