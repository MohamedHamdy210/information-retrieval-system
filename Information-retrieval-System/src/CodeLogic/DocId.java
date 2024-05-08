package CodeLogic;

import java.util.ArrayList;
/**
 * Document id class that contains the document id and the position list
 */
public class DocId {
    public int documentID;
     ArrayList<Integer> termPositionList;

    public DocId(int documentID) {
        this.documentID = documentID;
        termPositionList = new ArrayList<>();
    }

    DocId(int did, int position) {
        documentID = did;
        termPositionList = new ArrayList<>();
        termPositionList.add(position);
    }

    void insertPosition(int position) {
        termPositionList.add(position);
    }

    /**
     * Get all positions for the term
     * @return String contain all the positions of the term
     */
    public String getPositions () {
        StringBuilder positionsList = new StringBuilder("" + (documentID+1) + ":<");

        for (Integer pos : termPositionList)
            positionsList.append(pos).append(",");
        positionsList = new StringBuilder(positionsList.substring(0, positionsList.length() - 1) + ">");

        // Return the position list
        return positionsList.toString();
    }


    public String toString()
    {
        String docIdString = "Document " + (documentID + 1) + " start from position(s) :  < ";
        for (Integer pos : termPositionList) {
            docIdString += pos + " ,";
        }

        docIdString = docIdString.substring(0, docIdString.length() - 1) + ">\n";
        return docIdString;
    }

}