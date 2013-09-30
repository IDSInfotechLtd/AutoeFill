
<select name="districts">
    <option value="">(choose one)</option>
<?php foreach($options as $value)
{ 
    ?><option value="<?php echo $value['District']['DistrictId'];?>"><?php echo $value['District']['DistrictName'];?></option>
<?php }
?>
</select>

