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

public class signupPatient {

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

	public void RandomString() {
		Random r = new Random(); // just create one and keep it around
		String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890";

		final int N = 5;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
		}
		randomName = sb.toString();

		// System.out.println(randomName);
	}

	@Parameters({"url"})
	@Test
	public void testsignUPPatient(String url) throws InterruptedException{
		RandomString();
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("signup_link"))).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("signup_patient_link"))).click();
		Thread.sleep(1000);
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("signup_patient_fname"),objRead.ReadObjectRepo("signup_patient_fname"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("signup_patient_lname"),objRead.ReadObjectRepo("signup_patient_lname"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("signup_patient_email"), randomName	+ "@mailinator.com");
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("signup_patient_mobno"),objRead.ReadObjectRepo("signup_patient_mobno"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("signup_patient_pass"),objRead.ReadObjectRepo("signup_patient_pass"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("signup_patient_cpass"),objRead.ReadObjectRepo("signup_patient_cpass"));
		pu.wait_find_click(objWrite.WriteObjectRepo("signup_patient_submit"));
		Thread.sleep(2000);
		pu.assert_text(objRead.ReadObjectRepo("signup_patient_success"));
		
		pu.VerifyEmailMailinator(url, randomName);
		
		driver.findElement(By.partialLinkText("Mirai Consult")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='mailshowdivbody']//iframe")));
		String winHandleBefore=driver.getWindowHandle();
		Thread.sleep(2000);
		//pu.wait_find_click("html/body/div[1]/div/div/p[4]/font/a");
		String verifyurl=selenium.getText("//html/body/div[1]/div/div/p[4]/font/a");
		driver.get(verifyurl);
		Thread.sleep(2000);
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}

		pu.assert_text(objRead.ReadObjectRepo("patient_linked_Clicked_success"));
		
		driver.switchTo().window(winHandleBefore);
		driver.navigate().to(url);
		if(selenium.isTextPresent("Ask Doctors"))
		{
		driver.findElement(By.linkText("Login")).click();
		}
		else{
			System.out.println("already on login page");
		}
		pu.wait_find_clear(objWrite.WriteObjectRepo("lp_email"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"), randomName+"@mailinator.com");
		pu.wait_find_clear(objWrite.WriteObjectRepo("lp_password"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"), objRead.ReadObjectRepo("signup_patient_pass"));
		pu.wait_find_click(objWrite.WriteObjectRepo("lp_signin"));
		Thread.sleep(2000);
		driver.findElement(By.partialLinkText("Welcome")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.switchTo().alert();
		driver.switchTo().alert().accept();
		System.out.println("************* Registered patient login successful ************");

	}




}
