
with open("output.csv", "w") as output:
	clusters = []
	
	for i in range(0, 50):
			clusters.append( [] )
	
	with open("centroids2.csv", "r") as fd:	
		for line in fd:
			elements = line.split(",")
			
			for x in range(0, 50):
				clusters[x].append( elements[x].rstrip() )
	
	output.write("clusterID\t")
	
	for i in range(0, 32928):
		output.write( str(i) + "," )
		
	output.write("\n")
	
	for i in range(0, 50):
		for j in range(0, len( clusters[i] )):
			output.write( clusters[i][j] + "," )
		output.write("\n")
