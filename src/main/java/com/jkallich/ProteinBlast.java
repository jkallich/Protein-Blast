package com.jkallich;

import com.jkallich.xml.BlastOutput;
import com.jkallich.xml.Hit;
import com.jkallich.xml.Hsp;
import org.biojava.nbio.ws.alignment.qblast.BlastProgramEnum;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastOutputProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.biojava.nbio.ws.alignment.qblast.BlastAlignmentParameterEnum.ENTREZ_QUERY;

/*
Welcome to Protein BLAST!

Please check out my GitHub Repository for this project here: github.com/jkallich/Protein-Blast
*/

public class ProteinBlast {

    // An ArrayList of all the genes provided
    private static ArrayList<String> GENES = new ArrayList<>();
    // TODO: Ask Akka if she wants this in the output CSV file just to be safe
    // An ArrayList of all the UniProtIds provided
    private static ArrayList<String> UniProtIds = new ArrayList<>();
    // An ArrayList of all the squences provided
    private static ArrayList<String> SEQUENCES = new ArrayList<>();

    // indication of whether user would like to blast their input (aka obtain XML files)
    private static boolean blastFlag;
    // indication of whether user would like to process the obtained XML files (aka obtain the output)
    private static boolean parseFlag;

    // current date & time
    private static String startDateTime = Instant.now().toString().replaceAll("[T:]", "-").substring(0,19);

    // Holds the names of all files created during the process of obtaining the output (XML files from websites)
    private static List<String> FILES = new ArrayList<>();
    // Holds the 'Hits' whose data will be put in the CSV file
    private static ArrayList<DataRow> CSV = new ArrayList<>();

    // Has the different organisms being studied
    private static ArrayList<String> ORGANISMS;
    // Connects each organism's informal name to the name by which it is identified by the website(s)
    private static HashMap<String, String> ORGANISM_MAP = new HashMap<>();

    private static int BLAST_SLEEP_SECONDS = 10;

    // Name of the file containing the list of all the XML files created by the current -blast-
    private static String blastFiles = String.format("Data/%s/blast-files.txt", startDateTime);
    // Name of the file with the list of all the XML files created for all of the data in the current -group-
    private static String allBlastFiles;

    // Name of the CSV file that the results will be written to
    private static String csvFile = String.format("Data/%s/final-results.csv", startDateTime);

    public static void main(String[] args) throws Exception {
        System.out.println("startDateTime: " + startDateTime);

        getInput();

        String directoryName = String.format("Data/%s", startDateTime);
        createFolder(directoryName);

        // Get blastFlag and parseFlag from Configurations or from the terminal command
        blastFlag= Boolean.parseBoolean(System.getProperty("blastFlag", "false"));
        parseFlag= Boolean.parseBoolean(System.getProperty("parseFlag", "false"));
        allBlastFiles = System.getProperty("allBlastFiles", "blast-files.txt");
        ORGANISM_MAP = readOrganisms("organisms.txt");

        System.out.println("blastFlag: " + blastFlag);
        System.out.println("parseFlag: " + parseFlag);
        System.out.println("allBlastFiles: " + allBlastFiles);

        if(blastFlag) {
            // loops through the input and makes a file for each animal, and adds XML file names to FILES
            callBlast();
        }

        if(parseFlag) {
            if(FILES.isEmpty()) {
                // IF NO NAMES IN FILES VAR: If doing the blast/parse steps separately, then this is the part where you're doing the parse separately.
                // reads all the names of the XML files needed
                readFILES();
            }

            callXMLParse(); // gets  info needed from XML files and write CSV arrayList
        }

        if (!CSV.isEmpty()) {
            writeCSVFile(); // IF THERE ARE DATAROWS IN CSV VAR: writes the ArrayList of 'data rows' into a new CSV file
        }
    }

    /**
     * Accepts the name of the file the user would like to process,
     * reads the file/lines,
     * organises each line,
     * and complies all lines into an ArrayLists of input datasets for genes, ids, and sequences.
     * @throws FileNotFoundException In case the file name does not exist.
     */
    private static void getInput () throws FileNotFoundException {
        String filename = System.getProperty("filename","gene-symbols.txt");

        File file = new File(filename);
        Scanner reader = new Scanner(file);

        while (reader.hasNextLine()) {
            String line = reader.nextLine();

            String[] lineParts = line.split(",");

            GENES.add(lineParts[0].trim());
            UniProtIds.add(lineParts[1].trim());
            SEQUENCES.add(lineParts[2].trim());
        }

//        System.out.println("INPUTS:");
//        for(ArrayList<String> input : INPUTS) {
//            for(String part : input) {
//                System.out.print(part + "  ");
//            }
//            System.out.println();
//        }
        System.out.println("filename: " + filename);

    }

