package com.rosten.app.export;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rosten.app.export.ZipUtil;

public class WordExport {
	
	// 填充模版数据生成word文件
	private File getWord(String id) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
			
		File wordFile = FreeMarkerUtil.getWordFile(data,
				"classpath:com/rosten/app/template", "rxbdd.xml","wenj");
		return wordFile;
	}
	
	
	public String downloadZip(HttpServletResponse response)
			throws Exception {
		String[] pks = new String[]{"111"};
		if (null!=pks) {
			List<File> files = new ArrayList<File>();
			for (int i = 0, n = pks.length; i < n; i++) {
				File file = getWord(pks[i]);
				files.add(file);
			}
			File zipFile = ZipUtil.zip("员工信息",files.toArray(new File[] {}));
			FileUtil.outputZip(response, zipFile);
		}
		return null;
	}

}
