package org.mumineen.util;

public class HijriCal {

	private HijriCal() {};

	private static HijriCal instance = null;
	public static HijriCal getInstance() {
		if(instance == null)
			instance = new HijriCal();
		return instance;
	}
	// number of days in the year per month
	int daysInYear[] = {30, 59, 89, 118, 148, 177, 207, 236, 266, 295, 325, 355};

	// number of days in 30-years per year
	int [] daysIn30Years = {
			354,  708, 1063, 1417, 1771, 2126, 2480, 2834,  3189,  3543,
			3898, 4252, 4606, 4961, 5315, 5669, 6024, 6378,  6732,  7087,
			7441, 7796, 8150, 8504, 8859, 9213, 9567, 9922, 10276, 10631
	};

	// return new Hijri Date object associated with specified Gregorian date
	public Date ConvertGregorianToHijri(int year, int month, int day) {

		return ConvertAjdToHijri(ConvertGregorianToAjd(new Date(year, month, day)));
	};

	public Date ConvertHijriToGregorian(int year, int month, int day ) {
		return ConvertAjdToGregorian(ConvertHijriToAjd(new Date(year, month, day)));
	}

	private Date ConvertAjdToHijri(double ajd) 
	{
		// return new Hijri Date object associated with specified Astronomical Julian Day number

		int year, month, day, i = 0;
		int left = (int) Math.floor(ajd - 1948083.5);
		int y30 = (int) Math.floor(left / 10631.0);

		left -= y30*10631;
		while (left > daysIn30Years[i]) 
			i += 1; 

		year = (int) Math.round(y30*30.0 + i);
		if (i > 0)
			left -= daysIn30Years[i - 1];

		i = 0;
		while (left > daysInYear[i]) 
			i += 1;

		month = Math.round(i + 1);
		if (i > 0) {
			day = Math.round(left - daysInYear[i-1]);
		} else {
			day = Math.round(left);
		}

		return new Date(year, month, day);
	}

	// Astronomical Julian Day number associated with specified Gregorian date
	private double ConvertGregorianToAjd (Date date) {

		int a, b;

		int year = date.getYear();
		int month = date.getMonth();
		int day = date.getDay();

		if (month < 3) {
			year--;
			month += 12;
		}

		if (isJulian(date)) {
			b = 0;
		} else {
			a = (int)Math.floor(year / 100);
			b = (2 - a + (int)Math.floor(a / 4));
		}
		return Math.floor(365.25*(year + 4716))
				+ Math.floor(30.6001*(month + 1)) + day + b - 1524.5;
	}

	private double ConvertHijriToAjd(Date date) {
		int y30 =(int) Math.floor(date.getYear() / 30.0);
		double ajd = 1948083.5 + y30*10631 + dayOfYear(date);
		if (date.getYear() % 30 != 0) {
			ajd += daysIn30Years[date.getYear() - y30*30 - 1];
		}
		return ajd;
	}

	private int dayOfYear(Date date) {
		if (date.getMonth() == 1) {
			return date.getDay();
		}
		return daysInYear[date.getMonth() - 2] + date.getDay();
	}

	@SuppressWarnings("unused")
	private Date ConvertAjdToGregorian(double ajd) {
		double a, b, c, d, e, f, z, alpha,
		year, month, day, hrs, min, sec, msc;
		z = Math.floor(ajd + 0.5);
		f = (ajd + 0.5 - z);
		if (z < 2299161) {
			a = z;
		} else {
			alpha = Math.floor((z - 1867216.25) / 36524.25);
			a = z + 1 + alpha - Math.floor(alpha / 4);
		}
		b = a + 1524;
		c = Math.floor((b - 122.1) / 365.25);
		d = Math.floor(365.25 * c);
		e = Math.floor((b - d) / 30.6001);

		day = b - d - Math.floor(30.6001 * e) + f;
		hrs = (day - Math.floor(day)) * 24;
		min = (hrs - Math.floor(hrs)) * 60;
		sec = (min - Math.floor(min)) * 60;
		msc = (sec - Math.floor(sec)) * 1000;
		month = (e < 14) ? (e - 2) : (e - 14);
		year = (month < 2) ? (c - 4715) : (c - 4716);
		return new Date((int)year, (int)month + 1, (int)day);
	}

