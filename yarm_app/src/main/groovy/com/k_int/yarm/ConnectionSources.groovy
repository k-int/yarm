package com.k_int.yarm


import groovy.transform.CompileStatic
import org.grails.datastore.mapping.core.DatastoreUtils
import org.grails.datastore.mapping.core.connections.ConnectionSource
import org.grails.datastore.mapping.core.connections.ConnectionSourceFactory
import org.grails.datastore.mapping.core.connections.InMemoryConnectionSources
import org.springframework.core.env.PropertyResolver
import org.springframework.beans.factory.annotation.Autowired
import org.grails.datastore.mapping.core.connections.ConnectionSourceSettings
import org.grails.datastore.mapping.core.connections.AbstractConnectionSources

// Inspiration from https://github.com/grails/grails-data-mapping/blob/master/grails-datastore-core/src/main/groovy/org/grails/datastore/mapping/core/connections/InMemoryConnectionSources.groovy

import groovy.util.logging.*

@Log4j
public class ConnectionSources<T, S extends ConnectionSourceSettings> extends AbstractConnectionSources<T, S> {

  @javax.annotation.PostConstruct
  def init() {
    log.debug("ConnectionSources::init()");
    println("ConnectionSources::init() --println");
    Tenant t = Tenant.findByUriname('test') 
    if ( t == null ) 
      t = new Tenant(uriname:'test').save(flush:true, failOnError:true);
  }

  public ConnectionSources(ConnectionSource<T, S> defaultConnectionSource, ConnectionSourceFactory<T, S> connectionSourceFactory, PropertyResolver configuration) {
    super(defaultConnectionSource, connectionSourceFactory, configuration);
    println("ConnectionSources::ConnectionSources() --println");
    log.debug("ConnectionSources::ConnectionSources(...)");
  }

  @Override
  public Iterable<ConnectionSource<T, S>> getAllConnectionSources() {
    log.debug("getAllConnectionSources()");
    println("ConnectionSources::getAllConnectionSources() --println");
    return null;
  }

  @Override
  public ConnectionSource<T, S> getConnectionSource(String name) {
    log.debug("getConnectionSource(${name})");
    println("ConnectionSources::getConnectionSource(${name}) --println");
    return null;
  }

  @Override
  public ConnectionSource<T, S> addConnectionSource(String name, PropertyResolver configuration) {

    log.debug("addConnectionSource(${name},${configuration}");
    println("addConnectionSource(${name},${configuration}");

    if(name == null) {
      throw new IllegalArgumentException("Argument [name] cannot be null");
    }
    if(configuration == null) {
      throw new IllegalArgumentException("Argument [configuration] cannot be null");
    }

    ConnectionSource<T, S> connectionSource = connectionSourceFactory.createRuntime(name, configuration, (S)this.defaultConnectionSource.getSettings());

    if(connectionSource == null) {
      throw new IllegalStateException("ConnectionSource factory returned null");
    }

    //     this.connectionSourceMap.put(name, connectionSource);

    for(listener in listeners) {
      listener.newConnectionSource(connectionSource)
    }
    return connectionSource;
  }
}
