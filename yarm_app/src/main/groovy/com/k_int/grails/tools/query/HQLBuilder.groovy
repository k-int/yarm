package com.k_int.grails.tools.query

import com.k_int.grails.tools.utils.DomainUtils
import grails.util.GrailsClassUtils
import groovy.util.logging.*

import org.gokb.cred.*

@Slf4j
public class HQLBuilder {

  /**
   *  Accept a qbetemplate of the form
   *  [
   *		baseclass:'Fully.Qualified.Class.Name.To.Search',
   *		title:'Title Of Search',
   *            selectType:'scalar',         // scalar or objects
   *            discriminatorType:'manual',  // manual or type
   *		qbeConfig:[
   *			// For querying over associations and joins, here we will need to set up scopes to be referenced in the qbeForm config
   *			// Until we need them tho, they are omitted. qbeForm entries with no explicit scope are at the root object.
   *			qbeForm:[
   *				[
   *					prompt:'Name or Title',
   *					qparam:'qp_name',
   *					placeholder:'Name or title of item',
   *					contextTree:['ctxtp':'qry', 'comparator' : 'ilike', 'prop':'name']
   *				],
   *				[
   *					prompt:'ID',
   *					qparam:'qp_id',
   *					placeholder:'ID of item',
   *					contextTree:['ctxtp':'qry', 'comparator' : 'eq', 'prop':'id', 'type' : 'java.lang.Long']
   *				],
   *				[
   *					prompt:'SID',
   *					qparam:'qp_sid',
   *					placeholder:'SID for item',
   *					contextTree:['ctxtp':'qry', 'comparator' : 'eq', 'prop':'ids.value']
   *				],
   *			],
   *                    selectList:[
   *                      [ type:'bv', bv:'o.name' ]
   *                    ],
   *			qbeResults:[
   *				[heading:'Type', property:'class.simpleName'],
   *				[heading:'Name/Title', property:'name', link:[controller:'resource',action:'show',id:'x.r.class.name+\':\'+x.r.id'] ]
   *			]
   *		]
   *	]
   *
   *    selectList is an array of expressions that will form the basis of the select clauses
   *    qbeResults is used to form the results array - this may be values from the select list, or it may
   *       be expressions or function calls. Worst case, the object can be serialised and base methods called,
   *       but that should be avoided for performance reasons.
   *
   */
  public static def build(grailsApplication, 
                          qbetemplate,
                          params,
                          genericOIDService) {

    log.debug("build(...${qbetemplate}..)");
    def result = [:]

    if ( qbetemplate ) {
      def baseclass = Class.forName(qbetemplate.baseclass)
      def builder_result = internalBuild(grailsApplication,qbetemplate,params,genericOIDService,qbetemplate.selectType?:'objects',baseclass)
    
    
      result.reccount = baseclass.executeQuery(builder_result.count_hql, builder_result.bindvars)[0]
      result.recset = baseclass.executeQuery(builder_result.hql, 
                                             builder_result.bindvars,
                                             builder_result.query_params);

      log.debug("${builder_result.count_hql} ${result.reccount} ${result.recset.size()}");
    }

    result
  }

