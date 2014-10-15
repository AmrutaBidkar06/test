package superAdmin;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javatemp.objread;
import javatemp.objwrite;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import utility.PageUtils;

import com.thoughtworks.selenium.Selenium;

public class Reports {

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
			new WebDriverWait(driver, 300);
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
		if(selenium.isTextPresent("Do you have any health related questions?"))
		{
		driver.findElement(By.linkText("Existing user Login here")).click();
		}
		else{
			System.out.println("already on login page");
		}
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"),objRead.ReadObjectRepo("su_email"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"),objRead.ReadObjectRepo("su_password"));
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
	@Test(priority=1)//(enabled=false)
	public void testReportsLinkFunct(String url) throws InterruptedException{
		driver.findElement(By.linkText("Reports")).click();
		Thread.sleep(500);
		System.out.println("****** Reports link Present ******");
		//pu.assert_text("Reports");
		
	}
	
	@Parameters({"url"})
	@Test(priority=2)//(enabled=false)
	public void testSearchDoctor(String url) throws InterruptedException{
	//	testReportsLinkFunct(url);
		selenium.type(objWrite.WriteObjectRepo("report_search"), objRead.ReadObjectRepo("report_text"));
		pu.wait_find_click(objWrite.WriteObjectRepo("report_search_button"));
		if(selenium.isTextPresent(objRead.ReadObjectRepo("no_record"))){
			System.out.println("No record found");
		}
		else{
			assertTrue(selenium.getText(objWrite.WriteObjectRepo("report_table")).contains(objRead.ReadObjectRepo("report_text")));
		}
		Thread.sleep(1000);
		System.out.println("****** Reports Doctor Present ******");
	 }
	
	
	@Parameters({"url"})
	@Test(priority=3)//, enabled=false)
	public void testAppointmentsClickedCount(String url) throws InterruptedException{
		String bcount = null, acount = null;
		driver.findElement(By.linkText("Reports")).click();
		Thread.sleep(500);
		selenium.type(objWrite.WriteObjectRepo("report_search"), objRead.ReadObjectRepo("report_text"));
		pu.wait_find_click(objWrite.WriteObjectRepo("report_search_button"));
		
		List <WebElement> names=driver.findElements(By.xpath(objWrite.WriteObjectRepo("report_table")));
		// System.out.println("1");
		for(int i=1;i<=names.size();i++){
			if(selenium.getText(objWrite.WriteObjectRepo("report_app_name")).contains(objRead.ReadObjectRepo("report_text")));
		//	 System.out.println("2");
			  bcount=selenium.getText(objWrite.WriteObjectRepo("report_app_booked"));
			  if(bcount==null){
				  System.out.println("No appointments booked");
				  
			  }
			 System.out.println("bcount is : "+ bcount);
			// System.out.println("3");
			 
		login(objRead.ReadObjectRepo("report_login"),objRead.ReadObjectRepo("report_password"));
		driver.findElement(By.xpath("//*[@id='liFindDoc']/a")).click();
		//driver.navigate().to(url+"patient/finddoctor.aspx");
		
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("report_search"),objRead.ReadObjectRepo("report_text"));
		Thread.sleep(1000);
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("fd_search"))).click();
		Thread.sleep(1000);
		String winHandleBefore=driver.getWindowHandle();
		driver.findElement(By.linkText("Book Appointment")).click();
		Thread.sleep(1000);
        for(String winHandle : driver.getWindowHandles()){
            driver.switchTo().window(winHandle);
            System.out.println("Clicked Book appointment button");
        }
		
        driver.switchTo().window(winHandleBefore);
        System.out.println("Again On Mirai consult...");
		login(objRead.ReadObjectRepo("su_email"),objRead.ReadObjectRepo("su_password"));
		
		driver.findElement(By.linkText("Reports")).click();
		Thread.sleep(500);
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("report_search"), objRead.ReadObjectRepo("report_text"));
		pu.wait_find_click(objWrite.WriteObjectRepo("report_search_button"));

		for(i=1;i<=names.size();i++){
			if(selenium.getText(objWrite.WriteObjectRepo("report_app_name")).contains(objRead.ReadObjectRepo("report_text")));
			 acount=selenium.getText(objWrite.WriteObjectRepo("report_app_booked"));
			 System.out.println(acount +" Acount value");
			 break;
		 }
		
		 int acountint=Integer.parseInt(acount);
		 if(bcount==""){
			
			 assertTrue(acountint==1);
			 System.out.println("Appointments clicked count has been incremented");
			 break;
			
		 }
		 else{
			 
		 int bcountint=Integer.parseInt(bcount);
		 assertTrue(acountint==bcountint+1);
		 System.out.println("Appointments clicked count has been incremented");
		 break;
		 }
		}
		Thread.sleep(1000);
//		driver.findElement(By.partialLinkText("Welcome")).click();
//		driver.findElement(By.linkText("Logout")).click();
//		driver.switchTo().alert();
//		driver.switchTo().alert().accept();
//		Thread.sleep(3000);
	}
	
	public void login(String email, String password) throws InterruptedException{
		System.out.println("Inside login function");
		driver.findElement(By.partialLinkText("Welcome")).click();
		driver.findElement(By.linkText("Logout")).click();
		Thread.sleep(2000);
		//System.out.println(email);
		if(pu.isAlertPresent()){
			//driver.switchTo().alert();
			//driver.switchTo().alert().accept();
			}
		else 
		{
			//System.out.println("N0 alert present");
		}
		Thread.sleep(2000);
	//	if(wait.until(ExpectedConditions.alertIsPresent())!=null){
	/*	if(selenium.isTextPresent("Login"))
		{
		driver.findElement(By.linkText("Login")).click();
		}
		else{
			System.out.println("already on login page");
		}
		*/
		System.out.println("Inside login function after logout");
		//Thread.sleep(2000);
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"), email);
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"), password);
		pu.wait_find_click(objWrite.WriteObjectRepo("lp_signin"));
		Thread.sleep(5000);
	}
	
	
//	public boolean isAlertPresent() {
//
//		  boolean presentFlag = false;
//
//		  try {
//
//		   // Check the presence of alert
//		   Alert alert = driver.switchTo().alert();
//		   // Alert present; set the flag
//		   presentFlag = true;
//		   // if present consume the alert
//		   alert.accept();
//
//		  } catch (NoAlertPresentException ex) {
//		   // Alert not present
//		   ex.printStackTrace();
//		  }
//
//		  return presentFlag;
//
//		 }
//	@Parameters({"url"})
//	@Test(priority=4)
//	public void logOut(String url) throws InterruptedException {
//		driver.findElement(By.partialLinkText("Welcome")).click();
//		driver.findElement(By.linkText("Logout")).click();
//		Thread.sleep(1000);
//		if( pu.isAlertPresent())
//	 	   {
//			driver.switchTo().alert().accept();
//	 	   //System.out.println("Alert present");
//	 	   }
//	       	else
//	       	{
//	       		System.out.println("No alert present");
//	       	}
//		//driver.switchTo().alert();
//		Thread.sleep(1000);
//		//driver.switchTo().alert().accept();
//		System.out.println("********** Logged Out Successfully **********");
//	}
//	
}