package com.rosten.app.export;

import java.io.File;

import jxl.Sheet;
import jxl.Workbook;

import com.rosten.app.staff.PersonInfor;
import com.rosten.app.staff.StaffService;
import com.rosten.app.system.User;
import com.rosten.app.system.UserType;

public class ExcelImport {
	
	/**--数据导入*/
	public String personsjdr(String filePath,String realName,User userEntity,UserType userType) throws Exception{
		Sheet sourceSheet = Workbook.getWorkbook(new File(filePath+realName)).getSheet(0);
		int sourceRowCount = sourceSheet.getRows();//获得源excel的行数
		
		StaffService _service = new StaffService();

		for(int i=1;i<sourceRowCount;i++){
			 String ssbm =sourceSheet.getCell(0, i).getContents();	//所属部门
			 String xm =sourceSheet.getCell(1, i).getContents();	//姓名
			 String xb =sourceSheet.getCell(2, i).getContents();	//性别
			 String csny =sourceSheet.getCell(3, i).getContents();	//出生年月
			 String xl =sourceSheet.getCell(4, i).getContents();	//学历
			 String zzmm =sourceSheet.getCell(5, i).getContents();	//政治面貌
			 String jg =sourceSheet.getCell(6, i).getContents();	//籍贯
			 
			//根据用户名插入对应数据
			PersonInfor personInfor = new PersonInfor();
			personInfor.setChinaName(xm);
			personInfor.setUser(userEntity);
			personInfor.setUserTypeEntity(userType);
			personInfor.setSex(xb);
			//personInfor.setBirthday(csny);
			personInfor.setNativeAddress(jg);
			personInfor.setIdCard("000001");
			
			_service.commonSave(personInfor,ssbm,userEntity);
			
		}
		return "true";
	
	}
}