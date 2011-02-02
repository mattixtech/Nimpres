<?php
/*Load the settings and lets get started!*/
ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('settings.php');
require_once(INCLUDES_DIR . '/' . "functions.php");
require_once(INCLUDES_DIR . '/' . "MySQLDatabase.php");
require_once(INCLUDES_DIR . '/' . "Session.php");
require_once(INCLUDES_DIR . '/dsobjects/' . "DSUser.php");

if(GLOBAL_DEBUGGING)
{
	ini_set('display_errors',1);
	error_reporting(E_ALL|E_STRICT);
}
else
{
	ini_set('display_errors',0);
	error_reporting(0);
}

/*ob_start();//Output buffering is useful for redirection
date_default_timezone_set('America/New_York');
//This is the cookie we can use later if we need to see if user accepts cookies
if(!isset($_COOKIE['CHECK']))
{
	setcookie("CHECK", "OK");
}*/
?>