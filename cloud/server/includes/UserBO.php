<?php

ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('./includes/init.php');

	class UserBO{
			
		public static function createUser($id = '', $password = ''){

			$newUserDTO= new UserDTO;

			$newUserDTO->id = $id;
			$newUserDTO->password = $password;

			if (UserDO::createUser($newUserDTO))
				return TRUE;
			else
				return FALSE;
			
		}
		
		public static function validateLogin($id = '', $password = ''){
			
			$newUserDTO = UserDO::getUserById($id);
			
			if ($newUserDTO->password === $password){
				UserDO::clearFails($newUserDTO);
				return TRUE;
			}
			else{
				UserDO::incrementFails($newUserDTO);
				return FALSE;
			}
				
		}
	
	}
?>