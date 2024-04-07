package com.convertor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.convertor.exception.FileDataNotFoundException;

public class FetchRowDetails {

	public static void getRowDetails(File file, Map<String, Object> sheetDetails) throws FileDataNotFoundException {
		
		
		try {
		
			Workbook wb = new XSSFWorkbook(new FileInputStream(file)); // Created the workbook object using poi api
			Sheet sheet = wb.getSheetAt(0); // Selected the sheet 2nd which index is 1
			sheetDetails.put("sheet", sheet);
			int rowCount = sheet.getLastRowNum(); // Count the no. of rows in excel active sheet
			sheetDetails.put("rowCount",rowCount);
			
			if(!isSheetEmpty(sheet.getRow(0))) { // check sheet object is null or not
				
				// Created this variable to store the index of specific column as we are dealing with excel
				
				int name=0;
				int amount=0;
				int buildingFund=0;
				int missionFund=0;
				int specialFund=0;
				int total=0;
				int address=0;
				int city=0;
				int postalCode=0;
				int state=0;
				
				Row headerRow = sheet.getRow(0); // This fetch the first row which should be header
				
				Iterator<Cell> cellItr = headerRow.cellIterator(); // Using headerRow will get the cell Iterator
				while(cellItr.hasNext()) { // This loop iterate the header Row and store the index in declare variable
					Cell cell = cellItr.next();
					switch (cell.getStringCellValue().toLowerCase()) {
						case "name":
							name = cell.getColumnIndex();
							break;
						case "amount":
							amount = cell.getColumnIndex();
							break;
						case "building fund":
							buildingFund = cell.getColumnIndex();
							break;
						case "mission fund":
							missionFund = cell.getColumnIndex();
							break;
						case "special fund":
							specialFund = cell.getColumnIndex();
							break;
						case "total":
							total = cell.getColumnIndex();
							break;
						case "address":
							address = cell.getColumnIndex();
							break;
						case "city":
							city = cell.getColumnIndex();
							break;
						case "postal code":
							postalCode = cell.getColumnIndex();
							break;
						case "state":
							state = cell.getColumnIndex();
							break;
					}
				}
				sheetDetails.put("name",name);
				sheetDetails.put("amount", amount);
				sheetDetails.put("buildingFund", buildingFund);
				sheetDetails.put("missionFund", missionFund);
				sheetDetails.put("specialFund", specialFund);
				sheetDetails.put("total", total);
				sheetDetails.put("address", address);
				sheetDetails.put("city", city);
				sheetDetails.put("postalCode", postalCode);
				sheetDetails.put("state", state);
	
			} else {
				throw new FileDataNotFoundException("Conv004", "Sheet Not Found");
			}
		} catch (Exception e) {
			throw new FileDataNotFoundException("Conv003", "Please Check the File Data");
		}
	}
	public static boolean isSheetEmpty(Row row) {
		if (row == null) {
            return true;
        }
        for (Cell cell : row) {
            if (cell.getCellTypeEnum() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
