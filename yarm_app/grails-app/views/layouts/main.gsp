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

  <div class="navbar navbar-default navbar-fixed-top">

    <div class="container-fluid">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">YARM</a>
      </div>

      <div class="collapse navbar-collapse pull-right">
        <ul class="nav navbar-nav">
          <sec:ifLoggedIn>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown"><sec:username/><b class="caret"></b></a>
              <ul class="dropdown-menu pull-right">
                <li><g:link controller="directory" action="user" id="${current_user?.uriName}">Your Profile</g:link></li>
                <li class="divider"></li>
                <li><g:link controller="home" action="logout">Logout</g:link></li>
              </ul>
            </li>
          </sec:ifLoggedIn>
          <sec:ifNotLoggedIn>
            <li class="${controllerName=='home' && actionName=='login' ? 'active' : ''}"><g:link controller="home" action="login">Login</g:link></li>
          </sec:ifNotLoggedIn>
        </ul>
      </div>
    </div>
  </div>


  <div class="wrapper">
    <div class="row row-offcanvas row-offcanvas-left">
        <!-- sidebar -->
        <div class="column col-sm-3 col-xs-1 sidebar-offcanvas" id="sidebar">
          <ul class="nav" id="menu">
            <li><a href="#" data-toggle="offcanvas"><i class="glyphicon glyphicon-menu-hamburger gi-2x"></i></a></li>
            <sec:ifLoggedIn>
              <li><g:link controller="directory" action="user" id="${current_user?.uriName}"><i class="glyphicon glyphicon-user gi-2x"></i><span class="collapse in hidden-xs"> Profile</span></g:link></li>
              <li class="divider"></li>
              <li><g:link controller="home" action="index"><i class="glyphicon glyphicon-home gi-2x"></i><span class="collapse in hidden-xs"> Home</span></g:link></li>
              <li><g:link controller="home" action="switchContext"><i class="glyphicon glyphicon-education gi-2x"></i><span class="collapse in hidden-xs"> Change Home Institution</span></g:link></li>

              <g:if test="${params.institution_shortcode}">

                <li><g:link mapping="tenantTitles" 
                            params="${[institution_shortcode:params.institution_shortcode]}"><i class="glyphicon glyphicon-book gi-2x"></i><span class="collapse in hidden-xs"> Titles</span></g:link></li>


                <li>
                  <a href="#" data-target="#agreements-menu"
                              data-toggle="collapse"><i class="glyphicon glyphicon-file gi-2x"></i> <span class="collapse in hidden-xs">Agreements <span class="caret"></span></span></a>

                  <ul class="nav nav-stacked collapse left-submenu" id="agreements-menu">
                    <li><g:link mapping="tenantAgreements"
                                params="${[institution_shortcode:params.institution_shortcode]}"><i class="glyphicon glyphicon-file gi-2x"></i><span class="collapse in hidden-xs"> Current</span></g:link></li>
                  </ul>
                </li>
              </g:if>

              <li>
                  <a href="#" data-target="#kb-menu"
                              data-toggle="collapse"><i class="glyphicon glyphicon-file gi-2x"></i> <span class="collapse in hidden-xs">KB <span class="caret"></span></span></a>
                  <ul class="nav nav-stacked collapse left-submenu" id="kb-menu">
                    <li><g:link controller="kb" action="packages"><i class="glyphicon glyphicon-file gi-2x"></i><span class="collapse in hidden-xs"> Packages</span></g:link></li>
                    <li><g:link controller="kb" action="titles"><i class="glyphicon glyphicon-file gi-2x"></i><span class="collapse in hidden-xs"> Titles</span></g:link></li>
                  </ul>
              </li>

              <li><g:link controller="home" action="logout"><i class="glyphicon glyphicon-home gi-2x"></i><span class="collapse in hidden-xs"> Logout</span></g:link></li>

              <sec:ifAnyGranted roles="ROLE_ADMIN">
                <li>
                  <a href="#" data-target="#item1"
                              data-toggle="collapse"><i class="glyphicon glyphicon-certificate gi-2x"></i> <span class="collapse in hidden-xs">Admin <span class="caret"></span></span></a>
                  <ul class="nav nav-stacked collapse left-submenu" id="item1">
                    <li><a href="google.com"><i class="glyphicon glyphicon-lock gi-2x"></i><span class="collapse in hidden-xs"> Manage Affiliations</span></a></li>
                    <li><g:link controller="admin" action="orgsDataload"><i class="glyphicon glyphicon-cloud-upload gi-2x"></i><span class="collapse in hidden-xs"> Upload Organisations Data</span></g:link></li>
                    <li><g:link controller="admin" action="sync"><i class="glyphicon glyphicon-cloud-download gi-2x"></i><span class="collapse in hidden-xs"> Force Sync</span></g:link></li>
                    <li><link:admManageGlobalSources>Manage Global Sources</link:admManageGlobalSources></li>
                    <li><g:link mapping="admManageTenants">Manage Tenants</g:link></li>
                    <li><g:link mapping="admManageUsers">Manage Users</g:link></li>
                  </ul>
                </li>
              </sec:ifAnyGranted>

            </sec:ifLoggedIn>

          </ul>
        </div>
        <!-- /sidebar -->

        <!-- main right col -->
        <div class="column col-sm-9 col-xs-11" id="main">
          <g:layoutBody/>
        </div>
        <!-- /main -->
    </div>
  </div>

  <g:ifPageProperty name="page.footScripts">
    <g:pageProperty name="page.footScripts" />
  </g:ifPageProperty>

 

</body>
</html>
