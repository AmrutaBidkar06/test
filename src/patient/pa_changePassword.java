package patient;

import java.io.File;
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

public class pa_changePassword {

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
		public void setup(String browser, String url) throws Exception
		{

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
			Thread.sleep(1000);
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
		@Test(priority=1)
		public void testChangePassword_LinkFunct(String url) throws InterruptedException{
			driver.findElement(By.partialLinkText("Welcome")).click();
			Thread.sleep(1000);
			driver.findElement(By.linkText("Change Password")).click();
			//pu.assert_text("Change Password");
			System.out.println("********** Change Password Present **********");
		}
		
		@Parameters({"url"})
		@Test(priority=2)
		public void testchangePassword_MandatoryFields(String url) throws InterruptedException{
			driver.findElement(By.partialLinkText("Welcome")).click();
			Thread.sleep(1000);
			driver.findElement(By.linkText("Change Password")).click();
			Thread.sleep(1000);
			pu.wait_find_click(objWrite.WriteObjectRepo("doc_cp_submit"));
			Thread.sleep(1000);
			pu.assert_text(objRead.ReadObjectRepo("doc_curr"));
			pu.assert_text(objRead.ReadObjectRepo("doc_pass1"));
			pu.assert_text(objRead.ReadObjectRepo("doc_pass2"));
			Thread.sleep(1000);
			System.out.println("********** Change Password Mandatory Fields **********");
		}
		
		@Parameters({"url"})
		@Test(priority=3)
		public void testchangePass_PassLength(String url) throws InterruptedException{
			driver.findElement(By.partialLinkText("Welcome")).click();
			Thread.sleep(1000);
			driver.findElement(By.linkText("Change Password")).click();
			Thread.sleep(1000);
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_curr"),objRead.ReadObjectRepo("curr_pass"));
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_new"),"123");
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_confirm"),objRead.ReadObjectRepo("confirm_pass"));
			pu.wait_find_click(objWrite.WriteObjectRepo("doc_cp_submit"));
			pu.assert_text(objRead.ReadObjectRepo("doc_pass_len"));
			Thread.sleep(1000);
			System.out.println("********** Password Length **********");
		}
		
		@Parameters({"url"})
		@Test(priority=4)
		public void testchangePass_InvalidCurrentPass(String url) throws InterruptedException{
			driver.findElement(By.partialLinkText("Welcome")).click();
			Thread.sleep(1000);
			driver.findElement(By.linkText("Change Password")).click();
			Thread.sleep(1000);
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_curr"), "676767");
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_new"),objRead.ReadObjectRepo("new_pass"));
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_confirm"),objRead.ReadObjectRepo("confirm_pass"));
			pu.wait_find_click(objWrite.WriteObjectRepo("doc_cp_submit"));
			pu.assert_text(objRead.ReadObjectRepo("doc_invalid_curr"));
			Thread.sleep(1000);
			System.out.println("********** Invalid Current Password **********");
		}
		
		@Parameters({"url"})
		@Test(priority=5)
		public void testchangepass_InvalidConfirmpass(String url) throws InterruptedException{
			driver.findElement(By.partialLinkText("Welcome")).click();
			Thread.sleep(1000);
			driver.findElement(By.linkText("Change Password")).click();
			Thread.sleep(1000);
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_curr"),objRead.ReadObjectRepo("curr_pass"));
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_new"),objRead.ReadObjectRepo("new_pass"));
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_confirm"),objRead.ReadObjectRepo("confirm_pass") + "123");
			pu.wait_find_click(objWrite.WriteObjectRepo("doc_cp_submit"));
			pu.assert_text(objRead.ReadObjectRepo("doc_pass_match"));
			Thread.sleep(1000);
			System.out.println("********** Invalid Confirm Password **********");
	    }
		
		@Parameters({"url"})
		@Test(priority=6)
		public void testchangepass_CurrentNewPassSame(String url) throws InterruptedException{
			driver.findElement(By.partialLinkText("Welcome")).click();
			Thread.sleep(1000);
			driver.findElement(By.linkText("Change Password")).click();
			Thread.sleep(1000);
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_curr"),objRead.ReadObjectRepo("curr_pass"));
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_new"),objRead.ReadObjectRepo("curr_pass"));
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_confirm"),objRead.ReadObjectRepo("curr_pass"));
			pu.wait_find_click(objWrite.WriteObjectRepo("doc_cp_submit"));
			pu.assert_text(objRead.ReadObjectRepo("doc_pass_same"));
			Thread.sleep(1000);
			System.out.println("********** Current New Password Same **********");
	    }
		
		@Parameters({"url"})
		@Test(priority=7)
		public void testchangePass_Valid(String url) throws InterruptedException{
			driver.findElement(By.partialLinkText("Welcome")).click();
			Thread.sleep(1000);
			driver.findElement(By.linkText("Change Password")).click();
			Thread.sleep(1000);
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_curr"),objRead.ReadObjectRepo("curr_pass"));
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_new"),objRead.ReadObjectRepo("new_pass"));
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_confirm"),objRead.ReadObjectRepo("confirm_pass"));
			pu.wait_find_click(objWrite.WriteObjectRepo("doc_cp_submit"));
			pu.assert_text(objRead.ReadObjectRepo("doc_pass_change"));
			Thread.sleep(1000);
			
			//reset again
			driver.findElement(By.partialLinkText("Welcome")).click();
			Thread.sleep(1000);
			driver.findElement(By.linkText("Change Password")).click();
			Thread.sleep(1000);
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_curr"),objRead.ReadObjectRepo("new_pass"));
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_new"),objRead.ReadObjectRepo("curr_pass"));
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_cp_confirm"),objRead.ReadObjectRepo("curr_pass"));
			pu.wait_find_click(objWrite.WriteObjectRepo("doc_cp_submit"));
			pu.assert_text(objRead.ReadObjectRepo("doc_pass_change"));
			Thread.sleep(1000);
			System.out.println("********** Password Change Working **********");
		}
		
		@Parameters({"url"})
		@Test(priority=8)
		public void logOut(String url) throws InterruptedException {
			driver.findElement(By.partialLinkText("Welcome")).click();
			driver.findElement(By.linkText("Logout")).click();
			Thread.sleep(2000);
			if( pu.isAlertPresent())
		 	   {
				//driver.switchTo().alert().accept();
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
	
	
