<?php

ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('../includes/init.php');

//retrieve username and password from POST sent to the page
$login = $_POST['login'];
$password = $_POST['password'];

if(DSUser::validate_login($login,$password))
	echo 'OK';
else
	echo 'FAIL';

?>