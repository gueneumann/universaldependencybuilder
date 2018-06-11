# Processing IREAD languages based on UD 2_0:

conlluPath = "/local/data/UniversalDependencies/ireadUDTests/";
conllPath = "/local/data/UniversalDependencies/ireadConllTests/";

## POS tagging:

Testing instances: 12000
Sentences/sec: 5325
Words/sec: 150000
All pos: 12000 Correct: 11407 Accuracy: 95.06%
All OOV pos: 874 Correct: 711 Accuracy: 81.35%
All InV pos: 11126 Correct: 10696 Accuracy: 96.14%
Time: 5636; Complete testing for 4 languages:
Lang                 |  Acc   |  OOV   |  INV   |  Tok/Sec
------------------------------------------
English              |  92.72 |  73.87 |  94.61 | 
German               |  91.55 |  84.85 |  92.45 | 
Greek                |  95.26 |  82.39 |  97.93 | 
Spanish              |  95.06 |  81.35 |  96.14 | 
------------------------------------------
Avg                  |  93.65 |  80.62 |  95.28

## Morph tagging:

Testing instances: 12000
Sentences/sec: 2766
Words/sec: 77922
All pos: 12000 Correct: 11530 Accuracy: 96.08%
All OOV pos: 874 Correct: 737 Accuracy: 84.32%
All InV pos: 11126 Correct: 10793 Accuracy: 97.01%
Time: 47422; Complete testing for 4 languages:
Lang                 |  Acc   |  OOV   |  INV   |  Tok/Sec
------------------------------------------
English              |  94.21 |  81.81 |  95.46 | 
German               |  81.95 |  66.49 |  84.02 | 
Greek                |  89.17 |  73.62 |  92.39 | 
Spanish              |  96.08 |  84.32 |  97.01 | 
------------------------------------------
Avg                  |  90.35 |  76.56 |  92.22

## MDP dependency analysis on test:

Complete testing for 4 languages:
System time (msec): 4153
Lang                 |  UAS   |  LAS   |  Speed tot. 
--------------------------------------------
English              |  83.75 |  81.13 | 
German               |  76.46 |  71.24 | 
Greek                |  83.12 |  79.85 | 
Spanish              |  84.54 |  81.09 | 
--------------------------------------------
Avg                  |  81.97 |  78.33

## MDP dependency analysis on train:

Complete testing for 4 languages:
System time (msec): 13201
Lang                 |  UAS   |  LAS   |  Speed tot. 
--------------------------------------------
English              |  91.06 |  89.74 | 
German               |  86.91 |  84.63 | 
Greek                |  90.79 |  89.03 | 
Spanish              |  89.99 |  87.86 | 
--------------------------------------------
Avg                  |  89.69 |  87.81
