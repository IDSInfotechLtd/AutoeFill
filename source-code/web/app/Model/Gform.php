<?php
class Gform extends AppModel 
{ 
    var $name = 'Gform'; 
    var $useTable = false;
    public function _searchgform($postArray){
      
        if(!empty($postArray->query['cat']))
        $catId = $postArray->query['cat'];
        if(!empty($postArray->query['keyword']))
        $keyword = $postArray->query['keyword'];
        if(isset($postArray->query['level']))
        $level = $postArray->query['level'];
        
        
         $sql_string = "";$country_id="";$state_id="";$district_id="";
            if(!empty($catId))
             $sql_string .= " and Category='".$catId."'";
         
         if(!empty($keyword)){
            $sql_string .= " and FormName like '%".$keyword."%'";  
         }
         if(isset($level)){
             if($level == '1')
                 $sql_string .= " and FormLevel = '1'";
             elseif($level == '0'){
                 
                 $sql_string.=" and FormLevel='0'";
                 if(!empty($postArray->query['CountryId'])){
                    $country_id = $postArray->query['CountryId'];
                    $sql_string.= " and CountryId='".$country_id."'";
                 }
                 if(!empty($postArray->query['StateId'])){
                    $state_id = $postArray->query['StateId'];
                    $sql_string.= " and StateId='".$state_id."'";
                 }
                if(!empty($postArray->query['DistrictId'])){
                    $district_id = $postArray->query['DistrictId'];
                    $sql_string.= " and DistrictId='".$district_id."'";
                    //$sql_string.=" and FormLevel='0' and CountryId='".$country_id."' and StateId='".$state_id."' and DistrictId='".$district_id."'";
             } 
         }
         }
                  
         $sql = "select FormId,FormName,FormLocation,FormDirectory from tbl_gforms where isDeleted = '0' ".$sql_string." order by FormName asc";
      $result = $this->query($sql);
        
       
     
         return $result;
   }
    
   
} 
?>