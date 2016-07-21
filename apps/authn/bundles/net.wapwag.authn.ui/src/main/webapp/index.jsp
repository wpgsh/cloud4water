<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <title>index page</title>
    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="css/dashboard.css" rel="stylesheet">
    <!-- Styles for home -->
    <link href="css/home/home.css" rel="stylesheet">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
  <jsp:include page="main.jsp" />
    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
          <ul class="nav nav-sidebar">
            <li class="active"><a href="home.html">Essential information</a></li>
            <li><a href="#">Application information</a></li>
          </ul>
          <ul class="nav nav-sidebar">
            <li><a href="">User management</a></li>
            <li><a href="">Application management</a></li>
          </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <div class="row">
            <div class="panel panel-info">
              <div class="panel-heading">
                <h3 class="panel-title">Information</h3>
              </div>
              <div class="panel-body">
                <div class="col-sm-3">
                    <img id="upload-profile-imgShow" src="images/default.png" class="uploadImg" />
                </div>
                <div class="col-sm-9">
                    <form class="form-horizontal">
                      <div class="form-group">
                        <label for="inputTitle" class="col-sm-2 control-label">Title</label>
                        <div class="col-sm-6">
                          <input type="text" class="form-control" id="inputTitle" value="Wpg wisdom water" disabled>
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="inputName" class="col-sm-2 control-label">Name</label>
                        <div class="col-sm-6">
                          <input type="text" class="form-control" id="inputName" value="admin" disabled>
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="inputPhone" class="col-sm-2 control-label">Phone</label>
                        <div class="col-sm-6">
                          <input type="text" class="form-control" id="inputPhone" value="1234578910" disabled>
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="inputEmail" class="col-sm-2 control-label">Email</label>
                        <div class="col-sm-6">
                          <input type="text" class="form-control" id="inputEmail" value="99999999@163.com" disabled>
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="inputHomePage" class="col-sm-2 control-label">Homepage</label>
                        <div class="col-sm-6">
                          <input type="text" class="form-control" id="inputHomePage" value="https://www.baidu.com/" disabled>
                        </div>
                      </div>
                    </form><!-- form -->
                </div>
                <div class="col-sm-12 margin-top">
                    If you want to modify your basic information, click on the button below.
                </div>
                <div class="col-sm-12 margin-top">
                    <a href="settings/profile.html" type="submit" class="btn btn-success">Edit profile</a>
                </div>
              </div>
            </div><!-- panel -->
          </div><!-- Information -->
          <div class="row">
            <div class="panel panel-success">
              <div class="panel-heading">
                <h3 class="panel-title">Status</h3>
              </div>
              <div class="panel-body">
                Describes the status of the user in the current application, can not modify it.
              </div><!-- panel-body -->
              <ul class="list-group">
                <li class="list-group-item">Whether it has been disabled.<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span></li>
                <li class="list-group-item">Whether the mailbox has been verified.<span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span></li>
                <li class="list-group-item">Whether it has passed the validity period.<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span></li>
              </ul><!-- List group -->
            </div><!-- panel -->
          </div><!-- Status -->
        </div>
      </div><!-- row -->
    </div><!-- container-fluid -->

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="js/jquery-1.11.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="js/holder.min.js"></script>
    <script src="js/offcanvas.js"></script>
  </body>
</html>