    private static void createFolder(String directoryName) throws Exception{
        File directory = new File(directoryName);
        directory.mkdirs();
    }

    private static HashMap<String, String> readOrganisms(String file) throws Exception {
        System.out.println("Reading organisms.txt...");

        String trimmedLine;

        HashMap<String, String> organisms = new HashMap<>();

        Path path = Paths.get(file);

        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            trimmedLine = line.trim();

            String[] parts = line.split(",");

            if (parts.length != 2) {
                throw new Exception("MISSING INPUT\nIn 'organism.txt', make sure every line has two components (informal organism name & organism name for the website) separated by a comma.");
            }

            if (trimmedLine.length() > 5 && !trimmedLine.startsWith("#")) {
                String informalName = parts[0].trim();
                String formalName = parts[1].trim();

                organisms.put(informalName, formalName);
            }
        }

        ORGANISMS = new ArrayList<>(organisms.keySet());

        Collections.sort(ORGANISMS);

        return organisms;
    }

    /**
     * 'Blasts' the protein with the given data and obtains XML files containing the data needed for output
     * @throws Exception In case the file reading goes wrong.
     */
    private static void callBlast() throws Exception {
        String filePath;

        // Looping through every data set given
        for(int i= 0; i < SEQUENCES.size(); i++) {
            System.out.println("Starting files for gene " + GENES.get(i));

            String directoryName = String.format("Data/%s/%s--%d", startDateTime, GENES.get(i), i+1);
            createFolder(directoryName);

            // looping through the different organisms that are being studied
            for(int j= 0; j < ORGANISMS.size(); j++) {
                System.out.println("\tSubmitting request for " + ORGANISMS.get(j));
                filePath = blast(GENES.get(i), i+1, SEQUENCES.get(i), ORGANISMS.get(j));  // returns name of file created with the XML stuffs

                FILES.add(filePath);
            }
        }

        writeBlastFiles();
    }

    /**
     * Performs the blast:
     *    Asks the website to do the blast,
     *    Waits for the website to complete the blast,
     *    Then gathers result and saves it in an XML file.
     * @param gene Starting gene.
     * @param sequence No idea. An ID???
     * @param organism The animal the human gene is being aligned with
     * @return The name of the file that contains the XML results of the blast.
     * @throws Exception In case any errors show up.
     */
    private static String blast(String gene, int geneCount, String sequence, String organism) throws Exception {
        // A service that interacts with the NCBIQB website for us
        NCBIQBlastService service = new NCBIQBlastService();

        String rID = submitBlastRequest(sequence, organism, service); // ask NCBIQ to 'align' the sequence, to blast it

        waitUntilAvailable(service, rID); // 'alignment' takes some time, so give NCBIQ some time to get it for you

        String filePath = downloadResults(gene, geneCount, organism, service, rID); // get the results and save to a file (stores file name into string)

        return filePath;
    }

    /**
     * Sets certain properties of the blast before requesting a blast from NCBIQB
     * and obtaining/returning the blast's rID
     * @param sequence No idea. Maybe some kind of id?
     * @param organism The organism the blast should align to
     * @param service The class that will interact with the website.
     * @return the rID fot eh requested blast
     * @throws Exception In case an errors arise.
     */
    private static String submitBlastRequest (String sequence, String organism, NCBIQBlastService service) throws Exception {

        // Set alignment options
        NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
        props.setBlastProgram(BlastProgramEnum.blastp); // Sets blast algorithm
        props.setBlastDatabase("nr");

        // Set organism the blast should align to
        props.setAlignmentOption(ENTREZ_QUERY, ORGANISM_MAP.get(organism));

        // Send blast request and save rID
        String rID = service.sendAlignmentRequest(sequence, props);

        System.out.println("Blast rID: " + rID);

        return rID;
    }

    /**
     * Waits in increasing intervals for the results to become available.
     * If you do not slow down the rate at which you are checking for results,
     * the website will blacklist the user.
     * @param service The service that interacts with the websites.
     * @param rID the rID of the blast request.
     * @throws Exception In case any errors arise.
     */
    private static void waitUntilAvailable (NCBIQBlastService service, String rID) throws Exception {
        int totalWaitSec = 0;

        System.out.println("Waiting for results...");

        while (!service.isReady(rID)) {
            totalWaitSec += BLAST_SLEEP_SECONDS;

            System.out.printf("\tSleeping for another %d seconds...\n", BLAST_SLEEP_SECONDS);

            Thread.sleep(BLAST_SLEEP_SECONDS * 1000);
        }

        System.out.printf("Total wait was %d seconds.\n", totalWaitSec);
    }

    /**
     * Obtains the XML results from the NCBIQB website,
     * writes them to a file.
     * @param gene The gene of the results
     * @param geneCount The number of the gene on the list of all genes
     * @param organism The current organism the gene is being aligned to.
     * @param service The service which interacts with the website for us.
     * @param rID The rID of the blast request.
     * @return The name of the result file.
     * @throws Exception In case an error arises.
     */
    private static String downloadResults (String gene, int geneCount, String organism, NCBIQBlastService service, String rID) throws Exception {
        NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties();

        // Read Results
        InputStream in = service.getAlignmentResults(rID, outputProps); // InputStream 'gets' output in chunks

        // Write results to file
        String filename = String.format("Data/%s/%s--%d/output--%s--%s--%s--xml.txt", startDateTime, gene, geneCount, gene, organism, rID);
        Path path = Paths.get(filename);

        System.out.printf("Downloading results of blast to file '%s'.\n", filename);

        // If an Exception arises, the code after 'try' and before the curly brace will run (making sure the file closes)
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));
             BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            String line;

            while ((line = reader.readLine()) != null) {
                writer.write(line); // reads one line at a time and writes it, appending \n onto each line
                writer.newLine();
            }
        }

        return filename;
    }

    /**
     * Writes the blast file that contains the names of the files containing the XML results of each gene and its organisms
     * @throws IOException In case an error arises.
     */
    private static void writeBlastFiles () throws IOException {
        Path path = Paths.get(blastFiles);

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (String f : FILES) {
                writer.write(f);
                writer.newLine();
            }
        }
    }

    /**
     * Adds all of the XML file names from allBlastFiles.txt to the ArrayList FILES.
     * @throws IOException In case an error arises.
     */
    private static void readFILES () throws IOException {
        Path path = Paths.get(allBlastFiles);

        // Looping through all the lines of allBlastFiles.txt: looping through all the XML file names
        for(String file : Files.readAllLines(path, StandardCharsets.UTF_8)) {

            // if the current line is less 5 characters, ignore.
            // if the line starts with '#' (is it's a comment), ignore.
            if (file.trim().length() > 5 && !file.trim().startsWith("#")) {

                // Add the XML file name to the ArrayList of file names
                FILES.add(file);
            }
        }
    }

    /**
     * Processes every blast result stored in the file names in FILES
     * @throws Exception In case an error arises.
     */
    private static void callXMLParse () throws Exception {
        for (String filePath : FILES) {
            processBlast(filePath);
        }
    }

    private static void processBlast(String filePath) throws Exception {
        System.out.println("Processing blast results of: " + filePath);

        // Parse XML file and convert into Java class
        BlastOutput blastOutput = parseXMLFile(filePath);

        // The list of hits that shows up for the search.
        List<DataRow> dataRows = collectTableData(blastOutput, filePath);

        // organizes the DataRows
        Collections.sort(dataRows);

        // Printing out rows for debugging purposes.
        printDataRows(dataRows);

        // Picks the first DataRow from the list, the one that will be used.
        CSV.add(dataRows.get(0));
    }

    /**
     * Parses an XML file and stores the information in it into the Java class BlastOutput
     * @param filePath The path of the XML file to be parsed.
     * @return BlastOutput with info from XML file
     * @throws Exception In case errors arise.
     */
    private static BlastOutput parseXMLFile(String filePath) throws Exception {

        // To avoid an error that comes up later on
        System.setProperty("javax.xml.accessExternalDTD", "all");

        // Code below used from the BLAST library source.
        File file = new File(filePath);

        JAXBContext jc = JAXBContext.newInstance(BlastOutput.class);

        Unmarshaller u = jc.createUnmarshaller();

        return (BlastOutput) u.unmarshal(file);
    }

    /**
     * Picks out necesary information from the XML file object using the Protein Blast Library
     * @param blastOutput The XML file object
     * @param fileName the name/path of the file
     * @return An ArrayList of the DataRows that shows up
     */
    private static List<DataRow> collectTableData(BlastOutput blastOutput, String fileName) {
        // Looks for this in the description of the hit to picks it out.
        Pattern pattern = Pattern.compile("isoform X[0-9]*");

        // Gets all of the hits using the Protein Blast Library
        List<Hit> hits = blastOutput.getBlastOutputIterations().getIteration().get(0).getIterationHits().getHit();

        // Query Length
        int blastOutputQueryLen = Integer.parseInt(blastOutput.getBlastOutputQueryLen());

        ArrayList<DataRow> dataRows = new ArrayList<>();
        int rowCount = 0;
        for (Hit hit : hits) {
            DataRow row = new DataRow();
            dataRows.add(row);

            // the XML file of the gene/organism
            row.fileName = fileName;

            // a subcategory on the XML file that contains some of the data needed
            Hsp hsp = hit.getHitHsps().getHsp().get(0);
            row.hspEvalue = hsp.getHspEvalue();

//            row.hitAccession = hit.getHitAccession();     // not using b/c slightly off from hspID, which is the actual value shown 
            row.hitAccession = hit.getHitId().split("[|]")[1];

            // The definition from where the 'isoform (#)' will be extracted from
            String hitDef = hit.getHitDef();
            Matcher matcher = pattern.matcher(hitDef);
            row.isoform = "";
            if (matcher.find()) {
                row.isoform = matcher.group(0);
            }
            
            row.hspScore = Integer.parseInt(hsp.getHspScore());
            
            row.hitLen = Integer.parseInt(hit.getHitLen());

            int hspIdentity = Integer.parseInt(hsp.getHspIdentity());
            int hspAlignLen = Integer.parseInt(hsp.getHspAlignLen());
            row.percentageIdentity = hspIdentity * 100.0 / hspAlignLen;

            int hspQueryTo = Integer.parseInt(hsp.getHspQueryTo());
            int hspQueryFrom = Integer.parseInt(hsp.getHspQueryFrom());
            row.queryCover = (hspQueryTo - hspQueryFrom + 1) * 100.0 / blastOutputQueryLen;
            
            // Collects only 10 hits.
            rowCount++;
            if (rowCount > 10) {
                break;
            }
        }

        return dataRows;
    }

    /**
     * For debuggin purposes, prints out the rows collected for the certain gene/organims pair.
     * @param dataRows
     */
    private static void printDataRows(List<DataRow> dataRows) {
        String header = String.format("%s, %s, %s, %s, %s, %s, %s", "Query Cover", "E Value",
                "% identity", "Accession Length", "Accession", "isoform", "fileName");
        System.out.println(header);

        for (int i = 0; i < dataRows.size(); i++) {

            DataRow row = dataRows.get(i);

            // @formatter:off
            String dataLine = String.format("%.2f, %13s, %.2f, %s, %s, %s, %s",
                    row.queryCover,
                    row.hspEvalue,
                    row.percentageIdentity,
                    row.hitLen,
                    row.hitAccession,
                    row.isoform,
                    row.fileName);
            // @formatter:on

            System.out.println(dataLine);
        }
    }

    private static void writeCSVFile () throws IOException {
        System.out.println("--------------------");
        System.out.println("Writing CSV file: " + csvFile);

        Path path = Paths.get(csvFile);

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            // Header of each column
            String header = String.format("%s, %s, %s, %s, %s, %s, %s, %s", "Gene Code", "Organism",
                    "Query Cover", "E Value", "% identity", "Accession Length", "Accession", "Isoform");

            System.out.println(header);

            writer.write(header);
            writer.newLine();

            for (int i= 0; i < CSV.size(); i++) {
                DataRow row = CSV.get(i);

                String[] fileNameParts = row.fileName.split("--");
                String geneCode = fileNameParts[2];
                String organism = fileNameParts[3];

                String dataLine = String.format("%s, %s, %.2f, %s, %.2f, %s, %s, %s",
                        geneCode,
                        organism,
                        row.queryCover,
                        row.hspEvalue,
                        row.percentageIdentity,
                        row.hitLen,
                        row.hitAccession,
                        row.isoform);

                System.out.println(dataLine);

                writer.write(dataLine);
                writer.newLine();
            }
        }
    }
}
