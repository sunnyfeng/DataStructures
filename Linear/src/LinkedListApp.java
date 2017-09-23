import java.util.NoSuchElementException;

public class LinkedListApp {

	public static void main(String[] args) {
		
		LinkedList<String> powerRangers = new LinkedList<>();
		powerRangers.addToFront("Dino charge");
		powerRangers.addToFront("Jungle fury");
		powerRangers.addToFront("Ninja storm");
		powerRangers.traverse();
		
		LinkedList<Double> math = new LinkedList<>();
		math.addToFront(3.141592);
		math.addToFront(2.71828);
		math.traverse();
		
		LinkedList<Integer> numberOfComputers = new LinkedList<Integer>();
		try {
			numberOfComputers.deleteFront(); //exception will be thrown
		} catch(NoSuchElementException e) {
			System.out.println("Oooops, list empty");			
		}


	}

}
