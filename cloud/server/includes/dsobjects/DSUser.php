<?php

	class DSUser{
		/*Member variables*/
		
		function __construct(){
		}
		
		/*Static functions*/
		/******************/
		
		/*Returns TRUE if valid login, FALSE otherwise*/
		public static function validate_login($id = '', $password = ''){
			$mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
			if(GLOBAL_DEBUGGING)
				$mydb -> debug_on();
			$sql = "SELECT * FROM login WHERE id = '$id'";			
			$result = $mydb->run_unsafe_query($sql);
			$result = $mydb->get_array($result);
			
			if($result){//Check to make sure there was a result returned
				$dbPass = $result['password'];
				$fails  = $result['failed_attempts'];
				
				if($password === $dbPass){
					$mydb->put_record_value("login","id","'$login'","failed_attempts",0); //Reset failed attempts counter
					return TRUE;
				}
				else{
					$fails +=  1;
					$mydb->put_record_value("login","id","'$login'","failed_attempts","$fails"); //Increment failed attempts counter
				}
			}
			return FALSE;
		}
		
		/*Attempts to create a new login, returns TRUE if successful, FALSE otherwise*/
		static function create_new_login($id = '', $password = '', $key = ''){
		//TODO
			$mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
			  if(GLOBAL_DEBUGGING)
				  $mydb -> debug_on();
			  
			  if($mydb->record_key_exists("'$login'",'id','login'))	//Check for existence of the login id requested
				  return FALSE;
			  else if(strlen($password)< 8)	// Check if the supplied password is under 8 characters
				  return FALSE;
			  else if(strlen($id)< 1)
			  	  return FALSE;
			  else{
				  $insertId = $mydb->escape_query_data($id);
				  $insertPass = $mydb->escape_query_data($password);
				  
				  $sql = "INSERT INTO login (id, password) VALUE ('$insertId', '$insertPass')";
				  $mydb->run_unsafe_query($sql);
				  	return TRUE;

			  }
		}
		
		/*Member functions*/
		/******************/
	}
?>