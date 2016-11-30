package com.k_int.yarm

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
      result.discriminator = resource_description.discriminator
      result.save(flush:true, failOnError:true)
      addHash(session,'title',result.name)
      addHash(session,'discriminator',result.discriminator)
      tx.commit()
      session.close()
    }
    result.id
  }

}
