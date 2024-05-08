package GUI;

import CodeLogic.DocId;
import CodeLogic.PositionalIndex;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class PositionalIndexGUI extends JFrame implements ActionListener {
    private JButton BackButton  =  new JButton("Back");
    private JButton NextButton  =  new JButton("Enter phrase");
    private JPanel panel = new JPanel();
    private JLabel headerLabel = new JLabel ("Positional Index");
    private JScrollPane scrollPane;

    //Take instance from Positional Index class
    private PositionalIndex PI;

    private ArrayList termList ;
    private ArrayList docLists ;

    public PositionalIndexGUI() {
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

        PI = PositionalIndex.getInstance();
        // Get the outputs of the PI
        ArrayList outputs = PI.getPositionalIndex();
        termList = (ArrayList) outputs.get(0);
        docLists = (ArrayList) outputs.get(1);

        showTable();
    }
    private void showTable(){

        String[] column = { "Term ID", "Term", "Document Frequency", "Positions"};

        JTable table = new JTable ();

        DefaultTableModel tableModel = new DefaultTableModel (column,0);
        setSize(1000,700);

        this.setLocation(200, 30);
        setTitle ("Positional Index");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        panel.setLayout(null);
        panel.setBackground(Color.DARK_GRAY);
        panel.setBounds(0,0,950,700);
        add(panel);

        headerLabel.setBounds (330,20,600,40);
        headerLabel.setForeground (Color.GREEN);
//        headerLabel.setFont (new Font("Arial Rounded MT Bold",Font.ITALIC,40));
//        headerLabel.setFont (new Font(Font.MONOSPACED,Font.ITALIC,40));
        headerLabel.setFont (new Font(Font.MONOSPACED,Font.BOLD,35));

        panel.add (headerLabel);

        BackButton.setBounds(50, 600, 150, 40);
        BackButton.setBackground(Color.GRAY);
        BackButton.setForeground(Color.GREEN);
        BackButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
        panel.add (BackButton);

        NextButton.setBounds(770, 600, 200, 40);
        NextButton.setBackground(Color.GRAY);
        NextButton.setForeground(Color.GREEN);
        NextButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
        panel.add (NextButton);

        Object [] rowData = new Object[5];

        ArrayList<DocId> docList;
        for(int count = 0; count < termList.size (); count++)
        {
            docList = (ArrayList<DocId>) docLists.get(count);

            rowData[0] = count + 1;
            rowData[1] = termList.get(count);
            rowData[2] = docList.size();

            String termPositions = "";
            for (DocId docId : docList) {

                termPositions += docId.getPositions()+ ";   ";
            }
            // remove the last ";"
            termPositions = termPositions.trim();
            if(termPositions.endsWith(";")){
                termPositions = termPositions.substring(0, termPositions.length() - 1);
            }

            rowData[3] = termPositions;
            tableModel.addRow (rowData);
        }

        table.setModel (tableModel);
        table.setBackground (Color.LIGHT_GRAY);
        this.setLocationRelativeTo (null);
        table.setBounds (0,80,990,500);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(35);
        table.getColumnModel().getColumn(3).setPreferredWidth(550);

        table.setPreferredScrollableViewportSize(Toolkit.getDefaultToolkit().getScreenSize());
        table.setFont(new Font("Courier",Font.ITALIC, 20));
        table.setRowHeight(30);
        panel.add (table);

        // Scroll pan
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 90, 970, 500);
        scrollPane.setViewportView(table);
        panel.add(scrollPane);

        // Actions
        BackButton.addActionListener (this);
        NextButton.addActionListener(this);
        setVisible (true);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == BackButton)
        {
            this.setVisible(false);
            First_Frame first_frame =  new First_Frame();
        } else if (actionEvent.getSource() == NextButton) {
            this.setVisible(false);
            PhraseQueryGUI phraseQueryGUI =  new PhraseQueryGUI();
        }
    }
}
