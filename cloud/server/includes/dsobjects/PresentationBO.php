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
		
	}


?>