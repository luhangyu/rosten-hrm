package com.rosten.app.export;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

import com.rosten.app.staff.ContactInfor;
import com.rosten.app.staff.PersonInfor;
import com.rosten.app.staff.StaffService;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelExport {
	
	/**模板下载*/
	public String ygxxdc(OutputStream os,List<PersonInfor> personList){
		WritableWorkbook wwb = null;
		WritableSheet ws = null;
		try {
			
			VerticalAlignment vcenter = VerticalAlignment.CENTRE;
			Alignment acenter = Alignment.CENTRE;
			
			WritableCellFormat titlewcfStyle = new WritableCellFormat();
			WritableFont titlefont = new WritableFont(WritableFont.ARIAL, 14);
			titlefont.setBoldStyle(WritableFont.BOLD);
			titlewcfStyle.setFont(titlefont);
			titlewcfStyle.setBorder(Border.ALL, BorderLineStyle.THIN);
			titlewcfStyle.setAlignment(acenter);
			titlewcfStyle.setVerticalAlignment(vcenter);
			
			WritableCellFormat title1wcfStyle = new WritableCellFormat();
			WritableFont title1font = new WritableFont(WritableFont.ARIAL, 10);
			title1font.setBoldStyle(WritableFont.BOLD);
			title1wcfStyle.setFont(title1font);
			title1wcfStyle.setBorder(Border.ALL, BorderLineStyle.THIN);
			title1wcfStyle.setAlignment(acenter);
			title1wcfStyle.setVerticalAlignment(vcenter);
			
			WritableCellFormat bodywcfStyle = new WritableCellFormat();
			WritableFont bodyfont = new WritableFont(WritableFont.ARIAL, 10);
			bodywcfStyle.setFont(bodyfont);
			bodywcfStyle.setBorder(Border.ALL, BorderLineStyle.THIN);
			bodywcfStyle.setAlignment(acenter);
			bodywcfStyle.setVerticalAlignment(vcenter);
			
			wwb = Workbook.createWorkbook(os);
			ws = wwb.createSheet("数据导出", 0);
			
			ws.mergeCells(0,  0, 25,0);
			ws.addCell(new Label(0 , 0, "人员信息列表",titlewcfStyle));
			
			ws.addCell(new Label(0, 1, "姓名",title1wcfStyle));
			ws.addCell(new Label(1, 1, "部门",title1wcfStyle));
			ws.addCell(new Label(2, 1, "编制类别",title1wcfStyle));
			ws.addCell(new Label(3, 1, "性别",title1wcfStyle));
			ws.addCell(new Label(4, 1, "出生年月",title1wcfStyle));
			ws.addCell(new Label(5, 1, "身份证号",title1wcfStyle));
			ws.addCell(new Label(6, 1, "手机号码",title1wcfStyle));
			ws.addCell(new Label(7, 1, "民族",title1wcfStyle));
			ws.addCell(new Label(8, 1, "政治面貌",title1wcfStyle));
			
			ws.addCell(new Label(9, 1, "国籍",title1wcfStyle));
			ws.addCell(new Label(10, 1, "籍贯",title1wcfStyle));
			ws.addCell(new Label(11, 1, "血型",title1wcfStyle));
			ws.addCell(new Label(12, 1, "健康状况",title1wcfStyle));
			ws.addCell(new Label(13, 1, "户口所在地",title1wcfStyle));
			ws.addCell(new Label(14, 1, "档案转入时间",title1wcfStyle));
			ws.addCell(new Label(15, 1, "专业技术等级",title1wcfStyle));
			ws.addCell(new Label(16, 1, "入职时间",title1wcfStyle));
			ws.addCell(new Label(17, 1, "毕业院校",title1wcfStyle));
			ws.addCell(new Label(18, 1, "所学专业",title1wcfStyle));
			ws.addCell(new Label(19, 1, "学历",title1wcfStyle));
			ws.addCell(new Label(20, 1, "工作岗位",title1wcfStyle));
			ws.addCell(new Label(21, 1, "参加工作时间",title1wcfStyle));
			ws.addCell(new Label(22, 1, "QQ",title1wcfStyle));
			ws.addCell(new Label(23, 1, "邮编",title1wcfStyle));
			ws.addCell(new Label(24, 1, "电子邮箱",title1wcfStyle));
			
			
			ws.addCell(new Label(25, 1, "状态",title1wcfStyle));
			StaffService staffser = new StaffService();
			if(null!=personList&&personList.size()>0){
				for(int i=0;i<personList.size();i++){
					
					ws.addCell(new Label(0, i+2, personList.get(i).getChinaName(),bodywcfStyle));
					ws.addCell(new Label(1, i+2, (String)personList.get(i).getUserDepartName(),bodywcfStyle));
					ws.addCell(new Label(2, i+2, (String)personList.get(i).getUserTypeName(),bodywcfStyle));
					ws.addCell(new Label(3, i+2, personList.get(i).getSex(),bodywcfStyle));
					ws.addCell(new Label(4, i+2, (String)personList.get(i).getFormatteBirthday(),bodywcfStyle));
					ws.addCell(new Label(5, i+2, personList.get(i).getIdCard(),bodywcfStyle));
					
					if(null!=personList.get(i)){
						ContactInfor con = staffser.getContactInfor(personList.get(i));
						if(null==con){
							ws.addCell(new Label(6, i+2, "",bodywcfStyle));	
							ws.addCell(new Label(22, i+2, "",bodywcfStyle));
							ws.addCell(new Label(23, i+2, "",bodywcfStyle));
							ws.addCell(new Label(24, i+2, "",bodywcfStyle));
							
						}else{
						ws.addCell(new Label(6, i+2, con.getMobile(),bodywcfStyle));
						ws.addCell(new Label(22, i+2, con.getQq(),bodywcfStyle));
						ws.addCell(new Label(23, i+2, con.getPostcode(),bodywcfStyle));
						ws.addCell(new Label(24, i+2, con.getEmail(),bodywcfStyle));
					}
					
					
					ws.addCell(new Label(7, i+2, personList.get(i).getNationality(),bodywcfStyle));
					ws.addCell(new Label(8, i+2, personList.get(i).getPoliticsStatus(),bodywcfStyle));
					ws.addCell(new Label(9, i+2, personList.get(i).getCity(),bodywcfStyle));
					ws.addCell(new Label(10, i+2, personList.get(i).getNativeAddress(),bodywcfStyle));
					ws.addCell(new Label(11, i+2, personList.get(i).getBlood(),bodywcfStyle));
					ws.addCell(new Label(12, i+2, personList.get(i).getHealth(),bodywcfStyle));
					ws.addCell(new Label(13, i+2, personList.get(i).getHouseholdRegi(),bodywcfStyle));
					ws.addCell(new Label(14, i+2, (String)personList.get(i).getFormatteIntoday(),bodywcfStyle));
					ws.addCell(new Label(15, i+2, personList.get(i).getTechGrade(),bodywcfStyle));
					ws.addCell(new Label(16, i+2, (String)personList.get(i).getFormatteStaffOnday(),bodywcfStyle));
					
					ws.addCell(new Label(17, i+2, personList.get(i).getSchoolName(),bodywcfStyle));
					ws.addCell(new Label(18, i+2, personList.get(i).getMajor(),bodywcfStyle));
					ws.addCell(new Label(19, i+2, personList.get(i).getUpDegree(),bodywcfStyle));
					ws.addCell(new Label(20, i+2, personList.get(i).getWorkJob(),bodywcfStyle));
					ws.addCell(new Label(21, i+2, (String)personList.get(i).getFormatteWorkJobDate(),bodywcfStyle));
					
					ws.addCell(new Label(25, i+2, personList.get(i).getStatus(),bodywcfStyle));
				}
			}
			}
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
