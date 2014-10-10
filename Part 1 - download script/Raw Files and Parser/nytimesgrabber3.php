<?php
	$grab    = 1568;
	$version = "v3";
	$key     = "api-key=fada3ff5474b257d9cd5ecc4a2e7890e:10:69866438";
	$format  = ".sphp";
	$source  = "all/";
	$section = "all";
	//$limit   = "limit=20&";
	
	$documents = [];
	
	
	while( $grab < 1880 ) {
		
		// Set URL for query and request
		$offset  = "offset=" . strval($grab * 20) . "&";
	
		$baseURL = "http://api.nytimes.com/svc/news/" . $version. "/content/";
		//$URL = $baseURL . $source . $section . $format . "?" . $limit . $offset . $key;
		$URL = $baseURL . $source . $section . $format . "?" . $offset . $key;
	
		echo $URL . "\n";
		
		$raw = @file_get_contents( $URL );
		
		if( !($raw === false) ) {
			$response = unserialize($raw);
			
			// IF its a good grab...
			if( $response["status"] == "OK" ) {
				// Save raw sphp response
				file_put_contents( "raw" . $grab, $raw );
				$grab = $grab + 1;
				
				/**
				// Loop through responses, and load into documents
				foreach( $response["results"] as $doc ) {
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
				
				// Store new documents in file
				if( $grab % 25 == 0 ) {
					file_put_contents( "clean" . count($documents), serialize($documents) );
					
					echo "Now have " . count($documents) . " docs.\n";
				}
				*/
			}
		}
	}
	
	// Save the final output
	//file_put_contents( "finalCleanOutput", serialize($documents) );
	
	
	/**
	//echo $raw . "<br><br>";

	//echo var_dump($response);
	
	if( $response["status"] == "OK" ) {
		foreach( $response["results"] as $document ) {
			echo "URL:  " . $document["url"] . "<br>";
			echo "Title:  " . $document["title"] . "<br>";
			echo "Section:  " . $document["section"] . "<br>";
			echo "SubSection:  " . $document["subsection"] . "<br>";
			echo "Published:  " . $document["published_date"] . "<br>";
			echo "Updated:  " . $document["updated_date"] . "<br><br>";
			echo "Abstract:  " . $document["abstract"] . "<br>";
		} 
	} else {
		echo "Error Downloading.";
	} 
	**/
?>
