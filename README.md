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

	- maps UD source files to my conll files, which basically means
		- ignore: comment lines
		- ignore: multiple-span tokens indexes of form 1-2
		- ignore: alternative token id of form 8.1
		
	- create pure text files
		- collect tokens of a sentence into one line
		
		
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

	- adapt com.gn.data.ConlluToConllMapper.makeConlluFileName(String, String, String)


