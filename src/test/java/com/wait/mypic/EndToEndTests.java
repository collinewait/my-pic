package com.wait.mypic;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

/**
 * @author collinewaitire 15 Sep 2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTests {
	static ChromeDriverService service;
	static ChromeDriver driver;
	@LocalServerPort
	int port;

	@BeforeClass
	public static void setUp() throws IOException {
		System.setProperty("webdriver.chrome.driver", "ext/chromedriver");
		service = ChromeDriverService.createDefaultService();
		driver = new ChromeDriver(service);
		Path testResults = Paths.get("target", "test-results");
		if (!Files.exists(testResults)) {
			Files.createDirectory(testResults);
		}
	}

	@Test
	public void homePageShouldWork() throws IOException {
		driver.get("http://localhost:" + port);

		takeScreenshot("homePageShouldWork-1");
		assertThat(driver.getTitle()).isEqualTo("My Pic");

		String pageContent = driver.getPageSource();
		assertThat(pageContent)
				.contains("<a href=\"/images/my-first-image.jpg/raw\">");
		WebElement element = driver
				.findElement(By.cssSelector("a[href*=\"my-first-image.jpg\"]"));

		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().perform();

		takeScreenshot("homePageShouldWork-2");
		driver.navigate().back();
	}

	private void takeScreenshot(String name) throws IOException {
		FileCopyUtils.copy(driver.getScreenshotAs(OutputType.FILE),
				new File("target/test-results/TEST-" + name + ".jpg"));
	}

	@AfterClass
	public static void tearDown() {
		service.stop();
	}

}
