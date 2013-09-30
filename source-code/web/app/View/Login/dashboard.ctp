<script type="text/javascript">
function validate(){
	var name	=	$('#name').val();
	var surname	=	$('#surname').val();
        var uname	=	$('#uname').val();
	var email	=       $('#email').val();
        var phone	=       $('#phone').val();
	

	if(name.length == 0) {
		alert("Please enter first name.");
		$('#name').focus();
		return false;
	}
	if(surname.length == 0) {
		alert("Please enter last name.");
		$('#surname').focus();
		return false;
	}
        if(uname.length == 0) {
		alert("Please enter user name.");
		$('#uname').focus();
		return false;
	}
	if(email.length == 0) {
		alert("Please enter email address.");
		$('#email').focus();
		return false;
	}
	if(email.length > 0) {
		var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
		if(reg.test(email) == false) {
		  alert("Please enter valid email address.");
		  $('#email').focus();
		  return false;
		}
	}
        if(phone.length == 0) {
		alert("Please enter Phone Number.");
		$('#phone').focus();
		return false;
	}
        if(phone.length > 0) {
              //alert(isNaN(phone));
            if(isNaN(phone) !=false || phone.length != 10)  
            {  
                alert("Please enter a valid Phone Number");  
               return false;
            }  
            


        }
	
}
</script>
<form action="" name="paypal" method="post" onsubmit="return validate();">
	<table border="0" cellpadding="0" cellspacing="0" width="96%" class="table_margin">
		<tr>
			<td colspan="4">	
				<?php if(isset($errMsg) && sizeof($errMsg)!='0') { ?>
	    			<div class="errormessage tc">
						<?php echo $this->Utility->display_message($errMsg,'errmsg',1);?>		
		    		</div>
				<?php } ?>
				<div class=" successmsg tc"><?php echo $this->Session->flash();?></div>
		    </td>
		</tr>
		
		<tr>
			<td align="left" height="30" class="pageimg">
			<b>
				Welcome Admin Panel
			</b></td>
		</tr>
	
		<tr>
			<td colspan="4" class="gray_line" height="5px;"></td>
		</tr>
		
		<tr>
			<td height="25">&nbsp;</td>
		</tr>
                <tr>
			<td colspan="9"> 		    
				<table cellspacing="0" cellpadding="0" border="0" width="100%" class="table_margin">		        
					<tr>
						<!--<td><a href="<?php echo SITE_HTTP_URL ?>admin/pages/add/">Add New Page</a></td> -->

						<td align="right" colspan="8"><b><td align="left" colspan="8"><b><?php echo $this->Html->link("Change Password",'/login/changepass', array('escape' => false)); ?></b></td></b></td>
					</tr>
					<tr><td colspan="9" height="8px;"></td></tr>
				</table>
			</td>
		</tr>
		
		<tr>
			<td colspan="4" class="td2"></td>
		</tr>
		
		<tr>
		 	<td colspan="4">
				<table width="60%" cellpadding="0" cellspacing="0" border="0" class="addeditform">
					<tr class="tablebggrey">
						<td colspan="4" height="25" style="padding-left:10px;" class="formtitle"><b>My Profile</b></td>
					</tr>
					
					<tr>
						<td height="10" colspan="4"></td>
					</tr>
				
					<tr>
					<td valign="top">&nbsp; </td>
					<td align="left" valign="top" class="text"><b>First Name<font color="#FF0000">*</font></b></td>
					<td valign="top" style="padding-top:4px;"><b></b></td>
					<td align="left" valign="top">
					<?php echo $this->Form->input('User.name',array('id'=>'name','class'=>'manage_Input','maxlength'=>30,'label'=>false)); ?>
					</td>
				</tr>
				<tr>
					<td height="10" colspan="4"></td>
				</tr>
				<tr>
					<td valign="top">&nbsp; </td>
					<td align="left" valign="top" class="text"><b>Last Name<font color="#FF0000">*</font></b></td>
					<td valign="top" style="padding-top:4px;"><b></b></td>
					<td align="left" valign="top">
					<?php echo $this->Form->input('User.surname',array('id'=>'surname','class'=>'manage_Input','maxlength'=>30,'label'=>false)); ?>
					</td>
				</tr>		
				<tr>
					<td height="10" colspan="4"></td>
				</tr>
                                <tr>
					<td valign="top">&nbsp; </td>
					<td align="left" valign="top" class="text"><b>UserName<font color="#FF0000">*</font></b></td>
					<td valign="top" style="padding-top:4px;"><b></b></td>
					<td align="left" valign="top">
					<?php echo $this->Form->input('User.uname',array('id'=>'uname','class'=>'manage_Input','maxlength'=>30,'label'=>false)); ?>
					</td>
				</tr>
				<tr>
					<td height="10" colspan="4"></td>
				</tr>
				<tr>
					<td valign="top">&nbsp; </td>
					<td align="left" valign="top" class="text"><b>Email<font color="#FF0000">*</font></b></td>
					<td valign="top" style="padding-top:4px;"><b></b></td>
					<td align="left" valign="top">
					<?php echo $this->Form->input('User.email',array('id'=>'email','type'=>'text','class'=>'manage_Input','label'=>false,'div'=>'','readonly'=>FALSE)); ?>	
					</td>
				</tr>
					<tr>
						<td height="10" colspan="4"></td></tr>
					<tr>
                                        <tr>
					<td valign="top">&nbsp; </td>
					<td align="left" valign="top" class="text"><b>Mobile:<font color="#FF0000">*</font></b></td>
					<td valign="top" style="padding-top:4px;"><b></b></td>
					<td align="left" valign="top">
					
					<?php echo $this->Form->input('User.phone',array('id'=>'phone','type'=>'text','class'=>'manage_Input','label'=>false,'div'=>'','readonly'=>FALSE)); ?>	
                                        </td>
				</tr>
					
							
					<tr>
						<td height="10" colspan="4"></td>
					</tr>
					
								
				<tr>
					<td height="10" colspan="4"></td>
				</tr>
				<tr align="left">
					<td colspan="3" valign="top"></td>
					<td valign="top" style="float:left">
					<span style='float:left'>
					<?php echo $this->Form->submit("Save",array('class'=>'button')); ?>
					</span>
					
					</td>
				</tr>
				<tr>
					<td height="10" colspan="4"></td>
				</tr>			
			</table> 
		  </td>
		</tr>		
		<tr><td height="10" colspan="4"></td></tr>
	</table>
</form>
