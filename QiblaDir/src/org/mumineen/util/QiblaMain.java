package org.mumineen.util;

public class QiblaMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double MECCA_LAT = 21.422480;
		double MECCA_LON = 39.826170;
		double delta = .0005;
		
		System.out.println("north");
		System.out.println(QiblaDir.GetInstance().getDirection(MECCA_LAT + delta, MECCA_LON));
		
		//northeast

		System.out.println("northeast");
		System.out.println(QiblaDir.GetInstance().getDirection(MECCA_LAT + delta, MECCA_LON + delta));
		
		
		//east

		System.out.println("east");
		System.out.println(QiblaDir.GetInstance().getDirection(MECCA_LAT, MECCA_LON + delta));
		
		//southeast
		System.out.println("southeast");
		System.out.println(QiblaDir.GetInstance().getDirection(MECCA_LAT-delta, MECCA_LON + delta));
		
		
		//south
		System.out.println("south");
		System.out.println(QiblaDir.GetInstance().getDirection(MECCA_LAT - delta, MECCA_LON));
		
		//southwest
		System.out.println("southwest");
		System.out.println(QiblaDir.GetInstance().getDirection(MECCA_LAT - delta, MECCA_LON-delta));
		
		
		//west
		System.out.println("west");
		System.out.println(QiblaDir.GetInstance().getDirection(MECCA_LAT, MECCA_LON-delta));
		
		//northwest
		System.out.println("northwest");
		System.out.println(QiblaDir.GetInstance().getDirection(MECCA_LAT+delta, MECCA_LON-delta));
		
		System.out.println("Bakersfield, CA");
		System.out.println(QiblaDir.GetInstance().getDirection(35.8333, -119.0166));
		
		System.out.println("Bakersfield 20, CA");
		System.out.println(QiblaDir.GetInstance().getDirection(35.8333, -100.0166));
		
		System.out.println("Bakersfield 40, CA");
		System.out.println(QiblaDir.GetInstance().getDirection(35.8333, -80.0166));
	}

}
