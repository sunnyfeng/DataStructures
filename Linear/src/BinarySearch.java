
public class BinarySearch {
	
	public static void main(String[] args) {
		int [] array = {3,10,20,53,70,99};
		System.out.println("Target found at index " + binarySearch(array, 53));
		System.out.println("Target found at index " + binarySearch(array, 55));
		
		int low = 0;
		int high = array.length-1;
		System.out.println("Target found recursively at index " + binarySearchRecursive(array, 53, low, high));
		System.out.println("Target found recursively at index " + binarySearchRecursive(array, 55, low, high));
		
	}
	
	/**
	 * Binary Search for target through sorted array
	 * 
	 * @param array sorted array to search through
	 * @param target target integer to find
	 * @return index of location of target, -1 if not found
	 */
	public static int binarySearch(int [] array, int target) {
		int low = 0;
		int high = array.length-1;
		int middle;
		
		while (low <= high) {
			middle = (low+high)/2;
			if (array[middle] == target) {
				return middle;
			} else {
				if (array[middle] < target) {
					low = middle + 1;
				} else {
					high = middle - 1;
				}
			}
		}
		
		//not found
		return -1;
	}
	
	public static int binarySearchRecursive(int[] array, int target, int low, int high) {
		//base case
		if (low > high) {
			return -1;
		}
		
		//successful finding
		int m = (low+high)/2;
		if (array[m] == target) {
			return m;
		} else {
			//unsuccessful - call itself
			if (array[m] < target) {
				return binarySearchRecursive(array, target, m+1, high);
			} else {
				return binarySearchRecursive(array, target, low, m-1);
			}
		}
	}
	

}
