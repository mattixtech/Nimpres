<?php
/**
 * Project:			Nimpres Server API
 * File name: 		
 * Date modified:	2011-03-17
 * Description:		
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

/*Load the settings and lets get started!*/
ini_set('display_errors',1);
error_reporting(E_ALL|E_STRICT);
require_once('settings.php');
require_once("functions.php");
require_once("MySQLDatabase.php");
require_once("UserDTO.php");
require_once("UserDO.php");
require_once("UserBO.php");
require_once("PresentationDTO.php");
require_once("PresentationDO.php");
require_once("PresentationBO.php");

if(GLOBAL_DEBUGGING)
{
	ini_set('display_errors',1);
	error_reporting(E_ALL|E_STRICT);
}
else
{
	ini_set('display_errors',0);
	error_reporting(0);
}

/*ob_start();//Output buffering is useful for redirection
date_default_timezone_set('America/New_York');
//This is the cookie we can use later if we need to see if user accepts cookies
if(!isset($_COOKIE['CHECK']))
{
	setcookie("CHECK", "OK");
}*/
?>