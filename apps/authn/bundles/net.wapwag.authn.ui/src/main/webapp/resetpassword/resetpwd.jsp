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
    <title>Set new password</title>
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
      <form class="form-signin" id="form" action="/authn/passwordreset" method="post">
        <div class="logo-div">
            <img src="/authn/images/wpg.png">
        </div>
        <h2 class="form-signin-heading logo-title">Set your new password</h2>
        <span class="link-span">Enter the new password and confirm, click the button, you will reset your password.</span>
        <label for="inputNewPwd" class="sr-only">New password</label>
        <input type="password" name="passWord" class="form-control" placeholder="new password" required autofocus>
        <label for="inputConfirmPwd" class="sr-only">Confirm password</label>
        <input type="password" name="confirmPwd" class="form-control" placeholder="confirm password" required>
        <input type="hidden" name="resetkey">
        <button class="btn btn-lg btn-success btn-block link-btn" type="button" id="submit">Change password</button>
        </div>
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
    	var windowUrl = window.location.search;
    	if(windowUrl.indexOf("resetkey") > 0 
    			&& windowUrl.split("resetkey=").length > 1 
    			&& !isEmp(windowUrl.split("resetkey=")[1])){
    		 
    	}else{
    		window.location="/authn/resetpassword/linkpwd.jsp";
    	}
    	$("#submit").click(function() {
    		var resetkey = windowUrl.split("resetkey=")[1];
    		var passWord = $('input[name="passWord"]').val();
    		var confirmPwd = $('input[name="confirmPwd"]').val();
    		if(isEmp(passWord) || isEmp(confirmPwd)){
    			var message = "密码不可以为空";
    			showError($(".logo-title"), message);
    			return;
    		}
    		
    		if(passWord != confirmPwd){
    			var message = "验证码和确认密码不相同";
    			showError($(".logo-title"), message);
    			return;
    		}
    		$('input[name="resetkey"]').val(resetkey);
    		$("#form").ajaxSubmit({
    			type:'post',
    			dataType : 'json',  
    			success:function(data){
    				var errorCode = data.errorCode;
    				if("1" == errorCode){
    					var message = "回话已经超时";
    	    			showError($(".logo-title"), message);
    	    			return;
    				}
    				if("0" == errorCode){
    					window.location="/authn/login.jsp?reset=success";
    				}
    			},
    			error:function(data)
    			{
    				alert("error");
    			}
    		});
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
