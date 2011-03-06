<?php

ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('../includes/init.php');

$mydb = new MySQLDatabase(DATABASE_ADDR,DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
  if(GLOBAL_DEBUGGING)
	  $mydb -> debug_on();

//retrieve username and password from POST sent to the page
$login = $_GET['login'];
$password = $_GET['password'];
$key = 'id';

if($mydb->record_key_exists("'$login'",'id','login'))	//Check for existence of the login id requested
	echo 'AT';	//Reply with already taken
else if(strlen($password)< 8)	// Check if the supplied password is under 8 characters
	echo 'TS';	//Reply with too short
else if (strlen($login)< 1)
	echo 'NU';
else if((DSUser::create_new_login($login, $password, $key)))
	echo 'OK';
else
	echo 'FAIL';


?>