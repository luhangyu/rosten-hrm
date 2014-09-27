package com.rosten.app.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.springframework.util.ResourceUtils;


import freemarker.template.Configuration;
import freemarker.template.Template;


public class FreeMarkerUtil {

	
	private static final String ENCODING = "UTF-8";
	private static final String WORD_TYPE = ".doc";
	private static final String TEMP_DIR = "java.io.tmpdir";
	private static Configuration configuration = new Configuration();
	
	
	static {
		configuration.setDefaultEncoding(ENCODING);
	}
	

	public static synchronized File getWordFile(Map<String,Object> data,String templateDirectory,String templateName,String fileName){
		Writer out = null;
		try {
			Template template = null;
			//设置模版位置
			configuration.setDirectoryForTemplateLoading(ResourceUtils.getFile(templateDirectory));
			//模版文件名
			template=configuration.getTemplate(templateName);
			File wordFile = new File(System.getProperty(TEMP_DIR)+"/"+fileName+ WORD_TYPE);
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(wordFile), ENCODING));
			template.process(data, out);
			return wordFile;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @描述:生成word文件
	 * @作者：Penghui.Qu
	 * @日期：2013-5-15 上午10:58:44
	 * @修改记录: 修改者名字-修改日期-修改内容
	 * @param data
	 * @param template
	 * @return
	 * File 返回类型 
	 * @throws
	 */
	public static File getWordFile(Map<String,Object> data,String templateDirectory,String templateName){
		
		String fileName = String.valueOf(System.currentTimeMillis());
		
		return getWordFile(data, templateDirectory, templateName, fileName);
	}
	
	/**
	 * 
	 * @描述:生成word文件
	 * @作者：guoqb
	 * @日期：2014-8-29 10:38:47
	 * @修改记录: 修改者名字-修改日期-修改内容
	 * @param data
	 * @param template
	 * @return
	 * File 返回类型 
	 * @throws
	 */
	public static File getWordFile(String lqh, Map<String, Object> data, String templateDirectory, String templateName) {
		return getWordFile(data, templateDirectory, templateName, lqh);
	}
	
}
