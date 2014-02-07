package com.upokecenter.util;

public class SunriseSunsetTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		double lon = -65.0;
		double lat = 0;
		int year = 2000;
		int month = 6;
		int day = 21;
		 String fileNameFormat = "JavaSunriseTest_%04d-%02d-%02d";
		 String fileName = String.format(fileNameFormat, year,month,day);
		
		 
		 
		for (int i = 0; i<27; i++) {
			double trise[]=new double[1];
			double tset[]=new double[1];
			// Get sunrise times
			int t=SunriseSunset.__sunriset__(year, // year
					month,  // month
					day, // day
					lat,  //lat
					lon,  //lon
					-35.0/60.0,
					true,trise,tset);
			lon += 5;
			System.out.println(String.format("%f, %f, %04d-%02d-%02d",
											lon, lat, year, month, day));
			System.out.println(String.format("%s, %s",
											convertDoubleToTime(trise[0]),
											convertDoubleToTime(tset[0])));
	    }

	}
	
	private static String convertDoubleToTime (double numHours) {
		
		int remainingSeconds = (int)(numHours*3600) +6;
		int hours = remainingSeconds/3600;
		remainingSeconds -= hours*3600;
		int minutes = remainingSeconds / 60;
		remainingSeconds -= minutes*60;
		int seconds = remainingSeconds;
		return String.format("%02d:%02d:%02d", hours,minutes,seconds);
	}
    

}
