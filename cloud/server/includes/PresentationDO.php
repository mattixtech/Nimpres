<?php

ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('./includes/init.php');

	class PresentationDO {
		
		public static function getByUser($user = '')
		{
			if (!empty($user)){
				$mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
				if(GLOBAL_DEBUGGING)
					$mydb -> debug_on();
				$sql = "SELECT * FROM pres WHERE user = '$user'";
				$pres_rows = $mydb->run_unsafe_query($sql);

				$found_presentations = array();
				while ($rows = $mydb->get_array($pres_rows))
				{
					$newPresDTO = new PresentationDTO;
					$pid = $rows['pid'];
					array_push($found_presentations, self::getByPID($pid));
				}
				return $found_presentations;
			}
		}
		
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
				
				$newPresDTO = new PresentationDTO;
				
				//Populate the new PresentationDTO with values from the pres table
				$newPresDTO->pid = $pres_row['pid'];
				$newPresDTO->user = $pres_row['user'];
				$newPresDTO->title = $pres_row['title'];
				$newPresDTO->pres_pass = $pres_row['pres_pass'];
				$newPresDTO->created = $pres_row['created'];
				$newPresDTO->size = $pres_row['size'];
				$newPresDTO->length = $pres_row['length'];
				$newPresDTO->filename = $pres_row['filename'];
				
				//Continue populating PresentationDTO with values from the pres_status table
				$newPresDTO->slide_num = $pres_status_row['slide_num'];
				$newPresDTO->status = $pres_status_row['status'];
				$newPresDTO->updated_time = $pres_status_row['updated_time'];
				$newPresDTO->pres_pass = $pres_row['pres_pass'];
				$newPresDTO->over = $pres_status_row['over'];

				return $newPresDTO;
			}
		}
		
		public static function updatePres($newPresDTO = '')
		{
			ini_set('display_errors',1);
			error_reporting(E_ALL|E_STRICT);
			if (!empty($newPresDTO->pid)){
				
				$mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
				if(GLOBAL_DEBUGGING)
					$mydb -> debug_on();
			
				$sql = "UPDATE pres SET title = '$newPresDTO->title', pres_pass = '$newPresDTO->pres_pass' WHERE pid = '$newPresDTO->pid'";		
				$mydb->run_unsafe_query($sql);
					
				$sql = "UPDATE pres_status SET slide_num = '$newPresDTO->slide_num', status = '$newPresDTO->status',over = '$newPresDTO->over' WHERE pid = '$newPresDTO->pid'";		
				$mydb->run_unsafe_query($sql);
				return TRUE;
			}
			
			else{
				return FALSE;
			}
		}
		
		public static function insertPres($newPresDTO = '')
		{
			ini_set('display_errors',1);
			error_reporting(E_ALL|E_STRICT);

			if (empty($newPresDTO->pid)){
				
				$mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
				if(GLOBAL_DEBUGGING)
					$mydb -> debug_on();

				$sql = "INSERT INTO pres (user, title, pres_pass, size, length, filename) VALUE ('$newPresDTO->user', '$newPresDTO->title', '$newPresDTO->pres_pass', '$newPresDTO->size', '$newPresDTO->length', '$newPresDTO->filename')";
				$mydb->run_unsafe_query($sql);

				$pid = $mydb->get_last_id();
				$sql = "INSERT INTO pres_status (pid, slide_num, status, over) VALUE ('$pid', '$newPresDTO->slide_num', '$newPresDTO->status', '$newPresDTO->over')";
				$mydb->run_unsafe_query($sql);
				return $pid;
			}
			else{
				return -1;
			}
		}
		
		public static function deletePres($newPresDTO = ''){
			
			$mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
			if(GLOBAL_DEBUGGING)
				$mydb -> debug_on();
			
			if (!empty($newPresDTO->pid)){
				
				if(!($mydb->record_key_exists("'$newPresDTO->pid'",'pid','pres')))	//Check for existence of the presentationin pres
			      return FALSE;
				$sql = "DELETE FROM pres WHERE pid = '$newPresDTO->pid'";
				$mydb->run_unsafe_query($sql);
				
				if(!($mydb->record_key_exists("'$newPresDTO->pid'",'pid','pres_status')))	//Check for existence of the presentation in pres_status
			      return FALSE;
				$sql = "DELETE FROM pres_status WHERE pid = '$newPresDTO->pid'";
				$mydb->run_unsafe_query($sql);
				return TRUE;
			}
			else 
				return FALSE;
		}
	}
?>