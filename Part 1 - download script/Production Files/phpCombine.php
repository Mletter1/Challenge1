<?php
	$basepath = "/home/daniel/Software/PHP_Combine/raw";
	$documents = [];
	
	for( $i = 1; $i <= 8; $i ++ ) {
		$dir  = scandir( $basepath . $i );
		
		// echo var_dump($dir) . "\n\n";
		
		// Condense all raw files into one clean array
		foreach( $dir as $rawFile ) {
			if( $rawFile[0] == "r" ) {
				$rawGrab = file_get_contents($basepath . $i . "/" . $rawFile);
				
				$rawDocs = unserialize($rawGrab);
				
				//echo $rawDocs;
				
				foreach( $rawDocs["results"] as $doc ) {
					if( !array_key_exists($doc["url"], $documents) ) {
						// Build array holding key information
						$newDoc = [];
						
						$newDoc["url"]            = $doc["url"];
						$newDoc["title"]          = $doc["title"];
						$newDoc["section"]        = $doc["section"];
						$newDoc["subsection"]     = $doc["subsection"];
						$newDoc["published_date"] = $doc["published_date"];
						$newDoc["updated_date"]   = $doc["updated_date"];
						$newDoc["abstract"]       = $doc["abstract"];
						
						// Store with URL as key!!!
						$documents[ $doc["url"] ] = $newDoc;		
					}
				} 
			}
		}
	}
	
	// Generate required files
	echo count($documents) . "\n";
	
	$output = serialize( $documents );
	file_put_contents("cleanCopy.sphp", $output );
?>
