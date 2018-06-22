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


RUNTIME:

MDP:

Complete Training time: 47763 milliseconds.
Complete training for 4 languages:
System time (msec): 117884
Testing of: <English,en>
Time to read model (msec): 1050
using algorithm "covington"
No. of sentences: 2078
No. of threads: 8
Time to parse (msec): 224
Speed (sent/s): 9276.785714285714
Number of configurations: 67729
Average number of configurations per sentence: 32


Testing of: <German,de>
Time to read model (msec): 1018
using algorithm "covington"
No. of sentences: 978
No. of threads: 8
Time to parse (msec): 58
Speed (sent/s): 16862.068965517243
Number of configurations: 44825
Average number of configurations per sentence: 45


Testing of: <Greek,el>
Time to read model (msec): 145
using algorithm "covington"
No. of sentences: 457
No. of threads: 8
Time to parse (msec): 33
Speed (sent/s): 13848.484848484848
Number of configurations: 31270
Average number of configurations per sentence: 68


Testing of: <Spanish,es>
Time to read model (msec): 1432
using algorithm "covington"
No. of sentences: 427
No. of threads: 8
Time to parse (msec): 41
Speed (sent/s): 10414.634146341463
Number of configurations: 39194
Average number of configurations per sentence: 91

POS

Create eval file: eval/en-ud-test.txt
System time (msec): 258
Sentences: 2077
Testing instances: 25096
Sentences/sec: 8050
Words/sec: 97271

Create eval file: eval/de-ud-test.txt
System time (msec): 177
Sentences: 977
Testing instances: 16268
Sentences/sec: 5519
Words/sec: 91909

Create eval file: eval/el-ud-test.txt
System time (msec): 73
Sentences: 456
Testing instances: 10422
Sentences/sec: 6246
Words/sec: 142767

Create eval file: eval/es-ud-test.txt
System time (msec): 80
Sentences: 426
Testing instances: 12000
Sentences/sec: 5325
Words/sec: 150000

MORPH

Create eval file: eval/en-ud-test.txt
System time (msec): 290
Sentences: 2077
Testing instances: 25096
Sentences/sec: 7162
Words/sec: 86537

Create eval file: eval/de-ud-test.txt
System time (msec): 315
Sentences: 977
Testing instances: 16268
Sentences/sec: 3101
Words/sec: 51644

Create eval file: eval/el-ud-test.txt
System time (msec): 149
Sentences: 456
Testing instances: 10422
Sentences/sec: 3060
Words/sec: 69946

Create eval file: eval/es-ud-test.txt
System time (msec): 147
Sentences: 426
Testing instances: 12000
Sentences/sec: 2897
Words/sec: 81632



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

### MDP without projectivity test:

de.dfki.lt.mdparser.algorithm.Dependency.isPermissible(DependencyStructure, boolean)

Complete testing for 4 languages:
System time (msec): 4355
Lang                 |  UAS   |  LAS   |  Speed tot. 
--------------------------------------------
English              |  83.83 |  81.22 | 
German               |  77.94 |  72.52 | 
Greek                |  83.63 |  80.23 | 
Spanish              |  84.62 |  81.19 | 
--------------------------------------------
Avg                  |  82.51 |  78.79
