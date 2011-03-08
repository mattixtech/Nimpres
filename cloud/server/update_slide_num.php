<?php
ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('./includes/init.php');

$pid = $_GET['id'];
$slide_num = $_GET['slide_num'];
$pres_pass = $_GET['password'];

if (!empty($pid) && !empty($pres_pass) && !empty($slide_num) && $pres_pass === PresentationBO::getPresPass($pid)){
	if(PresentationBO::updateSlideNum($pid, $slide_num))
		echo 'OK';
	else
		echo 'FAIL';
}
else 
	echo 'FAIL';

?>