package com.cg.smt.java;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import com.cg.smt.java.bean.TagBean;

/**
* This java component would do the following.
* a) Take the JSP name and path from input.
* b) Make a copy (custom name and path also allowed) at the same location.
* c) Read and parse Struts1.x tags from File. Keep it in beans. 
* d) Replace struts1.x tags with Struts2.x tags using Jsoup library. 
* e) replace with proper tag names
* f) Output the log - successful/Failed.
*
* @author Dillip Mohanty
*/

public class JspMigrator {

	public static void main(String[] args) {
		String fileLoc = null;
		if(args.length == 1) {
			//for(int i = 0; i < args.length; i++) {
	            fileLoc = args[0];
	        //}
		} else {
			System.out.println("Please check the program arguments.");
		}
		String fileName = fileLoc.substring(fileLoc.lastIndexOf("/")+1);
		String filePath = fileLoc.substring(0, fileLoc.lastIndexOf("/"));
		String fileDesc = fileName.substring(0, fileName.lastIndexOf("."));
		String fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
//		System.out.println(fileName);
//		System.out.println(filePath);
//		System.out.println(fileDesc);
//		System.out.println(fileExt);
		StringBuffer strBuff = new StringBuffer(filePath);
		strBuff.append("/").append(fileDesc).append("2").append(".").append(fileExt);	
		
		File existfile = new File(fileLoc);
		File newFile = new File(strBuff.toString());
		
		try {
			Files.copy(existfile.toPath(), newFile.toPath());
		} catch (IOException e1) {
			System.out.println("IO error occured" + e1.getMessage());
			//e1.printStackTrace();
		}
		
		try {
			//Document html = Jsoup.parse(new File(filePath+'/'+fileName), "UTF-8");
			Document html = Jsoup.parse(existfile, "UTF-8");
			//System.out.println(html.html());
			
			List<String> tagList = new ArrayList<>();
			tagList.add("html:form");
			tagList.add("html:select");
			tagList.add("html:option");
			tagList.add("html:submit");
//			TagBean tagBean = null;
			for (String tag : tagList) {
				System.out.println("Tag name is: "+ tag);
				//Read Strtus1.x tags in the list
				Elements inputElements = html.getElementsByTag(tag);  
			    for (Element inputElement : inputElements) { 
//			    	tagBean = new TagBean();
//			    	tagBean.setTagName(inputElement.tagName());
//			    	tagBean.setFormName(inputElement.attr("name")); 
//			    	tagBean.setProperty(inputElement.attr("property"));
//			    	tagBean.setValue(inputElement.attr("value"));  
			        //System.out.println("Param name: "+key+" \nParam property: "+property+" \nParam value: "+value);
			        
			    	if(tag.equals("html:form")) {
			    		inputElement.tagName("s:form");
			    	} else if(tag.equals("html:select")) {
			    		inputElement.tagName("s:select");
			    	} else if(tag.equals("html:option")) {
			    		inputElement.tagName("s:option");
			    	} else if(tag.equals("html:submit")) {
			    		inputElement.tagName("s:submit");
			    	}
			    } 
			}
			//Read HTML tags
			System.out.println(html.html());
			
			//Write the file
			PrintWriter writer = new PrintWriter(newFile,"UTF-8");
			
			writer.write(html.html() ) ;
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
