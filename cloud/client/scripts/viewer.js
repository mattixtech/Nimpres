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
	  type: 'GET',
	  url: "http://api.nimpres.com/list_presentations.php",
	  dataType: "xml",
	  success: function(xml){		  
		$(xml).find('presentations').each(function(){
			$(this).find('user').each(function(){
				$(this).find('presentation').each(function(){
					count++;
					pid =  $(this).find('id').text();
					title = $(this).find('title').text();
					created = $(this).find('created').text();
					date = new Date(timestamp*1000);
					email = $(this).find('comment_email').text();
					comment_data = $(this).find('comment_data').text();
					<a href="url">Link text</a>
					outputText += "<p><span class='commName'><strong>"+name+"</strong></span> says: <br/><br/>"+comment_data+"<br/><br/><strong>Posted: "+date+"</strong></p><br/>"
				});
				
				outputText +='<span class="numcomment"># of Comments: <span class="normal">'+count+'</span></span>';
				$("#labComments").html(outputText);
			
			
		});
	  }   
	});
		
}
}