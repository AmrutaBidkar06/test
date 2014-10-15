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

public class pa_InviteFriends {

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
		String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";

		final int N = 5;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
		}
		randomName = sb.toString();

		// System.out.println(randomName);
	}
	
	@Parameters({"url"})
	@Test(priority=1)
	public void testInviteFriendsLinkFunct(String url){
		driver.findElement(By.linkText("Invite Friends")).click();
		//pu.assert_text("Invite your friends");
		System.out.println("****** Invite friend link Succesful ******");
	}

	@Parameters({"url"})
	@Test(priority=2)
	public void testInviteFriends_MandatoryFields(String url) throws InterruptedException{
		driver.findElement(By.linkText("Invite Friends")).click();
		pu.wait_find_click(objWrite.WriteObjectRepo("if_submit"));
		Thread.sleep(1000);
		pu.assert_text(objRead.ReadObjectRepo("if_email_error"));
		pu.assert_text(objRead.ReadObjectRepo("if_message_error"));
		Thread.sleep(1000);
		System.out.println("****** Invite friend mandetory link Succesful ******");
	}
	
	@Parameters({"url"})
	@Test(priority=3)
	public void testInviteFriends_InvalidEmail() throws InterruptedException{
		driver.findElement(By.linkText("Invite Friends")).click();
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("if_email_ids"),"google");
		pu.wait_find_click(objWrite.WriteObjectRepo("if_submit"));
		pu.assert_text(objRead.ReadObjectRepo("if_invalid_email"));
		//Thread.sleep(1000);
		System.out.println("****** Invite friend invalid email id Succesful ******");
	}
	
	@Parameters({"url"})
	@Test(priority=4)
	public void testSendInvitation(String url) throws InterruptedException{
		driver.findElement(By.linkText("Invite Friends")).click();
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("if_email_ids"),objRead.ReadObjectRepo("if_email_id1") + "," + objRead.ReadObjectRepo("if_email_id2"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("if_message_body"),objRead.ReadObjectRepo("if_message_body"));
		pu.wait_find_click(objWrite.WriteObjectRepo("if_submit"));
		pu.assert_text(objRead.ReadObjectRepo("if_success"));
		//Thread.sleep(2000);
		System.out.println("****** Invite friend Succesful ******");
		pu.wait_find_click(objWrite.WriteObjectRepo("if_reset"));
	}
	
	@Parameters({"url"})
	@Test(priority=5)
	public void testInviteFriends_ClearFields(String url) throws InterruptedException{
		driver.findElement(By.linkText("Invite Friends")).click();
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("if_email_ids"),objRead.ReadObjectRepo("if_email_id1"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("if_message_body"),objRead.ReadObjectRepo("if_message_body"));
		pu.wait_find_click(objWrite.WriteObjectRepo("if_reset"));
		Thread.sleep(1000);
		assertTrue(selenium.getText(objWrite.WriteObjectRepo("if_email_ids")).contains(""));
		assertTrue(selenium.getText(objWrite.WriteObjectRepo("if_message_body")).contains(""));
		System.out.println("****** Reset invites Succesful ******");
	}
	
	@Parameters({"url"})
	@Test(priority=6)
	public void testInviteFriendsFacebookShare(String url) throws InterruptedException{
		driver.findElement(By.linkText("Invite Friends")).click();
		String winHandleBefore=driver.getWindowHandle();
		selenium.click(objWrite.WriteObjectRepo("fb_link"));
        for(String winHandle : driver.getWindowHandles()){
            driver.switchTo().window(winHandle);
        }
        
        pu.wait_find_send_keys(objWrite.WriteObjectRepo("fb_id"), objRead.ReadObjectRepo("fb_id"));
        pu.wait_find_send_keys(objWrite.WriteObjectRepo("fb_password"), objRead.ReadObjectRepo("fb_password"));
       // selenium.uncheck(objWrite.WriteObjectRepo("fb_remember"));
        pu.wait_find_click(objWrite.WriteObjectRepo("fb_signin"));
        Thread.sleep(1000);
       pu.assert_text("MiraiConsult");
       pu.wait_find_click(objWrite.WriteObjectRepo("fb_publish"));
       Thread.sleep(1000);
       driver.switchTo().window(winHandleBefore);
       if( pu.isAlertPresent())
 	   {
    	   driver.switchTo().alert().accept();
    	   //System.out.println("Alert present");
 	   }
       	else
       	{
       		//System.out.println("No alert present");
       	}
       System.out.println("********** Miraiconsult posted successfully on Facebook **********");
       Thread.sleep(2000);
       driver.switchTo().window(winHandleBefore);
      }

	@Parameters({"url"})
	@Test(priority=7)
	public void testInviteFriendsTwitterShare(String url) throws InterruptedException{
		driver.findElement(By.linkText("Invite Friends")).click();
		RandomString();
		String winHandleBefore=driver.getWindowHandle();
		selenium.click(objWrite.WriteObjectRepo("tweet_link"));
        Thread.sleep(1000);
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
        Thread.sleep(1000);
        pu.wait_find_click(objWrite.WriteObjectRepo("tweet_tweet"));
       // pu.assert_text(objRead.ReadObjectRepo("tweet_success"));
        }
        else
        {
        	pu.wait_find_click(objWrite.WriteObjectRepo("tweet_tweet2"));   
        }
    
        System.out.println("********** Miraiconsult posted successfully on Tweeter **********");
        //Thread.sleep(2000);
        driver.switchTo().window(winHandleBefore);
        Thread.sleep(1000);
    	
      }
	
	@Parameters({"url"})
	@Test(priority=8)
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