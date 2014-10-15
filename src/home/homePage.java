package home;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javatemp.LinkNotFound;
import javatemp.objread;
import javatemp.objwrite;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import utility.PageUtils;

import com.thoughtworks.selenium.Selenium;

public class homePage {

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
	@Test(priority=1)
	public void testGlobalSearch(String url) throws InterruptedException, LinkNotFound{
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("hp_search_box"),objRead.ReadObjectRepo("hp_search_key")); 
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("hp_send_button"))).click();
		Thread.sleep(1000);
		if(driver.getCurrentUrl().contains(url + "similar-questions?question=")){
			System.out.println("*********** Search Successful***********");
		}
		else{
			System.out.println("*********** \"Global Search\" Not Working ***********");	
			throw new LinkNotFound();
		}
	}
	
	@Parameters({"url"})
	@Test(priority=2)//(enabled=false)
	public void testTopicSearch_link(String url) throws InterruptedException,LinkNotFound
	{
		driver.navigate().to(url);
		int i=0;
		pu.wait_find_click(objWrite.WriteObjectRepo("hp_topic_VM"));
		Thread.sleep(1000);
		//assertTrue(driver.getCurrentUrl().contains(url + "topics"));
		if(driver.getCurrentUrl().contains(url + "topics")){
		System.out.println("*********** View More success***********");
		}
		else{
			System.out.println("*********** View More Failed***********");	
			throw new LinkNotFound();
		}
		Actions action = new Actions(driver);
		WebElement we = driver.findElement(By.xpath(objWrite.WriteObjectRepo("hp_tag")));
		action.moveToElement(we).build().perform();
		Thread.sleep(1000);
		String tag=we.getText();
		//System.out.println(we.getText());
		String ans=we.getAttribute("title");
		//System.out.println(we.getAttribute("title"));
		if(ans.contains(tag + " 0 ")){
			System.out.println("*********** 0 answers ***********");
			
		}
		else{
			System.out.println("*********** More Answers ***********");
			i=1;
		}
		
		we.click();
		Thread.sleep(1000);
		
		if(driver.getCurrentUrl().contains(url + "topics/" + tag.toLowerCase())){
					if(i==0){
					if(selenium.isTextPresent("No records found")){
						System.out.println("*********** O Answers success ***********");
					}
					else{
						System.out.println("*********** O Answer fail ***********");
					}
				}
			else{
					pu.wait_find_click(objWrite.WriteObjectRepo("tag_que"));
					if(selenium.isElementPresent(objWrite.WriteObjectRepo("tag_que_attribute"))){
						System.out.println("*********** On Answers Page, more answer success ***********");
					}
					else{
						System.out.println("*********** More answer fail ***********");
					}
			}
			Thread.sleep(1000);
		}
		else{
			System.out.println("*********** Not redirected to \"" + tag + "\" tag detailed page ***********");
		}
	}
	
	@Parameters({"url"})
	@Test(priority=3)//(enabled=false)
	public void testContactUs(String url) throws InterruptedException
	{
		driver.navigate().to(url);
		String winHandleBefore = driver.getWindowHandle();
		driver.findElement(By.linkText("Contact Us")).click();
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
		pu.assert_text("Contact Us ");
		driver.findElement(By.id(objWrite.WriteObjectRepo("contact_us_name"))).sendKeys(objRead.ReadObjectRepo("contact_us_name"));
		driver.findElement(By.id(objWrite.WriteObjectRepo("contact_us_email"))).sendKeys(objRead.ReadObjectRepo("contact_us_email"));
		driver.findElement(By.id(objWrite.WriteObjectRepo("contact_us_message"))).sendKeys(objRead.ReadObjectRepo("contact_us_message"));
		driver.findElement(By.id(objWrite.WriteObjectRepo("contact_us_send_button"))).click();
		pu.assert_text("Thank you for contacting us.");
		driver.switchTo().window(winHandleBefore);
		System.out.println("Contact US Successful");
	}
	
	
	@Parameters({"url"})
	@Test(priority = 4)
	public void testWhoWeAre(String url) throws InterruptedException, LinkNotFound{
		driver.findElement(By.linkText("Who we are")).click();
		Thread.sleep(1000);
		String winHandleBefore = driver.getWindowHandle();
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
		if(driver.getCurrentUrl().contains("http://www.miraihealth.com/")){
			System.out.println("*********** Success Who We Are***********");
		}
		else{
			System.out.println("*********** \"Who we Are\" link Not Found ***********");	
			throw new LinkNotFound();
		}
		driver.switchTo().window(winHandleBefore);
		Thread.sleep(1000);
	}
	
	@Parameters({"url"})
	@Test(priority = 5)
	public void testHowWeWork(String url) throws InterruptedException, LinkNotFound{
		driver.navigate().to(url);
		driver.findElement(By.linkText("How we work")).click();
		Thread.sleep(1000);
		if(driver.getCurrentUrl().contains(url + "how-we-works")){
			System.out.println("*********** Success How We Work ***********");
		}
		else{
			System.out.println("*********** \"How We Work\" link Not Found ***********");	
			throw new LinkNotFound();
		}
	}
	
	
	@Parameters({"url"})
	@Test(priority = 6)
	public void testprivacypolicy(String url) throws InterruptedException, LinkNotFound
	{
		driver.navigate().to(url);
		driver.findElement(By.linkText("Privacy Policy")).click();
		Thread.sleep(1000);
		String winHandleBefore = driver.getWindowHandle();
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
		if(driver.getCurrentUrl().contains(url + "privacy-policy")){
			System.out.println("*********** Success Privacy Policy ***********");
		}
		else{
			System.out.println("*********** \"Privacy Policy\" link Not Found ***********");	
			throw new LinkNotFound();
		}
		driver.switchTo().window(winHandleBefore);
		Thread.sleep(1000);
	}
	@Parameters({"url"})
	@Test(priority = 7)
	public void testTermsAndCondotions(String url) throws InterruptedException, LinkNotFound
	{
		driver.navigate().to(url);
		driver.findElement(By.linkText("Terms Of Use")).click();
		Thread.sleep(1000);
		String winHandleBefore = driver.getWindowHandle();
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
		if(driver.getCurrentUrl().contains(url + "terms-of-use")){
			System.out.println("*********** Success Terms Of Use ***********");
		}
		else{
			System.out.println("*********** \"Terms of Use\" link Not Found ***********");	
			throw new LinkNotFound();
		}
		driver.switchTo().window(winHandleBefore);
		Thread.sleep(1000);
	}
	
//
//	@Parameters({"url"})
//	@Test(priority=)
//	public void testOurDoctors(String url) throws InterruptedException, LinkNotFound{
//		driver.navigate().to(url);
//		driver.findElement(By.linkText("Our Doctors")).click();
//		Thread.sleep(1000);
//		if(driver.getCurrentUrl().contains(url + "find-doctors")){
//			System.out.println("*********** Success Our Doctors ***********");
//		}
//		else{
//			System.out.println("*********** \"Our Doctor\" link Not Found ***********");	
//			throw new LinkNotFound();
//		}
//	}
//	
	
	
}