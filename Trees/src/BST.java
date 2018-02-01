import java.util.NoSuchElementException;

//generic binary search tree data structure
//duplicates not allowed
//Interface: Comparable - requires objects to implement compareTo (compares two values)
public class BST<T extends Comparable<T>> {
	
	BSTNode<T> root;
	int size;
	
	BST(){
		root = null;
		size = 0;
	}
	
	/**
	 * Best case O(logn), Worst case O(n)
	 * @param key
	 */
	public void delete(T key) {
		
		//find node to be deleted = search
		BSTNode<T> p = null;
		BSTNode<T> x = root;
		int c = 0; //comparator
		while (x != null ) {
			c = key.compareTo(x.key);
			//c = 0, found!
			if (c == 0) {
				break;
			}
			p = x;
			
			//if c < 0, x goes left, else x goes right
			x = (c < 0) ? x.left : x.right;
		}
		
		if (x == null) {
			//not found!
			throw new NoSuchElementException("Item not found");
		}
		
		BSTNode<T> y = null; //in order predecessor
		
		//case 3: handle node with 2 children
		if (x.left != null && x.right != null) {
			//find in order predecessor - make a left then a right
			y = x.left;
			p = x; //pointing to parent
			
			while (y.right != null) {
				p = y;
				y = y.right;
			}
			
			//copy y's data into x's data
			x.key = y.key;
			
			//prepare to delete y (which must either be a leaf or have only a left child)
			x = y; //other ones are using x to delete, deleting as if with other case
		}
		
		//check if x is the root and that it does not have two children
		if (p == null) {
			root = (x.left != null) ? x.left : x.right;
			return;
		}
		
		//handle case 1 (leaf) and 2 (node with 1 child)
		BSTNode<T> tmp = (x.right != null) ? x.right : x.left; //null if leaf
		if (x == p.left) {
			//left branch, disconnect
			p.left = tmp; //skips over the number if one child, makes it null if no children
		} else {
			p.right = tmp;
		}
		
		
	}
	
	public void insert(T key) {
		//search until failure
		BSTNode<T> ptr = root;
		BSTNode<T> prev = null;
		
		int c = 0; //use to compare
		while (ptr != null) {
			c = key.compareTo(ptr.key); //c = 0 if equal
			if (c == 0) {
				throw new IllegalArgumentException(key + " already in BST");
			} else {
				//increment ptr and prev
				prev = ptr;
				if (c < 0) {
					//insert key is less than ptr key = go left
					ptr = ptr.left;
				} else {
					//to be inserted key is bigger than current node = go right
					ptr = ptr.right;
				}
			}
		}
		
		//now insert key at this prev position
		BSTNode<T> node = new BSTNode<T>(key, null, null); //node is leaf, both subtrees empty
		
		//if tree is empty
		if (prev == null && ptr == null) {
			//the new node is the root since it is the first one
			root = node;
		} else if (c<0) {
			//if new key value is less than last node
			prev.left = node;
		} else {
			//if new key value greater than last node
			prev.right = node;
		}
		size++;
		
	}
	
	public BSTNode<T> search(T key) {
		BSTNode<T> ptr = root;
		int c = 0;
		while (ptr != null) {
			c = key.compareTo(ptr.key); //c = 0 if equal
			if (c == 0) {
				//key found
				return ptr;
			} else {
				if (c < 0) {
					//key less than ptr key = go left
					ptr = ptr.left;
				} else {
					//key is bigger than current node = go right
					ptr = ptr.right;
				}
			}
		}
		
		//out of loop means not found
		return null;
	}
	
	//recursive in order traversal
	public static <T extends Comparable<T>> void traverseInOrder(BSTNode<T> root){
		//print in sorted manner
		
		//base 
		if (root == null) {
			//stop here, go back and execute stacked functions
			return;
		}
		
		//go left, print, go all the way right
		traverseInOrder(root.left);
		System.out.print(root.key + " -> ");
		traverseInOrder(root.right);
	}

}
