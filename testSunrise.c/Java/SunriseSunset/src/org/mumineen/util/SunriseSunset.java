/*
Based on code from Sunriset.c, which was
released to the public domain by Paul Schlyter, December 1992
 */
/* Converted to Java and C# by Peter O., 2013. */

package org.mumineen.util;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;


public final class SunriseSunset {

	private double RADEG= 180.0/Math.PI;
	private double DEGRAD= Math.PI/180.0;
	private double INV360 = 1.0/360.0;


	private static SunriseSunset instance = null;

	public static SunriseSunset getInstance() {
		if (instance == null) {
			instance = new SunriseSunset();
		}
		return instance;
	}


	public int DawnAndDusk( int year, int month, int day, DateTimeZone tz, double lat, double lon,
			long[] trise, long[] tset )
	{
		double altitude = -35.0/60.0;
		return this.rise_set_altitude(year, month, day, tz, lon, lat, altitude, true, trise, tset);

	}

	public int NauticalDawnAndDusk( int year, int month, int day, DateTimeZone tz, double lat, double lon,
			long []trise, long []tset )
	{
		double altitude = -12.0;

		return this.rise_set_altitude(year, month, day, tz, lon, lat, altitude, false, trise, tset);

	}

	private double GMST0( double d )
	{

		/*******************************************************************/
		/* This function computes GMST0, the Greenwich Mean Sidereal Time  */
		/* at 0h UT (i.e. the sidereal time at the Greenwhich meridian at  */
		/* 0h UT).  GMST is then the sidereal time at Greenwich at any     */
		/* time of the day.  I've generalized GMST0 as well, and define it */
		/* as:  GMST0 = GMST - UT  --  this allows GMST0 to be computed at */
		/* other times than 0h UT as well.  While this sounds somewhat     */
		/* contradictory, it is very practical:  instead of computing      */
		/* GMST like:                                                      */
		/*                                                                 */
		/*  GMST = (GMST0) + UT * (366.2422/365.2422)                      */
		/*                                                                 */
		/* where (GMST0) is the GMST last time UT was 0 hours, one simply  */
		/* computes:                                                       */
		/*                                                                 */
		/*  GMST = GMST0 + UT                                              */
		/*                                                                 */
		/* where GMST0 is the GMST "at 0h UT" but at the current moment!   */
		/* Defined in this way, GMST0 will increase with about 4 min a     */
		/* day.  It also happens that GMST0 (in degrees, 1 hr = 15 degr)   */
		/* is equal to the Sun's mean longitude plus/minus 180 degrees!    */
		/* (if we neglect aberration, which amounts to 20 seconds of arc   */
		/* or 1.33 seconds of time)                                        */
		/*                                                                 */
		/*******************************************************************/

		double sidtim0;
		/* Sidtime at 0h UT = L (Sun's mean longitude) + 180.0 degr  */
		/* L = M + w, as defined in sunpos(). */
		sidtim0 = revolution( (180.0 + 356.0470 + 282.9404) +
				(0.9856002585 + 4.70935E-5) * d );
		return sidtim0;
	}  /* GMST0 */

	private double rev180( double x )
	/*********************************************/
	/* Reduce angle to within +180..+180 degrees */
	/*********************************************/
	{
		return( x - 360.0 * Math.floor( x * INV360 + 0.5 ) );
	}  /* revolution */

	private double revolution( double x )
	/*****************************************/
	/* Reduce angle to within 0..360 degrees */
	/*****************************************/
	{
		return( x - 360.0 * Math.floor( x * INV360 ) );
	}  /* revolution */

	private double[] sun_RA_dec( double d)
	{
		double lon, obl_ecl, x, y, z;

		/* Compute Sun's ecliptical coordinates */
		double lon_r[]=sunpos( d );
		lon=lon_r[0];
		/* Compute ecliptic rectangular coordinates (z=0) */
		x = lon_r[1] * Math.cos(DEGRAD*lon);
		y = lon_r[1] * Math.sin(DEGRAD*lon);

		/* Compute obliquity of ecliptic (inclination of Earth's axis) */
		obl_ecl = 23.4393 - 3.563E-7 * d;

		/* Convert to equatorial rectangular coordinates - x is unchanged */
		z = y * Math.sin(DEGRAD*obl_ecl);
		y = y * Math.cos(DEGRAD*obl_ecl);

		/* Convert to spherical coordinates */
		double RA = RADEG*Math.atan2( y, x );
		double dec = RADEG*Math.atan2( z, Math.sqrt(x*x + y*y) );
		return new double[]{RA,dec,lon_r[1]};
	}  /* sun_RA_dec */

