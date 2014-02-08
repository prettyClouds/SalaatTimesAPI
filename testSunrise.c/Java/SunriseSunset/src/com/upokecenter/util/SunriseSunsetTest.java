package com.upokecenter.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class SunriseSunsetTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		double lat = -65.0;
		double lon = 0;
		int year = 2000;
		int month = 06;
		int day = 21;
		String fileNameFormat = "JavaSunriseTest_%04d-%02d-%02d";
		String fileName = String.format(fileNameFormat, year,month,day);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		for (int i = 0; i<27; i++) {
			double trise[]=new double[1];
			double tset[]=new double[1];
			double nauticalTwilightArray[] = new double[1];
			double junk[] = new double[1];
			// Get sunrise times
			SunriseSunset.__sunriset__(year, // year
					month,  // month
					day, // day
					lon,  
					lat,  
					-35.0/60.0,
					true,trise,tset);
			
			SunriseSunset.__sunriset__(year, // year
					month,  // month
					day, // day
					lon, 
					lat,  
					-12,
					false,nauticalTwilightArray,junk);
			
			double sunrise = trise[0];
			double sunset = tset[0];
			double nauticalTwilight = nauticalTwilightArray[0];
			int nauticalTwilightOffset = 15*60 + 10;
			if (nauticalTwilight < .25) {
				nauticalTwilight = .25;
				nauticalTwilightOffset = 0;
			}
			
			writer.println(String.format("%f, %f, %04d-%02d-%02d",
											lat, lon, year, month, day));
			writer.println(String.format("%s, %s, %s",
											convertDoubleToTime(nauticalTwilight, nauticalTwilightOffset),//fajrStart[0], 15*60 + 6),
											convertDoubleToTime(sunrise, 10),
											convertDoubleToTime(sunset, 10)));
			lat += 5;
	    }
		
		writer.close();
	}
	
	private static String convertDoubleToTime (double numHours, int offset) {
		
		int remainingSeconds = (int)(numHours*3600) + offset;
		int hours = remainingSeconds/3600;
		remainingSeconds -= hours*3600;
		int minutes = remainingSeconds / 60;
		remainingSeconds -= minutes*60;
		int seconds = remainingSeconds;
		return String.format("%02d:%02d:%02d", hours,minutes,seconds);
	}
    

}
