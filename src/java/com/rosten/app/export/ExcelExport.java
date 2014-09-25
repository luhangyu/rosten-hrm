package com.rosten.app.export;

import java.io.File;
import java.io.OutputStream;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelExport {
	
	/**模板下载*/
	public String mbxz(OutputStream os){
		WritableWorkbook wwb = null;
		WritableSheet ws = null;
		try {
			wwb = Workbook.createWorkbook(os);
			ws = wwb.createSheet("数据导出", 0);
			
			ws.addCell(new Label(0, 0, "姓名"));
			ws.addCell(new Label(1, 0, "身份证号"));
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据导出失败!");
		} finally {
			try {
				wwb.write();
				wwb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**数据导入*/
	public String hkcjdrsjdr(String filePath) throws Exception{
		
		Sheet sourceSheet = Workbook.getWorkbook(new File(filePath)).getSheet(0);
		int sourceRowCount = sourceSheet.getRows();//获得源excel的行数
		String failInfo="";
		boolean b=false;
	
		if(b){
			return "更新成功！"+failInfo;
		}else{
			return "更新失败！"+failInfo;
		}
	}


}
