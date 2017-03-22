package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON


class AjaxSupportController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def lookup() {
    log.debug("AjaxController::lookup ${params}");
    def result = [:]
    params.max = params.max ?: 10;
    def domain_class = grailsApplication.getArtefact('Domain',params.baseClass)
    if ( domain_class ) {
      result.values = domain_class.getClazz().refdataFind(params);
    }
    else {
      log.error("Unable to locate domain class ${params.baseClass}");
      result.values=[]
    }

    if ( params.addEmpty=='Y' || params.addEmpty=='y' ) {
      result.values.add(0, [id:'', text:'']);
    }

    render result as JSON
  }


  /**
   *  addToCollection : Used to create a form which will add a new object to a named collection within the target object.
   * @param __context : the OID ("<FullyQualifiedClassName>:<PrimaryKey>") Of the context object
   * @param __newObjectClass : The fully qualified class name of the instance to create
   * @param __recip : Optional - If set, then new_object.recip will point to __context
   * @param __addToColl : The name of the local set to which the new object should be added
   * @param All other parameters are taken to be property names on newObjectClass and used to init the new instance.
   */
  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def addToCollection() {
    log.debug("AjaxController::addToCollection ${params}");

    def contextObj = resolveOID2(params.__context)
    def domain_class = grailsApplication.getArtefact('Domain',params.__newObjectClass)

    if ( domain_class ) {

      if ( contextObj ) {
        log.debug("Create a new instance of ${params.__newObjectClass}");

        def new_obj = domain_class.getClazz().newInstance();

        domain_class.getPersistentProperties().each { p -> // list of GrailsDomainClassProperty
          log.debug("${p.name} (assoc=${p.isAssociation()}) (oneToMany=${p.isOneToMany()}) (ManyToOne=${p.isManyToOne()}) (OneToOne=${p.isOneToOne()})");
          if ( params[p.name] ) {
            if ( p.isAssociation() ) {
              if ( p.isManyToOne() || p.isOneToOne() ) {
                // Set ref property
                log.debug("set assoc ${p.name} to lookup of OID ${params[p.name]}");
                // if ( key == __new__ then we need to create a new instance )
                new_obj[p.name] = resolveOID2(params[p.name])
              }
              else {
                // Add to collection
                log.debug("add to collection ${p.name} for OID ${params[p.name]}");
                new_obj[p.name].add(resolveOID2(params[p.name]))
              }
            }
            else {
              switch ( p.type ) {
                case Long.class:
                  log.debug("Set simple prop ${p.name} = ${params[p.name]} (as long=${Long.parseLong(params[p.name])})");
                  new_obj[p.name] = Long.parseLong(params[p.name]);
                  break;
                default:
                  log.debug("Set simple prop ${p.name} = ${params[p.name]}");
                  new_obj[p.name] = params[p.name]
                  break;
              }
            }
          }
        }

        if (params.__refdataName && params.__refdataValue) {
          log.debug("set refdata "+ params.__refdataName +" for component ${contextObj}")
          def refdata = resolveOID2(params.__refdataValue)
          new_obj[params.__refdataName] = refdata
        }

        // Need to do the right thing depending on who owns the relationship. If new obj
        // BelongsTo other, should be added to recip collection.
        if ( params.__recip ) {
          log.debug("Set reciprocal property ${params.__recip} to ${contextObj}");
          new_obj[params.__recip] = contextObj
          log.debug("Saving ${new_obj}");
          if ( new_obj.save() ) {
            log.debug("Saved OK");
          }
          else {
            new_obj.errors.each { e ->
              log.debug("Problem ${e}");
            }
          }
        }
        else if ( params.__addToColl ) {
          contextObj[params.__addToColl].add(new_obj)
          log.debug("Saving ${new_obj}");

          if ( new_obj.save() ) {
            log.debug("Saved OK");
          }
          else {
            new_obj.errors.each { e ->
              log.debug("Problem ${e}");
            }
          }

          if ( contextObj.save() ) {
            log.debug("Saved OK");
          }
          else {
            contextObj.errors.each { e ->
              log.debug("Problem ${e}");
            }
          }
        }
        else {
          // Stand alone object.. Save it!
          log.debug("Saving stand along reference object");
          if ( new_obj.save() ) {
            log.debug("Saved OK");
          }
          else {
            new_obj.errors.each { e ->
              log.debug("Problem ${e}");
            }
          }
        }

        // Special combo processing
        if ( ( new_obj != null ) &&
             ( new_obj.hasProperty('hasByCombo') ) && ( new_obj.hasByCombo != null ) ) {
          log.debug("Processing hasByCombo properties...${new_obj.hasByCombo}");
          new_obj.hasByCombo.keySet().each { hbc ->
            log.debug("Testing ${hbc} -> ${params[hbc]}");
            if ( params[hbc] ) {
              log.debug("Setting ${hbc} to ${params[hbc]}");
              new_obj[hbc] = resolveOID2(params[hbc])
            }
          }
          new_obj.save()
        }
      }
      else {
        log.debug("Unable to locate instance of context class with oid ${params.__context}");
      }
    }
    else {
      log.error("Unable to ookup domain class ${params.__newObjectClass}");
    }

    redirect(url: request.getHeader('referer'))
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def addToStdCollection() {
    log.debug("addToStdCollection(${params})");
    // Adds a link to a collection that is not mapped through a join object
    def contextObj = resolveOID2(params.__context)
    if ( contextObj ) {
      contextObj["${params.__property}"].add (resolveOID2(params.__relatedObject))
      contextObj.save(flush:true, failOnError:true)
      log.debug("Saved: ${contextObj.id}");
    }
    redirect(url: request.getHeader('referer'))
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def unlinkManyToMany() {
    log.debug("unlinkManyToMany(${params})");
    // Adds a link to a collection that is not mapped through a join object
    def contextObj = resolveOID2(params.__context)
    if ( contextObj ) {
      def item_to_remove = resolveOID2(params.__itemToRemove)
      if ( item_to_remove ) {
        if ( ( item_to_remove != null ) && ( item_to_remove.hasProperty('hasByCombo') ) && ( item_to_remove.hasByCombo != null ) ) {
          item_to_remove.hasByCombo.keySet().each { hbc ->
            log.debug("Testing ${hbc}");
            log.debug("here's the data: "+ item_to_remove[hbc])
            if (item_to_remove[hbc]==contextObj) {
              log.debug("context found");
              //item_to_remove[hbc]=resolveOID2(null)
              item_to_remove.deleteParent();
              log.debug(item_to_remove.children)
              log.debug(item_to_remove.heading)
              log.debug(item_to_remove.parent)
              log.debug("tried removal: "+item_to_remove[hbc]);
            }
          }
        }
        log.debug(params);
        log.debug("removing: "+item_to_remove+" from "+params.__property+" for "+contextObj);

            contextObj[params.__property].remove(item_to_remove);

                log.debug("child removed: "+ contextObj[params.__property]);
                if (contextObj.save()==false) {
                  log.debug(contextObj.errors.allErrors())
                } else {
                  log.debug("saved ok");
                }
                item_to_remove.refresh();
                if (params.__otherEnd && item_to_remove[params.__otherEnd]!=null) {
                  log.debug("remove parent: "+item_to_remove[params.__otherEnd])
                  //item_to_remove.setParent(null);
                  item_to_remove[params.__otherEnd]=null; //this seems to fail
                  log.debug("parent removed: "+item_to_remove[params.__otherEnd]);
                }
                if (item_to_remove.save()==false) {
                 log.debug(item_to_remove.errors.allError());
                }
      } else {
        log.error("Unable to resolve item to remove : ${params.__itemToRemove}");
      }
    } else {
      log.error("Unable to resolve context obj : ${params.__context}");
    }
    redirect(url: request.getHeader('referer'))
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def delete() {
    log.debug("delete(${params}), referer: ${request.getHeader('referer')}");
    // Adds a link to a collection that is not mapped through a join object
    def contextObj = resolveOID2(params.__context)
    if ( contextObj ) {
      contextObj.delete()
    }
    else {
      log.error("Unable to resolve context obj : ${params.__context}");
    }

    def redirect_to = request.getHeader('referer')

    if ( params.redirect ) {
      redirect_to = params.redirect
    }
    else if ( ( params.fragment ) && ( params.fragment.length() > 0 ) ) {
      redirect_to = "${redirect_to}#${params.fragment}"
    }

    redirect(url: redirect_to)
  }

}
