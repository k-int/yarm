<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
</head>
<body>
    <div class="container" id="content" role="main">
      <h1>This is the main view</h1>
      <p>
        Hello
        <oauth2:connect provider="google" id="google-connect-link"><button class="btn">Google</button></oauth2:connect>

        Logged with google?
        <oauth2:ifLoggedInWith provider="google">yes</oauth2:ifLoggedInWith>
        <oauth2:ifNotLoggedInWith provider="google">no</oauth2:ifNotLoggedInWith>
      </p>
    </div>

</body>
</html>
