package itqs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
//import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;



public class GenericKeywords {
	
	static String configFileLocation;
    static String ObjectRepositoryFileLocation;
    static Logger logger;
    


    
    public static void Initialize()
    {
        configFileLocation="./Resources/configuration.property";
        ObjectRepositoryFileLocation="./Resources/ObjectRepository.property";
        GenericKeywords.logger=Logger.getLogger("GenericKeywords");
        //PropertyConfigurator.configure("./Resources/log4j.properties");
        
    }
    public static WebDriver launchBrowser(String BrowserType)
    {
        WebDriver driver;
        driver=null;
        if(BrowserType.equalsIgnoreCase("firefox"))
        {
            //System.setProperty("webdriver.gecko.driver", ".\\libs\\geckodriver.exe");
            System.setProperty("webdriver.gecko.driver", GenericKeywords.getConfigDetails("fireFoxDriverPath"));
            driver=new FirefoxDriver();
            //logger.info("FireFox Browser launched");
        }
        
        if(BrowserType.equalsIgnoreCase("ie"))
        {
            //System.setProperty("webdriver.ie.driver", ".\\libs\\IEDriverServer.exe");
            System.setProperty("webdriver.ie.driver", GenericKeywords.getConfigDetails("ieDriverPath"));
            //DesiredCapabilities ieCap=DesiredCapabilities.internetExplorer();
            
            driver=new InternetExplorerDriver();
            //logger.info("IE Browser launched");
        }
        if(BrowserType.equalsIgnoreCase("chrome"))
        {
            //System.setProperty("webdriver.chrome.driver", ".\\libs\\chromedriver.exe");
            System.setProperty("webdriver.chrome.driver", GenericKeywords.getConfigDetails("chromeDriverPath"));
            driver=new ChromeDriver();
            GenericKeywords.logger.info("LaunchBrowser : Chrome Browser launched");
        }
        return driver;
    }
    
    public static void openApplication(WebDriver driver,String URL)
    {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        driver.get(URL);
        GenericKeywords.logger.info("OpenApplication : Application Launched");
    
    }
    
    public static String getConfigDetails(String findKey)
    {
        String keyValue;
        keyValue="";
        
        try {
            File configSourceFile= new File(configFileLocation);
            FileInputStream fis= new FileInputStream(configSourceFile);
            Properties propertyDetails=new Properties();
            propertyDetails.load(fis);
            keyValue=propertyDetails.getProperty(findKey);
            GenericKeywords.logger.info("getConfigDetails : Configuration Details Fetched");
        }  
        catch (Exception e) {
            // TODO Auto-generated catch block
        //System.out.println("There is some Excepton value please check : "+ e.getMessage());
        GenericKeywords.logger.info("getConfigDetails : Configuration Details does not Fetched");
        }
        
        return keyValue;
    }
    public static String getOR(String ORKey)
    {
        //System.out.println("Start :" + ObjectRepositoryFileLocation);
        String ORValue;
        ORValue="";
        try {
            //System.out.println(""+configurationFileLocation);
            File configSourceFile=new File(ObjectRepositoryFileLocation);
            FileInputStream fis=new FileInputStream(configSourceFile);
            Properties propertyDetails=new Properties();
            propertyDetails.load(fis);
            ORValue=propertyDetails.getProperty(ORKey);
            GenericKeywords.logger.info("getOR : OR Details Fetched");
        
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //System.out.println("OR Exception Value : " + e.getMessage());
            GenericKeywords.logger.info("getOR : OR Details does not Fetched");
        }
        return ORValue;
    }
    
