package com.gn.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import de.dfki.mlt.gnt.config.ConfigKeys;
import de.dfki.mlt.gnt.config.CorpusConfig;
import de.dfki.mlt.gnt.config.ModelConfig;
import de.dfki.mlt.gnt.data.Pair;

public class ConlluToConllMapper {

	private static CorpusConfig corpusConfigPos = new CorpusConfig(new PropertiesConfiguration());
	private static ModelConfig modelConfigPos = new ModelConfig(new PropertiesConfiguration());
	
	private static CorpusConfig corpusConfigMorph = new CorpusConfig(new PropertiesConfiguration());
	private static ModelConfig modelConfigMorph = new ModelConfig(new PropertiesConfiguration());
	
	/*
	 * Define file names for corpusProps and dataProps
	 */
	public  static String getCorpusConfigFileName(String languageName, String languageID, String tagger){
		return UDlanguages.conllPath + languageName + "/" + languageID + "." + tagger + ".corpus.conf";
	}
	
	public  static String getModelConfigFileName(String languageName, String languageID, String tagger){
		return UDlanguages.
				conllPath + languageName + "/" + languageID + "." + tagger + ".model.conf";
	}
	
	public  static String getGNTmodelZipFileName(String languageName, String languageID, String tagger){
		return UDlanguages.
				conllPath + languageName + "/" + languageID + "-" + tagger + "model.zip";
	}
	
	public static String getMDPmodelZipFileName(String languageName, String languageID) {
		return UDlanguages.
				conllPath + languageName + "/" + languageID + "-MDPmodel.zip";
	}
	
	public static String getConllMDPresultFile(String testFile) {
		return testFile.split("\\.conll")[0]+"-result.conll";
	}
	
	public static String getConllGNTresultFile(String testFile, String tagger) {
		return testFile.split("\\.conll")[0]+"-"+tagger+"-result.conll";
	}
	
	public static String getConllTrainFile(String languageName, String languageID) {
		return UDlanguages.
				conllPath + languageName + "/" + languageID + "-ud-train.conll";
	}
	public static String getConllTestFile(String languageName, String languageID) {
		return UDlanguages.
				conllPath + languageName + "/" + languageID + "-ud-test.conll";
	}

	public static String getConllDevFile(String languageName, String languageID) {
		return UDlanguages.
				conllPath + languageName + "/" + languageID + "-ud-dev.conll";
	}

	// GNT POS tagger settings
	private static void initLanguageCorpusConfigPos(String languageID){
		ConlluToConllMapper.corpusConfigPos.setProperty(
		    ConfigKeys.TAGGER_NAME, languageID.toUpperCase()+"UNIPOS");
		ConlluToConllMapper.corpusConfigPos.setProperty(ConfigKeys.WORD_FORM_INDEX, 1);
		ConlluToConllMapper.corpusConfigPos.setProperty(ConfigKeys.TAG_INDEX, 3);
	}
	
	//NOTE - SAME for ALL languages !
	private static void initLanguageModelConfigPos(String languageID){
		ConlluToConllMapper.modelConfigPos.setProperty(
		    ConfigKeys.TAGGER_NAME, languageID.toUpperCase()+"UNIPOS");
		// <!-- Liblinear settings -->
		ConlluToConllMapper.modelConfigPos.setProperty(ConfigKeys.SOLVER_TYPE, "MCSVM_CS");
		ConlluToConllMapper.modelConfigPos.setProperty(ConfigKeys.C, 0.1);
		ConlluToConllMapper.modelConfigPos.setProperty(ConfigKeys.EPS, 0.3);
		// <!-- Control parameters -->
		ConlluToConllMapper.modelConfigPos.setProperty(ConfigKeys.WINDOW_SIZE, 2);
		ConlluToConllMapper.modelConfigPos.setProperty(ConfigKeys.NUMBER_OF_SENTENCES, -1);
		ConlluToConllMapper.modelConfigPos.setProperty(ConfigKeys.DIM, 0);
		ConlluToConllMapper.modelConfigPos.setProperty(ConfigKeys.SUB_SAMPLING_THRESHOLD, 0.000000001);
		// <!-- features (not) activated -->
		ConlluToConllMapper.modelConfigPos.setProperty(ConfigKeys.WITH_WORD_FEATS, false);
		ConlluToConllMapper.modelConfigPos.setProperty(ConfigKeys.WITH_SHAPE_FEATS, true);
		ConlluToConllMapper.modelConfigPos.setProperty(ConfigKeys.WITH_SUFFIX_FEATS, true);
		ConlluToConllMapper.modelConfigPos.setProperty(ConfigKeys.WITH_CLUSTER_FEATS, false);
		ConlluToConllMapper.modelConfigPos.setProperty(ConfigKeys.WITH_LABEL_FEATS, false);
	}

