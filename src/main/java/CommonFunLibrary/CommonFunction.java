package CommonFunLibrary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import Utilities.PropertyFileUtil;

public class CommonFunction {
	public static WebDriver driver;
	public static WebDriver startBrowser() throws Throwable
	{
	if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("chrome"))
	{
		System.setProperty("webdriver.chrome.driver", "F:\\SELENIUM\\OJTProjectTesting\\ERP_StockMaven\\CommonDrivers\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}
	else if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("firefox"))
	{
		System.setProperty("webdriver.gecko.driver", "F:\\SELENIUM\\OJTProjectTesting\\ERP_StockMaven\\CommonDrivers\\geckodriver.exe");
		driver = new FirefoxDriver();
	}
	else
	{
		System.out.println("Browser value is Not matching");
	}
	return driver;
	}
	public static void openApplication(WebDriver driver)throws Throwable
	{
		driver.get(PropertyFileUtil.getValueForKey("Url"));
	}
	public static void waitForElement(WebDriver driver,String locatortype,String locatorvalue,String waittime)
	{
	WebDriverWait myWait = new WebDriverWait(driver, Integer.parseInt(waittime));
	if(locatortype.equalsIgnoreCase("name"))
	{
		myWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorvalue)));
	}
	else if(locatortype.equalsIgnoreCase("id"))
	{
		myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorvalue)));
	}
	else if(locatortype.equalsIgnoreCase("xpath"))
	{
		myWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorvalue)));
	}
	}
	public static void typeAction(WebDriver driver,String locatortype,String locatorvalue,String testData)
	{
		if(locatortype.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(locatorvalue)).clear();
			driver.findElement(By.name(locatorvalue)).sendKeys(testData);
		}
		else if(locatortype.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(locatorvalue)).clear();
			driver.findElement(By.id(locatorvalue)).sendKeys(testData);
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(locatorvalue)).clear();
			driver.findElement(By.xpath(locatorvalue)).sendKeys(testData);
		}
	}
	public static void clickAction(WebDriver driver,String locatortype,String locatorvalue)
	{
		if(locatortype.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(locatorvalue)).sendKeys(Keys.ENTER);
		}
		else if(locatortype.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(locatorvalue)).click();
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(locatorvalue)).click();
		}
	}
	public static void closeBrowser(WebDriver driver)
	{
		driver.close();
	}
	//java time stamp
	public static String generateDate()
	{
		Date date= new Date();
		SimpleDateFormat df= new SimpleDateFormat("YYYY_MM_dd_ss");
		return df.format(date);
	}
	//method for capturing in notepad
		public static void captureData(WebDriver driver, String locatortype, String locatorvalue) throws Throwable
		{
			String expectednumber=null;
			if(locatortype.equalsIgnoreCase("id"))
			{
				expectednumber=driver.findElement(By.id(locatorvalue)).getAttribute("value");
			}
			else if(locatortype.equalsIgnoreCase("name"))
			{
				expectednumber=driver.findElement(By.name(locatorvalue)).getAttribute("value");
			}
			//write expected no. into notepad
			FileWriter fw =new FileWriter("F:\\SELENIUM\\OJTProjectTesting\\ERP_StockMaven\\CaptureData\\Supplier.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(expectednumber);
			bw.flush();
			bw.close();
		}
	//method for table validation
	public static void stablevalidation (WebDriver driver, String columndata) throws Throwable
	{
		//read data from note pad
		FileReader fr = new FileReader("F:\\SELENIUM\\OJTProjectTesting\\ERP_StockMaven\\CaptureData\\Supplier.txt");
		BufferedReader br = new BufferedReader(fr);
		String exp_Data=br.readLine();
		//convert column data into integer type
		int column=Integer.parseInt(columndata);
		if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search.textbox"))).isDisplayed())
			//click on search panel button
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search.panel"))).click();
		Thread.sleep(5000);
		//clear search text box
		WebElement searchtext = driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search.textbox")));
		searchtext.clear();
		Thread.sleep(5000);
		searchtext.sendKeys(exp_Data);
		Thread.sleep(5000);
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search.button"))).click();
		Thread.sleep(5000);
		WebElement table = driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("webtable.path")));
		//count no. of rows
		java.util.List<WebElement> rows=table.findElements(By.tagName("tr"));
		for(int i = 1;i<=rows.size();i++)
		{
			//capture six cell data in a table
			String act_Data=driver.findElement(By.xpath("//table[@id='tbl_a_supplierslist']/tbody/tr["+i+"]/td["+column+"]/div/span/span")).getText();
			Reporter.log(exp_Data+"   "+act_Data,true);
			Assert.assertEquals(act_Data, exp_Data,"column data is not matching");
			break;
			
			
		}
		
	}
	
	public static void stockCategories(WebDriver driver) throws Throwable
	{
		Actions ac = new Actions(driver);
		ac.moveToElement(driver.findElement(By.linkText("Stock Items"))).perform();
		Thread.sleep(4000);
		ac.moveToElement(driver.findElement(By.xpath("(//a[text()='Stock Categories'])[2]"))).click().perform();
		Thread.sleep(4000);
	}
	public static void stockTable(WebDriver driver, String testData) throws Throwable
	{
		if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search.textbox"))).isDisplayed())
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search.panel"))).click();
		Thread.sleep(4000);
		WebElement searchtext = driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search.textbox")));
		searchtext.clear();
		Thread.sleep(5000);
		searchtext.sendKeys(testData);
		Thread.sleep(5000);
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search.button"))).click();
		Thread.sleep(5000);
		WebElement table= driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("webtable.stock")));
		List<WebElement> rows=table.findElements(By.tagName("tr"));
		for(int i=1;i<=rows.size();i++)
		{
			String actdata=driver.findElement(By.xpath("//table[@id='tbl_a_stock_categorieslist']/tbody/tr["+i+"]/td[4]/div/span/span")).getText();
		Assert.assertEquals(actdata,testData,"Category Not Matching");
		Reporter.log(testData+"  "+actdata,true);
		break;
		}
	}

	//An example for github update
	public static void addition(int a, int b)
	{
		a=100;
		b=200;
		int c= a+b;
		System.out.println("Addition value= "+c);
	}
}





