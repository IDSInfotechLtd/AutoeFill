<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html style="height:100%">
<head>
<title><?php echo $title_for_layout; ?></title>
<script>
var siteUrl = "<?php echo SITE_HTTP_URL;?>";
</script>
<?php echo $this->Html->css(SITE_HTTP_URL.'css/menu.css'); ?>
<?php echo $this->Html->css(SITE_HTTP_URL.'css/admin.css'); ?>
<!-- js for dropdown menu -->
<?php //echo $javascript->link('/js/rollover.js'); ?>
<!-- jquery (required for fck,) -->
<?php //echo $this->Javascript->link('/js/jquery-1.4.2.js');
echo $this->Html->script('/js/jquery-1.4.2.js'); ?>

<!-- js for fckeditor -->
<?php //echo $javascript->link('/js/fckeditor/fckeditor.js'); ?>
<!-- Common js for admin panel -->
<?php //echo $javascript->link('/js/adminCommon.js'); ?>
<!-- jQuerry -->



<?php if(isset($addjscss)) echo $addjscss->print_registered(); ?>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery('#biggest').click(function() {
			var small = jQuery(".make_select");
			if(jQuery(this).attr('checked')){
				small.attr('checked','checked');
			}else{
				small.removeAttr('checked');
			}
		});
		jQuery('#user_list').submit(function() {
			if(jQuery("#JournalOptionSelect").val() != '0'){
				var count = 0;
				var small = jQuery(".make_select");
				jQuery.each(small, function(key, value) { 
				  if(jQuery(this).attr("checked")){
						count++;
				  }
				});
				if(count){
					jQuery(this).submit();
				}else{
					alert("Please Select atleast one entry.");
					return false;
				}
			}
			alert("Please Select atleast one entry.");
			return false;
		});
	});
</script>
<link rel="shortcut icon" href="<?php echo SITE_HTTP_URL;?>app/webroot/favicon.ico" type="image/x-icon" />
</head>
<body style="height:100%">
<table border="0" width="100%" height="100%">
<tr>
	<td valign="top" align="left">  



<!--Header-->
<table cellpadding="0" cellspacing="0" width="100%" class="adminheader" border="0">
<tr>
  <td width="38%" class="adminlog" height="65px;" style="padding: 8px 0 8px 10px;">
	<div class="fl" style="width:620px;">
		<div class="fl " style="width:280px;">
			<!--font class="logoText">Great Skills Logo</font>
			<a href="<?php echo SITE_HTTP_URL ?>admin/dashboard" class="logo"-->
			<img alt="Logo" src="<?php echo SITE_HTTP_URL?>img/logo2.png" width="150"/></a>
		</div>
		<div class="fl tc" style="width:326px;"><h1 style="padding-top:25px; color: #000;
    	font-weight: bold;"><?php echo SITE_NAME; ?> Admin Panel</h1></div>
	</div>
 </td>
 <?php
 ?>
  <td width="32%" class="adminlogged" align="right" valign="top" style="padding-top:10px; padding-right:10px;">
  <!--<a href="<?php echo SITE_HTTP_URL ?>">Main Site</a>-->
  <img alt="Administrator" title="Administrator" src="<?php echo SITE_HTTP_URL?>img/adminuser.png" border="0" align="absmiddle" />&nbsp;Logged in as <?php
  $session_data = $this->Session->read("ADMIN_DATA");
  echo $session_data['Login']['UserName']; ?>&nbsp;<!--|&nbsp;<a href="javascript:void(0);">Main Site</a>&nbsp;-->|&nbsp;<?php echo $this->Html->link("Sign Out",array('controller'=>'login','action' => 'admin_logout'),array('escape' => false)); ?></td>
</tr>
</table>

<table cellpadding="0" cellspacing="0" border="0" width="100%" >
	<tr>
   	  <td>	  		
		<?php echo $this->element("admin_menu"); ?>
      </td>
	</tr>
</table>
<!--Header-->

<div id='contentarea'>
<?php echo $this->Session->flash(); ?>
<?php echo $content_for_layout ?></div>

	
	</td>
</tr>
<tr>
	<td valign="bottom" align="left"> 
		<!--Footer-->
		<table cellpadding="0" cellspacing="0" width="100%" >
		<tr>
			<td valign="middle" class="adminfooter">&nbsp;&nbsp;&nbsp;&copy;<?php echo date("Y");?>  <?php echo SITE_NAME; ?>. All rights reserved.</td>
		</tr>
		</table>
		<!--Footer-->
	</td>
</tr>


</table>

</body>
</html>    <?php echo $this->element('sql_dump'); ?>
