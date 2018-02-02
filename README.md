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


