package com.k_int.yarm

import com.k_int.goai.OaiClient
import java.text.SimpleDateFormat
import org.springframework.transaction.annotation.*

/*
 *  Implementing new rectypes..
 *  the reconciler closure is responsible for reconciling the previous version of a record and the latest version
 *  the converter is respnsible for creating the map strucuture passed to the reconciler. It needs to return a [:] sorted appropriate
 *  to the work the reconciler will need to do (Often this includes sorting lists)
 */

class GlobalSourceSyncService {


  public static boolean running = false;
  def genericOIDService
  def executorService
  boolean parallel_jobs = false

  def titleReconcile = { grt ,oldtitle, newtitle ->
    log.debug("Reconcile grt: ${grt} oldtitle:${oldtitle} newtitle:${newtitle}");

    // Does the remote title have a publisher (And is ours blank)
    def title_instance = genericOIDService.resolveOID(grt.localOid)

    if ( title_instance == null ) {
      log.debug("Failed to resolve ${grt.localOid} - Exiting");
      return
    }
    
    newtitle.identifiers.each {
      log.debug("Checking title has ${it.namespace}:${it.value}");
      title_instance.checkAndAddMissingIdentifier(it.namespace, it.value);
    }

    if ( ( newtitle.publisher != null ) && ( title_instance.getPublisher() == null ) ) {
      def publisher_identifiers = []
      def publisher = Org.lookupOrCreate(newtitle.publisher, 'publisher', null, publisher_identifiers, null)
      def pub_role = RefdataCategory.lookupOrCreate('Organisational Role', 'Publisher');
      log.debug("Asserting ${publisher} ${title_instance} ${pub_role}");
      OrgRole.assertOrgTitleLink(publisher, title_instance, pub_role)
    }

    // Title history!!
    newtitle.history.each { historyEvent ->
      log.debug("Processing title history event");
      // See if we already have a reference
      def fromset = []
      def toset = []

      historyEvent.from.each { he ->
        def participant = GlobalResource.lookupOrCreate(he.ids,he.title)
        fromset.add(participant)
      }

      historyEvent.to.each { he ->
        def participant = GlobalResource.lookupOrCreate(he.ids,he.title)
        toset.add(participant)
      }

      // Now - See if we can find a title history event for data and these particiapnts.
      // Title History Events are IMMUTABLE - so we delete them rather than updating them.
      def base_query = "select the from TitleHistoryEvent as the where the.eventDate = ? "
      // Need to parse date...
      def sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      def query_params = [(((historyEvent.date != null ) && ( historyEvent.date.trim().length() > 0 ) ) ? sdf.parse(historyEvent.date) : null)]

      fromset.each {
        base_query += "and exists ( select p from the.participants as p where p.participant = ? and p.participantRole = 'from' ) "
        query_params.add(it)
      }
      toset.each {
        base_query += "and exists ( select p from the.participants as p where p.participant = ? and p.participantRole = 'to' ) "
        query_params.add(it)
      }

      def existing_title_history_event = TitleHistoryEvent.executeQuery(base_query,query_params);
      log.debug("Result of title history event lookup : ${existing_title_history_event}");

      if ( existing_title_history_event.size() == 0  ) {
        log.debug("Create new history event");
        def he = new TitleHistoryEvent(eventDate:query_params[0]).save(flush:true)
        fromset.each {
          new TitleHistoryEventParticipant(event:he, participant:it, participantRole:'from').save(flush:true)
        }
        toset.each {
          new TitleHistoryEventParticipant(event:he, participant:it, participantRole:'to').save(flush:true)
        }
      }
    }
  }

