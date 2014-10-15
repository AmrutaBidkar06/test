package signup;

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

public class signupDoctor {

	// declaring the selenium object
	private Selenium selenium;

	// declaring the webdriver object
	private WebDriver driver = null;
	
	objread objRead = new objread();
	objwrite objWrite = new objwrite();

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
		selenium = new WebDriverBackedSelenium(driver, url);

		driver.navigate().to(url);

		driver.manage().window().maximize();
		Thread.sleep(1000);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(1000);
		pu = PageUtils.getInstance(selenium, driver, url);

	}

	@AfterClass(alwaysRun = true)
	public void StopDriver() throws Exception {
		// take the screenshot at the end of every test
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		// now save the screenshot to a file some place
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

	@Parameters({ "url" })
	@Test
	public void testSignUpDoctor(String url) throws InterruptedException {
		RandomString();
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("signup_link"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("signup_doc_link"))).click();
		Thread.sleep(1000);
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("signup_doc_fname"),objRead.ReadObjectRepo("signup_doc_fname"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("signup_doc_lname"),objRead.ReadObjectRepo("signup_doc_lname"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("signup_doc_email"), randomName	+ "@mailinator.com");
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("signup_doc_mobno"),objRead.ReadObjectRepo("signup_doc_mobno"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("signup_doc_pass"),objRead.ReadObjectRepo("signup_doc_pass"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("signup_doc_cpass"),objRead.ReadObjectRepo("signup_doc_cpass"));
		pu.wait_find_click(objWrite.WriteObjectRepo("signup_doc_submit"));
		Thread.sleep(2000);
		pu.assert_text(objRead.ReadObjectRepo("signup_success"));
		
		pu.VerifyEmailMailinator(url, randomName);
		
		driver.findElement(By.partialLinkText("Mirai Consult")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(driver.findElement(By.xpath(objWrite.WriteObjectRepo("mailinator_mailbody"))));
//		String winHandleBefore = driver.getWindowHandle();
		//pu.wait_find_click("//html/body/div[1]/div/div/p[4]/font/a");
		String verifyurl=selenium.getText("//html/body/div[1]/div/div/p[4]/font/a");
		driver.get(verifyurl);
		Thread.sleep(2000);
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}

		pu.assert_text(objRead.ReadObjectRepo("linked_Clicked_success"));
		/*
		// approve the doctor from superadmin account
		driver.switchTo().window(winHandleBefore);
		login(url,objRead.ReadObjectRepo("su_email"),objRead.ReadObjectRepo("su_password"));

		  pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_registered_checkbox"));
		   Thread.sleep(2000);
		    
		    if(driver.getPageSource().contains("No data available")){
		    	System.out.println("No registration requests");
		    	
		    }
		    else{
		    	driver.findElement(By.xpath(objWrite.WriteObjectRepo("su_list_search_box"))).sendKeys(randomName) ;
		    	Thread.sleep(1000);
		    	if(driver.getPageSource().contains("Showing 0 to 0 of 0 entries")){
			    	System.out.println("No registration requests for " + randomName );
			    	//driver.quit();
			    	//System.exit(0);
			    }
		    	else{
		    	pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_approve1"));
		    	driver.switchTo().alert();
		    	Thread.sleep(3000);
				driver.switchTo().alert().accept();
				Thread.sleep(3000);
				driver.switchTo().alert();
				Thread.sleep(3000);
				driver.switchTo().alert().accept();
				Thread.sleep(3000);
				System.out.println("********* Approve Registered Doctor Successful *********");
		    }
		}

		// log out from admin account
		logOut(url);

		// login to reg doc acc
		login(url, randomName + "@mailinator.com",objRead.ReadObjectRepo("signup_doc_pass"));
		Thread.sleep(1000);

		logOut(url);*/

	}

	public void login(String url, String email, String pass)
			throws InterruptedException {

		driver.navigate().to(url);
		if(selenium.isTextPresent("Ask Doctors"))
		{
		driver.findElement(By.linkText("Login")).click();
		}
		else{
			System.out.println("already on login page");
		}
		pu.wait_find_clear(objWrite.WriteObjectRepo("lp_email"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"), email);
		pu.wait_find_clear(objWrite.WriteObjectRepo("lp_password"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"), pass);
		pu.wait_find_click(objWrite.WriteObjectRepo("lp_signin"));
		Thread.sleep(5000);
	}

	public void logOut(String url) {
		driver.findElement(By.partialLinkText("Welcome")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.switchTo().alert();
		driver.switchTo().alert().accept();
	}

}
