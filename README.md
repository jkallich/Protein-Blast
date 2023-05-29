# Protein Blast
This application calls the Protein BLAST service maintained by NCBI and downloads the result in XML format and saves the result in a file. After that, it parses the XML file, picks the relavant data and saves them in a comma separated version (CSV) file.

# Acronyms
* NCBI: National Center for Biotechnology Information.
* BLAST: Basic Local Alignment Search Tool.

# Generate java code that help in parsing the XML output
```
$ xjc -d src -p com.jkallich.xml -dtd "http://www.ncbi.nlm.nih.gov/dtd/NCBI_BlastOutput.dtd"
```

# Move the generated code to maven directory
```
$ mv src/com/jkallich/xml src/main/java/com/jkallich/
```

# Installing Maven
```
sudo apt install maven
```

# Build the project and create *.jar file
```
$ mvn -s settings.xml clean package
```

# Create local files
Create blast-files.txt with list of files to be parsed. Any line that starts with a # sign is treated as a comment and are ignored.
This tep can be skipped if blast part is not run yet.
```
data/2023-05-28-02-22-20/output--LARP7--ORG1--75EPYT33011--xml.txt
data/2023-05-28-02-22-20/output--LARP7--ORG2--75EU6TKY012--xml.txt
data/2023-05-28-02-22-20/output--LARP7--ORG3--75EV6W45013--xml.txt
```

# Create local file
Create gene-symbols.txt with list of genes to be processed. Any line that starts with a # sign is treated as a comment and are ignored.
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

# Create local file
Create organisms.txt with list of organisms. Any line that starts with a # sign is treated as a comment and are ignored.
Supply three organisms in the format expected by NCBI.
```
Rabbit Rotavirus[ORGN]
Three-toed Sloth[ORGN]
Domestic Horse[ORGN]
```

# Run the program
The application can be run in 3 modes.

## 1. Run Blast service and download the results to a file.
```
$ java -DblastFlag=true -cp target/Protein-Blast-1.0.0-SNAPSHOT.jar com.jkallich.NcbiBlastApp
```
## 2. Parse the previously downloaded the results and generate CSV file.
```
$ java -DparseFlag=true -cp target/Protein-Blast-1.0.0-SNAPSHOT.jar com.jkallich.NcbiBlastApp
```
## 3. Do #1 and #2 both together
```
$ java -DblastFlag=true -DparseFlag=true -cp target/Protein-Blast-1.0.0-SNAPSHOT.jar com.jkallich.NcbiBlastApp
```

# References
* https://blast.ncbi.nlm.nih.gov/Blast.cgi?PAGE=Proteins
* http://ncbi.github.io/blast-cloud/dev/api.html
* https://biojava.org/wiki
* https://biojava.org/wiki/BioJava:GetStarted
* https://biojava.org/wiki/BioJava:CookBook3:NCBIQBlastService
* https://biojava.org/wiki/BioJava:CookBook3:ParsingBlastXMLOutput
* https://github.com/biojava/biojava
* https://github.com/biojava/biojava-tutorial/blob/master/core/README.md
* http://www.di.unito.it/~botta/didattica/biojavaTutorial.pdf
* http://alextblog.blogspot.com/2012/05/ncbi-blast-jaxb-biojava-blasting-like.html

