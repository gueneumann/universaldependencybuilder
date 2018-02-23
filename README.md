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
- integrate into MunderLinede.dfki.lt.mdparser.parser.ConllUtils.readConllFile(String, boolean)

- then

	create liblinear file
	run liblinear to train model
	integrate into to resulting ZIP file

## Use conll-2017 eval script


From ../../conll20/German/

call it: ./conll17_ud_eval.py -v de-ud-test.conll de-ud-test.conll
	

In order to use it as part of my UniversalDependencyBuilder

	Create conll-gn output file for predicted tasks
		- merge results for POS tagger, morphology
		- for dependency parser I already have it

### GNT tagger

Adapt method com.gn.UDlanguageGNTmodelFactory.testLanguage(String, String, boolean)

- should call tagger sentence-wise on test file
- should copy test file to result file
	- USE poas tag form tagger
	- keep all other information

- Make it work for POS and Morphology

NOTE:

- if this works, this can also be used as input for MDParser
		
		Testing of MDParser based on GNT POS predictions

### MDParser

For MDP parser is automatically done as part of de.dfki.lt.mdparser.caller.MDPrunner.parseAndEvalConllFile(String, String, String)

HOWEVER: 

infosite needs to be set to 10! It is defined and set in de.dfki.lt.mdparser.parser.ConllUtils.readConllFile(String, boolean)

-> make sure it is set proper!

-> I changed MDParser.ConllUtils: added a public static variable so that one can change its value

-> ConllUtils.infosize = 10 set now in com.gn.UDlanguageMDPmodelFactory.main(String[])

-> DONE

### First tests with German:

- cd /local/data/UniversalDependencies/conll20/German

- conll17_ud_eval.py de-ud-test.conll de-ud-test-result.conll

-> crashes !
	__main__.UDError: There are multiple roots in a sentence
	
In sentence:

1	unterstützt	unterstützen	VERB	VVFIN	VerbForm=Fin	0	root	_	root
2	deshalb	deshalb	ADV	PAV	_	12	advmod	_	advmod
3	die	der	DET	ART	Case=Acc|Definite=Def|Number=Plur|PronType=Art	12	det	_	det
4	in	in	ADP	APPR	_	5	case	_	case
5	Gaza	Gaza	PROPN	NE	_	12	nmod	_	nmod
6	und	und	CCONJ	KON	_	9	cc	_	cc
7	in	in	ADP	APPR	_	9	case	_	case
8	dem	der	DET	ART	Definite=Def|PronType=Art	9	det	_	det
9	Westjordanland	Westjordanland	PROPN	NE	_	5	conj	_	conj
10	geplanten	geplant	ADJ	ADJA	Case=Acc|Degree=Pos|Number=Plur	12	amod	_	amod
11	neun	neun	NUM	CARD	NumType=Card	12	nummod	_	nummod
12	Industrieparks	Industriepark	NOUN	NN	Case=Acc|Number=Plur	0	root	_	root
13	.	.	PUNCT	$.	_	12	punct	_	punct

token 12 manually correct to 1 obj -> then whole file works

output is: 

UAS        |     77.98 |     77.98 |     77.98 |     77.98

LAS        |     71.90 |     71.90 |     71.90 |     71.90

	-> NOTE: P, R is the same always, because I do not process raw text and as such, the number of 
		nodes in the predicted tree is the same as the one in the gold tree
	-> 

compared to MDP eval: 
de             |  77.98 |  71.28 | 

	NOTE: 
		UAS is the same
		LAS is a bit smaller for MDParser
	
-> __WHY__ ??

### SAME crash for English:

