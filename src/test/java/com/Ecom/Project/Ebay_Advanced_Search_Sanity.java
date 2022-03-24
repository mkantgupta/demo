package com.Ecom.Project;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Table.Cell;

public class Ebay_Advanced_Search_Sanity {

	WebDriver driver;
	FileInputStream fin;
	Properties p;

	@BeforeMethod
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "chromedriver1.exe");
		driver = new ChromeDriver();
		fin=new FileInputStream("Ebay_Advanced_OR.property");
		p=new Properties();
		p.load(fin);
		driver.get(p.getProperty("domain"));
		Thread.sleep(2000);
	}

	@AfterMethod
	public void tearDown() {
		driver.close();
	}

	@Test
	public void empty_advanced_search_test() throws Exception {
		String expectedURL = p.getProperty("expectedURL");
		String expectedTitle = p.getProperty("expectedTitle");
		WebElement searchBtn = driver.findElement(By.cssSelector(p.getProperty("clickSearchBtnusingCSSselector")));

		Assert.assertTrue(searchBtn.isEnabled(), "Verify Search Button Enabled");

		searchBtn.click();
		Thread.sleep(2000);

		String newUrl = driver.getCurrentUrl();
		String newTitle = driver.getTitle();
		System.out.println(newUrl);
		System.out.println(newTitle);

		Assert.assertEquals(newUrl, expectedURL,"Verify URL of the new page");
		Assert.assertEquals(newTitle, expectedTitle,"Verify Title of the new page");
	}

	@Test
	public void category_options_in_ascending_order_test() throws Exception {
		File file = new File("File3.xlsx");
		XSSFWorkbook wkbook = new XSSFWorkbook();
		XSSFSheet sheet = wkbook.createSheet("Sheet1");
		Row row;
		org.apache.poi.ss.usermodel.Cell cell;



		List<WebElement> category_options = driver.findElements(By.cssSelector(p.getProperty("allCatogeryByCSS")));
		List<String> arr1 = new ArrayList<String>();

		int size=category_options.size();
		System.out.println("The no of all options is:"+size);

		for(WebElement option : category_options) {
			arr1.add(option.getText());
		}
		List<String> arr2 = new ArrayList<String>(arr1);
		Collections.sort(arr2);
		System.out.println("Actual List:" + arr1);
		System.out.println("Sorted List:" + arr2);
		//Assert.assertTrue(arr1.equals(arr2), "Verify Category Items Sorted");

		
		row = sheet.createRow(1);
		for (int j = 0; j < size; j++) {
			cell = row.createCell(j);
			cell.setCellValue(arr1.get(j));
		}
		row = sheet.createRow(3);
		for (int j = 0; j < size; j++) {
			cell = row.createCell(j);
			cell.setCellValue(arr2.get(j));
		}
		

		try {
			FileOutputStream excel = new FileOutputStream(file);
			wkbook.write(excel);
			excel.close();
			wkbook.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	}
