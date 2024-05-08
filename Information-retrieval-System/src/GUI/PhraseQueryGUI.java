package GUI;

import CodeLogic.DocId;
import CodeLogic.PhraseQuery;
import CodeLogic.VectorSpaceModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;

public class PhraseQueryGUI extends JFrame implements ActionListener {

    private boolean flag = true;
    //Components
    private JPanel panel = new JPanel ();
    private JPanel Result_Panel = new JPanel ();

    private JLabel headerLabel = new JLabel ("Phrase Query to be find");
    private JLabel phraseQueryLabel = new JLabel ("Enter the phrase query :  ");
    private JLabel foundLabel = new JLabel("");
    private JLabel documentNames = new JLabel("");
    private JLabel notFoundLabel = new JLabel("");

    private JTextArea phraseQuery = new JTextArea (1 , 3);

    //Buttons
    private JButton submitBtn = new JButton ("Submit");
    private JButton similarityBtn = new JButton ("Show Documents Similarity");
    private JButton backBtn = new JButton ("Back");
    private JButton resetBtn = new JButton ("Reset");

    /*--------------------*/
    public PhraseQueryGUI()
    {
        showFrame();
    }
    private void showFrame() {
        //Confirmation Dialog When Close
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                int x = JOptionPane.showConfirmDialog(null, "You will lose all data,Are You Sure To Close Program..?",
                        "Exit Program", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (x == JOptionPane.YES_OPTION) {
                    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        /*--------------------*/
        this.setSize(950,700);
        this.setTitle("Information Retrieval System");
        this.setLocation(200, 30);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        getContentPane().setBackground(Color.DARK_GRAY);

        panel.setLayout(null);
        panel.setBounds(0,0,950,200);
        panel.setBackground(Color.DARK_GRAY);
        this.add(panel);

        Result_Panel.setLayout(null);
        Result_Panel.setBounds(0,300,950,400);
        Result_Panel.setBackground(Color.DARK_GRAY);
        Result_Panel.setVisible(true);
        this.add(Result_Panel);

        /*-----------------------------------*/
        notFoundLabel.setBounds (365,350,300,30);
        notFoundLabel.setForeground(Color.red);
        notFoundLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD+ Font.ITALIC, 40));
        Result_Panel.add (notFoundLabel);

        foundLabel.setBounds (400,250,150,40);
        foundLabel.setForeground(Color.GREEN);
        foundLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD+ Font.ITALIC, 40));
        Result_Panel.add (foundLabel);

        documentNames.setBounds (30,300,1000,40);
        documentNames.setForeground(Color.CYAN);
        documentNames.setFont(new Font(Font.MONOSPACED, Font.BOLD+ Font.ITALIC, 25));
        Result_Panel.add (documentNames);

        /*---------------------*/
        headerLabel.setBounds (210,5,700,40);
        headerLabel.setForeground (Color.WHITE);
        headerLabel.setFont (new Font(Font.MONOSPACED,Font.BOLD,35));
        panel.add (headerLabel);

        phraseQueryLabel.setBounds (50,70,600,30);
        phraseQueryLabel.setForeground(Color.WHITE);
        phraseQueryLabel.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 20));
        panel.add (phraseQueryLabel);


        /*---------------*/
        //Text Field
        phraseQuery.setBounds (375,70,200,30);
        phraseQuery.setBackground (Color.lightGray);
        panel.add (phraseQuery);

        /*---------------*/

        submitBtn.setBounds(660, 150, 200, 40);
        submitBtn.setBackground(Color.GRAY);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 24));
        panel.add (submitBtn);

        backBtn.setBounds(100, 150, 200, 40);
        backBtn.setBackground(Color.GRAY);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 24));
        panel.add (backBtn);

        resetBtn.setBounds(375, 150, 200, 40);
        resetBtn.setBackground(Color.GRAY);
        resetBtn.setForeground(Color.red);
        resetBtn.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 24));
        panel.add (resetBtn);

        // Finish Btn
        similarityBtn.setBounds(180, 580, 600, 40);
        similarityBtn.setBackground(Color.GRAY);
        similarityBtn.setForeground(Color.CYAN);
        similarityBtn.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 30));
        similarityBtn.setVisible(false);
        Result_Panel.add (similarityBtn);

        //actions
        similarityBtn.addActionListener (this);
        backBtn.addActionListener (this);
        submitBtn.addActionListener(this);
        resetBtn.addActionListener(this);

        //set the Result_Panel visible false and show it when press submit