    public static By getbjectLocator(String locatorProperty)
    {
        //String locatorProperty = propertyFile.getProperty(locatorName);
        //System.out.println(locatorProperty.toString());
        String locatorType = locatorProperty.split(":")[0];
        //System.out.println("LocatorType : " +locatorType );
        String locatorValue = locatorProperty.split(":")[1];
        //System.out.println("LocatorValue : " +locatorValue );
         By locator = null;
         
        if(locatorType.equalsIgnoreCase("Id")) locator = By.id(locatorValue);
        else if(locatorType.equalsIgnoreCase("Name")) locator= By.name(locatorValue);
        else if(locatorType.equalsIgnoreCase("CssSelector")) locator=By.cssSelector(locatorValue);
        else if (locatorType.equalsIgnoreCase("LinkText")) locator=By.linkText(locatorValue);
        else if (locatorType.equalsIgnoreCase("PartialLinkText")) locator= By.partialLinkText(locatorValue);
        else if (locatorType.equalsIgnoreCase("TagName")) locator=By.tagName(locatorValue);
        else if(locatorType.equalsIgnoreCase("Xpath")) locator=By.xpath(locatorValue);
        GenericKeywords.logger.info("getbjectLocator : Object Locator Identified");
        return locator;
    }

    public static int getWebElementCount(WebDriver driver,By locator )
	{

	  List<WebElement> WebElementList=  driver.findElements(locator);
	  GenericKeywords.logger.info("getWebElementCount : Get webelement count");
	  return WebElementList.size();
	}    
    
public static int setEditBox(WebDriver driver,By locator,String userEnteredValue )
	{
	int functionReturnResult;
	functionReturnResult=-1;
		if (GenericKeywords.getWebElementCount(driver, locator)>0)
		{
	    	driver.findElement(locator).sendKeys(userEnteredValue);
	    	functionReturnResult=1;
	    	GenericKeywords.logger.info("setEditBox : Data entered");
		}
		else
		{
			functionReturnResult=-1;
			GenericKeywords.logger.info("setEditBox : Data does not entered");
			
		}
		return functionReturnResult;
	}
public static void clickButton(WebDriver driver,By locator )

