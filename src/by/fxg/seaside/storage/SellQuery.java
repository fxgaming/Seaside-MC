package by.fxg.seaside.storage;

import by.fxg.seaside.utils.SQType;

public class SellQuery {
	public SQType type;
	public String seller;
	public String buyer;
	public int price;
	public int extData0;
	public int extData1;
	
	public SellQuery(SQType type, String seller, String buyer, int price, int e0, int e1) {
		this.type = type;
		this.seller = seller;
		this.buyer = buyer;
		this.price = price;
		this.extData0 = e0;
		this.extData1 = e1;
	}
}
