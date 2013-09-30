<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title><?php echo $title_for_layout?></title>
<?php echo $this->Html->css('admin');?>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<noscript>Your browser does not support JavaScript!</noscript>

</head>
<body  style="margin:0 auto;" >	
		<?php echo $this->Session->flash(); ?>
		<?php echo $content_for_layout ?>			
</body>
</html>