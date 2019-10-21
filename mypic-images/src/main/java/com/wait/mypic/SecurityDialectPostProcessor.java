package com.wait.mypic;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * @author collinewaitire 21 Oct 2019
 */
@Component
public class SecurityDialectPostProcessor implements BeanPostProcessor, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof SpringTemplateEngine) {
			SpringTemplateEngine engine = (SpringTemplateEngine) bean;
			SecurityExpressionHandler<MethodInvocation> handler = applicationContext
					.getBean(SecurityExpressionHandler.class);
			SecurityDialect dialect = new SecurityDialect(handler);
			engine.addDialect(dialect);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
