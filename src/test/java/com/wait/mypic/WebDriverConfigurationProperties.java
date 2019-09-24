package com.wait.mypic;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author collinewaitire 15 Sep 2019
 */
@Data
@ConfigurationProperties("com.wait.mypic.webdriver")
public class WebDriverConfigurationProperties {

	private Firefox firefox = new Firefox();
	private Safari safari = new Safari();
	private Chrome chrome = new Chrome();

	@Data
	static class Firefox {
		private boolean enabled = true;
	}

	@Data
	static class Safari {
		private boolean enabled = true;
	}

	@Data
	static class Chrome {
		private boolean enabled = true;
	}
}
