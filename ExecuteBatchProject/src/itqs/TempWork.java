package itqs;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TempWork {
public static WebDriver driver;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Logger logger=Logger.getLogger("TempWork");
		PropertyConfigurator.configure("./Resources/log4j.properties");
	Boolean a;	
	System.setProperty("webdriver.chrome.driver", ".\\libs\\chromedriver.exe");
	logger.info("Set property done");
	TempWork.driver=new ChromeDriver();
	TempWork.driver.get("https://www.amazon.com");
	TempWork.driver.manage().window().maximize();
	//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	a=TempWork.waitFor(TempWork.driver);
	System.out.println("Ghoshal  " + a.toString());
	}
	
	
public static boolean waitFor(WebDriver driver)
{
	WebElement element;
	WebDriverWait wait=new WebDriverWait(driver,2);
	try{
	element=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//Parag")));
	
	}catch(Exception e)
	{
		System.out.println("Parag ::");
		driver.quit();
		//driver.close();
		return true;
		
	}
	
return true;	
}

public static void writeToLog(String stringToWrite)
{
	Logger logger=Logger.getLogger("GenericKeywords");
	PropertyConfigurator.configure("log4j.property");
	

}

}