	{
    	driver.findElement(locator).click();
    	GenericKeywords.logger.info("clickButton : Button have been clicked");
	}

public static Boolean isElementExist(WebDriver driver,By locator )
	{
	GenericKeywords.logger.info("isElementExist : Element Exist");	
	return driver.findElement(locator).isDisplayed();
    	
	}    
//*******************Excel Methods*****************
//"C:\\Selenium_Projects\\Test.xlsx"
/*public static String readExcelCellData(String filefullPath,String sheetname,int rownum, int columnnum) throws Exception
	{
		String cellValue;
		cellValue="";
		
		
		FileInputStream excelFileObject= new FileInputStream(new File(filefullPath));
		Workbook workbook=new XSSFWorkbook(excelFileObject);
		Sheet dataSheet=workbook.getSheet(sheetname);
		//System.out.println(""+dataSheet.getFirstRowNum());
		//System.out.println(""+dataSheet.getLastRowNum());
		//Iterator<Row> iterator=dataSheet.iterator();
		iterator.
		while(iterator.hasNext()) // iterator.hasNext()
		{
			
			
			
			
		}//end iterator.hasNext()
		
		workbook.close();
		return cellValue;

	}

public static int getExcelRowCountWithHeaderRow(String filefullPath, String sheetname)
{
	int rowCount;
	rowCount=0;
	
	
	FileInputStream excelFileObject;
	try {
		excelFileObject = new FileInputStream(new File(filefullPath));
		Workbook workbook=new XSSFWorkbook(excelFileObject);
		Sheet dataSheet=workbook.getSheet(sheetname);
		rowCount=dataSheet.getLastRowNum();
		workbook.close();
		
		}
	catch(Exception e)
	{
				
		System.out.println(""+ e.getMessage());
	}
	return rowCount;
}

public static int getExcelRowCountWithoutHeaderRow(String filefullPath, String sheetname)
{
	int rowCount;
	rowCount=0;
	
	
	FileInputStream excelFileObject;
	try {
		excelFileObject = new FileInputStream(new File(filefullPath));
		Workbook workbook=new XSSFWorkbook(excelFileObject);
		Sheet dataSheet=workbook.getSheet(sheetname);
		rowCount=dataSheet.getLastRowNum();
		workbook.close();
		
		if(rowCount>=1)
		{
			rowCount--;
			
		}
		else
		{
			rowCount=-1;	
		}
		
		
		}
	catch(Exception e)
	{
				
		System.out.println(""+ e.getMessage());
	}
	return rowCount;
}

public static int getExcelColumnCount(String filefullPath, String sheetname,int rowNum)
{
	int colCount;
	colCount=0;
	
	
	FileInputStream excelFileObject;
	try {
		excelFileObject = new FileInputStream(new File(filefullPath));
		Workbook workbook=new XSSFWorkbook(excelFileObject);
		Sheet dataSheet=workbook.getSheet(sheetname);
		colCount=dataSheet.getRow(rowNum).getPhysicalNumberOfCells();
		workbook.close();
		
		}
	catch(Exception e)
	{
				
		System.out.println(""+ e.getMessage());
	}
	return colCount;	
}


public static String getExcelCellValue(String filefullPath, String sheetname,int rowNum, int colnum)
{
	String cellValue;
	cellValue="";
	FileInputStream excelFileObject;
	try {
			excelFileObject = new FileInputStream(new File(filefullPath));
			Workbook workbook=new XSSFWorkbook(excelFileObject);
			Sheet dataSheet=workbook.getSheet(sheetname);
			Cell cell=dataSheet.getRow(rowNum).getCell(colnum);
			if(cell!=null)
			{
				switch(cell.getCellTypeEnum())
				{
					case NUMERIC:
						cellValue= "" + cell.getNumericCellValue();
						break;
					case STRING:
						cellValue=cell.getStringCellValue();
						break;
					default:
						break;
				}
			workbook.close();
			
			}
	}
	catch(Exception e)
	{
				
		System.out.println(""+ e.getMessage());
	}
	return cellValue;	
}*/

public static int getSQLRecordCount(String excelFileLocation,String SQLQuery)
{
	int localRecordCount;
	localRecordCount=0;
	
	Fillo fillo=new Fillo();
	Connection connection = null;
	try {
		connection = fillo.getConnection(excelFileLocation);
		Recordset recordset= connection.executeQuery(SQLQuery);
		localRecordCount=recordset.getCount();
		recordset.close();
		connection.close();
		GenericKeywords.logger.info("getSQLRecordCount : get Record counts ");
		
	} catch (Exception e) {
		localRecordCount=-1;
		System.out.println(e.getMessage());
		GenericKeywords.logger.info("getSQLRecordCount : Record counts in negative ");
	}
	
	return localRecordCount;
}


public static String getSQLRecord(String excelFileLocation,String SQLQuery,int recordCountNumber,String fieldName)
{
	String RecordValue;
	RecordValue="";
	int localRecordCount;
	
	Fillo fillo=new Fillo();
	Connection connection = null;
	try {
		connection = fillo.getConnection(excelFileLocation);
		Recordset recordset= connection.executeQuery(SQLQuery);
		localRecordCount=recordset.getCount();
		
		if(localRecordCount>0)//if(localRecordCount>=0)
		{
			if (localRecordCount>=recordCountNumber)// (localRecordCount>=recordCountNumber)
			{
				for (int i = 1;i<=recordCountNumber;i++)
				{	
					recordset.next();
				}
				GenericKeywords.logger.info("getSQLRecord : get SQL Record details ");
				RecordValue=recordset.getField(fieldName);
			}
			else// (localRecordCount>=recordCountNumber)
			{
				GenericKeywords.logger.info("getSQLRecord :  SQL Record details in negative ");
				RecordValue= "-1";
			}
		}
		else//if(localRecordCount>=0)
		{
			GenericKeywords.logger.info("getSQLRecord :  SQL Record details is zero ");
			RecordValue= ""+0;
		}
		recordset.close();
		connection.close();
	} catch (Exception e) {
		RecordValue= "-1";
		//System.out.println(e.getMessage());
	}
	return RecordValue;
}

public static int executeKeyWord(Driver dobj)
{
	int keywordExecutionStatus;
	keywordExecutionStatus=1;
	
/*		if(!(dobj.TestStep_TC_ID.isEmpty()) && !(dobj.TestStep_ID.isEmpty()))
		{*/
			//System.out.println("abc");
	GenericKeywords.logger.info("executeKeyWord :  Keyword execution Start ");
			keywordExecutionStatus=KeywordMapping.keywordActionMapping(dobj);
/*		}
		else
		{
			
		}*/

	return keywordExecutionStatus;
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


public static void takeScreenShot(WebDriver driver,String FileName)
{
	try {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(FileName));
	} 
	catch (Exception e) {
		System.out.println("screenprint error :"+e.getMessage());
	}
	
}

public static XWPFDocument createWordDocument(String FileName)
{
	XWPFDocument document=null;
	File fileObj=new File(FileName);
	
	if(!fileObj.exists())
	{
		document= new XWPFDocument();
		return document;	
	}
	else
	{
		try {
			FileInputStream fis = new FileInputStream(FileName);
			document = new XWPFDocument(OPCPackage.open(fis));
			//XWPFParagraph subTitle = document.createParagraph();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Parag Parag "+e.getMessage());
		} 
	
		return document; 
		
	}

}
public static  void writeToWordDocument(XWPFDocument document,String FileName)
{
	try{
		FileOutputStream out= new FileOutputStream(new File(FileName));
		document.write(out);
		out.close();
		
	}catch(Exception e){
		System.out.println(e);
	}
	
}

public static void writeTextToWordDocument(XWPFDocument document,String TextToAdd,int alignment)
{
	XWPFParagraph subTitle = document.createParagraph();
	//subTitle.setBorderLeft(Borders.BASIC_THIN_LINES);
	
	if(alignment==0)
	{
		subTitle.setAlignment(ParagraphAlignment.LEFT);
	}
	else if(alignment==1)
	{
		subTitle.setAlignment(ParagraphAlignment.CENTER);
		
	}
	XWPFRun subTitleRun = subTitle.createRun();
	subTitleRun.setFontSize(16);
	subTitleRun.setBold(true);
	subTitleRun.setUnderline(UnderlinePatterns.SINGLE);
	subTitleRun.setText(TextToAdd.trim());
		
}


public static void addBreakToWordDocument(XWPFDocument document,String breaktype)
{
	XWPFParagraph subTitle = document.createParagraph();
	//subTitle.setBorderLeft(Borders.BASIC_THIN_LINES);
	XWPFRun subTitleRun = subTitle.createRun();
	if(breaktype.equalsIgnoreCase("page"))
	{
		subTitleRun.addBreak(BreakType.PAGE);
	}
	else if(breaktype.equalsIgnoreCase("line"))
	{
		subTitleRun.addBreak(BreakType.TEXT_WRAPPING);
	}	
}

public static void addImageToWordDocument(XWPFDocument document,String imageFileName)
{
	
	XWPFParagraph subTitle = document.createParagraph();
	subTitle.setAlignment(ParagraphAlignment.LEFT);
	
	subTitle.setBorderBottom(Borders.BASIC_THIN_LINES);
	subTitle.setBorderTop(Borders.BASIC_THIN_LINES);
	subTitle.setBorderLeft(Borders.BASIC_THIN_LINES);
	subTitle.setBorderRight(Borders.BASIC_THIN_LINES);
	
	XWPFRun subTitleRun = subTitle.createRun();
	try {
		FileInputStream pic=new FileInputStream(imageFileName);
		
		
		subTitleRun.addPicture(pic, XWPFDocument.PICTURE_TYPE_PNG, imageFileName, Units.toEMU(450), Units.toEMU(300));
		//subTitleRun.addPicture(pic, XWPFDocument.PICTURE_TYPE_PNG, imageFileName, 7, 6);
		
		subTitleRun.addBreak(BreakType.PAGE);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println(e.getMessage());
	}
		
}

public static void deleteFile(String FileName)
{
	try{

		File file = new File(FileName);

		if(file.exists()){
			file.delete();
			//System.out.println(file.getName() + " is deleted!");
		}else{
			System.out.println("Delete operation failed.");
		}

	}catch(Exception e){

		e.printStackTrace();

	}
	
}

public static XWPFTable createTableToWordDocument(XWPFDocument document,String dataValue,long bigIntegerValue,String alignmentValue)
{
	XWPFTableCell VCell;
	
	XWPFTable table = document.createTable();
	VCell=table.getRow(0).getCell(0);
	VCell.setVerticalAlignment(XWPFVertAlign.TOP);
	
	XWPFParagraph paragraph=VCell.addParagraph();
	XWPFRun run = paragraph.createRun();
	if(alignmentValue.equalsIgnoreCase("left"))
	{
		paragraph.setAlignment(ParagraphAlignment.LEFT);
		
	}
	else if (alignmentValue.equalsIgnoreCase("center"))
	{
		paragraph.setAlignment(ParagraphAlignment.CENTER);	
	}
	
	paragraph.setVerticalAlignment(TextAlignment.TOP);
	paragraph.setWordWrapped(true);
	
	run.setText(dataValue.trim());
	
	//.setText(dataValue);
	table.getRow(0).getCell(0).getCTTc().addNewTcPr().addNewTcW().setType(STTblWidth.DXA);
	table.getRow(0).getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(bigIntegerValue));
	
