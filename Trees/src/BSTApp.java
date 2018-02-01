
public class BSTApp {
	
	public static void main (String[] args) {
		BST<Integer> bst = new BST<Integer>();
		bst.insert(10);
		bst.insert(7);
		bst.insert(5);
		bst.insert(15);
		bst.insert(13);
		bst.insert(20);
		//BST.traverseInOrder(bst.root);
		//System.out.println();
		
		BST<Integer> bst2 = new BST<Integer>();
		bst2.insert(20);
		bst2.insert(10);
		bst2.insert(5);
		bst2.insert(15);
		bst2.insert(13);
		bst2.insert(25);
		bst2.insert(30);
		bst2.insert(26);
		BST.traverseInOrder(bst2.root);
		bst2.delete(10);
		System.out.println();
		BST.traverseInOrder(bst2.root);
		
	}

}
