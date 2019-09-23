package com.wait.mypic;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author collinewaitire 23 Sep 2019
 */
@Configuration
public class MockChromeConfiguration {
	@Bean
	ChromeDriverFactory chromeDriverFactory() {
		ChromeDriverFactory factory = mock(ChromeDriverFactory.class);
		given(factory.getObject()).willReturn(mock(ChromeDriver.class));
		return factory;
	}
}
