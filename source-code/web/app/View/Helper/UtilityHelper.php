<?php
/* SVN FILE: $Id: html.php 4205 2006-12-25 12:36:03Z phpnut $ */
/**
 * Utility helper file.
 *
 * Helping to retrieve the contents easily.
 *
 * CakePHP :  Rapid Development Framework <http://www.cakephp.org/>
 * Copyright (c)	2006, Cake Software Foundation, Inc.
 *								1785 E. Sahara Avenue, Suite 490-204
 *								Las Vegas, Nevada 89104
 *
 * Licensed under The MIT License
 * Redistributions of files must retain the above copyright notice.
 *
 * @filesource
 * @copyright		
 * @link			
 * @package			cake
 * @subpackage		cake.app.views.helpers.utility
 * @since			CakePHP v 0.9.1
 * @version			$Revision: 4205 $
 * @modifiedby		$LastChangedBy: GSM $
 * @lastmodified	$Date: 2006-12-25 06:36:03 -0600 (Mon, 25 Dec 2006) $
 * @license			http://www.opensource.org/licenses/mit-license.php The MIT License
 */

class UtilityHelper extends Helper{
	/**
	* Deprecated, This has been cary forward to APP.app_controller.php.
	* Utility function to return a value from a named array or a specified default
	*/
	function _getParam( &$arr, $name, $def=null, $mask=2 ) {
		if (isset( $arr[$name] )) {
			if (is_string( $arr[$name] )) {
				if (!($mask&_NOTRIM)) {
					$arr[$name] = trim( $arr[$name] );
				}
				if (!($mask&_ALLOWHTML)) {
					$arr[$name] = strip_tags( $arr[$name] );
				}
				if (!get_magic_quotes_gpc()) {
					$arr[$name] = addslashes( $arr[$name] );
				}
			}

			$arr[$name] = str_replace('$',"&#36;",$arr[$name]);
			return $arr[$name];
		} else {
			return $def;
		}
	}


	/*
		Function to dispay status icon
		@Param Status
	*/
	function display_status($status){
		if($status==1){
			return "Active";
		}else{
			return "Inactive";
		}
	}//EF

	/*Function to display messages
		@param1 Array of messages
		@param2 Css class
	*/
	function display_message($arr, $sClass='errMsg'){
		$str = '';	
		$errHead = 0;
		if(is_array($arr)){
			foreach($arr as $key=>$value){
				if($key == 0 && $value==1) continue;
				$errHead = 1;
				$str .= $value.'<br />';
			}
		}
		if($errHead){
			$str="<div class='".$sClass."'>".$str."</div>";
		}
		return $str;
	}//EF

	
	/*
		makeurl function
		@params
		helps to make url regarding pagination
		return string
	*/
	function makeurl()
	{
	   
        $url="";
		$count=0;
		$arrget=$_GET;
		foreach($arrget as $key=>$value)
		{
		 
		  if($key!='url')
		  {
		    
		     if($count==0)
			 {
			    $url.="?".$key."=".$value;
			 }
			 else
			 {
			    $url.="&".$key."=".$value;
			 }
			  $count++;
		  }
				  
		} 
		return $url;
	}
	
	function frontmakeurl()
	{
	    $url="";
		if(isset($this->params['pass']))
		{
			$arrget=$this->params['pass'];
			foreach($arrget as $value)
			{
			 $keydata=explode("-",$value);
			 if(isset($keydata[1]))
			 {
				$url.="/".$keydata[0]."-".$keydata[1];
			 }				  
			}
		} 
		return $url;
	}
	
	
	/*
	  Checks:
	  1) Url empty : No photo.
	  2) Url http/https: Direct url.
	  3) Url : image name.
	  
	  @params:
	  1) Path : Path of the folder at the server.
	  2) htmlAttributes : Array of the html attributes
	  3) Url : from the database.
	  4) Size : No image size.	
	 */
	
