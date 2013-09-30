<?php 
class LoginController extends AppController 
{ 
    
    var $name = 'Login'; 
    var $errors = array();
    var $uses = array('Login','Category','Country','State','District','User','CakeEmail');
    var $helpers = array('Session','Utility');
	/**
	 * Function to display login form.
	 * Validate user after submit the form.
	 * Allow the user to login if correct details entered.
	 * 
	 */
	function admin_index() 
	{
		$this->layout = 'admin_login';
		if($this->Session->check("ADMIN_DATA")){
			$this->redirect("/login/dashboard");
		}
		if(!empty($this->data)){
			if($this->_validate_admin_login_data()){
				$login_user = $this->Login->getUserData($this->data);
                             //   pr($login_user);die;
				if(!empty($login_user)){
					$this->Session->setFlash("Welcome to Admin Panel.");
					$this->Session->write('ADMIN_DATA',$login_user);
                                       // pr($this->Session);die;
					$this->redirect("/login/dashboard");
					exit;
				}else{
					$this->errors["error"] = "Either the username or the password is incorrect. Please try again.";
					$this->set('errMsg',$this->errors);
				}
			}else{
				$this->set('errMsg',$this->errors);
			}
		}
	}//EF 

	// to validate the admin login data
	function _validate_admin_login_data(){
		$username = $this->data["Admin"]["sLoginName"];
		$password = $this->data["Admin"]["sPassword"];
                
		if($username == ""){
			$this->errors["username"] = "Please Provide the valid username.";
		}

		if($password == ""){
			$this->errors["password"] = "Please Provide the valid password.";
		}

		if(empty($this->errors)){
			return true;
		}
		return false;
	}

	// show admin dashboard 
	function dashboard(){
            
		$this->layout = 'default_admin';
		$this->validate_admin_session();
                $session_data = $this->Session->read("ADMIN_DATA");
                $user_id =  $session_data['Login']['UserId'];
                if(!empty($_POST)) {
                    if($this->_validate_admin_user_data($user_id)){
                    
			

                $result	=	$this->Login->saveProfile($_POST,$user_id);		
                if($result == true){
                        $this->Session->setFlash("Profile Information has been saved successfully.");
                        $this->redirect(SITE_HTTP_URL.'/login/dashboard/');
                }
                    }
		else{
                    $this->set('errMsg',$this->errors);
                }	
		}
                $myprofile=  $this->User->find('all',array(
                'conditions' => array('UserId' => $user_id),
                 
                ));
                
                $this->request->data["User"]["name"] = $myprofile[0]["User"]["FirstName"];
		$this->request->data["User"]["surname"] = $myprofile[0]["User"]["LastName"];
                $this->request->data["User"]["email"] = $myprofile[0]["User"]["Email"];
                $this->request->data["User"]["phone"] = $myprofile[0]["User"]["Mobile"];
                $this->request->data["User"]["uname"] = $myprofile[0]["User"]["UserName"];

                //pr($myprofile);die;
                
	
	}

