<?php

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