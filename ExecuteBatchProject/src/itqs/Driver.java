package itqs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.openqa.selenium.WebDriver;

public class Driver {
	
	static WebDriver driver;
	//*****Declare Instance Variables****Start
	int Test_Case_Record_Count,Data_Record_Count,TestStep_Record_Count,TC_Loop_Count,TestStep_Loop_Count,Data_Loop_Count,dobj_Final_Execution_Result;
	int step_Pass_Flag,step_Fail_Flag,TestScript_RunNumber_Fail_Flag;
	String TestScript_Excel_Location,TestDataExcelLocation,TestCase_ID,TestCase_Name,TestCase_Description;
	String TestStep_TC_ID,TestStep_ID,TestStep_Description,TestStep_Expected,TestStep_Actual_PassCondition,TestStep_Actual_FailCondition,TestStep_Keyword_Used;
	String TestStep_ObjectName,TestStep_TestDataAvailable,TestStep_Screenshot_Flag,ScreenShot_File_Location;
	String TestCase_SQL_Query,Data_SQL_Query,TestStep_SQL_Query;
	String Data_Step_Value,Data_Run_ID;
	String TestCaseLevel,TestStepLevel,OverAllTestLevel;
	String coloreCode;
	XWPFDocument document;
	XWPFTable table,overallExecutionTable;
	String wordDocumentFileName;
	Map<String,XWPFTable> tableNameMap;
	Map<String,XWPFTable> StepTableNameMap;
	long BigNumber_overallExecutionTable,BigNumber_tableNameMap,BigNumber_StepTableNameMap;
	static Logger logger;
	//*****Declare Instance Variables****End
	//*****Initialize Instance Variables****Start
	public void InitilizeDriverVariables()
	{
		this.document=null;
		this.table=null;
		this.overallExecutionTable=null;
		this.TestScript_Excel_Location="";
		this.TestDataExcelLocation="";
		this.TestCase_ID="";
		this.TestCase_Name="";
		this.TestCase_Description="";
		this.TestCase_SQL_Query="";
		this.Data_SQL_Query="";
		this.TestStep_SQL_Query="";
		this.TestStep_ID="";
		this.TestStep_Screenshot_Flag="";
		this.TestStep_TC_ID="";
		this.Data_Step_Value="";
		this.Data_Run_ID="";
		this.tableNameMap= new HashMap<String,XWPFTable>();
		this.StepTableNameMap=new HashMap<String,XWPFTable>();
		this.TC_Loop_Count=1;
		this.TestStep_Loop_Count=1;
		this.Data_Loop_Count=1;
		this.Test_Case_Record_Count=0;
		this.Data_Record_Count=0;
		this.TestStep_Record_Count=0;
		this.dobj_Final_Execution_Result=1;
		this.coloreCode="b3b3b3";
		this.step_Pass_Flag=0;
		this.step_Fail_Flag=0;
		this.TestScript_RunNumber_Fail_Flag=0;
		this.TestCase_SQL_Query="select * from Test_Case where Execution_Flag='Y'";
		this.TestCaseLevel="Test Case Name,Test Case Description,Run Number,Run Status,Execution Date,Execution Start Time,Total Time Taken";
		this.TestStepLevel="Step Description,Step Expected,Step Actual,Step Status";
		//this.TestScript_Excel_Location=dataSheetLocation_Local;//".//Data//Automation_Test_Script.xls";
		this.TestScript_Excel_Location=".//Data//Automation_Test_Script.xls";
		this.wordDocumentFileName=".//Result//ExecutionReport.docx";//".//Data//ExecutionReport.docx";
		this.ScreenShot_File_Location=".//Result//SC.png";//".//Data//SC.png";
		this.TestDataExcelLocation=TestScript_Excel_Location;
		this.OverAllTestLevel="";
		this. BigNumber_overallExecutionTable=2000;
		this.BigNumber_tableNameMap=5000;
		this.BigNumber_StepTableNameMap=5000;
		//***Log information
		
		PropertyConfigurator.configure("./Resources/log4j.properties");
		Driver.logger=Logger.getLogger("Driver");
		//**Log End Information
	}
	//*****Initialize Instance Variables****End
	
