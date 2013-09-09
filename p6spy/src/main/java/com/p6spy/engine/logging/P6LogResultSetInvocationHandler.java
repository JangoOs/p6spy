package com.p6spy.engine.logging;

import com.p6spy.engine.proxy.GenericInvocationHandler;
import com.p6spy.engine.proxy.MethodNameAndParameterLikeMatcher;
import com.p6spy.engine.proxy.MethodNameMatcher;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Quinton McCombs
 * @since 09/2013
 */
public class P6LogResultSetInvocationHandler extends GenericInvocationHandler<ResultSet> {

  /**
   * Creates a new invocation handler for the given object.
   *
   * @param underlying The object being proxied
   */
  public P6LogResultSetInvocationHandler(final ResultSet underlying, final StatementInformation statementInformation)
      throws SQLException {
    super(underlying);

    P6LogResultSetInformation resultSetInformation = new P6LogResultSetInformation(statementInformation);
    P6LogResultSetNextDelegate nextDelegate = new P6LogResultSetNextDelegate(resultSetInformation);
    P6LogResultSetGetColumnValueDelegate getColumnValueDelegate = new P6LogResultSetGetColumnValueDelegate(resultSetInformation);

    addDelegate(
        new MethodNameMatcher("next"),
        nextDelegate
    );

    // add delegates for the basic getXXXX(int) and getXXXX(String) methods
    addDelegate(
        new MethodNameAndParameterLikeMatcher("get*", int.class),
        getColumnValueDelegate
    );
    addDelegate(
        new MethodNameAndParameterLikeMatcher("get*", String.class),
        getColumnValueDelegate
    );
  }
}
