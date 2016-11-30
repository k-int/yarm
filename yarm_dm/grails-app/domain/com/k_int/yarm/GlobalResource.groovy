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


  public static GlobalResource create(session, 
                                      Map resource_description,
                                      work) {
    def result = null
    if ( resource_description.title ) {
      Transaction tx = session.beginTransaction();
      result = new GlobalResource()
      result.name = resource_description.title
      result.discr = resource_description.discriminator
      result.normname = null
      result.shortcode = null
      result.work = work
      result.save(flush:true, failOnError:true)

      result.addHash(session,'title',result.name)
      result.addHash(session,'discriminator',result.discriminator)

      // Add hashvalues for title and discriminator
      tx.commit()
      session.close()
    }
    result
  }

}
