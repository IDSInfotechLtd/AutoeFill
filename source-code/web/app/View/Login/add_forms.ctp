<script type="text/javascript">
function validate(){
	var fname	=	$('#form_name').val();
	var level	=	$('#levels').val();
	var categories	=	$('#categories').val();
	var countries	=	$('#countries').val();
        var states	=	$('#states').val();
        var districts	=	$('#districts').val();
        var zip_file	=	$('#zip_file').val();
        var tt = $('#my_var').val();
        

	if(fname.length == 0) {
		alert("Please enter form name.");
		$('#fname').focus();
		return false;
	}
        if(categories.length == 0) {
		alert("Please Choose Category.");
		$('#categories').focus();
		return false;
	}
	if(level.length == 0) {
		alert("Please Choose Level.");
		$('#level').focus();
		return false;
	}
	
	

	if(countries.length == 0 && tt == 0) {
		alert("Please Choose Country.");
		$('#countries').focus();
		return false;
	}
	if(states.length == 0 && tt == 0 ) {
		alert("Please Choose State.");
		$('#states').focus();
		return false;
	}
	if(districts.length == 0 && tt == 0) {
		alert("Please Choose District.");
		$('#districts').focus();
		return false;
	}
        if(zip_file.length == 0) {
		alert("Please Choose File.");
		return false;
	}else{
            Checkfiles();
            

        };
	
	return true;
}
 function Checkfiles()
    {
        var fup = document.getElementById('zip_file');
        var fileName = fup.value;
        var ext = fileName.substring(fileName.lastIndexOf('.') + 1);

    if(ext =="zip" )
    {
        return true;
    }
    else
    {
        alert("Upload zip folders only");
        return false;
    }
    }
