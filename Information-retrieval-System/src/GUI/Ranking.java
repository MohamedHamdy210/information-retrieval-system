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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Ranking extends JFrame implements ActionListener {

    //Components
    private JPanel panel = new JPanel ();

    private JLabel headerLabel = new JLabel ("Similarity");
//    private JLabel documentNames = new JLabel ();
private JLabel  documentNames= new JLabel("<html><yourTagHere><yourOtherTagHere>this is <br/> your text</yourOtherTagHere></yourTagHere></html>");



    //Buttons
    private JButton FinishBtn = new JButton ("Finish");
    private JButton BackBtn = new JButton ("Back");
    private JButton openFiles = new JButton("Open File");

    /*--------------------*/
    public Ranking()
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
        this.setVisible(true);
        getContentPane().setBackground(Color.DARK_GRAY);

        panel.setLayout(null);
        panel.setBounds(0,0,950,200);
        panel.setBackground(Color.DARK_GRAY);
        this.add(panel);

        /*---------------------*/
        headerLabel.setBounds (340,20,600,40);
        headerLabel.setForeground (Color.GREEN);
        headerLabel.setFont (new Font(Font.MONOSPACED,Font.BOLD,40));
        panel.add (headerLabel);

        // Back Btn
        BackBtn.setBounds(50, 600, 150, 40);
        BackBtn.setBackground(Color.GRAY);
        BackBtn.setForeground(Color.WHITE);
        BackBtn.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 24));
        panel.add (BackBtn);

        openFiles.setBounds(350, 600, 230, 40);
        openFiles.setBackground(Color.GRAY);
        openFiles.setForeground(Color.CYAN);
        openFiles.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 30));
        panel.add (openFiles);

        // Finish Btn
        FinishBtn.setBounds(750, 600, 150, 40);
        FinishBtn.setBackground(Color.GRAY);
        FinishBtn.setForeground(Color.WHITE);
        FinishBtn.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 24));
        panel.add (FinishBtn);

        // Buttons Actions
        FinishBtn.addActionListener (this);
        BackBtn.addActionListener (this);
        openFiles.addActionListener(this);

        /*----------------------------------*/
        showResult();
    }

    private void showResult() {
        // Prepare the ranks
        // Here i use html because JLabel, don't understand \n
        String[] Ranking = VectorSpaceModel.StringRanking;
        String Ranks = "<html>";
        for(String rank: Ranking) {
            Ranks += rank + " <br/> ";
        }
        Ranks += "</html>";

        // Set the ranks
        documentNames.setText(   Ranks );
        documentNames.setBounds (40,100,1000,300);
        documentNames.setForeground(Color.CYAN);
        documentNames.setFont(new Font(Font.MONOSPACED, Font.BOLD+ Font.ITALIC, 35));
        panel.add (documentNames);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource () == FinishBtn)
        {
            this.setVisible(false);
            First_Frame first_frame =  new First_Frame();

        } else if (e.getSource() == openFiles) {
            // Get all matched docs
            int[] matchedDocs =  VectorSpaceModel.docs;
            // Create options [files names]
            Object[] options = new Object[matchedDocs.length];
            int index = 0;
            for (int doc: matchedDocs) {
                options[index++] = doc + ".txt";
            }
            // Show tje dialog
            int x = JOptionPane.showOptionDialog(null, "Which file you wish to open ?",
                    "Open File",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            // Get the Choice and open the file
            if (x != -1) {
                String choice = (String) options[x];
                ProcessBuilder process = new ProcessBuilder("Notepad.exe", "Docs/" + choice);
                try {
                    process.start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else {
                System.out.println(":( no choice");
            }
        }
        else if (e.getSource() == BackBtn)
        {
            int x = JOptionPane.showConfirmDialog(null,"You will lose this data if you back ,Are You Sure To Back?",
                    "Alert", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(x == JOptionPane.YES_OPTION)
            {
                this.setVisible(false);
                PhraseQueryGUI phraseQueryGUI = new PhraseQueryGUI();
            }
        }
    }
}