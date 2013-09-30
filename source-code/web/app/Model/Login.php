<?php
	class Login extends AppModel {
		var $name = 'User'; 
                var $useTable= "users";
		var $errMsg=array();
		var $err= 0;	
		
		
		function getUserData($postArray){
                    
                    $username = trim($postArray["Admin"]["sLoginName"]);
                    $password = md5($postArray["Admin"]["sPassword"]);
                    $rs= $this->find('first',array('conditions' => array(
                    'Password' => $password ,'UserName' =>$username
                    )));
                    if(!empty($rs)){
                       return $rs; 
                    }else{
                        return false;
                    }
                        
			
		}
                function getFormData($user_id){
                    if($user_id!="1")
                    {
                        $cond = " and tbl_users.UserId='".$user_id."'";
                    }
                     $sql = "select * from tbl_gforms inner join tbl_categories on tbl_gforms.Category =  tbl_categories.CategoryId inner join tbl_users on tbl_gforms.UploadedBy = tbl_users.UserId where tbl_gforms.isDeleted=0".$cond;
                    $rs = $this->query($sql);
                    return $rs;
                    
                }
                function insertFormData($postArray,$user_id,$name,$location){
                    //pr($postArray);die;
                    if($postArray['levels'] == '1')
                    {
                        $country_id = '1';
                        $state_id = "";
                        $district_id="";
                    }else{
                        $country_id = $postArray['countries'];
                        $state_id = $postArray['states'];
                        $district_id=$postArray['districts'];
                    }
                     $sql = "insert into tbl_gforms set FormName='".$postArray['form_name']."',
                                                       FormDirectory ='".$name."',
                                                       FormLocation = '".$location."',
                                                       Category='".$postArray['categories']."',
                                                       DistrictId='".$district_id."',
                                                       StateId='".$state_id."',
                                                       CountryId='".$country_id."',
                                                       FormLevel='".$postArray['levels']."',
                                                       UploadedBy='".$user_id."'";
                    $rs = $this->query($sql);
                    return true;;
                        
                                                        
                    
                }
                function deleteForm($id,$user_id){
                    $sql = "update tbl_gforms set isDeleted=1,isDeletedOn='".date('Y-m-d h:i:s')."', isDeletedBy='".$user_id."' where FormId='".$id."'";
                    $rs = $this->query($sql);
                    return true;
                }
                
                function saveProfile($postArray,$user_id)
                {
                   // pr($postArray);echo date('d-m-y h:i:s');die;
                     $sql = "update tbl_users set FirstName='".$postArray['data']['User']['name']."',
                                                       LastName ='".$postArray['data']['User']['surname']."',
                                                       Email='".$postArray['data']['User']['email']."',
                                                       Mobile='".$postArray['data']['User']['phone']."',
                                                       UserName ='".$postArray['data']['User']['uname']."',
                                                       LastModified = '1',
                                                       LastModifiedBy = '".$user_id."',
                                                       LastModifiedOn = '".date('Y-m-d h:i:s')."'
                                                       where UserId='".$user_id."'";
                    $rs = $this->query($sql);
                    return true;
                    
                }
                
                /**
	 * Function to validate admin change password.
	 */
	function validPassword($postArray,$userid)
	{
           
		if(isset($postArray['sOldPassword'])) {
			if(empty($postArray['sOldPassword'])) {
				$this->errMsg[]=ERR_OLDPASSWORD_EMPTY;
				//$this->errMsg[]= __('Err_oldpwd_empty', true);
				$this->err=1;
			} elseif(!$this->__isPasswordValid($postArray['sOldPassword'],$userid)) {
				$this->errMsg[]=ERR_OLDPASSWORD_INVALID;
				$this->err=1;
			}
		
			if($this->err != 1){
				if(empty($postArray['sPassword']) && empty($postArray['sConfirmPassword']) ) {
					$this->errMsg[]=ERR_NEWPWD_CONFIRMPWD_EMPTY;
					$this->err=1;
				} elseif (empty($postArray['sPassword'])) {
					$this->errMsg[]=ERR_PASSWORD_NEW_EMPTY;
					$this->err=1;
				} elseif (empty($postArray['sConfirmPassword'])) {		
					$this->errMsg[]=ERR_CONFIRMPASSWORD_EMPTY;
					$this->err=1;
				} elseif (trim($postArray['sPassword'])!=trim($postArray['sConfirmPassword'])) {		
					$this->errMsg[]=ERR_PASSNOTMATCH_EMPTY;
					$this->err=1;
				}
			}
		}
		return $this->err;
	}//EF
        
        /**
	 * Function to validate the old password.
	 */
	function __isPasswordValid($oldPassword,$userid){
                $data= $this->find('first',array('conditions' => array(
                    'Password' => md5($oldPassword) ,'UserId' =>$userid
                    )));
		
                
                
		if($data){
			return true;
		}
		return false;
	}//EF
        
        function savePassword($passwd,$adminId){
            $sql = "update tbl_users set Password='".$passwd."' where UserId='".$adminId."'";
            $rs = $this->query($sql);
            return true;
            
            
        }
        // function to check wheather admin email is right or not.
		// Jatin Seth
		function validUserExists($email_Id){

			$data= $this->find('first',array('conditions' => array(
                        'Email' => $email_Id 
                        )));
                        if(!empty($data))
                        {
                            return $data;
                        }else{
                            return false;
                        }

		}
                function savenewPassword($email,$newPassword){
                    $sql = "update tbl_users set Password='".$newPassword."' where Email='".$email."'";
                    $rs = $this->query($sql);
                    return true;
                    
                }
                
                function getdeletedforms($user_id){
                     if($user_id!="1")
                    {
                        $cond = " and u.UserId='".$user_id."'";
                    }
                    $sql = "select f.FormId,f.FormName,c.CategoryName,u.FirstName,f.isDeletedOn,f.DownloadCount from tbl_gforms f inner join tbl_categories c on f.Category =  c.CategoryId inner join tbl_users u on f.IsDeletedBy = u.UserId where f.isDeleted=1".$cond;
                    $rs = $this->query($sql);
                    return $rs;
                }
                function restoreform($id){
                   $sql = "update tbl_gforms set isDeleted=0 where FormId='".$id."'";
                   $rs = $this->query($sql);
                   return true;;
                   
               }
                function UserEmailIdExistance($email,$user_id){

		$rs= $this->find('first',array('conditions' => array(
                    'Email' =>  $email
                    )));
                $sql = "select * from tbl_users where Email = '".$email."' and UserId != '".$user_id."'";
                $rs = $this->query($sql);
		if(!empty($rs)){
                       return false; 
                    }else{
                        return true;
                    }
	}
}
?>