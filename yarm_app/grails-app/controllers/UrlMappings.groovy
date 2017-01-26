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
    }
}
