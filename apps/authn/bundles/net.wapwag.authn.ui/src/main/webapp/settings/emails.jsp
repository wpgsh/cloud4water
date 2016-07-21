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
                <h3 class="panel-title font-color">Email</h3>
              </div>
              <div class="panel-body" data-example-id="simple-horizontal-form">
                <form class="form-horizontal">
                  <div class="form-group">
                    <div class="col-sm-12 margin-bottom">
                      <label for="inputEmail" class="control-label">Email</label>                    
                    </div>
                    <div class="col-sm-6">
                      <input type="email" class="form-control" id="inputEmail" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-12">
                      <button type="submit" class="btn btn-success">Save email</button>
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
  </body>
</html>
