# Use conll-2017 eval script


From ../../conll20/German/

call it: ./conll17_ud_eval.py -v de-ud-test.conll de-ud-test.conll
	

In order to use it as part of my UniversalDependencyBuilder

	Create conll-gn output file for predicted tasks
		- merge results for POS tagger, morphology
		- for dependency parser I already have it

## GNT tagger

Adapt method com.gn.UDlanguageGNTmodelFactory.testLanguage(String, String, boolean)

- should call tagger sentence-wise on test file
- should copy test file to result file
	- USE pos tag form tagger
	- keep all other information

- Make it work for ***POS*** and ***Morphology***

	- de.dfki.mlt.gnt.tagger.GNTagger.createConllResultFileFromEvalFile(int, Path, String, String)
	
			basically takes evalFile created by GNT and merges output of tagger with test file
			
			called from de.dfki.mlt.gnt.tagger.GNTagger.evalAndWriteResultFile(String, String, String) and then from UDlanguage GNT factory
			
	- can be used for POS tagger and MorphTagger by setting com.gn.UDlanguageGNTmodelFactory.tagger

- if this works, this can also be used as input for MDParser
		
		Testing of MDParser based on GNT POS predictions
		
### First tests


conll17_ud_eval.py -v de-ud-test.conll de-ud-test-POS-result.conll 

		get same accuracy as my own script
		
		seems to be the case for all languages
		
		
### First tests with Arabic MORPH

conll17_ud_eval.py -v ar-ud-test.conll ar-ud-test-MORPH-result.conll 

		get same accuracy as my own script

## MDParser

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