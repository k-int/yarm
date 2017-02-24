<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Library Resource Manager"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <asset:stylesheet src="application.css"/>

    <g:layoutHead/>
</head>
<body>

<div class="wrapper">
    <div class="row row-offcanvas row-offcanvas-left">
        <!-- sidebar -->
        <div class="column col-sm-3 col-xs-1 sidebar-offcanvas" id="sidebar">

          <ul class="nav" id="menu">
            <sec:ifLoggedIn>
              <li><g:link controller="directory" action="user" id="${current_user?.uriName}"><i class="glyphicon glyphicon-user gi-2x"></i><span class="collapse in hidden-xs"> Profile</span></g:link></li>
              <li class="divider"></li>
              <li><g:link controller="titles" action="list"><i class="glyphicon glyphicon-book gi-2x"></i><span class="collapse in hidden-xs"> Titles</span></g:link></li>
              <li><g:link controller="home" action="logout"><span class="collapse in hidden-xs">Logout</span></g:link></li>
            </sec:ifLoggedIn>
            <sec:ifNotLoggedIn>
              <li class="${controllerName=='home' && actionName=='login' ? 'active' : ''}"><g:link controller="home" action="login"><i class="glyphicon glyphicon-user gi-2x"></i>Login</g:link></li>
            </sec:ifNotLoggedIn>

                <li><a href="#"><i class="fa fa-list-alt"></i> <span class="collapse in hidden-xs">Link 1</span></a></li>
                <li><a href="#"><i class="fa fa-list"></i> <span class="collapse in hidden-xs">Stories</span></a></li>
                <li><a href="#"><i class="fa fa-paperclip"></i> <span class="collapse in hidden-xs">Saved</span></a></li>
                <li><a href="#"><i class="fa fa-refresh"></i> <span class="collapse in hidden-xs">Refresh</span></a></li>
                <li>
                    <a href="#" data-target="#item1" data-toggle="collapse"><i class="fa fa-list"></i> <span class="collapse in hidden-xs">Menu <span class="caret"></span></span></a>
                    <ul class="nav nav-stacked collapse left-submenu" id="item1">
                        <li><a href="google.com">View One</a></li>
                        <li><a href="gmail.com">View Two</a></li>
                    </ul>
                </li>
                <li>
                    <a href="#" data-target="#item2" data-toggle="collapse"><i class="fa fa-list"></i> <span class="collapse in hidden-xs">Menu <span class="caret"></span></span></a>
                    <ul class="nav nav-stacked collapse" id="item2">
                        <li><a href="#">View One</a></li>
                        <li><a href="#">View Two</a></li>
                        <li><a href="#">View Three</a></li>
                    </ul>
                </li>
                <li><a href="#"><i class="glyphicon glyphicon-list-alt"></i> <span class="collapse in hidden-xs">Link</span></a></li>
          </ul>
        </div>
        <!-- /sidebar -->

        <!-- main right col -->
        <div class="column col-sm-9 col-xs-11" id="main">
            <p><a href="#" data-toggle="offcanvas"><i class="glyphicon glyphicon-menu-hamburger gi-2x"></i></a></p>

            <p>
              <g:layoutBody/>
            </p>
        </div>
        <!-- /main -->
    </div>
</div>



  <asset:javascript src="application.js"/>

  <g:ifPageProperty name="page.footScripts">
    <g:pageProperty name="page.footScripts" />
  </g:ifPageProperty>

</body>
</html>
