<?php
/**
 * Project:			Nimpres Server API
 * File name: 		create_presentation2.php
 * Date modified:	2011-03-17
 * Description:		This class creates a presentation entry in pres and pres_status tables, labels the slides in the slides table
 * 					, uploads a .dps file and fills a folder with the extracted contents
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

$user = $_GET['user_id'];
$password = $_GET['user_password'];
$title = $_GET['title'];
$pres_pass = $_GET['pres_password'];
$length = $_GET['length'];
$slide_num = 0;
$status = 'available';
$over = 0;

//TODO adjust for slide ordering
if (!empty($user) && !empty($password) && !empty($title) && !empty($length) && is_numeric($slide_num) && !empty($status) && userBO::validateLogin($user, $password)){
	$id = PresentationBO::createPres($user, $title, $pres_pass, $length, $slide_num, $status, $over);
	if ($id >= 0){
		$target_folder = PRESENTATIONS_DIR;
		$target_folder = $target_folder . $id;
		$target_path = PRESENTATIONS_DIR;
		$target_path = $target_path . $id . '.dps';
		$target_pathToUnzip = $target_folder;
		
		if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) {
				if (mkdir($target_folder, 0777)){
				 	$zip = new ZipArchive;
					if ($zip->open($target_path) === TRUE) {
					    $zip->extractTo($target_pathToUnzip);
					    $adjustIndex = 0;
					    for($i = 0; $i < $zip->numFiles; $i++){   
     						$adjustIndex++;
         					$name = $zip->getNameIndex($i);
         					if ($name != 'meta-inf.xml')
         						PresentationBO::storeSlide($id, $adjustIndex, $name);
         					else 
         						$adjustIndex--;
    					}
					    $zip->close();
					    echo $id; 
					} else 
					    echo 'FAIL';
				}else
					echo 'FAIL';
		}else
		    echo 'FAIL';
	}else
		echo 'FAIL';
}else 
	echo 'FAIL';

?>