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

        name 'tenantDash' : "/instituion/$institution_shortcode" ( controller:'Institution', action:'index')
        name 'tenantTitles' : "/instituion/$institution_shortcode/titles" ( controller:'DBSearch', action:'index') { srch_cfg='tenant_titles' }
        name 'tenantAgreements' : "/instituion/$institution_shortcode/agreements" ( controller:'Institution', action:'agreements')

        name 'tenantAgreement': "/instituion/$institution_shortcode/agreements/$id" ( controller:'Resource', action:'index' ) {
          cls='com.k_int.yarm.Agreement'
          gsp='tennantAgreement'
        }

        name 'admManageGlobalSources': '/admin/globalSources' ( controller:'DBSearch', action:'index' ) { srch_cfg='adm_global_sources' }
        name 'admEditGlobalSource': "/admin/globalSources/$id" ( controller:'Resource', action:'index' ) { 
          cls='com.k_int.yarm.GlobalRecordSource' 
          gsp='globalRecordSource'
        }

        name 'admManageTenants': '/admin/tenants' ( controller:'DBSearch', action:'index' ) { 
          srch_cfg='adm_tenants' 
          newResourceMapping='admEditTenant'
        }
        name 'admEditTenant': "/admin/tenants/$id" ( controller:'Resource', action:'index' ) { 
          cls='com.k_int.yarm.Tenant' 
          gsp='admTenant'
        }

        name 'kbtitles': '/kb/titles' ( controller:'DBSearch', action:'index' ) { 
          srch_cfg='kb_titles' 
        }
        name 'kbtitle': "/kb/titles/$id" ( controller:'Resource', action:'index' ) {
          cls='com.k_int.yarm.GlobalResource'
          gsp='kbtitle'
        }


        name 'kbpackages': '/kb/packages' ( controller:'DBSearch', action:'index' ) { 
          srch_cfg='kb_packages' 
        }
        name 'kbpackage': "/kb/packages/$id" ( controller:'Resource', action:'index' ) {
          cls='com.k_int.yarm.Package'
          gsp='kbpackage'
        }


        name 'admManageUsers': '/admin/users' ( controller:'DBSearch', action:'index' ) {
          srch_cfg='adm_users'
          newResourceMapping='admEditUser'
        }
      
        "/instituion/$institution_shortcode/agreements/$id/$action" (controller:'Institution')
    }
}