1	Microsoft	Microsoft	PROPN	NNP	Number=Sing	3	nsubj	_	nsubj
2	is	be	AUX	VBZ	Mood=Ind|Number=Sing|Person=3|Tense=Pres|VerbForm=Fin	3	cop	_	cop
3	4	4	NUM	CD	NumType=Card	0	root	_	root
4	-	-	SYM	SYM	_	5	case	_	case
5	0	0	NUM	CD	NumType=Card	3	nmod	_	nmod
6	(	(	PUNCT	-LRB-	_	8	punct	_	punct
7	they	they	PRON	PRP	Case=Nom|Number=Plur|Person=3|PronType=Prs	8	nsubj	_	nsubj
8	took	take	VERB	VBD	Mood=Ind|Tense=Past|VerbForm=Fin	3	conj	_	conj
9	down	down	ADP	RP	_	8	compound:prt	_	compound:prt
10	Netscape	Netscape	PROPN	NNP	Number=Sing	8	obj	_	obj
11	,	,	PUNCT	,	_	13	punct	_	punct
12	Suns	Suns	PROPN	NNPS	Number=Plur	13	compound	_	compound
13	Systems	Systems	PROPN	NNPS	Number=Plur	10	conj	_	conj
14	,	,	PUNCT	,	_	15	punct	_	punct
15	MAC	MAC	PROPN	NNP	Number=Sing	10	conj	_	conj
16	and	and	CCONJ	CC	_	17	cc	_	cc
17	IBM	IBM	PROPN	NNP	Number=Sing	10	conj	_	conj
18	)	)	PUNCT	-RRB-	_	3	punct	_	punct
19	and	and	CCONJ	CC	_	20	cc	_	cc
20	Google	Google	PROPN	NNP	Number=Sing	3	conj	_	conj
21	may	may	AUX	MD	VerbForm=Fin	25	aux	_	aux
22	be	be	AUX	VB	VerbForm=Inf	25	cop	_	cop
23	their	they	PRON	PRP$	Number=Plur|Person=3|Poss=Yes|PronType=Prs	25	nmod:poss	_	nmod:poss
24	next	next	ADJ	JJ	Degree=Pos	25	amod	_	amod
25	target	target	NOUN	NN	Number=Sing	0	root	_	root
26	.	.	PUNCT	.	_	25	punct	_	punct

AND

1	American	american	ADJ	JJ	Degree=Pos	2	amod	_	amod
2	Food	food	NOUN	NN	Number=Sing	0	root	_	root
3	,	,	PUNCT	,	_	2	punct	_	punct
4	Soul	soul	NOUN	NN	Number=Sing	5	compound	_	compound
5	Food	food	NOUN	NN	Number=Sing	2	list	_	list
6	,	,	PUNCT	,	_	2	punct	_	punct
7	Mexican	mexican	ADJ	JJ	Degree=Pos	2	amod	_	amod
8	,	,	PUNCT	,	_	9	punct	_	punct
9	Italian	italian	ADJ	JJ	Degree=Pos	7	conj	_	conj
10	,	,	PUNCT	,	_	12	punct	_	punct
11	and	and	CCONJ	CC	_	12	cc	_	cc
12	Chinese	chinese	ADJ	JJ	Degree=Pos	7	conj	_	conj
13	are	be	AUX	VBP	Mood=Ind|Tense=Pres|VerbForm=Fin	15	cop	_	cop
14	the	the	DET	DT	Definite=Def|PronType=Art	15	det	_	det
15	options	option	NOUN	NNS	Number=Plur	2	obj	_	root
16	.	.	PUNCT	.	_	15	punct	_	punct

Same structural problem !

After manual correction it works:

UAS        |     84.28 |     84.28 |     84.28 |     84.28

LAS        |     81.38 |     81.38 |     81.38 |     81.38

MDParser eval:

en         |  84.28 |  81.06 | 


### Spanish

1	Tanto	tanto	ADV	_	_	3	advmod	_	advmod
2	el	el	DET	_	Definite=Def|Gender=Masc|Number=Sing|PronType=Art	3	det	_	det
3	trato	trato	NOUN	_	Gender=Masc|Number=Sing	0	root	_	root
4	como	como	CCONJ	_	_	6	case	_	case
5	el	el	DET	_	Definite=Def|Gender=Masc|Number=Sing|PronType=Art	6	det	_	det
6	servicio	servicio	NOUN	_	Gender=Masc|Number=Sing	3	nmod	_	nmod
7	es	ser	VERB	_	Mood=Ind|Number=Sing|Person=3|Tense=Pres|VerbForm=Fin	8	cop	_	cop
8	bueno	bue	ADJ	_	Gender=Masc|Number=Sing	0	root	_	root
9	.	.	PUNCT	_	_	8	punct	_	punct

Same structrual problem.

UAS        |     84.92 |     84.92 |     84.92 |     84.92

LAS        |     81.19 |     81.19 |     81.19 |     81.19

MDParser eval: 

es         |  84.92 |  81.12 | 