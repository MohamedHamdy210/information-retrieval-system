/**
 * First Frame
 * This is the first frame of the program
 */

package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class First_Frame extends JFrame implements ActionListener
{
    private JButton letsBegin = new JButton("Let's Begin");
    private JLabel headerLabel = new JLabel("Information Retrieval");
    private JLabel welcome_to_our_Project = new JLabel("Welcome To Our IR Project..");
    private JPanel panel = new JPanel();

    public First_Frame()
    {
        //Confirmation Dialog When Close the program
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent evt){
                int x = JOptionPane.showConfirmDialog(null,"Are You Sure to Close the Program..?",
                        "Exit Program", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if(x == JOptionPane.YES_OPTION)
                {
                    System.exit(0); // stop program
                    dispose(); // close window
                }else{
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        /*----------------*/

        this.setTitle("Information Retrieval"); ///jframe
        this.setSize(950, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocation(200, 30);
        this.setLayout(null);

        ////jpanel
        panel.setSize(950, 700);
        panel.setLayout(null);
        panel.setBackground(Color.darkGray);
        add(panel);

        ////jlabel
        headerLabel.setBounds(230, 80, 700, 50);
        headerLabel.setForeground (Color.WHITE);
        headerLabel.setFont (new Font(Font.MONOSPACED,Font.BOLD + Font.ITALIC,37));
        panel.add(headerLabel);

        ///JButton
        letsBegin.setBounds(320, 300, 270, 50);
        letsBegin.setBackground(Color.GRAY);
        letsBegin.setForeground(Color.GREEN);
        letsBegin.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 29));

        panel.add(letsBegin);

        ///label welcome
        welcome_to_our_Project.setBounds(240, 500, 700, 40);
        welcome_to_our_Project.setForeground(Color.white);
        welcome_to_our_Project.setFont(new Font(Font.MONOSPACED,Font.BOLD + Font.ITALIC, 28));
        panel.add(welcome_to_our_Project);

        ///add actions
        letsBegin.addActionListener(this);
        this.setVisible(true);
    }

    //actions

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == letsBegin) {
            int response = JOptionPane.showConfirmDialog(null,"Are You Ready..?",
                    "Funny Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if(response == JOptionPane.YES_OPTION)
            {
                this.setVisible(false);
                PositionalIndexGUI positionalIndex = new PositionalIndexGUI();
            }
            else
            {
                JOptionPane.showConfirmDialog(null,"Be Ready and Come again..!",
                        "Motivation Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }
}