  public static def internalBuild(grailsApplication,
                                  qbetemplate,
                                  params,
                                  genericOIDService,
                                  returnObjectsOrRows,
                                  baseclass) {

    log.debug("build ${params}");
    def ibr = [:]

    // Step 1 : Walk through all the properties defined in the template and build a list of criteria
    def criteria = []
    qbetemplate.qbeConfig.qbeForm.each { query_prop_def ->
      if ( ( params[query_prop_def.qparam] != null ) && 
           ( params[query_prop_def.qparam].toString().length() > 0 ) ) {
        criteria.add([defn:query_prop_def, value:params[query_prop_def.qparam]]);
      }
    }

    qbetemplate.qbeConfig.qbeGlobals.each { global_prop_def ->
      log.debug("Adding query global: ${global_prop_def}");
      // create a contextTree so we can process the filter just like something added to the query tree
      // Is this global user selectable
      if ( global_prop_def.qparam != null ) {  // Yes
        if ( params[global_prop_def.qparam] == null ) { // If it's not be set
          if ( global_prop_def.default == 'on' ) { // And the default is set
            log.debug("Adding prop ${global_prop_def.prop} ${global_prop_def.prop.replaceAll('\\.','_')}");
            criteria.add([defn:[ qparam:global_prop_def.prop.replaceAll('\\.','_'), contextTree:global_prop_def],
                          value:interpretGlobalValue(grailsApplication,global_prop_def.value)])
          }
        }
        else if ( params[global_prop_def.qparam] == 'on' ) { // It's set explicitly, if its on, add the criteria
          criteria.add([defn:[qparam:global_prop_def.prop.replaceAll('\\.','_'),contextTree:global_prop_def],value:global_prop_def.value])
        }
      }
      else {
        criteria.add([defn:[qparam:global_prop_def.prop.replaceAll('\\.','_'),contextTree:global_prop_def],value:global_prop_def.value])
      }
    }

    def hql_builder_context = [:]
    hql_builder_context.declared_scopes = [:]
    hql_builder_context.query_clauses = []
    hql_builder_context.bindvars = [:]
    hql_builder_context.genericOIDService = genericOIDService;
    hql_builder_context.sort = params.sort ?: ( qbetemplate.containsKey('defaultSort') ? qbetemplate.defaultSort : null )
    hql_builder_context.order = params.order ?: ( qbetemplate.containsKey('defaultOrder') ? qbetemplate.defaultOrder : null )

    criteria.each { crit ->
      log.debug("Processing crit: ${crit}");
      processProperty(hql_builder_context, hql_builder_context.query_clauses, crit,baseclass)
      // List props = crit.def..split("\\.")
    }

    // log.debug("At end of build, ${hql_builder_context}");
    hql_builder_context.declared_scopes.each { ds ->
      log.debug("Scope: ${ds}");
    }

    hql_builder_context.query_clauses.each { qc ->
      log.debug("QueryClause: ${qc}");
    }

    def hql = outputHql(hql_builder_context, qbetemplate)
    log.debug("HQL: ${hql}");
    log.debug("BindVars: ${hql_builder_context.bindvars}");

    def count_hql = null; //"select count (distinct o) ${hql}"
    if ( qbetemplate.useDistinct == true ) {
      
      // SO: Because of the way Hibernate handles distinct, the distinction may not be carried out by the DBMS.
      // Adding the ID should ensure the distinct happens at the DB end rather than filtered manually by Hibernate.
      count_hql = "select count (distinct o.id) ${hql}"
    }
    else {
      count_hql = "select count (o) ${hql}"
    }

    def fetch_hql = null
    switch ( returnObjectsOrRows ) {
      case 'objects' :
        if ( qbetemplate.useDistinct == true ) {
        
          // SO: Because of the way Hibernate handles distinct, the distinction may not be carried out by the DBMS.
          // Adding the ID should ensure the distinct happens at the DB end rather than filtered manually by Hibernate.
          // fetch_hql = "select ${qbetemplate.useDistinct == true ? 'distinct' : ''} o ${hql}"
          fetch_hql = "select o from ${qbetemplate.baseclass} o where o.id IN (select distinct o.id ${hql})"
        } else {
          fetch_hql = "select o ${hql}"
        }
        break;
      case 'scalar':
        fetch_hql = "select ${buildFieldList(qbetemplate)} ${hql}"
        break;
      default:
        throw new RuntimeException("Unhandled fetch type ${returnObjectsOrRows} - must be scalar or objects");
        break;
    }
    
    log.debug("Consider sort: ${hql_builder_context.sort}");

    // Only add ordering to the fetch query.
    if ( ( hql_builder_context.sort != null ) && ( hql_builder_context.sort.length() > 0 ) ) {
      fetch_hql += (" order by o.${hql_builder_context.sort} ${hql_builder_context.order}");
    }

    log.debug("Attempt count qry ${count_hql}");
    log.debug("Attempt qry ${fetch_hql}");
    log.debug("Params ${hql_builder_context.bindvars}");

    ibr.count_hql = count_hql

    def query_params = [:]
    if ( params.max )
      query_params.max = params.max;
    if ( params.offset )
      query_params.offset = params.offset

    ibr.hql = fetch_hql
    ibr.query_params = query_params
    ibr.bindvars = hql_builder_context.bindvars

    return ibr
  }

  /**
   *  Process the property as it appears on the search form -- EG "The title input box"
   */
  static def processProperty(hql_builder_context, query_clauses, crit,baseclass) {
    processContextTree(hql_builder_context, query_clauses, crit, crit.defn.contextTree, baseclass);
  }


  static def processContextTree(hql_builder_context, query_clauses, crit, contextTree, baseclass) {

    // println("processContextTree(${contextTree.ctxtp})\n");

    switch ( contextTree.ctxtp ) {
      case 'qry':
        processQryContextType(hql_builder_context, query_clauses, crit, contextTree, baseclass)
        break;
      case 'filter':
        processQryContextType(hql_builder_context, query_clauses, crit, contextTree, baseclass)
        break;
      case 'disjunctive':
        // println("Disjunctive case\n");
        processDisjunctive(hql_builder_context, query_clauses, crit, contextTree, baseclass)
        break;
      default:
        log.error("Unhandled property context type ${contextTree}");
        throw new Exception("Unhandled property context type ${contextTree.ctxtp}");
        break;
    }
  }

