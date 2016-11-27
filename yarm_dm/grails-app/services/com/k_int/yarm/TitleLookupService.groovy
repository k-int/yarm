package com.k_int.yarm

import grails.transaction.Transactional
import org.hibernate.Transaction
import org.hibernate.StatelessSession


/**
 *  A resource has a set of properties. Often, a combination of these properties can be used to uniquely identify
 *  an item. This form of lookup -- "Does title alone identify the item?" No? "How about Title and Primary Author"...
 *  is costly. Identifiers like ISBN and ISSN are used to try and solve this problem. The problem is that vendors
 *  are careless with these and often identify the wrong items and assign the wrong identifiers. 
 *
 *  This class implements a value based lookup like the original "Is title alone enough" strategy. After identifying
 *  an item, shared identifiers like ISSN may be used as an effective "Checksum", but the consensus is that we are in
 *  such a mess in the eResources problem space with broken identifiers, that we are better to fall back to value based
 *  lookup.
 *
 *  Our approach, then, is to make use of normalisation, hashing and an optional "Discriminator" field to create a hash
 *  which will uniquely identify any item. A rule set is defined from most general to most specific. If at any point
 *  a rule matches a unique item, we deem to have a match with the KB. If at some point we realise that 2 items
 *  hash onto a single key, more specific rules will be tried. This situation can also be resolved by adding discriminator
 *  values to the incoming items.
 */
class TitleLookupService {

  def sessionFactory
  def titleNormalisationService

  static def ruleset = [
    [ ruleName:'Simple Title and Discriminator', properties: [ 'title', 'discriminator' ] ]
  ]

  /**
   *  Use rules particular to a resource type to either look up or create a DB record
   *  based on the provided description. Return the ID of the resource. Should use stateless session
   *  and not impact other transactions
   *  @Return ID of GlobalResource - created or looked up
   *
   *  @param resource_description
   *      title: 'String'
   *      discriminator: 'String'
   *      identifiers: [ [ ns:'String', value:'String' ] ]
   */
  public Long upsert(resource_description) {

    log.debug("TitleLookupService::upsert ${resource_description}");

    def result = lookup(resource_description)
  }

  public Long lookup(resource_description) {
    StatelessSession session = sessionFactory.openStatelessSession();
    Transaction tx = session.beginTransaction();

    boolean found = false;

    for ( rule in ruleset ) {

      def query_hashes = []
      // Use the ruleset to assemble the query hashes      

      rule.properties.each { hash_property ->
        def candidate_value = resource_description[hash_property]?.toString()
        if ( ( candidate_value ) && ( candidate_value.trim().length() > 0 ) ) {
          def candidate_hash = titleNormalisationService.generateComponentHash(titleNormalisationService.normalise(candidate_value))
          query_hashes.add([property:hash_property, value:candidate_hash])
        }
      }

      // Query for any components where there is a match for the given hash set
      log.debug("Find components for hash values matching ${query_hashes}");

      if ( found )
        break;
    }

    // Lookup operations never commit
    tx.rollback()
    session.close()
  }

}