	private double[] sunpos( double d )
	/******************************************************/
	/* Computes the Sun's ecliptic longitude and distance */
	/* at an instant given in d, number of days since     */
	/* 2000 Jan 0.0.  The Sun's ecliptic latitude is not  */
	/* computed, since it's always very near 0.           */
	/******************************************************/
	{
		double M,         /* Mean anomaly of the Sun */
		w,         /* Mean longitude of perihelion */
		/* Note: Sun's mean longitude = M + w */
		e,         /* Eccentricity of Earth's orbit */
		E,         /* Eccentric anomaly */
		x, y,      /* x, y coordinates in orbit */
		v;         /* True anomaly */

		/* Compute mean elements */
		M = revolution( 356.0470 + 0.9856002585 * d );
		w = 282.9404 + 4.70935E-5 * d;
		e = 0.016709 - 1.151E-9 * d;

		/* Compute true longitude and radius vector */
		E = M + e * RADEG * Math.sin(DEGRAD*M) * ( 1.0 + e * Math.cos(DEGRAD*M) );
		x = Math.cos(DEGRAD*E) - e;
		y = Math.sqrt( 1.0 - e*e ) * Math.sin(DEGRAD*E);
		double r = Math.sqrt( x*x + y*y );              /* Solar distance */
		v = RADEG*Math.atan2( y, x );                  /* True anomaly */
		double lon = v + w;                        /* True solar longitude */
		if ( lon >= 360.0 ) {
			lon -= 360.0;                   /* Make it 0..360 degrees */
		}
		return new double[]{lon, r};
	}