	private static void writeCorpusConfigPos(String languageName, String languageID) throws IOException{
		String corpusConfigFileName = 
		    ConlluToConllMapper.getCorpusConfigFileName(languageName, languageID, "POS");
		try (Writer out = Files.newBufferedWriter(Paths.get(corpusConfigFileName))) {
		  ConlluToConllMapper.corpusConfigPos.write(out);
		} catch (ConfigurationException e) {
      e.printStackTrace();
    }
	}

	private static void writeModelConfigPos(String languageName, String languageID) throws IOException{
		String modelConfigFileName = ConlluToConllMapper.getModelConfigFileName(languageName, languageID, "POS");
		try (Writer out = Files.newBufferedWriter(Paths.get(modelConfigFileName))) {
		  ConlluToConllMapper.modelConfigPos.write(out);
		} catch (ConfigurationException e) {
      e.printStackTrace();
    }
	}
	
	// GNT Morph tagger settings
	private static void initLanguageCorpusConfigMorph(String languageID){
		ConlluToConllMapper.corpusConfigMorph.setProperty(
		    ConfigKeys.TAGGER_NAME, languageID.toUpperCase()+"UNIMORPH");
		ConlluToConllMapper.corpusConfigMorph.setProperty(ConfigKeys.WORD_FORM_INDEX, 1);
		ConlluToConllMapper.corpusConfigMorph.setProperty(ConfigKeys.TAG_INDEX, 5);
	}
	
	//NOTE - SAME for ALL languages !
	private static void initLanguageModelConfigMorph(String languageID){
		ConlluToConllMapper.modelConfigMorph.setProperty(
		    ConfigKeys.TAGGER_NAME, languageID.toUpperCase()+"UNIMORPH");
		// <!-- Liblinear settings -->
		ConlluToConllMapper.modelConfigMorph.setProperty(ConfigKeys.SOLVER_TYPE, "MCSVM_CS");
		ConlluToConllMapper.modelConfigMorph.setProperty(ConfigKeys.C, 0.1);
		ConlluToConllMapper.modelConfigMorph.setProperty(ConfigKeys.EPS, 0.3);
		// <!-- Control parameters -->
		ConlluToConllMapper.modelConfigMorph.setProperty(ConfigKeys.WINDOW_SIZE, 2);
		ConlluToConllMapper.modelConfigMorph.setProperty(ConfigKeys.NUMBER_OF_SENTENCES, -1);
		ConlluToConllMapper.modelConfigMorph.setProperty(ConfigKeys.DIM, 0);
		ConlluToConllMapper.modelConfigMorph.setProperty(ConfigKeys.SUB_SAMPLING_THRESHOLD, 0.000000001);
		// <!-- features (not) activated -->
		ConlluToConllMapper.modelConfigMorph.setProperty(ConfigKeys.WITH_WORD_FEATS, false);
		ConlluToConllMapper.modelConfigMorph.setProperty(ConfigKeys.WITH_SHAPE_FEATS, true);
		ConlluToConllMapper.modelConfigMorph.setProperty(ConfigKeys.WITH_SUFFIX_FEATS, true);
		ConlluToConllMapper.modelConfigMorph.setProperty(ConfigKeys.WITH_CLUSTER_FEATS, false);
		ConlluToConllMapper.modelConfigMorph.setProperty(ConfigKeys.WITH_LABEL_FEATS, true);
	}

	private static void writeCorpusConfigMorph(String languageName, String languageID) throws IOException{
		String corpusConfigFileName = 
		    ConlluToConllMapper.getCorpusConfigFileName(languageName, languageID, "MORPH");
		try (Writer out = Files.newBufferedWriter(Paths.get(corpusConfigFileName))) {
		  ConlluToConllMapper.corpusConfigMorph.write(out);
		} catch (ConfigurationException e) {
      e.printStackTrace();
    }
	}

	private static void writeModelConfigMorph(String languageName, String languageID) throws IOException{
		String modelConfigFileName = ConlluToConllMapper.getModelConfigFileName(languageName, languageID, "MORPH");
		try (Writer out = Files.newBufferedWriter(Paths.get(modelConfigFileName))) {
		  ConlluToConllMapper.modelConfigMorph.write(out);
		} catch (ConfigurationException e) {
      e.printStackTrace();
    }
	}

