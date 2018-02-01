package com.gn.data;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import de.dfki.mlt.gnt.data.Pair;

public class UDlanguages {

	public static String conlluPath = null;
	public static String conllPath = null;
	public static String version = null;

	public static boolean ignore = true;
	
	public static List<String> ignoreList = new ArrayList<String>();

	public static List<Pair<String,String>> languages = new ArrayList<Pair<String,String>>();

	private static void getAllLanguagePairs(String path) {
		File dir = new File(path);
	      File[] subdirs = dir.listFiles();
	      FileFilter subDirFilter = new FileFilter() {
	         public boolean accept(File file) {
	            return file.isDirectory();
	         }
	      };
	      subdirs = dir.listFiles(subDirFilter);
	      
	      if (subdirs.length == 0) {
	          System.out.println("Either dir does not exist or is not a directory");
	       } else {
	          for (int i = 0; i< subdirs.length; i++) {
	        	  File filename = subdirs[i];
	             String languageFullName = filename.getName().split("UD_")[1];
	             String trainFileSuffix = "-ud-train.conllu";
	             
	             FileFilter fileFilter = new FileFilter() {
	    	         public boolean accept(File file) {
	    	        	return file.getName().contains(trainFileSuffix);
	    	         }
	    	      };
	    	     File[] subdirFiles = filename.listFiles(fileFilter);
	             
	             if (subdirFiles.length > 0) {
	             String languageID = subdirFiles[0].getName().split(trainFileSuffix)[0];
	             if (ignore) {
	            	 if (!ignoreList.contains(languageID)) {
	            		 languages.add(new Pair<String,String>(languageFullName, languageID));
	            	 }
	            	 else
	            		 System.out.println("Ignoring: " + languageID);
	             }
	             else
	             languages.add(new Pair<String,String>(languageFullName, languageID));
	             }
	          }
	       }
	      
	   }

	private static List<Pair<String,String>> addLanguages_1_2(){
		setVersion_1_2();
		UDlanguages.getAllLanguagePairs(conlluPath);
		return languages;
	}

	private static List<Pair<String, String>> addLanguages_1_3() {
		setVersion_1_3();
		UDlanguages.getAllLanguagePairs(conlluPath);
		return languages;
	}

	private static List<Pair<String, String>> addLanguages_2_1() {
		setVersion_2_1();
		UDlanguages.getAllLanguagePairs(conlluPath);
		return languages;
	}

	public static List<Pair<String,String>> addLanguages(){
		switch (UDlanguages.version){
		case "1_2": return addLanguages_1_2();
		case "1_3": return addLanguages_1_3();
		case "2_1": return addLanguages_2_1();
		default: return null;
		}
	}

	public static void setVersion_1_2(){
		conlluPath = "/Users/gune00/data/UniversalDependencies/ud-treebanks-v1.2/";
		conllPath = "/Users/gune00/data/UniversalDependencies/conll/";
		version = "1_2";
	}

	public static void setVersion_1_3(){
		ignoreList.add("la_proiel");
		ignoreList.add("en_esl");
		conlluPath = "/Users/gune00/data/UniversalDependencies/ud-treebanks-v1.3/";
		conllPath = "/Users/gune00/data/UniversalDependencies/conll3/";
		version = "1_3";	
	}
	
	public static void setVersion_2_1(){
		ignoreList.add("la_proiel");
		ignoreList.add("sme"); // has no devel
		ignoreList.add("ro_nonstandard"); // has no devel
		conlluPath = "/local/data/UniversalDependencies/ud-treebanks-v2.1/ud-treebanks-v2.1/";
		conllPath = "/local/data/UniversalDependencies/conll21/";
		version = "2_1";	
	}
	
	public static void setUdVersion(String udVersion){
		switch (udVersion){
		case "1_2": UDlanguages.setVersion_1_2();
		case "1_3": UDlanguages.setVersion_1_3();
		case "2_1": UDlanguages.setVersion_2_1();
		}
	}

	public static void main(String[] args) {
		version = "2_1";
		addLanguages();
		for (Pair<String,String> language : languages) {
			System.out.println(language.toString());
		}
	}
}
