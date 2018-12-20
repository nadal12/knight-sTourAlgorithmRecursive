/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knight;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author nadal
 */
public class Knight extends JFrame implements MouseListener {

    //Variables globales.
    private final int DIMENSION = 8;
    private JLabel box;
    private JPanel board;
    private JPanel menu;
    private boolean busySpots[] = new boolean[DIMENSION * DIMENSION];
    boolean defaultCursor = true;

    //Declaraciones de la interfaz gráfica. 
    JButton left;
    JButton right;
    JButton lightBulb;
    JButton block;
    JButton help;

    public Knight() {

        initComponents();
        initControlMenu();
        initBoard();

    }

    public void initComponents() {

        this.setSize(700, 600);
        this.setResizable(false);
        this.setTitle("Knight's tour");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

    }

    public void initBoard() {

        //Se inicializa el tablero 
        board = new JPanel();

        //Se define un gridLayout.
        GridLayout gl = new GridLayout();
        gl.setRows(DIMENSION);
        gl.setColumns(DIMENSION);
        board.setLayout(gl);

        printBoard();

        //Se añade el panel a la ventana. 
        this.add(board);

    }

    public void initControlMenu() {

        //Declaraciones.
        int iconSize = 30;
        menu = new JPanel();

        GridLayout gl = new GridLayout();
        gl.setRows(3);
        gl.setColumns(2);
        menu.setLayout(gl);

        left = new JButton();
        right = new JButton();
        lightBulb = new JButton();
        help = new JButton();
        block = new JButton();

        //Ajustar el tamaño de las imágenes de los botones. 
        ImageIcon i1 = new ImageIcon(new ImageIcon("left.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i2 = new ImageIcon(new ImageIcon("right.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i3 = new ImageIcon(new ImageIcon("light_bulb.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i4 = new ImageIcon(new ImageIcon("help.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i5 = new ImageIcon(new ImageIcon("block.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));

        //Configurar los botones.
        left.setIcon(i1);
        left.setToolTipText("Retroceder un paso");
        right.setIcon(i2);
        right.setToolTipText("Avanzar un paso");
        lightBulb.setIcon(i3);
        lightBulb.setToolTipText("Ver solución");
        lightBulb.setCursor(Cursor.getDefaultCursor());
        help.setIcon(i4);
        help.setToolTipText("Ver ayuda");
        help.setCursor(Cursor.getDefaultCursor());
        block.setIcon(i5);
        block.setToolTipText("Bloquear casillas");
        
        help.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbHelpActionPerformed(evt);
            }

        });

        block.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbBlockActionPerformed(evt);
            }

        });

        //Añadir los botones al panel del menú.
        menu.add(help);
        menu.add(lightBulb);
        menu.add(left);
        menu.add(right);
        menu.add(block);

        this.add(menu, BorderLayout.WEST);

    }

    private void jbBlockActionPerformed(ActionEvent evt) {

        if (defaultCursor) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage("block.png");
            Cursor c = toolkit.createCustomCursor(image, new Point(menu.getX(), menu.getY()), "img");
            menu.setCursor(c);
            board.setCursor(c);
            left.setEnabled(false);
            right.setEnabled(false);
            defaultCursor = false;

        } else {

            menu.setCursor(Cursor.getDefaultCursor());
            board.setCursor(Cursor.getDefaultCursor());
            left.setEnabled(true);
            right.setEnabled(true);
            defaultCursor = true;
        }

    }
    
    private void jbHelpActionPerformed(ActionEvent evt) {
    
        info();
    
    }

    public static void main(String[] args) {

        //Heredar estilo de menús del sistema operativo donde se ejecuta. 
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Knight.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Se marca la ventana como objeto visible.    
        new Knight().setVisible(true);

    }

    //  FÓRMULA USADA: Posición = casilla(j)+Dimension*Fila(i)
    public void printBoard() {

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {

                box = new JLabel();
                box.setOpaque(true);
                box.addMouseListener(this);

                if (DIMENSION % 2 == 0) {

                    if ((((j + (DIMENSION * i)) % 2 == 0)) && (i % 2 == 0)) {

                        box.setBackground(Color.white);

                    }

                    if ((((j + (DIMENSION * i)) % 2 != 0)) && (i % 2 != 0)) {

                        box.setBackground(Color.white);

                    }

                    if ((((j + (DIMENSION * i)) % 2 != 0)) && (i % 2 == 0)) {

                        box.setBackground(Color.black);

                    }
                    if ((((j + (DIMENSION * i)) % 2 == 0)) && (i % 2 != 0)) {

                        box.setBackground(Color.black);

                    }
                } else {

                    if ((j + (DIMENSION * i)) % 2 == 0) {
                        box.setBackground(Color.white);
                    } else {
                        box.setBackground(Color.black);
                    }
                }
                board.add(box);
            }
        }
    }
    
     public void info() {        
         
        JOptionPane.showMessageDialog(null, "INSTRUCCIONES\n"
                + "A\n"
                + "B\n"
                + "C\n", "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        JLabel label = new JLabel();

        if (!defaultCursor) {

            label = (JLabel) e.getSource();

            if (label.getIcon() == null) {

                ImageIcon block = new ImageIcon(new ImageIcon("block.png").getImage().getScaledInstance(label.getWidth() - 20, label.getHeight() - 20, Image.SCALE_DEFAULT));
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setIcon(block);
                

            } else {

                label.setIcon(null);

            }

        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
