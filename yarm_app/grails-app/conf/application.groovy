

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.k_int.yarm.auth.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.k_int.yarm.auth.UserRole'
grails.plugin.springsecurity.authority.className = 'com.k_int.yarm.auth.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/home/index',     access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

reserved_names = [ 'new', 'admin', 'system', 'master' ]

// Configuration for various search tools
srch_cfg = [
  'adm_global_sources':[
    name:'Search Global Sources',
    baseclass:'com.k_int.yarm.GlobalRecordSource',
    title:'Global Sources',
    selectType:'scalar',   // scalar or objects
    discriminatorType:'manual',  // manual or type
    qbeConfig:[
      // For querying over associations and joins, here we will need to set up scopes to be referenced in the qbeForm config
      // Until we need them tho, they are omitted. qbeForm entries with no explicit scope are at the root object.
      qbeForm:[
        [
          prompt:'Name or Title',
          qparam:'qp_name',
          placeholder:'Name or title of item',
          contextTree:['ctxtp':'qry', 'comparator' : 'like', 'prop':'name']
        ]
      ],
      selectList:[
        // ID and class are added by default
        [ type:'bv', bv:'o.name', name:'name' ]
      ],
      enrichments:[  // Things we add to the row after we get the raw values back
        [ name:'lnk', type:'link', label:[prop:'name'], typeProp:'__cls', idProp:'__id', mapping:'admEditGlobalSource' ]
      ],
      qbeResults:[
        // [ heading:'Type', expression:[type:'fn', name:'type', params:[[type:'bv', bv:'o']]], labelKey:'type.name'],
        [ heading:'ID', labelKey:'resource.title',  visible:false, name:"__id" ],
        [ heading:'Class', labelKey:'resource.title',  visible:false, name:"__cls" ],
        [ heading:'Name/Title', labelKey:'resource.title',  visible:true, name:"name" ],
        [ heading:'Link', labelKey:'resource.Link',  visible:true, name:"lnk", type:'link' ]
      ]
    ]
  ],
  'tenant_titles':[
    name:'All Titles',
    baseclass:'com.k_int.yarm.GlobalResource',
    title:'All Titles',
    selectType:'scalar',
    discriminatorType:'manual',
    qbeConfig:[
      // For querying over associations and joins, here we will need to set up scopes to be referenced in the qbeForm config
      // Until we need them tho, they are omitted. qbeForm entries with no explicit scope are at the root object.
      qbeForm:[
        [
          prompt:'Name or Title',
          qparam:'qp_name',
          placeholder:'Name or title of item',
          contextTree:['ctxtp':'qry', 'comparator' : 'like', 'prop':'name']
        ]
      ],
      selectList:[
        [ type:'bv', bv:'o.name', name:'name' ]
      ],
      enrichments:[
        [ name:'lnk', type:'link', label:[prop:'name'], typeProp:'__cls', idProp:'__id', mapping:'admEditGlobalSource' ]
      ],
      qbeResults:[
        [ heading:'ID', labelKey:'resource.title',  visible:false, name:"__id" ],
        [ heading:'Class', labelKey:'resource.title',  visible:false, name:"__cls" ],
        [ heading:'Name/Title', labelKey:'resource.title',  visible:true, name:"name" ],
        [ heading:'Link', labelKey:'resource.Link',  visible:true, name:"lnk", type:'link' ]
      ]
    ]
  ],
  'adm_tenants':[
    name:'Tenants',
    baseclass:'com.k_int.yarm.Tenant',
    title:'All Tenants',
    selectType:'scalar',
    discriminatorType:'manual',
    qbeConfig:[
      // For querying over associations and joins, here we will need to set up scopes to be referenced in the qbeForm config
      // Until we need them tho, they are omitted. qbeForm entries with no explicit scope are at the root object.
      qbeForm:[
        [
          prompt:'Name or Title',
          qparam:'qp_name',
          placeholder:'Name or title of item',
          contextTree:['ctxtp':'qry', 'comparator' : 'like', 'prop':'uriName']
        ]
      ],
      selectList:[
        [ type:'bv', bv:'o.uriName', name:'uriName' ],
        [ type:'bv', bv:'o.displayName', name:'displayName' ]
      ],
      enrichments:[
        [ name:'lnk', type:'link', label:[prop:'uriName'], typeProp:'__cls', idProp:'__id', mapping:'admEditTenant' ]
      ],
      qbeResults:[
        [ heading:'ID', labelKey:'resource.ID',  visible:false, name:"__id" ],
        [ heading:'Class', labelKey:'resource.Class',  visible:false, name:"__cls" ],
        [ heading:'Display Name', labelKey:'resource.displayName',  visible:true, name:"displayName" ],
        [ heading:'URI', labelKey:'resource.uriName',  visible:true, name:"lnk", type:'link' ]
      ]
    ]
  ],
  'adm_users':[
    name:'User',
    baseclass:'com.k_int.yarm.auth.User',
    title:'Users',
    selectType:'scalar',
    discriminatorType:'manual',
    qbeConfig:[
      // For querying over associations and joins, here we will need to set up scopes to be referenced in the qbeForm config
      // Until we need them tho, they are omitted. qbeForm entries with no explicit scope are at the root object.
      qbeForm:[
        [
          prompt:'Name or Title',
          qparam:'qp_name',
          placeholder:'Name or title of item',
          contextTree:['ctxtp':'qry', 'comparator' : 'like', 'prop':'uriName']
        ],
        [
          prompt:'Related Party',
          qparam:'qp_relparty',
          placeholder:'Related Party',
          contextTree:[ 'ctxtp':'qry', 'comparator' : 'eq', 'prop':'outgoingRelations.to.id'],
          hide:true
        ]
      ],
      selectList:[
        [ type:'bv', bv:'o.username', name:'username' ],
        [ type:'bv', bv:'o.displayName', name:'displayName' ]
      ],
      enrichments:[
        [ name:'lnk', type:'link', label:[prop:'username'], typeProp:'__cls', idProp:'__id', mapping:'admEditUser' ]
      ],
      qbeResults:[
        [ heading:'ID', labelKey:'resource.ID',  visible:false, name:"__id" ],
        [ heading:'Class', labelKey:'resource.Class',  visible:false, name:"__cls" ],
        [ heading:'Display Name', labelKey:'resource.displayName',  visible:true, name:"displayName" ],
        [ heading:'Username', labelKey:'resource.username',  visible:true, name:"lnk", type:'link' ]
      ]
    ]
  ],
  'tenant_agreements':[
    name:'Agreements',
    baseclass:'com.k_int.yarm.Agreement',
    title:'Agreements',
    selectType:'scalar',
    discriminatorType:'manual',
    qbeConfig:[
      // For querying over associations and joins, here we will need to set up scopes to be referenced in the qbeForm config
      // Until we need them tho, they are omitted. qbeForm entries with no explicit scope are at the root object.
      qbeForm:[
        [
          prompt:'Name or Title',
          qparam:'qp_name',
          placeholder:'Name or title of item',
          contextTree:['ctxtp':'qry', 'comparator' : 'like', 'prop':'name']
        ],
        [
          prompt:'Owner',
          qparam:'qp_owner',
          placeholder:'Owner',
          contextTree:[ 'ctxtp':'qry', 'comparator' : 'eq', 'prop':'owner.id'],
          hide:true
        ]
      ],
      selectList:[
        [ type:'bv', bv:'o.name', name:'name' ],
        [ type:'bv', bv:'o.owner.id', name:'ownerId' ],
        [ type:'bv', bv:'o.owner.uriName', name:'institution_shortcode' ]
      ],
      enrichments:[
        [ name:'lnk', type:'link', label:[prop:'name'], typeProp:'__cls', idProp:'__id', mapping:'tenantAgreement', params:['institution_shortcode'] ]
      ],
      qbeResults:[
        [ heading:'ID', labelKey:'resource.ID',  visible:false, name:"__id" ],
        [ heading:'Class', labelKey:'resource.Class',  visible:false, name:"__cls" ],
        [ heading:'Agreement Name', labelKey:'resource.name',  visible:true, name:"lnk", type:'link' ],
        [ heading:'Owner', labelKey:'resource.ownerUriName',  visible:true, name:"institution_shortcode" ]
      ]
    ]
  ]
]
