package com.k_int.yarm

import org.hibernate.Transaction
import org.hibernate.StatelessSession


/**
 *  An instance - IE a particular occurrence of a work -- EG the Electronic version of New Scientist with eISSN 1234-5566
 */ 
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
      session.insert(result)

      result.addHash(session,'title',result.name)
      result.addHash(session,'discriminator',result.discr)

      // Add hashvalues for title and discriminator
      tx.commit()
    }
    result
  }

  def lookupOrCreate(identifiers,name,work) {
    log.debug("lookupOrCreate(${identifiers},${name},${work}");
  }


}
