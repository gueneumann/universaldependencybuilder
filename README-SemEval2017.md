# Processing CONLL 2017 data

- treebanks are here:

	/local/data/UniversalDependencies/conllSharedtask2017/Universal Dependencies 2.0/ud-treebanks-conll2017

	Inclduing raw text files

- gold test files:

	/local/data/UniversalDependencies/ud-test-v2.0-conll2017/gold/conll17-ud-test-2017-05-09

- raw test text files:

	/local/data/UniversalDependencies/ud-test-v2.0-conll2017/input/conll17-ud-test-2017-05-09

	includes test files for 82 languages
	including "surprise" languages (PUD)
	and one corrupted: 

## Approach

- core idea is to process raw text files .txt 

- create initial CONLL files from them

- do POS tagging with GNT before testing MDP model

- OR use pre-tokenized files

-> using given POS tags or those computed by GNT

- since test data is in separated folder

	- adapt com.gn.data.ConlluToConllMapper.makeConlluFileName(String, String, String) -> DONE
	
## Testing for 55 big treebank languages
	
		POS tagging -> DONE
		MDP parsing -> DONE
		Morph		 -> CRASHES
			for language Czech -> 
			Exception in thread "main" java.lang.IllegalArgumentException: 'number of classes' * 'number of instances' is too large: 2487*1826516
		
### Morph tagger

- run it for iREAD languages first
- integrate into MunderLinede.dfki.lt.mdparser.parser.ConllUtils.readConllFile(String, boolean)

- Then, I think following can work

	create liblinear file
		run gnt.conf file with create.liblinear.input.file = true 
		or make sure a different global gnt.conf can be selected
		make sure that the liblinear input files are stored in a temp-dir so that they can be deleted later
		NOTE: this will already create zip file XY-MORPHmodel.zip, but without the liblinear file
			update: de.dfki.mlt.gnt.trainer.TrainerInMem.trainFromConllTrainingFilesInMemory(List<String>, int, int, int)
	run liblinear to train model
		train -s 4 -c 0.1 -e 0.3 liblinear_input_model_CSUNIMORPH_2_0iw-1sent_FTTFF_MCSVM_CS.txt model_CSUNIMORPH_2_0iw-1sent_FTTFF_MCSVM_CS.txt
	
	OOOHHH: liblinear crashs - file to big ? -> OK, then test language-wise
		
	integrate into to resulting ZIP file
	
	
