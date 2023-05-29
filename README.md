# Protein Blast
This application calls the Protein BLAST service maintained by NCBI and downloads the result in XML format and saves the result in a file. After that, it parses the XML file, picks the relavant data and saves them in a comma separated version (CSV) file.

# Acronyms
* NCBI: National Center for Biotechnology Information.
* BLAST: Basic Local Alignment Search Tool.

# Tools used in the development
* Java 11 version
* Generate Java classes for the NCBI schema
```
# These objects help in parsing the XML output
xjc -d src -p com.jkallich.xml -dtd "http://www.ncbi.nlm.nih.gov/dtd/NCBI_BlastOutput.dtd"
# Move the generated code to maven directory
mv src/com/jkallich/xml src/main/java/com/jkallich/
```

# Installing Maven
```
sudo apt install maven
```

# Build the project and create *.jar file
```
mvn -s settings.xml clean package
```

# Edit blast-files config file
Create blast-files.txt with list of files to be parsed. Lines that start with a # sign is treated as a comment and are ignored.
This step can be skipped if blast part is not run yet.
```
data/2023-05-28-02-22-20/output--LARP7--ORG1--75EPYT33011--xml.txt
data/2023-05-28-02-22-20/output--LARP7--ORG2--75EU6TKY012--xml.txt
data/2023-05-28-02-22-20/output--LARP7--ORG3--75EV6W45013--xml.txt
```

# Edit gene symbols config file
Create gene-symbols.txt with list of genes to be processed. Lines that start with a # sign is treated as a comment and are ignored.
```
Field order: GeneCode, UniProtKB, Sequence
If Sequence is missing, the program will look up Sequence and write it to data/2023-05-28-02-42-02/gene-symbols.txt 
which can be used to spot check if the program is working properly
Also, it can be added to the main gene-symbols.txt file so that we don't need to look it up the next time.
Example lines
LBH, Q53QV2, MSIYFPIHCPDYLRSAKMTEVMMNTQPMEEIGLSPRKDGLSYQIFPDPSDFDRCCKLKDRLPSIVVEPTEGEVESGELRWPPEEFLVQEDEQDNCEETAKENKEQ
LBR, Q14739
```
The initial creation of this file can be somewhat automated with these steps.
```
Go to uniprot.org

Click on Id Mapping link
    from: choose "Gene Name"
    to: choose "UniProtKB"
    organism: choose "Home Sapiens [9606]"
    click submit

Go to results tab
    note the messages
        464 IDs were mapped to 2,835 results
        26 ID were not mapped

    pick fewer columns

    download results as TSV
        open in excel or google docs
        each gene name has multiple UniProtKB ids
        to pick first one we can use this excel formula and filter by that field
            formula: =IF(A2=A1,0,1)

    spot check UniProtKB id by looking up GeneCode on GeneCards.com

    we can use this as input: gene-symbols.txt
```

# Edit organisms config file
Create organisms.txt with list of organisms. Lines that start with a # sign is treated as a comment and are ignored.
Supply three organisms in the format expected by NCBI.
```
DEER, deer[ORGN]
HORSE, Domestic Horse[ORGN]
DOG, dog[ORGN]
```

# Run the program
The application can be run in 3 modes. The use of third one is preferred.

#### 1. Run Blast service and download the results to a file.
```
$ java -DblastFlag=true -DparseFlag=false -cp target/Protein-Blast-1.0.0-SNAPSHOT.jar com.jkallich.ProteinBlast
```
#### 2. Parse the previously downloaded the results and generate CSV file.
```
$ java -DblastFlag=false -DparseFlag=true -cp target/Protein-Blast-1.0.0-SNAPSHOT.jar com.jkallich.ProteinBlast
```
#### 3. Do #1 and #2 both together
```
$ java -DblastFlag=true -DparseFlag=true -cp target/Protein-Blast-1.0.0-SNAPSHOT.jar com.jkallich.ProteinBlast
```

# Output 
* [Sample Console Output](https://github.com/jkallich/Protein-Blast/blob/main/sample-console-output.txt)
* [Sample CSV File with Final Result](https://github.com/jkallich/Protein-Blast/blob/main/sample-final-results.csv)

# References
* https://blast.ncbi.nlm.nih.gov/Blast.cgi?PAGE=Proteins
* http://ncbi.github.io/blast-cloud/dev/api.html
* https://www.UniProt.org/
* https://www.GeneCards.org/
* https://biojava.org/wiki
* https://biojava.org/wiki/BioJava:GetStarted
* https://biojava.org/wiki/BioJava:CookBook3:NCBIQBlastService
* https://biojava.org/wiki/BioJava:CookBook3:ParsingBlastXMLOutput
* https://github.com/biojava/biojava
* https://github.com/biojava/biojava-tutorial/blob/master/core/README.md
* http://www.di.unito.it/~botta/didattica/biojavaTutorial.pdf
* http://alextblog.blogspot.com/2012/05/ncbi-blast-jaxb-biojava-blasting-like.html

