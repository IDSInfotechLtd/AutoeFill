<?php
	class User extends AppModel {
		var $name = 'User'; 
                var $useTable= "users";
		var $errMsg=array();
		var $err= 0;	
		
		
		function insertUser($postArray,$user_id,$un_id){
                    
                   
                     $sql = "insert into tbl_users set FirstName='".$postArray['data']['User']['name']."',
                                                       LastName ='".$postArray['data']['User']['surname']."',
                                                       Email='".$postArray['data']['User']['email']."',
                                                       Mobile='".$postArray['data']['User']['phone']."',
                                                       UserName ='".$postArray['data']['User']['uname']."',
                                                       Password='".md5($un_id)."',
                                                       RoleId='1',
                                                       CreatedBy='".$user_id."'";
                    $rs = $this->query($sql);
                    return true;;
                        
			
		}
               function deleteUser($id,$user_id)
               {
                   $sql = "update tbl_users set isDeleted=1, isDeletedOn='".date('Y-m-d h:i:s')."', isDeletedBy='".$user_id."' where UserId='".$id."'";
                   $rs = $this->query($sql);
                   return true;;
               }
               function getusers(){
                   $sql = "SELECT u.UserId as UserId, u.FirstName as FirstName,u.LastName as LastName, u.Email as Email, u.UserName as UserName,u.Createdon as Createdon,us.FirstName as CreatedBy FROM tbl_users u LEFT JOIN tbl_users us ON ( u.CreatedBy = us.UserId ) WHERE u.CreatedBy <>0 and u.isDeleted=0";
                   $rs = $this->query($sql);
                   return $rs;;
                   
               }
               function getdeletedusers(){
                   $sql = "SELECT u.UserId as UserId, u.FirstName as FirstName,u.LastName as LastName, u.Email as Email, u.UserName as UserName,u.isDeletedOn as DeletedOn,us.FirstName as DeletedBy FROM tbl_users u LEFT JOIN tbl_users us ON ( u.IsDeletedBy = us.UserId ) WHERE u.isDeleted=1";
                   $rs = $this->query($sql);
                   return $rs;;
                   
               }
               function activateUser($id){
                   $sql = "update tbl_users set isDeleted=0 where UserId='".$id."'";
                   $rs = $this->query($sql);
                   return true;;
                   
               }
               function UserEmailIdExistance($email){

		$rs= $this->find('first',array('conditions' => array(
                    'Email' =>  $email
                    )));
		if(!empty($rs)){
                       return false; 
                    }else{
                        return true;
                    }
	}
        }
?>