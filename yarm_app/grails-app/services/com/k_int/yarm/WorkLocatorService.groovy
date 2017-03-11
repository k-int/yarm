package com.k_int.yarm

import org.springframework.transaction.annotation.*
import com.k_int.grails.tools.utils.GOKbTextUtils

/*
 */

class WorkLocatorService {

  

  def locateWorkFor(titleinfo) {

    long start_tm = System.currentTimeMillis();
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

    log.debug("time-locateWorkFor(${titleinfo.title.name},...) elapsed:${System.currentTimeMillis() - start_tm}");
    result
  }

  /**
   *   Return the ID of a global resource diven the description and work ID.
   *   This function shoud eventually implement partitioned executors so that we can't create duplicate
   *   titles. For now this will suffice tho.
   */
  def locateGlobalResourceIdFor(description, work) {

    long start_tm = System.currentTimeMillis();

    log.debug("Looking up global resource ${description.title}");
    def global_resource_id = null;

    List<GlobalResource> matches = GlobalResource.lookup(description.identifiers,'g.id')
    switch ( matches.size() ) {
      case 0:
        long create_start_tm = System.currentTimeMillis()
        def new_res = GlobalResource.create(description, work)
        global_resource_id = new_res.id
        log.debug("time-CreateGlobalResource : ${System.currentTimeMillis() - create_start_tm}");
        break;
      case 1:
        global_resource_id = matches.get(0)
        break;
      default:
        throw new RuntimeException("Package Title Row Matched Multiple Items - this is an error in the package definition or the KB, please correct and re-ingest");
        break;
    }

    log.debug("time-locateGlobalResourceFor(${description.title},...) elapsed:${System.currentTimeMillis() - start_tm} - result ${global_resource_id}");

    global_resource_id;
  }

}
