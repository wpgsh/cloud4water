<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<!-- font files  -->
<link href='http://fonts.useso.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>
<link href='http://fonts.useso.com/css?family=Nunito:400,300,700' rel='stylesheet' type='text/css'>
<!-- /font files  -->
<!-- css files -->
<link href="css/style.css" rel='stylesheet' type='text/css' media="all" />
<!-- /css files -->
</head>
  <script src="scripts/jquery-1.11.1.min.js"></script>
  <script src="scripts/jquery-form.js"></script>
<script> 
jQuery(document).ready(function() {
	$("#submit").click(function() {
		var userName = $('input[name="userName"]').val();
		var passWord = $('input[name="passWord"]').val();
		var checkCode = $('input[name="checkCode"]').val();
		var i = 0;
		if(isEmp(userName))
		{
			$("#userNameMsg").html("UserName is null!");
			i++;
		}else{
			$("#userNameMsg").html("");
		}
		if(isEmp(passWord))
		{
			$("#pwMsg").html("Password is null!");
			i++;
		}else{
			$("#pwMsg").html("");
		}
		if(isEmp(checkCode))
		{
			$("#checkCodeMsg").html("CheckCodeMsg is null!");
			i++;
		}else{
			$("#checkCodeMsg").html("");
		}
		if(0 < i){
			refulsCodeImg();
			return;
		}
		$("#form").ajaxSubmit({
			type:'post',
			dataType : 'json',  
			success:function(data){
				var errorCode = data.errorCode;
				if("1" == errorCode){
					$("#userNameMsg").html("UserName or password is error!");
				}else{
					$("#userNameMsg").html("");
				}
				
				if("2" == errorCode){
					$("#checkCodeMsg").html("CheckCode is error!");
				}else{
					$("#checkCodeMsg").html("");
				}
				if("0" == errorCode){
					window.location="index.jsp";  
				}
				refulsCodeImg();
			},
			error:function(data)
			{
				refulsCodeImg();
				alert("error");
			}
		});
	});
	$("#reset").click(function() {
		$("#checkCodeMsg").html("");
		$("#userNameMsg").html("");
		$("#pwMsg").html("");
		$('input[name="userName"]').val("");
		$('input[name="passWord"]').val("");
		$('input[name="checkCode"]').val("");
		refulsCodeImg();
	});
});
function isEmp(str){
	if(undefined == str || null == str || "" == str){
		return true;
	}
	str = str.trim();
	if(undefined == str || null == str || "" == str){
		return true;
	}
	return false;
}
function refulsCodeImg(){
	$("#codeImg").attr("src","/authn/checkCode?" + Math.random());
}
</script>  
<body>
<h1>Login to our system</h1>
<div class="log">
	<div class="content1">
		<h2>Sign In Form</h2>
		<form id="form" action="/authn/login" method="post">
			<input type="text" name="userName" value="" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = '';}">
			<p class="p-container" id="userNameMsg"></p>
			<input type="password" name="passWord" value="" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = '';}">
			<p class="p-container" id="pwMsg"></p>
			<div class="img-container">
				<div class="input-code">
					<input type="text" name="checkCode" value="" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = '';}">
				</div>
				<div class="img-code">
					<img id="codeImg" src="/authn/checkCode" alt="refresh" border="0" align="absmiddle" onclick="this.src='/authn/checkCode?rnd=' + Math.random();" />
				</div>
			</div>
			<p class="p-container" id="checkCodeMsg"></p>
			<div class="button-row">
				<input type="button" id="reset" class="sign-in" value="Reset">
				<input type="button" id="submit" class="reset" value="Login">
				<div class="clear"></div>
			</div>
		</form>
	</div>
	<div class="clear"></div>
</div>
<div class="footer">
	<p>Copyright &copy; 2016.Company name All rights reserved</p>
</div>

</body>
</html>