package com.k_int.yarm


import groovy.transform.CompileStatic
import org.grails.datastore.mapping.core.DatastoreUtils
import org.grails.datastore.mapping.core.connections.ConnectionSource
import org.grails.datastore.mapping.core.connections.ConnectionSourceFactory
import org.grails.datastore.mapping.core.connections.InMemoryConnectionSources
import org.springframework.core.env.PropertyResolver

// Inspiration from https://github.com/grails/grails-data-mapping/blob/master/grails-datastore-core/src/main/groovy/org/grails/datastore/mapping/core/connections/InMemoryConnectionSources.groovy

@CompileStatic
public class ConnectionSources<T, S extends ConnectionSourceSettings> extends AbstractConnectionSources<T, S>

  public ConnectionSources(ConnectionSource<T, S> defaultConnectionSource, ConnectionSourceFactory<T, S> connectionSourceFactory, PropertyResolver configuration) {
    super(defaultConnectionSource, connectionSourceFactory, configuration);
  }

  @Override
  public Iterable<ConnectionSource<T, S>> getAllConnectionSources() {
    return null;
  }

  @Override
  public ConnectionSource<T, S> getConnectionSource(String name) {
    return null;
  }

  @Override
  public ConnectionSource<T, S> addConnectionSource(String name, PropertyResolver configuration) {
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
