package com.rosten.app.export;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;



public class ZipUtil {

	private static final String ZIP = ".zip";
	private static Log logger = LogFactory.getLog(ZipUtil.class);
	
	
	public static File zip(String srcPath){
		
		File srcDir = new File(srcPath);
		if (!srcDir.exists()){
			logger.error("srcPath not exists !");
			return null;
		}
		
		Project project = new Project();
		
		Zip zip = new Zip();
		zip.setProject(project);
		File destFile = new File(System.currentTimeMillis()+ ZIP);
		
		zip.setDestFile(destFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(project);
		fileSet.setDir(srcDir);
		
		zip.addFileset(fileSet);
		zip.execute();
		
		return destFile;
	}
	
	public static File zip(File... files){
		
		if (files.length == 0){
			logger.error("files can't null ");
			return null;
		}
		
		Project project = new Project();
		Zip zip = new Zip();
		zip.setProject(project);
		File destFile = new File(System.currentTimeMillis()+ ZIP);
		
		zip.setDestFile(destFile);
		
		for (File file : files){
			FileSet fileSet = new FileSet();
			fileSet.setProject(project);
			fileSet.setFile(file);
			//fileSet.set
			zip.addFileset(fileSet);
		}
		zip.execute();
		return destFile;
	}
	
	public static File zip(String filename,File... files){
		
		if (files.length == 0){
			logger.error("files can't null ");
			return null;
		}
		
		Project project = new Project();
		Zip zip = new Zip();
		zip.setProject(project);
		File destFile = new File(filename+ZIP);
		
		zip.setDestFile(destFile);
		
		for (File file : files){
			FileSet fileSet = new FileSet();
			fileSet.setProject(project);
			fileSet.setFile(file);
			//fileSet.set
			zip.addFileset(fileSet);
		}
		zip.execute();
		return destFile;
	}
	
}
