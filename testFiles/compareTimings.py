import sys




def readFile(filename):

    with open(filename, 'r') as f:
         read_data = f.readlines()
    f.closed    
    return read_data
    
        
        
def getSec(s):
    l = s.split(':')
    seconds = int(l[0]) * 3600 + int(l[1]) * 60 
    if len(l) >= 3:
        seconds += int(l[2])
    return seconds    
    
if __name__ == '__main__':
    testFile = sys.argv[1]
    baseFile = sys.argv[2]

    testFileData = readFile(testFile);
    baseFileData = readFile(baseFile)
    counter = 1
    f = open("comparison.txt", 'w')
    for x in range(1, 28):
        testSunrise = getSec(testFileData[counter].split(',')[0])
        testSunset = getSec(testFileData[counter].split(',')[1])
        baseSunrise = getSec(baseFileData[counter].split(',')[0])
        baseSunset = getSec(baseFileData[counter].split(',')[1])
        f.write( "%d,%d\n" %(baseSunrise - testSunrise, baseSunset - testSunset) )
        counter += 2 
    f.close()
        
        