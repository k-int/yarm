package com.k_int.yarm.utils;
 
import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;
 
import java.util.List;
 
/**
 * 
 * @see : http://grails-dev.blogspot.co.uk/2014/10/adding-custom-operators-in-hql-for.html
 * @see : https://metabroadcast.com/blog/hibernate-and-postgres-fts
 * @see : http://java-talks.blogspot.co.uk/2014/04/use-postgresql-full-text-search-with-hql.html
 */ 
public class PgFullTextSearchFunction implements SQLFunction {
 
  @SuppressWarnings("unchecked")
  @Override
  public String render(Type type, List args, SessionFactoryImplementor sessionFactoryImplementor) throws QueryException{
    if (args.size() < 2) {
      throw new IllegalArgumentException( "The function must be passed 2 arguments");
    }
 
    String field = (String) args.get(0);
    String value = (String) args.get(1);
    String fragment = "";
    fragment += "to_tsvector(" + field + ") @@ ";
    fragment +="to_tsquery("+value+")";

    return fragment;
  }
 
  @Override
  public Type getReturnType(Type columnType, Mapping mapping) throws QueryException {
    return new BooleanType();
  }
 
  @Override
  public boolean hasArguments() {
    return true;
  }
 
  @Override
  public boolean hasParenthesesIfNoArguments() {
    return false;
  }

}
