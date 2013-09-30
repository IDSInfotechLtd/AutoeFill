<div id="login">
<table border="0" cellpadding="0" cellspacing="0">
<tr>
				<td><h2><?php echo SITE_NAME; ?> Admin Panel<!--<img alt="Logo" src="<?php echo SITE_HTTP_URL?>img/admin/logo.png" />--></h2></td>
			</tr>

</table>

<div class="div1">Please enter your login details.</div>

	<div class="div2">
	<form name="loginform" id="loginform" action="<?php echo SITE_HTTP_URL ?>login/admin_index" method="post">

	<table border="0" cellpadding="0" cellspacing="0">
	<tr>
	<td align="center" colspan="2">
		<?php if(isset($errMsg)) { ?>
		 <div class="cff0000 err" ><?php echo $this->Utility->display_message($errMsg,'errmsg'); ?></div>
		<?php } ?>
		 <?php //$session->flash();?> 
	</td>
	</tr>

	<tr>
	<td align="center" rowspan="3"><img alt="Please enter your login details." src="<?php echo SITE_HTTP_URL ?>img/login.png"/></td>
	</tr>

	<tr>
	<td>Username:<br/>
	
	<?php echo $this->Form->input('Admin.sLoginName',array('id'=>'sLoginName','label' => false,'class'=>'txtbox','div'=>''));?>
	
	<br/>
	<br/>
	Password:<br/>
	
	<?php echo $this->Form->password('Admin.sPassword',array('id'=>'sPassword','label' => false,'class'=>'txtbox')); ?>
	</td>
	</tr>
	</tr>
	
	<tr>
	<td valign="middle" colspan="2" align="left" class="forgot"><a href="<?php echo SITE_HTTP_URL ?>login/forgot">Forgot Password?</a></td>
	</tr>
	<tr>
	<td align="center" colspan="2">
	
	<?php echo $this->Form->submit('Login',array('label' => false,'class'=>'button','div'=>'')); ?>
	</td>
	</tr>
	</table>

	</form>
	</div>


<div class="div3"/></div>
</div>