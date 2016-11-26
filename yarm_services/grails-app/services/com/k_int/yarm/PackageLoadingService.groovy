package com.k_int.yarm

import com.k_int.goai.OaiClient
import java.text.SimpleDateFormat
import org.springframework.transaction.annotation.*

public class PackageLoadingService {

  def executorService
  def running = false;
  def parallel_jobs = false
  def titleLookupService

  /*
   *  Implementing new rectypes..
   *  the reconciler closure is responsible for reconciling the previous version of a record and the latest version
   *  the converter is respnsible for creating the map strucuture passed to the reconciler. It needs to return a [:] sorted appropriate
   *  to the work the reconciler will need to do (Often this includes sorting lists)
   */



 

  @javax.annotation.PostConstruct
  def init() {
    log.debug("PackageLoadingService::init");
  }

  public void triggerSync() {
    log.debug("PackageLoadingService::triggerSync()");
    synchronized(this) {
      if ( running == false ) {
        running = true;
        runSync()
      }
    }
  }

  public void runSync() {
    log.debug("PackageLoadingService::runSync");

    def jobs = RemoteKB.executeQuery('select rkb.id, rkb.type from RemoteKB as rkb');

    jobs.each { sync_job ->
      log.debug("running sync job ${sync_job}");
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
      intOAI(sync_job)
    }
    log.debug("doneOAISync");
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  def intOAI(sync_job_id) {
    log.debug("internalOAI processing ${sync_job_id}");
    def sync_job = RemoteKB.get(sync_job_id)

    int rectype = sync_job.rectype.longValue()
    def cfg = rectypes[rectype]

    if ( cfg ) {

      log.debug("Use config ${cfg}");

      def oai_client = new OaiClient(host:sync_job.uri)
      def max_timestamp = 0
      def date = null
     
      log.debug("Collect changes since ${date}");

      oai_client.getChangesSince(date, sync_job.fullPrefix) { rec ->
        log.debug("Got OAI Record ${rec.header.identifier} datestamp: ${rec.header.datestamp} job:${sync_job.id} url:${sync_job.uri}");
        def r = cfg.converter.call(rec.metadata, sync_job)
  
        log.debug("Parsed incoming package - contains ${r?.parsed_rec?.tipps?.size()} tipp records");
      }
    }
    else {
      log.error("Unable to locate config for rectype ${rectype}");
    }


  }



  def packageConv = { md, synctask ->
    println("Package conv...");
    // Convert XML to internal structure ansd return
    def result = [:]
    // result.parsed_rec = xml.text().getBytes();
    result.title = md.gokb.package.name.text()

    result.parsed_rec = [:]
    result.parsed_rec.packageName = md.gokb.package.name.text()
    result.parsed_rec.packageId = md.gokb.package.'@id'.text()
    result.parsed_rec.tipps = []
    int ctr=0
    md.gokb.package.TIPPs.TIPP.each { tip ->
      log.debug("Processing tipp ${ctr++} from package ${result.parsed_rec.packageId} - ${result.title} (source:${synctask.uri})");
      def newtip = [
                     title: [
                       name:tip.title.name.text(), 
                       identifiers:[]
                     ],
                     titleId:tip.title.'@id'.text(),
                     platform:tip.platform.name.text(),
                     platformId:tip.platform.'@id'.text(),
                     coverage:[],
                     url:tip.url.text(),
                     identifiers:[]
                   ];

      tip.coverage.each { cov ->
        newtip.coverage.add([
                       startDate:cov.'@startDate'.text(),
                       endDate:cov.'@endDate'.text(),
                       startVolume:cov.'@startVolume'.text(),
                       endVolume:cov.'@endVolume'.text(),
                       startIssue:cov.'@startIssue'.text(),
                       endIssue:cov.'@endIssue'.text(),
                       coverageDepth:cov.'@coverageDepth'.text(),
                       coverageNote:cov.'@coverageNote'.text(),
                     ]);
      }

      tip.title.identifiers.identifier.each { id ->
        newtip.title.identifiers.add([namespace:id.'@namespace'.text(), value:id.'@value'.text()]);
      }
      newtip.title.identifiers.add([namespace:'uri',value:newtip.titleId]);

      log.debug("Harmonise identifiers");
      harmoniseTitleIdentifiers(newtip);

      result.parsed_rec.tipps.add(newtip)
    }

    result.parsed_rec.tipps.sort{it.titleId}
    println("Rec conversion for package returns object with title ${result.parsed_rec.title} and ${result.parsed_rec.tipps?.size()} tipps");
    return result
  }

  def packageReconcile = { grt ,oldpkg, newpkg ->
    def pkg = null;
    boolean auto_accept_flag = false

    def scope = RefdataCategory.lookupOrCreate(RefdataCategory.PKG_SCOPE,(newpkg?.scope)?:'Unknown');
    def listStatus = RefdataCategory.lookupOrCreate(RefdataCategory.PKG_LIST_STAT,(newpkg?.listStatus)?:'Unknown');
    def breakable = RefdataCategory.lookupOrCreate(RefdataCategory.PKG_BREAKABLE,(newpkg?.breakable)?:'Unknown');
    def consistent = RefdataCategory.lookupOrCreate(RefdataCategory.PKG_CONSISTENT,(newpkg?.consistent)?:'Unknown');
    def fixed = RefdataCategory.lookupOrCreate(RefdataCategory.PKG_FIXED,(newpkg?.fixed)?:'Unknown');
    def paymentType = RefdataCategory.lookupOrCreate('Package.PaymentType',(newpkg?.paymentType)?:'Unknown');
    def global = RefdataCategory.lookupOrCreate('Package.Global',(newpkg?.global)?:'Unknown');
    def isPublic = RefdataCategory.lookupOrCreate('YN','Yes');

    // Firstly, make sure that there is a package for this record
    if ( grt.localOid != null ) {
      pkg = genericOIDService.resolveOID(grt.localOid)
    }
    else {
      // create a new package

      // Auto accept everything whilst we load the package initially
      auto_accept_flag = true;

      pkg = new Package(
                         identifier:grt.identifier,
                         name:grt.name,
                         impId:grt.owner.identifier,
                         autoAccept:false,
                         packageType:null,
                         packageStatus:null,
                         packageListStatus:listStatus,
                         breakable:breakable,
                         consistent:consistent,
                         fixed:fixed,
                         isPublic:isPublic,
                         packageScope:scope
                       )


      if ( pkg.save() ) {
        grt.localOid = "com.k_int.kbplus.Package:${pkg.id}"
        grt.save()
      }
    }

    def onNewTipp = { ctx, tipp, auto_accept ->
      def sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      println("new tipp: ${tipp}");
      println("identifiers: ${tipp.title.identifiers}");

      def title_instance = null; // TitleInstance.lookupOrCreate(tipp.title.identifiers,tipp.title.name)
      println("Result of lookup or create for ${tipp.title.name} with identifiers ${tipp.title.identifiers} is ${title_instance}");

      def plat_instance = Platform.lookupOrCreatePlatform([name:tipp.platform]);
      def tipp_status_str = tipp.status ? tipp.status.capitalize():'Current'
      def tipp_status = RefdataCategory.lookupOrCreate(RefdataCategory.TIPP_STATUS,tipp_status_str);

      if ( auto_accept ) {
        def new_tipp = [:] // new TitleInstancePackagePlatform()
        new_tipp.pkg = ctx;
        new_tipp.platform = plat_instance;
        new_tipp.title = title_instance;
        new_tipp.status = tipp_status;

        // We rely upon there only being 1 coverage statement for now, it seems likely this will need
        // to change in the future.
        // tipp.coverage.each { cov ->
        def cov = tipp.coverage[0]
          new_tipp.startDate=((cov.startDate != null ) && ( cov.startDate.length() > 0 ) ) ? sdf.parse(cov.startDate) : null;
          new_tipp.startVolume=cov.startVolume;
          new_tipp.startIssue=cov.startIssue;
          new_tipp.endDate= ((cov.endDate != null ) && ( cov.endDate.length() > 0 ) ) ? sdf.parse(cov.endDate) : null;
          new_tipp.endVolume=cov.endVolume;
          new_tipp.endIssue=cov.endIssue;
          new_tipp.embargo=cov.embargo;
          new_tipp.coverageDepth=cov.coverageDepth;
          new_tipp.coverageNote=cov.coverageNote;
        // }
        new_tipp.hostPlatformURL=tipp.url;

        // new_tipp.save();
      }
      else {
        println("Register new tipp event for user to accept or reject");

        def cov = tipp.coverage[0]
        def change_doc = [ 
                           pkg:[id:ctx.id],
                           platform:[id:plat_instance.id],
                           title:[id:title_instance.id],
                           status:[id:tipp_status.id],
                           startDate:((cov.startDate != null ) && ( cov.startDate.length() > 0 ) ) ? sdf.parse(cov.startDate) : null,
                           startVolume:cov.startVolume,
                           startIssue:cov.startIssue,
                           endDate:((cov.endDate != null ) && ( cov.endDate.length() > 0 ) ) ? sdf.parse(cov.endDate) : null,
                           endVolume:cov.endVolume,
                           endIssue:cov.endIssue,
                           embargo:cov.embargo,
                           coverageDepth:cov.coverageDepth,
                           coverageNote: cov.coverageNote];

        // changeNotificationService.registerPendingChange('pkg',
        //                                                 ctx,
        //                                                 "New TIPP for ${title_instance.title} from ${plat_instance.name}",
        //                                                  null,
        //                                                 [
        //                                                   newObjectClass:"com.k_int.kbplus.TitleInstancePackagePlatform",
        //                                                   changeType:'New Object',
        //                                                   changeDoc:change_doc
        //                                                 ])

      }
    }

    def onUpdatedTipp = { ctx, tipp, oldtipp, changes, auto_accept ->
      println("updated tipp, ctx = ${ctx.toString()}");

      // Find title with ID tipp... in package ctx
      def title_of_tipp_to_update = null // TitleInstance.lookupOrCreate(tipp.title.identifiers,tipp.title.name)

      def db_tipp = ctx.tipps.find { it.title.id == title_of_tipp_to_update.id }

      if ( db_tipp != null) {
        changes.each { chg ->

          def change_doc = [ 
              startDate:tipp.coverage[0].startDate,
              startVolume:tipp.coverage[0].startVolume,
              startIssue:tipp.coverage[0].startIssue,
              endDate:tipp.coverage[0].endDate,
              endVolume:tipp.coverage[0].endVolume,
              endIssue:tipp.coverage[0].endIssue,
              embargo:tipp.coverage[0].embargo,
              coverageDepth:tipp.coverage[0].coverageDepth,
              coverageNote:tipp.coverage[0].coverageNote,
              // status:null,
              // option:null,
              // delayedOA:null,
              // hybridOA:null,
              // statusReason:null,
              // payment:null,
              hostPlatformURL:tipp.url
          ]

          // changeNotificationService.registerPendingChange('pkg',
          //                                                 ctx,
          //                                                 "A tipp/coverage update for \"${title_of_tipp_to_update.title}\" (Start Date:${tipp.coverage[0].startDate}, Start Volume:${tipp.coverage[0].startVolume}, Start Issue:${tipp.coverage[0].startIssue}, End Date:${tipp.coverage[0].endDate} , End Volume:${tipp.coverage[0].endVolume}, End Issue:${tipp.coverage[0].endIssue}, Embargo:${tipp.coverage[0].embargo}, Coverage Depth:${tipp.coverage[0].coverageDepth}, Coverage Note:${tipp.coverage[0].coverageNote}, url:${tipp.url}",
          //                                                 null,
          //                                                 [
          //                                                   changeTarget:"com.k_int.kbplus.TitleInstancePackagePlatform:${db_tipp.id}",
          //                                                   changeType:'Update Object',
          //                                                   changeDoc:change_doc
          //                                                 ])
        }
      }
      else {
        throw new RuntimeException("Unable to locate TIPP for update. ctx:${ctx}, tipp:${tipp}");
      }
    }

    def onDeletedTipp = { ctx, tipp, auto_accept ->
      println("deletd tipp");
    }

    def onPkgPropChange = { ctx, propname, value, auto_accept ->
      println("updated pkg prop");
    }

    def onTippUnchanged = {ctx, tippa ->
    }

    // com.k_int.kbplus.GokbDiffEngine.diff(pkg, oldpkg, newpkg, onNewTipp, onUpdatedTipp, onDeletedTipp, onPkgPropChange, onTippUnchanged, auto_accept_flag)
  }

  /**
   *  When this system sees a title from a remote source, we need to try and find a common canonical identifier. We will use the
   *  GoKB TitleID for this. Each time a title is seen we make sure that we locally know what the GoKB Title ID is for that remote
   *  record.
   */
   def harmoniseTitleIdentifiers(titleinfo) {
    // println("harmoniseTitleIdentifiers");
    // println("Remote Title ID: ${titleinfo.titleId}");
    // println("Identifiers: ${titleinfo.title.identifiers}");
    def resource_description = [
      title:titleinfo.title.name,
      identifiers:titleinfo.title.identifiers
    ]

    def title_instance = titleLookupService.upsert(resource_description) // TitleInstance.lookupOrCreate(titleinfo.title.identifiers,titleinfo.title.name, true)
  }

  // Main configuration map
  def rectypes = [
    [ name:'Package', converter:packageConv, reconciler:packageReconcile, newRemoteRecordHandler:null, complianceCheck:null ],
    // [ name:'Package', converter:packageConv, reconciler:packageReconcile, newRemoteRecordHandler:null, complianceCheck:testPackageCompliance ],
    // [ name:'Title', converter:titleConv, reconciler:titleReconcile, newRemoteRecordHandler:onNewTitle, complianceCheck:testTitleCompliance ],
  ]


}
