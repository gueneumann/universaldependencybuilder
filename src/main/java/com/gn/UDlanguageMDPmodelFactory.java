package com.gn;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.gn.data.ConlluToConllMapper;
import com.gn.data.UDlanguages;
import com.gn.performance.MDPperformance;
import com.gn.performance.UDlanguagePerformance;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.dfki.lt.mdparser.caller.MDPrunner;
import de.dfki.lt.mdparser.eval.Eval;
import de.dfki.lt.mdparser.parser.Trainer;
import de.dfki.mlt.gnt.data.Pair;

public class UDlanguageMDPmodelFactory {

 public UDlanguageMDPmodelFactory(String version){
		UDlanguages.version = version;
		UDlanguages.addLanguages();
	}

	private void trainLanguage(String languageName, String languageID) 
			throws IOException, NoSuchAlgorithmException, InvalidInputDataException{

		String trainFile = ConlluToConllMapper.getConllTrainFile(languageName, languageID);
		String modelZipFileName = ConlluToConllMapper.getMDPmodelZipFileName(languageName, languageID);

		System.out.println("MDP training: " + trainFile + " into ModelFile: " + modelZipFileName);

		Trainer.train(trainFile, modelZipFileName);
	}

	// TODO: this is basically the same as in UDlanguageGNTmodelFactory
	private void trainAllLanguages() 
			throws IOException, NoSuchAlgorithmException, InvalidInputDataException{
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

	private Eval testLanguage(String languageName, String languageID) throws IOException{

		String testFile = ConlluToConllMapper.getConllTestFile(languageName, languageID);
		String modelZipFileName = ConlluToConllMapper.getMDPmodelZipFileName(languageName, languageID);
		String mdpResultFile = ConlluToConllMapper.getConllMDPresultFile(testFile);

		return MDPrunner.parseAndEvalConllFile(testFile, mdpResultFile, modelZipFileName);
	}

	private void testAllLanguages() throws IOException{
		UDlanguagePerformance udPerformance = new UDlanguagePerformance();
		long time1;
		long time2;
		time1 = System.currentTimeMillis();
		for (Pair<String, String> language : UDlanguages.languages){
			System.out.println("Testing of: " + language);
			Eval eval = this.testLanguage(language.getLeft(), language.getRight());
			System.out.println("\n");

			MDPperformance mdpPerformance = new MDPperformance(eval);
			udPerformance.addNewLanguageMDPperformance(language.getRight(), mdpPerformance);
		}
		time2 = System.currentTimeMillis();
		System.out.println("Complete testing for " + UDlanguages.languages.size() + " languages:");
		System.out.println("System time (msec): " + (time2-time1));
		System.out.println(udPerformance.toMDPString());
	}
	
	private void trainSingleLanguage(String language, String languageID) throws NoSuchAlgorithmException, IOException, InvalidInputDataException {
		this.trainLanguage(language, languageID);
	}
	
	private void testSinglelanguage(String language, String languageID, UDlanguagePerformance udPerformance) throws IOException {
		System.out.println("Testing of: " + language);
		Eval eval = testLanguage(language, languageID);
		System.out.println("\n");

		MDPperformance mdpPerformance = new MDPperformance(eval);
		udPerformance.addNewLanguageMDPperformance(languageID, mdpPerformance);	
	}
	
	// This is for training and testing for the languages handled in EU project IREAD
	private void trainIREADlanguages() throws NoSuchAlgorithmException, IOException, InvalidInputDataException {
		//trainSingleLanguage("English", "en");
		trainSingleLanguage("German", "de");
		trainSingleLanguage("Greek", "el");
		trainSingleLanguage("Spanish", "es");
	}
	
	private void testIREADlanguages() throws IOException{
		UDlanguagePerformance udPerformance = new UDlanguagePerformance();
		long time1;
		long time2;
		
		time1 = System.currentTimeMillis();
		this.testSinglelanguage("English", "en", udPerformance);
		this.testSinglelanguage("German", "de", udPerformance);
		this.testSinglelanguage("Greek", "el", udPerformance);
		this.testSinglelanguage("Spanish", "es", udPerformance);
		
		time2 = System.currentTimeMillis();
		System.out.println("Complete testing for " + UDlanguages.languages.size() + " languages:");
		System.out.println("System time (msec): " + (time2-time1));
		System.out.println(udPerformance.toMDPString());
	}
	

	public static void main(String[] args) 
			throws IOException, NoSuchAlgorithmException, InvalidInputDataException{
		UDlanguageMDPmodelFactory udFactory = new UDlanguageMDPmodelFactory("2_1");
		UDlanguages.ignore = true;
//		udFactory.trainAllLanguages();
//		udFactory.testAllLanguages();
		udFactory.testIREADlanguages();
	}
}
