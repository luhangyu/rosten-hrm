package com.rosten.app.export;

import java.io.File;

import jxl.Sheet;
import jxl.Workbook;

import com.rosten.app.staff.ContactInfor;
import com.rosten.app.staff.PersonInfor;
import com.rosten.app.staff.StaffService;
import com.rosten.app.system.User;
import com.rosten.app.util.Util;


public class ExcelImport {
	
	/**--数据导入*/
	public String personsjdr(String filePath,String realName,User userEntity) throws Exception{
		Sheet sourceSheet = Workbook.getWorkbook(new File(filePath+realName)).getSheet(0);
		int sourceRowCount = sourceSheet.getRows();//获得源excel的行数
		
		StaffService _service = new StaffService();

		for(int i=1;i<sourceRowCount;i++){
			 String ssbm =sourceSheet.getCell(1, i).getContents();	//所属部门
			 String xm =sourceSheet.getCell(2, i).getContents();	//姓名
			 String xb =sourceSheet.getCell(3, i).getContents();	//性别
			 String hyzk =sourceSheet.getCell(4, i).getContents();	//婚姻状况
			 String jg =sourceSheet.getCell(5, i).getContents();	//籍贯
			 String sfzh =sourceSheet.getCell(6, i).getContents();	//身份证号
			 String byxx =sourceSheet.getCell(7, i).getContents();	//毕业学校
			 
			 String sxzy =sourceSheet.getCell(8, i).getContents();	//所学专业
			 String xl =sourceSheet.getCell(9, i).getContents();	//学历
			 String zc =sourceSheet.getCell(10, i).getContents();	//职称
			 String zzmm =sourceSheet.getCell(11, i).getContents();	//政治面貌
			 String gzgw =sourceSheet.getCell(12, i).getContents();	//工作岗位
			 String ygxs =sourceSheet.getCell(13, i).getContents();	//用工形式
			 String cjgzsj =sourceSheet.getCell(14, i).getContents();	//参加工作时间
			 String dxhgzsj =sourceSheet.getCell(15, i).getContents();	//到协会工作时间
			 String sjhm =sourceSheet.getCell(16, i).getContents();	//手机号码
			 
			 
			 ContactInfor con = new ContactInfor();
			 con.setMobile(null==sjhm?"":sjhm);
			 con.setQq("无");
			 con.setAddress("无");
			 con.setPostcode("无");
			 
			//根据用户名插入对应数据
			PersonInfor personInfor = new PersonInfor();
			personInfor.setChinaName(xm);
			personInfor.setSex(xb);
			personInfor.setMarriage(hyzk);
			personInfor.setNativeAddress(jg);
			personInfor.setIdCard(sfzh);
			personInfor.setSchoolName(byxx);
			personInfor.setMajor(sxzy);
			
			personInfor.setUpDegree(xl);
			personInfor.setTechGrade(zc);
			personInfor.setPoliticsStatus(zzmm);
			personInfor.setWorkJob(gzgw);
			
			if(!"".equals(cjgzsj)){
				personInfor.setWorkJobDate(Util.convertToTimestamp(cjgzsj));
			}
			if(!"".equals(dxhgzsj)){
				personInfor.setStaffOnDay(Util.convertToTimestamp(dxhgzsj));
				personInfor.setIntoday(Util.convertToTimestamp(dxhgzsj));
			}
			
			_service.commonSave(personInfor,con,userEntity,ssbm,ygxs);
			
		}
		return "true";
	
	}
}