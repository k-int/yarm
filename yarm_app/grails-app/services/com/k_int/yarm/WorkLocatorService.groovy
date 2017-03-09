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

}
