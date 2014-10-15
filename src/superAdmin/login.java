package superAdmin;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javatemp.objread;
import javatemp.objwrite;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.openqa.selenium.By;
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

public class login {

	//declaring the selenium object
    public Selenium selenium; 
	
	//declaring the webdriver object
     WebDriver driver;// = null;

	public PageUtils pu;// = null;
	objread objRead = new objread();
	objwrite objWrite = new objwrite();
	
	FileInputStream fi;
	Workbook w;
	Sheet s;

	
	String url, randomName;

	@BeforeClass//(alwaysRun = true)
	
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
		
		selenium = new WebDriverBackedSelenium(driver,url+"/login");
        driver.navigate().to(url + "/login");

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		pu = PageUtils.getInstance(selenium,driver, url);
	
	}
	
	@Parameters({"browser"})
	@AfterClass//(alwaysRun = true)
	public void StopDriver(String browser) throws Exception {
		driver.quit();
		System.out.println("Closing "+browser);
		
	}
	
	public login() throws BiffException, IOException{
		fi=new FileInputStream(".\\login.xls");
		w=Workbook.getWorkbook(fi);
		s=w.getSheet(0);
	}
	
	@Parameters({"url"})
	@Test(priority=5)//Valid Login
	public void testlogin(String url) throws InterruptedException{
	//	for(int i=0;i<=2;i++){
		driver.navigate().to(url + "/login");
		if(selenium.isTextPresent("Do you have any health related questions?"))
		{
		driver.findElement(By.linkText("Login")).click();
		}
		else{
			System.out.println("already on login page");
		}
		
			
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"),objRead.ReadObjectRepo("lp_email"));
			pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"),objRead.ReadObjectRepo("lp_password"));
			//pu.wait_find_send_keys("//*[@id='ctl00_MainPage_txtPassword']", "");
			//pu.wait_find_click("//*[@id='ctl00_MainPage_btnLogin']");
			pu.wait_find_click(objWrite.WriteObjectRepo("lp_signin"));
			Thread.sleep(5000);
			
			driver.findElement(By.partialLinkText("Welcome")).click();
			driver.findElement(By.linkText("Logout")).click();
			Thread.sleep(3000);
			System.out.println("*********Valid Superadmin Login *********");
			driver.switchTo().alert();
			driver.switchTo().alert().accept();
	//	}
		
	} 

	
	@Parameters({"url"})
	@Test(priority=1)// blank email ID and password
	public void testBlankLogin(String url) throws InterruptedException{
		driver.navigate().to(url + "/login");
		if(selenium.isTextPresent("Do you have any health related questions?"))
		{
		driver.findElement(By.linkText("Login")).click();
		}
		else{
			System.out.println("already on login page");
		}
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"), "");
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"), "");
		pu.wait_find_click(objWrite.WriteObjectRepo("lp_signin"));
		Thread.sleep(3000);
		pu.assert_text(objRead.ReadObjectRepo("lp_email_error"));
		pu.assert_text(objRead.ReadObjectRepo("lp_password_error"));
		Thread.sleep(3000);
		System.out.println("********* blank email ID and password *********");
	}
	
	@Parameters({"url"})
	@Test(priority=2) //Blank email ID
	public void testBlankEmailLogin(String url) throws InterruptedException{
		driver.navigate().to(url + "/login");
		if(selenium.isTextPresent("Do you have any health related questions?"))
		{
		driver.findElement(By.linkText("Login")).click();
		}
		else{
			System.out.println("already on login page");
		}
		
		//pu.wait_find_send_keys("//*[@id='ctl00_MainPage_txtEmail']", "");
		//pu.wait_find_send_keys("//*[@id='ctl00_MainPage_txtPassword']", "123123");
		//pu.wait_find_click("//*[@id='ctl00_MainPage_btnLogin']");
		
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"), "");
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"), "123123");
		pu.wait_find_click(objWrite.WriteObjectRepo("lp_signin"));
		pu.assert_text(objRead.ReadObjectRepo("lp_email_error"));
		Thread.sleep(3000);
		System.out.println("********* Blank email ID *********");
		
	}
	
	@Parameters({"url"})
	@Test(priority=3) // Blank password
	public void testBlankPassLogin(String url) throws InterruptedException{
		driver.navigate().to(url + "/login");
		if(selenium.isTextPresent("Do you have any health related questions?"))
		{
		driver.findElement(By.linkText("Login")).click();
		}
		else{
			System.out.println("already on login page");
		}
		
		//pu.wait_find_send_keys("//*[@id='ctl00_MainPage_txtEmail']", "autodoctor@mailinator.com");
		//pu.wait_find_send_keys("//*[@id='ctl00_MainPage_txtPassword']", "");
		//pu.wait_find_click("//*[@id='ctl00_MainPage_btnLogin']");
		
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"),objRead.ReadObjectRepo("lp_email"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"), "");
		pu.wait_find_click(objWrite.WriteObjectRepo("lp_signin"));
		pu.assert_text(objRead.ReadObjectRepo("lp_password_error"));
		Thread.sleep(3000);
		System.out.println("********* Blank password *********");
	
	}
	
	@Parameters({"url"})
	@Test(priority=4) //Invalid email ID and password
	public void testInvlaidLogin(String url) throws InterruptedException{
		driver.navigate().to(url + "/login");
		if(selenium.isTextPresent("Do you have any health related questions?"))
		{
		driver.findElement(By.linkText("Login")).click();
		}
		else{
			System.out.println("already on login page");
		}
		//pu.wait_find_send_keys("//*[@id='ctl00_MainPage_txtEmail']", "test@doctor.com");
		//pu.wait_find_send_keys("//*[@id='ctl00_MainPage_txtPassword']", "6456456");
		//pu.wait_find_click("//*[@id='ctl00_MainPage_btnLogin']");
		
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"),objRead.ReadObjectRepo("lp_email") + ".in.com");
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"), objRead.ReadObjectRepo("lp_password") + ".in.com");
		pu.wait_find_click(objWrite.WriteObjectRepo("lp_signin"));
		pu.assert_text(objRead.ReadObjectRepo("lp_email_password_error"));
		Thread.sleep(3000);
		System.out.println("********* Invalid email ID and password *********");
		
	}
	
	
	@Parameters({"url"})
	@Test(priority=6) //Doc-connect Doctor login
	public void testDocConnectDoctor_Login(String url) throws InterruptedException{
		driver.navigate().to(url + "/login");
		if(selenium.isTextPresent("Do you have any health related questions?"))
		{
		driver.findElement(By.linkText("Login")).click();
		}
		else{
			System.out.println("already on login page");
		}
		//pu.wait_find_send_keys("//*[@id='ctl00_MainPage_txtEmail']", "yassh88@outlook.com");
		//pu.wait_find_send_keys("//*[@id='ctl00_MainPage_txtPassword']", "sal123");
		//pu.wait_find_click("//*[@id='ctl00_MainPage_btnLogin']");
		
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_email"),objRead.ReadObjectRepo("DocConnectDoctor_Email"));
		pu.wait_find_send_keys(objWrite.WriteObjectRepo("lp_password"),objRead.ReadObjectRepo("DocConnectDoctor_Password"));
		pu.wait_find_click(objWrite.WriteObjectRepo("lp_signin"));
		Thread.sleep(5000);
		driver.findElement(By.partialLinkText("Welcome")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.switchTo().alert();
		driver.switchTo().alert().accept();
		Thread.sleep(3000);
		System.out.println("*********Doc-connect Doctor login********");
	}
	
}
