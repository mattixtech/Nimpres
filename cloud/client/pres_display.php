<?php
$pid = $_GET['pres_id'];
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Testing Page</title>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
<script type="text/javascript" src="scripts/pres_display.js"></script>

<script type="text/javascript">

$(document).ready(function()
{
	var t=setInterval("getSlideNum(<?php echo $pid; ?>)",2000);
}
);

</script>
</head>
<body>

<img src="" id="slide" />

</body>
</html>
