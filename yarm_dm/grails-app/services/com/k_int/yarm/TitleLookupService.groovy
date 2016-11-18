package com.k_int.yarm

//import grails.transaction.Transactional
//import org.hibernate.Transaction
//import org.hibernate.Session

class TitleLookupService {

  def sessionFactory

  /**
   *  Use rules particular to a resource type to either look up or create a DB record
   *  based on the provided description. Return the ID of the resource. Should use stateless session
   *  and not impact other transactions
   *  @Return ID of GlobalResource - created or looked up
   */
  public Long upsert(resource_description) {
//    Session session = sessionFactory.openStatelessSession();
//    Transaction tx = session.beginTransaction();
//    tx.commit()
//    session.close()
  }

}
