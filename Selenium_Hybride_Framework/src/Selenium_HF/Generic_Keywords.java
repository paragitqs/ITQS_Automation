package Selenium_HF;

import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;

public class Generic_Keywords {
	
	public int openApp(WebDriver driver,String URL)
	{
		
		//System.setProperty("webdriver.gecko.driver","C:\\Selenium Videos\\GeckoDriver\\geckodriver-v0.19.1-win64\\geckodriver.exe");
		//WebDriver driver = new FirefoxDriver();
		driver.get(URL);
		if (driver.getTitle().contains("Amazon"))
		{
			//System.out.println("Exist");
			return 1;
		}
		else
		{
			return 0;
			
		}
		//driver.quit();
		//System.out.println(driver.toString());
		
	}
	
	

}
