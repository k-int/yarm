

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
        [ type:'bv', bv:'o.name' ]
      ],
      qbeResults:[
        // [ heading:'Type', expression:[type:'fn', name:'type', params:[[type:'bv', bv:'o']]], labelKey:'type.name'],
        [ heading:'Name/Title', property:'name', link:[controller:'resource',action:'show',id:'x.r.class.name+\':\'+x.r.id'], labelKey:'resource.title' ]
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
        [ type:'bv', bv:'o.name' ]
      ],
      qbeResults:[
        // [ heading:'Type', expression:[type:'fn', name:'type', params:[[type:'bv', bv:'o']]], labelKey:'type.name'],
        [ heading:'Name/Title', property:'name', link:[controller:'resource',action:'show',id:'x.r.class.name+\':\'+x.r.id'],labelKey:'resource.title' ]
      ]
    ]

  ]
]
