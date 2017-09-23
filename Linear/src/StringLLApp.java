
public class StringLLApp {
	
	public static void main (String[] args) {
		StringLL shoppingList = new StringLL();
		shoppingList.addToFront("Milk");
		shoppingList.addToFront("Cereal");
		shoppingList.addToFront("Bananas");
		shoppingList.addToFront("Apples");
		shoppingList.traverse();
		
		StringNode target = shoppingList.search("Cereal");
		if (target != null) {
			System.out.printf("Found target %s\n", target.data);
		}
		
		StringLL iceCream = new StringLL();
		iceCream.addToFront("Chocolate");
		iceCream.addToFront("Rum raisin");
		iceCream.addToFront("Mint Chocolate Chip");
		iceCream.traverse();
	}
}
