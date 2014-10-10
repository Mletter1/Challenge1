<?php
	// Path to save documents
    $outputPath = "/home/bigdata/Documents/PHP/abstracts/";

	// Load Cleaned Documents SPHP
	$cleanCopyPath = "/home/bigdata/Documents/PHP/cleanCopy.sphp";
	$file          = file_get_contents( $cleanCopyPath );
	$documents     = unserialize( $file );
	
	// Pull each abstract - Index and create document with just abstract
	$index         = [];
	$docCounter    = 1;
	
	foreach( $documents as $doc ) {
		$index[ $doc["url"] ] = $docCounter;
		
		file_put_contents( $outputPath . $docCounter . ".txt", $doc["abstract"] );
		
		$docCounter++;
	}
	
	// Save index for later use
	$packedIndex = serialize( $index );
	file_put_contents( "abstractIndex.sphp", $packedIndex );
?>
