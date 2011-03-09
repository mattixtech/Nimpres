<?php
ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('./includes/init.php');

	class UserDO {
		
		public static function createUser($newUserDTO = ''){
			
			  $mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
			  if(GLOBAL_DEBUGGING)
				  $mydb -> debug_on();
			  $id = $newUserDTO->id;
			  
			  if($mydb->record_key_exists("'$id'",'id','login'))	//Check for existence of the login id requested 
			      return FALSE;
			  else if(strlen($newUserDTO->password) < 8)	// Check if the supplied password is under 8 characters
				  return FALSE;
			  else if(empty($newUserDTO->id))
  				  return FALSE;
			  else{
				  $sql = "INSERT INTO login (id, password) VALUE ('$newUserDTO->id', '$newUserDTO->password')";
				  $mydb->run_unsafe_query($sql);
				  return TRUE;
			  }
		}
		
		public static function getUserById($id = ''){
			$mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
			if(GLOBAL_DEBUGGING)
				$mydb -> debug_on();
			
			if(!empty($id)){

				$sql = "SELECT * FROM login WHERE id = '$id'";			
				$user_row = $mydb->run_unsafe_query($sql);
				$user_row = $mydb->get_array($user_row);
				
				if($user_row){ //Check to make sure there was a result returned
					
					$newUserDTO = new UserDTO;
					
					$newUserDTO->id = $user_row['id'];
					$newUserDTO->password = $user_row['password'];
					$newUserDTO->failed_attempts  = $user_row['failed_attempts'];

					return $newUserDTO;
				}
			}
		}
			
		public static function incrementFails($newUserDTO = ''){
			
		$mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
		if(GLOBAL_DEBUGGING)
			$mydb -> debug_on();
			$fails = $newUserDTO->failed_attempts+1;
			$sql = "UPDATE login SET failed_attempts = '$fails' WHERE id = '$newUserDTO->id'";		
			$mydb->run_unsafe_query($sql);
		}
		
		public static function clearFails($newUserDTO = ''){
			
		$mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
		if(GLOBAL_DEBUGGING)
			$mydb -> debug_on();
			
			$sql = "UPDATE login SET failed_attempts = '0' WHERE id = '$newUserDTO->id'";		
			$mydb->run_unsafe_query($sql);
		}
	}

		
?>