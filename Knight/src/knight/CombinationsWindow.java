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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Nadal
 */
public class CombinationsWindow extends JFrame {

    private JTextArea textArea;
    private int sizeX = 400;
    private int sizeY = 200;

    public CombinationsWindow() {
        super("Calculando solución...");
        // setDefaultCloseOperation();
        dispose();
        this.setSize(sizeX, sizeY);
        setResizable(false);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        textArea = new JTextArea();
        textArea.setFont(new Font("Serif", Font.ITALIC, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        this.add(textArea);

    }

    public void modifyValue(BigInteger i, boolean validSolution) {

        if (validSolution) {
            textArea.setText("¡Se ha encontrado una solución!\n\n" + "Número de combinaciones probadas: " + String.valueOf(i));

        } else {

            textArea.setText("No se ha encontrado ningúna solución para esta configuración de tablero.\n\n" + "Número de combinaciones probadas: " + String.valueOf(i));

        }
    }

}
