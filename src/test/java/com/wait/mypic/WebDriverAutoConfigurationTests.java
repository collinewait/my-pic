package com.wait.mypic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * @author collinewaitire 15 Sep 2019
 */
public class WebDriverAutoConfigurationTests {
	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	private void load(Class<?>[] configs, String... environment) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(WebDriverAutoConfiguration.class);

		if (configs.length > 0) {
			applicationContext.register(configs);
		}
		TestPropertyValues.of(environment).applyTo(applicationContext);
		applicationContext.refresh();
		this.context = applicationContext;
	}

	@Test
	public void fallbackToNonGuiModeWhenAllBrowsersDisabled() {
		load(new Class[] {}, "com.wait.mypic.webdriver.firefox.enabled:false",
				"com.wait.mypic.webdriver.safari.enabled:false",
				"com.wait.mypic.webdriver.chrome.enabled:false");

		WebDriver driver = context.getBean(WebDriver.class);
		assertThat(
				ClassUtils.isAssignable(TakesScreenshot.class, driver.getClass()))
						.isFalse();
		assertThat(ClassUtils.isAssignable(HtmlUnitDriver.class, driver.getClass()))
				.isTrue();
	}
}
