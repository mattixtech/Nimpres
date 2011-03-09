<?php

	class PresentationBO{
		
		public static function createPres($user, $title, $pres_pass, $length, $slide_num, $status, $over)
		{
			$newPresDTO = new PresentationDTO;

			$newPresDTO -> user = $user;
			$newPresDTO -> title = $title;
			$newPresDTO -> pres_pass = $pres_pass;
			$newPresDTO -> length = $length;
			$newPresDTO -> slide_num = $slide_num;
			$newPresDTO -> status = $status;
			$newPresDTO -> over = $over;

			if (PresentationDO::insertPres($newPresDTO))
				return TRUE;
			else
				return FALSE;
			
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
			$newPresDTO-> slide_num = $slide_num;
			if (PresentationDO::updatePres($newPresDTO))
				return TRUE;
			else
				return FALSE;
		}
		
		public static function deletePres($pid){
			
			$newPresDTO = new PresentationDTO;
			$newPresDTO-> pid = $pid;
			
			if(PresentationDO::deletePres($newPresDTO))
				return TRUE;
			else 
				return FALSE;
		}
		
	}


?>