package itqs;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javafx.scene.control.CheckBox;

public class SpecificKeywords {
	//static Logger logger;
	//SpecificKeywords.logger=Logger.getLogger("SpecificKeywords");
	
	
	public static void exitBrowser(WebDriver driver )
	{
	
    	driver.close();
    	
    	
    	
    	
	}

	public static int getchildElementCount(WebDriver driver, String parentWebElement, String childWebElement)
	{
		int subElementCountValue;
		subElementCountValue=0;
		By parentLocator,childLocator;
		WebElement parentAvailableWebElement;
		List<WebElement> chileWebElements=null;
		parentLocator=null;
		childLocator=null;
		parentAvailableWebElement=null;
		
		parentLocator=GenericKeywords.getbjectLocator(GenericKeywords.getOR(parentWebElement));
		childLocator=GenericKeywords.getbjectLocator(GenericKeywords.getOR(childWebElement));
		
		if (!(parentLocator.equals(null) && childLocator.equals(null)) )
		{
			parentAvailableWebElement=driver.findElement(parentLocator);
			if(!parentAvailableWebElement.equals(null))
			{
				chileWebElements=parentAvailableWebElement.findElements(childLocator);
				subElementCountValue=chileWebElements.size();
			}
			else
			{
				subElementCountValue=-1;
			}
		}
		else
		{
			subElementCountValue=-1;
		}
		return subElementCountValue;
	}
	
	
public static int checkLeftPaneCheckBox(WebDriver driver, String checkBoxLebelValue)
{ 
	int abletoCheck;
	abletoCheck=0;
	String checkBoxXPath="";
	WebElement checkBoxObject=null;

	checkBoxXPath="//*[text()='"+checkBoxLebelValue+"']/parent::*/parent::*/input[@type='checkbox']";
	checkBoxObject=driver.findElement(By.xpath(checkBoxXPath));
	if(!(checkBoxObject.equals(null) && checkBoxObject.isSelected()) )
	{
		checkBoxObject.click();
		GenericKeywords.waitForTime(driver,5);
		//GenericKeywords.waitForObjectToLoad(driver, checkBoxObject, 300);
		abletoCheck=1;
	}
	else
	{
		abletoCheck=-1;
	}
	//driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
	return abletoCheck;
}
	


	
	
}
