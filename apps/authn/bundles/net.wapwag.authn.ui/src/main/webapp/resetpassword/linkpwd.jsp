<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Reset password</title>
    <!-- Bootstrap core CSS -->
    <link href="/authn/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="/authn/css/sign/sign.css" rel="stylesheet">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <div class="container">
      <form id="form" class="form-signin" action="/authn/password_reset" method="post">
        <div class="logo-div">
            <img src="/authn/images/wpg.png">
        </div>
        <h2 class="form-signin-heading logo-title">Reset your password</h2>
        <span class="link-span">Enter your email address and we will send you a link to reset your password.</span>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="email" name="email" id="inputEmail" class="form-control" placeholder="email address" required autofocus>
        <button class="btn btn-lg btn-success btn-block link-btn" type="button" id="submit">Send password reset email</button>
      </form>
      <div class="bottom-content">
        Copyright Â© 2016.Wei Pai Ge All rights reserved
      </div>
    </div> <!-- /container -->


    <!-- Javascript for login -->
    <script src="/authn/js/jquery-1.11.1.min.js"></script>
    <script src="/authn/scripts/jquery-form.js"></script>
    <script>
    jQuery(document).ready(function() {
    	$("#submit").click(function() {
    		var email = $('input[name="email"]').val();
    		if(!CheckMail(email)){
    			var message = "邮箱格式错误";
    			showError($(".logo-title"), message);
    			return false;
    		}
    		$("#form").ajaxSubmit({
    			type:'post',
    			dataType : 'json',  
    			success:function(data){
    				var errorCode = data.errorCode;
    				if("1" == errorCode){
    					var message = "你的邮箱还未注册";
    	    			showError($(".logo-title"), message);
    	    			return;
    				}
    				if("2" == errorCode){
    					var message = "系统繁忙，发送邮件失败，请稍候再试";
    	    			showError($(".logo-title"), message);
    	    			return;
    				}
    				if("0" == errorCode){
    					window.location="/authn/resetpassword/sendsuccess.jsp";
    				}
    			},
    			error:function(data)
    			{
    				alert("error");
    			}
    		});
    	});
    	
    	// 回车键事件 
		// 绑定键盘按下事件  
	    $(document).keypress(function(e) {  
	    // 回车键事件  
	    	if(e.which == 13) {  
	    	   $("#submit").click();  
	       }  
	    }); 
    });
    
    function CheckMail(mail){
    	var check = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/; 
    	if(check.test(mail))
    	{
    		return true;
    	}else{
    		return false;
    	}
    }
    /* 消息提示模板 */
	var showError = function(obj, msg){
		$("#errorMsg").remove();
		var model = "<div id='errorMsg' class='errMesage'><span>"+msg+"</span><label>x</label></div>";
		obj.after(model);

		// 取消提示
		$(".errMesage label").on("click", function(){
			$(".errMesage").fadeOut(200, function(){
				$(this).remove();
			});
		});
	}
    </script>
  </body>
</html>
