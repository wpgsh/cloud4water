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
    <!-- Custom styles for this template -->
    <link href="css/offcanvas.css" rel="stylesheet">
    <!-- Styles for form -->
    <link rel="stylesheet" type="text/css" href="css/docs.min.css">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
    <nav class="navbar navbar-fixed-top navbar-inverse">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">WPG Wisdom Water</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse navbar-right">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="index.html">Home</a></li>
            <li><a href="settings.html">Settings</a></li>
            <li><a href="#">Help</a></li>
            <li><a href="login.html">Sign out</a></li>
          </ul>
          <form class="navbar-form navbar-right">
            <input type="text" class="form-control" placeholder="Search...">
          </form>
        </div><!-- /.nav-collapse -->
      </div><!-- /.container -->
    </nav><!-- /.navbar -->

    <div class="container">

      <div class="row row-offcanvas row-offcanvas-right">

        <div class="col-xs-6 col-sm-3 sidebar-offcanvas" id="sidebar">
          <div class="list-group">
            <a href="javascript:;" class="list-group-item panel-title active">Personal settings</a>
            <a href="#" class="list-group-item">Profile</a>
            <a href="#" class="list-group-item">Account</a>
            <a href="#" class="list-group-item">Emails</a>
            <a href="#" class="list-group-item">Notifications</a>
            <a href="#" class="list-group-item">Billing</a>
            <a href="#" class="list-group-item">SSH and GPG keys</a>
            <a href="#" class="list-group-item">Security</a>
            <a href="#" class="list-group-item">OAuth applications</a>
            <a href="#" class="list-group-item">Personal access tokens</a>
            <a href="#" class="list-group-item">Repositories</a>
            <a href="#" class="list-group-item">Organizations</a>
            <a href="#" class="list-group-item">Saved replies</a>
          </div>
        </div>

        <div class="col-xs-12 col-sm-9">
          <p class="pull-right visible-xs">
            <button type="button" class="btn btn-primary btn-xs" data-toggle="offcanvas">Toggle nav</button>
          </p>
          <div class="row">
            <div class="highlight" data-example-id="simple-horizontal-form">
                <form class="form-horizontal">
                  <div class="form-group">
                    <label for="inputTitle" class="col-sm-2 control-label">Title</label>
                    <div class="col-sm-6">
                      <input type="text" class="form-control" id="inputTitle" placeholder="Title" autofocus>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputName" class="col-sm-2 control-label">name</label>
                    <div class="col-sm-6">
                      <input type="text" class="form-control" id="inputName" placeholder="name" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputEmail" class="col-sm-2 control-label">Email</label>
                    <div class="col-sm-6">
                      <input type="email" class="form-control" id="inputEmail" placeholder="Email" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputPhone" class="col-sm-2 control-label">Phone</label>
                    <div class="col-sm-6">
                      <input type="text" class="form-control" id="inputPhone" placeholder="Phone" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputPassword" class="col-sm-2 control-label">Password</label>
                    <div class="col-sm-6">
                      <input type="password" class="form-control" id="inputPassword" placeholder="Password" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputHomePage" class="col-sm-2 control-label">Homepage</label>
                    <div class="col-sm-6">
                      <input type="text" class="form-control" id="inputHomePage" placeholder="Homepage">
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                      <button type="submit" class="btn btn-success">Update profile</button>
                    </div>
                  </div>
                </form><!-- form -->
            </div><!-- highlight -->
          </div><!--/row-->
        </div><!--/.col-xs-12.col-sm-9-->
        <!--/.sidebar-offcanvas-->
      </div><!--/row-->

      <hr>

      <footer>
        <p>&copy; Wei pai ge 2016</p>
      </footer>

    </div><!--/.container-->

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="js/jquery-1.11.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/offcanvas.js"></script>
  </body>
</html>
