	package home;

	import java.io.File;
import java.util.concurrent.TimeUnit;

	import javatemp.LinkNotFound;
import javatemp.objread;
import javatemp.objwrite;

	import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
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

	public class AskDoctor {

		// declaring the selenium object
		private Selenium selenium;
		
		// declaring the webdriver object
		private WebDriver driver = null;
		
		private PageUtils pu = null;
		String url, randomName;
		
		objread objRead = new objread();
		objwrite objWrite = new objwrite();
		
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
			selenium = new WebDriverBackedSelenium(driver, url );

	        driver.navigate().to(url);

			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
			pu = PageUtils.getInstance(selenium, driver, url);
			
		
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
	@Test(priority = 1)
	public void testAskDoctors_withQuestion(String url) throws InterruptedException{
		driver.navigate().to(url);
		Thread.sleep(1000);
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("hp_search_box"),objRead.ReadObjectRepo("hp_question_text"));
		Thread.sleep(1000);
	    selenium.keyPress(objWrite.WriteObjectRepo("hp_search_box"), "\\13");
	    driver.findElement(By.linkText(objRead.ReadObjectRepo("question_text"))).click();
	    Thread.sleep(1000);
	    System.out.println("Question present.....");
    }
	
	@Parameters({"url"})
	@Test(priority = 2)
	public void testAskDoctors_withoutQuestion(String url) throws InterruptedException{
		driver.navigate().to(url);
		Thread.sleep(1000);
		selenium.keyPress(objWrite.WriteObjectRepo("hp_search_box"), "\\13");
	    if(selenium.isTextPresent("0/200")) {
	    System.out.println("********** Ask Doctors without Question Success ************");
	    }
	    else
	    {
	    	System.out.println("********** Ask Doctors without Question Failed ************");
	    }
    }
	
	@Parameters({"url"})
	@Test(priority = 3)
	public void testAskDoctors_login(String url) throws InterruptedException, LinkNotFound{
		driver.navigate().to(url);
		Thread.sleep(1000);
		pu.wait_find_click(objWrite.WriteObjectRepo("hp_send_button"));
		Thread.sleep(500);
		if(selenium.isTextPresent("0/200")) {
		driver.findElement(By.partialLinkText("Not Satisfied with below Answers? ")).click();
		}
	    else
	    {
	    	System.out.println("********** Ask Doctors without Question Failed ************");
	    }
		if(driver.getCurrentUrl().contains(url + "login")){
			System.out.println("*********** Redirecting To login Page ***********");
		}
		else{
			System.out.println("*********** Not redirecting to login page ***********");	
			throw new LinkNotFound();
		}
    }
	
	@Parameters({"url"})
	@Test(priority = 4)
	public void testAskDoctors_loginWithQuestion(String url) throws InterruptedException, LinkNotFound{
		driver.navigate().to(url);
		Thread.sleep(1000);
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("hp_search_box"),objRead.ReadObjectRepo("hp_question_text"));
		Thread.sleep(2000);
		pu.wait_find_click(objWrite.WriteObjectRepo("hp_send_button"));
	    Thread.sleep(2000);
	   	driver.findElement(By.partialLinkText("Not Satisfied with below Answers?")).click();
	   	Thread.sleep(2000);
	    	if(driver.getCurrentUrl().contains(url + "login")){
				System.out.println("*********** Redirecting To login Page ***********");
			}
			else{
				System.out.println("*********** Not redirecting to login page ***********");	
				throw new LinkNotFound();
			}
    }
	
	@Parameters({"url"})
	@Test(priority = 5)
	public void testAskDoctors_200(String url) throws InterruptedException, LinkNotFound{
		driver.navigate().to(url);
		Thread.sleep(1000);
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("hp_search_box"),objRead.ReadObjectRepo("hp_question_200"));
		Thread.sleep(1000);
		pu.wait_find_click(objWrite.WriteObjectRepo("hp_send_button"));
		Thread.sleep(1000);
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("qp_question_box"),objRead.ReadObjectRepo("hp_question_200" + "TEST"));
		//driver.findElement(By.linkText(objRead.ReadObjectRepo("question_text"))).click();
	    Thread.sleep(1000);
	    if(selenium.isTextPresent("200/200")) {
	    		System.out.println("********** Accepting only 200 characters success ************");
			}
		    else
		    {
		    	System.out.println("********** Accepting more than 200 characters ************");
		    }
	   
		    driver.findElement(By.partialLinkText("Send your Question to the Mirai")).click();
		    Thread.sleep(2000);
	    	if(driver.getCurrentUrl().contains(url + "login")){
				System.out.println("*********** Redirecting To login Page ***********");
			}
			else{
				System.out.println("*********** Not redirecting to login page ***********");	
				throw new LinkNotFound();
			}
	}
	
}
