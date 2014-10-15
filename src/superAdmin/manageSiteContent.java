package superAdmin;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utility.PageUtils;
import com.thoughtworks.selenium.Selenium;

public class manageSiteContent {

	// declaring the selenium object
	private Selenium selenium;
	
	// declaring the webdriver object
	private WebDriver driver = null;
	
	private PageUtils pu = null;
	String url, randomName;
	

	@BeforeClass(alwaysRun = true)
	
	@Parameters({ "browser", "url" })
	public void setup(String browser, String url) throws Exception {

		System.out.println("Browser: " + browser);

		if (browser.equals("FF")) {
			System.out.println("FF is selected");
		 ProfilesIni allProfiles = new ProfilesIni();
		 FirefoxProfile profile = allProfiles.getProfile("selenium");
			driver = new FirefoxDriver(profile);
			
		}

		else if (browser.equals("IE")) {
			System.out.println("IE is selected");
			driver = new InternetExplorerDriver();

		}

		else if (browser.equals("CH")) {
			System.out.println("Google chrome is selected");
			driver = new ChromeDriver();
		}

		else if (browser.equals("SF")) {
			System.out.println("Safari is selected");
			driver = new SafariDriver();
		}
		selenium = new WebDriverBackedSelenium(driver, url + "admin/Login.aspx");

        driver.navigate().to(url + "admin/Login.aspx");

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		pu = PageUtils.getInstance(selenium, driver, url);
		pu.wait_find_send_keys("//*[@id='ctl00_MainPage_txtEmail']", "super@admin.com");
		pu.wait_find_send_keys("//*[@id='ctl00_MainPage_txtPassword']", "admin!2#4");
		pu.wait_find_click("//*[@id='ctl00_MainPage_btnLogin']");
		Thread.sleep(5000);
	
	}
	@AfterClass(alwaysRun = true)
	public void StopDriver() throws Exception {
		 // take the screenshot at the end of every test
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        // now save the screenshto to a file some place
        FileUtils.copyFile(scrFile, new File(".\\screenshot.jpg"));
        
		driver.quit();
	}
	
	@Parameters({"url"})
	@Test//(enabled=false)
	public void testManageSiteContent_Option(String url) throws InterruptedException{
		driver.findElement(By.partialLinkText("Welcome")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("Manage Site Content")).click();
		Thread.sleep(2000);
		pu.assert_text("Homepage Corousal");
	}
	
	@Parameters({"url"})
	@Test//(enabled=false)
	public void testBlankUpload(String url) throws InterruptedException{
		testManageSiteContent_Option(url);
		pu.wait_find_click("//*[@id='ctl00_MainPage_btnSubmit']");
		pu.assert_text("Please select atleast one image.");
	}
	
	@Parameters({"url"})
	@Test(enabled=false) //Not uploading image.
	public void testUploadValidImage(String url) throws InterruptedException{
		testManageSiteContent_Option(url);
		WebElement fileInput = driver.findElement(By.xpath("//input[@id='fileUploadForImage']"));
		Thread.sleep(3000);
		
		//type the path of the file which is to be uploaded
		fileInput.sendKeys(".\\testfiles\\Tulips.jpg");
		Thread.sleep(3000);
		pu.wait_find_click("//*[@id='ctl00_MainPage_btnSubmit']");
		Thread.sleep(1000);
		pu.assert_text("Your images saved successfully.");
	}
	
	
	@Parameters({"url"})
	@Test//(enabled=false)
	public void testUploadInValidImage(String url) throws InterruptedException{
		testManageSiteContent_Option(url);
		WebElement fileInput = driver.findElement(By.xpath("//*[@id='fileUploadForImage']"));
		Thread.sleep(3000);
		
		//type the path of the file which is to be uploaded
		fileInput.sendKeys(".\\testfiles\\tutorial.txt");
		Thread.sleep(3000);
		pu.assert_text("Supports only .jpg,.png,.gif,.jpeg formats. Please upload a file with either of these extensions.");
	}
	
	
}