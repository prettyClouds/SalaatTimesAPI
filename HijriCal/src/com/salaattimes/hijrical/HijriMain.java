package com.salaattimes.hijrical;

import java.util.ArrayList;
import java.util.List;

public class HijriMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Date> dates = new ArrayList<Date>();
		
		dates.add(new Date(1888,8,4));
		dates.add(new Date(1965,11,12));
		
		dates.add(new Date(1915,3,6));
		dates.add(new Date(2014,2,17));

		dates.add(new Date(1946, 8, 20));
		
		dates.add(new Date(2014,2,9));
	
		for (Date date : dates) {
			System.out.println(String.format("%s,%s", 
											date.toString(),
											HijriCal.getInstance().ConvertGregorianToHijri(date).toString()));
		}
		
	}

}
