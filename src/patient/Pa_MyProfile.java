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

import utility.PageUtils;
import static org.testng.Assert.assertTrue;

import com.thoughtworks.selenium.Selenium;

public class Pa_MyProfile 
{
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

			final int N = 6;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < N; i++) {
				sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
			}
			randomName = sb.toString();

			// System.out.println(randomName);
		}
		
		@Parameters({"url"})
		@Test(priority=1)
		public void TestMyProfilelink(String url) throws InterruptedException
		{
			driver.findElement(By.partialLinkText("Welcome")).click();
			Thread.sleep(1000);
			driver.findElement(By.linkText("My Profile")).click();
			//Thread.sleep(1000);
			System.out.println("********** My Profile Link Present **********");	
			Thread.sleep(1000);
		}
		

		@Parameters({"url"})
		@Test(priority=2)
		public void testMyProfile_SaveButtonFunct(String url) throws InterruptedException{
			pu.wait_find_click(objWrite.WriteObjectRepo("pat_mp_submit"));
			pu.assert_text(objRead.ReadObjectRepo("pat_mp_success"));
			System.out.println("********** My Profile Updated **********");
			Thread.sleep(1000);
		}
		
		
		@Parameters({"url"})
		@Test(priority=3)
		public void testMyProfile_ResetButtonFunct(String url) throws InterruptedException{
			String fname=selenium.getValue(objWrite.WriteObjectRepo("pat_mp_fname"));
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("pat_mp_fname"), " 1234567890");
			Thread.sleep(1000);
			pu.wait_find_click(objWrite.WriteObjectRepo("pat_mp_reset"));
			assertTrue(selenium.getValue(objWrite.WriteObjectRepo("pat_mp_fname")).equalsIgnoreCase(fname));
			System.out.println("********** Reset Working **********");
			Thread.sleep(1000);
		}
		
		@Parameters({"url"})
		@Test(priority=4)
		public void logOut(String url) throws InterruptedException {
			driver.findElement(By.partialLinkText("Welcome")).click();
			driver.findElement(By.linkText("Logout")).click();
			Thread.sleep(1000);
			if( pu.isAlertPresent()){}
		 	else{}
			//driver.switchTo().alert();
			Thread.sleep(1000);
			//driver.switchTo().alert().accept();
			System.out.println("********** Logged Out Successfully **********");
		}
}
