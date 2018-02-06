package com.gn;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.gn.data.ConlluToConllMapper;
import com.gn.data.UDlanguages;
import com.gn.performance.GNTperformance;
import com.gn.performance.MDPperformance;
import com.gn.performance.UDlanguagePerformance;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.dfki.lt.mdparser.eval.Eval;
import de.dfki.mlt.gnt.caller.TrainTagger;
import de.dfki.mlt.gnt.corpus.ConllEvaluator;
import de.dfki.mlt.gnt.data.Pair;
import de.dfki.mlt.gnt.tagger.GNTagger;

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
	
	private void trainLanguage(String languageName, String languageID)
	    throws IOException, ConfigurationException{
		String corpusFilename = ConlluToConllMapper.getCorpusConfigFileName(languageName, languageID);
		String dataFilename = ConlluToConllMapper.getModelConfigFileName(languageName, languageID);
		String modelZipFileName = ConlluToConllMapper.getGNTmodelZipFileName(languageName, languageID);
		
		TrainTagger gntTrainer = new TrainTagger();
		
		System.out.println(corpusFilename);
		
		//GNTdataProperties.configTmpFileName = "resources/dataConfig.xml";
		
		gntTrainer.trainer(dataFilename, corpusFilename, modelZipFileName);
	}
	
	private void trainAllLanguages() throws IOException, ConfigurationException{
		long time1;
		long time2;
		time1 = System.currentTimeMillis();
		for (Pair<String, String> language : UDlanguages.languages){
			System.out.println("Training of: " + language);
			this.trainLanguage(language.getLeft(), language.getRight());	
		}
		time2 = System.currentTimeMillis();
		System.out.println("Complete training for " + UDlanguages.languages.size() + " languages:");
		System.out.println("System time (msec): " + (time2-time1));
	}
	
	private GNTperformance testLanguage(String languageName, String languageID, boolean debugTest) 
	    throws IOException, ConfigurationException{

		String corpusFilename = ConlluToConllMapper.getCorpusConfigFileName(languageName, languageID);
		String modelZipFileName = ConlluToConllMapper.getGNTmodelZipFileName(languageName, languageID);
		String testFile = ConlluToConllMapper.getConllTestFile(languageName, languageID);
		
		//GNTdataProperties.configTmpFileName = "resources/dataConfig.xml";
		GNTagger tagger = new GNTagger(modelZipFileName);
    ConllEvaluator evaluator = tagger.eval(corpusFilename);
    
    return new GNTperformance(evaluator);
	}
	
	private void trainSingleLanguage(String language, String languageID) throws IOException, ConfigurationException {
		this.trainLanguage(language, languageID);
	}
	
	private void testSinglelanguage(String language, String languageID, UDlanguagePerformance udPerformance) throws IOException, ConfigurationException {
		System.out.println("Testing of: " + language);
		GNTperformance gntPerformance = testLanguage(language, languageID, false);
		System.out.println("\n");

		udPerformance.addNewLanguageGNTperformance(languageID, gntPerformance);	
	}
	
	private void testAllLanguages(boolean debugTest) throws IOException, ConfigurationException{
		UDlanguagePerformance udPerformance = new UDlanguagePerformance();
		long time1 = System.currentTimeMillis();;
		
		for (Pair<String, String> language : UDlanguages.languages){
			System.out.println("Testing of: " + language);
			GNTperformance gntPerformance = 
			    this.testLanguage(language.getLeft(), language.getRight(), debugTest);
			// NOTE: will only use values from last call of corpus.EvalConllFile.computeAccuracy(String, boolean)
			// if several are called for one language. Currently this is just the test file;
			udPerformance.addNewLanguageGNTperformance(language.getRight(), gntPerformance);
		}
		long time2 = System.currentTimeMillis();
		long timeNeeded = (time2 -time1);
		System.out.println("Time: " +  timeNeeded + "; Complete testing for " + UDlanguages.languages.size() + " languages:");
		System.out.println(udPerformance.toGNTString());
	}
	
	public static void main(String[] args) throws IOException, ConfigurationException{
		UDlanguageGNTmodelFactory udFactory = new UDlanguageGNTmodelFactory("2_0");
		udFactory.trainAllLanguages();
		udFactory.testAllLanguages(false);
//		udFactory.trainSingleLanguage("Italian-PoSTWITA","it_postwita");
	}

}
