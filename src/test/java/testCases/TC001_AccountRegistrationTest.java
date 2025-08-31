package testCases;

import java.time.Duration;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;

public class TC001_AccountRegistrationTest extends BaseClass {
	
	@Test(groups={"Regression","Master"})
	void verify_account_registration()
	{
		logger.info("--------started TC001_AccountRegistrationTest----------------");
		try
		{
		
		
		HomePage hp=new HomePage(driver);
		hp.clickMyAccount();
		logger.info("Clicked on MyAccount Link");
		hp.clickRegister();
		logger.info("Clicked on Register Link");
		AccountRegistrationPage regpage= new AccountRegistrationPage(driver);
		logger.info("Providing Customer Details");
		regpage.setFirstName(randomString().toUpperCase());
		regpage.setLastName(randomString().toUpperCase());
		regpage.setEmail(randomString()+"@gmail.com");
		regpage.setTelephone(randomNumber());
		
		String password=randomAlphaNumeric();
		regpage.setPassword(password);
		regpage.setConfirmPassword(password);
		
		regpage.setPrivacyPolicy();
		regpage.clickContinue();
		
		logger.info("Validating expected message...");
		String confmsg=regpage.getConfirmationMsg();
		
		Assert.assertEquals(confmsg,"Your Account Has Been Created!");
		}
		catch(Exception e)
		{
			logger.error("Test Failed....");
			//logger.debug("Debug logs....");
			Assert.fail();
		}
		logger.info("-------------finished TC001_AccountRegistrationTest---------");
		
		}
	
	
	
}
