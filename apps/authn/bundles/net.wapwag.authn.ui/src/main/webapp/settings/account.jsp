<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Settings</title>
    <!-- Bootstrap core CSS -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <style type="text/css">
        .list-group-item {
            padding: 8px 15px;
        }
    </style>
    <!-- Custom styles for this template -->
    <link href="../css/offcanvas.css" rel="stylesheet">
    <!-- Styles for form -->
    <link rel="stylesheet" type="text/css" href="../css/docs.min.css">
    <!-- Personal profile choose -->
    <link rel="stylesheet" type="text/css" href="../css/settings/style.css">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
    <jsp:include page="../main.jsp" />
    <div class="container max-width">
      <div class="row row-offcanvas row-offcanvas-right">

        <jsp:include page="settingmain.jsp" />

        <div class="col-xs-12 col-sm-9">
          <p class="pull-right visible-xs">
            <button type="button" class="btn btn-primary btn-xs" data-toggle="offcanvas">Toggle nav</button>
          </p>
          <div class="row">
            <div class="panel panel-default">
              <div class="panel-heading">
                <h3 class="panel-title font-color">Change password</h3>
              </div>
              <div class="panel-body" data-example-id="simple-horizontal-form" id="form">
                <form class="form-horizontal" method="post">
                  <input type="hidden" name="userId" value="<%=session.getAttribute("userId")%>">
                  <div class="form-group">
                    <div class="col-sm-12 margin-bottom">
                      <label for="inputPassword" class="control-label">Old password</label>
                    </div>
                    <div class="col-sm-6">
                      <input type="password" class="form-control" name="inputPassword" id="inputPassword" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-12 margin-bottom">
                      <label for="inputNewPassword" class="control-label">New password</label>
                    </div>
                    <div class="col-sm-6">
                      <input type="password" class="form-control" name="inputNewPassword" id="inputNewPassword" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-12 margin-bottom">
                      <label for="inputConfirm" class="control-label">Confirm new password</label>
                    </div>
                    <div class="col-sm-6">
                      <input type="password" class="form-control" name="inputConfirm" id="inputConfirm" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-12">
                      <p id="submit" class="btn btn-success">Update password</p>
                      <h2 class="form-signin-heading logo-title"></h2>
                    </div>
                  </div>
                </form><!-- form -->
              </div><!-- panel-body -->
            </div><!-- panel -->
          </div><!--/row-->
        </div><!--/.col-xs-12.col-sm-9-->
        <!--/.sidebar-offcanvas-->
      </div><!--/row-->

      <hr>

      <footer>
        <p>&copy; Wei pai ge 2016,Inc.</p>
      </footer>

    </div><!--/.container-->

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="../js/jquery-1.11.1.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="../js/offcanvas.js"></script>
    <script src="../scripts/jQuery.md5.js"></script>
    <script type="text/javascript">
    jQuery(document).ready(function() {
    	$("#account_choose").addClass("choose");
    	
    	$("#submit").click(function() {
    		var userId = $('input[name="userId"]').val();
    		var inputPassword = $('input[name="inputPassword"]').val();
    		var inputNewPassword = $('input[name="inputNewPassword"]').val();
    		var inputConfirm = $('input[name="inputConfirm"]').val();
    		if(isEmp(inputPassword) || isEmp(inputNewPassword) || isEmp(inputConfirm)){
    			var message = "password can not be null";
    			showError($(".logo-title"), message);
    			return;
    		}
    		
    		if(inputNewPassword != inputConfirm){
    			var message = "new password is different with confirmation password";
    			showError($(".logo-title"), message);
    			return;
    		}
    		
    		$.ajax({
    			type:'post',
    			url:'/authn/changePasswordServlet',
    			dataType : 'json',
    			data:{'userId':userId,'inputPassword':$.md5(inputPassword),'inputNewPassword':$.md5(inputNewPassword)},
    			success:function(data){
    				var errorCode = data.errorCode;
    				if("1" == errorCode){
    					var message = "old password is wrong";
    	    			showError($(".logo-title"), message);
    	    			return;
    				}
    				if("0" == errorCode){
    					var message = "password change success";
    	    			showError($(".logo-title"), message);
    	    			return;
    				}
    				if("3" == errorCode){
    					window.location.href = "../login.jsp";
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
    
    /* 消息提示模板 */
	var showError = function(obj, msg){
    	$("#errorMsg").remove();
		var model = "<div id='errorMsg' class='errMesage'><span>"+msg+"</span><label></label></div>";
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
