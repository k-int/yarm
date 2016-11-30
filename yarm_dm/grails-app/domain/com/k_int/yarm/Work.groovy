package com.k_int.yarm

import org.hibernate.Transaction
import org.hibernate.StatelessSession

public class Work extends Component {
  
  static mapping = {
  }


  static constraints = {
  }

  public static Work create(session, Map resource_description) {
    def result = null
    if ( resource_description.title ) {
      Transaction tx = session.beginTransaction();
      result = new Work()
      result.name = resource_description.title
      result.discr = resource_description.discriminator
      session.insert(result)
      result.addHash(session,'title',result.name)
      result.addHash(session,'discriminator',result.discr)
      tx.commit()
    }
    result
  }

}