	function validate_admin_session(){
		if(!$this->Session->check("ADMIN_DATA")){
			$this->Session->setFlash("You are not authorized to access this page.");
			$this->redirect("/login/admin_index");
			exit;
		}
		
	}
        /**
	 * Function to get the list of pages.
	 *  Jatin Seth
	 */
	function viewForms() 
	{
            
        $this->layout = 'default_admin';
        $this->validate_admin_session();
        $session_data = $this->Session->read("ADMIN_DATA");
        $user_id =  $session_data['Login']['UserId'];
        
        $forms_data = $this->Login->getFormData($user_id);
        // pr($forms_data);die;
        $this->set("pagination_data", $forms_data);
               
                
		
	}
        function addForms() 
	{
            
               $this->layout = 'default_admin';
		$this->validate_admin_session();
                $categories =   $this->Category->find('all',array(
                'fields' => array('CategoryId', 'CategoryName'),
                'conditions' => array('IsDeleted' => 0),
                'order' => array('CategoryName' => 'asc')

                ));
                $this->set("Categories", $categories);
                $countries=  $this->Country->find('all',array(
                'fields' => array('CountryId', 'CountryName'),
                'conditions' => array('IsDeleted' => 0),
                 'order' => array('CountryName' => 'asc')
                ));
                 $this->set("Countries", $countries);
                 $states=  $this->State->find('all',array(
                    'fields' => array('StateId', 'StateName'),
                    'conditions' => array('IsDeleted' => 0),
                    'order' => array('StateName' => 'asc')
                    )
                );
                 $this->set("States", $states);
		if(!empty($_POST))
		{
                    $session_data = $this->Session->read("ADMIN_DATA");
                    $user_id =  $session_data['Login']['UserId'];
                   // pr($_FILES);die;
                    
                    if($_FILES["zip_file"]["name"]) {
                        $filename = $_FILES["zip_file"]["name"];
                        $source = $_FILES["zip_file"]["tmp_name"];
                        $type = $_FILES["zip_file"]["type"];
     
                        $name = explode(".", $filename);
                        $accepted_types = array('application/zip', 'application/x-zip-compressed', 'multipart/x-zip', 'application/x-compressed');
                        foreach($accepted_types as $mime_type) {
                        if($mime_type == $type) {
                        $okay = true;
                        break;
                        }
                        }
     
                        $continue = strtolower($name[1]) == 'zip' ? true : false;
                        if(!$continue) {
                        $message = "The file you are trying to upload is not a .zip file. Please try again.";
                        }
     
                        /* PHP current path */
                        $path = HTML_PATH; // absolute path to the directory where zipper.php is in
                        $filenoext = basename ($filename, '.zip'); // absolute path to the directory where zipper.php is in (lowercase)
                        $filenoext = basename ($filenoext, '.ZIP'); // absolute path to the directory where zipper.php is in (when uppercase)

                        $targetdir = $path . $filenoext; // target directory
                        $targetzip = $path . $filename; // target zip file
                        
                        /* create directory if not exists', otherwise overwrite */
                        /* target directory is same as filename without extension */
                        
                        if(is_dir($targetdir))
                        {$this->_rmdir_recursive($targetdir);}
                        
     
                        //echo $targetdir;die;
                        mkdir($targetdir, 0777);
     
     
                        /* here it is really happening */

                        if(move_uploaded_file($source, $targetzip)) {
                        $zip = new ZipArchive();
                        $x = $zip->open($targetzip); // open the zip file to extract
                        
                        if ($x === true) {
                        $zip->extractTo($targetdir); // place in the directory with same name
                        $allTypesOfFiles = array();
                        $wavs = $this->_getAllFiles($targetdir,$allTypesOfFiles);
                        $location = $wavs['0'];
                        $zip->close();

                        unlink($targetzip);
                        }
                        $message = "Your .zip file was uploaded and unpacked.";
                        } else {	
                        $message = "There was a problem with the upload. Please try again.";
                        }
                        $res = $this->Login->insertFormData($_POST,$user_id,$name['0'],$location);
                        if($res==1)
                            {
                                    $this->Session->setFlash("Application form has been added.");
                                    $this->redirect("viewForms");
                            }
                        }
                        }
                        
               
                
		
	}
        function getDistricts()
        {
            
             $this->autoRender=false;
             $this->Layout='ajax';
            $this->validate_admin_session();
             
             $districts=  $this->District->find('all',array(
             'fields' => array('DistrictId', 'DistrictName'),
             'conditions' => array('IsDeleted' => 0,'StateId'=>$_POST['state']),
             'order' => array('DistrictName' => 'asc')
             )
         );
           echo '<select name="districts"><option value="">(choose one)</option>';
               foreach($districts as $value){
               echo '<option value="'.$value['District']['DistrictId'].'">'.$value['District']['DistrictName'].'</option>';
               }
               echo '</select>';
                                                
             
        }
        
