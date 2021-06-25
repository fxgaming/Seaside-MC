package by.fxg.seaside.systems.businesses;

public enum EnumBusiness {
	NONE,
	AUTOSCHOOL,
	AUTOSERVICE, MECHANIC_RENT,
	TAXI_RENT,
	PIZZASTALL, PIZZABIKE_RENT,
	TRUCK_RENT, OILSTATION, FACTORY,
	SHOP24BY7, GASSTATION,
	
	INSURANCE_AGENCY,
	SHOP_CLOTHES, SHOP_ACESSORIES,
	BARBERSHOP,
	PLASTIC_SURGERY,
	PHARMACY,
	
	
	;
	
	public static EnumBusiness getByID(int id) {
		for (EnumBusiness value : values()) {
			if (value.ordinal() == id) {
				return value;
			}
		}
		return null;
	}
}
