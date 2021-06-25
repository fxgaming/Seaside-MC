package by.fxg.seaside.systems.jobs;

import by.fxg.seaside.systems.businesses.EnumBusiness;

public enum EnumJob {
	NONE(false, 0),
	LICENSOR(false, 0), 			//Лицензиар
	MECHANIC(false, 0),				//Автомеханик
	TAXIDRIVER(false, 0),			//Таксист
	PIZZADELIVERYMAN(false, 0),		//Доставщик пиццы
	TRUCKDRIVER(true, 0),			//Дальнобойщик
	BUILDER(true, 0),				//Строитель
	AUTODEALER(true, 0),			//Автодиллер
	FARMER(false, 0), 				//Фермер
	COLLECTOR(true, 100), 			//Инкассатор
	LAWYER(true, 0),				//Адвокат
	
	
	;
	public boolean isChangeable;
	public int salary;
	EnumJob(boolean a, int b) {
		this.isChangeable = a;
		this.salary = b;
	}
	
	public static EnumJob getByID(int id) {
		for (EnumJob value : values()) {
			if (value.ordinal() == id) {
				return value;
			}
		}
		return null; 
	}
}
