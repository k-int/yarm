package com.k_int.yarm

// import com.k_int.goai.OaiClient
import java.text.SimpleDateFormat
import org.springframework.transaction.annotation.*

public class PackageLoadingService {

  def executorService
  def running = false;


  // Main configuration map
  def rectypes = [
    // [ name:'Package', converter:packageConv, reconciler:packageReconcile, newRemoteRecordHandler:null, complianceCheck:testPackageCompliance ],
    // [ name:'Title', converter:titleConv, reconciler:titleReconcile, newRemoteRecordHandler:onNewTitle, complianceCheck:testTitleCompliance ],
  ]


  @javax.annotation.PostConstruct
  def init() {
    log.debug("PackageLoadingService::init");
  }

  public void triggerSync() {
    sychronized(this) {
      if ( running == false ) {
        running = true;
        runSync()
      }
    }
  }

  public void runSync() {
    log.debug("PackageLoadingService::runSync");

    def jobs = RemoteKB.executeQuery('select rkb.id, rbk.type from RemoteKB as rkb');

    jobs.each { sync_job ->
      log.debug(sync_job);
      // String identifier
      // String name
      // String type
      // Date haveUpTo
      // String uri
      // String listPrefix
      // String fullPrefix
      // String principal
      // String credentials
      switch ( sync_job[1] ) {
        case 'OAI':
          log.debug("start internal sync");
          this.doOAISync(sync_job[0])
          log.debug("this.doOAISync has returned...");
          break;
        default:
          log.error("Unhandled sync job type: ${sync_job[1]}");
          break;
      }
    }

    running = false
  }

  def private doOAISync(sync_job) {
    log.debug("doOAISync");
    if ( parallel_jobs ) {
      def future = executorService.submit({ intOAI(sync_job.id) } as java.util.concurrent.Callable)
    }
    else {
      intOAI(sync_job.id)
    }
    log.debug("doneOAISync");
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  def intOAI(sync_job_id) {
    log.debug("internalOAI processing ${sync_job_id}");
  }

}
