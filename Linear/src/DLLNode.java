
public class DLLNode<T> {
	public T data;
	public DLLNode<T> previous;
	public DLLNode<T> next;
	
	DLLNode(T data, DLLNode<T> previous, DLLNode<T>next){
		this.data = data;
		this.previous = previous;
		this.next = next;
	}
	
	public T data() {
		return data;
	}
	
	public DLLNode<T> previous(){
		return previous;
	}
	
	public DLLNode<T> next(){
		return next;
	}
	
	
}
