package com.wait.mypic;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.openqa.selenium.safari.SafariDriver;
import org.springframework.context.annotation.Bean;

/**
 * @author collinewaitire 23 Sep 2019
 */
public class MockSafariConfiguration {
	@Bean
	SafariDriverFactory safariDriverFactory() {
		SafariDriverFactory factory = mock(SafariDriverFactory.class);
		given(factory.getObject()).willReturn(mock(SafariDriver.class));
		return factory;
	}
}
