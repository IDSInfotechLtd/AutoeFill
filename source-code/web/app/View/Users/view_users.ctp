  	<table cellspacing="0" cellpadding="0" border="0" width="96%" class="table_margin">
	 <tbody>
		<tr>
			<td height="20" align="left" class="pageimg"><b>User Management</b></td>
		</tr>
		<tr>
			<td height="5px;" class="gray_line" colspan="9"></td>
		</tr>
		
		
		<tr>
			<td height="15">&nbsp;</td>
		</tr>

		<tr>
			<td colspan="9"> 		    
				<table cellspacing="0" cellpadding="0" border="0" width="100%" class="table_margin">		        
					<tr>
						<!--<td><a href="<?php echo SITE_HTTP_URL ?>admin/pages/add/">Add New Page</a></td> -->

						<td align="right" colspan="8"><b><td align="right" colspan="8"><b><?php echo $this->Html->link("Add New User",'/users/addUser', array('escape' => false)); ?></b></td></b></td>
					</tr>
                                        <tr>
						<!--<td><a href="<?php echo SITE_HTTP_URL ?>admin/pages/add/">Add New Page</a></td> -->

						<td align="right" colspan="8"><b><td align="right" colspan="8">&nbsp;</td></b></td>
					</tr>
                                        <tr>
						<!--<td><a href="<?php echo SITE_HTTP_URL ?>admin/pages/add/">Add New Page</a></td> -->

						<td align="right" colspan="8"><b><td align="right" colspan="8"><b><?php echo $this->Html->link("View Deleted Users",'/users/viewDeletedUsers', array('escape' => false)); ?></b></td></b></td>
					</tr>
					<tr><td colspan="9" height="8px;"></td></tr>
				</table>
			</td>
		</tr>
		
		<tr>
			<td colspan="9"> 		    
				<table cellspacing="0" cellpadding="0" border="0" width="100%" class="table_margin">		        
					<tr>
						<td align="right" colspan="8"><b><?php //echo $this->element('pagination/pagination'); ?></b></td>
					</tr>
					<tr><td colspan="9" height="8px;"></td></tr>
				</table>
			</td>
		</tr>	
		
		<tr>
		 <td colspan="9">	
		  <table  border="0" width="100%" cellspacing="1" cellpadding="2" align="center" class="tablebggrey">
			<tr class="">
				
				<td class="white_font" height="26px">Sr.No.</td>
                                <td class="white_font" height="26px">Name</td>
                                <td class="white_font" height="26px">User Name</td>
                                <td class="white_font" height="26px">Email Address</td>
                                <td class="white_font" height="26px">CreatedBy</td>
                                <td class="white_font" height="26px">CreatedOn</td>

				

		  </tr>		
			
			<?php 
			$row='1';
			if($pagination_data){ ?>	
				<div style="display: none;">
				<input type="checkbox" id="chkRecordId" name="del[]" value="0" disabled/>
				</div>		  
				<?php $i=1;foreach($pagination_data as $formList){
                                   // pr($pagination_data);die;
					$cssClass=($row%2)?'gridrow1':'gridrow2';
					?>
					<tr class=<?php echo $cssClass; ?>>				
						
						
						<td class="text" align="left">
						<?php echo $i; ?>
						</td>
                                                <td class="text" align="left">
						<?php echo $formList["u"]["FirstName"]." ".$formList["u"]["LastName"]; ?>
						</td>
                                                <td class="text" align="left">
						<?php echo $formList["u"]["UserName"]; ?>
						</td>
                                                <td class="text" align="left">
						<?php echo $formList["u"]["Email"]; ?>
						</td>
						<td class="text" align="center"> 	
						   	<?php echo $formList["us"]["CreatedBy"]; ?>
						</td>
                                                <td class="text" align="center"> 	
						   	<?php echo date( "Y-m-d",strtotime($formList["u"]["Createdon"])); ?>
						</td>

				

                                            
                                            <td class="text" align="center"> 	
						<?php 
							echo $this->Html->link($this->Html->image("delete.gif",array("title"=>"Delete User", "alt"=>"Delete User")),'deleteUser/'.$formList["u"]["UserId"], array('escape' => false),'Are you sure you wish to delete the selected record(?'); ?>
							   	
                                            </td>
					</tr>
					
					<?php
					$row++;
                                        $i++;
				}?>				
			
			<?php }else{?>
			<tr>
				<td align="center" colspan="9" class="error gridrow1"><b>No records found.</b>&nbsp;</td>
			</tr>
			<?php }?> 
			              
			<tr class="displayhead">
				<td colspan="9">
					 <!--<select name="data[Service][status]" id='JournalOptionSelect'>
				
					<option value="0">--Select--</option>
					<option value="delete">Delete</option>
					</select>
					<input type="submit" name="del_admin" value="Go" />-->
				</td>
			</tr> 				
		  </table>	
	    </td>
	 </tr>	
  </table>
</form>
<br/><br/>