	private boolean isJulian(Date date) {
		// is the specified date a Julian Date (ie. before 5 October 1582)?
		if (date.getYear() < 1582) {
			return true;
		} else if (date.getYear() == 1582) {
			if (date.getMonth() < 10) {
				return true;
			} else if (date.getMonth() == 10) {
				if (date.getDay() < 5) {
					return true;
				}
			}
		}
		return false;
	}


}



/*(function () { "use strict";

var hijrical = {};


hijrical.data = {
    monthNames: {
        // long month names
        long: [
            null,
            'Moharram al-Haraam',
            'Safar al-Muzaffar',
            'Rabi al-Awwal',
            'Rabi al-Aakhar',
            'Jumada al-Ula',
            'Jumada al-Ukhra',
            'Rajab al-Asab',
            'Shabaan al-Karim',
            'Ramadaan al-Moazzam',
            'Shawwal al-Mukarram',
            'Zilqadah al-Haraam',
            'Zilhaj al-Haraam'
        ],
        // short month names
        short: [
            null,
            'Moharram',
            'Safar',
            'Rabi I',
            'Rabi II',
            'Jumada I',
            'Jumada II',
            'Rajab',
            'Shabaan',
            'Ramadaan',
            'Shawwal',
            'Zilqadah',
            'Zilhaj'
        ]
    },


};

// utility functions
hijrical.utils = {

    // Gregorian date associated with specified Astronomical Julian Day number
    ajdToGregorian: function (ajd) {
        "use strict";
        var a, b, c, d, e, f, z, alpha,
            year, month, day, hrs, min, sec, msc;
        z = Math.floor(ajd + 0.5);
        f = (ajd + 0.5 - z);
        if (z < 2299161) {
            a = z;
        } else {
            alpha = Math.floor((z - 1867216.25) / 36524.25);
            a = z + 1 + alpha - Math.floor(alpha / 4);
        }
        b = a + 1524;
        c = Math.floor((b - 122.1) / 365.25);
        d = Math.floor(365.25 * c);
        e = Math.floor((b - d) / 30.6001);

        day = b - d - Math.floor(30.6001 * e) + f;
        hrs = (day - Math.floor(day)) * 24;
        min = (hrs - Math.floor(hrs)) * 60;
        sec = (min - Math.floor(min)) * 60;
        msc = (sec - Math.floor(sec)) * 1000;
        (e < 14) ? (month = e - 2) : (month = e - 14);
        (month < 2) ? (year = c - 4715) : (year = c - 4716);
        return moment([year, month + 1, day, hrs, min, sec, msc]);
    }
};

// Hijri date object 
hijrical.Date = function (day, month, year) {
    "use strict";
    this.day = day;
    this.month = month;
    this.year = year;

    // day of the year
    this.dayOfYear = function () {
        if (this.month == 1) {
            return this.day;
        }
        return hijrical.data.daysInYear[this.month - 2] + this.day;
    };

    // Astronomical Julian Day number associated with this Hijri date
    this.ajd = function () {
        var y30 = Math.floor(this.year / 30.0),
            ajd = 1948083.5 + y30*10631 + this.dayOfYear();
        if (this.year % 30 != 0) {
            ajd += hijrical.data.daysIn30Years[this.year - y30*30 - 1];
        }
        return ajd;
    };

    // Gregorian date associated with this Hijri date
    this.gregorian = function () {
        return hijrical.utils.ajdToGregorian(this.ajd());
    };
};

// is the specified year a Kabisa year?
hijrical.Date.isKabisa = function (year) {
    "use strict";
    var i, remainders = [2, 5, 8, 10, 13, 16, 19, 21, 24, 27, 29];
    for (i in remainders) {
        if (year % 30 == remainders[i]) {
            return true;
        }
    }
    return false;
};

// number of days in specified month and year
hijrical.Date.daysInMonth = function (month, year) {
    "use strict";
    if ((month == 12) && (hijrical.Date.isKabisa(year)) || (month % 2 == 1)) {
        return 30;
    }
    return 29;
};

/


// export as AMD module / Node module / browser variable

if (typeof define === 'function' && define.amd) {
    define(hijrical);
} else if (typeof module !== 'undefined') {
    module.exports = hijrical;
} else {
    window.hijrical = hijrical;
}

}());
 */