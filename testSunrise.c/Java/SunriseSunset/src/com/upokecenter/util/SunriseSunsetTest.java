package com.upokecenter.util;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

public class SunriseSunsetTest {

	/**
	 * @param args
	 */
	
	private static String getDateString(int year, int month, int day) {
		return String.format("%04d-%02d-%02d", year,month,day);
	}
	public static void main(String[] args) {
		
		double lat = 42.5;
		double lon = -83;
		int year = 2000;
		int month = 06;
		int day = 21;
		String dateString = getDateString(year, month, day);
		String fileNameFormat = "JavaSunriseTest_%s_lon%.3f.txt";
		String fileName = String.format(fileNameFormat, dateString, lon);
		System.out.println(fileName);
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
		
	for (int i = 0; i<1; i++) {

			List<DateTime> results = getTimings(year, month, day, lat, lon);
			
			writer.println(String.format("%.3f, %.3f, %04d-%02d-%02d",
											lat, lon, year, month, day));
			writer.println(String.format("%s, %s, %s",
										results.get(0).toString("HH:mm:ss"),
										results.get(1).toString("HH:mm:ss"),
										results.get(2).toString("HH:mm:ss")));
			
			System.out.println(String.format("%.3f, %.3f, %04d-%02d-%02d",
					lat, lon, year, month, day));
			System.out.println(String.format("%s, %s, %s",
											results.get(0).toString("HH:mm:ss"),
											results.get(1).toString("HH:mm:ss"),
											results.get(2).toString("HH:mm:ss")));
				lat += 5;
	    }
		
		writer.close();
	}
	
	private static List<DateTime> getTimings(int year, int month, int day, double lat,double lon) {
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
	
		LocalDate dtUtcInit = new LocalDate(getDateString(year, month, day));
		List<DateTime> results = new ArrayList<DateTime>();
		DateTime dtUtc = dtUtcInit.toDateTimeAtStartOfDay(DateTimeZone.UTC);
		DateTime sunriseUTC = dtUtc.plusSeconds((int)(trise[0]*3600)).plusSeconds(10);
		DateTime sunsetUTC = dtUtc.plusSeconds((int)(tset[0]*3600)).plusSeconds(10);
		DateTime fajrUTC = dtUtc.plusSeconds((int)(nauticalTwilightArray[0]*3600)).plusMinutes(15).plusSeconds(10);
		results.add(fajrUTC.withZone(DateTimeZone.getDefault()));
		results.add(sunriseUTC.withZone(DateTimeZone.getDefault()));
		results.add(sunsetUTC.withZone(DateTimeZone.getDefault()));
		
		
		return results;
		/*if (nauticalTwilight < .25) {
			nauticalTwilight = .25;
			nauticalTwilightOffset = 0;
		}*/
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
