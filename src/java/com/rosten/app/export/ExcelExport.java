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
			
			wwb = Workbook.createWorkbook(os);
			ws = wwb.createSheet("数据导出", 0);
			
			ws.mergeCells(0,  0, 9,0);
			ws.addCell(new Label(0 , 0, "人员信息列表",titlewcfStyle));
			
			ws.addCell(new Label(0, 1, "姓名"));
			ws.addCell(new Label(1, 1, "部门"));
			ws.addCell(new Label(2, 1, "编制类别"));
			ws.addCell(new Label(3, 1, "性别"));
			ws.addCell(new Label(4, 1, "出生年月"));
			ws.addCell(new Label(5, 1, "身份证号"));
			ws.addCell(new Label(6, 1, "手机号码"));
			ws.addCell(new Label(7, 1, "民族"));
			ws.addCell(new Label(8, 1, "政治面貌"));
			ws.addCell(new Label(9, 1, "状态"));
			StaffService staffser = new StaffService();
			if(null!=personList&&personList.size()>0){
				for(int i=0;i<personList.size();i++){
					
					ws.addCell(new Label(0, i+2, personList.get(i).getChinaName()));
					ws.addCell(new Label(1, i+2, (String)personList.get(i).getUserDepartName()));
					ws.addCell(new Label(2, i+2, (String)personList.get(i).getUserTypeName()));
					ws.addCell(new Label(3, i+2, personList.get(i).getSex()));
					ws.addCell(new Label(4, i+2, (String)personList.get(i).getFormatteBirthday()));
					ws.addCell(new Label(5, i+2, personList.get(i).getIdCard()));
					
					if(null!=personList.get(i)){
						ContactInfor con = staffser.getContactInfor(personList.get(i));
						ws.addCell(new Label(6, i+2, con.getMobile()));
					}else{
						ws.addCell(new Label(6, i+2, ""));
					}
					
					
					ws.addCell(new Label(7, i+2, personList.get(i).getNationality()));
					ws.addCell(new Label(8, i+2, personList.get(i).getPoliticsStatus()));
					ws.addCell(new Label(9, i+2, personList.get(i).getStatus()));
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
