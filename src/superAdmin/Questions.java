package superAdmin;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javatemp.objread;
import javatemp.objwrite;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class Questions {



	// declaring the selenium object
	private Selenium selenium;
	
	// declaring the webdriver object
	private WebDriver driver = null;
	
	
	objread objRead = new objread();
	objwrite objWrite = new objwrite();
	
	private PageUtils pu = null;
	String url, randomName;
	
	@Test
	public void login1()
	{
		System.out.println("Inside login");
	}

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
		
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"),objRead.ReadObjectRepo("lp_email"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"),objRead.ReadObjectRepo("lp_password"));
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
	
	@Parameters({"url"})
	@Test(priority=1 )//, enabled=false)
	public void testQuestionsLinkFunct(String url) throws InterruptedException{
       driver.findElement(By.linkText("Questions")).click();
       Thread.sleep(3000);
       pu.assert_text("All Questions");
       System.out.println("All questions");
	}
	
	@Parameters({"url"})
	@Test(priority=2)//, enabled=false)
	public void testFilterByAllQuestions(String url) throws InterruptedException{
		 driver.findElement(By.linkText("Questions")).click();
       Thread.sleep(3000);
		pu.wait_find_check(objWrite.WriteObjectRepo("all_questions"));
		assertTrue(driver.getPageSource().contains("ua-question.png"));
		assertTrue(driver.getPageSource().contains("a-question.png"));
		System.out.println("All questions filter working..");
		
	}
	
	@Parameters({"url"})
	@Test(priority=3)//, enabled=false)
	public void testFilterByUnassignedQuestions(String url) throws InterruptedException{
		 driver.findElement(By.linkText("Questions")).click();
       Thread.sleep(3000);
		pu.wait_find_check(objWrite.WriteObjectRepo("unassigned_questions"));
		Thread.sleep(1000);
		if(selenium.isTextPresent("No records found")){
			System.out.println("No records found");
		}
		else{
		String path=driver.findElement(By.xpath("//*[@id='question']/div[1]/div[1]/img")).getAttribute("src");
		assertTrue(path.contains("ua-question.png"));
		System.out.println("Unassigned questions filter working..");
		}
	}
	
	@Parameters({"url"})
	@Test(priority=4)//, enabled=false)
	public void testRejectQuestion(String url) throws InterruptedException{
		 driver.findElement(By.linkText("Questions")).click();
       Thread.sleep(3000);
		pu.wait_find_click("//*[@id='question']/div[1]/div[3]/img");
		driver.switchTo().alert();
		driver.switchTo().alert().accept();
		Thread.sleep(5000);
		driver.switchTo().alert();
		driver.switchTo().alert().accept();
		
	}
	
	@Parameters({"url"})
	@Test(priority=5)//, enabled=false)
	public void testassignDocToQuestion(String url) throws InterruptedException{
		driver.findElement(By.linkText("Questions")).click();
	    Thread.sleep(2000);
		pu.wait_find_click(objWrite.WriteObjectRepo("1st_question"));
		Thread.sleep(1000);
		//System.out.println("Doctor search");
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_list"),objRead.ReadObjectRepo("temp_doc"));
		Thread.sleep(1000);
		//System.out.println("1");
		String docname="Amruta BIDKAR";//selenium.getTable(objWrite.WriteObjectRepo("1st_doctor"));
	//	System.out.println("Doc name = " + docname);
		pu.wait_find_check(objWrite.WriteObjectRepo("1st_doc_assign"));
		pu.wait_find_click(objWrite.WriteObjectRepo("assign_doc_button"));
		//System.out.println("2");
		Thread.sleep(3000);
		
		driver.findElement(By.linkText("Questions")).click();
		pu.wait_find_click(objWrite.WriteObjectRepo("1st_question"));
		WebElement Webtable=driver.findElement(By.id("assign-doctor-list")); // Replace TableID with Actual Table ID or Xpath
		List<WebElement> TotalRowCount=Webtable.findElements(By.xpath("//*[@id='assign-doctor-list']/tbody/tr"));

		System.out.println("No. of Rows in the WebTable: "+TotalRowCount.size());
		int i1=TotalRowCount.size();
		//System.out.println(i1);
		Thread.sleep(3000);
			for(int i=1;i<=i1;i++)
			{
				if(selenium.getText(objWrite.WriteObjectRepo("assigned_doc_list") + i+"]/td[1]").contains(docname)){
					System.out.println("Question assigned to doctor");
				//	System.out.println(i);
					break;
				}
				else{
					//System.out.println("Row count : " + i1);
				}
				//i1++;
			}
	System.out.println("Exit from Assign Doc to Test");
	}
		
	
	@Parameters({"url"})
	@Test(priority=6)//, enabled=true)
	public void testassignDocConnectDocToQuestion(String url) throws InterruptedException{
		 driver.findElement(By.linkText("Questions")).click();
	     Thread.sleep(3000);
		if(selenium.isElementPresent(objWrite.WriteObjectRepo("1st_question")))
		{
		pu.wait_find_click(objWrite.WriteObjectRepo("1st_question"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_list"),objRead.ReadObjectRepo("docconnect_doc"));
		Thread.sleep(1000);
		String docname="Anuja Joshi";
		if(selenium.isTextPresent("Showing 0 to 0 of 0 entries")){
			System.out.println("No data available");
		}
		else{
		pu.wait_find_check(objWrite.WriteObjectRepo("1st_doc_assign"));
		pu.wait_find_click(objWrite.WriteObjectRepo("assign_doc_button"));
		Thread.sleep(3000);
		driver.findElement(By.linkText("Questions")).click();
		pu.wait_find_click(objWrite.WriteObjectRepo("1st_question"));
		WebElement Webtable=driver.findElement(By.id("assign-doctor-list")); // Replace TableID with Actual Table ID or Xpath
		List<WebElement> TotalRowCount=Webtable.findElements(By.xpath("//*[@id='assign-doctor-list']/tbody/tr"));

		System.out.println("No. of Rows in the WebTable: "+TotalRowCount.size());
		int i1=TotalRowCount.size();
		//System.out.println(i1);
		Thread.sleep(3000);
			for(int i=1;i<=i1;i++)
			{
				if(selenium.getText(objWrite.WriteObjectRepo("assigned_doc_list") + i+"]/td[1]").contains(docname)){
					System.out.println("Question assigned to doctor");
				//	System.out.println(i);
					break;
				}
			}
		}
		}
		System.out.println("Exit from Assign Docconnect Doc to Test");
	}
	

	
	@Parameters({"url"})
	@Test(priority=7)//, enabled=true)
	public void testremoveAssignedDoctor(String url) throws InterruptedException{
		 driver.findElement(By.linkText("Questions")).click();
	       Thread.sleep(3000);
	       pu.wait_find_click(objWrite.WriteObjectRepo("1st_question"));
		pu.wait_find_click(objWrite.WriteObjectRepo("2nd_assigned_doc"));
		 Alert alt=driver.switchTo().alert();
		    System.out.println(alt.getText());
		    alt.accept();
		    Thread.sleep(4000);
		    pu.wait_find_click(objWrite.WriteObjectRepo("1st_assigned_doc"));
			 Alert alt1=driver.switchTo().alert();
			    System.out.println(alt1.getText());
			    alt1.accept();
			    Thread.sleep(4000);
//        driver.switchTo().alert();
//        driver.switchTo().alert().accept();
       // driver.switchTo().alert();
     //   driver.switchTo().alert().accept();
		
	}
	
	@Parameters({"url"})
	@Test(priority=9)//, enabled=true)
	public void testAddTagsToQuestion(String url) throws InterruptedException{
		
		driver.findElement(By.linkText("Questions")).click();
		Thread.sleep(2000);
	    pu.wait_find_click(objWrite.WriteObjectRepo("1st_question"));
	    Thread.sleep(2000);
		pu.wait_find_click(objWrite.WriteObjectRepo("tag_text_box"));
		String Tag1= selenium.getText(objWrite.WriteObjectRepo("tag_1st_tag"));
		Thread.sleep(2000);
		pu.wait_find_click(objWrite.WriteObjectRepo("tag_1st_tag"));
	//	String Tag1= selenium.getText(objWrite.WriteObjectRepo("tag_1st_tag"));
		pu.wait_find_click(objWrite.WriteObjectRepo("tag_assign_button"));
		Thread.sleep(3000);
		pu.wait_find_click(objWrite.WriteObjectRepo("tag_text_box"));
		String Tag2= selenium.getText(objWrite.WriteObjectRepo("tag_1st_tag"));
		//pu.wait_find_click(objWrite.WriteObjectRepo("tag_1st_tag"));
		//pu.wait_find_click(objWrite.WriteObjectRepo("tag_assign_button"));
		if (Tag1.equals(Tag2)){
		System.out.println("Tag inserted successfully");
		}
		else {
			System.out.println("Tag not inserted");
		}
		Thread.sleep(2000);
		if(selenium.getText(objWrite.WriteObjectRepo("tag_text_box")).contains(selenium.getText(objWrite.WriteObjectRepo("tag_2nd_tag")))){
			//tag present
		}
		else{
		pu.wait_find_click(objWrite.WriteObjectRepo("tag_text_box"));
		Thread.sleep(1000);
		pu.wait_find_click(objWrite.WriteObjectRepo("tag_2nd_tag"));
		Thread.sleep(1000);
		pu.wait_find_click(objWrite.WriteObjectRepo("tag_assign_button"));
		Thread.sleep(1000);
		}
		pu.wait_find_click(objWrite.WriteObjectRepo("tag_1st_tag_delete"));
		if( pu.isAlertPresent())
	 	   {
			//driver.switchTo().alert().accept();
	 	   //System.out.println("Alert present");
	 	   }
	       	else
	       	{
	       		System.out.println("No alert present");
	       	}
		
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
		Thread.sleep(3000);
	}
	
	
	
	
	@Parameters({"url"})
	@Test(priority=8)//, enabled=false)
	public void testAddBlankTagToQuestion(String url) throws InterruptedException{
		driver.findElement(By.linkText("Questions")).click();
		Thread.sleep(3000);
		pu.wait_find_click(objWrite.WriteObjectRepo("1st_question"));
		if(selenium.getText(objWrite.WriteObjectRepo("tag_text_box")).isEmpty()){
		pu.wait_find_click(objWrite.WriteObjectRepo("tag_assign_button"));
		 Alert alt=driver.switchTo().alert();
		    System.out.println(alt.getText());
		    alt.accept();
		    Thread.sleep(2000);}
		else{System.out.println("Already tag is present");}
//		driver.switchTo().alert();
//		driver.switchTo().alert().accept();
	}
	

	@Parameters({"url"})
	@Test(priority=10)//, enabled=false)
	public void testSearchQuestions(String url) throws InterruptedException{
		driver.findElement(By.linkText("Questions")).click();
	    Thread.sleep(3000);
		pu.wait_find_click(objWrite.WriteObjectRepo("1st_question"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("doc_list"),"Priyanka");
		if(selenium.isTextPresent("No data available")){
			System.out.println("Result not found");
		}
		else{
			assertTrue(selenium.getText("//*[@id='doctor-list']/tbody/tr/td[2]").contains("priyanka"));
		}
		
		
	}
	
	@Parameters({"url"})
	@Test(priority=11)
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
	


