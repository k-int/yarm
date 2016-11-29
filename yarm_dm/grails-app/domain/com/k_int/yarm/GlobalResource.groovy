package com.k_int.yarm

import org.hibernate.Transaction
import org.hibernate.StatelessSession


public class GlobalResource extends Component {

  Work work
  
  static mapping = {
    work column:'work_fk'
  }


  static constraints = {
  }


  public static GlobalResource create(Map resource_description) {
    def result = null
    if ( resource_description.title ) {
      StatelessSession session = sessionFactory.openStatelessSession();
      Transaction tx = session.beginTransaction();
      result = new GlobalResource()
      result.name = resource_description.title
      result.discriminator = resource_description.discriminator
      result.normname = null
      result.shortcode = null
      result.save(flush:true, failOnError:true)

      addHash(session,'title',name)
      addHash(session,'discriminator',discriminator)

      // Add hashvalues for title and discriminator
      tx.commit()
      session.close()
    }
    result
  }

}
