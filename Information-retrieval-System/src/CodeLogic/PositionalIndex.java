package CodeLogic;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *                  Singleton class
 * This Section will introduce the Singleton design pattern
 *
 * The Singleton class defines the `GetInstance` method that serves as an
 * alternative to constructor and lets clients access the same instance of this
 * class over and over.
 *
 */
public class PositionalIndex {
    // Hold the class instance.
    private static PositionalIndex instance = null ;
    public static int documentCount ;
    public static ArrayList<String> docs;

    private static ArrayList<String> termList; // To holds all the distinct terms
    private static ArrayList<ArrayList<DocId>> docLists; // To hold all docLists


    // Get the term list
    public ArrayList<String> getTermList() {
        return termList;
    }

    public ArrayList<ArrayList<DocId>> getDocLists() {
        return docLists;
    }

    /**
     * This is the static method that controls the access to the singleton
     * instance.
     * On the first run, it creates a singleton object and places it
     * into the static field.
     * On subsequent runs, it returns the existing
     * object stored in the static field.
     */
    public static PositionalIndex getInstance()
    {
        if(instance == null)
        {
            instance = new PositionalIndex();
        }
        return instance;
    }

    /*
     *Constructor
     *   The Singleton's constructor should always be private to prevent direct
     *   construction calls with the `new` operator.
     */

    /**
     * Construct a positional index
     *
     */

    private PositionalIndex() {
        instance = this;
        termList = new ArrayList<>();
        docLists = new ArrayList<>();
        ArrayList<File> FILES = new ArrayList<>();

        try {
            File[] FileArray = new File("Docs").listFiles();
            for(File file: FileArray){
                if(file.getName().endsWith(".txt")){
                    FILES.add(file);
                }
            }
            docs = new ArrayList<>();
            documentCount = FILES.size();

            System.out.println("Document: "+ documentCount);
            // Add files names
            for (File file : FILES) {
                docs.add(file.getName());
            }

            // Sort the document
            int[] docsIds = new int[docs.size()];

            for (int i = 0; i < docs.size(); i++) {
                if(Character.isDigit(docs.get(i).charAt(1))) {
                    docsIds[i] = Integer.parseInt(docs.get(i).substring(0,2));
                    System.out.println(docsIds[i]);
                } else {
                    docsIds[i] = Integer.parseInt(String.valueOf(docs.get(i).charAt(0)));
                }
            }

            // Start sorting
            int temp;
            String temp2;
            File temp3;
            for (int i = 0; i < docsIds.length; i++) {
                for (int j = i + 1; j < docsIds.length; j++) {
                    if (docsIds[i] > docsIds[j]) {
                        temp = docsIds[i];
                        temp2 = docs.get(i);
                        temp3 = FILES.get(i);

                        docsIds[i] = docsIds[j];
                        docs.set(i, docs.get(j));
                        FILES.set(i, FILES.get(j));

                        docsIds[j] = temp;
                        docs.set(j, temp2);
                        FILES.set(j, temp3);
                    }
                }
            }
            /*--------------------------------------*/
            // Loop on all documents
            for (int i = 0; i < FILES.size(); i++) {
                // Read the file content
                StringBuilder all_lines = new StringBuilder();

                BufferedReader reader = new BufferedReader(new FileReader(FILES.get(i)));

                // Read all file content
                String line;
                line = reader.readLine();


                while ((line != null)){
                    // Normalization - Take all lower case  .
                    all_lines.append(line.toLowerCase());
                    // add new line after every line
                    all_lines.append("\n");

                    // read next line
                    line = reader.readLine();
                }

                // Tokenization -- Split all the file content on spaces and new lines
                String[] tokens = all_lines.toString().split("\\s+");

                //* Construct the PI
                for (int j = 0; j < tokens.length; j++)
                {
                    // Remove the term if it is  stop word
                    if (StopWords.IsStopWord(tokens[j])) {
                        continue; // skip this term
                    }

                    // Check if the term is already exists on the term list
                    if ( ! termList.contains( tokens[j] ) )
                    {
                        termList.add(tokens[j]); // Add the token to the term list
                        DocId documentId = new DocId(i, j); // Save the position of the term

                        ArrayList<DocId> docList = new ArrayList<>();

                        docList.add(documentId); // Add the documentId(Hold the docId it's position) to the docList

                        docLists.add(docList); // save the docList to the Global docLists
                    }
                    // If the term is already exists on the term list
                    else {

                        int INDEX = termList.indexOf(tokens[j]);
                        ArrayList<DocId> docList = docLists.get(INDEX);
                        boolean match = false;
                        // Old term same document also seen before
                        for (DocId doid : docList) {
                            if (doid.documentID == i) {
                                doid.insertPosition(j);
                                match = true;
                            } // end if
                        } // end for

                        // Old term new document
                        if (!match) {
                            DocId doid = new DocId(i, j);
                            docLists.get(INDEX).add(doid);
                        } // end if
                    } // end else
                } // end for (loops terms)
            }// end for (loops documents)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the positional index for all terms
     * @return arrayList contains the termList and the docList
     */
    public ArrayList<ArrayList> getPositionalIndex () {
        ArrayList<ArrayList> Outputs = new ArrayList<>();

        Outputs.add(termList);
        Outputs.add(docLists);

        return Outputs;
    }

    /**
     * Return the string representation of a positional index
     */
    public String toString()
    {
        String matrixString = "";
        ArrayList<DocId> docList;
        for(int i=0;i<termList.size();i++){
            docList = docLists.get(i);
            matrixString += String.format("%-15s", termList.get(i));
            matrixString += String.format("Docfreq : %-10s", docList.size());

            for (DocId docId : docList) {
                matrixString += docId + "\t";
            }
            matrixString += "\n";
        }

        return matrixString;
    }
}