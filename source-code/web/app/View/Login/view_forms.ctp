  	<table cellspacing="0" cellpadding="0" border="0" width="96%" class="table_margin">
	 <tbody>
		<tr>
			<td height="20" align="left" class="pageimg"><b>Application Forms</b></td>
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

						<td align="right" colspan="8"><b><td align="right" colspan="8"><b><?php echo $this->Html->link("Add New Application Form",'/login/addForms', array('escape' => false)); ?></b></td></b></td>
					</tr>
                                        <tr>
						<!--<td><a href="<?php echo SITE_HTTP_URL ?>admin/pages/add/">Add New Page</a></td> -->

						<td align="right" colspan="8"><b><td align="right" colspan="8"><b>&nbsp;</b></td></b></td>
					</tr>
                                        <tr>
						<!--<td><a href="<?php echo SITE_HTTP_URL ?>admin/pages/add/">Add New Page</a></td> -->

						<td align="right" colspan="8"><b><td align="right" colspan="8"><b><?php echo $this->Html->link("View Deleted Application Forms",'/login/viewDeletedForms', array('escape' => false)); ?></b></td></b></td>
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
				
				<td class="white_font" height="26px">Sr No.</td>

				<td class="white_font" height="26px">Form Name</td>

				<td class="white_font" height="26px">Category</td>

				<td class="white_font" height="26px">Uploaded On</td>
                                
                                <td class="white_font" height="26px">Uploaded By</td>
                                <?php $user_id = $_SESSION['ADMIN_DATA']['Login']['UserId'];
                                if($user_id=="1"){?>
                                
                                <td class="white_font" height="26px"></td>
                                <?php } ?>

		  </tr>		
			
			<?php 
			$row='1';
			if($pagination_data){ ?>	
				<div style="display: none;">
				<input type="checkbox" id="chkRecordId" name="del[]" value="0" disabled/>
				</div>		  
				<?php $i=1;
                                        foreach($pagination_data as $formList){
                                  //  pr($pagination_data);die;
					$cssClass=($row%2)?'gridrow1':'gridrow2';
					?>
					<tr class=<?php echo $cssClass; ?>>				
						
						
						<td class="text" align="left">
						<?php echo $i; ?>
						</td>

						<td class="text" align="center"> 	
						   	<?php echo $formList["tbl_gforms"]["FormName"]; ?>
						</td>

						<td class="text" align="center"> 	
						   	<?php echo $formList["tbl_categories"]["CategoryName"]; ?>
                                                </td>

					    <td class="text" align="center"> 	

						 <?php echo date( "Y-m-d",strtotime($formList["tbl_gforms"]["UploadedOn"])); ?>  	
                                            </td>
                                            <td class="text" align="center"> 	
						 <?php echo $formList["tbl_users"]["UserName"]; ?>  	
                                            </td>
                                            <?php
                                            
                                            if($user_id=="1"){?>
                                            <td class="text" align="center"> 	
						<?php 
							echo $this->Html->link($this->Html->image("delete.gif",array("title"=>"Delete Provider", "alt"=>"Delete Provider")),'deleteForms/'.$formList["tbl_gforms"]["FormId"], array('escape' => false),'Are you sure you wish to delete the selected record(s)?'); ?>
							   	
                                            </td>
                                            <?php } ?>
                                           
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