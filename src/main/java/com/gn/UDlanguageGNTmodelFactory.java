package com.gn;

import java.io.IOException;

import com.gn.data.ConlluToConllMapper;
import com.gn.data.UDlanguages;
import com.gn.performance.GNTperformance;
import com.gn.performance.UDlanguagePerformance;

import caller.RunTagger;
import caller.TrainTagger;
import data.GNTdataProperties;
import data.Pair;

/**
 * Call GNT trainer and runner for each language define in UDlanguages.java
 * 
 * - get corpusPropsFile
 * - get dataPropsFile 
 * - call trainer
 * - create modelfile.ZIP
 * @author gune00
 *
 */
public class UDlanguageGNTmodelFactory {
	
	public UDlanguageGNTmodelFactory(String version){
		UDlanguages.version = version;
		UDlanguages.addLanguages();
	}
	
	private void trainLanguage(String languageName, String languageID) throws IOException{
		String corpusFilename = ConlluToConllMapper.getCorpusPropsFile(languageName, languageID);
		String dataFilename = ConlluToConllMapper.getDataPropsFile(languageName, languageID);
		String modelZipFileName = ConlluToConllMapper.getGNTmodelZipFileName(languageName, languageID);
		String archiveTxtName = modelZipFileName.split("\\.zip")[0]+".txt";
		
		TrainTagger gntTrainer = new TrainTagger();
		
		System.out.println(corpusFilename);
		
		GNTdataProperties.configTmpFileName = "resources/dataConfig.xml";
		
		gntTrainer.trainer(dataFilename, corpusFilename, modelZipFileName, archiveTxtName);
	}
	
	private void trainAllLanguages() throws IOException{
		long time1;
		long time2;
		time1 = System.currentTimeMillis();
		for (Pair<String, String> language : UDlanguages.languages){
			System.out.println("Training of: " + language);
			this.trainLanguage(language.getL(), language.getR());	
		}
		time2 = System.currentTimeMillis();
		System.out.println("Complete training for " + UDlanguages.languages.size() + " languages:");
		System.out.println("System time (msec): " + (time2-time1));
	}
	
	private void testLanguage(String languageName, String languageID, boolean debugTest) throws IOException{

		String corpusFilename = ConlluToConllMapper.getCorpusPropsFile(languageName, languageID);
		String modelZipFileName = ConlluToConllMapper.getGNTmodelZipFileName(languageName, languageID);
		String archiveTxtName = modelZipFileName.split("\\.zip")[0]+".txt";
		
		GNTdataProperties.configTmpFileName = "resources/dataConfig.xml";
		
		RunTagger.runner(modelZipFileName, corpusFilename, archiveTxtName, debugTest);
	}
	
	private void testAllLanguages(boolean debugTest) throws IOException{
		UDlanguagePerformance udPerformance = new UDlanguagePerformance();
		long time1;
		long time2;
		time1 = System.currentTimeMillis();
		for (Pair<String, String> language : UDlanguages.languages){
			System.out.println("Testing of: " + language);
			this.testLanguage(language.getL(), language.getR(), debugTest);
			// NOTE: will only use values from last call of corpus.EvalConllFile.computeAccuracy(String, boolean)
			// if several are called for one language. Currently this is just the test file;
			GNTperformance gntPerformance = new GNTperformance();
			udPerformance.addNewLanguageGNTperformance(language.getR(), gntPerformance);
		}
		time2 = System.currentTimeMillis();
		System.out.println("Complete testing for " + UDlanguages.languages.size() + " languages:");
		System.out.println(udPerformance.toGNTString());
	}
	
	public static void main(String[] args) throws IOException{
		UDlanguageGNTmodelFactory udFactory = new UDlanguageGNTmodelFactory("1_3");
		//udFactory.trainAllLanguages();
		udFactory.testAllLanguages(false);
	}

}
