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

/**
* Database classes and functions by Matt Brooks. 2010-05-05. V0.1
*/

/**
* Database class for MySQL
*/
class MySQLDatabase{

	/*Database Attributes*/
	private $db_host;		//Hostname of Database
	private $database;		//Database to use
	private $db_user;		//Username to login to Database
	private $db_password;	//Password for username used
	private $link;			//Link to store the datbase connection
	private $query_result;	//Stores the results of the last executed query
	private $last_query;    //Last SQL query as a String
	private $debug_mode;	//Allows the use of debug functions if set to true
	private $magic_quotes_active;
	private $real_escape_string_exists;
	
	/**
	* Construct a Database from passed arguments
	*/
	function __construct($host="", $db="", $user="", $passwd="")
	{
		if($host!="" && $db!="" && $user!="" && $passwd!="")
		{
			$this->db_host = $host;
			$this->database = $db;
			$this->db_user = $user;
			$this->db_password = $passwd;
			$this->link = mysql_connect($this->db_host,$this->db_user,$this->db_password);				//Open a new connection
			
			if(!$this->link)
			{
					die('Not connected: ' . mysql_error());
			}
			else
			{
				//Well link was valid but lets reset it so the other functions can reinitialize it
				$this->link = false;
				$this->real_escape_string_exists = function_exists("mysql_real_escape_string");
				$this->magic_quotes_active = get_magic_quotes_gpc();
				$this->debug_off();
			}
		}
	}

	/**
	* Initializes a connection and stores it in $link
	* 
	*/	
	private function db_connect()
	{
		$this->link = mysql_connect($this->db_host,$this->db_user,$this->db_password);				//Open a new connection
		$this->debug_dump('Creating a connection to the database...');
		if(!$this->link)
		{
			if($this->debug_mode)
			{
				die('Not connected: ' . mysql_error());
			}
		}
		else
		{
			$db_selected = mysql_select_db($this->database,$this->link);									//Select the desired database
			if(!$db_selected)																			//If there was an error selecting datbase
			{
				if($this->debug_mode)
				{
					die('Error selecting database: ' . mysql_error());
				}														//Print out a little error message
			}
		}
	}
	
	/**
	* Close current connection
	*/
	private function db_disconnect()
	{
		mysql_close($this->link);																	//Close current connection
	}
	
	/**
	* Execute a query on the current connection, stores it in $query_result and returns it
	* NOTE: Query must be made safe _BEFORE_ passing to this function
	*/
	public function run_unsafe_query($query)
	{
		$this->last_query = $query; //Save this query string
		
		if(!$this->link) //Open the connection if it isn't already
		{
			$this->db_connect();
		}
		
		$result = mysql_query($query,$this->link); //Run the query
		
		if(!$result)
		{
			if($this->debug_mode)//If debug mode is active we can print the last query
			{
				$this->debug_dump('Database Query Failed:');
				die(mysql_error() . ' Query: ' . $this->last_query);
			}
			return false;
		}
		else
		{
			$this->query_result = $result; //Store the query result
			$this->debug_dump('Successfully executed a query!');
			return $result;  //Now return it
		}		
	}
	
	/**
	* Attempt to make data safe for a query
	*
	*/
	public function escape_query_data($query)
	{
		if(!$this->link)//Open the connection if it isn't already
		{
			$this->db_connect();
		}
		
		if($this->real_escape_string_exists)
		{
			if($this->magic_quotes_active)
			{
				$query = stripslashes($query);	
			}
			$query = mysql_real_escape_string($query,$this->link);
		}
		else
		{
			if(!$this->magic_quotes_active)
			{
				$query = addslashes($query);
			}
		}
		return $query;
	}
	
