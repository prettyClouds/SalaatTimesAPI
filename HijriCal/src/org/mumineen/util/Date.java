package org.mumineen.util;

public class Date{

	public Date(int year2, int month2, int day2) {
		year = year2;
		month = month2;
		day = day2;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}

	private int year;
	private int month;
	private int day;
	
	 @Override public String toString() {
		 return String.format("%04d-%02d-%02d", year, month, day);
	 }
}