package DriverFactory;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CommonFunLibrary.CommonFunction;
import Utilities.ExcelFileUtil;

public class DriverScript{
	WebDriver driver;
	ExtentReports report;
	ExtentTest test;
	String inputpath="F:\\SELENIUM\\OJTProjectTesting\\ERP_StockMaven\\Testinput\\Maven_Project_input.xlsx";
	String outputpath="F:\\SELENIUM\\OJTProjectTesting\\ERP_StockMaven\\Testoutput\\MavenResults.xlsx";
	public void startTest()throws Throwable
	{
		//access excel methods
		ExcelFileUtil xl = new ExcelFileUtil(inputpath);
		//iterating all row in MasterTestCases sheet
		for(int i=1; i<=xl.rowCount("MasterTestCases");i++)
		{
			if(xl.getCellData("MasterTestCases", i, 2).equalsIgnoreCase("Y"))
			{
				//store module name into TCModule
				String TCModule=xl.getCellData("MasterTestCases", i, 1);
				//generate report under project
		report= new ExtentReports("./ExtentReports//"+TCModule+CommonFunction.generateDate()+".html");
				//iterate all rows in TCModule sheet
				for(int j=1;j<=xl.rowCount(TCModule);j++)
				{
					//start test
					test=report.startTest(TCModule);
					//read all cell from TCModule
					String Description=xl.getCellData(TCModule, j, 0);
					String Function_Name=xl.getCellData(TCModule, j, 1);
					String Locator_Type=xl.getCellData(TCModule, j, 2);
					String Locator_Value=xl.getCellData(TCModule, j, 3);
					String Test_Data=xl.getCellData(TCModule, j, 4);
					try {
						if(Function_Name.equalsIgnoreCase("startBrowser"))
						{
							driver=CommonFunction.startBrowser();
							test.log(LogStatus.INFO, Description);
						}
						else if(Function_Name.equalsIgnoreCase("openApplication"))
						{
							CommonFunction.openApplication(driver);
							test.log(LogStatus.INFO, Description);
						}
						else if(Function_Name.equalsIgnoreCase("waitForElement"))
						{
							CommonFunction.waitForElement(driver, Locator_Type, Locator_Value, Test_Data);
							test.log(LogStatus.INFO, Description);
						}
						else if(Function_Name.equalsIgnoreCase("typeAction"))
						{
							CommonFunction.typeAction(driver, Locator_Type, Locator_Value, Test_Data);
							test.log(LogStatus.INFO, Description);
						}
						else if(Function_Name.equalsIgnoreCase("clickAction"))
						{
							CommonFunction.clickAction(driver, Locator_Type, Locator_Value);
							test.log(LogStatus.INFO, Description);
						}
						else if(Function_Name.equalsIgnoreCase("closeBrowser"))
						{
							CommonFunction.closeBrowser(driver);
							test.log(LogStatus.INFO, Description);
						}
						else if (Function_Name.equalsIgnoreCase("captureData"))
						{
							CommonFunction.captureData(driver, Locator_Type, Locator_Value);
							test.log(LogStatus.INFO, Description);
						}
						else if (Function_Name.equalsIgnoreCase("stableValidation"))
						{
							CommonFunction.stablevalidation(driver, Test_Data);
							test.log(LogStatus.INFO, Description);
						}						
						else if (Function_Name.equalsIgnoreCase("stockCategories"))
						{
							CommonFunction.stockCategories(driver);
							test.log(LogStatus.INFO, Description);
						}
						else if(Function_Name.equalsIgnoreCase("stockTable"))
						{
							CommonFunction.stockTable(driver, Test_Data);
							test.log(LogStatus.INFO, Description);
						}
						//write as pass into status column TCModule
						xl.setCellData(TCModule, j, 5, "Pass", outputpath);
						test.log(LogStatus.PASS, Description);
						//write as pass into MaterTestCases sheet
						xl.setCellData("MasterTestCases", i, 3, "Pass", outputpath);
					}catch(Exception e)
					{
						//write as Fail into status column TCModule
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						test.log(LogStatus.FAIL, Description); 
						//write as Fail into MaterTestCases sheet
						xl.setCellData("MasterTestCases", i, 3, "Fail", outputpath);
					}
					report.endTest(test);
					report.flush();
				}
			}
			else
			{
				//write as blocked into status cell in mastertestcases sheet
				xl.setCellData("MasterTestCases", i, 3, "Blocked", outputpath);
			}
		}
		{
		}}}	
		
	