//        Result_Panel.setVisible(false);

        this.setVisible(true);
    }
    private void setResultNotFound()
    {
        notFoundLabel.setText("Not Found");
        flag = false;
    }

    private void setResultFound (String docsNames) {
        foundLabel.setText("Found");

        // Display the positions
        documentNames.setText("On Document(s):  " + docsNames);

        similarityBtn.setVisible(true);
        flag = false;
    }

    private void resetResultPanel () {
        foundLabel.setText("");
        notFoundLabel.setText("");
        phraseQuery.setText("");
        documentNames.setText("");
        similarityBtn.setVisible(false);
        flag = true;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource () == submitBtn) {

            if(!flag) {
                JOptionPane.showMessageDialog (null,"Please reset the previous result first...!","Failed",JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(!phraseQuery.getText ().equals (""))
            {
                /*-------------------*/
                //get Data
                PhraseQuery PH = new PhraseQuery();

                ArrayList<DocId> results;

                String phraseQueryText = phraseQuery.getText ();

                String[] tokens = phraseQueryText.split(" ");

                for (int counter = 0; counter < tokens.length; counter++) {
                    tokens[counter] = tokens[counter].trim().toLowerCase();
                }

                results = PH.phraseQuery(tokens);

                JOptionPane.showMessageDialog (null,"Great Job..,We almost Finish","Information",JOptionPane.INFORMATION_MESSAGE);

                if(results == null)
                {
                    setResultNotFound();
                }
                else
                {
                    // Check if the result is empty
                    if(results.size() == 0 ) {
                        setResultNotFound();
                        return;
                    }

                    int[] DocsIds =  new int[results.size()];

                    // Get the docs names and display it
                    String DocumentNumbers = "";
                    int index = 0;
                    for (DocId docId: results) {
                        DocsIds[index++] = docId.documentID + 1;

                        DocumentNumbers += " " + (docId.documentID + 1 ) + " , ";
                    }

                    /*-----Vector Space Model-------*/
                    ArrayList<String> queryTokens = new ArrayList<>();

                    Collections.addAll(queryTokens, tokens);

                    VectorSpaceModel vectorSpaceModel = new VectorSpaceModel(DocsIds, queryTokens);

                    /*-------End Vector Space Model-------*/

                    System.out.println("[" +DocumentNumbers + "]");
                    DocumentNumbers = DocumentNumbers.trim();
                    if (DocumentNumbers.endsWith(",")) {
                        DocumentNumbers = DocumentNumbers.substring(0, DocumentNumbers.length() - 1);
                    }

                    // Display Results
                    setResultFound(DocumentNumbers);
                }
                /*------------------*/
            }
            else {
                JOptionPane.showMessageDialog (null,"Invalid input or Missing required Fields ... !  Please, complete them before submit ...!","Failed",JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(e.getSource () == similarityBtn)
        {
            this.setVisible(false);
            Ranking ranking = new Ranking();
        }
        else if (e.getSource() == backBtn)
        {
            int x = JOptionPane.showConfirmDialog(null,"You will lose this data if you back ,Are You Sure To Back?",
                    "Alert", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(x == JOptionPane.YES_OPTION)
            {
                this.setVisible(false);
                PositionalIndexGUI positionalIndexGUI = new PositionalIndexGUI();
            }
        } else if (e.getSource() == resetBtn) {
            resetResultPanel();
        }
    }
}