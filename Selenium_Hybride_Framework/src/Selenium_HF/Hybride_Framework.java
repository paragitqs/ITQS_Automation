package Selenium_HF;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Hybride_Framework {
	
	public static void main(String[] args)
	{
		System.setProperty("webdriver.gecko.driver","C:\\Selenium Videos\\GeckoDriver\\geckodriver-v0.19.1-win64\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		//System.out.println("Parag GHoshal");
	Generic_Keywords Generic_Keyword_Object=new Generic_Keywords();
	Generic_Keyword_Object.openApp(driver,"https://www.amazon.in//");
	Project_Specific_Keywords PSK_Object= new Project_Specific_Keywords();
	PSK_Object.setEditBoxValue(driver,"twotabsearchtextbox", "Cell Phone");
	PSK_Object.ClickID(driver, "nav-search-submit-text");
	
	}
	


}
