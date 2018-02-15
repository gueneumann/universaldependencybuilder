# UniversalDependencyBuilder

A simple tool for creating model files for GNT and MDParser using conllu files.

It is tested for UD versions 1.2 and 1.3 and 2.1

When features are changed make sure to run first:

com.gn.data.ConlluToConllMapper.runUDversion()

And then run:
GNT and MDP training factory!

Otherwise, the old corpus and data properties will be used!

## How it works:

- UDlanguages.java

	- define paths to source UD dir and my conll dir
	- set languages to be ignore (those that cause errors)
	- only consider languages which have training file
	- define pairs of (languageName, languageID) from file names

- ConlluToConllMapper.java:

	- maps UD source files to my conll-gn files, which basically means
		- ignore: comment lines
		- ignore: multiple-span tokens indexes of form 1-2
			German treebank example:
				2	hingen	hängen	VERB	VVFIN	Number=Plur|Person=3|VerbForm=Fin	0	root	_	_
				3-4	zum	_	_	_	_	_	_	_	_
				3	zu	zu	ADP	APPR	_	5	case	_	_
			-> ignore second raw
		- ignore: alternative token id of form 8.1
			Finish treebank example:
				8	joskus	joskus	ADV	Adv	_	9	orphan	8.1:advmod	_
				8.1	kuuluu	kuulua	VERB	_	Mood=Ind|Number=Sing|Person=3|Tense=Pres|VerbForm=Fin|Voice=Act	_	_	3:conj	_
				9	karjahdus	karjahdus	NOUN	N	Case=Nom|Number=Sing	3	conj	8.1:nsubj	_		
			-> ignore second raw
	- this basically means: only make sure to ignore certain conll-u lines
		
	- conll-u format:
		16	the	the	DET	DT	Definite=Def|PronType=Art	18	det	_	_
	- conll-gn format:
		16	the	the	DET	DT	Definite=Def|PronType=Art	18	det	_	_
		
	- create pure text files
		- collect tokens of a sentence into one line
		
## Extracting POS tags

counting columns from 0

	conll-gn columns: 
		1: word form
		3: universal pos tag

## Extracting MDParser tags

counting columns from 0

	conll-u columns: 
		1: word form
		3: universal pos tag
		6: headId
		7: label

## Extracting MORPH tags

counting columns from 0

	conll-gn columns: 
		1: word form
		5: feature
		
Take care:

	if no feature, then create "_" tag

Done

		
## Processing conll 2017 data

- treebanks are here: /local/data/UniversalDependencies/conllSharedtask2017/Universal Dependencies 2.0/ud-treebanks-conll2017

- test files:		/local/data/UniversalDependencies/ud-test-v2.0-conll2017/gold/conll17-ud-test-2017-05-09

	includes test files for 82 languages
	including "surprise" languages (PUD)
	and one corrupted: 

- core idea is to process raw text files .txt 

- create initial CONLL files from them

- do POS tagging with GNT before testing MDP model

- OR use pre-tokenized files

-> using given POS tags or those computed by GNT

- since test data is in separated folder

	- adapt com.gn.data.ConlluToConllMapper.makeConlluFileName(String, String, String) -> DONE
	
## Testing

	- 55 big treebank languages
	
		POS tagging -> DONE
		MDP parsing -> DONE
		Morph		 -> CRASHES
			for language Czech -> 
			Exception in thread "main" java.lang.IllegalArgumentException: 'number of classes' * 'number of instances' is too large: 2487*1826516
		
## Morph tagger

- run it for iREAD languages first
- integrate into MunderLine

- then

	create liblinear file
	run liblinear to train model
	integrate into to resulting ZIP file

## Use conll-2017 eval script

	from:	./conll17_ud_eval.py -v ../../conll20/German/de-ud-test.conll ../../conll20/German/de-ud-test.conll
	call it: ./conll17_ud_eval.py -v ../../conll20/German/de-ud-test.conll ../../conll20/German/de-ud-test.conll
	
### In order to use it as part of my UniversalDependencyBuilder

	Create conll-gn output file for predicted tasks
		- merge results for POS tagger, morphology, dependency parser