</script>
<form id="govt_forms" name="govt_forms" accept-charset="utf-8" method="post" onsubmit="return validate();" enctype="multipart/form-data" action="/open_data_app/login/addForms">
	<table border="0" cellpadding="0" cellspacing="0" width="96%" class="table_margin">
		<tr>
			<td colspan="4">	
			<?php  if(isset($errMsg) && sizeof($errMsg)!='0') { ?>
			<div class="errormessage tc"><?php  echo $utility->display_message($errMsg,'errmsg',1);?>		
			</div>
			<?php } ?>
			</td>
		</tr>		
		<tr>
			<td align="left" height="30" class="pageimg"><b>Add New Application Form</b></td>
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
			<table width="60%" cellpadding="0" cellspacing="0" border="0" class="addeditform">
				<tr class="tablebggrey">
					<td colspan="4" height="25" style="padding-left:10px;" class="formtitle"><b>Add New Application Form</b></td>
				</tr>				
				<tr>
					<td height="10" colspan="4"></td>
				</tr>
				<tr>
						<td width="0%" align="left" valign="top">&nbsp;</td>
						<td width="31%" align="left" valign="top" class="text"><b>Form Name:<font color="#FF0000">*</font></b></td>
						<td width="0%" valign="top" style="padding-top:4px;"><b></b></td>
						<td width="69%" align="left" valign="top">
						<input id="form_name" class="manage_input" type="text" name="form_name">
						</td>
                               </tr>
                               <tr>
                                    <td height="10" colspan="4"></td>
                               </tr>
                                <tr>
						<td width="0%" align="left" valign="top">&nbsp;</td>
						<td width="31%" align="left" valign="top" class="text"><b>Choose Category:<font color="#FF0000">*</font></b></td>
						<td width="0%" valign="top" style="padding-top:4px;"><b></b></td>
						<td width="69%" align="left" valign="top">
                                                
                                                <select name="categories" id="categories">
                                                <option value="">(choose one)</option>
                                                <?php foreach($Categories as $value)
                                                { 
                                                    ?><option value="<?php echo $value['Category']['CategoryId'];?>"><?php echo $value['Category']['CategoryName'];?></option>
                                                <?php }
                                                ?>
                                                
                                                
                                                </select>
						

			      </tr>
                               
                              <tr>
                                    <td height="10" colspan="4"></td>
                               </tr>
                             <tr>
						<td width="0%" align="left" valign="top">&nbsp;</td>
						<td width="31%" align="left" valign="top" class="text"><b>Choose Level:<font color="#FF0000">*</font></b></td>
						<td width="0%" valign="top" style="padding-top:4px;"><b></b></td>
						<td width="69%" align="left" valign="top">
						<select name="levels" id="levels" onchange="leveldetails(this.value);">
                                                <option value="">(choose one)</option>
                                                 <option value="1">National</option>
                                                  <option value="0">District</option>
                                                                                           
                                                
                                                </select>
                                                </td>
			      </tr>
                              <tr>
                                    <td height="10" colspan="4"></td>
                               </tr>
                               
                              <tr id="row-1">
						<td width="0%" align="left" valign="top">&nbsp;</td>
						<td width="31%" align="left" valign="top" class="text"><b>Choose Country:<font color="#FF0000">*</font></b></td>
						<td width="0%" valign="top" style="padding-top:4px;"><b></b></td>
						<td width="69%" align="left" valign="top">
                                                <select name="countries" id="countries" >
                                                <option value="">(choose one)</option>
                                                <?php foreach($Countries as $value)
                                                { 
                                                    ?><option value="<?php echo $value['Country']['CountryId'];?>"><?php echo $value['Country']['CountryName'];?></option>
                                                <?php }
                                                ?>
                                                
                                                
                                                </select>
						</td>
			      </tr>
                              <tr id="row-2">
                                    <td height="10" colspan="4"></td>
                               </tr>
                              <tr id="row-3">
						<td width="0%" align="left" valign="top">&nbsp;</td>
						<td width="31%" align="left" valign="top" class="text"><b>Choose State<font color="#FF0000">*</font></b></td>
						<td width="0%" valign="top" style="padding-top:4px;"><b></b></td>
						<td width="69%" align="left" valign="top">
                                                 <select name="states" id="states" onchange="getdistricts(this.value);">
                                                <option value="">(choose one)</option>
                                                <?php foreach($States as $value)
                                                { 
                                                    ?><option value="<?php echo $value['State']['StateId'];?>"><?php echo $value['State']['StateName'];?></option>
                                                <?php }
                                                ?>
                                                
                                                
                                                </select>
						</td>
			      </tr>
                              <tr id="row-4">
                                    <td height="10" colspan="4"></td>
                               </tr>
                                <tr id="row-5">
						<td width="0%" align="left" valign="top">&nbsp;</td>
						<td width="31%" align="left" valign="top" class="text"><b>Choose District<font color="#FF0000">*</font></b></td>
						<td width="0%" valign="top" style="padding-top:4px;"><b></b></td>
						<td width="69%" align="left" valign="top">
                                                 <div id="my_div"><select name="districts" id="districts">
                                                <option value="">(choose one)</option>
                                                                                       
                                                </select></div>
						</td>
			      </tr>
                              <tr id="row-6">
                                    <td height="10" colspan="4"></td>
                               </tr>
                               
                              <tr>
						<td width="0%" align="left" valign="top">&nbsp;</td>
						<td width="31%" align="left" valign="top" class="text"><b>Upload Form<font color="#FF0000">*</font></b></td>
						<td width="0%" valign="top" style="padding-top:4px;"><b></b></td>
						<td width="69%" align="left" valign="top">
						<?php //echo $this->Form->input('Html', array('type' => 'file', 'id'=>"html_file", 'class'=>'manage_input')); ?>
						<input type="file" name="zip_file" id="zip_file"/>
                                                </td>
			      </tr>
                              <tr>
                                    <td height="10" colspan="4"></td>
                               </tr>
                               
				<tr align="left">
					<td colspan="3" valign="top"></td>
					<td valign="top" style="float:left">
					<span style='float:left'>
                                        <input type="hidden" name="my_var" id="my_var" value="">
					<?php echo $this->Form->submit("Add",array('class'=>'button')); ?>
					<!--input type="hidden" name="data['User']['iStatus']" value="1"/--></span>
					&nbsp;<span>
					<input type="button" value="Cancel" name="B2" class="button" OnClick="javascript:window.location='<?php echo SITE_HTTP_URL;?>login/viewForms'">
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
<script>
function getdistricts(state_id){
    jQuery.ajax({
            type: "POST",
            url: "getDistricts",
            data: {state : state_id },
            success: function(data){
            jQuery('#my_div').html(data);
            }
         });
    
}
function leveldetails(level)
{
    if(level == '1')
        { 
            //$('#my_var').val() = '1';
            $("#my_var").val("1");
            $('#row-1').hide();
            $('#row-2').hide();
            $('#row-3').hide();
            $('#row-4').hide();
            $('#row-5').hide();
            $('#row-6').hide();
            
            
           
        }
        else{
            $("#my_var").val("0");
            $('#row-1').show();
            $('#row-2').show();
            $('#row-3').show();
            $('#row-4').show();
            $('#row-5').show();
            $('#row-6').show();
        }
}
</script>

