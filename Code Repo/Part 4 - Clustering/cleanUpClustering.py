
def numClusters( num ):
	myNum = num.split(" ")
	newNum = myNum[0].split("=")
	return newNum[1]

from decimal import Decimal	


def addToDictionary( index, score, topTwenty = {} ):
	if( len(topTwenty) < 20 ):
		if( score > 0 ):
			topTwenty[index] = score
	else:
		if( score > 0 ):
			lowIndex = 0
			lowValue = 1000
			
			
			for key in topTwenty.keys():
				if( lowValue > topTwenty.get(key) ):
					lowIndex = key
					lowValue = topTwenty.get(key)
			
			if( score > lowValue ):
				del topTwenty[lowIndex]
				topTwenty[index] = score
	

with open("clusterInfo.txt", "w") as outfile:
	with open("clustering_maybeFinal.txt", "r") as file:
		for line in file:
			if( line[0] == 'V' ):
				cluster = line.split("{")
				clusterID = cluster[0]         				# is VL-xxxx, the cluster ID
				
				stats = cluster[1].split("[")
				numVectorsClustered = numClusters(stats[0]) # number of documents in this cluster
				
				centroid = stats[1].replace("[", "")
				centroid = stats[1].replace("]", "")
				centroid = centroid.replace("}", "")
				
				cAndR = centroid.split("r")                # now have the centroid and radius
				
				points = cAndR[0].split(",")
				
				topTwenty = {}
				
				for element in points:
					val = element.split(":")
					wordID = val[0].strip()
					value = Decimal(val[1])
					
					addToDictionary( wordID, value, topTwenty )
				
				outfile.write(str(clusterID) + "\t" + numVectorsClustered + "\n")
				
				for key in topTwenty.keys():
					outfile.write("\t" + str(key) + "\t" + str(topTwenty[key]) + "\n")
					
				outfile.write("\n")
				
				
				
				
				
			
