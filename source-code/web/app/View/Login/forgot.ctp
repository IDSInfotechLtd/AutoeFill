<div id="login">
<table border="0" cellpadding="0" cellspacing="0">
<tr>
	<td><h2><?php echo SITE_NAME; ?><!--<img alt="Logo" src="<?php echo SITE_HTTP_URL?>img/admin/logo.png" />--></h2></td>
</tr>
</table>
<div class="div1">Please enter your details.</div>
	<div class="div2">
	<form name="loginform" id="loginform" action="<?php echo SITE_HTTP_URL ?>login/forgot" method="post">			
 	<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td align="center" colspan="2">
			<?php if(isset($errMsg)) { ?>
			 <div class="cff0000 err" ><?php echo $this->Utility->display_message($errMsg,'errmsg',1); ?></div>
			<?php } ?>
			 <?php echo $this->Session->flash();?> 
		</td>
		</tr>
	 
		<tr>
		<td>Email Address:<br/>
		<td><?php echo $this->Form->input('Admin.sLoginName',array('label' => false,'class'=>'txtbox','div'=>'','maxlength'=>'45')); ?>					
					</td>
		</tr>
		</tr>
		
	 
		<tr>
		<td align="center" colspan="2">
		<?php echo $this->Form->submit('Submit',array('label' => false,'class'=>'button','div'=>'')); ?>
		</td>
		</tr>
	</table>
 	</form>
 </div>
<div class="div3"/></div>
</div>