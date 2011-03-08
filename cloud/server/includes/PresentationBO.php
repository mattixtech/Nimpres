<?php

	class PresentationBO{
		
		public static function getSlideNum($pid)
		{
			$newDTO = PresentationDO::getByPID($pid);
			return $newDTO->slide_num;
		}
		
		public static function getPresPass($pid)
		{

			$newDTO = PresentationDO::getByPID($pid);
			return $newDTO->pres_pass;
		}
		
		public static function updateSlideNum($pid,$slide_num)
		{
			
			$newDTO = PresentationDO::getByPID($pid);
			$newDTO-> slide_num = $slide_num;
			if (PresentationDO::setByPID($newDTO))
				return TRUE;
			else(
				return FALSE;
			)
		}
		
	}


?>