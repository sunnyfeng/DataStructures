//generic type
public class Node<T> {
	T data; //type T
	Node<T> next; //Type T node - placeholder for type we wish to use
	
	Node (T data, Node<T> next){
		this.data = data;
		this.next = next;
	}
}
