<?php


/**
 *  Get the Current URL and explode by '/'
 *  Match the value of index 1 of the array.
 *  If value matched then apply the active class to show the menu as active.
 *  
 */

$arrExp = $this->params['action'];
$user_id = $_SESSION['ADMIN_DATA']['Login']['UserId'];



$dashboardActive = "";$usersActive = ''; $profileActive = ''; $formActive = ''; 
$activeVal = "Style ='background: none repeat scroll 0 0 #92B5DF'";

	switch($arrExp){
		case 'dashboard':
			$profileActive = $activeVal;
			break;
                case 'changepass':
			$profileActive = $activeVal;
			break;
                case 'viewUsers':
			$usersActive = $activeVal;
			break;
                case 'addUser':
			$usersActive = $activeVal;
			break;
                case 'viewDeletedUsers':
			$usersActive = $activeVal;
			break;
		case 'viewForms':
			$formActive = $activeVal;
			break;
		case 'addForms':
			$formActive = $activeVal;
			break;
		case 'viewDeletedForms':
			$formActive = $activeVal;
			break;
		
	}
?>
<!--@ND on 25 feb 2013:added height to fl div-->
<div class="fl mainnav" style="height:70px;">
	<table width="100%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="text" style="border:1px solid #cccccc">
	  <tr>
		<td valign="top"  height="23">
		   <div class="suckertreemenu">
			 <ul id="treemenu1">
			 
				<li><a href="#" <?php echo $profileActive?> >Profile</a>
				
				<ul class="sub-menu">
					<li>
						<?php echo $this->Html->link("My Profile",'/login/dashboard', array('escape' => false)); ?>
					</li>
					<li>
						<?php echo $this->Html->link("Change Password",'/login/changepass', array('escape' => false)); ?>
					</li>
				</ul>	
				</li>
                                
				<li style="width:140px;"><a href="#" <?php echo $formActive ?>>Application Forms</a>
						<ul class="sub-menu">
					<li>
						<?php echo $this->Html->link("Add New Application Form",'/login/addForms', array('escape' => false)); ?>
					</li>
					<li>
                                                <?php echo $this->Html->link("View All Application Forms",'/login/viewForms', array('escape' => false)); ?>
						
					</li>
					<li>
						<?php echo $this->Html->link("View Deleted Application Forms",'/login/viewDeletedForms', array('escape' => false)); ?>
					</li>	
				</ul>
				</li>
                              <?php if($user_id=="1"){?>
				<li style="width:140px;"><a href="#" <?php echo $usersActive?>>Users Management</a>
				<ul class="sub-menu">
					<li>
						<?php echo $this->Html->link("Add New User",'/users/addUser', array('escape' => false)); ?>
					</li>
					<li>
                                                <?php echo $this->Html->link("View All Users",'/users/viewUsers', array('escape' => false)); ?>
						
					</li>
					<li>
                                                <?php echo $this->Html->link("View Deleted Users",'/users/viewDeletedUsers', array('escape' => false)); ?>
						
					</li>	
				</ul>
				</li>
                              <?php }?>
		     </ul>	
		  </div>
	      <br clear="all" />
		</td>
	  </tr>	
	</table>	
</div>