  static def processDisjunctive(hql_builder_context, query_clauses, crit, contextTree, baseclass) {

    def new_clauses = []

    contextTree.terms.each { c2 ->
      // println("SUBTERM:${c2}\n");
      processContextTree(hql_builder_context, new_clauses, crit, c2, baseclass)
    }

    query_clauses.add(new_clauses);
  }

  static def processQryContextType(hql_builder_context, query_clauses, crit, contextTree, baseclass) {
    List l =  contextTree.prop.split("\\.")
    processQryContextType(hql_builder_context, query_clauses, crit, contextTree, l, 'o', baseclass)
  }

  /**
   *  @param hql_builder_context - context
   *  @param query_clauses - clauses
   *  @param crit - criteria defn
   *  @param proppath 
   *  @param parent_scope
   *  @param the_class
   */ 
  static def processQryContextType(hql_builder_context, 
                                   query_clauses,
                                   crit,  
                                   contextTree,  
                                   proppath,  
                                   parent_scope,  
                                   the_class) {

    // log.debug("processQryContextType.... ${proppath}");

    if ( proppath.size() > 1 ) {
      
      def head = proppath.remove(0)
      def newscope = parent_scope+'_'+head
      if ( hql_builder_context.declared_scopes.containsKey(newscope) ) {
        // Already established scope for this context
        // log.debug("${newscope} already a declared contest");
      }
      else {
        // log.debug("Intermediate establish scope - ${head} :: ${proppath}");          
        // Standard association, just make a bind variable..
        establishScope(hql_builder_context, parent_scope, head, newscope)
      }
      processQryContextType(hql_builder_context, query_clauses, crit, contextTree, proppath, newscope, the_class)
    }
    else {
      // log.debug("Standard property...");
      // The property is a standard property
      addQueryClauseFor(crit, contextTree, hql_builder_context, query_clauses, parent_scope+'.'+proppath[0], the_class)
    }
  }

  static def establishScope(hql_builder_context, parent_scope, property_to_join, newscope_name) {
    // log.debug("Establish scope ${newscope_name} as a child of ${parent_scope} property ${property_to_join}");
    hql_builder_context.declared_scopes[newscope_name] = "${parent_scope}.${property_to_join} as ${newscope_name}" 
  }

  /**
   *  addQueryClauseFor add a query clause for the given criteria to the given query_clauses list, adn add any requred bind variables
   *  @param crit - The criteria to be added
   *  @param hql_builder_context - Parent query builder context
   *  @param query_clauses - The list of query clauses - an entry in this list may itself be a list
   *  @param scoped_property - The actual property, which has been duly scoped eg CONTEXT.scopedProperty
   */
  static def addQueryClauseFor(crit, contextTree, hql_builder_context, query_clauses, scoped_property, the_class) {

    // log.debug("addQueryClauseFor....");

    switch ( contextTree.comparator ) {
      case 'eq':
        if ( crit.defn.type=='lookup' ) {
          query_clauses.add("${contextTree.negate?'not ':''}${scoped_property} = :${crit.defn.qparam}")
          hql_builder_context.bindvars[crit.defn.qparam] = hql_builder_context.genericOIDService.resolveOID2(crit.value)
        }
        else {
          
          def type = contextTree.type
          boolean isDomain = false
          if (!type) {
            // Determine type...
            def conf = DomainUtils.resolveProperty(the_class, contextTree.prop)
            type = conf.type
            isDomain = conf.domain
            log.debug "Determined type of ${contextTree.prop} as ${type}"
          }
          
          Class c = (type as Class)
          
          
          // Grab the simpleName to typeThe parameter to allow for mixed types across disjunctions.
          String simpleType = c.simpleName

          // Test for parser method.
          def value
          String strVal = "${crit.value}".trim()
          if (strVal.respondsTo("to${simpleType}")) {
            
            if (!strVal.respondsTo("is${simpleType}") || strVal."is${simpleType}"()) {
              log.debug "Using to${simpleType} to parse value..."
              value = strVal."to${simpleType}"()
            } else {
              simpleType = null
            }
            
          } else if (crit.value?.respondsTo("to${simpleType}")) {
            if (!strVal.respondsTo("is${simpleType}") || strVal."is${simpleType}"()) {
              log.debug "Using to${simpleType} to convert value..."
              value = crit.value?.trim()?."to${simpleType}"()
            } else {
              simpleType = null
            }
            
          } else {
            if (isDomain) {
              // Use the ID.
              scoped_property += ".id"
              value = "${crit.value}".toLong()
            } else {
              log.debug "Do simple comparison..."
              simpleType = ""
              value = crit.value
            }
          }
          
          // If we have no simple type then we should not include the parameter.
          if (simpleType) {
            query_clauses.add("${contextTree.negate?'not ':''}${scoped_property} = :${simpleType}${crit.defn.qparam}")
            log.debug "Binding val ${value} to param ${crit.defn.qparam}"
            hql_builder_context.bindvars["${simpleType}${crit.defn.qparam}"] = value
          }
        }
        break;

      case 'ilike':
        query_clauses.add("${contextTree.negate?'not ':''}lower(${scoped_property}) like :${crit.defn.qparam}");
        def base_value = crit.value.toLowerCase().trim()
        if ( contextTree.normalise == true ) {
          base_value = base_value
        }
        hql_builder_context.bindvars[crit.defn.qparam] = ( ( contextTree.wildcard=='L' || contextTree.wildcard=='B') ? '%' : '') +
                                                         base_value +
                                                         ( ( contextTree.wildcard=='R' || contextTree.wildcard=='B') ? '%' : '')
        break;

      case 'like':
        query_clauses.add("${contextTree.negate?'not ':''}${scoped_property} like :${crit.defn.qparam}");
        def base_value = crit.value.trim()
        if ( contextTree.normalise == true ) {
          base_value = base_value
        }
        hql_builder_context.bindvars[crit.defn.qparam] = ( ( contextTree.wildcard=='L' || contextTree.wildcard=='B') ? '%' : '') +
                                                         base_value +
                                                         ( ( contextTree.wildcard=='R' || contextTree.wildcard=='B') ? '%' : '')
        break;

      default:
        log.error("Unhandled comparator '${contextTree.comparator}'. crit: ${crit}");
    }
  }

