
public class DLLApp {
	
	public static void main(String[] args) {
		DLL<String> shoes = new DLL<>();
		shoes.addToFront("Vans");
		shoes.addToFront("Crocs");
		shoes.addToFront("Nike");
		shoes.addAfter("Merrels","Crocs");
		shoes.traverse();
		shoes.delete("Nike");
		shoes.traverse();
	}

}
