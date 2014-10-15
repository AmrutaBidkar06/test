package patient;

import static org.testng.Assert.assertTrue;

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

public class pa_findDoctors {

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
	public void testfindDoctorsOption(String url){
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_finddoc_link"));
		pu.assert_ElementPresent(objWrite.WriteObjectRepo("pat_fd_search"));
		System.out.println("********** Find Doctor Link Present **********");
	}
	
	@Parameters({"url"})
	@Test(priority=2)
	public void testSearchAllDoctors(String url) throws InterruptedException{
		//pu.wait_find_click(objWrite.WriteObjectRepo("pat_finddoc_link"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_city_close"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_fd_search"));
	//	Thread.sleep(1000);
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("1st_doc"))){
			System.out.println("********** Search Result found **********");
		}
		else{
			System.out.println("********** No doctor present **********");
		}
	
	}
	
	@Parameters({"url"})
	@Test(priority=3)
	public void testsearchSpecialityDoctor(String url){
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("pat_speciality"),objRead.ReadObjectRepo("speciality"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_fd_search"));
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("1st_doc"))){
			System.out.println("********** Search successful **********");
		}
		else{
			System.out.println("********** Search Result not found **********");
		}
	
	}
	
	@Parameters({"url"})
	@Test(priority=4)
	public void testSearchDoctorByCity(String url) throws InterruptedException{
		Thread.sleep(1000);
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("pat_speciality"))).clear();
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_doc_city"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_doc_city_pune"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_fd_search"));
		Thread.sleep(1000);
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("1st_doc"))){
			System.out.println("********** Search successful **********");
		}
		else{
			System.out.println("********** Search Result not found **********");
		}
		
	}
	
	
	@Parameters({"url"})
	@Test(priority=5)
	public void testSearchDoctorByLocation(String url) throws InterruptedException{
		Thread.sleep(1000);
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_doc_loc"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_doc_loc_akurdi"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_fd_search"));
		Thread.sleep(1000);
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("1st_doc"))){
			System.out.println("********** Search successful **********");
		}
		else{
			System.out.println("********** Search Result not found **********");
		}
	}
	
	@Parameters({"url"})
	@Test(priority=6)
	public void TestSearchAllOptions(String url) throws InterruptedException{
		Thread.sleep(1000);
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("pat_speciality"))).clear();
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("pat_speciality"))).sendKeys(objRead.ReadObjectRepo("speciality"));
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_fd_search"));
		Thread.sleep(1000);
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("1st_doc"))){
			System.out.println("********** Search successful **********");
		}
		else{
			System.out.println("********** Search Result not found **********");
		}
	}
	
	@Parameters({"url"})
	@Test(priority=7)
	public void InvalidSearch(String url) throws InterruptedException{
		Thread.sleep(1000);
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("pat_speciality"))).clear();
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("pat_speciality"),"!@#$%^&*()");
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_fd_search"));
		Thread.sleep(1000);
		assertTrue(selenium.isTextPresent("No records found"));
		System.out.println("********** Invalid Search Successful  **********");
		Thread.sleep(1000);
	}
	/*
	@Parameters({"url"})
	@Test(priority=8) //Having issue with calender
	public void BookAppointment(String url) throws InterruptedException{
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("pat_speciality"))).clear();
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("pat_speciality"))).sendKeys("Neurology");
		pu.wait_find_click(objWrite.WriteObjectRepo("pat_fd_search"));
		Thread.sleep(1000);
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("1st_doc"))){
			String beforehandler=driver.getWindowHandle();
			pu.wait_find_click("//a[contains(text(),'Book Appointment')]");
			System.out.println("Clicked on Book Appointment button");
			if(selenium.isElementPresent("//*[@id='myModal']")){
				System.out.println(" ********** Can not book Appointment ********** ");
				pu.wait_find_click("//*[@id='myModal']/div/div/div[3]/button");
			}
			else{
		
	
	          for(String handlers:driver.getWindowHandles())
	          {
	        	  driver.switchTo().window(handlers);
	        	  System.out.println("Switched to connect site");
	        	  Thread.sleep(3000);
	        	  System.out.println(driver.getCurrentUrl());
	        	  Thread.sleep(500);
	         
	          }
	          String handler1=driver.getWindowHandle();
	          if(pu.isAlertPresent())
	          {
	        	  System.out.println("Inside if...");
	        	  for(String handlers:driver.getWindowHandles())
		          {
		        	  driver.switchTo().window(handlers);
		        	  System.out.println("Switched to Alert");
		        	  Thread.sleep(3000);
		        	  
		          }
	        	  
	        	  driver.switchTo().activeElement().click();
	        	  System.out.println("Accepted alert");
	        	 Thread.sleep(1000);
	        	 driver.switchTo().window(handler1);
	        	 System.out.println("No working hours present");
	          }
	          
	          else{
	        	  
	        	  System.out.println("*************");
		          assertTrue(selenium.isElementPresent("//div[@class='avtar']"));
		          System.out.println("On connect site........");
		          Thread.sleep(3000);
					//pu.wait_find_click("//*[@id='calendar']/table/tbody/tr/td[1]/span[29]");
			checkClosedDay1(url);
			//checkPastTime(url);
		    bookAppointment(url);
		          
	          }
	         System.out.println("Redirecting back to consult");
	         driver.switchTo().window(beforehandler);
		}
		}
		 
	}

	public void bookAppointment(String url) throws InterruptedException{
	     //  pu.wait_find_click("//*[@id='calendar']/div/div/div/div/div/table/tbody/tr[1]/td[1]");
		   pu.wait_find_send_keys(objWrite.WriteObjectRepo("app_sub"), objRead.ReadObjectRepo("app_sub"));
		   pu.wait_find_send_keys(objWrite.WriteObjectRepo("app_name"), objRead.ReadObjectRepo("app_name"));
		   pu.wait_find_send_keys(objWrite.WriteObjectRepo("app_mob"), objRead.ReadObjectRepo("app_mob"));
		   pu.wait_find_send_keys(objWrite.WriteObjectRepo("app_email"), objRead.ReadObjectRepo("app_email"));
		   Thread.sleep(3000);
   		 	pu.wait_find_click(objWrite.WriteObjectRepo("app_save"));
   		 	System.out.println("Clicked on save button");
   		// Thread.sleep(300);
   		 if(driver.getPageSource().contains("Appointment already exists at this time.")){
   			 
   			 System.out.println("Appointment exists. Cannot book appointment today.");
   		}

   		 else{
   			 driver.switchTo().alert();
   			 System.out.println("Active element");
   			 driver.switchTo().alert().accept();
   			 System.out.println("verification code has been sent to patients mobile no.");
   			 //pu.wait_find_click("//button/span[contains(text(),'Cancel')]");
   			 driver.navigate().to(url);
   		 }
}
   		 
   		 
public void checkClosedDay1(String url) throws InterruptedException{
		
		while(true){               
		    	if(selenium.isElementPresent("//*[@id='calendar']/div/div/div/div/div/table/tbody/tr[1]/td/div"))
		    	{
		    		pu.wait_find_click("//*[@id='calendar']/table/tbody/tr/td[1]/span[29]");
		    		System.out.println("--------------------------");
		    	}
		    	    pu.wait_find_click("//*[@id='calendar']/div/div/div/div/div/table/tbody/tr[1]/td");
		            Thread.sleep(1000);
					
						if(selenium.isTextPresent("Closed Day.") || selenium.isTextPresent("Can not select past date.")){
							pu.wait_find_click("//*[@id='calendar']/table/tbody/tr/td[1]/span[29]");
							System.out.println("next day");
							continue;
						}
						else{
							System.out.println("Inside else");
							break;
						}
					}
		    	
		   
	   }*/
	
	@Parameters({"url"})
	@Test(priority=8)
	public void logOut(String url) throws InterruptedException {
		driver.findElement(By.partialLinkText("Welcome")).click();
		driver.findElement(By.linkText("Logout")).click();
		Thread.sleep(1000);
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