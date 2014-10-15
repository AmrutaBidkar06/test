package doctor;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

public class doc_Feed {

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
		selenium = new WebDriverBackedSelenium(driver, url);

        driver.navigate().to(url);

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		pu = PageUtils.getInstance(selenium, driver, url);
		if(selenium.isTextPresent("Ask Doctors"))
		{
		driver.findElement(By.linkText("Login")).click();
		}
		else{
			System.out.println("already on login page");
		}
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"),objRead.ReadObjectRepo("doc_email"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"),objRead.ReadObjectRepo("doc_password"));
		pu.wait_find_click(objWrite.WriteObjectRepo("lp_signin"));
		Thread.sleep(2000);
		
	}
	@AfterClass(alwaysRun = true)
	public void StopDriver() throws Exception {
		 // take the screenshot at the end of every test
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        // now save the screenshto to a file some place
        FileUtils.copyFile(scrFile, new File(".\\screenshot.jpg"));
        
		driver.quit();
	}
	
	public void RandomString() {
		Random r = new Random(); // just create one and keep it around
		String alphabet = "abcdefghijklmnopqrstuvwxyz";

		final int N = 4;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
		}
		randomName = sb.toString();

		// System.out.println(randomName);
	}
	
	
	@Parameters({"url"})
	@Test(priority=1)//(enabled=false)
	public void testFeedOptionFunct(String url) throws InterruptedException{
		driver.findElement(By.linkText("Answers")).click();
		Thread.sleep(2000);
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("doc_1st_feed"))){
			Thread.sleep(1000);
			System.out.println("********** Feeds present **********");
		}
		else{
			System.out.println("********** No Feeds **********");
		}
	}
	
	@Parameters({"url"})
	@Test(priority=2)//(enabled=false)
	public void testFeedFilter(String url) throws InterruptedException{
		driver.findElement(By.linkText("Answers")).click();
		selenium.check(objWrite.WriteObjectRepo("doc_feed_all"));
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("doc_1st_feed"))){
			System.out.println("Feeds are present");
		}
		else{
			System.out.println("No feeds");
		}
		selenium.check(objWrite.WriteObjectRepo("doc_feed_my"));
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("doc_1st_feed"))){
			System.out.println("Feeds are present");
		}
		else{
			System.out.println("No feeds");
		}
		System.out.println("********** Feeds filtered **********");
	}
	
	@Parameters({"url"})
	@Test(priority=3)//(enabled=false)
	public void testFeedQuestionLinkFunct(String url) throws InterruptedException{
		driver.findElement(By.linkText("Answers")).click();
		selenium.check(objWrite.WriteObjectRepo("doc_feed_all"));
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("doc_1st_feed"))){
		  pu.wait_find_click(objWrite.WriteObjectRepo("doc_1st_feed"));
		  Thread.sleep(2000);
		//  assertTrue(selenium.isElementPresent("//*[@id='aspnetForm']/div[3]/div[3]/div/div/div/div/div[4]/div/div/a[1]/img"));
		  System.out.println("Feed present");
		}
		else{
			System.out.println("No Feeds present");
		}
		System.out.println("********** Feeds link present **********");
	}
	
	@Parameters({"url"})
	@Test(priority=4)//(enabled=false)
	public void testFeedEndorse_Funct(String url) throws InterruptedException{
		driver.findElement(By.linkText("Answers")).click();
		selenium.check(objWrite.WriteObjectRepo("doc_feed_all"));
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_1st_feed"));
		Thread.sleep(2000);
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("doc_feed,endorese")))
		{
			pu.wait_find_click(objWrite.WriteObjectRepo("doc_feed,endorese"));
			Thread.sleep(2000);
			System.out.println("Endorsed feed");
		}
		else
		{
			System.out.println("already endorsed....");
		}
		System.out.println("********** Feed Endorsed **********");
	}
	
	
	@Parameters({"url"})
	@Test(priority=5)//(enabled=false) 
	public void testFeedFacebookShare(String url) throws InterruptedException{
		driver.findElement(By.linkText("Answers")).click();
		selenium.check(objWrite.WriteObjectRepo("doc_feed_all"));
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_1st_feed"));
		Thread.sleep(2000);
		String winHandleBefore=driver.getWindowHandle();
		selenium.click(objWrite.WriteObjectRepo("doc_feed_fb_share"));
        for(String winHandle : driver.getWindowHandles()){
            driver.switchTo().window(winHandle);
        }
        
      
        pu.wait_find_send_keys(objWrite.WriteObjectRepo("fb_id"), objRead.ReadObjectRepo("fb_id"));
        pu.wait_find_send_keys(objWrite.WriteObjectRepo("fb_password"), objRead.ReadObjectRepo("fb_password"));
       // selenium.uncheck(objWrite.WriteObjectRepo("fb_remember"));
        pu.wait_find_click(objWrite.WriteObjectRepo("fb_signin"));
        Thread.sleep(2000);
     //  pu.assert_text("MiraiConsult");
       pu.wait_find_click(objWrite.WriteObjectRepo("fb_publish"));
       Thread.sleep(2000);
       driver.switchTo().window(winHandleBefore);
       if( pu.isAlertPresent())
 	   {
 	   System.out.println("Alert present");
 	   }
       	else
       	{
       		System.out.println("No alert present");
       	}
       System.out.println("********** Question posted successfully on Facebook **********");
       Thread.sleep(2000);
       driver.switchTo().window(winHandleBefore);
 }

	@Parameters({"url"})
	@Test(priority=6)//(enabled=false)
	public void testFeedTwitterShare(String url) throws InterruptedException{
		driver.findElement(By.linkText("Answers")).click();
		selenium.check(objWrite.WriteObjectRepo("doc_feed_all"));
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_1st_feed"));
		Thread.sleep(2000);
		RandomString();
		String winHandleBefore=driver.getWindowHandle();
		selenium.click(objWrite.WriteObjectRepo("doc_feed_tweet_share"));
		Thread.sleep(2000);
        for(String winHandle : driver.getWindowHandles()){
            driver.switchTo().window(winHandle);
        }
        selenium.type("//*[@id='status']", randomName+"Automated Question tweeted from ask-Mirai");
        if(selenium.isElementPresent("//input[contains(@value,'Sign in and Tweet')]"))
        {
        pu.wait_find_send_keys(objWrite.WriteObjectRepo("tweet_id"),objRead.ReadObjectRepo("tweet_id"));
        pu.wait_find_send_keys(objWrite.WriteObjectRepo("tweet_password"),objRead.ReadObjectRepo("tweet_password"));
     //   selenium.uncheck(objWrite.WriteObjectRepo("tweet_remember"));
        pu.wait_find_click(objWrite.WriteObjectRepo("tweet_signin"));
        Thread.sleep(2000);
        pu.wait_find_click(objWrite.WriteObjectRepo("tweet_tweet"));
       // pu.assert_text(objRead.ReadObjectRepo("tweet_success"));
        }
        else
        {
        	pu.wait_find_click(objWrite.WriteObjectRepo("tweet_tweet2"));   
        }
        driver.get("https://twitter.com/intent/tweet/complete?latest_status_id=511377874952015872&original_referer=https%3A%2F%2Ftwitter.com%2Flogin%2Ferror%3Fusername_or_email%3DRTCFoobar%26redirect_after_login");
       // pu.wait_find_click(objWrite.WriteObjectRepo("tweet_username"));
       // pu.wait_find_click(objWrite.WriteObjectRepo("tweet_logout"));
        System.out.println("********** Miraiconsult posted successfully on Tweeter **********");
        driver.switchTo().window(winHandleBefore);
       // Thread.sleep(2000);
       // logOut(url);
   }
	
	
	@Parameters({"url"})
	@Test(enabled=false) // displays windows pop up which cannot be handled using selenium
	public void testFeedGmailShare(String url) throws InterruptedException{
		driver.findElement(By.linkText("Answers")).click();
		selenium.check(objWrite.WriteObjectRepo("doc_feed_all"));
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_1st_feed"));
		Thread.sleep(2000);
		String winHandleBefore=driver.getWindowHandle();
		//String question=driver.findElement(By.xpath("//*[@id='aspnetForm']/div[4]/div/div[2]/div/div[1]/div/p")).getText();
        pu.wait_find_click(objWrite.WriteObjectRepo("doc_feed_gmail_share"));
        for(String winHandle : driver.getWindowHandles()){
            driver.switchTo().window(winHandle);
        }
        
        pu.wait_find_send_keys("//*[@id='Email']", "ooyalatester@vertisinfotech.com");
        pu.wait_find_send_keys("//*[@id='Passwd']", "!password*");
        selenium.uncheck("//*[@id='PersistentCookie']");
        pu.wait_find_click("//*[@id='signIn']");
        Thread.sleep(1000);
      //  pu.assert_text(question);
        System.out.println("********** Question posted successfully on Gmail **********");
        
        driver.switchTo().window(winHandleBefore);
    }
	
	@Parameters({"url"})
	@Test(priority=7)
	public void logOut(String url) throws InterruptedException {
		driver.findElement(By.partialLinkText("Welcome")).click();
		driver.findElement(By.linkText("Logout")).click();
		Thread.sleep(1000);
		if( pu.isAlertPresent())
	 	   {
			driver.switchTo().alert().accept();
	 	   //System.out.println("Alert present");
	 	   }
	       	else
	       	{
	       		System.out.println("No alert present");
	       	}
		//driver.switchTo().alert();
		Thread.sleep(1000);
		//driver.switchTo().alert().accept();
		System.out.println("********** Logged Out Successfully **********");
	}

	
}
	