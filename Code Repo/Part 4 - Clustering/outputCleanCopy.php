<?php
	$clean = file_get_contents("cleanCopy.sphp");
	$ind   = file_get_contents("abstractIndex.sphp");

	$documents = unserialize( $clean );
	$index     = unserialize( $ind );
	
	$csv = "";
	
	foreach( $documents as $doc ) {
		$url        = $doc["url"];
		
		if( strlen( $url ) > 1 ) { 
			$docID      = $index[ $url ];
			$section    = $doc["section"];
			$subSection = $doc["subsection"];
			
			$csv = $csv . $docID . "," . $url . "," . $section . "," . $subSection . "\n";
		}
	}
	
	file_put_contents($csv, "docData.csv");

?>
