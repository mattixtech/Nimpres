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

	class PresentationBO{
		
		public static function createPres($user, $title, $pres_pass, $length, $slide_num, $status, $over)
		{
			$newPresDTO = new PresentationDTO;

			$newPresDTO->user = $user;
			$newPresDTO->title = $title;
			$newPresDTO->pres_pass = $pres_pass;
			$newPresDTO->length = $length;
			$newPresDTO->slide_num = $slide_num;
			$newPresDTO->status = $status;
			$newPresDTO->over = $over;
			
			$pid = PresentationDO::insertPres($newPresDTO);
			return $pid;
		}
		
		public static function listPres($user)
		{
			$Presentations = array();
			$Presentations = PresentationDO::getByUser($user);
			
			$xmlPresList = '';
			$xmlPresList.= '<presentation_user>';
			$xmlPresList.= $Presentations[0]->user;
			$xmlPresList.= '</presentation_user>';
			$presNum = 1;
			
			foreach($Presentations as $presentation)
			{

				$xmlPresList.= '<presentation number="';
				$xmlPresList.= $presNum;
				$xmlPresList.= '">';
				$xmlPresList.= '<title>';
				$xmlPresList.= $presentation->title;
				$xmlPresList.= '</title>';
				$xmlPresList.= '<length>';
				$xmlPresList.= $presentation->length;
				$xmlPresList.= '</length>';
				$xmlPresList.= '<size>';
				$xmlPresList.= $presentation->size;
				$xmlPresList.= '</size>';
				$xmlPresList.= '<slide_num>';
				$xmlPresList.= $presentation->slide_num;
				$xmlPresList.= '</slide_num>';
				$xmlPresList.= '<created>';
				$xmlPresList.= $presentation->created;
				$xmlPresList.= '</created>';
				$xmlPresList.= '<updated_time>';
				$xmlPresList.= $presentation->updated_time;
				$xmlPresList.= '</updated_time>';
				$xmlPresList.= '<status>';
				$xmlPresList.= $presentation->status;
				$xmlPresList.= '</status>';
				$xmlPresList.= '<over>';
				$xmlPresList.= $presentation->over;
				$xmlPresList.= '</over>';
				$xmlPresList.= '</presentation>';
				$presNum++;
			}
			return $xmlPresList;
		}
		
		public static function getSlideNum($pid)
		{
			$newPresDTO = PresentationDO::getByPID($pid);
			return $newPresDTO->slide_num;
		}
		
		public static function getPresPass($pid)
		{

			$newPresDTO = PresentationDO::getByPID($pid);
			return $newPresDTO->pres_pass;
		}
		
		public static function updateSlideNum($pid,$slide_num)
		{
			
			$newPresDTO = PresentationDO::getByPID($pid);
			$newPresDTO->slide_num = $slide_num;
			
			if (PresentationDO::updatePres($newPresDTO))
				return TRUE;
			else
				return FALSE;
		}
		
		public static function deletePres($pid){
			
			$newPresDTO = new PresentationDTO;
			$newPresDTO->pid = $pid;
			
			if(PresentationDO::deletePres($newPresDTO))
				return TRUE;
			else 
				return FALSE;
		}
		
	}


?>