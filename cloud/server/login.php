<?php

ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('./includes/init.php');

//retrieve username and password from POST sent to the page
$login = $_GET['login'];
$password = $_GET['password'];

if(!empty($login) && !empty($password)){
	if (UserBO::validateLogin($login,$password))
		echo 'OK';
	else 
		echo 'FAIL';
}
else
	echo 'FAIL';

?>