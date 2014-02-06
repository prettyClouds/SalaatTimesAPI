import requests
import json


API = "http://api.mumineen.org/salaat"
#?lat=%f&lng=%f&timezone=%s&start=%s&end=%s" 
lat = -65.0
lng = 0.0
date = "2000-12-21"
f = open('%s_MDO_timings.txt' %(date), 'w')


for x in range(-1,26):
    f.write("%f, %f, %s\n" %(lat,lng,date))
    #formatString %(lat + 5*x, lng, timeZone, date, date)
    r = requests.get(API,params={"lat": lat, "lng": lng, "timezone":"Etc/UTC", "start":date, "end":date })
    
    f.write("%s, %s\n" %(r.json()[date]["sunrise"],r.json()[date]["maghrib"]))
    lat += 5.0

f.close()    