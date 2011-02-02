<?php
/**
*File: Session.php
*Author: Matthew Brooks
*
*Description:
*/
?>
<?php
/**
* Some Notes:
* Must set global $login_attempting to true on your main login page so it does not trigger a login redirection loop
*/
class Session
{
	
	private $logged_in = false;
	public $user_id;
	public $privilege_level = 0;
	public $contacts_only = false;
	
	public static function trigger_login($msg="")
	{
		session_destroy();
		redirect_to(LOGIN_PAGE_ADDR . "?message=$msg");
		$reset_session = new Session;
	}
	
	function __construct()
	{
		session_start();
		
		/*Code below makes sure the session has not expired*/
		if(isset($_SESSION['SESSION_LAST_REFRESH_TIME']))
		{
			$last_time = $_SESSION['SESSION_LAST_REFRESH_TIME'];
			$current_time = time();
			
			if(($current_time - $last_time) > SESSION_TIMEOUT)
			{
				session_destroy();
				redirect_to(ERROR_PAGE_ADDR . '?error=session_timeout');
			}
		}
		
		$_SESSION['SESSION_LAST_REFRESH_TIME'] = time();
		
		$this->check_login();	
		
		/*The following is a little security addition*/
		/*If somehow a different browser requests the same session we will destroy it and force it to regenerate*/
		if (isset($_SESSION['HTTP_USER_AGENT']))
		{
   			if ($_SESSION['HTTP_USER_AGENT'] != md5($_SERVER['HTTP_USER_AGENT']))
    		{
        		/* Prompt for password */
				session_destroy();
				redirect_to(WEB_ROOT . '/login.php');
        		exit;
    		}
		}
		else
		{
    		$_SESSION['HTTP_USER_AGENT'] = md5($_SERVER['HTTP_USER_AGENT']);
		}
		
		if($this->logged_in)
		{
			//Any code for if a user is already logged in
		}
		else
		{
			
		}
	}
	
	public function login($user_id)
	{
		if($user_id)
		{
			$this->user_id = $_SESSION['user_id'] = $user_id;
			$_SESSION['privilege_level'] = $this->privilege_level;
			$this->logged_in = true;
		}
	}
	
	public function logout()
	{
		unset($_SESSION['user_id']);
		unset($this->user_id);
		unset($_SESSION['privilege_level']);
		unset($this->privilege_level);
		$this->logged_in = false;
		//session_destroy();		
	}
	
	public function is_logged_in()
	{
		return $this->logged_in;
	}
	
	private function check_login()
	{
		if(isset($_SESSION['user_id']))
		{
			$this->user_id = $_SESSION['user_id'];
			$this->logged_in = true;
			//Check for maintenance mode to log user out
			if(MAINTENANCE_MODE && $this->user_id != 'admin'){
				session_destroy();
				redirect_to(LOGIN_PAGE_ADDR);
			}
		}
		else
		{
			unset($this->user_id);
			$this->logged_in = false;
		}
		
		
		if(isset($_SESSION['privilege_level']))
		{
			$this->privilege_level = $_SESSION['privilege_level'];
		}
		else
		{
			unset($this->privilege_level);
		}
		
		if(isset($_SESSION['contacts_only'])){
			$this->contacts_only = $_SESSION['contacts_only'];
		}
		else
		{
			unset($this->contacts_only);
		}
	}
}

?>