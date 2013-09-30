<?php 
class ListingsController extends AppController 
{ 
    
    var $name = 'Listings'; 
    var $uses = array('Country','State','District','Gform','Category');
    
    /**
     *
     * @return list of countries
     */
     function getcountrylist() {
       
        $this->autoRender=false;
        $result['errocode'] = 0;
	$result['message'] = '';
        $countries=  $this->Country->find('all',array(
        'fields' => array('CountryId', 'CountryName'),
        'conditions' => array('IsDeleted' => 0),
         'order' => array('CountryName' => 'asc')
        ));
        $result['data'] = $countries;
        if(empty($result['data'])){
           $result['errocode'] = 1;
           $result['message'] = 'No country found';
           
        }
        echo json_encode($result);
       
           
     } 
    
     
     /**
     *
     * @return list of states by country
     */
     
     function getstatelist($CountryId="") {
        
        $this->autoRender=false;  
       $CountryId = $this->params->query['CountryId'];
          
        //echo $CountryId;die("meeee");
        if($CountryId != ""){
         $result['errocode'] = 0;
	 $result['message'] = '';
         $states=  $this->State->find('all',array(
             'fields' => array('StateId', 'StateName'),
             'conditions' => array('IsDeleted' => 0,'CountryId'=>$CountryId),
             'order' => array('StateName' => 'asc')
             )
         );
         $result['data'] = $states;
         if(empty($states)){
           $result['errocode'] = 1;
           $result['message'] = 'No state found';
           //echo json_encode($result);
             
         }
        }else{
            $result['errocode'] = 1;
	    $result['message'] = 'Please enter country id';
            //echo json_encode($result);
        }
        echo json_encode($result);
        
        }
        function getstates() {
        
        $this->autoRender=false;  
        $result['errocode'] = 0;
	 $result['message'] = '';
         $states=  $this->State->find('all',array(
             'fields' => array('StateId', 'StateName','CountryId'),
             'conditions' => array('IsDeleted' => 0),
             'order' => array('StateName' => 'asc')
             )
         );
         $result['data'] = $states;
         if(empty($states)){
           $result['errocode'] = 1;
           $result['message'] = 'No state found';
           //echo json_encode($result);
             
         }
       
        echo json_encode($result);
        
        }
    
        
        /**
     *
     * @return list of districts by state
     */
        
      function getdistrictlist($StateId="") {
        
        $this->autoRender=false; 
        $StateId = $this->params->query['StateId'];
          
        //echo $CountryId;die("meeee");
        if($StateId != ""){
         $result['errocode'] = 0;
	 $result['message'] = '';
         $districts=  $this->District->find('all',array(
             'fields' => array('DistrictId', 'DistrictName'),
             'conditions' => array('IsDeleted' => 0,'StateId'=>$StateId),
             'order' => array('DistrictName' => 'asc')
             )
         );
         $result['data'] = $districts;
         if(empty($districts)){
           $result['errocode'] = 1;
           $result['message'] = 'No District found';
           //echo json_encode($result);
             
         }
        }else{
            $result['errocode'] = 1;
	    $result['message'] = 'Please enter state id';
            //echo json_encode($result);
        }
        return json_encode($result);
        exit;
        }
         function getdistricts() {
        
        $this->autoRender=false; 
        
         $result['errocode'] = 0;
	 $result['message'] = '';
         $districts=  $this->District->find('all',array(
             'fields' => array('DistrictId', 'DistrictName','StateId'),
             'conditions' => array('IsDeleted' => 0),
             'order' => array('DistrictName' => 'asc')
             )
         );
         $result['data'] = $districts;
         if(empty($districts)){
           $result['errocode'] = 1;
           $result['message'] = 'No District found';
           //echo json_encode($result);
             
         }
        
        return json_encode($result);
        exit;
        }
        
        /**
     *
     * @return list of categories
     */
     function getcategorylist() {
       
        $this->autoRender=false;
        $result['errocode'] = 0;
	$result['message'] = '';
        $categories =   $this->Category->find('all',array(
        'fields' => array('CategoryId', 'CategoryName'),
        'conditions' => array('IsDeleted' => 0),
        'order' => array('CategoryName' => 'asc')
         
        ));
        $result['data'] = $categories;
        if(empty($result['data'])){
           $result['errocode'] = 1;
           $result['message'] = 'No country found';
           
        }
        return json_encode($result);
       
           
     } 
        
      
     
     function searchform($Keyword="",$FormName="",$Category="",$DistrictId="",$StateId="",$CountryId=""){
         
        $this->autoRender=false;
        $result['errocode'] = 0;
	$result['message'] = '';
        if($this->params->query['level'] != "")
         $level = $this->params->query['level'];
        if($level != ""){
            
        $search_all_results = $this->Gform->_searchgform($this->params);
        
        for($i=0;$i<sizeof($search_all_results);$i++){
            $search_result[$i]['tbl_gforms']['FormId'] = $search_all_results[$i]['tbl_gforms']['FormId'];
            $search_result[$i]['tbl_gforms']['FormName'] = $search_all_results[$i]['tbl_gforms']['FormName'];
           $search_result[$i]['tbl_gforms']['FormLocation'] = 'http://st3.idsil.com/open_data_app/html/'.$search_all_results[$i]['tbl_gforms']['FormDirectory'].'/'.$search_all_results[$i]['tbl_gforms']['FormLocation'];
            
        }
        
        $result['data'] = $search_result;
        if(empty($result['data'])){
           $result['errocode'] = 1;
           $result['message'] = 'No matches found';
           
        }
        }else{
            $result['errocode'] = 1;
           $result['message'] = 'Please choose a level';
        }
        return json_encode($result);
        
         
         
         
     }
     
        

} 

    ?>