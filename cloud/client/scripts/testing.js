function getSlideNum() {
	var pres_id="73";
	lastResponse = "0";
	$.ajax({//Perform the Ajax get query
		type: 'POST',
		url: "http://api.nimpres.com/get_slide_num.php",
		dataType: "text",
		data: {
			pres_id: pres_id,
			pres_password: "test"
		},
		success: function(response){
			if(response != lastResponse){
				lastResponse = response;
				$("#slide").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password=test&user_id=test&user_password=test1234&slide_num="+(parseInt(response)+1));
			}
			
		}
	});
}

