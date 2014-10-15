package superAdmin;

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
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import utility.PageUtils;

import com.thoughtworks.selenium.Selenium;

public class signUpRequests {

	// declaring the selenium object
	private Selenium selenium;
	
	objread objRead = new objread();
	objwrite objWrite = new objwrite();
		
	// declaring the webdriver object
	private WebDriver driver = null;
	
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
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		pu = PageUtils.getInstance(selenium, driver, url );
		if(selenium.isTextPresent("Ask Doctors"))
		{
		driver.findElement(By.linkText("Login")).click();
		}
		else{
			System.out.println("already on login page");
		}
			
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"),objRead.ReadObjectRepo("su_email"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"),objRead.ReadObjectRepo("su_password"));
		pu.wait_find_click(objWrite.WriteObjectRepo("lp_signin"));
		
		Thread.sleep(1000);
		System.out.println("********* Valid Superadmin Login *********");
		
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
	public void testglobalSearch(String url) throws InterruptedException{
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("global_search"), objRead.ReadObjectRepo("global_search_key"));
		Thread.sleep(1000);
		//assertTrue(selenium.isElementPresent("//*[starts-with(@id,'ui-id-')]"));
		System.out.println("********* Global Search Box Working *********");
	}
	
	@Parameters({"url"})
	@Test(priority=2)//(enabled=false) //bug search result list disappears on mouse out
	public void testglobalSearchLinkFunct(String url) throws InterruptedException{
		driver.findElement(By.xpath(objWrite.WriteObjectRepo("global_search"))).clear();
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("global_search"), objRead.ReadObjectRepo("global_search_key"));
		Thread.sleep(2000);
		//driver.findElement(By.partialLinkText(objRead.ReadObjectRepo("global_search_key"))).click();
		pu.wait_find_click(objWrite.WriteObjectRepo("global_search_result"));
		Thread.sleep(1000);
		System.out.println("inside global search");
		//pu.assert_text("Relevant answers");
		//driver.findElement(By.partialLinkText(objRead.ReadObjectRepo("global_search_key"))).click();
		//System.out.println("inside question");
		pu.assert_text("Assign More Tags:");
		//System.out.println("Global search");
		System.out.println("********* Global Search Working *********");
  }
	
	@Parameters({"url"})
	@Test(priority=3)//(enabled=false)
	public void testshowRegisteredRequests(String url) throws InterruptedException{
	    driver.findElement(By.partialLinkText("Signup")).click();
	   
	   selenium.uncheck(objWrite.WriteObjectRepo("su_signup_approve_checkbox"));
	   selenium.uncheck(objWrite.WriteObjectRepo("su_signup_reject_checkbox"));
	   pu.wait_find_check(objWrite.WriteObjectRepo("su_signup_registered_checkbox"));
	   Thread.sleep(2000);
	    
	    if(selenium.isTextPresent("No data available")){
	    	System.out.println("No registration requests");
	    }
	    else{
	    	//assertTrue(selenium.isElementPresent(objWrite.WriteObjectRepo("su_signup_approve3")));
	    	System.out.println("********* Registration requests present *********");
	    }
	}
	
	@Parameters({"url"})
	@Test(priority=4)//(enabled=false)
	public void testshowApprovedDoctors(String url) throws InterruptedException{
		 driver.findElement(By.partialLinkText("Signup")).click();
	    
	     selenium.uncheck(objWrite.WriteObjectRepo("su_signup_registered_checkbox"));
	     selenium.uncheck(objWrite.WriteObjectRepo("su_signup_reject_checkbox"));
	     pu.wait_find_check(objWrite.WriteObjectRepo("su_signup_approve_checkbox"));
	     Thread.sleep(2000);
	     if(selenium.isTextPresent("No data available")){
		    	System.out.println("No approved requests");
		    }
		    else{
		    	assertTrue(selenium.isElementPresent(objWrite.WriteObjectRepo("su_signup_reject")));
		    	System.out.println("Approved requests present");
		    }
	}
	
	
	@Parameters({"url"})
	@Test(priority=5)//(enabled=false)
	public void testshowRejectedDoctors(String url) throws InterruptedException{
		 driver.findElement(By.partialLinkText("Signup")).click();
	   
		 selenium.uncheck(objWrite.WriteObjectRepo("su_signup_approve_checkbox"));
	     selenium.uncheck(objWrite.WriteObjectRepo("su_signup_registered_checkbox"));
	    
	     pu.wait_find_check(objWrite.WriteObjectRepo("su_signup_reject_checkbox"));
	     Thread.sleep(2000);
	     if(selenium.isTextPresent("No data available")){
		    	System.out.println("No rejected requests");
		    }
		    else{
		    	assertTrue(selenium.isElementPresent(objWrite.WriteObjectRepo("su_signup_approve")));
		    	System.out.println("Rejected requests present");
		    }
	 }
	
	@Parameters({"url"})
	@Test(priority=6)//(enabled=false)
	public void testEditRegisterdRequests(String url) throws InterruptedException{
		 driver.findElement(By.partialLinkText("Signup")).click();
		   
		  pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_registered_checkbox"));
		   Thread.sleep(2000);
		    
		    if(driver.getPageSource().contains("No data available")){
		    	System.out.println("No registration requests");
		    	
		    }
		    else{
		         pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_doclist") + "/tr/td[7]/a[1]");
		         Thread.sleep(3000);
		         pu.wait_find_click(objWrite.WriteObjectRepo("su_doc_profile"));
		         Thread.sleep(1000);
		         if(driver.getPageSource().contains(objRead.ReadObjectRepo("Doc_Profile_Update_Msg"))){
		         System.out.println("********* Doctor Profile edited Successfully *********");
		         }
		         else
		         {
		        	 System.out.println("Can not update profile");
		        	// driver.close();
		         }
		    }
	}
	
	
	@Parameters({"url"})
	@Test(priority=7)//(enabled=false)
	public void testEditApprovedRequests(String url) throws InterruptedException{
		 driver.findElement(By.partialLinkText("Signup")).click();
		  pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_approve_checkbox"));
		   Thread.sleep(2000);
		    
		    if(driver.getPageSource().contains("No data available")){
		    	System.out.println("No registration requests");
		    	
		    }
		    else{
		         pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_doclist") + "/tr/td[7]/a[1]");
		         Thread.sleep(3000);
		         pu.wait_find_click(objWrite.WriteObjectRepo("su_doc_profile"));
		         Thread.sleep(1000);
		         if(driver.getPageSource().contains(objRead.ReadObjectRepo("Doc_Profile_Update_Msg"))){
		         System.out.println("********* Doctor Profile edited Successfully *********");
		         }
		         else
		         {
		        	 System.out.println("Can not update profile");
		        	// driver.close();
		         }
		    }
	}
	
	@Parameters({"url"})
	@Test(priority=8)//(enabled=false) //issue - edit button does not work
	public void testEditRejectedRequests(String url) throws InterruptedException{
		 driver.findElement(By.partialLinkText("Signup")).click();
		   
		  pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_reject_checkbox"));
		   Thread.sleep(2000);
		    
		    if(driver.getPageSource().contains("No data available")){
		    	System.out.println("No registration requests");
		    	
		    }
		    else{
		         pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_doclist") + "/tr/td[7]/a[1]");
		         Thread.sleep(3000);
		         pu.wait_find_click(objWrite.WriteObjectRepo("su_doc_profile"));
		         Thread.sleep(1000);
		         if(driver.getPageSource().contains(objRead.ReadObjectRepo("Doc_Profile_Update_Msg"))){
		         System.out.println("********* Doctor Profile edited Successfully *********");
		         }
		         else
		         {
		        	 System.out.println("Can not update profile");
		        	// driver.close();
		         }
		    }
	}
	
	
	@Parameters({"url"})
	@Test(priority=9)//(enabled=false) 
	public void testsearchRequests(String url){
		 driver.findElement(By.partialLinkText("Signup")).click();
		 pu.wait_find_send_keys(objWrite.WriteObjectRepo("su_list_search_box"), "autodoctor");
		 if(selenium.isTextPresent("Showing 0 to 0 of 0 entries")){
			 System.out.println("Record not found");
		 }
		 else{
		 assertTrue(selenium.getText( objWrite.WriteObjectRepo("su_signup_doclist") + "/tr/td[3]").contains("autodoctor"));
		 }
		
	}
	
	@Parameters({"url"})
	@Test(priority=10)//(enabled=false)
	public void testApproveRegisterdRequests(String url) throws InterruptedException{
		 driver.findElement(By.partialLinkText("Signup")).click();
		   
		   pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_registered_checkbox"));
		   Thread.sleep(2000);
		    
		    if(driver.getPageSource().contains("No data available")){
		    	System.out.println("No registration requests");
		    	
		    }
		    else{
		    	driver.findElement(By.xpath(objWrite.WriteObjectRepo("su_list_search_box"))).sendKeys(objRead.ReadObjectRepo("temp_mail_id_appr3")) ;
		    	Thread.sleep(1000);
		    	if(driver.getPageSource().contains("Showing 0 to 0 of 0 entries")){
			    	System.out.println("No registration requests for " + objRead.ReadObjectRepo("temp_mail_id_appr3"));
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
	}
	
	
	@Parameters({"url"})
	@Test(priority=11)//(enabled=false)
	public void testRejectRegisterdRequests(String url) throws InterruptedException{
		 driver.findElement(By.partialLinkText("Signup")).click();
		   
		    pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_registered_checkbox"));
		   Thread.sleep(2000);
		    
		    if(driver.getPageSource().contains("No data available")){
		    	System.out.println("No registration requests");
		    	
		    }
		    else{
		    	driver.findElement(By.xpath(objWrite.WriteObjectRepo("su_list_search_box"))).sendKeys(objRead.ReadObjectRepo("temp_mail_id_appr4")) ;
		    	Thread.sleep(1000);
		    	if(driver.getPageSource().contains("Showing 0 to 0 of 0 entries")){
			    	System.out.println("No registration requests for " + objRead.ReadObjectRepo("temp_mail_id_appr4"));
			    //	driver.quit();
			    //	System.exit(0);
			    }
		    	else
		    	{
		    	pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_reject1"));
		    	driver.switchTo().alert();
		    	Thread.sleep(3000);
				driver.switchTo().alert().accept();
				Thread.sleep(3000);
				driver.switchTo().alert();
				Thread.sleep(3000);
				driver.switchTo().alert().accept();
				Thread.sleep(3000);
				System.out.println("********* Reject Registered Doctor Successful *********");
		    }
		    }
	}
	
	
	@Parameters({"url"})
	@Test(priority=12)//(enabled=false)
	public void testRejectApprovedDoctors(String url) throws InterruptedException{
		 driver.findElement(By.partialLinkText("Signup")).click();
		 Thread.sleep(2000);  
		 pu.wait_find_check(objWrite.WriteObjectRepo("su_signup_approve_checkbox"));
		 
		 Thread.sleep(2000);
		 if(driver.getPageSource().contains("No data available")){
		   		System.out.println("No approved requests");  	
		    }
		 else{
			 	
			 	Select dropdown = new Select(driver.findElement(By.name("Doctorlist_length")));
			 	dropdown.selectByValue("100");
		    
	    					driver.findElement(By.xpath(objWrite.WriteObjectRepo("su_list_search_box"))).sendKeys(objRead.ReadObjectRepo("temp_mail_id_appr1"));
	    					System.out.println("Doctor selected");
	    					driver.findElement(By.xpath(objWrite.WriteObjectRepo("su_signup_reject"))).click();//reject doctor
	    					Thread.sleep(6000);
	    					pu.wait_find_check(objWrite.WriteObjectRepo("su_signup_approve_checkbox"));
	    					driver.findElement(By.xpath(objWrite.WriteObjectRepo("su_list_search_box"))).sendKeys(objRead.ReadObjectRepo("temp_mail_id_appr1"));
	    				
	    					Thread.sleep(2000);
	    					
	    					if(driver.getPageSource().contains("Showing 0 to 0 of 0 entries")){ 
	    					System.out.println("Doctor rejected");  	
	    					    }
	    					else System.out.println("Doctor not deleted");
	    				
	    					Thread.sleep(6000);
	    				
		    	 System.out.println("********* Doctor Found *********");	
		    }
	Thread.sleep(2000);	
	}
	
	
	
	@Parameters({"url"})
	@Test(priority=13)//(enabled=false)
	public void testApproveRejectedDoctors(String url) throws InterruptedException{
		driver.findElement(By.partialLinkText("Signup")).click();
		   
		 pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_reject_checkbox"));
	
		   Thread.sleep(2000);
		   if(driver.getPageSource().contains("No data available")){
		    	System.out.println("No Rejected requests");
		    	
		    }
		    else{
		    	driver.findElement(By.xpath(objWrite.WriteObjectRepo("su_list_search_box"))).sendKeys(objRead.ReadObjectRepo("temp_mail_id_appr2")) ;
		    	Thread.sleep(1000);
		    	if(driver.getPageSource().contains("Showing 0 to 0 of 0 entries")){
			    	System.out.println(objRead.ReadObjectRepo("temp_mail_id_appr2") + " Doctor was not rejected doctor");
			    	driver.quit();
			    	System.exit(0);
			    }
		    	pu.wait_find_click(objWrite.WriteObjectRepo("su_signup_approve"));
		    	Thread.sleep(2000);
		    	driver.switchTo().alert();
		    	Thread.sleep(2000);
				driver.switchTo().alert().accept();
				Thread.sleep(6000);
				//driver.switchTo().activeElement().click();
				//driver.switchTo().alert();
				driver.switchTo().alert().accept();
				Thread.sleep(3000);
		      	pu.wait_find_check(objWrite.WriteObjectRepo("su_signup_approve_checkbox"));
				driver.findElement(By.xpath(objWrite.WriteObjectRepo("su_list_search_box"))).sendKeys(objRead.ReadObjectRepo("temp_mail_id_appr2"));
				if(driver.getPageSource().contains("Showing 0 to 0 of 0 entries")){
			    	System.out.println(objRead.ReadObjectRepo("temp_mail_id_appr2") + " Doctor is not approved");
			    	driver.quit();
			    	System.exit(0);
			    }
				else
				{
					System.out.println("********* Doctor Approved Successfully *********");					
				}
			}
		Thread.sleep(3000);
	}
	
	@Parameters({"url"})
	@Test(priority=14)
	public void logOut(String url) throws InterruptedException {
		
		driver.findElement(By.partialLinkText("Welcome")).click();
		driver.findElement(By.linkText("Logout")).click();
		Thread.sleep(1000);
		if( pu.isAlertPresent())
	 	   {
	 	   //System.out.println("Alert present");
	 	   }
	       	else
	       	{
	       	//	System.out.println("No alert present");
	       	}
		Thread.sleep(1000);
		System.out.println("********** Logged Out Successfully **********");
	}
}
	