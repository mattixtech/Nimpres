<?php

ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('./includes/init.php');

$pid = $_GET['id'];
$pres_pass = $_GET['password'];


if (!empty($pid) && $pres_pass === PresentationBO::getPresPass($pid))
{
	if(PresentationBO::deletePres($pid))
		echo 'OK';
	else
		echo 'FAIL';
}
else
	echo 'FAIL';
?>