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

  @javax.annotation.PostConstruct
  def init() {
    log.debug("TitleLookupService::init");
    // Configure 26 Linked/DynamicBlockingQueue instances and set up Executor pool
  }



  static def ruleset = [
    [ ruleName:'Simple Title and Discriminator', properties: [ 'title', 'discriminator' ] ]
  ]

  /**
   *  Use rules particular to a resource type to either look up or create a DB record
   *  based on the provided description. Return the ID of the resource. Should use stateless session
   *  and not impact other transactions
   *
   *  In a heavy usage environment it is important to be able to scale out the lookup process, as a particular
   *  package may create thousands of resource records. This method is the control point for that process.
   *  Work queues are created based on the hashed title. These work queues are the serviced by a worker pool
   *  that can be scaled in accordance with the installation. So long as titles which are in the same "Family"
   *  are processed in the same queue we will never see duplicate items created because 2 threads got the same
   *  title at the same time. http://groovy-programming.com/post/26923146865
   *
   *  @Return ID of GlobalResource - created or looked up
   *
   *  @param resource_description
   *      type: 'com.k_int.yarm.GlobalResource',
   *      title: 'String',
   *      discriminator: 'String',
   *      identifiers: [ [ ns:'String', value:'String' ] ]
   */
  public Long upsert(resource_description) {
    internalUpsert(resource_description)
  }

  private Long internalUpsert(resource_description) {
    def result = null;
    log.debug("TitleLookupService::upsert ${resource_description}");
    def lookup_result = lookup(resource_description)
    switch ( lookup_result.status ) {
      case 0:
        log.debug("Does not exist in KB. Create");
        def cls = Class.forName(resource_description.type)
        def res_obj = cls.create(resource_description)
        result = res_obj.id
        break;
      case 1:
        result = lookup_result.id
        break;
      case 2:
        log.debug("Ambiguous lookup - define discriminators");
        break;
      default:
        throw new RuntimeException("Unexpected return code from lookup : ${result.status}");
        break;
    }

    result;
  }

  

  public Map lookup(resource_description) {

    // Start off assuming we have not been able to look up a unique item
    def result = [status:2]

    StatelessSession session = sessionFactory.openStatelessSession();
    Transaction tx = session.beginTransaction();

    boolean resolved = false;

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
      def qry_params = []
      StringWriter sw = new StringWriter();
      sw.write('select c.id from Component as c where ');
      def first=true;
      query_hashes.each { qhc ->
        first ? first=false : sw.write(' and ');
        sw.write('exists ( select ch from ComponentHash as ch where ch.owner = c and ch.hashType.value = ? and ch.hashValue = ? )')
        qry_params.add(qhc.property)
        qry_params.add(qhc.value)
      }

      def qry = sw.toString()

      log.debug("Lookup query : ${qry} ${qry_params}");

      def matching_components = Component.executeQuery(qry, qry_params)

      switch (matching_components.size()) {
        case 0:
          // No matches - this must be a new item
          resolved=true
          result.status=0
          break;
        case 1:
          resolved=true
          result.status=1
          result.id=matching_components.get(0);
          break;
        default:
          break;
      }

      if ( resolved ) {
        break;
      }
    }

    // Lookup operations never commit
    tx.rollback()
    session.close()
    result
  }

}
