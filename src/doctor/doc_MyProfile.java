package doctor;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javatemp.objread;
import javatemp.objwrite;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
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
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import utility.PageUtils;

import com.thoughtworks.selenium.Selenium;

public class doc_MyProfile {

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
	}
	
	@Parameters({"url"})
	@Test(priority=1)
	public void testMyProfile_LinkFunct(String url) throws InterruptedException{
		driver.findElement(By.partialLinkText("Welcome")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("My Profile")).click();
		Thread.sleep(1000);
		System.out.println("********** My Profile Link Present **********");
	}
	
	@Parameters({"url"})
	@Test(priority=2)
	public void testMyProfile_SaveButtonFunct(String url) throws InterruptedException{
		driver.findElement(By.partialLinkText("Welcome")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("My Profile")).click();
		Thread.sleep(1000);
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_mp_submit"));
		pu.assert_text(objRead.ReadObjectRepo("doc_mp_success"));
		System.out.println("********** My Profile Updated **********");
	}
	
	@Parameters({"url"})
	@Test(priority=3)
	public void testMyProfile_ResetButtonFunct(String url) throws InterruptedException{
		driver.findElement(By.partialLinkText("Welcome")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("My Profile")).click();
		Thread.sleep(1000);
		String fname=selenium.getValue(objWrite.WriteObjectRepo("doc_mp_fname"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_mp_fname"), "clear");
		Thread.sleep(1000);
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_mp_reset"));
		assertTrue(selenium.getValue(objWrite.WriteObjectRepo("doc_mp_fname")).equalsIgnoreCase(fname));
		System.out.println("********** Reset Working **********");
	}
	
	@Parameters({"url"})
	@Test(priority=4)
	public void testAddQualifictationDetails(String url) throws InterruptedException{
		driver.findElement(By.partialLinkText("Welcome")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("My Profile")).click();
		Thread.sleep(1000);
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_mp_degree_add"));
		Thread.sleep(1000);
		Select degree= new Select(driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_mp_degree_select"))));
		degree.selectByVisibleText("B.H.M.S.");
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_mp_degree_save"));
		System.out.println("********** Degree Added **********");
		Thread.sleep(2000);
		
		//delete the degree
		WebElement Webtable=driver.findElement(By.id("Education-list")); // Replace TableID with Actual Table ID or Xpath
		List<WebElement> TotalRowCount=Webtable.findElements(By.xpath("//*[@id='Education-list']/tbody/tr"));
		
		int i1=TotalRowCount.size();
		for(int i=1;i<=i1;i++)
		{
			if(selenium.getText(objWrite.WriteObjectRepo("doc_mp_degree_loop") + i+"]/td[1]").contains("B.H.M.S.")){
				pu.wait_find_click(objWrite.WriteObjectRepo("doc_mp_degree_loop") + i+"]/td[4]/a");
				Thread.sleep(2000);
				driver.switchTo().alert();
				driver.switchTo().alert().accept();
				Thread.sleep(1000);
				driver.switchTo().alert();
				driver.switchTo().alert().accept();
				Thread.sleep(1000);
				System.out.println("********** Degree Deleted **********");
					break;
				}
				else{	}
		}
		System.out.println("********** Degree Add/Delete Working **********");
	}
	
	
	@Parameters({"url"})
	@Test(priority=5)
	public void testAddClinicDetails(String url) throws InterruptedException{
		driver.findElement(By.partialLinkText("Welcome")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("My Profile")).click();
		Thread.sleep(1000);
		RandomString();
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_mp_clinic_add"));
		Select country= new Select(driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_mp_clinic_country"))));
		country.selectByVisibleText("India");
		Select state= new Select(driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_mp_clinic_state"))));
		state.selectByVisibleText("Maharashtra");
		Select city= new Select(driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_mp_clinic_city"))));
		city.selectByVisibleText("Pune");
		Select location= new Select(driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_mp_clinic_location"))));
		location.selectByVisibleText("Aundh");
		pu.wait_find_send_keys( objWrite.WriteObjectRepo("doc_mp_clinic_name"), randomName);
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_mp_clinic_save"));
		Thread.sleep(1000);
		pu.assert_text(objRead.ReadObjectRepo("doc_mp_clinic"));
		System.out.println("********** Clinic Added **********");
	
		//delete the location
		
		WebElement Webtable=driver.findElement(By.id("Location-List")); // Replace TableID with Actual Table ID or Xpath
		List<WebElement> TotalRowCount=Webtable.findElements(By.xpath("//*[@id='Location-List']/tbody/tr"));
				
		int i1=TotalRowCount.size();
		for(int i=1;i<=i1;i++)
			{
				if(selenium.getText(objWrite.WriteObjectRepo("doc_mp_clinic_loop") + i+"]/td[4]").contains(randomName)){
					pu.wait_find_click(objWrite.WriteObjectRepo("doc_mp_clinic_loop") + i+"]/td[8]/a");
					Thread.sleep(2000);
					driver.switchTo().alert();
					driver.switchTo().alert().accept();
					Thread.sleep(1000);
					driver.switchTo().alert();
					driver.switchTo().alert().accept();
					Thread.sleep(1000);
					System.out.println("********** Clinic Deleted **********");
					break;
				}
				else{}
			}
		System.out.println("********** Clinic Add/Delete Working **********");
	}
	
	
	@Parameters({"url"})
	@Test(priority=6)
	public void testAddOtherDetails(String url) throws InterruptedException{
		String winHandleBefore=driver.getWindowHandle();
		driver.findElement(By.partialLinkText("Welcome")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("My Profile")).click();
		Thread.sleep(1000);
		RandomString();
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_mp_other_add"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_mp_other_certification"), randomName);
		Thread.sleep(1000);
        pu.wait_find_click(objWrite.WriteObjectRepo("doc_mp_other_save"));
        //handled modal dialog box
        Alert alt=driver.switchTo().alert();
	    System.out.println(alt.getText());
	    alt.accept();
	   
	    System.out.println("********** Other Detials Added **********");
	
		//delete the location

		WebElement Webtable=driver.findElement(By.id("OtherDetail-List")); // Replace TableID with Actual Table ID or Xpath
		List<WebElement> TotalRowCount=Webtable.findElements(By.xpath("//*[@id='OtherDetail-List']/tbody/tr"));
		
		int i1=TotalRowCount.size();
		for(int i=1;i<=i1;i++)
		{
			if(selenium.getText(objWrite.WriteObjectRepo("doc_mp_other_loop") + i+"]/td[1]").contains(randomName)){
				pu.wait_find_click(objWrite.WriteObjectRepo("doc_mp_other_loop") + i+"]/td[4]/a");
				Thread.sleep(2000);
				Alert alt1=driver.switchTo().alert();
				alt1.accept();
				Alert alt2=driver.switchTo().alert();
				alt2.accept();
				System.out.println("********** Other details Deleted **********");
				break;
				}
				else{
					}
		
		}
	
		System.out.println("********** Other Details Add/Delete Working **********");
	
		driver.switchTo().window(winHandleBefore);
		logOut(url);
	}
	
	public void logOut(String url) throws InterruptedException {
		System.out.println("In logout.....");
		driver.findElement(By.partialLinkText("Welcome")).click();
		driver.findElement(By.linkText("Logout")).click();
		Thread.sleep(1000);
		if( pu.isAlertPresent())
	 	   {}
		else {}
		
		System.out.println("********** Logged Out Successfully **********");
	}
}