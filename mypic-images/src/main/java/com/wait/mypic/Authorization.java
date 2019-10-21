package com.wait.mypic;

import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ParseException;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.util.SimpleMethodInvocation;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.spring5.context.webflux.ISpringWebFluxContext;

/**
 * @author collinewaitire 21 Oct 2019
 */
public class Authorization {

	private static final Logger log = LoggerFactory.getLogger(Authorization.class);

	private ISpringWebFluxContext context;
	private SecurityExpressionHandler<MethodInvocation> handler;

	public Authorization(ISpringWebFluxContext context, SecurityExpressionHandler<MethodInvocation> handler) {
		this.context = context;
		this.handler = handler;
	}

	public boolean expr(String accessExpression) {
		Map<String, Object> sessionVars = (Map<String, Object>) this.context.getVariable("session");
		SecurityContext securityContext = (SecurityContext) sessionVars.get("SPRING_SECURITY_CONTEXT");
		Authentication authentication = securityContext.getAuthentication();

		log.debug("Checking if user \"{}\" meets expr \"{}\".",
				new Object[] { (authentication == null ? null : authentication.getName()), accessExpression });

		/*
		 * In case this expression is specified as a standard variable expression
		 * (${...}), clean it.
		 */
		String expr = ((accessExpression != null && accessExpression.startsWith("${") && accessExpression.endsWith("}"))
				?

				accessExpression.substring(2, accessExpression.length() - 1)
				: accessExpression);

		try {
			if (ExpressionUtils.evaluateAsBoolean(handler.getExpressionParser().parseExpression(expr),
					handler.createEvaluationContext(authentication, new SimpleMethodInvocation()))) {

				log.debug("Checked \"{}\" for user \"{}\". " + "Access GRANTED",
						new Object[] { accessExpression, (authentication == null ? null : authentication.getName()) });

				return true;
			} else {
				log.debug("Checked \"{}\" for user \"{}\". " + "Access DENIED",
						new Object[] { accessExpression, (authentication == null ? null : authentication.getName()) });

				return false;
			}
		} catch (ParseException e) {
			throw new TemplateProcessingException("An error happened parsing \"" + expr + "\"", e);
		}
	}
}
