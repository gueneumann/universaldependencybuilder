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

## Used files

Currently, only the 55 big treebank languages are used, because 
8 small language do not have development data (can be solved), and surprise language do not have training data.

		// The 8 small data set
		ignoreList.add("fr_partut"); // devel does not exist
		ignoreList.add("ga"); // devel does not exist
		ignoreList.add("gl_treegal"); // devel does not exist
		ignoreList.add("la"); // devel does not exist
		ignoreList.add("sl_sst"); // devel does not exist
		ignoreList.add("ug"); // devel does not exist
		ignoreList.add("uk"); // devel does not exist
		ignoreList.add("kk"); // devel does not exist
		// Ignore because has no test data - also done challenge
		ignoreList.add("it_partut"); // test does not exist

## Testing for 55 big treebank languages

POS tagging -> DONE
		
MDP parsing -> DONE

Morph		 -> DONE for 48/ of the 55 languages
		
		UDlanguageGNTmodelFactory.ignoreList.add("cs"); // liblinear crashes -> too large input file ?
		UDlanguageGNTmodelFactory.ignoreList.add("cs_cac"); // liblinear crashes -> too large input file ?
		UDlanguageGNTmodelFactory.ignoreList.add("fi"); // too small java heap (16GB)
		UDlanguageGNTmodelFactory.ignoreList.add("ru_syntagrus"); // too small java heap  (16GB)
		
		UDlanguageGNTmodelFactory.ignoreList.add("en_lines"); // no morphology
		UDlanguageGNTmodelFactory.ignoreList.add("ja"); // no morphology
		UDlanguageGNTmodelFactory.ignoreList.add("sv_lines"); // no morphology
	
	
