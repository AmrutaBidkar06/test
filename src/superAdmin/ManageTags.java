package superAdmin;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javatemp.objread;
import javatemp.objwrite;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class ManageTags {

	// declaring the selenium object
	private Selenium selenium;
	
	// declaring the webdriver object
	private WebDriver driver = null;
	
	private PageUtils pu = null;
	String url, randomName;
	private String tag=null;
	
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
		if(selenium.isTextPresent("Do you have any health related questions?"))
		{
		driver.findElement(By.linkText("Login")).click();
		}
		else{
			System.out.println("already on login page");
		}
		pu = PageUtils.getInstance(selenium, driver, url);
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"),objRead.ReadObjectRepo("lp_email"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"),objRead.ReadObjectRepo("lp_password"));
		//pu.wait_find_send_keys("//*[@id='ctl00_MainPage_txtPassword']", "");
		//pu.wait_find_click("//*[@id='ctl00_MainPage_btnLogin']");
		pu.wait_find_click(objWrite.WriteObjectRepo("lp_signin"));
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
    public void testManageTagsfunct(String url) throws InterruptedException{
		driver.findElement(By.linkText("Tags")).click();
		//pu.assert_text("Invite your friends");
		Thread.sleep(2000);
		System.out.println("****** Manage Tag link Present ******");
		//pu.assert_text("Manage tags");
	}
	
	@Parameters({"url"})
    @Test(priority=2)
    public void testAddNewTag(String url) throws InterruptedException{
		RandomString();
		testManageTagsfunct(url);
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("mt_new_tag"),"Auto"+randomName);
		Thread.sleep(3000);
		tag = selenium.getValue(objWrite.WriteObjectRepo("mt_new_tag"));
		
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("mt_new_tag"))).sendKeys(Keys.RETURN);
		
	}
	
	

	@Parameters({"url"}) 
    @Test(priority=3)
	public void testSearchTag(String url) throws InterruptedException{
		//testAddNewTag(url);
		//pu.wait_find_send_keys(objWrite.WriteObjectRepo("mt_new_tag"),"");
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("mt_search_tag"), tag);
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("mt_search_tag"))).sendKeys(Keys.RETURN);
		Thread.sleep(5000);
		System.out.println("the tag is:"+tag);
		assertTrue(driver.findElement(By.xpath(objWrite.WriteObjectRepo("mt_element")+ ","+tag+")]")).isDisplayed());
        System.out.println("Search successful");
        driver.findElement(By.xpath(objWrite.WriteObjectRepo("mt_new_tag"))).clear();
        driver.findElement(By.xpath(objWrite.WriteObjectRepo("mt_search_tag"))).clear();
	}
	
	@Parameters({"url"}) 
    @Test(priority=4)
	public void testExistingTag(String url) throws InterruptedException{
		//testAddNewTag(url);
	
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("mt_new_tag"),tag);
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("mt_new_tag"))).sendKeys(Keys.RETURN);
		Thread.sleep(2000);
		String alert_text = driver.switchTo().alert().getText();
		driver.switchTo().alert().accept();
		if (alert_text.equals(objRead.ReadObjectRepo("mt_tag_text"))){
		System.out.println("Duplicate tag not inserted");
		}
		else {
			System.out.println("Duplicate tag inserted");
		}
	}
	
	@Parameters({"url"})
    @Test(priority=5)
	public void testDeleteTag(String url) throws InterruptedException{
		testSearchTag(url);
		pu.wait_find_click(objWrite.WriteObjectRepo("mt_delete_tag"));
		driver.switchTo().alert();
		Thread.sleep(2000);
		driver.switchTo().alert().accept();
		driver.switchTo().alert();
		Thread.sleep(2000);
		driver.switchTo().alert().accept();
		Thread.sleep(3000);
		driver.findElement(By.partialLinkText("Welcome")).click();
		driver.findElement(By.linkText("Logout")).click();
		Thread.sleep(3000);
		driver.switchTo().alert();
		driver.switchTo().alert().accept();
		System.out.println("Deleted tag successfully");
	}
	
	
}