	// Just create it initially with taggername
	// and directly write it out

	// Create the CONLL files

	/**
	 * Example
	 * 
	 * Input: "/Users/gune00/data/UniversalDependencies/" "Arabic" "ar" "dev"
	 * 
	 * Output: "/Users/gune00/data/UniversalDependencies/UD_Arabic-master/ar-ud-dev.conllu"
	 * 
	 * @param conlluPath 
	 * @param languageName
	 * @param languageID
	 * @param mode
	 * @return
	 */
	private static String makeConlluFileName(String languageName, String languageID, String mode) {
		String fileName = "";
		switch (UDlanguages.version) {
		case "1_2":
			fileName = UDlanguages.conlluPath + "UD_" + languageName + "-master/" + languageID + "-ud-" + mode
					+ ".conllu";
			break;
		case "2_0":
			if (mode.equals("test")) {
				fileName = UDlanguages.conlluTestPath + languageID + ".conllu";
				
			} 
			else {
				fileName = UDlanguages.conlluPath + "UD_" + languageName + "/" + languageID + "-ud-" + mode
						+ ".conllu";
			}
			;
			break;
		default:
			fileName = UDlanguages.conlluPath + "UD_" + languageName + "/" + languageID + "-ud-" + mode + ".conllu";
			break;

		}
		return fileName;
	}

	/**
	 * Creates 
	 * "/Users/gune00/data/UniversalDependencies/conll/Arabic/ar-ud-dev.conll";
	 * @param languageName
	 * @param languageID
	 * @param mode
	 * @return
	 */
	private  static String makeConllFileName (String languageName, String languageID, String mode){
		String conllDirName = UDlanguages.conllPath + languageName +"/";
		File conllLangDir = new File(conllDirName);
		if (!conllLangDir.exists()) conllLangDir.mkdir();
		String fileName = conllDirName + languageID + "-ud-" + mode + ".conll";
		return fileName;
	}

	private static String makeSentenceFileName(String conllFileName){
		return conllFileName.split("\\.conll")[0]+"-sents.txt";
	}
	
	private static boolean containsmultipleSpanToken(String conllTokenLine) {
		String[] tokenizedLine = conllTokenLine.split("\t");
		boolean multipleToken = false;
		if (tokenizedLine[0].contains("-") ||
				tokenizedLine[0].contains(".")
				) {
			multipleToken = true;
		}
		return multipleToken;
	}

