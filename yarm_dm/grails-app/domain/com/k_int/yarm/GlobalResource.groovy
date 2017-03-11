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


  public static GlobalResource create(Map resource_description, work) {

    log.debug("GlobalResource::Create(${resource_description},...)");

    def result = null
    if ( resource_description.title ) {
      result = new GlobalResource()
      result.name = resource_description.title?.name
      result.discr = resource_description.discriminator
      result.normname = null
      result.shortcode = null
      result.work = work
      result.save(flush:true, failOnError:true);

      resource_description.identifiers.each { id ->
        def new_io = new IdentifierOccurrence( owner:result,  identifier:Identifier.lookupOrCreateCanonicalIdentifier(id.namespace, id.value)).save(flush:true, failOnError:true)
      }

      log.debug("GlobalResource created, refresh");

      result.refresh()
      // result.addHash(session,'title',result.name)
      // result.addHash(session,'discriminator',result.discr)
    }
    log.debug("GlobalResource::create returning");
    result
  }

  public static List<GlobalResource> lookup(List identifiers) {
    log.debug("lookup(${identifiers}");

    def result = lookupByValueOnly(identifiers)
    if ( result.size() > 1 ) {
      // Too many matches when trying to lookup by value only, lets try with namespaces
      result = lookupByValueAndNamespace(identifiers)
    }
    result
  }

  public static List<GlobalResource> lookupByValueAndNamespace(List identifiers) {
    String base_query = 'select g from GlobalResource as g where exists ( select io from IdentifierOccurrence as io where '
    StringWriter sw = new StringWriter()
    def bindvars = [:]
    int ctr=0;
    identifiers.each { id ->
      if ( ctr>0 ) { sw.write(' OR ' ) }
      sw.write("(io.owner = g AND io.identifier.value = :v${ctr} AND io.identifier.namespace.ns = :ns${ctr} )");
      bindvars["v${ctr}"] = id.value
      bindvars["ns${ctr}"] = id.namespace
      ctr++
    }
    def lookup_query = base_query+sw.toString()+' ) ';
    // log.debug("Lookup global resource ${lookup_query} ${bindvars}");
    GlobalResource.executeQuery(lookup_query, bindvars);
  }

  private static List<GlobalResource> lookupByValueOnly(List identifiers) {
    String base_query = 'select g from GlobalResource as g where exists ( select io from IdentifierOccurrence as io where '
    StringWriter sw = new StringWriter()
    def bindvars = [:]
    int ctr=0;
    identifiers.each { id ->
      if ( ctr>0 ) { sw.write(' OR ' ) }
      sw.write("(io.owner = g AND io.identifier.value = :v${ctr})");
      bindvars["v${ctr}"] = id.value
      ctr++
    }
    def lookup_query = base_query+sw.toString()+' ) ';
    GlobalResource.executeQuery(lookup_query, bindvars);
  }


}
