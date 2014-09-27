<html>
	<head>
		<meta charset="UTF8">
	</head>
	<body>
		<?php
			$version = "v3";
			$key     = "api-key=c0bb5c76006765d2300174594f83bbf8:14:69866438";
			$format  = ".sphp";
			$source  = "all/";
			$section = "all";
			$limit   = "limit=10";
			$offset  = "offset=0";
			
			$baseURL = "http://api.nytimes.com/svc/news/" . $version. "/content/";
			$URL = $baseURL .  $source . $section . $format . "?" . $limit . "&" . $offset . "&" . $key;
		
			echo $URL . "<br><br>";
			
			$raw = file_get_contents( $URL );
			
			//echo $raw . "<br><br>";
			
			$response = unserialize( $raw );
			
			//echo var_dump($response);
			
			foreach( $response["results"] as $document ) {
				echo "URL:  " . $document["url"] . "<br>";
				echo "Abstract:  " . $document["abstract"] . "<br><br>";
			} 
		?>
	</body>
</html>