	/**
	 * bascially maps a conllu to conll format - very simple process so far.
	 * @param sourceFileName
	 * @param targetFileName
	 * @throws IOException
	 */
	private static void transformConlluToConllFile(String sourceFileName, String targetFileName)
			throws IOException {

		String sourceEncoding = "utf-8";
		String targetEncoding = "utf-8";
		// init reader for CONLL style file
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(sourceFileName),
						sourceEncoding));

		// init writer for line-wise file
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(targetFileName),
						targetEncoding));

		String line = "";
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty()) 
				writer.newLine();
			else
			{
				// Normalize line which is assumed to correspond to a sentence.
				if (!line.startsWith("#")){
					if (!containsmultipleSpanToken(line)){
					writer.write(line);
					writer.newLine();
					}
				}
			}
		}
		reader.close();
		writer.close();
	}

	private static void transcodeConllToSentenceFile(String sourceFileName, String targetFileName)
			throws IOException {
		String sourceEncoding = "utf-8";
		String targetEncoding = "utf-8";
		// init reader for CONLL style file

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(sourceFileName),
						sourceEncoding));

		// init writer for line-wise file
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(targetFileName),
						targetEncoding));

		String line = "";
		List<String> tokens = new ArrayList<String>();
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty()) {
				// If we read a newline it means we know we have just extracted the words
				// of a sentence, so write them to file
				if (!tokens.isEmpty()){
					writer.write(sentenceToString(tokens)+"\n");
					tokens = new ArrayList<String>();
				}
			}
			else
			{
				// Extract the word from each CONLL token line
				String[] tokenizedLine = line.split("\t");
				tokens.add(tokenizedLine[1]);
			}
		}
		reader.close();
		writer.close();
	}

	private static String sentenceToString(List<String> tokens){
		String sentenceString = "";
		for (int i=0; i < tokens.size()-1; i++){
			sentenceString = sentenceString + tokens.get(i)+" ";
		}
		return sentenceString+tokens.get(tokens.size()-1);
	}



	/**
	 * Transform files for train/test/dev and call them in own main caller
	 * @param languageName
	 * @param languageID
	 * @throws IOException
	 */
	private static void transformerTrain(String languageName, String languageID) throws IOException{
		String conlluFile = ConlluToConllMapper.makeConlluFileName(languageName, languageID, "train");
		String conllFile = ConlluToConllMapper.makeConllFileName(languageName, languageID, "train");
		String sentFile = ConlluToConllMapper.makeSentenceFileName(conllFile);
		ConlluToConllMapper.corpusConfigPos.setProperty(ConfigKeys.TRAINING_LABELED_DATA, conllFile);	
		ConlluToConllMapper.corpusConfigPos.setProperty(ConfigKeys.TRAINING_UNLABELED_DATA, sentFile);
		ConlluToConllMapper.corpusConfigMorph.setProperty(ConfigKeys.TRAINING_LABELED_DATA, conllFile);	
		ConlluToConllMapper.corpusConfigMorph.setProperty(ConfigKeys.TRAINING_UNLABELED_DATA, sentFile);

		ConlluToConllMapper.transformConlluToConllFile(conlluFile, conllFile);
		ConlluToConllMapper.transcodeConllToSentenceFile(conllFile, sentFile);
	}

	private static void transformerDev(String languageName, String languageID) throws IOException{
		String conlluFile = ConlluToConllMapper.makeConlluFileName(languageName, languageID, "dev");
		String conllFile = ConlluToConllMapper.makeConllFileName(languageName, languageID, "dev");
		String sentFile = ConlluToConllMapper.makeSentenceFileName(conllFile);
		ConlluToConllMapper.corpusConfigPos.setProperty(ConfigKeys.DEV_LABELED_DATA, conllFile);
		ConlluToConllMapper.corpusConfigPos.setProperty(ConfigKeys.DEV_UNLABELED_DATA, sentFile);
		ConlluToConllMapper.corpusConfigMorph.setProperty(ConfigKeys.DEV_LABELED_DATA, conllFile);
		ConlluToConllMapper.corpusConfigMorph.setProperty(ConfigKeys.DEV_UNLABELED_DATA, sentFile);

		ConlluToConllMapper.transformConlluToConllFile(conlluFile, conllFile);
		ConlluToConllMapper.transcodeConllToSentenceFile(conllFile, sentFile);
	}

	private static void transformerTest(String languageName, String languageID) throws IOException{
		String conlluFile = ConlluToConllMapper.makeConlluFileName(languageName, languageID, "test");
		String conllFile = ConlluToConllMapper.makeConllFileName(languageName, languageID, "test");
		String sentFile = ConlluToConllMapper.makeSentenceFileName(conllFile);
		ConlluToConllMapper.corpusConfigPos.setProperty(ConfigKeys.TEST_LABELED_DATA, conllFile);
		ConlluToConllMapper.corpusConfigPos.setProperty(ConfigKeys.TEST_UNLABELED_DATA, sentFile);
		ConlluToConllMapper.corpusConfigMorph.setProperty(ConfigKeys.TEST_LABELED_DATA, conllFile);
		ConlluToConllMapper.corpusConfigMorph.setProperty(ConfigKeys.TEST_UNLABELED_DATA, sentFile);

		ConlluToConllMapper.transformConlluToConllFile(conlluFile, conllFile);
		ConlluToConllMapper.transcodeConllToSentenceFile(conllFile, sentFile);
	}

	public static void transformer(String languageName, String languageID) throws IOException{
		ConlluToConllMapper.transformerTrain(languageName, languageID);
		ConlluToConllMapper.transformerDev(languageName, languageID);
		ConlluToConllMapper.transformerTest(languageName, languageID);
	}
	
	public static void runUDversion() throws IOException{
		for (Pair<String, String> language : UDlanguages.languages){
			System.out.println("Processing: " + language);
			initLanguageCorpusConfigPos(language.getRight());
			initLanguageModelConfigPos(language.getRight());
			initLanguageCorpusConfigMorph(language.getRight());
			initLanguageModelConfigMorph(language.getRight());

			transformer(language.getLeft(), language.getRight());

			writeCorpusConfigPos(language.getLeft(), language.getRight());
			writeModelConfigPos(language.getLeft(), language.getRight());
			writeCorpusConfigMorph(language.getLeft(), language.getRight());
			writeModelConfigMorph(language.getLeft(), language.getRight());
		}
	}

	/**
	 * Loop across languages
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		UDlanguages.setVersion_udify();
		UDlanguages.addLanguages();
		runUDversion();
	}
}
