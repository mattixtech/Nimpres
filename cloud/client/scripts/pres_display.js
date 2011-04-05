	lastResponse = "-1";

function getSlideNum(pres_id, pres_password) {

	$.ajax({//Perform the Ajax get query
		type: 'POST',
		url: "http://api.nimpres.com/get_slide_num.php",
		dataType: "text",
		data: {
			pres_id: pres_id,
			pres_password: pres_password
		},
		success: function(response){
			if(response != lastResponse){
				lastResponse = response;
				response = parseInt(response)+1;
				if(response > 0)
				$("#slide").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password="+pres_password+"&user_id=test&user_password=test1234&slide_num="+response);
				
				//preload images
				if((response - 2) > 0)
					$("#preload_1").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password="+pres_password+"&user_id=test&user_password=test1234&slide_num="+(response - 2));
				if((response - 1) > 0)
					$("#preload_2").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password="+pres_password+"&user_id=test&user_password=test1234&slide_num="+(response - 1));				
				$("#preload_3").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password="+pres_password+"&user_id=test&user_password=test1234&slide_num="+(response + 1));
				$("#preload_4").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password="+pres_password+"&user_id=test&user_password=test1234&slide_num="+(response + 2));
			}
		}
	});
}

