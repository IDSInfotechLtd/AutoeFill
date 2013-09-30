<?php 
define("SITE_BASE_PATH", "'/usr/local/apache/htdocs/open_data_app/");
define('SITE_HTTPS_URL', "http://".$_SERVER['HTTP_HOST']."/open_data_app/");
define('SITE_URL', "http://".$_SERVER['HTTP_HOST']."/open_data_app/");

// this line is in the app controller on the server
define("SITE_HTTP_URL", "http://".$_SERVER['HTTP_HOST']."/open_data_app/");

define("SITE_NAME","Open Data App"); 
define("BASE_PATH","https://".$_SERVER['HTTP_HOST']."/open_data_app/");

// this will be ssl link on live server
//define("BASE_PATH","http://".$_SERVER['HTTP_HOST']."/neetu/open_data_app/");

// this section is already there in app controller on the live server
define("IMAGES_PATH", SITE_HTTP_URL."app/webroot/img/");
define("CSS_PATH", SITE_HTTP_URL."app/webroot/css/");
define("JS_PATH", SITE_HTTP_URL."app/webroot/js/");
define("FILES_PATH", SITE_BASE_PATH."app/webroot/files/");
define("FILES_PATH_URL", SITE_HTTP_URL."app/webroot/files/");
define("HTML_PATH", "/usr/local/apache/htdocs/open_data_app/app/webroot/html/");

define("ADMIN","1"); 
define("USER","2");


//ADMIN CHANGE PASSWORD
	define("ERR_OLDPASSWORD_EMPTY","Please enter your current password.");
	define("ERR_OLDPASSWORD_INVALID","Current password do not match. Please try again.");
	define("ERR_NEWPWD_CONFIRMPWD_EMPTY","Please enter your new password and confirm password.");
	define("ERR_PASSWORD_NEW_EMPTY","Please enter your new password.");	
	define("ERR_CONFIRMPASSWORD_EMPTY","Please enter your Confirm Password.");
	define("ERR_PASSNOTMATCH_EMPTY","Passwords do not match. Please try again.");
	define("MSG_PASSWORD_CHANGED","You have successfully changed your password.");
	define("ERR_FORGOT_PWD_NOT_AUTHORIZED","Either the username or email is invalid. Please try again.");	
        
//Forgot Password
define("ERR_EMPTY_FORGOT","Please enter your username/ email address.");
define("ERR_INVALID_FORGOT","Email address not found. Please try again.");



?>