package Selenium_HF;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;

public class Project_Specific_Keywords {
	
	public void setEditBoxValue(WebDriver driver,String LocatorName,String StrValue)
	{
		//System.setProperty("webdriver.gecko.driver","C:\\Selenium Videos\\GeckoDriver\\geckodriver-v0.19.1-win64\\geckodriver.exe");
		//WebDriver driver = new FirefoxDriver();
		WebElement eBox= driver.findElement(By.id(LocatorName));
		eBox.sendKeys(StrValue);
	}
	
	
	public void ClickID(WebDriver driver,String LocatorName)
	{
		//System.setProperty("webdriver.gecko.driver","C:\\Selenium Videos\\GeckoDriver\\geckodriver-v0.19.1-win64\\geckodriver.exe");
		//WebDriver driver = new FirefoxDriver();
		WebElement eID= driver.findElement(By.id(LocatorName));
		eID.click();
	}
	
	

}
