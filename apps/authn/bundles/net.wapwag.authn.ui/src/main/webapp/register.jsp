<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Sign up</title>
    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="css/sign/sign.css" rel="stylesheet">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <div class="container">
      <form class="form-signin" action="home.html">
        <div class="logo-div">
            <img src="images/wpg.png" width="118" height="41">
        </div>
        <h2 class="form-signin-heading logo-title">Authentication Application</h2>
        <label for="inputName" class="sr-only">Username</label>
        <input type="text" id="inputName" class="form-control" placeholder="username" required autofocus>
        <label for="inputEmail" class="sr-only">Email</label>
        <input type="email" id="inputEmail" class="form-control" placeholder="email" required>
        <label for="inputPhone" class="sr-only">Phone</label>
        <input type="text" id="inputPhone" class="form-control" placeholder="phone" required>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" class="form-control" placeholder="password" required>
        <label for="confirmPassword" class="sr-only">Confirm password</label>
        <input type="password" id="confirmPassword" class="form-control" placeholder="confirm password" required>
        <button class="btn btn-lg btn-success btn-block" type="submit" id="register">Sign up</button>
        <div class="checkbox">
          <label>
            Already have an account? <a href="index.html">To login.</a>
          </label>
        </div>
      </form>
      <div class="bottom-content">
        Copyright © 2016.Wei Pai Ge All rights reserved
      </div>
    </div> <!-- /container -->

    <!-- Javascript for login -->
    <script src="js/jquery-1.11.1.min.js"></script>
    <script src="js/main/index.js"></script>
  </body>
</html>
