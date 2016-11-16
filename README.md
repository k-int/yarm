YARM experimental branch

yarmapi - backend
yarm_ux - frontend
  nvm use v6.5.0
  npm start


Current work - having the / route use a different layout for logged in / not logged in users
  -- Current resources
    -- https://github.com/davezuko/react-redux-starter-kit/issues/831
    -- http://stackoverflow.com/questions/35354422/how-to-share-the-root-url-according-to-the-auth-status-using-reactjs-redux-and
    -- https://github.com/davezuko/react-redux-starter-kit/issues/906


Notes on mongo and grails::
    -- http://stackoverflow.com/questions/31707811/installing-and-using-mongodb-in-grails-3-x
    -- http://gorm.grails.org/latest/mongodb/manual/index.html
    -- http://gorm.grails.org/latest/mongodb/manual/index.html#multiTenancy


DB Setup -- 

If you are running postgres inside a docker container, use

    psql -h localhost -U postgres

    You will have set a PGSql Password at install - change it from any default!

If you are running a local container just connect as needed

To get a session and then issue

    CREATE USER knowint WITH PASSWORD 'knowint';
    CREATE DATABASE yarm_dev;
    GRANT ALL PRIVILEGES ON DATABASE yarm_dev to knowint;
    CREATE DATABASE yarm_t_demo;
    GRANT ALL PRIVILEGES ON DATABASE yarm_t_demo to knowint;


http://plugins.grails.org/plugin/spring-security-oauth2