	/**
	* Checks for the existance of a single record based on a single field value in a single table.
	* Preserves the contents of $query_result
	*/
	public function record_key_exists($key,$field,$table)
	{
		$tempResult = $this->query_result;
		
		$query = sprintf("SELECT * FROM %s WHERE %s = %s;",$table,$field,$key);
		$result = $this->run_unsafe_query($query);
		$row = $this->get_array($result);
		$this->query_result = $tempResult;
		
		if(!empty($row[$field]))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	* Checks for the existance of a single record pair based on two fields in a single table.
	* Preserves the contents of $query_result
	*/
	public function record_pair_exists($key1,$field1,$key2,$field2,$table)
	{
		$tempResult = $this->query_result;		
		$query = sprintf("SELECT * FROM %s WHERE %s = %s AND %s = %s",$table,$field1,$key1,$field2,$key2);
		$result = $this->run_unsafe_query($query);
		$row=mysql_fetch_array($result,MYSQLI_ASSOC);
		$this->query_result = $tempResult;
		
		if(!empty($row))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	* Returns the currently sql query as a fetched array
	*/
	public function get_array_from_sql($sql='')
	{
		if($sql === '')
		{
			return FALSE;
		}
		else
		{
			return $this->get_array($this->run_unsafe_query($result));
		}
	}
	
	/**
	* Returns the currently stored query as a fetched array
	*/
	public function get_array($result = '')
	{
		if($result === '')
		{
			return mysql_fetch_array($this->query_result);
		}
		else
		{
			return mysql_fetch_array($result);
		}
	}
	
	/**
	* Returns the number of rows in the result passed
	*/
	public function query_num_rows($result = '')
	{
		if($result === '')
		{
			return mysql_num_rows($this->last_query);
		}
		else
		{
			return mysql_num_rows($result);
		}
	}
	
	/**
	* Get a specific value in a field for a given unique key
	*/
	public function get_record_value($table,$primary_field,$primay_key,$value_field)
	{
								
		$sql = "SELECT $value_field FROM $table WHERE $primary_field = $primay_key;";
		$result = $this->run_unsafe_query($sql);
		$result = $this->get_array($result);

		if(!empty($result))
		{
			return $result[$value_field];
		}
		else
		{
			$this->debug_dump("Just Failed a record value get on: $sql");
			return false;
		}
	}
	
	/**
	* Put a specific value in a field for a given unique key
	*/
	public function put_record_value($table,$primary_field,$primary_key,$value_field,$value="")
	{								
		$sql = "UPDATE $table SET $value_field = $value WHERE $primary_field = $primary_key;";
		$result = $this->run_unsafe_query($sql);
		
		if(!empty($result))
		{
			return true;
		}
		else
		{
			$this->debug_dump("Just Failed a record value put on: $sql");
			return false;
		}
	}
	
	/**
	* Inserts a new single value (does no checks whatsoever)
	*/
	public function insert_new_value($table,$value_field,$value="")
	{								
		$sql = "INSERT INTO $table ($value_field) VALUES ($value)";
		//$sql = "UPDATE $table SET $value_field = $value WHERE $primary_field = $primary_key;";
		$result = $this->run_unsafe_query($sql);
		
		if(!empty($result))
		{
			return true;
		}
		else
		{
			$this->debug_dump("Just Failed a record value put on: $sql");
			return false;
		}
	}	
	
	/**
	*
	*/
	public function delete_row($table,$primary_field,$primary_key)
	{
		$sql = "DELETE FROM $table WHERE $primary_field=$primary_key;";
		$result = $this->run_unsafe_query($sql);
	}
	
	/**
	* Dump the object
	* You should probably never do this unless testing.
	*/
	public function debug_dump($msg = '')
	{
		/**/
		if($this->debug_mode == true)
		{
			if($msg!='')
			{
				echo "<br/><strong>DB DUMP SAYS: $msg</strong>";
			}
			echo '<br/><strong>Dumping Database Object:</strong><br/>';
			echo 'Database Host: ' . $this->db_host . '<br/>';
			echo 'Database User: ' . $this->db_user . '<br/>';
			if(!empty($this->db_password))
			{
				$using_pass = 'Yes';
			}
			else
			{
				$using_pass = 'No';	
			}
			echo 'Database Password: ' . $using_pass . '<br/>';
			echo 'Database Link: ' . $this->link . '<br/>';
			echo 'Database Last Query: ' . $this->last_query . '<br/>';
			echo 'Database Query VAR: ' . $this->query_result . '<br/>';	
		}
	}
	
	/***************************************************************************************************************
	***************************************************************************************************************/
	
	
	/*TODO:*/
	//Add function to XXXX
	
	/**
	* Returns the result of the last query ran
	*/
	public function get_query_result()
	{
			return $this->query_result;
	}
	
	/**
	* Returns the last SQL query string if debug_mode is on
	*/
	public function get_last_query()
	{
		if($this->debug_mode)
		{
			return $this->last_query;
		}
		else
		{
			return false;
		}
	}
	
	/**
	* Returns the last auto incremented ID generated by a query
	*/	
	public function get_last_id()
	{
		if(!empty($this->last_query))
			return mysql_insert_id($this->link);
		else
			return false;
	}
	
	/**
	* Turns debug_mode on
	*/
	public function debug_on()
	{
		$this->debug_mode = true;
	}
	
	/**
	* Turns debug_mode off
	*/
	public function debug_off()
	{
		$this->debug_mode = false;
	}
}
?>