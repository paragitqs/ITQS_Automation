package itqs;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class classLog {
	Logger logger;
	
	public void WriteToLog(String className,String ValueToLog)
	{
		logger=Logger.getLogger(className);	
		logger.info(ValueToLog);
		
	}
	
	

}
