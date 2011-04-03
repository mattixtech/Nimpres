/**
 * Project:			Nimpres Web Client
 * File name: 		viewer.js
 * Date modified:	2011-03-08
 * Description:		Android entrypoint for Nimpres app
 * 
 * License:			Copyright (c) 2011 (Matthew Brooks, Jordan Emmons, William Kong)
					
					Permission is hereby granted, free of charge, to any person obtaining a copy
					of this software and associated documentation files (the "Software"), to deal
					in the Software without restriction, including without limitation the rights
					to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
					copies of the Software, and to permit persons to whom the Software is
					furnished to do so, subject to the following conditions:
					
					The above copyright notice and this permission notice shall be included in
					all copies or substantial portions of the Software.
					
					THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
					IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
					FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
					AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
					LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
					OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
					THE SOFTWARE.
 */

function listByHost(){
	var host = $("#host").val();
	if(host != null && host.length > 1){
		ajaxListPres(host); 	
		$("#find_host").hide();
		$("#loading").show();
	}
}

function ajaxListPres(host){
		
	var outputText = "";
	var count = 0;
	$.ajax({//Perform the Ajax get query
	  type: 'POST',
	  url: "http://api.nimpres.com/list_presentations.php",
	  dataType: "xml",
	  data: {
		  	user_id: "test",
		  	user_password: "test1234",
		  	user_search: host,
		},
	  success: function(xml){		  
		$(xml).find('presentations').each(function(){
			$(this).find('presentation_user').each(function(){});
				$(this).find('presentation').each(function(){
					count++;
					title = $(this).find('title').text();
					pid =  $(this).find('id').text();
					created = $(this).find('created').text();
					length = $(this).find('length').text();
					size = $(this).find('size').text();
					slide_num = $(this).find('slide_num').text();
					updated_time = $(this).find('updated_time').text();
					status = $(this).find('status').text();
					over = $(this).find('over').text();
					outputText += "<p><a href='http://api.nimpres.com/testing/pres_display.php?pres_id="+pid+"'><strong>"+title+"</a></strong></p><br/>";
				});
				$("#loading").hide();
				$("#output_list").show();
				$("#output_list").html(outputText);
				
			
	  });   
	}
		
	});
}