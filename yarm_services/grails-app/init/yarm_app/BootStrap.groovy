package yarm_app

import com.k_int.yarm.*

class BootStrap {

  def init = { servletContext ->
    log.debug("BootStrap::init");

    def gokb_record_source = RemoteKB.findByIdentifier('gokbPackages') ?: new RemoteKB(
                                                                                           identifier:'gokbPackages',
                                                                                           name:'GOKB',
                                                                                           type:'OAI',
                                                                                           haveUpTo:null,
                                                                                           uri:'https://gokb.kuali.org/gokb/oai/packages',
                                                                                           listPrefix:'oai_dc',
                                                                                           fullPrefix:'gokb',
                                                                                           principal:null,
                                                                                           credentials:null,
                                                                                           rectype:0)
    gokb_record_source.save(flush:true, stopOnError:true);
    log.debug("New gokb record source: ${gokb_record_source}");
  }

  def destroy = {
  }
}
