package by.fxg.seaside.systems.pickup;

import by.fxg.seaside.systems.jobs.EnumJob;

public enum EnumPickup {
	NONE,
	HOUSE,
	HOUSE_GARAGE,
	BUSINESS,
	
	//4-5
	INTERIOR_ENTER,
	INTERIOR_EXIT,
	
	//6-7 системное
	GET_PASSPORT,				//получить пасспорт
	GET_BANK_CARD,				//получить банковскую карту
	//бизнесы
	INSURANCE_AGENCY,			//страховка
	GAS_STATION,				//АЗС(Газовая ёбля)
	BARBERSHOP,					//Парикмахерская
	PLASTIC_SURGERY,			//Пластическая хирургия(смена вшености)
	PHARMACY,					//АПТЭКА
	PIZZASTALL,					//Стойка с пиццей для бомжев!
	
	//работа
	JOB_CLAIM,  				//устройство
	JOB_CHANGE, 				//смена
	JOB_PAYMENT,				//зп если работа уебанская)
	
	JOB_GET, 					//взять
	JOB_SET, 					//дать
	JOB_WORK, 					//работать :D

	
	;
	
	public static EnumPickup getByID(int id) {
		for (EnumPickup value : values()) {
			if (value.ordinal() == id) {
				return value;
			}
		}
		return null;
	}
}
