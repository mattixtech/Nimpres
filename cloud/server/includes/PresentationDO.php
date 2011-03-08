<?php

ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('../includes/init.php');

	class PresentationDO {
		
		public static function getByPID($pid = '')
		{
			if (!empty($pid)){

				$mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
				if(GLOBAL_DEBUGGING)
					$mydb -> debug_on();
				$sql = "SELECT * FROM pres WHERE pid = '$pid'";			
				$pres_row = $mydb->run_unsafe_query($sql);
				$pres_row = $mydb->get_array($pres_row);
				
				$mydb2 = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
				if(GLOBAL_DEBUGGING)
					$mydb2 -> debug_on();
				$sql = "SELECT * FROM pres_status WHERE pid = '$pid'";			
				$pres_status_row = $mydb2->run_unsafe_query($sql);
				$pres_status_row = $mydb2->get_array($pres_status_row);

				ini_set('display_errors',1);
				error_reporting(E_ALL|E_STRICT);
				
				$newDTO = new PresentationDTO;
				
				//Populate the new PresentationDTO with values from the pres table
				$newDTO-> pid = $pres_row['pid'];
				$newDTO-> user = $pres_row['user'];
				$newDTO-> title = $pres_row['title'];
				$newDTO-> pres_pass = $pres_row['pres_pass'];
				$newDTO-> created = $pres_row['created'];
				$newDTO-> size = $pres_row['size'];
				$newDTO-> length = $pres_row['length'];
				$newDTO-> filename = $pres_row['filename'];
				
				//Continue populating PresentationDTO with values from the pres_status table
				$newDTO-> slide_num = $pres_status_row['slide_num'];
				$newDTO-> status = $pres_status_row['status'];
				$newDTO-> updated_time = $pres_status_row['updated_time'];
				$newDTO-> pres_pass = $pres_row['pres_pass'];

				return $newDTO;
			}
			
		}
		
		public static function setByPID($newDTO = '')
		{
			ini_set('display_errors',1);
			error_reporting(E_ALL|E_STRICT);
			if (!empty($newDTO->pid) && !empty($slide_num)){
				
				$mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
				if(GLOBAL_DEBUGGING)
					$mydb -> debug_on();
			
				$insert_slide_num = $mydb->escape_query_data($slide_num);
				$sql = "UPDATE pres_status SET slide_num = '$slide_num' WHERE pid = '$newDTO->pid'";			
				$mydb->run_unsafe_query($sql);
				  	return TRUE;
			}
			
			else{
				return FALSE;
			}
		}
	}

?>