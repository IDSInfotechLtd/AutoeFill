<script type="text/javascript">
function validate(){
	var oldPassword	=	$('#oldPassword').val();
	var sPassword	=	$('#sPassword').val();
	var confirm_password	=	$('#confirm_password').val();

	if(oldPassword.length == 0) {
		alert("Please enter old password.");
		$('#oldPassword').focus();
		return false;
	}

	if(sPassword.length == 0) {
		alert("Please enter new password.");
		$('#sPassword').focus();
		return false;
	}
	if(sPassword.length > 0 && sPassword.length < 6) {
		alert("Password should be atleast six characters.");
		$('#sPassword').focus();
		return false;
	}
	if(confirm_password.length == 0) {
		alert("Please confirm your password.");
		$('#confirm_password').focus();
		return false;
	}
	if(sPassword != confirm_password) {
		alert("Passwords do not match. Please try again.");
		$('#confirm_password').focus();
		return false;
	}
	return true;
}
</script>
<form action="<?php echo SITE_HTTP_URL ?>login/changepass/<?php echo $adminId ?>" name="manage_adminstrators" id="changeAdminPass" method="post" onsubmit="return validate();">

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
				Change Password
			</b></td>
		</tr>
	
		<tr>
			<td colspan="4" class="gray_line" height="5px;"></td>
		</tr>
		
		<tr>
			<td height="25">&nbsp;</td>
		</tr>
		
		<tr>
			<td colspan="4" class="td2"></td>
		</tr>
		
		<tr>
		 	<td colspan="4">
				<table width="100%" cellpadding="0" cellspacing="0" border="0" class="addeditform">
					<tr class="tablebggrey">
						<td colspan="4" height="25" style="padding-left:10px;" class="formtitle"><b>Change Password</b></td>
					</tr>
					
					<tr>
						<td height="10" colspan="4"></td>
					</tr>
				
					<tr>
						<td width="0%" align="left" valign="top">&nbsp;</td>
						<td width="31%" align="left" valign="top" class="text"><b>Current Password<font color="#FF0000">*</font></b></td>
						<td width="0%" valign="top" style="padding-top:4px;"><b></b></td>
						<td width="69%" align="left" valign="top">
						<?php echo $this->Form->password('Admin.sOldPassword',array('id'=>'oldPassword','class'=>'manage_Input','maxlength'=>20,'label'=>false)); ?>
						</td>
					</tr>
				
					<tr>
						<td height="10" colspan="4"></td>
					</tr>
					
					<tr>				
						<td valign="top">&nbsp; </td>
						<td align="left" valign="top" class="text"><b>New Password<font color="#FF0000">*</font></b></td>
						<td valign="top" style="padding-top:4px;"><b></b></td>
						<td align="left" valign="top">
						<?php echo $this->Form->password('Admin.sPassword',array('id'=>'sPassword','class'=>'manage_Input','maxlength'=>20,'label'=>false)); ?>
						</td>
					</tr>
					
					<tr>
						<td height="10" colspan="4"></td>
					</tr>
					
					<tr>
						<td valign="top">&nbsp; </td>
						<td align="left" valign="top" class="text"><b>Confirm Password<font color="#FF0000">*</font></b></td>
						<td valign="top" style="padding-top:4px;"><b></b></td>
						<td align="left" valign="top">
						<?php echo $this->Form->password('Admin.sConfirmPassword',array('id'=>'confirm_password','class'=>'manage_Input','maxlength'=>20,'label'=>false)); ?>
						</td>
					</tr>
					
					<tr>
						<td height="10" colspan="4"></td>
					</tr>
					
					<tr>
						<td valign="top" colspan="4" class="highlitetext">&nbsp;</td>
					</tr>
					
					<tr align="left">
						<td colspan="3" valign="top"></td>
						<td valign="top" style="float:left">
						<span style='float:left'><?php echo $this->Form->submit('Save',array('class'=>'button')); ?></span>
						&nbsp;<span>
						<input type="button" value="Cancel" name="B2" class="button" OnClick="javascript:window.location='<?php echo SITE_HTTP_URL;?>/login/dashboard'">
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



