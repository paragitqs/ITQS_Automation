package itqs;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

public class KeywordMapping {
	
	public enum KeyWord
	{
		OPENAPP,
		KILLBROWSER,
		SETEDITBOX,
		CLICKBUTTON,
		WAIT,
		CHECK_LEFTPANE_CHECKBOX,
		//Enter other Keyword values here
		OTHER
	}
	
	
	
	public static int keywordActionMapping(Driver dobj)
	{
		int executionStatus;
		By locator;
		executionStatus=1;
		locator=null;
		
		KeyWord enumKeyWordValue=KeyWord.valueOf(dobj.TestStep_Keyword_Used.toUpperCase());
		
		switch(enumKeyWordValue)
		{
		case OPENAPP:
			Driver.driver=GenericKeywords.launchBrowser(GenericKeywords.getConfigDetails("BrowserDetails"));
			GenericKeywords.openApplication(Driver.driver, dobj.Data_Step_Value);
			 executionStatus=1;
			 break;
					
		case KILLBROWSER:
			SpecificKeywords.exitBrowser(Driver.driver);
			 executionStatus=1;
			 break;
			
		case SETEDITBOX:
			locator=GenericKeywords.getbjectLocator(GenericKeywords.getOR(dobj.TestStep_ObjectName));
			if(GenericKeywords.isElementExist(Driver.driver, locator))
			{
				executionStatus=GenericKeywords.setEditBox(Driver.driver, locator, dobj.Data_Step_Value);
				System.out.println("Editbox does  exist : " +dobj.TestStep_ObjectName );
			}
			else//isElementExist
			{
				executionStatus=-1;
				System.out.println("Editbox does not exist : " +dobj.TestStep_ObjectName );
			}//isElementExist
			break;
		case CLICKBUTTON:
			locator=GenericKeywords.getbjectLocator(GenericKeywords.getOR(dobj.TestStep_ObjectName));
			if(GenericKeywords.isElementExist(Driver.driver, locator))
			{
				GenericKeywords.clickButton(Driver.driver, locator);
				executionStatus=1;
			}
			else//isElementExist
			{
				executionStatus=-1;
			}//isElementExist
			break;
		case WAIT:
			Driver.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			executionStatus=1;
			break;
		case CHECK_LEFTPANE_CHECKBOX:
			
			executionStatus=SpecificKeywords.checkLeftPaneCheckBox(Driver.driver, dobj.Data_Step_Value);
			break;
		case OTHER:
			executionStatus=-1;
			break;
		}
		
		/*			locator=GenericKeywords.getbjectLocator(GenericKeywords.getOR(dobj.TestStep_ObjectName));
		if(GenericKeywords.isElementExist(Driver.driver, locator))
		{
			
		executionStatus=1	
		}
		else//isElementExist
		{
			executionStatus=-1;
		}//isElementExist
		return executionStatus;
*/		
		return executionStatus;
	}
}

/*public static int keywordActionMapping(Driver dobj)
{
	int executionStatus;
	By locator;
	executionStatus=1;
	locator=null;
	
	
	if(dobj.TestStep_Keyword_Used.equalsIgnoreCase("OpenApp"))
	{
		Driver.driver=GenericKeywords.launchBrowser(GenericKeywords.getConfigDetails("BrowserDetails"));
		GenericKeywords.openApplication(Driver.driver, dobj.Data_Step_Value);
	}
	else if (dobj.TestStep_Keyword_Used.equalsIgnoreCase("KillBrowser"))
	{
		SpecificKeywords.exitBrowser(Driver.driver);
	}
	else
	{
		locator=GenericKeywords.getbjectLocator(GenericKeywords.getOR(dobj.TestStep_ObjectName));
		
		if(GenericKeywords.isElementExist(Driver.driver, locator))//isElementExist
		{
			if(dobj.TestStep_Keyword_Used.equalsIgnoreCase("setEditBox"))
			{
					GenericKeywords.setEditBox(Driver.driver, locator, dobj.Data_Step_Value);
			}
			else if(dobj.TestStep_Keyword_Used.equalsIgnoreCase("ClickButton"))
			{
					GenericKeywords.clickButton(Driver.driver, locator);
			}

			executionStatus=1;
		}//isElementExist
		else//isElementExist
		{
			executionStatus=-1;
		}//isElementExist
		
		if(dobj.TestStep_Screenshot_Flag.equals("1"))
		{
			GenericKeywords.takeScreenShot(Driver.driver, "./Data/SC.png");	
		}
	}

return executionStatus;
}*/
