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
import org.openqa.selenium.WebElement;
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

public class doc_Questions {

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
		String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890";

		final int N = 5;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
		}
		randomName = sb.toString();
	}
	

	@Parameters({"url"})
	@Test(priority=1)
	public void testQuestionsoptionFunct(String url) throws InterruptedException{
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_que_link"))).click();
		Thread.sleep(1000);
		System.out.println("********** Question Link Present **********");
	}
	
	@Parameters({"url"})
	@Test(priority=2)
	public void testquestFilter(String url) throws InterruptedException{
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_que_link"))).click();
		Thread.sleep(1000);
		selenium.check(objWrite.WriteObjectRepo("doc_que_all"));
		Thread.sleep(1000);
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("doc_1st_que"))){
			System.out.println("********** Questions present in all questions **********");
		}
		else{
			System.out.println("********** No questions in all fields **********");
		}
		
		selenium.check(objWrite.WriteObjectRepo("doc_que_unans"));
		Thread.sleep(1000);
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("doc_1st_que"))){
			System.out.println("********** Questions present in unanswered questions **********");
		}
		else{
			System.out.println("********** No questions in unanswered field **********");
		}
		
	}
	
	
	@Parameters({"url"})
	@Test(priority=3)
	public void testquestsLinkfunct(String url) throws InterruptedException{
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_que_link"))).click();
		Thread.sleep(1000);
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("doc_1st_que"))){
			//System.out.println("Questions present in all questions");
			pu.wait_find_click(objWrite.WriteObjectRepo("doc_1st_que"));
			Thread.sleep(1000);
			//assertTrue(selenium.isElementPresent("//*[@id='aspnetForm']/div[4]/div/div[2]/div/div[4]/div/button"));
			if(driver.getCurrentUrl().contains("questions/")){
			System.out.println("********** On question details page **********"); }
			else{
				System.out.println("********** Not on questions page **********");
				}
		}
		else{
			System.out.println("********** There are no questions **********");
		}
	}
	
	
    @Parameters({"url"})
	@Test(priority=4)
	public void testUnanwseredQuestsMandatoryFields(String url) throws InterruptedException{
	driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_que_link"))).click();
	Thread.sleep(1000);
	selenium.check(objWrite.WriteObjectRepo("doc_que_unans"));
	Thread.sleep(1000);
	if(selenium.isElementPresent(objWrite.WriteObjectRepo("doc_1st_que"))){
		//	System.out.println("Questions present in unanswered questions");
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_1st_que"));
		Thread.sleep(1000);
		pu.wait_find_click(objWrite.WriteObjectRepo("doc_ans_submit"));
		Thread.sleep(1000);
		pu.assert_text(objRead.ReadObjectRepo("enter_ans"));
		System.out.println("********** Answer Mandatory fields working **********");	
		}
	else {
		System.out.println("No unanswered questions");
		}
	}
	
   	@Parameters({"url"})
	@Test(priority=5)
	public void testClearFields(String url) throws InterruptedException{
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_que_link"))).click();
		Thread.sleep(1000);
		RandomString();
		 selenium.check(objWrite.WriteObjectRepo("doc_que_unans"));
			Thread.sleep(1000);
			if(selenium.isElementPresent(objWrite.WriteObjectRepo("doc_1st_que"))){
				pu.wait_find_click(objWrite.WriteObjectRepo("doc_1st_que"));
				Thread.sleep(1000);
				pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_ans_title"), "Automated Answer");
				pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_ans_answer"), "This is Automated Answer" + randomName);
				driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_ans_browser"))).sendKeys("C:\\Users\\Amruta\\Desktop\\heart.jpeg");
				Thread.sleep(1000);
				pu.wait_find_click(objWrite.WriteObjectRepo("doc_ans_clear"));
				Thread.sleep(1000);
				assertTrue(selenium.getText(objWrite.WriteObjectRepo("doc_ans_title")).equalsIgnoreCase(""));
				assertTrue(selenium.getText(objWrite.WriteObjectRepo("doc_ans_answer")).equalsIgnoreCase(""));
				System.out.println("********** Successfully cleared the answer **********");	
			}
			else {
				System.out.println("No unanswered questions");
				}	//type the path of the file which is to be uploaded
}

	
	@Parameters({"url"})
	@Test(priority=6)
	public void testUploadInvalidFile(String url) throws InterruptedException{
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_que_link"))).click();
		Thread.sleep(1000);
		selenium.check(objWrite.WriteObjectRepo("doc_que_unans"));
		Thread.sleep(1000);
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("doc_1st_que"))){
			pu.wait_find_click(objWrite.WriteObjectRepo("doc_1st_que"));
			Thread.sleep(1000);
			pu.wait_find_click(objWrite.WriteObjectRepo("doc_ans_submit"));
			Thread.sleep(1000);
			WebElement fileInput = driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_ans_browser")));
			Thread.sleep(1000);
			
			//type the path of the file which is to be uploaded
			
		    fileInput.sendKeys("C:\\Users\\Amruta\\Desktop\\Today.txt");
		    Thread.sleep(1000);
		    System.out.println("********** Selected file **********");
			Thread.sleep(1000);
			pu.assert_text("Supports only .jpg,.png,.gif,.jpeg formats. Please upload a file with either of these extensions.");
			System.out.println("********** File upload error working working **********");	
		}
		else {
			System.out.println("No unanswered questions");
			}
	}
	
	 
	@Parameters({"url"})
	@Test(priority=7)
	public void testAnswerWithValidFile(String url) throws InterruptedException{
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_que_link"))).click();
		Thread.sleep(1000);
		RandomString();
		 selenium.check(objWrite.WriteObjectRepo("doc_que_unans"));
			Thread.sleep(1000);
			if(selenium.isElementPresent(objWrite.WriteObjectRepo("doc_1st_que"))){
				pu.wait_find_click(objWrite.WriteObjectRepo("doc_1st_que"));
				Thread.sleep(1000);
				pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_ans_title"), "Automated Answer");
				pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_ans_answer"), "This is Automated Answer" + randomName );
				WebElement fileInput = driver.findElement(By.xpath(objWrite.WriteObjectRepo("doc_ans_browser")));
				fileInput.sendKeys("C:\\Users\\Amruta\\Desktop\\heart.jpeg");
				pu.wait_find_click(objWrite.WriteObjectRepo("doc_ans_submit"));
				Thread.sleep(2000);
				pu.assert_text("Thank you for answering the question.");
				System.out.println("********** Successfully answered a question **********");	
			}
			else {
				System.out.println("No unanswered questions");
				}
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
	