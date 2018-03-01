package com.gn.data;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.dfki.mlt.gnt.data.Pair;

public class UDlanguages {

	public static String conlluPath = null;
	public static String conlluTestPath = null;
	public static String conllPath = null;
	public static String version = null;

	public static boolean ignore = true;
	public static boolean sortLanguageID = true; // if true use language ID else language full name
	
	public static List<String> ignoreList = new ArrayList<String>();

	public static List<Pair<String,String>> languages = new ArrayList<Pair<String,String>>();

	/**
	 * This method processes all UD grammar subdirectories
	 * and collect all language. languageID pairs.
	 * It will do only for those grammars which have train, devel, and test CONLLU files.
	 * All others are ignored.
	 * We will do so, as a starter for mapping them to our conll-x format and for creating GNT and MDP cofnig files.
	 * @param path
	 */
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
			for (int i = 0; i < subdirs.length; i++) {
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
							languages.add(new Pair<String, String>(languageFullName, languageID));
						} else
							System.out.println("Ignoring: " + "<" + languageFullName + " , " + languageID + ">");
					} else
						languages.add(new Pair<String, String>(languageFullName, languageID));
				}
			}
		}

	}

	public static List<Pair<String, String>> addLanguages() {
		switch (UDlanguages.version) {
		case "1_2":
			setVersion_1_2(); break;
		case "1_3":
			setVersion_1_3(); break;
		case "2_0":
			setVersion_2_0(); break;
		case "2_1":
			setVersion_2_1(); break;
		default: System.err.println("UD version not known: " + UDlanguages.version); System.exit(0);
		}
		UDlanguages.getAllLanguagePairs(conlluPath);
		sortLanguages();
		return languages;
	}
	
	private static void sortLanguages() {
		Collections.sort(languages, new Comparator<Pair<String,String>>() {
            @Override
            public int compare(Pair<String,String> item, Pair<String,String> t1) {
                String s1 = (sortLanguageID)?item.getRight():item.getLeft();
                String s2 = (sortLanguageID)?t1.getRight():t1.getLeft();;
                return s1.compareToIgnoreCase(s2);
            }

        });
	}

	// Only used currently in Nemex
	public static void setUdVersion(String udVersion){
		switch (udVersion){
		case "1_2": UDlanguages.setVersion_1_2();
		case "1_3": UDlanguages.setVersion_1_3();
		case "2_0": UDlanguages.setVersion_2_0();
		case "2_1": UDlanguages.setVersion_2_1();
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
	
	// GN: for shared 2017 data set
	public static void setVersion_2_0(){
		// The 8 small data set
		ignoreList.add("fr_partut"); // devel does not exist
		ignoreList.add("ga"); // devel does not exist
		ignoreList.add("gl_treegal"); // devel does not exist
		ignoreList.add("la"); // devel does not exist
		ignoreList.add("sl_sst"); // devel does not exist
		ignoreList.add("ug"); // devel does not exist
		ignoreList.add("uk"); // devel does not exist
		ignoreList.add("kk"); // devel does not exist
		// and this ?
		ignoreList.add("it_partut"); // test does not exist
		
	
		
		// remaining 55 languages are the "big" onces (training data larger than devel at -> check)
		conlluPath = "/local/data/UniversalDependencies/conllSharedtask2017/Universal Dependencies 2.0/ud-treebanks-conll2017/";
		conlluTestPath = "/local/data/UniversalDependencies/ud-test-v2.0-conll2017/gold/conll17-ud-test-2017-05-09/";
		conllPath = "/local/data/UniversalDependencies/conll20/";
		version = "2_0";	
	}
	
	public static void setVersion_2_1(){
		ignoreList.add("ar_nyuad"); // NO wordforms !! -> have to obtain lexical data base from somewhere else
		ignoreList.add("fr_ftb"); // NO wordforms !! -> have to obtain lexical data base from somewhere else
		ignoreList.add("it_postwita"); // GNT error: feature nodes must be sorted by index in ascending order
		conlluPath = "/local/data/UniversalDependencies/ud-treebanks-v2.1/ud-treebanks-v2.1/";
		conllPath = "/local/data/UniversalDependencies/conll21/";
		version = "2_1";	
	}
	
	public static void main(String[] args) {
		version = "2_0";
		addLanguages();
		for (Pair<String,String> language : languages) {
			System.out.println(language.toString());
		}
		System.out.println("Languages: " + languages.size());
	}
}
