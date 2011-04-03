<?php
$pid = $_GET['pres_id'];
$title = $_GET['title'];
?>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Nimpres - Cross platform unified real time presentations</title>
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
<!-- Include the viewer code -->
<script type="text/javascript" src="scripts/viewer.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		$("#pres_pass").focus(function(){$(this).val('');});
	});
</script>

</head>

<body>
<div id="main" align="center">
<h1>Nimpres - <i>The nimble presenter</i></h1>
<h2>Cross platform unified real time presentations</h2>

	<div id="find_pass">
        <p>
            <input type="text" id="pres_pass" name="pres_pass" value="Enter Presentation Pass (Blank if none)" class="main_input"/>
            <br/><br/>
            <input type="button" value="Validate" onclick="javascript:validatePass(<?php echo $pid; ?>, '<?php echo $title; ?>')"/>
        </p>
    </div>
	
	<div id="outputText">
    </div>
    
</div>


</body>
</html>
