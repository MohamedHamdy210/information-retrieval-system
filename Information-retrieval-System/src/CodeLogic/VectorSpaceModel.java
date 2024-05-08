package CodeLogic;

import java.util.ArrayList;
import java.util.Collections;

public class VectorSpaceModel {
    public static int [] docs;
    private int docsLength;

    private int[][] TF;
    private int[] Doc_Feq;
    private double[][] TF_Weight;
    private double[] IDF;
    private double[][] TF_IDF;
    private double[][] Unit_Vector;

    private double[] documentLength;

    private ArrayList termList;
    private ArrayList<ArrayList<DocId>> docLists;
    /*------------Query------------*/
    private ArrayList Query_Tokens;
    private int[] Query_TF;
    private double[] Query_TF_Weight;
    private int[] Query_DF;
    private double[] Query_IDF;
    private double[] QueryTF_IDF;
    private double QueryLength;
    private double[] Query_Unit_Vector;
    /*------------------------*/
    private double[] ProdUnitVectors;
    public static double[] Ranking;
    public static String[] StringRanking;
    /*-------------------*/
    public VectorSpaceModel(int[] docs, ArrayList queryTokens) {
        // Initialize all arrays
        Init(docs, queryTokens);

        // Calling the important functions
        // 1. Document related functions
        TermFreq();
        PrintTF();
        DocumentFreq();
        PrintDocumentFreq();
        TfWeight();
        PrintTFWeight();
        IDF();
        PrintIDF();
        TfIdF();
        PrintTfIdf();
        calcDocLength();
        PrintDocumentLength();
        unitVector();
        PrintUnitVector();

        // 2. Query related functions
        QueryTF();
        PrintQueryTF();
        QueryTfWeight();
        PrintQueryTfWeight();
        QueryDocFreq();
        PrintQueryDocFreq();

        QueryIDF();
        PrintQueryIDF();

        QueryTfIdF();
        PrintQueryTfIdf();
        calcQueryLength();

        PrintQueryLength();
        QueryUnitVector();
        PrintQueryUnitVector();

        ProductOFUnitVectors();
        PrintProduct();

        Ranking();
        PrintRanking();

    }

    private void Init(int[] docs, ArrayList queryTokens) {
        this.docs = docs;
        // Remove Empty Docs
        this.docsLength = PositionalIndex.documentCount;

        PositionalIndex PI = PositionalIndex.getInstance();

        this.termList = PI.getTermList();
        this.docLists = PI.getDocLists();

        int termCount = termList.size();

        this.TF = new int[termCount][docsLength];
        this.Doc_Feq = new int[termCount];
        this.TF_Weight = new double[termCount][docsLength];
        this.IDF = new double[termCount];
        this.TF_IDF = new double[termCount][docsLength];
        this.Unit_Vector = new double[termCount][docsLength];
        this.documentLength = new double[docsLength];
        /*-----------------------*/
        this.Query_Tokens = queryTokens;
        this.Query_TF = new int[queryTokens.size()];
        this.Query_TF_Weight =  new double[queryTokens.size()];
        this.Query_DF = new int[queryTokens.size()];
        this.Query_IDF = new double[queryTokens.size()];
        this.QueryTF_IDF = new double[queryTokens.size()];
        this.QueryLength = 0.0;
        this.Query_Unit_Vector = new double[queryTokens.size()];
        /*-------------------*/
        this.ProdUnitVectors = new double[docs.length];

        Ranking = new double[ProdUnitVectors.length];
        StringRanking = new String[Ranking.length];
    }

