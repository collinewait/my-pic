package com.wait.mypic;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * @author collinewaitire 21 Oct 2019
 */
public class SecurityDialect extends AbstractDialect implements IExpressionObjectDialect {

	private final SecurityExpressionHandler<MethodInvocation> handler;

	public SecurityDialect(SecurityExpressionHandler<MethodInvocation> handler) {
		super("Security Dialect");
		this.handler = handler;
	}

	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new SecurityExpressionObjectFactory(handler);
	}
}
