package com.k_int.yarm

class RefdataCategory {
  
  private static rdv_cache = [:]

  String description
  String label

  static mapping = {
  }

  static hasMany = [
  ]

  static mappedBy = [
  ]

  static constraints = {
    label(blank:true, nullable:true)
  }

  static RefdataValue lookupOrCreate(session, category_name, value) {
    return lookupOrCreate(session,category_name,value,null);
  }

  static RefdataValue lookupOrCreate(session, category_name, value, sortkey) {

    // log.debug("lookupOrCreate(${category_name}, ${value}, ${sortkey})");
  
    if ( ( value == null ) || ( category_name == null ) )
      throw new RuntimeException("Request to lookupOrCreate null value in category ${category_name}");

    def result = null;

    def rdv_cache_key = category_name+':'+value+':'+sortkey
    def rdv_id  = rdv_cache[rdv_cache_key]
    if ( rdv_id ) {
      result = RefdataValue.get(rdv_id);
    }
    else {
      // The category.
      RefdataCategory.withTransaction { status ->
  
  
        def cats = RefdataCategory.executeQuery('select c from RefdataCategory as c where c.desc = ?',category_name);
        def cat = null;
  
        if ( cats.size() == 0 ) {
          log.debug("Create new refdata category ${category_name}");
          cat = new RefdataCategory(desc:category_name, label:category_name)
          if ( cat.save(failOnError:true, flush:true) ) {
          }
          else {
            log.error("Problem creating new category ${category_name}");
            cat.errors.each {
              log.error("Problem: ${it}");
            }
          }
  
          // log.debug("Create new refdataCategory(${category_name}) = ${cat.id}");
        }
        else if ( cats.size() == 1 ) {
          cat = cats[0]
          result = RefdataValue.findByOwnerAndValueIlike(cat, value)
        }
        else {
          throw new RuntimeException("Multiple matching refdata category names");
        }
  
        if ( !result ) {
          // Create and save a new refdata value.
          log.info("Attempt to create new refdataValue(${category_name},${value},${sortkey})");
          result = new RefdataValue(owner:cat, value:value, sortKey:sortkey)
          if ( result.save(failOnError:true, flush:true) ) {
          }
          else {
            log.debug("Problem saving new refdata item");
            result.errors.each {
              log.error("Problem: ${it}");
            }
          }
        }
        else {
          rdv_cache[rdv_cache_key] = result.id
        }
      }
    }
    

    assert result != null

    // return the refdata value.
    result
  }

}
