package com.wait.mypic;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author collinewaitire 23 Sep 2019
 */
@Configuration
public class MockFirefoxConfiguration {
	@Bean
	FirefoxDriverFactory firefoxDriverFactory() {
		FirefoxDriverFactory factory = mock(FirefoxDriverFactory.class);
		given(factory.getObject()).willReturn(mock(FirefoxDriver.class));
		return factory;
	}
}
