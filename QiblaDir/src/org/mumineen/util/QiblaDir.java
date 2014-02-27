package org.mumineen.util;

import org.joda.time.DateTime;
import org.orekit.data.ClasspathCrawler;
import org.orekit.data.DataProvider;
import org.orekit.data.DataProvidersManager;
import org.orekit.errors.OrekitException;
import org.orekit.models.earth.GeoMagneticElements;
import org.orekit.models.earth.GeoMagneticField;
import org.orekit.models.earth.GeoMagneticFieldFactory;

public class QiblaDir {
	double MECCA_LAT = 21.422480;
	double MECCA_LON = 39.826170;
	public static QiblaDir GetInstance() {
		if(instance == null) {
			instance = new QiblaDir();
		}
		return instance;
	}
	private static QiblaDir instance;
	
	public double getDirection(double lat, double lon) {
		//final File home = new File(System.getProperty("user.home"));
		//final File orekitDir = new File(home, "orekit-data");
		//System.out.println(orekitDir.toString());
		DataProvider provider = null;
		try {
			provider = new ClasspathCrawler("WMM.COF", "IGRF.COF");//new DirectoryCrawler(orekitDir);
		} catch (OrekitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataProvidersManager.getInstance().addProvider(provider);
		double trueDir = getTrueDirection(lat, lon);
		return getMagneticDirection(trueDir, lat, lon);
		//return getMagneticDirection(trueDir);
	}

	private double getTrueDirection(double lat, double lon) {
		double numerator = sind(MECCA_LON-lon);
		double denominator = cosd(lat)*tand(MECCA_LAT)-sind(lat)*cosd(lon-MECCA_LON);
		return atan2(numerator,denominator);
		
	}

	private double getMagneticDirection(double trueDir, double lat, double lon) {
		DateTime dt = DateTime.now();
		double year = GeoMagneticField.getDecimalYear(dt.getDayOfMonth(), dt.getMonthOfYear(), dt.getYear());
		GeoMagneticField model = null;
		try {
			//System.out.println("year");
			//System.out.println(year);
			
			model = GeoMagneticFieldFactory.getWMM(year);
		} catch (OrekitException e) {
			e.printStackTrace();
		}
		GeoMagneticElements result = model.calculateField(lat, lon, 0);
		//System.out.println("lat: " + lat + ',' + " lon: " + (lon+360)%360);
		//System.out.println("trueDir: " + trueDir);
		//System.out.println("declination: " + result.getDeclination());
		return (trueDir - result.getDeclination()+360) %360;
	}
	
	
	private double RADEG= 180.0/Math.PI;
	private double DEGRAD= Math.PI/180.0;
	
	private double sind(double degrees) {return Math.sin(degrees*DEGRAD);}

	private double cosd(double degrees) {return Math.cos(degrees*DEGRAD);} 
	private double tand(double degrees) {return Math.tan(degrees*DEGRAD);} 
	
	private double atan2(double length1, double length2) {return RADEG*Math.atan2(length1, length2);}
	
}