        function deleteForms($id='id'){
            $this->autoRender=false;
            $session_data = $this->Session->read("ADMIN_DATA");
            $user_id =  $session_data['Login']['UserId'];
            $res=$this->Login->deleteForm($id,$user_id);
		if($res==true)
		{
			$this->Session->setFlash("Application Form has been deleted.");
			$this->redirect("viewForms");
                }
		
            
        }
        function _rmdir_recursive($dir) {
            foreach(scandir($dir) as $file) {
            if ('.' === $file || '..' === $file) continue;
            if (is_dir("$dir/$file"))
              $this-> _rmdir_recursive("$dir/$file");
            else unlink("$dir/$file");
            }

            rmdir($dir);
        }
        function admin_logout() {

		$this->Session->delete("ADMIN_DATA");
		$this->Session->setFlash("You have Successfully Logged out.");
		$this->redirect("/login/admin_index");
		exit();

	}
        function forgot(){
            $this->layout = 'admin_login';
            if(!empty($this->data)){
			$_response = $this->Login->validUserExists($this->data['Admin']['sLoginName']);

			if($_response !== false){
				$_string_new_password = time();
				$newPassword = md5($_string_new_password);
				$_response_2 = $this->Login->savenewPassword($this->data['Admin']['sLoginName'],$newPassword);
				if($_response_2){

					$this->__sendForgotPassword($_response,$this->data['Admin']['sLoginName'],$_string_new_password);

					$this->Session->setFlash("New password is sent to your registered email.");
					$this->redirect(SITE_HTTP_URL.'login/forgot');
				}else{
					$this->Session->setFlash("There is an error during the reset password process.");
					$this->redirect(SITE_HTTP_URL.'login/forgot');
				}
			}else{
				if(empty($this->data['Admin']['sLoginName'])){
					$msg=array(ERR_EMPTY_FORGOT);
				}else{
					$msg=array(ERR_INVALID_FORGOT);
				}
				$this->set('errMsg',$msg);
			}//End email check

		}//End data check
        }
        /**
	 * Function to display change password form.
	 * Save the new password.
	 * 
	 */	
	function changepass($adminId="")
	{
		
		$this->layout = 'default_admin';
                $this->validate_admin_session();
		$session_data = $this->Session->read("ADMIN_DATA");
                $adminId =  $session_data['Login']['UserId'];
			
		if(!empty($_POST)) {
			if(!$this->Login->validPassword($this->params['data']['Admin'],$adminId)){
				$this->Login->savePassword(md5($this->data['Admin']['sPassword']),$adminId);
				$this->Session->setFlash(MSG_PASSWORD_CHANGED);
				$this->redirect(SITE_HTTP_URL.'/login/dashboard/');
			}
		}
		$this->set("adminId",$adminId);
		$this->set("errMsg",$this->Login->errMsg);
	}//EF
        
        
        /**
	 * Function to send email after reseting admin password
	 * some variables are defined in site_constant.php
	 * eg. ADMIN_EMAIL
	 */	
	Private function __sendForgotPassword($_response,$email_id,$newPassword){



		$body ="Dear ".$_response["Login"]["FirstName"].",<br/><br/>

				Here are your new login details:<br/>
				Username: ".$_response["Login"]["UserName"]."<br/>
				Password: ".$newPassword." <br/><br/>

				Please use the URL below to login to the secure Admin area:<br/>
				<a href='".SITE_HTTP_URL."login/admin_index'>".SITE_HTTP_URL."login/admin_index</a><br/><br/>

				Thanks & Regards,<br/>
				Website Support <br/>
				Auto-e-filling <br/>";

                    
               // echo $body;die;
                              
                
                
                App::uses('CakeEmail', 'Network/Email');
                $email = new CakeEmail();
                
                $email->emailFormat('html')
                        ->from(array('noreply@autoefilling.com' => 'Auto-e-filling'))
                        ->to($email_id)
                        ->subject('Forgot Password')
                        ->send($body);
                return;
	}//EF		
        
        
        function viewDeletedForms(){
            $this->layout = 'default_admin';				
            $this->validate_admin_session();
            $session_data = $this->Session->read("ADMIN_DATA");
            $user_id =  $session_data['Login']['UserId'];
            $forms_data = $this->Login->getdeletedforms($user_id);
            //pr($forms_data);
            $this->set("pagination_data", $forms_data);
        }
        function restoreForm($id='id'){
             $this->autoRender=false;
            $this->validate_admin_session();
             $res=$this->Login->restoreform($id);
             if($res==true)
		{
			$this->Session->setFlash("Application Form has been restored.");
			$this->redirect("viewDeletedForms");
                }
            
            
        }
        
        function showForm(){
             $this->layout = 'ajax';			
        }
         // validate User Information
	function _validate_admin_user_data($user_id){
           
		$email = $this->data["User"]["email"];
		$name = $this->data["User"]["name"];
		$surname = $this->data["User"]["surname"];
                $uname = $this->data["User"]["uname"];
                $phone = $this->data["User"]["phone"];

		$not = "/^([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+)*(@[a-zA-Z]+)(\.[a-z]{2})*(\.[a-z]{2,3})$/";
		if($email == '' || !preg_match($not,$email,$regs)){
			$this->errors['email'] = "Email id is not valid";
		}elseif(!$this->Login->UserEmailIdExistance($email,$user_id)){
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
        function _getAllFiles($dir,$allFiles,$extension = null){
            $files = scandir($dir);
             foreach($files as $file){           
            if(is_dir($dir.'/'.$file)) {
               // $allFiles = getAllFiles($dir.'/'.$file,$allFiles,$extension);
            }else{
                if(empty($extension)){
                    array_push($allFiles,$file);
                }
            }
        }
        return $allFiles;
}
        

}
?>