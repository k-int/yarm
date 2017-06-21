YARM - dev-integration

Root Directory

* settings.gradle  -- Controls build and links the yarm_dm plugin to the yarm_app and yarm_services grails apps in a dev environment.
* yarm_app -- The front end application for yarm tenants
* yarm_dm -- The data model - contained in it's own plugin module so it's easy to incorporate into different applications
* yarm_services -- The services component - backend services like sync with upstream data sources, tenant management, internal interfaces

# DNS Setup

The app is configured to look for postgres on phgost - this is to insulate the developer from docker/local/other configurations. 
Developers working with a local pg can simply add pghost to the list of names agains 127.0.0.1, for example, on ubuntu edit the 127.0.0.1 line of /etc/hosts  to add

    127.0.0.1	localhost pghost

Tenants are set up using the domain name tenant resolver. For developers, this raises the question of how to emulate DNS tenants. The dev system is configured with a
demo tenant. In order to access this tenant on http://demo.localdomain:8080/ add demo.localdomain to the same line in the hosts file, giving:

    127.0.0.1	localhost pghost demo.localdomain

# DB Setup

If you are running postgres inside a docker container, use

    psql -h localhost -U postgres
    -- You will have set a PGSql Password at install - change it from any default!

If you are running postgres locally sudo su - postgres

Once connected, issue

    CREATE USER knowint WITH PASSWORD 'knowint';
    DROP DATABASE yarm_dev;
    CREATE DATABASE yarm_dev;
    GRANT ALL PRIVILEGES ON DATABASE yarm_dev to knowint;
    CREATE DATABASE yarm_t_demo;
    GRANT ALL PRIVILEGES ON DATABASE yarm_t_demo to knowint;


DB Session usually checked with

    psql -h localhost -U knowint yarm_dev


# Build

The yarm_app can be built without building the dm dependency first. Currently targeted at grais 3.2.3 (This will go up in line with grails releases however) run

    grails run-app

to run the app

# Multi Tenant configuration

The hostname/dns tenant resolver is specified in application.yml - the app uses DISCRIMINATOR in some classes to differentiate tenant data

# OAuth config

* http://plugins.grails.org/plugin/spring-security-oauth2

Google OAuth creds page::
https://console.developers.google.com/apis/credentials

# Configuring OAuth service provider

Google API keys are passed to the app via environment variables set before starting the app

    export YARM_GOOGLE_API_KEY="xxxxx"
    export YARM_GOOGLE_SECRET="xxxxx"
    export YARM_ADM_PW="xxxx"

Usually these are set in a script that wraps the start command




Current work - having the / route use a different layout for logged in / not logged in users
* Current resources
    * https://github.com/davezuko/react-redux-starter-kit/issues/831
    * http://stackoverflow.com/questions/35354422/how-to-share-the-root-url-according-to-the-auth-status-using-reactjs-redux-and
    * https://github.com/davezuko/react-redux-starter-kit/issues/906


Notes on mongo and grails::
* http://stackoverflow.com/questions/31707811/installing-and-using-mongodb-in-grails-3-x
* http://gorm.grails.org/latest/mongodb/manual/index.html
* http://gorm.grails.org/latest/mongodb/manual/index.html#multiTenancy


http://krixi.com/index.php/2016/05/17/bulk-insert-grails-gorm/#more-174


## Next Tasks

Add Package to Agreement
Add Title to Agreement
Share Agreement
Sign Agreement
Search My Titles
Export Title List

KB
Search Packages
Search Titles


https://github.com/yury-dymov/redux-oauth-client-demo


# Assembly

at the root level 


    gradle yarmAssemble

Should assembly all the components into a runnable unit


Extras

ln -s ./ImplicitAuthSvc/ImplicitAuthService ./implicitauth

git submodule update --init --recursive

