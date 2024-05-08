package CodeLogic;

import java.util.*;
public class PhraseQuery {

    private int distance = 1;
    /**
     *
     * @param query a phrase query that consists of any number of terms in the sequential order
     * @return ids of documents that contain the phrase
     */
    public ArrayList<DocId> phraseQuery(String[] query) {
        // Take instance from  Positional Index Singletone Class

        PositionalIndex PI = PositionalIndex.getInstance();
        ArrayList termList = PI.getTermList();
        ArrayList docLists = PI.getDocLists();

        ArrayList<DocId> docList1;
        ArrayList<DocId> docList2;
        ArrayList<DocId> docList;
        //ArrayList<ArrayList<DocId>> result = new ArrayList<ArrayList<DocId>>();

        if(query.length == 0)
            return null;
        else if(query.length == 1)
        {
            if (termList.contains(query[0])) {
                // get the document list for this term
                docList1 = (ArrayList<DocId>) docLists.get(termList.indexOf(query[0]));
                return docList1;
            }
            return null;
        }
        else {
            System.out.println(query[0]);
            System.out.println(termList.indexOf(query[0]));

            System.out.println(query.length);
            // Computer Science
            ArrayList<DocId> result;
            if (termList.contains(query[0])) {
                if (termList.indexOf(query[0]) != -1) {
                    // get the document list for this term
                    docList1 = (ArrayList<DocId>) docLists.get(termList.indexOf(query[0])); // computer
                }
                else {
                    return null;
                }
            }

            else
                docList1 = null;

            if (termList.contains(query[1])) // true
               if (termList.indexOf(query[1]) != -1) {
                   docList2 = (ArrayList<DocId>) docLists.get(termList.indexOf(query[1])); // Science
               } else {
                   return null;
               }
            else
                docList2 = null;

            if (docList1 == null || docList2 == null) {
                return null;
            }
             // Check if there is in sequential order
            result = intersect(docList1, docList2);

            distance++; // Init  = 1

            // start from 2 -- because we already taken the first two.
            for (int i = 2; i < query.length; i++) {
                if (termList.contains(query[i]))
                    docList = (ArrayList<DocId>) docLists.get(termList.indexOf(query[i]));
                else
                    docList = null;
                result = intersect(result, docList);
                distance++;
            }

            return result;
        }
    }

    /**
     * Intersect two posting lists
     * @param postingListOne first postings
     * @param postingListTwo second postings
     * @return merged result of two postings
     */
    public ArrayList<DocId> intersect(ArrayList<DocId> postingListOne, ArrayList<DocId> postingListTwo)
    {
        if(postingListOne == null)
            return postingListTwo;

        else if (postingListTwo == null)
            return postingListOne;
        
        ArrayList<DocId> mergedList = new ArrayList<>();

        int pointer_1 = 0 ,pointer_2 = 0;

        while(pointer_1 < postingListOne.size() && pointer_2 < postingListTwo.size())
        {
            // If there is match between the document id (The two terms in the same document)
            if(postingListOne.get(pointer_1).documentID == postingListTwo.get(pointer_2).documentID)
            {
                // Get the positions list for each term
                ArrayList<Integer> termOnePositionList = postingListOne.get(pointer_1).termPositionList;
                ArrayList<Integer> termTwoPositionList = postingListTwo.get(pointer_2).termPositionList;

                int positionPointer_1 = 0, positionPointer_2;

                boolean isMatch = false;

                // Loop on all positions of the term
                while(positionPointer_1 < termOnePositionList.size())
                {
                    positionPointer_2 = 0;
                    // Loop on all positions of the term
                    while(positionPointer_2 < termTwoPositionList.size())
                    {
                        // Check if the difference between the two positions is one
                        if(termTwoPositionList.get(positionPointer_2) - termOnePositionList.get(positionPointer_1) ==  distance)
                        {
                            if(!isMatch)
                            {
                                // If matching document has not been added originally
                                if(termOnePositionList.get(positionPointer_1) > termTwoPositionList.get(positionPointer_2))
                                {
                                    DocId docList = new DocId(postingListOne.get(pointer_1).documentID, termTwoPositionList.get(positionPointer_2));
                                    mergedList.add(docList);
                                }
                                else
                                {
                                    // matching document has been added originally
                                    DocId docList = new DocId(postingListOne.get(pointer_1).documentID, termOnePositionList.get(positionPointer_1));
                                    mergedList.add(docList);
                                }
                                isMatch = true;
                            }
                            else
                            {
                                int k = 0;
                                for(DocId docList:mergedList)
                                {
                                    if(postingListOne.get(pointer_1).documentID == docList.documentID)
                                    {
                                        if(positionPointer_1 > positionPointer_2) {
                                            mergedList.get(k).insertPosition(positionPointer_2);
                                        }
                                        else
                                        {
                                            mergedList.get(k).insertPosition(positionPointer_1);
                                        }
                                    }
                                    k++;
                                }
                            }
                        }
                        //else if(termTwoPositionList.get(positionPointer_2) > termOnePositionList.get(positionPointer_1))
                        //      break;
                        positionPointer_2++;
                    }
                    positionPointer_1++;
                }
                pointer_1++;
                pointer_2++;
            }
            else if(postingListOne.get(pointer_1).documentID > postingListTwo.get(pointer_2).documentID)
                pointer_2++;
            else
                pointer_1++;
        }
        return mergedList;
    }

}
