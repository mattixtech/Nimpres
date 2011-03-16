<?php

ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('./includes/init.php');

$pid = $_GET['id'];
$pres_pass = $_GET['password'];

if (!empty($pid) && $pres_pass === PresentationBO::getPresPass($pid))
{
	readfile(PRESENTATIONS_DIR.$pid.'.dps');
}
else
	echo 'FAIL';
?>