	function imageMain($path="", $htmlAttributes = array('alt'=>''),$url="",$return=false,$returnurl=false) {
		unset($htmlAttributes['height']);
	    if(!isNull($url))
		{		    
			if (!strpos($url, '://')) {			    			   
				if(!is_file(IMAGE_PATH.$path.$url) || !file_exists(IMAGE_PATH.$path.$url)){				    
					$url="/img/noimage.gif";
					$htmlAttributes = array('alt'=>'');
				}
				else
				{
				    $url = $this->webroot . SITE_IMAGEURL . $path.$url;
				}
			}				
		}
		else
		{
		     $url="/img/noimage.gif";
			 $htmlAttributes = array('alt'=>'');
		}
		
		if($returnurl)
		{
		   return $url;
		}       
		return $this->output(sprintf($this->tags['image'], $url, $this->parseHtmlOptions($htmlAttributes, null, '', ' ')), $return);
	}
	
	
	
	/* Function to have htmlspecialchars
	   @param1 string
	*/

	function hsp($string)
	{
		$string=trim($string);
        return htmlspecialchars($string);
	}//EF 
	
	/*
		Function to get rating image according to value
		@param1 rating value
	*/
	function shownames($array,$url="",$link=0){	    
		$names=array();
		$string="";
		foreach($array as $values)
		{
		  $str="";
		  $values['sName']=$this->hsp($values['sName']);
		  if($link)
		  {
		     $str='<a href="'.$url.$values['sSeoTitle'].'" title="'.$values['sName'].'">'.$values['sName']."</a>";
		  }
		  else
		  {
		     $str=$values['sName'];
		  }
		  $names[]=$str;		   
		}
		$string=implode(", ",$names);
		return $string;
	}//EF
	
	
	function downloadnow($product,$class="",$image="",$index='Product')
	{
	  $str="";
	  if(trim($product[$index]['sMoreInfoUrl'])!=""){ 
	     if(trim($image)!=""){	  
			  $str="<a href='/product/downloadnow/".$product[$index]['sSeoTitle']."'><img src='".$image."' alt='' /></a>";
//			  			  $str="<a href='/product/downloadnow/".$product[$index]['sSeoTitle']."' onclick='return winPop(this.href);'><img src='".$image."' alt='' /></a>";
			  }
			  else
			  {
			     $str="<a href='/product/downloadnow/".$product[$index]['sSeoTitle']."' class='".$class."'>Download Now</a>";
			  }
	  }
	  return $str;
	}
	
	#function is used to download the product
	function downloadnowprovd($provider,$product,$class="",$image="",$index=null)
	{
		 $str="";
	  if(trim($provider[$index]['sDemoUrl'])!=""){ 
	     if(trim($image)!=""){	  
			  $str="<a href='/product/downloadnowprovd/".$product['Product']['sSeoTitle']."/".$provider[$index]['provider_id']."'><img src='".$image."' alt='' /></a>";
//			  $str="<a href='/product/downloadnow/".$product[$index]['sSeoTitle']."' onclick='return winPop(this.href);'><img src='".$image."' alt='' /></a>";
			  }
			  else
			  {
			     $str="<a href='/product/downloadnowprovd/".$product['Product']['sSeoTitle']."/".$provider[$index]['provider_id']."' class='".$class."'>Download Now</a>";
			  }
	  }
	  return $str;
	}
	#End

	#function is used to buy products
	function buynowprovd($provider,$product,$class="",$image="",$index=null)
	{
	
	  $str="";
	  if(trim($provider[$index]['sBuyUrl'])!=""){ 
	     if(trim($image)!=""){			 
			  $str="<a href='/product/buynowprovd/".$product['Product']['sSeoTitle']."/".$provider[$index]['provider_id']."'><img src='".$image."' alt='' /></a>";
			  }
			  else
			  {			    
				 $str="<a href='/product/buynowprovd/".$product['Product']['sSeoTitle']."/".$provider[$index]['provider_id']."' class='".$class."'>Buy Now</a>";
			  }
	  }
	  return $str;
	}
	#End