  static def outputHql(hql_builder_context, qbetemplate) {
    StringWriter sw = new StringWriter()
    sw.write(" from ${qbetemplate.baseclass} as o")

    hql_builder_context.declared_scopes.each { scope_name,ds ->
      sw.write(" left outer join ${ds}\n");
    }
    
    if ( hql_builder_context.query_clauses.size() > 0 ) {
      sw.write(" where");
      boolean conjunction=false
      hql_builder_context.query_clauses.each { qc ->
        if ( conjunction ) {
          // output and on second and subsequent clauses
          sw.write(" AND");
        }
        else {  
          conjunction=true
        }
        sw.write(" ");
        if ( ( qc instanceof String ) || ( qc instanceof org.codehaus.groovy.runtime.GStringImpl ) ) {
          sw.write(qc);
        }
        else if ( qc instanceof List ) {
          // println("DISJUNCTIVE");
          def first=true;
          sw.write(" ( ");
          qc.each { nestedqc ->
            if ( first ) { first = false; } else { sw.write(' OR ' ) }
            sw.write(" ( ");
            sw.write(nestedqc);
            sw.write(" ) ");
          }
          sw.write(" ) ");
        }
        else {
          throw new RuntimeException("Unhandled query clause type ${qc?.class?.name} ${qc}");
        }
      }
    }

    // Return the toString of the writer
    sw.toString();
  }

  static def buildFieldList(cfg) {

    def defns = cfg.qbeConfig.selectList

    def result = new java.io.StringWriter()
    result.write('o.id');
    // type(o) only works for polymorphic queries -- thats a real pain in the ass.
    // result.write(',type(o)');
    switch ( cfg.discriminatorType ) {
      case 'manual':
        result.write(',\''+cfg.baseclass+'\'');
        break;
      case 'type':
        result.write(',type(o)');
        break;
      default:
        throw new RuntimeException("Unknown discriminatorType ${cfg.discriminatorType}. Must be manual (for explicit class name) or type (for type(o))");
        break;
    }

    defns.each { defn ->
      result.write(",");
      addExpression(defn, result);
    }
    result.toString();
  }

  static def addExpression(expr, writer) {
    switch ( expr.type ) {
      case 'fn':
        writer.write(expr.name)
        writer.write('(');
        def first=true;
        expr.params.each { p ->
          if ( first) { first=false; } else { writer.write(',') }
          addExpression(p,writer)
        }
        writer.write(')');
        break;
      case 'bv':
        writer.write(expr.bv);
        break;
      default:
        log.warn("Unhandled expression type ${expr.type}");
        break;
    }
  }

  // If a value begins __ then it's a special value and needs to be interpreted, otherwise just return the value
  static def interpretGlobalValue(grailsApplication,value) {
    // log.debug("interpretGlobalValue(ctx,${value})");
    def result=null;
    switch(value?.toString()) {
      case '__USERID':
        def springSecurityService = grailsApplication.getMainContext().getBean('springSecurityService')
        result=''+springSecurityService?.currentUser?.id;
        break;
      default:
        result=value;
        break;
    }
    // log.debug("Returning ${result} ${result.class.name}");
    return result;
  }
}