  def titleConv = { md, synctask ->
    log.debug("titleConv.... ${md}");
    def result = [:]
    result.parsed_rec = [:]
    result.parsed_rec.identifiers = []
    result.parsed_rec.history = []

    result.title = md.gokb.title.name.text()
    result.parsed_rec.title = md.gokb.title.name.text()
    result.parsed_rec.publisher = md.gokb.title.publisher?.name?.text()

    md.gokb.title.identifiers.identifier.each { id ->
      result.parsed_rec.identifiers.add([namespace:id.'@namespace'.text(), value:id.'@value'.text()])
    }

    md.gokb.title.history?.historyEvent.each { he ->
      def history_statement = [:]
      history_statement.internalId = he.'@id'.text()
      history_statement.date = he.date.text()
      history_statement.from = []
      history_statement.to = []

      he.from.each { hef ->
        def new_history_statement = [:]
        new_history_statement.title=hef.title.text()
        new_history_statement.ids = []
        hef.identifiers.identifier.each { i ->
          new_history_statement.ids.add([namespace:i.'@namespace'.text(), value:i.'@value'.text()])
        }
        history_statement.from.add(new_history_statement);
      }

      he.to.each { het ->
        def new_history_statement = [:]
        new_history_statement.title=het.title.text()
        new_history_statement.ids = []
        het.identifiers.identifier.each { i ->
          new_history_statement.ids.add([namespace:i.'@namespace'.text(), value:i.'@value'.text()])
        }
        history_statement.to.add(new_history_statement);
      }

      result.parsed_rec.history.add(history_statement)
    }

    log.debug(result);
    result
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
        grt.localOid = "com.k_int.yarm.Package:${pkg.id}"
        grt.save()
      }
    }

    def onNewTipp = { ctx, tipp, auto_accept ->
      def sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      println("new tipp: ${tipp}");
      println("identifiers: ${tipp.title.identifiers}");

      def title_instance = GlobalResource.lookupOrCreate(tipp.title.identifiers,tipp.title.name)
      println("Result of lookup or create for ${tipp.title.name} with identifiers ${tipp.title.identifiers} is ${title_instance}");

      def plat_instance = Platform.lookupOrCreatePlatform([name:tipp.platform]);
      def tipp_status_str = tipp.status ? tipp.status.capitalize():'Current'
      def tipp_status = RefdataCategory.lookupOrCreate(RefdataCategory.TIPP_STATUS,tipp_status_str);

        def new_tipp = new Grpp()
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

        new_tipp.save();
    }

    def onUpdatedTipp = { ctx, tipp, oldtipp, changes, auto_accept ->
      println("updated tipp, ctx = ${ctx.toString()}");

      // Find title with ID tipp... in package ctx
      def title_of_tipp_to_update = GlobalResource.lookupOrCreate(tipp.title.identifiers,tipp.title.name)

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

    com.k_int.yarm.GokbDiffEngine.diff(pkg, oldpkg, newpkg, onNewTipp, onUpdatedTipp, onDeletedTipp, onPkgPropChange, onTippUnchanged, auto_accept_flag)
  }

  def testTitleCompliance = { json_record ->
    log.debug("testTitleCompliance:: ${json_record}");
    
    def result = RefdataCategory.lookupOrCreate("YNO","No")

    if ( json_record.identifiers?.size() > 0 ) {
      result = RefdataCategory.lookupOrCreate("YNO","Yes")
    }

    result
  }

  // def testKBPlusCompliance = { json_record ->
  def testPackageCompliance = { json_record ->
    // Iterate through all titles..
    def error = false
    def result = null
    def problem_titles = []

    log.debug(json_record.packageName);
    log.debug(json_record.packageId);

    // GOkb records containing titles with no identifiers are not valid in KB+ land
    json_record?.tipps.each { tipp ->
      log.debug(tipp.title.name);
      // tipp.title.identifiers
      if ( tipp.title?.identifiers?.size() > 0 ) {
        // No problem
      }
      else {
        problem_titles.add(tipp.title.titleId)
        error = true
      }

      // tipp.titleid
      // tipp.platform
      // tipp.platformId
      // tipp.coverage
      // tipp.url
      // tipp.identifiers
    }

    if ( error ) {
      result = RefdataCategory.lookupOrCreate("YNO","No")
    }
    else {
      result = RefdataCategory.lookupOrCreate("YNO","Yes")
    }
    
    result
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


  // We always match a remote title against a local one, or create a local one to mirror the remote
  // definition. Having created the remote title, we synchronize the other details (Title History for example)
  // using the standard reconciler with the new info and null as the old info - essentially a full update the first time.
  def onNewTitle = { global_record_info, newtitle ->

    log.debug("onNewTitle.... ${global_record_info} ${newtitle} ");

    // We need to create a new global record tracker. If there is already a local title for this remote title, link to it,
    // otherwise create a new title and link to it. See if we can locate a title.
    def title_instance = GlobalResource.lookupOrCreate(newtitle.identifiers,newtitle.title)

    if ( title_instance != null ) {

      title_instance.refresh()

      // merge in any new identifiers we have
      newtitle.identifiers.each {
        log.debug("Checking title has ${it.namespace}:${it.value}");
        title_instance.checkAndAddMissingIdentifier(it.namespace, it.value);
      }


      log.debug("Creating new global record tracker... for title ${title_instance}");


      def grt = new GlobalRecordTracker(
        owner:global_record_info,
        localOid:title_instance.class.name+':'+title_instance.id,
        identifier:java.util.UUID.randomUUID().toString(),
        name:newtitle.title
      ).save(flush:true)

      log.debug("call title reconcile");
      titleReconcile(grt, null, newtitle)
    }
    else {
      log.error("Unable to lookup or create title... ids:${newtitle.identifiers}, title:${newtitle.title}");
    }
  }


  // Main configuration map
  def rectypes = [
    [ name:'Package', converter:packageConv, reconciler:packageReconcile, newRemoteRecordHandler:null, complianceCheck:testPackageCompliance ],
    [ name:'Title', converter:titleConv, reconciler:titleReconcile, newRemoteRecordHandler:onNewTitle, complianceCheck:testTitleCompliance ],
  ]

  def runAllActiveSyncTasks() {

    if ( running == false ) {
      def future = executorService.submit({ internalRunAllActiveSyncTasks() } as java.util.concurrent.Callable)
    }
    else {
      log.warn("Not starting duplicate OAI thread");
    }
  }

  def internalRunAllActiveSyncTasks() {

      running = true;

     def jobs = GlobalRecordSource.findAll() 

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
       switch ( sync_job.type ) {
         case 'OAI':
           log.debug("start internal sync");
           this.doOAISync(sync_job)
           log.debug("this.doOAISync has returned...");
           break;
         default:
           log.error("Unhandled sync job type: ${sync_job.type}");
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
 
  /**
   * This is the main worker method for this service - take a sync job definition and reconcile the data found
   * with the local database.
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  def intOAI(sync_job_id) {

    log.debug("internalOAI processing ${sync_job_id}");

    def sync_job = GlobalRecordSource.get(sync_job_id)
    int rectype = sync_job.rectype.longValue()
    def cfg = rectypes[rectype]

    try {
  
      log.debug("Rectype: ${rectype} == config ${cfg}");
  
        log.debug("internalOAISync records from [job ${sync_job_id}] ${sync_job.uri} since ${sync_job.haveUpTo} using ${sync_job.fullPrefix}");
  
        if ( cfg == null ) {
          throw new RuntimeException("Unable to resolve config for ID ${sync_job.rectype}");
        }
  
        def sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  
        def date = sync_job.haveUpTo
  
        log.debug("upto: ${date} uri:${sync_job.uri} prefix:${sync_job.fullPrefix}");
  
        def oai_client = new OaiClient(host:sync_job.uri)
        def max_timestamp = 0
  
        log.debug("Collect ${cfg.name} changes since ${date}");
  
        oai_client.getChangesSince(date, sync_job.fullPrefix) { rec ->
  
          log.debug("Got OAI Record ${rec.header.identifier} datestamp: ${rec.header.datestamp} job:${sync_job.id} url:${sync_job.uri} cfg:${cfg.name}")
  
          def qryparams = [sync_job.id, rec.header.identifier.text()]
          def record_timestamp = sdf.parse(rec.header.datestamp.text())
          def existing_record_info = GlobalRecordInfo.executeQuery('select r from GlobalRecordInfo as r where r.source.id = ? and r.identifier = ?',qryparams);
          if ( existing_record_info.size() == 1 ) {
            log.debug("convert xml into json - config is ${cfg} ");
            def parsed_rec = cfg.converter.call(rec.metadata, sync_job)
  
            // Deserialize
            def bais = new ByteArrayInputStream((byte[])(existing_record_info[0].record))
            def ins = new ObjectInputStream(bais);
            def old_rec_info = ins.readObject()
            ins.close()
            def new_record_info = parsed_rec.parsed_rec
  
            // For each tracker we need to update the local object which reflects that remote record
            existing_record_info[0].trackers.each { tracker ->
              cfg.reconciler.call(tracker, old_rec_info, new_record_info)
            }
  
            log.debug("Calling compliance check, cfg name is ${cfg.name}");
            existing_record_info[0].yarmCompliant = cfg.complianceCheck.call(parsed_rec.parsed_rec)
            log.debug("Result of compliance check: ${existing_record_info[0].yarmCompliant}");
  
            // Finally, update our local copy of the remote object
            def baos = new ByteArrayOutputStream()
            def out= new ObjectOutputStream(baos)
            out.writeObject(new_record_info)
            out.close()
            existing_record_info[0].record = baos.toByteArray();
            existing_record_info[0].desc="Package ${parsed_rec.title} consisting of ${parsed_rec.parsed_rec.tipps?.size()} titles"
            existing_record_info[0].save()
          }
          else {
            log.debug("First time we have seen this record - converting ${cfg.name}");
            def parsed_rec = cfg.converter.call(rec.metadata, sync_job)
            log.debug("Converter thinks this rec has title :: ${parsed_rec.title}");
  
            // Evaluate the incoming record to see if it meets KB+ stringent data quality standards
            log.debug("Calling compliance check, cfg name is ${cfg.name}");
            def yarm_compliant = cfg.complianceCheck.call(parsed_rec.parsed_rec) // RefdataCategory.lookupOrCreate("YNO","No")
            log.debug("Result of compliance [new] check: ${yarm_compliant}");
  
            def baos = new ByteArrayOutputStream()
            def out= new ObjectOutputStream(baos)
            log.debug("write object ${parsed_rec.parsed_rec}");
            out.writeObject(parsed_rec.parsed_rec)

            log.debug("written, closed...");

            out.close()
  
            log.debug("Create new GlobalRecordInfo");

            // Because we don't know about this record, we can't possibly be already tracking it. Just create a local tracking record.
            existing_record_info = new GlobalRecordInfo(
                                                        ts:record_timestamp,
                                                        name:parsed_rec.title,
                                                        identifier:rec.header.identifier.text(),
                                                        desc:"${parsed_rec.title}",
                                                        source: sync_job,
                                                        rectype:sync_job.rectype,
                                                        record: baos.toByteArray(),
                                                        yarmCompliant: yarm_compliant);
  
            if ( existing_record_info.save(flush:true) ) {
              log.debug("existing_record_info created ok");
            }
            else {
              log.error("Problem saving record info: ${existing_record_info.errors}");
            }

            if ( yarm_compliant?.value == 'Yes' ) {
              if ( cfg.newRemoteRecordHandler != null ) {
                log.debug("Calling new remote record handler...");
                cfg.newRemoteRecordHandler.call(existing_record_info, parsed_rec.parsed_rec)
                log.debug("Call completed");
              }
              else {
                log.debug("No new record handler");
              }
            }
            else {
              log.debug("Skip record - not KBPlus compliant");
            }
          }
  
          if ( record_timestamp.getTime() > max_timestamp ) {
            max_timestamp = record_timestamp.getTime()
            log.debug("Max timestamp is now ${record_timestamp}");
          }
  
          log.debug("Updating sync job max timestamp");
          sync_job.haveUpTo=new Date(max_timestamp)
          sync_job.save(flush:true);
        }
    }
    catch ( Exception e ) {
      log.error("Problem",e);
      log.error("Problem running job ${sync_job_id}, conf=${cfg}",e);
    }
    finally {
      log.debug("internalOAISync completed for job ${sync_job_id}");
    }
  }

  def parseDate(datestr, possible_formats) {
    def parsed_date = null;
    if ( datestr && ( datestr.toString().trim().length() > 0 ) ) {
      for(Iterator i = possible_formats.iterator(); ( i.hasNext() && ( parsed_date == null ) ); ) {
        try {
          parsed_date = i.next().parse(datestr.toString());
        }
        catch ( Exception e ) {
        }
      }
    }
    parsed_date
  }

  def dumpPkgRec(pr) {
    log.debug(pr);
  }

  def initialiseTracker(grt) {
    int rectype = grt.owner.rectype.longValue()
    def cfg = rectypes[rectype]

    def oldrec = [:]
    oldrec.tipps=[]
    def bais = new ByteArrayInputStream((byte[])(grt.owner.record))
    def ins = new ObjectInputStream(bais);
    def newrec = ins.readObject()
    ins.close()

    cfg.reconciler.call(grt,oldrec,newrec)
  }

  def initialiseTracker(grt, localPkgOID) {
    int rectype = grt.owner.rectype.longValue()
    def cfg = rectypes[rectype]
    def localPkg = genericOIDService.resolveOID(localPkgOID)

    def oldrec = localPkg.toComparablePackage()

    def bais = new ByteArrayInputStream((byte[])(grt.owner.record))
    def ins = new ObjectInputStream(bais);
    def newrec = ins.readObject()
    ins.close()

    cfg.reconciler.call(grt,oldrec,newrec)
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
    def title_instance = GlobalResource.lookupOrCreate(titleinfo.title.identifiers,titleinfo.title.name, true)
  }

  def diff(localPackage, globalRecordInfo) {

    def result = []

    def oldpkg = localPackage ? localPackage.toComparablePackage() : [tipps:[]];

    def bais = new ByteArrayInputStream((byte[])(globalRecordInfo.record))
    def ins = new ObjectInputStream(bais);
    def newpkg = ins.readObject()
    ins.close()

    def onNewTipp = { ctx, tipp, auto_accept -> ctx.add([tipp:tipp, action:'i']); }
    def onUpdatedTipp = { ctx, tipp, oldtipp, changes, auto_accept -> ctx.add([tipp:tipp, action:'u', changes:changes, oldtipp:oldtipp]); }
    def onDeletedTipp = { ctx, tipp, auto_accept  -> ctx.add([oldtipp:tipp, action:'d']); }
    def onPkgPropChange = { ctx, propname, value, auto_accept -> null; }
    def onTippUnchanged = { ctx, tipp -> ctx.add([tipp:tipp, action:'-']);  }

    com.k_int.yarm.GokbDiffEngine.diff(result, oldpkg, newpkg, onNewTipp, onUpdatedTipp, onDeletedTipp, onPkgPropChange, onTippUnchanged, false)

    return result
  }

}
