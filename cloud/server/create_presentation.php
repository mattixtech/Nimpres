<?php

ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('./includes/init.php');

$user = $_POST['user'];
$title = $_POST['title'];
$pres_pass = $_POST['password'];
$length = $_POST['length'];
$slide_num = $_POST['slide_num'];
$status = $_POST['status'];
$over = $_POST['over'];

if (!empty($user) && !empty($title) && !empty($length) && !empty($slide_num) && !empty($status))
{
	$id = PresentationBO::createPres($user, $title, $pres_pass, $length, $slide_num, $status, $over);
	if ($id >= 0){
		$target_path = PRESENTATIONS_DIR;
		
		$target_path = $target_path . $id . '.dps'; 
		
		if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) 
		{
		    echo 'OK';
		} 
		else
		{
		    echo 'FAIL';
		}	
	}
	else
		echo 'FAIL';
}
else 
	echo 'FAIL';

?>