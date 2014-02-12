package org.mumineen.util;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

public class SalaatTimes {

	private SalaatTimes() {}
	
	
	private static SalaatTimes instance = null;
	public static SalaatTimes getInstance() {
		if(instance == null) {
			instance = new SalaatTimes();
		}
		return instance;
	}
	
	public enum SalaatTimeName {SIHORI, FAJR, SUNRISE, ZAWAAL, ZUHR, ASR, MAGHRIB};
	
	public Map<SalaatTimeName, DateTime> GetTimesInTz(int year, int month, int day, DateTimeZone tz, double lat, double lon) {
		Map<SalaatTimeName, DateTime> salaatTimesInUtc = GetTimesUtc(year, month, day, tz, lat, lon);
		Map<SalaatTimeName, DateTime> salaatTimesInTz = new HashMap<SalaatTimeName, DateTime>();
		for (Map.Entry<SalaatTimeName, DateTime> entry : salaatTimesInUtc.entrySet())
		{
		    salaatTimesInTz.put(entry.getKey(),  entry.getValue().withZone(tz));
		}

		return salaatTimesInTz;
	}
	
	private Map<SalaatTimeName, DateTime> GetTimesUtc(int year, int month, int day, DateTimeZone tz, double lat, double lon) {
	
		Map<SalaatTimeName, DateTime> SalaatTimes = new HashMap<SalaatTimeName, DateTime>();
		long trise[]=new long[1];
		long tset[]=new long[1];
		
		
		/*int dawnDuskResult = */SunriseSunset.getInstance().DawnAndDusk(year, month, day, tz, lat, lon, trise, tset);
		DateTime sunrise =  convertLongToDateTime(year, month, day, trise[0]);
		DateTime maghrib = convertLongToDateTime(year,month,day, tset[0]);
		/*int nauticalResult = */SunriseSunset.getInstance().NauticalDawnAndDusk(year, month, day, tz, lat, lon, trise, tset);
		DateTime fajr = GetFajrFromNauticalDawn(year, month, day, trise[0]);
		DateTime zawaal = GetZawaal(sunrise, maghrib);
		DateTime zuhr = GetZuhr(zawaal, sunrise, maghrib);
		DateTime asr = GetAsr(zuhr, sunrise, maghrib);
		DateTime sihori = GetSihori(sunrise);
		
		SalaatTimes.put(SalaatTimeName.SIHORI, sihori);
		SalaatTimes.put(SalaatTimeName.FAJR, fajr);
		SalaatTimes.put(SalaatTimeName.SUNRISE, sunrise);
		SalaatTimes.put(SalaatTimeName.ZAWAAL, zawaal);
		SalaatTimes.put(SalaatTimeName.ZUHR, zuhr);
		SalaatTimes.put(SalaatTimeName.ASR, asr);
		SalaatTimes.put(SalaatTimeName.MAGHRIB, maghrib);
		return SalaatTimes;
	}
	
	
	
	
	private DateTime GetSihori(DateTime sunrise) {
		return sunrise.minusMinutes(75);
	}

	private DateTime GetAsr(DateTime zuhr, DateTime sunrise, DateTime maghrib) {
		return zuhr.plusMillis((int) (2*GetDayGhari(sunrise, maghrib)));
	}

	private DateTime GetZuhr(DateTime zawaal, DateTime sunrise, DateTime maghrib) {
		return zawaal.plusMillis((int) (2*GetDayGhari(sunrise, maghrib)));
	}

	private int GetDayGhari(DateTime sunrise, DateTime maghrib) {
		Duration dayLength = new Duration(sunrise, maghrib);
		return (int) (dayLength.getMillis()/12.0);
	}
	private int GetDayLength(DateTime sunrise, DateTime maghrib) {
		Duration dayLength = new Duration(sunrise, maghrib);
		return (int) dayLength.getMillis();
	}

	private DateTime GetZawaal(DateTime sunrise, DateTime maghrib) {
		return sunrise.plusMillis(GetDayLength(sunrise, maghrib)/2);
	}

	private DateTime GetFajrFromNauticalDawn(int year, int month, int day,
			long msse) {
		
		DateTime dtUtc = convertLongToDateTime(year, month, day, msse);
		return dtUtc.plusMinutes(15);
	}

	private DateTime convertLongToDateTime(int year, int month, int day,
			long msse) {
		
		return new DateTime(msse, DateTimeZone.UTC); 
	}



	@SuppressWarnings("unused")
	private static String getDateString(int year, int month, int day) {
		return String.format("%04d-%02d-%02d", year,month,day);
	}
}
