<?php
$pid = $_GET['pres_id'];
$pres_pass = $_GET['pres_password'];
$title = $_GET['title'];
?>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo $title; ?> - Brought to you by Nimpres</title>
<style type="text/css">
body,td,th {
	font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
	font-size: 18px;
	color: #30A;
}
body {
	background-color: #FFF;
}

.main_input{
	background: #FFFFFF;
	border-bottom: 2px solid #898989;
	border-left: 1px solid #bcbcbc;
	border-right: 2px solid #898989;
	border-top: 1px solid #bcbcbc;
	color:#000000;
	font-size: 24px;
	padding: 2px;
	margin: 2px;
	font-weight: bold;
	height: 35px;
	margin-bottom: 2px;
	text-align: left;
	width: 500px;
	font-family: Arial, Helvetica, sans-serif;	
}
</style>
<!-- Include jQuery -->
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
<!-- Include the presentation display code -->
<script type="text/javascript" src="scripts/pres_display.js"></script>

<script type="text/javascript">

$(document).ready(function()
{
	var t=setInterval("getSlideNum(<?php echo $pid;?>,'<?php echo $pres_pass;?>')",1500);
}
);

</script>

</head>

<body>
<div id="main" align="center">
	<div id="pres_title">
	<h2><?php echo $title; ?></h2>
	</div>

<img src="" id="slide" />
<img src="" id="preload_1" style="display:none;"/>
<img src="" id="preload_2" style="display:none;"/>
<img src="" id="preload_3" style="display:none;"/>
<img src="" id="preload_4" style="display:none;"/>
    
</div>
</body>

</html>