	public static void main(String[] args) {
		Driver dobj=new Driver();
		dobj.InitilizeDriverVariables();
		Driver.logger.info("Driver Variables are Initialized");
		GenericKeywords.Initialize();
		GenericKeywords.deleteFile(dobj.wordDocumentFileName);
		GenericKeywords.deleteFile(dobj.ScreenShot_File_Location);
		
		
		dobj.Test_Case_Record_Count=GenericKeywords.getSQLRecordCount(dobj.TestScript_Excel_Location,dobj.TestCase_SQL_Query);//Get Test Case tab record count with execution flag "Y"
		if(dobj.Test_Case_Record_Count>0)//Test_Case_Record_Count : if there is any test case in Test Case Tab with execution flag "Y"
		{
			//******word document : Create Overall Execution Table and set Header details ****Start
			dobj.document=GenericKeywords.createWordDocument(dobj.wordDocumentFileName);
			GenericKeywords.writeTextToWordDocument(dobj.document, "Test Automation Execution Report",1);
			GenericKeywords.addBreakToWordDocument(dobj.document, "line");
			GenericKeywords.writeTextToWordDocument(dobj.document, "Overall Test Automation Execution Report",0);
			dobj.overallExecutionTable=GenericKeywords.createTableToWordDocument(dobj.document, "Test Case ID",2000,"center");
			GenericKeywords.addCellAndSetCellValueOfTableToWordDocument(dobj.overallExecutionTable, 0, "Test Case Name",1000,"center");
			GenericKeywords.addCellAndSetCellValueOfTableToWordDocument(dobj.overallExecutionTable, 0, "Description",1000,"center");
			GenericKeywords.addCellAndSetCellValueOfTableToWordDocument(dobj.overallExecutionTable, 0, "Run Number",1000,"center");
			GenericKeywords.addCellAndSetCellValueOfTableToWordDocument(dobj.overallExecutionTable, 0, "Run Status",1000,"center");
			GenericKeywords.setRowOrColumnColor(dobj.overallExecutionTable,"row",dobj.coloreCode);
			GenericKeywords.addBreakToWordDocument(dobj.document, "page");
			GenericKeywords.writeToWordDocument(dobj.document, dobj.wordDocumentFileName);
			//******word document : Create Overall Execution Table and set Header details ****End
			
			while(dobj.TC_Loop_Count<=dobj.Test_Case_Record_Count)//(TC_Loop_Count<=Test_Case_Record_Count)
			{
				//****Get Test Case tab test script details***Start
				dobj.TestCase_ID=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestCase_SQL_Query, dobj.TC_Loop_Count, "TC_ID");
				dobj.TestCase_Name=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestCase_SQL_Query, dobj.TC_Loop_Count, "TC_Name");
				dobj.TestCase_Description=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestCase_SQL_Query, dobj.TC_Loop_Count, "TC_Description");
				//****Get Test Case tab test script details**End
				
				//System.out.println("count :"+dobj.TC_Loop_Count+" :- "+dobj.TestCase_ID+","+dobj.TestCase_Name+","+dobj.TestCase_Description );
				
				//***get test data details with execution flag "Y"***Start
				dobj.Data_SQL_Query="select * from "+dobj.TestCase_ID+"_DATASHEET where Execution_Flag='Y'";
				dobj.TestStep_SQL_Query="select * from "+dobj.TestCase_ID;
				dobj.Data_Record_Count=GenericKeywords.getSQLRecordCount(dobj.TestDataExcelLocation,dobj.Data_SQL_Query);
				//***get test data details with execution flag "Y"***End
				
