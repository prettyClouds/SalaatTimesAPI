SalaatTimes.jar
====

dependencies
=
  You'll need the joda time library to 1) pass a timezone into the method to get salaat times 2) retrieve and manipulate the DateTime object you get back. You can get them at: http://sourceforge.net/projects/joda-time/files/joda-time/

location
=
  ./testSunrise.c/Java/SunriseSunset/SalaatTimes.jar


usage
=
  SalaatTimes st = SalaatTimes.getInstance();

  Map\<SalaatTimesName, DateTime\> results = st.GetTimesInTz(year, month, day, DateTimeZone.forID("EST"), lat, lon);

  results.get(SalaatTimesName.SIHORI).toString("HH:mm:ss");

  results.get(SalaatTimesName.FAJR).toString("HH:mm:ss");

  results.get(SalaatTimesName.SUNRISE).toString("HH:mm:ss");

  results.get(SalaatTimesName.ZAWAAL).toString("HH:mm:ss");

  results.get(SalaatTimesName.ZUHR).toString("HH:mm:ss");

  results.get(SalaatTimesName.ASR).toString("HH:mm:ss");

  results.get(SalaatTimesName.MAGHRIB).toString("HH:mm:ss");
