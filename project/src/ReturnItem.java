

public class ReturnItem {

	
		//attributes
		public int itemID;
		public int daysSinceReturn;
		
		public ReturnItem(int itemID,int daysSinceReturn)
		{
			this.itemID = itemID; this.daysSinceReturn = daysSinceReturn;
		}
		
		public int getItemID(){return itemID;}
		public int getDays(){return daysSinceReturn;}
}
