package itqs;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TNGTest {

Driver DriverObject;	
	
@BeforeTest
public void Init()
{
	String[] args= new String[] {"Hello"};
	Driver.main(args);
}
	
@Test
public void TestAB()
{
}

@AfterTest
public void finalCode()
{

}




}