	return 	table;
}
public static  XWPFTableRow createTableRowToWordDocument(XWPFTable table)
{
	
	XWPFTableRow tableRow = table.createRow();
	//tableRow.setHeight(1);
	return tableRow;	
}




public static  void addCellAndSetCellValueOfTableToWordDocument(XWPFTable table,int RowNumber,String DataValue,long bigIntegerValue,String alignmentValue)
{
	XWPFTableCell VCell;
	
	VCell=table.getRow(RowNumber).createCell();
	VCell.setVerticalAlignment(XWPFVertAlign.TOP);
	XWPFParagraph paragraph=VCell.addParagraph();
	paragraph.setAlignment(ParagraphAlignment.LEFT);
	XWPFRun run = paragraph.createRun();
	run.setText(DataValue.trim());
	
	if(alignmentValue.equalsIgnoreCase("left"))
	{
		paragraph.setAlignment(ParagraphAlignment.LEFT);	
	}
	else if (alignmentValue.equalsIgnoreCase("center"))
	{
		paragraph.setAlignment(ParagraphAlignment.CENTER);	
	}
	paragraph.setVerticalAlignment(TextAlignment.TOP);
	paragraph.setWordWrapped(true);
	//VCell.setText(DataValue);
	VCell.getCTTc().addNewTcPr().addNewTcW().setType(STTblWidth.DXA);
	VCell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(bigIntegerValue));
	
	
	//tableRow.createCell().setText(DataValue);
}

