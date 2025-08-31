package testCases;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;  // make sure this is imported
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass {
    
    protected static WebDriver driver;   // static so it’s shared across tests
    public Logger logger;                // log4j2
    public Properties p;

    @BeforeClass(groups = {"Sanity","Regression","Master"})
    @Parameters({"os","browser"})
    void setup(String os, String br) throws IOException {
        // Load config.properties file
        FileReader file = new FileReader("./src//test//resources//config.properties");
        p = new Properties();
        p.load(file);

        logger = LogManager.getLogger(this.getClass()); // log4j2
        
        if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
		{
			DesiredCapabilities capabilities=new DesiredCapabilities();
			
			//os
			if(os.equalsIgnoreCase("windows"))
			{
				capabilities.setPlatform(Platform.WIN10);
			}
			else if(os.equalsIgnoreCase("linux"))
			{
				capabilities.setPlatform(Platform.LINUX);
				
			}
			else if (os.equalsIgnoreCase("mac"))
			{
				capabilities.setPlatform(Platform.MAC);
			}
			else
			{
				System.out.println("No matching os");
				return;
			}
		
			
			//browser
			switch(br.toLowerCase())
			{
			case "chrome": capabilities.setBrowserName("chrome"); break;
			case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
			case "firefox": capabilities.setBrowserName("firefox"); break;
			default: System.out.println("No matching browser"); return;
			}
			
			driver=new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities);
		}
		
        if(p.getProperty("execution_env").equalsIgnoreCase("local"))
		{

			switch(br.toLowerCase())
			{
			case "chrome" : driver=new ChromeDriver(); break;
			case "edge" : driver=new EdgeDriver(); break;
			case "firefox": driver=new FirefoxDriver(); break;
			default : System.out.println("Invalid browser name.."); return;
			}
		}
		
		

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(p.getProperty("appURL")); // reading url from properties file
        driver.manage().window().maximize();
    }

    @AfterClass
    void tearDown() {
        if (driver != null) {
            driver.quit();   // better than close()
        }
    }

    // ========= Utility methods =========
    public String randomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public String randomNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public String randomAlphaNumeric() {
        String generatedstring = RandomStringUtils.randomAlphabetic(3);
        String generatednumber = RandomStringUtils.randomNumeric(3);
        return (generatedstring + "@" + generatednumber);
    }

    public String captureScreen(String tname) throws IOException {
        if (driver == null) {
            throw new RuntimeException("Driver is not initialized. Cannot capture screenshot!");
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

        String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";
        File targetFile = new File(targetFilePath);

        FileUtils.copyFile(sourceFile, targetFile);  // reliable saving

        return targetFilePath;
    }
}
