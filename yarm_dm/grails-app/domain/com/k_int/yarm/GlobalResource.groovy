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

  public static List<GlobalResource> lookup(List identifiers, String select_list) {
    log.debug("lookup(${identifiers}");

    def result = lookupByValueOnly(identifiers, select_list)
    if ( result.size() > 1 ) {
      // Too many matches when trying to lookup by value only, lets try with namespaces
      result = lookupByValueAndNamespace(identifiers, select_list)
    }
    result
  }

  public static List<GlobalResource> lookupByValueAndNamespace(List identifiers, String select_list) {
    String base_query = 'select '+select_list+' from GlobalResource as g where exists ( select io from IdentifierOccurrence as io where '
    StringWriter sw = new StringWriter()
    def bindvars = [:]
    int ctr=0;
    identifiers.each { id ->
      if ( ctr>0 ) { sw.write(' OR ' ) }
      sw.write("(io.owner = g AND io.identifier.normalisedValue = :v${ctr} AND io.identifier.namespace.ns = :ns${ctr} )");
      bindvars["v${ctr}"] = Identifier.normalizeIdentifier(id.value)
      bindvars["ns${ctr}"] = id.namespace
      ctr++
    }
    def lookup_query = base_query+sw.toString()+' ) ';
    // log.debug("Lookup global resource ${lookup_query} ${bindvars}");
    GlobalResource.executeQuery(lookup_query, bindvars);
  }

  private static List<GlobalResource> lookupByValueOnly(List identifiers, String select_list) {
    String base_query = 'select '+select_list+' from GlobalResource as g where exists ( select io from IdentifierOccurrence as io where '
    StringWriter sw = new StringWriter()
    def bindvars = [:]
    int ctr=0;
    identifiers.each { id ->
      if ( ctr>0 ) { sw.write(' OR ' ) }
      sw.write("(io.owner = g AND io.identifier.normalisedValue = :v${ctr})");
      bindvars["v${ctr}"] = Identifier.normalizeIdentifier(id.value)
      ctr++
    }
    def lookup_query = base_query+sw.toString()+' ) ';
    log.debug("Created query ${lookup_query}, ${bindvars}");
    def result = GlobalResource.executeQuery(lookup_query, bindvars);
    log.debug("Compete - found ${result.size()} items");

    result;
  }

  public static lookupOrCreate(idlist,name) {
    log.debug("GlobalResource.lookupOrCreate(${idlist},${name})")

    def result = null
    def matches = GlobalResource.lookup(idlist,'g')
    switch ( matches.size() ) {
      case 0:
        throw new RuntimeException("GlobalResource.lookupOrCreate(${idlist},${name}) not implemented");
        //result = GlobalResource.create()
        break;
      case 1:
        result = matches.get(0);
        break;
      default:
        throw new RuntimeException("Too many matches (${matches.size()})");
        break;
    }
    result
  }


}