public static  void SetCellValueOfTableToWordDocument(XWPFTable table,int CellRowNumber,int CellColumnNumber,String DataValue,String alignmentValue)
{
	XWPFTableCell VCell;
	
	VCell=table.getRow(CellRowNumber).getCell(CellColumnNumber);
	VCell.setVerticalAlignment(XWPFVertAlign.TOP);
	XWPFParagraph paragraph=VCell.addParagraph();
	paragraph.setAlignment(ParagraphAlignment.LEFT);
	paragraph.setWordWrapped(true);
	XWPFRun run = paragraph.createRun();
	run.setText(DataValue.trim());
	//GenericKeywords.deleteTableCellTextOfTableToWordDocument(table,CellRowNumber,CellColumnNumber,DataValue);
	if(alignmentValue.equalsIgnoreCase("left"))
	{
		paragraph.setAlignment(ParagraphAlignment.LEFT);	
	}
	else if (alignmentValue.equalsIgnoreCase("center"))
	{
		paragraph.setAlignment(ParagraphAlignment.CENTER);	
	}
	paragraph.setVerticalAlignment(TextAlignment.TOP);
	
	//.setText(DataValue);
	//tableRow.createCell().setText(DataValue);
}

public static  void deleteTableCellTextOfTableToWordDocument(XWPFTable table,int CellRowNumber,int CellColumnNumber,String DataValue)
{
	XWPFTableCell VCell;
	XWPFParagraph currectParagraph;
	List<XWPFParagraph> existingParagraphList;
	XWPFDocument doc;
	int paragraphPosition;
	
	VCell=table.getRow(CellRowNumber).getCell(CellColumnNumber);
	existingParagraphList=VCell.getParagraphs();
	//System.out.println("Size : "+existingParagraphList.size());
	if(existingParagraphList.size()>0)
		{
			for(int pg=0;pg<=existingParagraphList.size()-1;pg++)
			{
				//System.out.println("Size Insite Loop");
				currectParagraph=existingParagraphList.get(pg);
				if (currectParagraph.getText().equalsIgnoreCase(DataValue))
				{
					//System.out.println("Size Insite If");
					doc=currectParagraph.getDocument();
					paragraphPosition=doc.getPosOfParagraph(currectParagraph);
					doc.removeBodyElement(paragraphPosition);	
				}
			}
		}
}


