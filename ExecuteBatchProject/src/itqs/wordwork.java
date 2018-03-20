package itqs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public class wordwork {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XWPFDocument document;
		XWPFTable table;
		XWPFTableRow tableRow1,tableRow2;
		String FileName;
		FileName="./Data/ABC.docx";
		
		wordwork ww=new wordwork();
		
		document=ww.createWordDocument(FileName);
		
		/*wordwork.writeTextToWordDocument(document, "Test Automation Execution Report");
		wordwork.writeTextToWordDocument(document, "Test Level Details");*/
		
		table=wordwork.createTableToWordDocument(document,"Test Case Name");
		/*wordwork.addCellAndSetCellValueOfTableToWordDocument(table, 0, "TC_01");
		tableRow1=wordwork.createTableRowToWordDocument(table);
		wordwork.SetCellValueOfTableToWordDocument(table, 1, 0, "Step Name");
		wordwork.SetCellValueOfTableToWordDocument(table, 1, 1, "Step_01");*/
/*		tableRow1=wordwork.createTableRowToWordDocument(table);
		wordwork.SetCellValueOfTableToWordDocument(table, 1, 1, "Shruti");*/
		/*wordwork.SetTableRowToWordDocument(tableRow1, "Hamood");
		wordwork.SetTableRowToWordDocument(tableRow1, "ghoshal");
		wordwork.SetTableRowToWordDocument(tableRow1, "hurre");*/
		
/*		tableRow2=wordwork.createTableRowToWordDocument(table);*/
		/*wordwork.SetTableRowToWordDocument(tableRow2, "Hamood");
		wordwork.SetTableRowToWordDocument(tableRow2, "ghoshal");
		wordwork.SetTableRowToWordDocument(tableRow2, "hurre");*/
		
		
		wordwork.writeToWordDocument(document, FileName);
	
		
		

	}
	
	
	public XWPFDocument createWordDocument(String FileName)
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Parag Parag "+e.getMessage());
			} 
			return document; 
			
		}

	}
	public static void writeToWordDocument(XWPFDocument document,String FileName)
	{
		try{
			FileOutputStream out= new FileOutputStream(new File(FileName));
			document.write(out);
			out.close();
			document.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
	}
	
	public static void writeTextToWordDocument(XWPFDocument document,String TextToAdd)
	{
		XWPFParagraph subTitle = document.createParagraph();
		
		subTitle.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun subTitleRun = subTitle.createRun();
		subTitleRun.setText(TextToAdd);
			
	}
	
	public static XWPFTable createTableToWordDocument(XWPFDocument document,String dataValue)
	{
		XWPFTable table = document.createTable();
		table.getRow(0).getCell(0).setText(dataValue);
		table.getRow(0).getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(4000));
		table.getRow(0).getCell(0).getCTTc().addNewTcPr().addNewTcW().setType(STTblWidth.DXA);
		
		return 	table;
	}
	public static XWPFTableRow createTableRowToWordDocument(XWPFTable table)
	{
		
		XWPFTableRow tableRow = table.createRow();
		return tableRow;	
	}
	

	
	public static void addCellAndSetCellValueOfTableToWordDocument(XWPFTable table,int RowNumber,String DataValue)
	{
		table.getRow(RowNumber).createCell().setText(DataValue);
		//tableRow.createCell().setText(DataValue);
	}
	
	public static void SetCellValueOfTableToWordDocument(XWPFTable table,int CellRowNumber,int CellColumnNumber,String DataValue)
	{
		table.getRow(CellRowNumber).getCell(CellColumnNumber).setText(DataValue);
		//tableRow.createCell().setText(DataValue);
	}
	
	}
	
	
	
	

