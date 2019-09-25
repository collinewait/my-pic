package com.wait.mypic.ops;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @author collinewaitire 24 Sep 2019
 */
@Component
public class MyPicHealthIndicator implements HealthIndicator {
	@Override
	public Health health() {
		try {
			URL url = new URL("http://localhost:3000");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			int statusCode = conn.getResponseCode();
			if (statusCode >= 200 && statusCode < 300) {
				return Health.up().build();
			} else {
				return Health.down().withDetail("HTTP Status Code", statusCode).build();
			}
		} catch (IOException e) {
			return Health.down(e).build();
		}
	}
}
