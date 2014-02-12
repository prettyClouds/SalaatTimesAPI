SalaatTimesAPI
==============

This repository is dedicated to building libraries that will support Namaaz Time and Miqaat Calendar applications for mumineen.

SalaatTimes.jar
====

Dependencies
==
You'll need the joda time library to 1) pass a timezone into the method to get salaat times 2) retrieve and manipulate the DateTime object you get back. You can get them at: http://sourceforge.net/projects/joda-time/files/joda-time/

location
==
./testSunrise.c/Java/SunriseSunset/SalaatTimes.jar


usage
==
SalaatTimes st = SalaatTimes.getInstance();\n
Map<String, DateTime> results = st.GetTimesInTz(year, month, day, DateTimeZone.forID("EST"), lat, lon);\n
results.get("sihori").toString("HH:mm:ss");\n
results.get("fajr").toString("HH:mm:ss");\n
results.get("zawaal").toString("HH:mm:ss");\n
results.get("zuhr").toString("HH:mm:ss");
results.get("asr").toString("HH:mm:ss");
results.get("sunrise").toString("HH:mm:ss"),;
results.get("maghrib").toString("HH:mm:ss")));