	function downloadnowlink($product,$class="",$image="",$index='Product')
	{
	  $str="";
	  if(trim($product[$index]['sMoreInfoUrl'])!=""){ 
	     if(trim($image)!=""){	  
			  $str="<a href='/product/downloadnow/".$product[$index]['sSeoTitle']."' onclick='return showdomore(\"domore\",this.href);'><img src='".$image."' alt='' /></a>";
			  }
			  else
			  {
			     $str="<a href='/product/downloadnow/".$product[$index]['sSeoTitle']."' onclick='return showdomore(\"domore\",this.href);' class='".$class."'>Download Now</a>";
			  }
	  }
	  return $str;
	}
	
	function buynow($product,$class="",$image="",$index='Product')
	{
	  $str="";
	  if(trim($product[$index]['sBuyUrl'])!=""){ 
	     if(trim($image)!=""){	  
			  $str="<a href='/product/buynow/".$product[$index]['sSeoTitle']."'><img src='".$image."' alt='' /></a>";
			  }
			  else
			  {
			     $str="<a href='/product/buynow/".$product[$index]['sSeoTitle']."' class='".$class."'>Buy Now</a>";
			  }
	  }
	  return $str;
	}
	
	function playnow($product,$class="",$index='Product')
	{
	  $str="";
	  $titlestr="Click for Demo";
	  if($product[$index]['maincat_id']==4)
	  {
	      $titlestr="Listen to sample";
	  }
	  if(trim($product[$index]['sDemoUrl'])!=""){ 
	        $str="<a href='".$this->hsp($product[$index]['sDemoUrl'])."' onclick='return winPop(this.href);' class='".$class."'>".$titlestr."</a>";			
	  }	 	  
	  return $str;
	}

	function playnownew($product,$class="",$index='Product')
	{
	  $str="";
	  $filename='';
	  $fileid='';	 
	  if(trim($product[$index]['sListenUrl'])!=""){ 
		  //$filename=$this->hsp($product[$index]['sListenUrl']);
		  $fileid=$product[$index]['id'];		  
		  //$file=session_id().".xml";
		 # $a = session_id();
	       #$str="<a onClick='player(\"$fileid\", true,\"play.php?id=$a\")' class='".$class."' style='cursor:pointer;'>Play this</a>";	
		    $str="<a href='".$this->hsp($product[$index]['sListenUrl'])."' onclick='return winPop(this.href);' class='".$class."' style='text-decoration:none;'>Play this</a>";	
	  }
	  return $str;
	}
	
	/*
		Function to handle [quote] in messages
		@param String to be parsed
	*/
	function handleQuote($text,$html=0,$admin=0){
	$text=str_replace("\n","<br />",$text);
	$patterns = array(
            "/\[link\](.*?)\[\/link\]/",
			"/\[quote=(.*?)\](.*?)\[\/quote\]/",
            "/\[url\](.*?)\[\/url\]/",
            "/\[img\](.*?)\[\/img\]/",
            "/\[b\](.*?)\[\/b\]/",
            "/\[u\](.*?)\[\/u\]/",
            "/\[i\](.*?)\[\/i\]/"
        );
        $replacements = array(
            "<a href=\"\\1\">\\1</a>",
			"<br /><br /><div class='float_left quote'>Originally posted by <a href=\"/\\1\">\\1</a><br />\\2</div><br />",
            "<a href=\"\\1\">\\1</a>",
            "<img src=\"\\1\">",
            "<b>\\1</b>",
            "<u>\\1</u>",
            "<i>\\1</i>"
           
        );
        $newText = preg_replace($patterns,$replacements, $text);
		return $newText;
	}//EF

	/**
		
	*/
	function showArr($arr, $sClass='error', $display=false){
		$str = '';	$errHead = 0;
		$strPrefix = ''; 
		$strSuffix = '';
		if(is_array($arr)){
			$strPrefix = '<ul class="ul_'.$sClass.'">';
			foreach($arr as $key=>$value){
				if($key == 0 && $value==1) continue;
				$errHead = 1;

				$str .= '<li style="list-style-type:none;">'.$value.'</li>';
			}
			$strSuffix = "</ul>";
		}

		if($errHead){
			$str = $strPrefix.$str.$strSuffix;
		}else{
			$str = '';
		}
		
		if($errHead && $sClass=='error')
			$str = "<strong>Errors occured:</strong>\r\n".$str;

		return $str;
	}

}//End class