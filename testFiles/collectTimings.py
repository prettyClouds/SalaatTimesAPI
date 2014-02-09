import requests
import json
import sys

season = sys.argv[1]
if season == 'w':
    date = "2000-12-21"

elif season == 's':
    date = "2000-06-21"

lat = -65.0
lng = 0.0
if len(sys.argv) == 3:
    lng = float(sys.argv[2])

API = "http://api.mumineen.org/salaat"
#?lat=%f&lng=%f&timezone=%s&start=%s&end=%s" 

f = open('MDO_timings_%s_lon%.3f.txt' %(date, lng), 'w')

#f.write("%s,%s,%s\n" %("fajr","sunrise","maghrib"))
for x in range(-1,26):
    f.write("%.3f, %.3f, %s\n" %(lat,lng,date))
    #formatString %(lat + 5*x, lng, timeZone, date, date)
    r = requests.get(API,params={"lat": lat, "lng": lng, "timezone":"Etc/UTC", "start":date, "end":date })
    #print r.json()
    f.write("%s,%s,%s\n" %(r.json()[date]["fajr"],
                           r.json()[date]["sunrise"],
                           r.json()[date]["maghrib"]))
    lat += 5.0

f.close()    