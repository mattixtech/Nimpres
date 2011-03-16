<?php
ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('./includes/init.php');

$user = $_GET['user'];

if (!empty($user))
{
	$xmlPresList = PresentationBO::listPres($user);
	echo '<?xml version="1.0"?>';
	echo '<presentations>';
	echo $xmlPresList;
	echo '</presentations>';
}
else 
	echo 'FAIL';

?>