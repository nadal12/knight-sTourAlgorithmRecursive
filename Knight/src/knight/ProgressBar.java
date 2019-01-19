/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knight;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author Nadal
 */
public class ProgressBar extends JFrame {

    JProgressBar progress;

    public ProgressBar(int min, int max) {

        super("Progress");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pane = new JPanel();
        pane.setLayout(new FlowLayout());
        progress = new JProgressBar(min, max);
        progress.setValue(0);
        progress.setStringPainted(true);
        pane.add(progress);
        setContentPane(pane);

    }
    
    public void barValue(int i) {
    
        progress.setValue(i);
    
    }

}
