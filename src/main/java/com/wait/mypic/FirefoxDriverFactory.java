package com.wait.mypic;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;

/**
 * @author collinewaitire 15 Sep 2019
 */
public class FirefoxDriverFactory implements ObjectFactory<FirefoxDriver> {
	private WebDriverConfigurationProperties properties;

	FirefoxDriverFactory(WebDriverConfigurationProperties properties) {
		this.properties = properties;
	}

	@Override
	public FirefoxDriver getObject() throws BeansException {
		if (properties.getFirefox().isEnabled()) {
			try {
				return new FirefoxDriver();
			} catch (WebDriverException e) {
				e.printStackTrace();
				// swallow the exception
			}
		}
		return null;
	}
}