				if(dobj.Data_Record_Count>0)//Data_Record_Count>0: If there is any data in datasheet with execution flag "Y"
				{
					dobj.TestStep_Record_Count=GenericKeywords.getSQLRecordCount(dobj.TestScript_Excel_Location,dobj.TestStep_SQL_Query);// Get test scripts Step count
					while(dobj.Data_Loop_Count<=dobj.Data_Record_Count)//Data_Loop_Count<=Data_Record_Count : Loop through test data set
					{
						dobj.Data_Run_ID=GenericKeywords.getSQLRecord(dobj.TestDataExcelLocation, dobj.Data_SQL_Query, dobj.Data_Loop_Count, "Iteration");
						
						//******word document : Enter values in Overall execution Table***Start
						GenericKeywords.createTableRowToWordDocument(dobj.overallExecutionTable);
						GenericKeywords.SetCellValueOfTableToWordDocument(dobj.overallExecutionTable, dobj.overallExecutionTable.getNumberOfRows()-1, 0, dobj.TestCase_ID,"left");
						GenericKeywords.SetCellValueOfTableToWordDocument(dobj.overallExecutionTable, dobj.overallExecutionTable.getNumberOfRows()-1, 1, dobj.TestCase_Name,"left");
						GenericKeywords.SetCellValueOfTableToWordDocument(dobj.overallExecutionTable, dobj.overallExecutionTable.getNumberOfRows()-1, 2, dobj.TestCase_Description,"left");
						GenericKeywords.SetCellValueOfTableToWordDocument(dobj.overallExecutionTable, dobj.overallExecutionTable.getNumberOfRows()-1, 3, dobj.Data_Run_ID,"left");
						
						GenericKeywords.writeToWordDocument(dobj.document, dobj.wordDocumentFileName);
						//******word document : Enter values in Overall execution Table***End
						if(dobj.TestStep_Record_Count>0)//TestStep_Record_Count>0' : If test steps exist for the test script
						{
							//***WordLIST : ISSUE Here
							GenericKeywords.writeTextToWordDocument(dobj.document, "Execution Status",0);
							//System.out.println("HashMap"+dobj.TestCase_ID+"_"+dobj.Data_Run_ID);
							// Create Test Script tables handle and put into hashtable
							dobj.tableNameMap.put(dobj.TestCase_ID+"_"+dobj.Data_Run_ID, GenericKeywords.createTableToWordDocument(dobj.document, "Test Case ID",dobj.BigNumber_tableNameMap,"left"));
							//GenericKeywords.addCellAndSetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID), dobj.Data_Loop_Count-1, dobj.TestCase_ID);
							GenericKeywords.addCellAndSetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID),0, dobj.TestCase_ID,dobj.BigNumber_tableNameMap,"left");
							//*****set Test Script level tables header ***Start
							for(int j=0;j<dobj.TestCaseLevel.split(",").length;j++)
							{
								GenericKeywords.createTableRowToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID));
								GenericKeywords.SetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID), dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID).getNumberOfRows()-1, 0, dobj.TestCaseLevel.split(",")[j],"left");
							}
							//*****set Test Script level tables header ***End
							//*****set Test Script level tables Values ***Start
							//GenericKeywords.addCellAndSetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID), 1, dobj.TestCase_ID);
							GenericKeywords.SetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID), 1, 1,dobj.TestCase_Name,"left" );
							GenericKeywords.SetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID), 2, 1,dobj.TestCase_Description,"left" );
							GenericKeywords.SetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID), 3, 1,dobj.Data_Run_ID,"left" );
							GenericKeywords.SetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID), 5, 1,new SimpleDateFormat("dd-MM-yyyy").format(new Date() ),"left");
							GenericKeywords.SetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID), 6, 1,new SimpleDateFormat("HH:mm:ss").format(new Date() ),"left");
							GenericKeywords.setRowOrColumnColor(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID),"column",dobj.coloreCode);
							//System.out.println("Table Count : " + GenericKeywords.getWordTableRowIndex(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID),"Test Case Name"));
							GenericKeywords.writeToWordDocument(dobj.document, dobj.wordDocumentFileName);						
							//********set Test Script level tables Values ***End
							while(dobj.TestStep_Loop_Count<=dobj.TestStep_Record_Count)//TestStep_Loop_Count<=TestStep_Record_Count
							{
								dobj.TestStep_TC_ID=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestStep_SQL_Query, dobj.TestStep_Loop_Count, "TC_ID");
								dobj.TestStep_ID=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestStep_SQL_Query, dobj.TestStep_Loop_Count, "TS_ID");
								dobj.TestStep_Description=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestStep_SQL_Query, dobj.TestStep_Loop_Count, "TS_Description");
								dobj.TestStep_Expected=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestStep_SQL_Query, dobj.TestStep_Loop_Count, "Expected");
								dobj.TestStep_Actual_PassCondition=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestStep_SQL_Query, dobj.TestStep_Loop_Count, "Actual_Result_PASSED_Condition");
								dobj.TestStep_Actual_FailCondition=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestStep_SQL_Query, dobj.TestStep_Loop_Count, "Actual_Result_FAILED_Condition");
								dobj.TestStep_Keyword_Used=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestStep_SQL_Query, dobj.TestStep_Loop_Count, "Keyword_Used");
								dobj.TestStep_ObjectName=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestStep_SQL_Query, dobj.TestStep_Loop_Count, "ObjectName");
								dobj.TestStep_TestDataAvailable=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestStep_SQL_Query, dobj.TestStep_Loop_Count, "TestDataAvailable");
								dobj.TestStep_Screenshot_Flag=GenericKeywords.getSQLRecord(dobj.TestScript_Excel_Location, dobj.TestStep_SQL_Query, dobj.TestStep_Loop_Count, "ScreenshotFlag");
								if(dobj.TestStep_TestDataAvailable.equals("1")||dobj.TestStep_TestDataAvailable.equalsIgnoreCase("Y"))//dobj.TestStep_TestDataAvailable.equals("1")
								{
									dobj.Data_Step_Value=GenericKeywords.getSQLRecord(dobj.TestDataExcelLocation, dobj.Data_SQL_Query, dobj.Data_Loop_Count, dobj.TestStep_ID);	
								}
								else//dobj.TestStep_TestDataAvailable.equals("1")
								{
									dobj.Data_Step_Value="";	
								}
								
								
								dobj.dobj_Final_Execution_Result=GenericKeywords.executeKeyWord(dobj);
								
								
								if(!dobj.TestStep_Description.equalsIgnoreCase(""))//!dobj.TestStep_Description.equalsIgnoreCase(null)
								{	
									System.out.println("Paaaa : " +dobj.TestStep_Description );
									//***wORD
									//System.out.println("StepTableNameMap"+dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID);
									GenericKeywords.addBreakToWordDocument(dobj.document, "line");
									dobj.StepTableNameMap.put(dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID, GenericKeywords.createTableToWordDocument(dobj.document, "Step ID",dobj.BigNumber_StepTableNameMap,"left"));
									GenericKeywords.addCellAndSetCellValueOfTableToWordDocument(dobj.StepTableNameMap.get(dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID),0, dobj.TestStep_ID,dobj.BigNumber_StepTableNameMap,"left");
									for(int j=0;j<dobj.TestStepLevel.split(",").length;j++)
									{
										GenericKeywords.createTableRowToWordDocument(dobj.StepTableNameMap.get(dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID));
										GenericKeywords.SetCellValueOfTableToWordDocument(dobj.StepTableNameMap.get(dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID), dobj.StepTableNameMap.get(dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID).getNumberOfRows()-1, 0, dobj.TestStepLevel.split(",")[j],"left");
									}
									GenericKeywords.SetCellValueOfTableToWordDocument(dobj.StepTableNameMap.get(dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID), 1, 1,dobj.TestStep_Description,"left" );
									GenericKeywords.SetCellValueOfTableToWordDocument(dobj.StepTableNameMap.get(dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID), 2, 1,dobj.TestStep_Expected,"left" );
									if (dobj.dobj_Final_Execution_Result==1 )
									{
										GenericKeywords.SetCellValueOfTableToWordDocument(dobj.StepTableNameMap.get(dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID), 3, 1,dobj.TestStep_Actual_PassCondition,"left" );	
										GenericKeywords.SetCellValueOfTableToWordDocument(dobj.StepTableNameMap.get(dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID), 4, 1,"PASSED","left" );
										dobj.step_Pass_Flag=1;
										//GenericKeywords.SetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID), 4, 1,"PASSED","left" );
									}
									else if(dobj.dobj_Final_Execution_Result==0)
									{
										GenericKeywords.SetCellValueOfTableToWordDocument(dobj.StepTableNameMap.get(dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID), 3, 1,dobj.TestStep_Actual_FailCondition,"left" );
										GenericKeywords.SetCellValueOfTableToWordDocument(dobj.StepTableNameMap.get(dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID), 4, 1,"FAILED","left" );
										dobj.step_Fail_Flag=1;
										//GenericKeywords.SetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID), 4, 1,"FAILED","left" );
									}
									GenericKeywords.setRowOrColumnColor(dobj.StepTableNameMap.get(dobj.TestCase_ID+"_"+dobj.TestStep_ID+"_"+dobj.Data_Run_ID),"column",dobj.coloreCode);
									GenericKeywords.addBreakToWordDocument(dobj.document, "line");
									if(dobj.TestStep_Screenshot_Flag.equals("1"))
									{
										GenericKeywords.takeScreenShot(Driver.driver,dobj.ScreenShot_File_Location);
										GenericKeywords.addImageToWordDocument(dobj.document,dobj.ScreenShot_File_Location);
										GenericKeywords.deleteFile(dobj.ScreenShot_File_Location);
									}
									else
									{
										
										
									}
									
									
									GenericKeywords.writeToWordDocument(dobj.document, dobj.wordDocumentFileName);						
									//***wORD
								}//!dobj.TestStep_Description.equalsIgnoreCase(null)
								
								dobj.Data_Step_Value="";
								dobj.TestStep_Loop_Count++;
								
							}//TestStep_Loop_Count<=TestStep_Record_Count
							
							if(dobj.step_Fail_Flag==1)
							{
								GenericKeywords.SetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID), 4, 1,"FAILED","left" );
								GenericKeywords.writeToWordDocument(dobj.document, dobj.wordDocumentFileName);
								dobj.step_Pass_Flag=0;
								dobj.TestScript_RunNumber_Fail_Flag=1;
							}
							else if (dobj.step_Pass_Flag==1)
							{
								
								GenericKeywords.SetCellValueOfTableToWordDocument(dobj.tableNameMap.get(dobj.TestCase_ID+"_"+dobj.Data_Run_ID), 4, 1,"PASSED","left" );
								GenericKeywords.writeToWordDocument(dobj.document, dobj.wordDocumentFileName);
							}
							dobj.step_Pass_Flag=0;
							dobj.step_Fail_Flag=0;
							
							//************yaha
							if(dobj.TestScript_RunNumber_Fail_Flag==1)
							{
								GenericKeywords.SetCellValueOfTableToWordDocument(dobj.overallExecutionTable, dobj.overallExecutionTable.getNumberOfRows()-1, 4, "FAILED","left");
								GenericKeywords.writeToWordDocument(dobj.document, dobj.wordDocumentFileName);
								dobj.TestScript_RunNumber_Fail_Flag=0;
							}
							else
							{
								GenericKeywords.SetCellValueOfTableToWordDocument(dobj.overallExecutionTable, dobj.overallExecutionTable.getNumberOfRows()-1, 4, "PASSED","left");
								GenericKeywords.writeToWordDocument(dobj.document, dobj.wordDocumentFileName);
							}
							
						}
						else//TestStep_Record_Count>0
						{
							System.out.println("Test Step not found");
						}
						dobj.TestStep_Loop_Count=1;
						dobj.Data_Loop_Count++;	
					}//Data_Loop_Count<=Data_Record_Count

				}
				else//(Data_Record_Count>0
				{
					System.out.println("Data record count is 0");
				}
				dobj.Data_Loop_Count=1;
				dobj.TC_Loop_Count++;	
			}//(TC_Loop_Count<=Test_Case_Record_Count)
		}
		else//Test_Case_Record_Count
		{
			
			System.out.println("no test script to execute");
		}
		dobj.Data_SQL_Query="";
		try {
			dobj.document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
		GenericKeywords.deleteFile(dobj.ScreenShot_File_Location);
	}
	
	
}
