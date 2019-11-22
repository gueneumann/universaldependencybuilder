package com.gn;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gn.data.ConlluToConllMapper;
import com.gn.data.UDlanguages;

public class UDprocessor {

	private static final Logger LOG = LoggerFactory.getLogger(UDprocessor.class);
	private	String udVersion = "";
	private String factory = "";

	// Parameter setting functions

	private void initNemexArguments(String[] args) throws IOException{

		for (int i=0; i < args.length;i++){
			switch (args[i]){
			case "-factory"			: this.factory = args[i+1].toLowerCase(); break;
			case "-udVersion"		: this.udVersion = args[i+1].toLowerCase(); break;
			}
		}
		UDlanguages.setUdVersion(udVersion);
		UDlanguages.addLanguages();
		ConlluToConllMapper.runUDversion();
	}


	private void errorMessageAndExit(){
		System.err.println(
				"Use 'udProcessor"
				+ "-factory GNT|MDP "
				+ "-udVersion 1_2|1_3|udify|iread "
				+ "to run udProcessor factory.");
		// Exit with error !
		System.exit(1);
	}

	private void setArgValues(String[] args) throws IOException {
		if (args.length == 0) {
			System.err.println("No arguments specified. Run:");
			errorMessageAndExit();
		} else if ((args.length % 2) != 0) {
			System.err.println("Not all arguments have values! Check!");
			errorMessageAndExit();
		} else
			this.initNemexArguments(args);
	}

	// Main callers

	private static void runUDprocessor (UDprocessor udProcessor) throws IOException, ConfigurationException {

		LOG.info("Create UD models : ");

		if (udProcessor.factory.equalsIgnoreCase("gnt")) {
			UDlanguageGNTmodelFactory udFactory = new UDlanguageGNTmodelFactory(UDlanguages.version);
			
			udFactory.runMultilingualPOStagger();
			
		}
	}
	
	/**
	 * Ok, seems to work:
	 * need to augment to different taggers, and MDP
	 * make sure that config files have correct path names
	 * this is why I call mapper in it init function
	 * but, this needs only to be done once or only have parameters changed !
	 */

	/**
	 * @param args
	 * @throws XMLStreamException 
	 * @throws IOException 
	 * @throws InvalidPropertiesFormatException 
	 * @throws ConfigurationException 
	 */
	public static void main(String[] args) 
			throws InvalidPropertiesFormatException, IOException, XMLStreamException, ConfigurationException {
		UDprocessor udProcessor = new UDprocessor();
		udProcessor.setArgValues(args);
	    runUDprocessor(udProcessor);
	}
}
