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
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <style type="text/css">
        .list-group-item {
            padding: 8px 15px;
        }
    </style>
    <!-- Custom styles for this template -->
    <link href="css/offcanvas.css" rel="stylesheet">
    <!-- Styles for form -->
    <link rel="stylesheet" type="text/css" href="css/docs.min.css">
    <!-- Personal profile choose -->
    <link rel="stylesheet" type="text/css" href="css/settings/style.css">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
    <jsp:include page="main.jsp" />
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
                <h3 class="panel-title font-color">Public profile</h3>
              </div>
              <div class="panel-body" data-example-id="simple-horizontal-form">
                <form class="form-horizontal" action="updateProfileServlet" method="post" id="form" enctype="multipart/form-data">
                  <input type="hidden" name="userId" id="userId" value="<%=session.getAttribute("userId")%>">
                  <div class="form-group">
                    <div class="col-sm-12 margin-bottom">
                      <label for="inputName" class="control-label">Profile picture</label>
                    </div>
                    <div class="col-sm-2">
                        <img id="upload-profile-imgShow" src="getAvatarByUserIdServlet?userId=<%=session.getAttribute("userId")%>" class="uploadImg" />
                    </div>
                    <div class="col-sm-4">
                      <label for="upload-profile-picture" class="btn btn-default upload-label">
                        Upload new picture
                        <input id="upload-profile-picture" type="file" name="file" class="upload-input">
                      </label>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-12 margin-bottom">
                      <label for="inputName" class="control-label">Name</label>
                    </div>
                    <div class="col-sm-6">
                      <input type="text" class="form-control" id="inputName" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9]/g,'')" onpaste="value=value.replace(/[^\a-\z\A-\Z0-9]/g,'')" oncontextmenu = "value=value.replace(/[^\a-\z\A-\Z0-9]/g,'')" name="inputName" value="<%=session.getAttribute("userName")%>" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-12 margin-bottom">
                      <label for="inputPhone" class="control-label">Phone</label>
                    </div>
                    <div class="col-sm-6">
                      <input type="text" class="form-control" id="inputPhone" onkeyup="value=value.replace(/[^0-9]/g,'')" onpaste="value=value.replace(/[^0-9]/g,'')" oncontextmenu = "value=value.replace(/[^0-9]/g,'')" name="inputPhone" value="<%=session.getAttribute("phone")%>" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-12 margin-bottom">
                      <label for="inputEmail" class="control-label">Email</label>
                    </div>
                    <div class="col-sm-6">
                      <input type="text" class="form-control" id="inputEmail" name="inputEmail" value="<%=session.getAttribute("email")%>" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-12 margin-bottom">
                      <label for="inputHomePage" class="control-label">Homepage</label>
                    </div>
                    <div class="col-sm-6">
                      <input type="text" class="form-control" id="inputHomePage" name="inputHomePage" value="<%=session.getAttribute("homePage")%>">
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-12">
                      <p id="submit1" class="btn btn-success">Update profile</p>
                      <h2 class="form-signin-heading logo-title"></h2>
                    </div>
                  </div>
                </form>
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
    <script src="js/jquery-1.11.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/offcanvas.js"></script>
    <script src="scripts/jquery-form.js"></script>
    <script src="js/upload/uploadPreview.js"></script>
    <script>
    jQuery(document).ready(function() {
    	$("#profile_choose").addClass("choose");
    	var sign = 0;
    	$("#submit1").click(function() {
    		var flag = true;
    		
    		var userId = <%=session.getAttribute("userId")%>;
    		if(!userId){
    			window.location.href = "login.jsp";
    		}
    		
    		var filepath = $("input[name='file']").val();
    		var userId = $('input[name="userId"]').val();
    		var name = $('input[name="inputName"]').val();
    		var phone = $('input[name="inputPhone"]').val();
    		var email = $('input[name="inputEmail"]').val();
    		var homePage = $('input[name="inputHomePage"]').val();
    		
    		if(!checkPic()){
    			flag = false;
    			return;
    		}
    		
    		if(isEmp(name) || isEmp(phone)){
    			var message = "Name or Phone can not be Null";
    			flag = false;
    			showError($(".logo-title"), message);
    			return;
    		}
    		
    		if(!CheckPhone(phone)){
    			var message = "Phone Number is wrong";
    			flag = false;
    			showError($(".logo-title"), message);
    			return;
    		}
    		
<%--     		if(name == '<%=session.getAttribute("userName")%>' && phone == '<%=session.getAttribute("phone")%>' --%>
<%--     		&& email == '<%=session.getAttribute("email")%>' && homePage == '<%=session.getAttribute("homePage")%>'  --%>
//     		&& isEmp(filepath)){
//     			var message = "data no change";
//     			flag = false;
//     			showError($(".logo-title"), message);
//     			return;
//     		}
    		
    		if(!CheckEmail(email)){
    			var message = "Email Number is wrong";
    			flag = false;
    			showError($(".logo-title"), message);
    			return;
    		}
    		
    		if(flag){
    			if(sign == 0){
    			sign = 1;
    			$("#submit1").attr("disabled","true");
    			$("#form").ajaxSubmit({
	    			type:'post',
	    			dataType : 'json',
	    			success:function(data){
	    				sign = 0;
	    				$("#submit1").removeAttr("disabled");
	    				var errorCode = data.errorCode;
	    				if("1" == errorCode){
	    					var message = "time out";
	    	    			showError($(".logo-title"), message);
	    	    			return;
	    				}
	    				if("0" == errorCode){
	    					var message = "save success";
	    	    			showError($(".logo-title"), message);
	    	    			return;
	    				}
	    				if("2" == errorCode){
	    					var message = "file size over 2M";
	    	    			showError($(".logo-title"), message);
	    	    			return;
	    				}
	    				if("3" == errorCode){
	    					window.location.href = "login.jsp";
	    				}
	    			},
	    			error:function(data)
	    			{
	    				$("#submit1").removeAttr("disabled");
	    				alert("error");
	    			}
	    		});
    			}else{
    				alert("submitting ,please wait");
    			}
    		}
    	});
    	
    	// 回车键事件 
		// 绑定键盘按下事件  
	    $(document).keypress(function(e) {  
	    // 回车键事件  
	    	if(e.which == 13) {  
	    	   $("#submit1").click();  
	       }  
	    }); 
    });
    
    function checkPic(){
    	var filepath = $("input[name='file']").val();
    	if(!isEmp(filepath)){
	        var extStart = filepath.lastIndexOf(".");
	        var ext = filepath.substring(extStart, filepath.length).toUpperCase();
	        if (ext != ".BMP" && ext != ".PNG" && ext != ".GIF" && ext != ".JPG" && ext != ".JPEG") {
	            showError($(".logo-title"), "only bmp,png,gif,jpeg,jpg");
	            return false;
	        }
           
    	}
        return true;
    }
    
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
    
    function CheckPhone(phone){
    	var check = /^1[34578]\d{9}$/; 
    	return check.test(phone);
    }
    
    function CheckEmail(email){
    	var check = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/; 
    	return check.test(email);
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
