package yarm_app

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:'home',action:'index')
        "/directory/org/$id/dashboard"(controller:'directory',action:'orgDash')
        "500"(view:'/error')
        "404"(view:'/notFound')

        name 'tenantTitles' : "/instituion/$institution_shortcode/titles" ( controller:'DBSearch', action:'index') { srch_cfg='tenant_titles' }

        name 'admManageGlobalSources': '/admin/globalSources' ( controller:'DBSearch', action:'index' ) { srch_cfg='adm_global_sources' }
        name 'admEditGlobalSource': "/admin/globalSources/$id" ( controller:'Resource', action:'index' ) { 
          cls='com.k_int.yarm.GlobalRecordSource' 
          mapping='admEditGlobalSource'
          gsonTemplate='globalRecordSource'
          jsonSchema='GlobalRecordSource'
          layout='GlobalRecordSource'
        }
    }
}
