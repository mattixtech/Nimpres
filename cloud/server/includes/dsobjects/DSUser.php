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
		
		/*Member functions*/
		/******************/
	}
?>