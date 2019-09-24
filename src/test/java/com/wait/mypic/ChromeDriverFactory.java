package com.wait.mypic;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;

/**
 * @author collinewaitire 15 Sep 2019
 */
public class ChromeDriverFactory implements ObjectFactory<ChromeDriver> {
	private WebDriverConfigurationProperties properties;

	ChromeDriverFactory(WebDriverConfigurationProperties properties) {
		this.properties = properties;
	}

	@Override
	public ChromeDriver getObject() throws BeansException {
		if (properties.getChrome().isEnabled()) {
			try {
				return new ChromeDriver();
			} catch (WebDriverException e) {
				e.printStackTrace();
				// swallow the exception
			}
		}
		return null;
	}
}
