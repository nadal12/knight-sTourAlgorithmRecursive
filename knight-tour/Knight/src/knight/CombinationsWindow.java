/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knight;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigInteger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author Nadal
 */
public class CombinationsWindow extends JFrame {

    private JTextArea textArea;
    private final int sizeX = 400;
    private final int sizeY = 200;

    /**
     * Constructor de Ventana de combinaciones.
     * @param parent 
     */
    public CombinationsWindow(JFrame parent)  {
        super("Calculando solución...");
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
        
        /*Añadir windowListener para habilitar ventana del programa principal
        al cerrar la ventana emergente*/
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                parent.setEnabled(true);
            }
        });
        
        //Establecer icono de ventana. 
        ImageIcon wi = new ImageIcon("IMAGENES/knight.png");
        Image windowIcon = wi.getImage();
        this.setIconImage(windowIcon);

    }

    /**
     * Metodo que modifica el texto en la ventana para notificar sobre el 
     * resultado del algoritmo KnightsTour
     * @param i
     * @param validSolution 
     */
    public void modifyValue(BigInteger i, boolean validSolution) {
        
        //Si la solución presentada es válida, muestra el mensaje
        //correspondiente con el número de combinaciones probadas
        if (validSolution) {
            textArea.setText("¡Se ha encontrado una solución!\n\n" + 
                    "Número de combinaciones probadas: " + String.valueOf(i));
        } else {
        //En caso contrario, muestra el mensaje correspondiente
            textArea.setText("No se ha encontrado ningúna solución para esta "+
                    "configuración de tablero.\n\nNúmero de combinaciones "+
                    "probadas: " + String.valueOf(i));
        }
    }

}
