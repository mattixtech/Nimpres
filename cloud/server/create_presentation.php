<?php

ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('./includes/init.php');

$user = $_GET['user'];
$title = $_GET['title'];
$pres_pass = $_GET['password'];
$length = $_GET['length'];
$slide_num = $_GET['slide_num'];
$status = $_GET['status'];
$over = $_GET['over'];

if (!empty($user) && !empty($title) && !empty($length) && !empty($slide_num) && !empty($status)){
	if(PresentationBO::createPres($user, $title, $pres_pass, $length, $slide_num, $status, $over))
		echo 'OK';
	else
		echo 'FAIL';
}
else 
	echo 'FAIL';

?>