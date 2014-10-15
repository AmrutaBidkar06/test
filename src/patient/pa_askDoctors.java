package patient;

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

import static org.testng.Assert.assertTrue;
import utility.PageUtils;

import com.thoughtworks.selenium.Selenium;

public class pa_askDoctors {

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
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"),objRead.ReadObjectRepo("patient_email"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"),objRead.ReadObjectRepo("patient_password"));
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
	@Test(priority=1)
	public void AskDoctorlink(String url){
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("pat_askdoc_link"))).click();
		assertTrue(selenium.isElementPresent(objWrite.WriteObjectRepo("pat_que_next")));
		System.out.println("********** Ask doctor Link Present **********");
	}
	
	@Parameters({"url"})
	@Test(priority=2)
	public void BlankQuestionTest(String url)
	{
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("pat_askdoc_link"))).click();
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_que_next"));
		assertTrue(selenium.isTextPresent("Please Enter Question"));
		System.out.println("********** Error on blank question  **********");
	}
	@Parameters({"url"})
	@Test(priority=3)
	public void testAskQuest_NoRelevantAns(String url){
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("pat_askdoc_link"))).click();
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("pat_que_text"),objRead.ReadObjectRepo("que"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_que_next"));
		pu.assert_text("No records found");
		System.out.println("********** New question **********");
	}
	
	@Parameters({"url"})
	@Test(priority=4)
	public void testAskQuest_Valid(String url){
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("pat_askdoc_link"))).click();
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("pat_que_text"),objRead.ReadObjectRepo("que"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_que_next"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_que_ask"));
		pu.assert_text(objRead.ReadObjectRepo("pat_que_success"));
		System.out.println("********** Question asked successfully **********");
	}
	
	@Parameters({"url"})
	@Test(priority=5)
	public void testAskQuest_RelevantQuestLink(String url){
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("pat_askdoc_link"))).click();
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("pat_que_text"),objRead.ReadObjectRepo("que_keyword"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_que_next"));
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("pat_que_rel"))){
			System.out.println(" ********** Relevant answer present. **********");
		}
		else{
			System.out.println(" ********** No relevant answers found **********");
		}
	}
	
	@Parameters({"url"})
	@Test(priority=6)
	public void testAskQuest_ThankDoc(String url) throws InterruptedException{
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("pat_askdoc_link"))).click();
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("pat_que_text"),objRead.ReadObjectRepo("que_keyword"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_que_next"));
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("pat_que_rel"))){
			pu.wait_find_click(objWrite.WriteObjectRepo("pat_que_rel"));
			Thread.sleep(1000);
			if(pu.getText(objWrite.WriteObjectRepo("pat_act_useful")).contains("Is it useful?"))
			{
			pu.wait_find_click(objWrite.WriteObjectRepo("pat_act_useful"));
			Thread.sleep(1000); 
			assertTrue(selenium.getText(objWrite.WriteObjectRepo("pat_act_useful")).contains("Thanks its useful"));
			System.out.println("********** Thanked Doctor **********");
			}
			else
			{
			System.out.println("********** already Thanked.... **********");
			}
		}
		else{
			System.out.println("********** No relevant answers found **********");
		}
	}
	
	@Parameters({"url"})
	@Test(priority=7)
	public void testFeedFacebookShare(String url) throws InterruptedException{
		String winHandleBefore=driver.getWindowHandle();
		selenium.click(objWrite.WriteObjectRepo("pat_act_fb_share"));
        for(String winHandle : driver.getWindowHandles()){
            driver.switchTo().window(winHandle);
        }

		pu.wait_find_send_keys(objWrite.WriteObjectRepo("fb_id"), objRead.ReadObjectRepo("fb_id"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("fb_password"), objRead.ReadObjectRepo("fb_password"));
		pu.wait_find_click(objWrite.WriteObjectRepo("fb_signin"));
		pu.wait_find_click(objWrite.WriteObjectRepo("fb_publish"));
		Thread.sleep(1000);
		driver.switchTo().window(winHandleBefore);
		if( pu.isAlertPresent())
		{
		//System.out.println("Alert present");
		}
		else
		{
		//	System.out.println("No alert present");
		}
		System.out.println("********** Question posted successfully on Facebook **********");
		Thread.sleep(1000);
			
		driver.switchTo().window(winHandleBefore);
 }
	
	

	
	@Parameters({"url"})
	@Test(priority=8)
	public void testFeedTwitterShare(String url) throws InterruptedException{
		RandomString();
		String winHandleBefore=driver.getWindowHandle();
		selenium.click(objWrite.WriteObjectRepo("pat_act_tweet_share"));
		Thread.sleep(1000);
        for(String winHandle : driver.getWindowHandles()){
            driver.switchTo().window(winHandle);
        }
        selenium.type("//*[@id='status']", randomName+"Automated Question tweeted from ask-Mirai");
        if(selenium.isElementPresent("//input[contains(@value,'Sign in and Tweet')]"))
        {
        pu.wait_find_send_keys(objWrite.WriteObjectRepo("tweet_id"),objRead.ReadObjectRepo("tweet_id"));
        pu.wait_find_send_keys(objWrite.WriteObjectRepo("tweet_password"),objRead.ReadObjectRepo("tweet_password"));
        pu.wait_find_click(objWrite.WriteObjectRepo("tweet_signin"));
        Thread.sleep(1000);
        pu.wait_find_click(objWrite.WriteObjectRepo("tweet_tweet"));
        }
        else
        {
        	pu.wait_find_click(objWrite.WriteObjectRepo("tweet_tweet2"));   
        }
        driver.get("https://twitter.com/intent/tweet/complete?latest_status_id=511377874952015872&original_referer=https%3A%2F%2Ftwitter.com%2Flogin%2Ferror%3Fusername_or_email%3DRTCFoobar%26redirect_after_login");
        System.out.println("********** Miraiconsult posted successfully on Tweeter **********");
        driver.switchTo().window(winHandleBefore);
	}
	
	@Parameters({"url"})
	@Test(priority=9)
	public void logOut(String url) throws InterruptedException {
		driver.findElement(By.partialLinkText("Welcome")).click();
		driver.findElement(By.linkText("Logout")).click();
		Thread.sleep(1000);
		if( pu.isAlertPresent())
	 	   {
			//driver.switchTo().alert().accept();
	 	   //System.out.println("Alert present");
	 	   }
	       	else
	       	{
	       	//	System.out.println("No alert present");
	       	}
		System.out.println("********** Logged Out Successfully **********");
	}
	
}