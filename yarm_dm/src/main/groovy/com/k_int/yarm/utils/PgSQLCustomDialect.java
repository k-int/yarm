package com.k_int.yarm.utils;

import org.hibernate.dialect.PostgreSQL81Dialect;

public class PgSQLCustomDialect extends PostgreSQL81Dialect {
 
  public PgSQLCustomDialect() {
    registerFunction("textSearch", new PgFullTextSearchFunction());
  }
}
