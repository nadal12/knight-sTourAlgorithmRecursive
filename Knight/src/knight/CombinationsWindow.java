/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knight;

import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigInteger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Nadal
 */
public class CombinationsWindow extends JFrame {
    
    private JTextArea textArea;
    

    public CombinationsWindow() {
        super("Number of combinations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pane = new JPanel();
        pane.setLayout(new FlowLayout());
        this.setSize(400, 200);

        setLocationRelativeTo(null);
        textArea = new JTextArea();
        textArea.setFont(new Font("Serif", Font.ITALIC, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        pane.add(textArea);
        setContentPane(pane);

    }
    
    public void modifyValue(BigInteger i) {

        textArea.setText(String.valueOf(i));
    
    }  

}
