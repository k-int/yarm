package com.k_int.yarm

import grails.transaction.Transactional
import org.grails.orm.hibernate.connections.HibernateConnectionSource
import org.grails.datastore.mapping.core.DatastoreUtils

@Transactional
class TenantManagerService {

  // @Autowired
  // HibernateDatastore hibernateDatastore
  def hibernateDatastore

  Date highest_timestamp = new Date(0);

  def getLatestTenants() {
    log.debug("TenantManagerService::getLatestTenants()");
    Tenant.executeQuery('select t from Tenant as t where t.dateCreated > :d order by t.dateCreated',[d:highest_timestamp]).each {
      log.debug("Adding tenant : ${it}");
      if ( highest_timestamp < it.dateCreated ) {
        highest_timestamp = it.dateCreated
      }

      // http://gorm.grails.org/6.0.x/hibernate/api/org/grails/datastore/mapping/core/connections/ConnectionSource.html
      def def_ds = hibernateDatastore.getConnectionSources().getDefaultConnectionSource();

      def new_ds_settings = def_ds.getSettings()
      def new_ds_source = def_ds.getSource()

      // See http://gorm.grails.org/latest/hibernate/api/org/grails/orm/hibernate/connections/HibernateConnectionSourceSettings.html
      log.debug("Settings for new ds: ${new_ds_settings.toProperties()} clsname:${new_ds_settings.class.name}");

      log.debug("Source for new ds: ${new_ds_source} ${new_ds_source.class.name}");

      def new_ds_config = [
                           dbCreate:'update',
                           url:'jdbc:postgresql://pghost:5432/'+it.uriname,
                           username:'knowint',
                           password:'knowint',
                           driverClassName:'org.postgresql.Driver',
                           dialect:'org.hibernate.dialect.PostgreSQLDialect'
                          ]

      HibernateConnectionSource new_ds = hibernateDatastore.getConnectionSources().addConnectionSource(it.uriname, 
                                                DatastoreUtils.createPropertyResolver(new_ds_config));

      log.debug("Created new ds ${new_ds} with url ${new_ds_config.url}");
    }
  }
}