//****need to work
public static  int getWordDocumentTableColumnIndex(XWPFTable table,int CellRowNumber,String cellDataValue)
{
	int columnIndex;
	columnIndex=-1;
	List valueDetails;
	XWPFTableCell VCell;
	//VCell.get
	//VCell.
	valueDetails= table.getRow(CellRowNumber).getTableCells();

		for(int ik=0;ik<valueDetails.size();ik++)
		{
			//System.out.println(" vaue 123: " + table.getRow(CellRowNumber).getCell(ik).getText());
			
		}

	return columnIndex;
}

//********************

public static int getWordTableRowIndex(XWPFTable table,String fieldValue)
{
	int rowIndex;
	String textValue;
	rowIndex=-1;
	List<XWPFTableCell> cells;
	
	for(int i=0;i<table.getNumberOfRows();i++)
	{
		cells = table.getRow(i).getTableCells();
		textValue=cells.get(0).getText().toString();
		
		//System.out.println("humana : "+textValue);
		if(fieldValue.equalsIgnoreCase(table.getRow(i).getCell(0).getText()))
		{
		
			break;	
		}
	}
return rowIndex;

}

public static void setRowOrColumnColor(XWPFTable table, String RowColumn, String coloreCode)
{
	if (RowColumn.equalsIgnoreCase("row"))
	{
		for(int i=0;i<table.getRow(0).getTableCells().size();i++)
		{
			table.getRow(0).getCell(i).setColor(coloreCode);
			
		}
		
	}
	else if (RowColumn.equalsIgnoreCase("column"))
	{
		for(int j=0;j<table.getNumberOfRows();j++)
		{
			table.getRow(j).getCell(0).setColor(coloreCode);
			
		}
		
	}
	
	
	
}

public static void waitForTime(WebDriver driver, int timeInSeconds)
{
	WebElement element;
	WebDriverWait wait;
	element=null;
	wait=null;
	
	try {
	wait=  new WebDriverWait(driver,timeInSeconds);
	element=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//Parag")));

	} catch (Exception e) {

		System.out.println("Hero :"+ e.getMessage());
	}
	
	
}

/*public static void waitForObjectToLoad(WebDriver driver,WebElement element,int timeToWait)
{
	WebDriverWait wait=new WebDriverWait(driver,timeToWait);
	wait.until(ExpectedConditions.visibilityOf(element));
	

}*/




}
