
public class HeapSort {
	public static void main(String[] args) {
		
		int[] n = {20, 3, 1, 4, 15};
		Heap<Integer> heap = new Heap<Integer>(Heap.MIN_HEAP);
		
		// build phase
		for ( int i = 0; i < n.length; i++ ) {
			heap.insert(n[i]);
		}
		
		// sort phase
		int i = 0;
		while ( !heap.isEmpty() ) {
			n[i] = heap.delete();
			i += 1;
		}
		
		for ( i = 0; i < n.length; i++) {
			System.out.print(n[i] + " ");
		}
	}
}
