
//Binary search tree generic node
public class BSTNode<T> {
	
	//fields
	T key;
	BSTNode<T> left;  //left child/subtree
	BSTNode<T> right; //right child/subtree
	
	//constructor
	BSTNode(T key, BSTNode<T> left, BSTNode<T> right){
		this.key = key;
		this.left = left;
		this.right = right;
	}

}
