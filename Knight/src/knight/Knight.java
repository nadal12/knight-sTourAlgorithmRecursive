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
import javax.swing.Icon;
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
    boolean setBlockedBoxes = false;
    boolean setKnightStartBox = false;
    int knightBox = -1;

    //Declaraciones de la interfaz gráfica. 
    JButton jbleft;
    JButton jbright;
    JButton jblightBulb;
    JButton jbblock;
    JButton jbhelp;
    JButton jbknight;

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

        jbleft = new JButton();
        jbright = new JButton();
        jblightBulb = new JButton();
        jbhelp = new JButton();
        jbblock = new JButton();
        jbknight = new JButton();

        //Ajustar el tamaño de las imágenes de los botones. 
        ImageIcon i1 = new ImageIcon(new ImageIcon("left.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i2 = new ImageIcon(new ImageIcon("right.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i3 = new ImageIcon(new ImageIcon("light_bulb.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i4 = new ImageIcon(new ImageIcon("help.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i5 = new ImageIcon(new ImageIcon("block.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i6 = new ImageIcon(new ImageIcon("knight.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));

        //Configurar los botones.
        jbleft.setIcon(i1);
        jbleft.setToolTipText("Retroceder un paso");
        jbright.setIcon(i2);
        jbright.setToolTipText("Avanzar un paso");
        jblightBulb.setIcon(i3);
        jblightBulb.setToolTipText("Ver solución");
        jblightBulb.setCursor(Cursor.getDefaultCursor());
        jbhelp.setIcon(i4);
        jbhelp.setToolTipText("Ver ayuda");
        jbhelp.setCursor(Cursor.getDefaultCursor());
        jbblock.setIcon(i5);
        jbblock.setToolTipText("Bloquear casillas");
        jbknight.setIcon(i6);
        jbknight.setToolTipText("Establecer posición de inicio");

        jbhelp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbHelpActionPerformed(evt);
            }

        });

        jbblock.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbBlockActionPerformed(evt);
            }

        });

        jbknight.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbKnightActionPerformed(evt);
            }

        });

        //Añadir los botones al panel del menú.
        menu.add(jbhelp);
        menu.add(jblightBulb);
        menu.add(jbleft);
        menu.add(jbright);
        menu.add(jbblock);
        menu.add(jbknight);

        this.add(menu, BorderLayout.WEST);

    }

    private void jbBlockActionPerformed(ActionEvent evt) {

        if (!setBlockedBoxes) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage("block.png");
            Cursor c = toolkit.createCustomCursor(image, new Point(menu.getX(), menu.getY()), "img");
            menu.setCursor(c);
            board.setCursor(c);
            jbleft.setEnabled(false);
            jbright.setEnabled(false);
            jblightBulb.setEnabled(false);
            jbknight.setEnabled(false);
            setBlockedBoxes = true;

        } else {

            menu.setCursor(Cursor.getDefaultCursor());
            board.setCursor(Cursor.getDefaultCursor());
            jbleft.setEnabled(true);
            jbright.setEnabled(true);
            jblightBulb.setEnabled(true);
            jbknight.setEnabled(true);
            setBlockedBoxes = false;
        }

    }

    private void jbHelpActionPerformed(ActionEvent evt) {

        info();

    }

    private void jbKnightActionPerformed(ActionEvent evt) {

        if (!setKnightStartBox) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage("knight.png");
            Cursor c = toolkit.createCustomCursor(image, new Point(menu.getX(), menu.getY()), "img");
            menu.setCursor(c);
            board.setCursor(c);
            jbleft.setEnabled(false);
            jbright.setEnabled(false);
            jblightBulb.setEnabled(false);
            jbblock.setEnabled(false);
            setKnightStartBox = true;

        } else {

            menu.setCursor(Cursor.getDefaultCursor());
            board.setCursor(Cursor.getDefaultCursor());
            jbleft.setEnabled(true);
            jbright.setEnabled(true);
            jblightBulb.setEnabled(true);
            jbblock.setEnabled(true);
            setKnightStartBox = false;
        }

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
    
    public boolean emptyBox(JLabel jl) {
    
        return jl.getIcon()==null;
    
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

        //Declaraciones
        JLabel label = (JLabel) e.getSource();
        ImageIcon block = new ImageIcon(new ImageIcon("block.png").getImage().getScaledInstance(label.getWidth() - 20, label.getHeight() - 20, Image.SCALE_DEFAULT));
        ImageIcon knight = new ImageIcon(new ImageIcon("knight.png").getImage().getScaledInstance(label.getWidth() - 20, label.getHeight() - 20, Image.SCALE_DEFAULT));
        
        if (setBlockedBoxes) {
        
            if (emptyBox(label)) {
            
                label.setIcon(block);
                label.setHorizontalAlignment(JLabel.CENTER);

            } else {
                System.out.println("Llega");
                  label.setIcon(null);
             
            
            }
        
        }
        
        if (setKnightStartBox) {
        
            if ((emptyBox(label))&&(knightBox==-1)) {
            
                label.setIcon(knight);
                label.setHorizontalAlignment(JLabel.CENTER);

                
            } 
        
        }
        

      /*  if (setBlockedBoxes) {

            label = (JLabel) e.getSource();
            block = new ImageIcon(new ImageIcon("block.png").getImage().getScaledInstance(label.getWidth() - 20, label.getHeight() - 20, Image.SCALE_DEFAULT));

            if (label.getIcon() == null) {

                label.setHorizontalAlignment(JLabel.CENTER);
                label.setIcon(block);

                for (int i = 0; i < board.getComponentCount(); i++) {

                    if (board.getComponent(i).equals(label)) {

                        busySpots[i] = true;

                    }
                }
            } else {

                label.setIcon(null);

                for (int i = 0; i < board.getComponentCount(); i++) {

                    if (board.getComponent(i).equals(label)) {

                        busySpots[i] = false;

                    }
                }

            }

        }

        if (setKnightStartBox) {

            label = (JLabel) e.getSource();
            knight = new ImageIcon(new ImageIcon("knight.png").getImage().getScaledInstance(label.getWidth() - 20, label.getHeight() - 20, Image.SCALE_DEFAULT));

            if ((label.getIcon() == null)) {

                if (startBox != -1) {

                    for (int i = 0; i < board.getComponentCount(); i++) {

                        aux = (JLabel) board.getComponent(i);

                        if (aux.getIcon()==knight) {

                            aux.setIcon(null);
                            
                        }

                    }

                }
                
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setIcon(knight);

                for (int i = 0; i < board.getComponentCount(); i++) {

                    if (board.getComponent(i).equals(label)) {

                        startBox = i;

                    }
                }

            }

        }*/

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
