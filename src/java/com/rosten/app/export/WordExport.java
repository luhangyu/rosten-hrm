package com.rosten.app.export;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rosten.app.export.ZipUtil;
import com.rosten.app.staff.ContactInfor;
import com.rosten.app.staff.PersonInfor;
import com.rosten.app.staff.StaffService;

public class WordExport {
	
	// 填充模版数据生成word文件（登记表）
	private File getDjbWord(String id) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		StaffService staffser = new StaffService();
		PersonInfor personInfor = staffser.getPersonInfor(id);
		data.put("personInfor", personInfor);	
		if(null!=personInfor){
			ContactInfor con = staffser.getContactInfor(personInfor);
			data.put("contactInfor", con);	
		}
		File wordFile = FreeMarkerUtil.getWordFile(data,
				"classpath:com/rosten/app/template", "ygdjb.xml",personInfor.getChinaName()+"登记表");
		return wordFile;
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
			PersonInfor personInfor = staffser.getPersonInfor(id);
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
		
		// 填充模版数据生成word文件(离职交接单)
		private File getDdjjdWord(String id) throws Exception {
			Map<String, Object> data = new HashMap<String, Object>();
			StaffService staffser = new StaffService();
			PersonInfor personInfor = staffser.getPersonInfor(id);
			data.put("personInfor", personInfor);	
			File wordFile = FreeMarkerUtil.getWordFile(data,
					"classpath:com/rosten/app/template", "ddjjd.xml",personInfor.getChinaName()+"调动交接单");
			return wordFile;
		}
		
		/**
		 * 单个打印离职交接单
		 */
		public String dyDdjjd(HttpServletResponse response,String ids) throws Exception {
			
			File wordFile = getDdjjdWord(ids);
			FileUtil.outputWord(response,wordFile);
			return null;
		}
		
		/**
		 * 批量打印离职交接单
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
