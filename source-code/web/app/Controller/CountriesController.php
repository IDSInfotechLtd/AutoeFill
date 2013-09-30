<?php 
class CountriesController extends AppController 
{ 
    
    var $name = 'tblCountry'; 
    var $uses = array('tblCountry');
    function listing() {
        
         $countries=  $this->tblCountry->find('all',array(
             'fields' => array('CountryId', 'CountryName'),
             'conditions' => array('IsDeleted' => 0)
             )
         );
         echo json_encode($countries);
         exit;
    } 

} 

    ?>


