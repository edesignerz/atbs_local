<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script> 
<script type="text/javascript">
function fillme(){
alert("sunik");
var f=$('#foo');
f.load(function(){ 
f.contents().find('txtYDFirstName').hide(); 
})

}
</script>
    
  </head>
  <body>
<input type='text' id='fill' onchange="fillme()" />

<iframe id="foo" src="https://www.adrianflux.co.uk/quote/quickquote/car/details/LANDWN03/" width="800" height="1000" />
    
  </body>
</html>



