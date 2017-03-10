package com.k_int.yarm

import org.springframework.transaction.annotation.*
import com.k_int.grails.tools.utils.GOKbTextUtils

/*
 */

class WorkLocatorService {


  def locateWorkFor(titleinfo) {

    def result = null;

    if ( ( titleinfo?.title?.name == null ) ||  
         ( titleinfo.title.name.length() == 0 ) ) 
      throw new RuntimeException("Cannot locate a work without a title ${titleinfo}");
   
    def h = GOKbTextUtils.simpleComponentHash(titleinfo.title.name)

    log.debug("Searching for bucket matches for ${h}");
    def bucketMatches = Work.executeQuery('select w from Work as w where w.componentHash = :h',[h:h]);

    switch( bucketMatches.size() ) {
      case 0:
        log.debug("No matches - create work ${titleinfo.title.name} ${h}");
        result = new Work(name: titleinfo.title.name, componentHash:h).save(flush:true, failOnError:true)
        result.save(flush:true, failOnError:true)
        break;
      case 1:
        log.debug("Good enough unique match on componentHash");
        result = bucketMatches[0]
        break;
      default:
        log.debug("Mached multiple works - use discriminator properties");
        break;
    }

    result
  }

  /**
   *   Return the ID of a global resource diven the description and work ID.
   *   This function shoud eventually implement partitioned executors so that we can't create duplicate
   *   titles. For now this will suffice tho.
   */
  def locateGlobalResourceFor(description, work) {
    log.debug("Looking up global resource ${description}");
    def result = null;

    List<GlobalResource> matches = GlobalResource.lookup(description.identifiers)
    switch ( matches.size() ) {
      case 0:
        result = GlobalResource.create(description, work)
        break;
      case 1:
        result = matches.get(0)
        break;
      default:
        throw new RuntimeException("Package Title Row Matched Multiple Items - this is an error in the package definition or the KB, please correct and re-ingest");
        break;
    }
    result;
  }

}
