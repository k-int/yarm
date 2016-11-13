package com.k_int.yarm


import org.grails.datastore.mapping.core.DatastoreUtils
import org.grails.datastore.mapping.core.connections.ConnectionSource
import org.grails.datastore.mapping.core.connections.ConnectionSourceFactory
import org.grails.datastore.mapping.core.connections.InMemoryConnectionSources
import org.springframework.core.env.PropertyResolver
import org.springframework.beans.factory.annotation.Autowired
import org.grails.datastore.mapping.core.connections.ConnectionSourceSettings
import org.grails.datastore.mapping.core.connections.AbstractConnectionSources

import org.springframework.core.env.PropertyResolver;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


// Inspiration from https://github.com/grails/grails-data-mapping/blob/master/grails-datastore-core/src/main/groovy/org/grails/datastore/mapping/core/connections/InMemoryConnectionSources.groovy

import groovy.util.logging.*

@Log4j
public class ConnectionSources<T, S extends ConnectionSourceSettings> extends AbstractConnectionSources<T, S> {

  protected final Map<String, ConnectionSource<T, S>> connectionSourceMap = new ConcurrentHashMap<>();

  @javax.annotation.PostConstruct
  def init() {
    log.debug("ConnectionSources::init()");
  }

  public ConnectionSources(ConnectionSource<T, S> defaultConnectionSource, ConnectionSourceFactory<T, S> connectionSourceFactory, PropertyResolver configuration) {
    super(defaultConnectionSource, connectionSourceFactory, configuration);

    this.connectionSourceMap.put(ConnectionSource.DEFAULT, defaultConnectionSource);

    for(String name : getConnectionSourceNames(connectionSourceFactory, configuration)) {
      log.debug("Register ds ${name}");
      if(name.equals("dataSource")) continue; // data source is reserved name for the default
      ConnectionSource<T, S> connectionSource = connectionSourceFactory.create(name, configuration, defaultConnectionSource.getSettings());
      if(connectionSource != null) {
        this.connectionSourceMap.put(name, connectionSource);
      }
    }
  }

  @Override
  public Iterable<ConnectionSource<T, S>> getAllConnectionSources() {
    log.debug("getAllConnectionSources()");
    return Collections.unmodifiableCollection(this.connectionSourceMap.values());
  }

  @Override
  public ConnectionSource<T, S> getConnectionSource(String name) {
    log.debug("getConnectionSource(${name})");
    return this.connectionSourceMap.get(name);
  }

  @Override
  public ConnectionSource<T, S> addConnectionSource(String name, PropertyResolver configuration) {
    log.debug("addConnectionSource(${name},${configuration}");

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

    log.debug("Adding ${name} to connection source map");
    this.connectionSourceMap.put(name, connectionSource);

    for(listener in listeners) {
      listener.newConnectionSource(connectionSource)
    }

    return connectionSource;
  }
}