	/**
	 * Note: timestamp = unixtimestamp (NEEDS to be 00:00:00 UT)
	 *       Eastern longitude positive, Western longitude negative       
	 *       Northern latitude positive, Southern latitude negative       
	 *       The longitude value IS critical in this function!            
	 *       altit = the altitude which the Sun should cross              
	 *               Set to -35/60 degrees for rise/set, -6 degrees       
	 *               for civil, -12 degrees for nautical and -18          
	 *               degrees for astronomical twilight.                   
	 *         upper_limb: non-zero -> upper limb, zero -> center         
	 *               Set to non-zero (e.g. 1) when computing rise/set     
	 *               times, and to zero when computing start/end of       
	 *               twilight.                                            
	 *        *rise = where to store the rise time                        
	 *        *set  = where to store the set  time                        
	 *                Both times are relative to the specified altitude,  
	 *                and thus this function can be used to compute       
	 *                various twilight times, as well as rise/set times   
	 * Return value:  0 = sun rises/sets this day, times stored at        
	 *                    *trise and *tset.                               
	 *               +1 = sun above the specified "horizon" 24 hours.     
	 *                    *trise set to time when the sun is at south,    
	 *                    minus 12 hours while *tset is set to the south  
	 *                    time plus 12 hours. "Day" length = 24 hours     
	 *               -1 = sun is below the specified "horizon" 24 hours   
	 *                    "Day" length = 0 hours, *trise and *tset are    
	 *                    both set to the time when the sun is at south.  
	 *                                                                    
	 */
	private int rise_set_altitude(int year, 
			int month, 
			int day, 
			DateTimeZone tz, 
			double lon, 
			double lat, 
			double altit, 
			boolean upper_limb, 
			long []ts_rise, 
			long []ts_set)
	{
		double d,  /* Days since 2000 Jan 0.0 (negative before) */
		sr,         /* Solar distance, astronomical units */
		sRA,        /* Sun's Right Ascension */
		sdec,      /* Sun's declination */
		sradius,    /* Sun's apparent radius */
		t,          /* Diurnal arc */
		tsouth,     /* Time when Sun is at south */
		sidtime;    /* Local sidereal time */


		int rc = 0; /* Return cde from function - usually 0 */

		/*
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		cal.set(year, month, day, 0, 0);
		long msseUtc = cal.getTimeInMillis();
		 */
		LocalDate dtInit = new LocalDate(getDateString(year, month, day));
		DateTime dtUtc = dtInit.toDateTimeAtStartOfDay(DateTimeZone.UTC);
		//System.out.println(dtUtc);
		long msseUtc = dtUtc.getMillis();

		long msseTz = dtInit.toDateTimeAtStartOfDay(tz).plusHours(12).getMillis();

		/*
		cal = new GregorianCalendar(TimeZone.getTimeZone(tz.getID()));
		cal.set(year, month, day, 12, 0);
		long msseTz = cal.getTimeInMillis();
		 */

		//DateTime dtTz = dtInit.toDateTimeAtStartOfDay(DateTimeZone.UTC);
		/*
		System.out.println("millisecond comparison");
		System.out.println(msseUtc);
		System.out.println(msseTz);
		 */

		/*
		/* Compute d of 12h local mean solar time */
		//timestamp = t_loc->sse;

		d = timelib_ts_to_juliandate(msseTz/1000.0) - lon/360.0;

		/* Compute local sidereal time of this moment */
		sidtime = revolution(GMST0(d) + 180.0 + lon);

		/* Compute Sun's RA + Decl at this moment */
		double ra_dec_sr[] =sun_RA_dec( d  );
		sRA=ra_dec_sr[0];
		sdec=ra_dec_sr[1];
		sr=ra_dec_sr[2];

		/* Compute time when Sun is at south - in hours UT */
		tsouth = 12.0 - rev180(sidtime - sRA) / 15.0;

		/* Compute the Sun's apparent radius, degrees */
		sradius = 0.2666 / sr;

		/* Do correction to upper limb, if necessary */
		if (upper_limb) {
			altit -= sradius;
		}

		/* Compute the diurnal arc that the Sun traverses to reach */
		/* the specified altitude altit: */

		double cost;
		cost = (sind(altit) - sind(lat) * sind(sdec)) / (cosd(lat) * cosd(sdec));

		if (cost >= 1.0) {
			rc = -1;
			t = 0.0;       /* Sun always below altit */

			ts_rise[0] = ts_set[0] = (long) (msseUtc + (tsouth * 3600 * 1000));
		} else if (cost <= -1.0) {
			rc = +1;
			t = 12.0;      /* Sun always above altit */

			ts_rise[0] = msseUtc;
			ts_set[0]  = (msseUtc + (24 * 3600)*1000);
		} else {
			t = acosd(cost) / 15.0;   /* The diurnal arc, hours */

			/* Store rise and set times - as Unix Timestamp */


			ts_rise[0] =  (long) ( (tsouth - t) * 3600*1000 ) + msseUtc;
			ts_set[0]  = (long) ( (tsouth + t) * 3600*1000 ) + msseUtc;

		}


		/*
		System.out.println(String.format("d: %f",  d));
		System.out.println(String.format("sr: %f",  sr));
		System.out.println(String.format("sRA: %f",  sRA));
		System.out.println(String.format("sdec: %f",  sdec));
		System.out.println(String.format("sradius: %f",  sradius));
		System.out.println(String.format("t: %f",  t));
		System.out.println(String.format("tsouth: %f",  tsouth));
		System.out.println(String.format("sidtime: %f",  sidtime));
		System.out.println(String.format("cost: %f",  cost));
		System.out.println(String.format("trise: %d",  ts_rise[0]));
		System.out.println(String.format("tset: %d\n",  ts_set[0]));
		 */
		return rc;
	}

	private double timelib_ts_to_juliandate(double ts)
	{
		double tmp;

		tmp = ts;
		tmp /= 86400.0;
		tmp += 2440587.5;
		tmp -= 2451543.0;

		return tmp;
	}

	private String getDateString(int year, int month, int day) {
		return String.format("%04d-%02d-%02d", year,month,day);
	}

	private double sind(double degrees) {return Math.sin(degrees*DEGRAD);}

	private double cosd(double degrees) {return Math.cos(degrees*DEGRAD);} 
	
	private double acosd(double length) {return RADEG*Math.acos(length);}
	
	private SunriseSunset(){}

}