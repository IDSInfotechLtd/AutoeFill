<?php 
class UsersController extends AppController 
{ 
	var $name = 'Users'; 
    var $errors = array();
    var $uses = array('User');
    var $helpers = array('Session','Utility');
	
	public function viewUsers() {
		        $this->layout = 'default_admin';				
                        $this->validate_admin_session();
                        $forms_data = $this->User->getusers();
                        //pr($forms_data);
                $this->set("pagination_data", $forms_data);
	} 
	
	public function validate_admin_session(){
		if(!$this->Session->check("ADMIN_DATA")){
			$this->Session->setFlash("You are not authorized to access this page.");
			$this->redirect("/admin/login");
			exit;
		}		
	}
	
	public function addUser() {
		        $this->layout = 'default_admin';
                        $this->validate_admin_session();
                        if(!empty($_POST))
                        {
                            if($this->_validate_admin_user_data()){
                            $session_data = $this->Session->read("ADMIN_DATA");
                            $user_id =  $session_data['Login']['UserId'];
                            $un_id = $this->_uniqueidgen();
                            $email = $_POST['data']['User']['email'];
                            $name = $_POST['data']['User']['name'];
                            $uname = $_POST['data']['User']['uname'];
                            $this->_sendmail($email,$name,$uname,$un_id);                            
                            $res = $this->User->insertUser($_POST,$user_id,$un_id);
                            if($res==1)
                                {
                                        $this->Session->setFlash("A new user has been added.");
                                        $this->redirect("viewUsers");
                                }
                            }else{
                            $this->set('errMsg',$this->errors);
			}
                        }
                            
                        }
          function _uniqueidgen(){
                $d=date ("d");
                $m=date ("m");
                $y=date ("Y");
                $dmt=$d+$m+$y;    
                $un=  uniqid();
                $dmtun = $dmt.$un;
                return $dmtun;
          }
          function _sendmail($email,$name,$uname,$un_id){
              $from = "Admin<Auto-e-filling>";
              $to = $email;
              $subject = "Your account has been created";
              $message = "Dear ".$name.",<br/><br/>Your account has been created on Auto-e-filling.<br/>Login Credentials:<br/>Username: ".$uname."<br/>Password: ".$un_id.".<br/><br/><br/>Your's sincerely,<br/>Admin<br/>Auto-e-filling team.";
              $headers = 'From:Admin<Auto-e-filling> ' . "\r\n" .
                'X-Mailer: PHP/' . phpversion();
              $headers .= "MIME-Version: 1.0\r\n";
              $headers .= "Content-Type: text/html; charset=ISO-8859-1\r\n";
              mail($to, $subject, $message, $headers);
              
          }
           function deleteUser($id='id'){
            $this->autoRender=false;
            $this->validate_admin_session();
            $session_data = $this->Session->read("ADMIN_DATA");
            $user_id =  $session_data['Login']['UserId'];
            $res=$this->User->deleteUser($id,$user_id);
		if($res==true)
		{
			$this->Session->setFlash("User has been deleted.");
			$this->redirect("viewUsers");
                }
		
            
        }
        function viewDeletedUsers(){
                        $this->layout = 'default_admin';				
                        $this->validate_admin_session();
                        $forms_data = $this->User->getdeletedusers();
                        //pr($forms_data);
                $this->set("pagination_data", $forms_data);
            
        }
        function activateUser($id='id'){
             $this->autoRender=false;
            $this->validate_admin_session();
             $res=$this->User->activateUser($id);
             if($res==true)
		{
			$this->Session->setFlash("User has been activated.");
			$this->redirect("viewDeletedUsers");
                }
            
            
        }
        
        // validate User Information
	function _validate_admin_user_data(){
           
		$email = $this->data["User"]["email"];
		$name = $this->data["User"]["name"];
		$surname = $this->data["User"]["surname"];
                $uname = $this->data["User"]["uname"];
                $phone = $this->data["User"]["phone"];

		$not = "/^([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+)*(@[a-zA-Z]+)(\.[a-z]{2})*(\.[a-z]{2,3})$/";
		if($email == '' || !preg_match($not,$email,$regs)){
			$this->errors['email'] = "Email id is not valid";
		}elseif(!$this->User->UserEmailIdExistance($email)){
			$this->errors["email"] = "Email address already exist.";
		}
		
		if($name == ""){
			$this->errors["name"] = "Please Provide the valid First Name.";

		}
		if($surname == ""){
			$this->errors["surname"] = "Please Provide the valid Last Name.";

		}
                if($uname == ""){
			$this->errors["uname"] = "Please Provide the valid User Name.";

		}
                if($phone == ""){
			$this->errors["phone"] = "Please Provide the valid Phone number.";

		}
		if(empty($this->errors)){
			return true;
		}
		return false;
	}
		
	}


?>


