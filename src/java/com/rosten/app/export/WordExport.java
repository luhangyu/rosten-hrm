package com.rosten.app.export;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

//import sun.misc.BASE64Encoder;









import com.rosten.app.util.Base64Util;
import com.rosten.app.export.ZipUtil;
import com.rosten.app.staff.ContactInfor;
import com.rosten.app.staff.Degree;
import com.rosten.app.staff.DepartChange;
import com.rosten.app.staff.PersonInfor;
import com.rosten.app.staff.StaffService;
import com.rosten.app.staff.StatusChange;
import com.rosten.app.staff.WorkResume;
import com.rosten.app.system.Attachment;

public class WordExport {
	
	// 填充模版数据生成word文件（登记表）
	private File getDjbWord(String id) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		StaffService staffser = new StaffService();
		String zpstr="";
		PersonInfor personInfor = staffser.getPersonInfor(id);
		data.put("personInfor", personInfor);
		Attachment attachment = staffser.getAttachment(id);
		if(null!=attachment){
			 zpstr = getZpStr("web-app/images/staff/" + attachment.getRealName());
		}else{
			zpstr = getZpStr("web-app/images/staff/regpic.gif");
		}
		
		if(null!=personInfor){
			ContactInfor con = staffser.getContactInfor(personInfor);
			if (null==con){
				con = new ContactInfor();
				con.setHomeAddress("");
			}
			//简历
			WorkResume work = staffser.getWorkResumeByPersonInfor(personInfor);
			if (null==work){
				work = new WorkResume();
				work.setDuty("");
			}
			
			//学习经历
			Degree deg = staffser.getDegreeByPersonInfor(personInfor);
			if (null==deg){
				deg = new Degree();
				deg.setDegree("");
			}
			
			//简历
			String worstr =  staffser.getWorkResume(personInfor);
			
			//家庭成员
			String famstr =  staffser.geFamilyInfor(personInfor);
			
			//人力资源
			List<Map<String, Object>> rlzylist = staffser.getCommentByStatus(personInfor.getId(),"部门审核");
			String rlzyyj="";
			if(null!=rlzylist){
				for(int i=0;i<rlzylist.size();i++){
					rlzyyj +=rlzylist.get(i).get("name")+"  "+ rlzylist.get(i).get("content")+"  "+rlzylist.get(i).get("date")+"&#x000D;";
				}
			}
			
			//秘书长
			List<Map<String, Object>> mszlist = staffser.getCommentByStatus(personInfor.getId(),"秘书长审核");
			
			String mszyyj="";
			if(null!=mszlist){
				for(int i=0;i<mszlist.size();i++){
					mszyyj += mszlist.get(i).get("name")+"  "
				+(null== mszlist.get(i).get("content")||"".equals(mszlist.get(i).get("content"))?"": mszlist.get(i).get("content")
				+"  "
				+mszlist.get(i).get("date")+"&#x000D;");
				}
			}
			
			//协会审批
			List<Map<String, Object>> lszlist = staffser.getCommentByStatus(personInfor.getId(),"理事长审批");
			String lszyyj="";
			if(null!=lszlist){
				for(int i=0;i<lszlist.size();i++){
					lszyyj += lszlist.get(i).get("name")+"  "
				+(null== lszlist.get(i).get("content")||"".equals(lszlist.get(i).get("content"))?"": lszlist.get(i).get("content")
				+"  "
				+lszlist.get(i).get("date")+"&#x000D;");
				}
				
			}
			
			data.put("contactInfor", con);	
			data.put("workResume", work);	
			data.put("zpstr", zpstr);	
			data.put("degree", deg);	
			data.put("worstr", worstr);
			data.put("famstr", famstr);
			
			data.put("rlzyyj", rlzyyj);
			
			data.put("mszyyj", mszyyj);
			
			data.put("lszyyj", lszyyj);
			
			Calendar now = Calendar.getInstance();  
			String year = String.valueOf(now.get(Calendar.YEAR));  
			String month = String.valueOf(now.get(Calendar.MONTH) + 1);  
			String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));  
			
			data.put("year", year);
			data.put("month", month);
			data.put("day", day);

		}
		File wordFile = FreeMarkerUtil.getWordFile(data,
				"classpath:com/rosten/app/template", "ygdjb.xml",personInfor.getChinaName()+"登记表");
		return wordFile;
	}
	
	/**
	 * 獲取照片
	 */
	@SuppressWarnings("static-access")
	public String getZpStr(String filePath)  {
		byte[] buffer  = null;  
		try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(100000);  
            byte[] b = new byte[100000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
//			BASE64Encoder encoder = new BASE64Encoder();
//			return buffer != null ? encoder.encode(buffer) : null;
		
			Base64Util encoder = new Base64Util();
			byte[] b = encoder.encode(buffer);
			String s = null;
			try {
				s = new String(b, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return buffer != null ?s : null;
	}
	
	
	/**
	 * 单个打印登记表
	 */
	public String dyDjb(HttpServletResponse response,String ids) throws Exception {
		
		File wordFile = getDjbWord(ids);
		FileUtil.outputWord(response,wordFile);
		return null;
	}
	
	/**
	 * 批量打印登记表
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String downloadDjbZip(HttpServletResponse response,String ids)
			throws Exception {
		String[] pks = ids.split(",");
		if (null!=pks) {
			List<File> files = new ArrayList<File>();
			for (int i = 0, n = pks.length; i < n; i++) {
				File file = getDjbWord(pks[i]);
				files.add(file);
			}
			File zipFile = ZipUtil.zip("登记表",files.toArray(new File[] {}));
			FileUtil.outputZip(response, zipFile);
		}
		return null;
	}
	
	
	// 填充模版数据生成word文件(入职清单)
	private File getRzqdWord(String id) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		StaffService staffser = new StaffService();
		PersonInfor personInfor = staffser.getPersonInfor(id);
		data.put("personInfor", personInfor);	
		File wordFile = FreeMarkerUtil.getWordFile(data,
				"classpath:com/rosten/app/template", "rzsxqd.xml",personInfor.getChinaName()+"入职手续清单");
		return wordFile;
	}
	
	/**
	 * 单个打印登记表
	 */
	public String dyRzqd(HttpServletResponse response,String ids) throws Exception {
		
		File wordFile = getRzqdWord(ids);
		FileUtil.outputWord(response,wordFile);
		return null;
	}
	
	/**
	 * 批量打印登记表
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String downloadRzqdZip(HttpServletResponse response,String ids)
			throws Exception {
		String[] pks = ids.split(",");
		if (null!=pks) {
			List<File> files = new ArrayList<File>();
			for (int i = 0, n = pks.length; i < n; i++) {
				File file = getRzqdWord(pks[i]);
				files.add(file);
			}
			File zipFile = ZipUtil.zip("入职手续清单",files.toArray(new File[] {}));
			FileUtil.outputZip(response, zipFile);
		}
		return null;
	}
	
	// 填充模版数据生成word文件(离职交接单)
		private File getLzjjdWord(String id) throws Exception {
			Map<String, Object> data = new HashMap<String, Object>();
			StaffService staffser = new StaffService();
			StatusChange statusChange = staffser.getStatusChange(id);
			PersonInfor personInfor = staffser.getPersonByStatuCh(id);
			data.put("statusChange", statusChange);	
			data.put("personInfor", personInfor);	
			
			File wordFile = FreeMarkerUtil.getWordFile(data,
					"classpath:com/rosten/app/template", "lzjjd.xml",personInfor.getChinaName()+"离职交接单");
			return wordFile;
		}
		
		/**
		 * 单个打印离职交接单
		 */
		public String dyLzjjd(HttpServletResponse response,String ids) throws Exception {
			
			File wordFile = getLzjjdWord(ids);
			FileUtil.outputWord(response,wordFile);
			return null;
		}
		
		/**
		 * 批量打印离职交接单
		 * @param response
		 * @return
		 * @throws Exception
		 */
		public String downloadLzjjdZip(HttpServletResponse response,String ids)
				throws Exception {
			String[] pks = ids.split(",");
			if (null!=pks) {
				List<File> files = new ArrayList<File>();
				for (int i = 0, n = pks.length; i < n; i++) {
					File file = getLzjjdWord(pks[i]);
					files.add(file);
				}
				File zipFile = ZipUtil.zip("离职交接单",files.toArray(new File[] {}));
				FileUtil.outputZip(response, zipFile);
			}
			return null;
		}
		
		// 填充模版数据生成word文件(调动交接单)
		private File getDdjjdWord(String id) throws Exception {
			Map<String, Object> data = new HashMap<String, Object>();
			StaffService staffser = new StaffService();
			DepartChange depaChange = staffser.getDepartChange(id);
			PersonInfor personInfor = staffser.getPersonByDepaCh(id);
			data.put("personInfor", personInfor);
			data.put("depaChange", depaChange);	
			File wordFile = FreeMarkerUtil.getWordFile(data,
					"classpath:com/rosten/app/template", "ddjjd.xml",personInfor.getChinaName()+"调动交接单");
			return wordFile;
		}
		
		/**
		 * 单个打印调动交接单
		 */
		public String dyDdjjd(HttpServletResponse response,String ids) throws Exception {
			
			File wordFile = getDdjjdWord(ids);
			FileUtil.outputWord(response,wordFile);
			return null;
		}
		
		/**
		 * 批量打印调动交接单
		 * @param response
		 * @return
		 * @throws Exception
		 */
		public String downloadDdjjdZip(HttpServletResponse response,String ids)
				throws Exception {
			String[] pks = ids.split(",");
			if (null!=pks) {
				List<File> files = new ArrayList<File>();
				for (int i = 0, n = pks.length; i < n; i++) {
					File file = getDdjjdWord(pks[i]);
					files.add(file);
				}
				File zipFile = ZipUtil.zip("调动交接单",files.toArray(new File[] {}));
				FileUtil.outputZip(response, zipFile);
			}
			return null;
		}

}
