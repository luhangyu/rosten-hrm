package com.rosten.app.export;

import java.io.File;

import jxl.Sheet;
import jxl.Workbook;

public class ExcelImport {
	
	/**--数据导入*/
	public String personsjdr(String filePath,String realName) throws Exception{
		Sheet sourceSheet = Workbook.getWorkbook(new File(filePath+realName)).getSheet(0);
		int sourceRowCount = sourceSheet.getRows();//获得源excel的行数
		String sfzh = "";

		for(int i=1;i<sourceRowCount;i++){
			sfzh=sourceSheet.getCell(1, i).getContents();
			System.out.println(sfzh);
		}
	return "";

}
}