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

