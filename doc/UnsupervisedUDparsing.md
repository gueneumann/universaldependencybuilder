February, 2018:

# Ideas for developing an unsupervised UD parser.

__Can we use this also as a basis for OIE ? __

## Background 

Background is UDP parser defined in Alonso et al., ACL, 2017 - http://aclweb.org/anthology/E/E17/E17-1022.pdf

Core idea using English development data as reference:
		
		to define online an ordering over the content words of a sentence, which  defines the their tree level
		
		define function words as heads
		
			AUX, DET, SCONJ -> right-attaching
			
			CONJ, PUNCT -> left-attaching
			
			OTHERS -> no constraints
			
			IF last token(sent)=PUNCT -> attach it mainb predicate
		
		define online direction of ADP based on bigrams in sentence
		
Parsing assume PS-tagged as input.

Only unlabeled UD trees are computed




### UDP dependency rules:

ADJ −→ ADV

NOUN −→ ADJ, NOUN, PROPN

NOUN −→ ADP, DET, NUM

PROPN −→ ADJ, NOUN, PROPN

PROPN −→ ADP, DET, NUM

VERB −→ ADV, AUX, NOUN

VERB −→ PROPN, PRON, SCONJ


## My ideas

- do some statistics over "word-free" tree banks

	- collect all data from all languages
	
		- validate/check rules defined by UDP
	
	- do it for each individual languages
	
		- compute some sort of TF/IDF for head rules
		
- find alternative approach for sorting content words

- use morphological feature

- extend to get labeled edges and evaluate LAS
