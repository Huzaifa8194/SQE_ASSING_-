

public class Item {
	
	//attributes
	public int itemID;
	public String itemName;
	public float price;
	public int amount; //amount of items on inventory
	
	//methods
	public Item(int itemID,String itemName, float price, int amount)
	{
		this.itemID = itemID; this.itemName = itemName; this.price = price; this.amount = amount;
	}
	
	String getItemName() {return itemName;}
	int getItemID() {return itemID;}
	float getPrice() {return price;}
	int getAmount() {return amount;} 
	
	public void updateAmount(int amount) {this.amount = amount;}
					
}
