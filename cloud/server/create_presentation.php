<?php
/**
 * Project:			Nimpres Server API
 * File name: 		create_presentation.php
 * Date modified:	2011-03-17
 * Description:		This class defines a DPS package
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