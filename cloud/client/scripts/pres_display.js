	lastResponse = "-1";

function getSlideNum(pres_id, pres_password) {
	
	if($("#isPaused").val() == "false"){
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
					if(response > 0){
						$("#slide").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password="+pres_password+"&user_id=test&user_password=test1234&slide_num="+response);
						$("#slideNumber").val(""+response);
					}
					preload(response);					
				}
			}
		});
	}
}

//This function preloads the previous and the next 2 images
function preload(response){
	pres_id = $("#id").val();
	pres_password = $("#password").val();
	//preload images
	if((response - 2) > 0)
		$("#preload_1").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password="+pres_password+"&user_id=test&user_password=test1234&slide_num="+(response - 2));
	if((response - 1) > 0)
		$("#preload_2").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password="+pres_password+"&user_id=test&user_password=test1234&slide_num="+(response - 1));				
	$("#preload_3").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password="+pres_password+"&user_id=test&user_password=test1234&slide_num="+(response + 1));
	$("#preload_4").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password="+pres_password+"&user_id=test&user_password=test1234&slide_num="+(response + 2));
}

function next(){
	slideNumber = parseInt($("#slideNumber").val());
	slideNumber++;
	lastResponse = slideNumber;
	preload(lastResponse);
	$("#slideNumber").val(""+slideNumber);
	pres_id = $("#id").val();
	pres_password = $("#password").val();
	$("#slide").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password="+pres_password+"&user_id=test&user_password=test1234&slide_num="+slideNumber);
}

function previous(){
	slideNumber = parseInt($("#slideNumber").val());
	slideNumber--;
	lastResponse = slideNumber;
	preload(lastResponse);
	$("#slideNumber").val(""+slideNumber);
	pres_id = $("#id").val();
	pres_password = $("#password").val();
	$("#slide").attr("src","http://api.nimpres.com/get_slide_file.php?pres_id="+pres_id+"&pres_password="+pres_password+"&user_id=test&user_password=test1234&slide_num="+slideNumber);
}

function pause(){
	if($("#isPaused").val() == "false"){
		$("#isPaused").val("true");
		$("#pauseButton").val("Resume");
		$("#previousButton").attr("disabled","");
		$("#nextButton").attr("disabled","");
	}
	else{
		$("#isPaused").val("false");
		lastResponse = "-1";
		$("#pauseButton").val("Pause");
		$("#previousButton").attr("disabled","disabled");
		$("#nextButton").attr("disabled","disabled");
	}	
}