    private void TermFreq() {
        for (int i = 0; i < termList.size(); i++) {
            ArrayList<DocId> docList = docLists.get(i);
            for (DocId docId : docList) {

                int docID = docId.documentID ;
                int termFreqInDoc = docId.termPositionList.size();

                TF[i][docID] = termFreqInDoc;
            }
        }
    }
    private void PrintTF() {
        System.out.println("*************Term Document Count Matrices *************");
        int index = 0;
        for (int[] x : TF) {
            System.out.print(termList.get(index++) + "  ");
            for (int y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
        System.out.println("*******************End Term Document Count Matrices***********************");
    }
    private void DocumentFreq() {
        for (int i = 0; i < termList.size(); i++) {
            Doc_Feq[i] = docLists.get(i).size();
        }
    }
    private void PrintDocumentFreq() {
        System.out.println("\n\n*************Term Document Frequency *************");
        int index = 0;
        for (int x : Doc_Feq) {
            System.out.print(termList.get(index++) + "  ");

            System.out.print(x + " ");

            System.out.println();
        }
        System.out.println("*******************End Document Frequency***********************");
    }
    private void TfWeight() {
        int tf;
        for (int i = 0; i < TF.length; i++) {
            for (int j = 0; j < TF[0].length; j++) {
                tf = TF[i][j];

                if (tf > 0) {
                    TF_Weight[i][j] = Math.round((1 + Math.log10(tf)) * 100.0) / 100.0;
                } else {
                    TF_Weight[i][j] = 0;
                }
            }
        }
    }
    private void PrintTFWeight() {
        System.out.println("\n*************TF Weight*************");
        int index = 0;
        for (double[] x : TF_Weight) {
            System.out.print(termList.get(index++) + "  ");
            for (double y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
        System.out.println("*******************End TF Weight***********************");
    }
    private void IDF() {
        for (int i = 0; i < Doc_Feq.length; i++) {
            IDF[i] = Math.round(Math.log10((double) docsLength / Doc_Feq[i]) * 100.0) / 100.0;
        }
    }
    private void PrintIDF() {
        System.out.println("\n*************IDF*************");
        int index = 0;
        for (double x : IDF) {
            System.out.print(termList.get(index++) + "  ");

            System.out.print(x + " ");

            System.out.println();
        }
        System.out.println("*******************End IDF***********************");
    }
    private void TfIdF() {
        double tfIdf;
        for (int i = 0; i < TF.length; i++) {
            for (int j = 0; j < TF[0].length; j++) {

                tfIdf = Math.round((TF_Weight[i][j] * IDF[i]) * 100.0) / 100.0;

                TF_IDF[i][j] = tfIdf;
            }
        }
    }
    private void PrintTfIdf() {
        System.out.println("\n*************TF_IDF*************");

        int index = 0;
        for (double[] x : TF_IDF) {
            System.out.print(termList.get(index++) + "  ");
            for (double y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
        System.out.println("*******************End TF_IDF***********************");
    }
    private void unitVector() {
        for (int i = 0; i < TF_IDF[0].length; i++) {
            for (int j = 0; j < TF_IDF.length; j++) {
                Unit_Vector[j][i] = Math.round(TF_IDF[j][i] / documentLength[i] * 100.0) / 100.0;
            }
        }
    }
    private void PrintUnitVector() {
        System.out.println("\n*************Unit Vector*************");

        int index = 0;
        for (double[] x : Unit_Vector) {
            System.out.print(termList.get(index++) + "  ");
            for (double y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
        System.out.println("*******************End Unit Vector***********************");
    }
    private void calcDocLength() {
        double allTfIdf = 0.0;

        for (int i = 0; i < docsLength; i++) {
            for (double[] doubles : TF_IDF) {

                allTfIdf += Math.pow(doubles[i], 2);

            }
            documentLength[i] = Math.round(Math.sqrt(allTfIdf) * 100.0) / 100.0;
            allTfIdf = 0.0;
        }
    }
    private void PrintDocumentLength() {

        int index = 0;
        for (double l : documentLength) {
            System.out.println("Document " + index++ + " Length is " + l);
        }
    }

    /*-----------------------------------*/
    private void QueryTF() {

        for (int i = 0; i < Query_Tokens.size(); i++) {
            Query_TF[i] = Collections.frequency(Query_Tokens, Query_Tokens.get(i));
        }
    }
    private void PrintQueryTF() {
        System.out.println("*************Query Term Document Count Matrices *************");
        int index = 0;
        for (int x : Query_TF) {
            System.out.print(Query_Tokens.get(index++) + "  ");

            System.out.print(x + " ");

            System.out.println();
        }
        System.out.println("*******************End Query Term Document Count Matrices***********************");
    }
    private void QueryTfWeight() {
        int tf;
        for (int i = 0; i < Query_TF.length; i++) {
            tf = Query_TF[i];

            if (tf > 0) {
                Query_TF_Weight[i] = Math.round((1 + Math.log10(tf)) * 100.0) / 100.0;
            } else {
                Query_TF_Weight[i] = 0;
            }
        }
    }
    private void PrintQueryTfWeight() {
        System.out.println("\n*************Query TF Weight*************");
        int index = 0;
        for (double x : Query_TF_Weight) {
            System.out.print(Query_Tokens.get(index++) + "  ");

            System.out.print(x + " ");

            System.out.println();
        }
        System.out.println("*******************End Query TF Weight***********************");
    }
    private void QueryDocFreq() {
        int termIndex;
        for (int i = 0; i < Query_Tokens.size(); i++) {

            if(termList.contains(Query_Tokens.get(i))) {
                // Get its index
                termIndex = termList.indexOf(Query_Tokens.get(i));
                // Get its doc freq
                Query_DF[i] = docLists.get(termIndex).size();
            }
        }
    }
    private void PrintQueryDocFreq() {
        System.out.println("\n\n************* Query Term Document Frequency *************");
        int index = 0;
        for (int x : Query_DF) {
            System.out.print(Query_Tokens.get(index++) + "  ");
            System.out.print(x + " ");


            System.out.println();
        }
        System.out.println("*******************End Query TermDocument Frequency***********************\n");
    }
    private void QueryIDF() {
        for (int i = 0; i < Query_DF.length; i++) {
            Query_IDF[i] = Math.round(Math.log10((double) docsLength / Query_DF[i]) * 100.0) / 100.0;
        }
    }
    private void PrintQueryIDF() {
        System.out.println("\n*************Query IDF*************");
        int index = 0;
        for (double x : Query_IDF) {
            System.out.print(Query_Tokens.get(index++) + "  ");

            System.out.print(x + " ");

            System.out.println();
        }
        System.out.println("*******************End Query IDF***********************");
    }
    private void QueryTfIdF() {
        double tfIdf;
        for (int i = 0; i < Query_TF.length; i++) {
            tfIdf = Math.round((Query_TF_Weight[i] * Query_IDF[i]) * 100.0) / 100.0;
            QueryTF_IDF[i] = tfIdf;
        }
    }
    private void PrintQueryTfIdf() {
        System.out.println("\n*************QueryTF_IDF*************");
        int index = 0;
        for (double x : QueryTF_IDF) {
            System.out.print(Query_Tokens.get(index++) + "  ");

            System.out.print(x + " ");

            System.out.println();
        }
        System.out.println("*******************End Query TF_IDF***********************");
    }
    private void calcQueryLength() {
        double allTfIdf = 0.0;

        for (double v : QueryTF_IDF) {

            allTfIdf += Math.pow(v, 2);
        }
        QueryLength = Math.round(Math.sqrt(allTfIdf) * 100.0) / 100.0;
    }
    private void PrintQueryLength() {
        System.out.println("Query Length is " + QueryLength);
    }
    private void QueryUnitVector() {
        for (int i = 0; i < QueryTF_IDF.length; i++) {
            Query_Unit_Vector[i] = Math.round(QueryTF_IDF[i] / QueryLength * 100.0) / 100.0;
        }
    }
    private void PrintQueryUnitVector() {
        System.out.println("\n*************Query Unit Vector*************");

        int index = 0;
        for (double x : Query_Unit_Vector) {
            System.out.print(Query_Tokens.get(index++) + "  ");
            System.out.print(x + " ");
            System.out.println();
        }
        System.out.println("*******************End Query Unit Vector***********************");
    }
    /*------------------------------------*/
    private void ProductOFUnitVectors() {
        int termIndex;
        double UV;
        int docIndex;

        for (int i = 0; i < docs.length; i++) {
            for (int j = 0; j < Query_Tokens.size(); j++) {
                if (termList.contains(Query_Tokens.get(j))) {
                    docIndex = docs[i] - 1;

                    // Get its index
                    termIndex = termList.indexOf(Query_Tokens.get(j));

                    // Get its unit vector
                    UV = Unit_Vector[termIndex][docIndex];

                    if (Query_Tokens.size() == 1) {
                        ProdUnitVectors[i] += UV * Query_Unit_Vector[0];
                    } else {
                        ProdUnitVectors[i] += UV * Query_Unit_Vector[j];
                    }
                }
            }
        }
    }

    private void PrintProduct() {
        for (double prodUnitVector : ProdUnitVectors) {
            System.out.println(prodUnitVector);
        }
    }
    private void Ranking() {
        //sorting logic
        double temp;
        int temp2;
        for (int i = 0; i < ProdUnitVectors.length; i++) {
            for (int j = i + 1; j < ProdUnitVectors.length; j++) {
                if (ProdUnitVectors[i] < ProdUnitVectors[j]) {
                    temp = ProdUnitVectors[i];
                    temp2 = docs[i];

                    ProdUnitVectors[i] = ProdUnitVectors[j];
                    docs[i] = docs[j];

                    ProdUnitVectors[j] = temp;
                    docs[j] = temp2;
                }
            }
            // Set the ranking result
            Ranking = ProdUnitVectors;
        }
    }

    private void PrintRanking() {
        int index = 0;
        String Rank = "" ;
        for (double v : ProdUnitVectors) {
            Rank = "On Doc " + docs[index];
            Rank += " Similarity is :  " + (int)(v * 100) + "%";
            System.out.println(Rank);
            StringRanking[index] = Rank;

            index++;
        }
    }
}