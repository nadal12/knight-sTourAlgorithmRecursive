package knight;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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

public class Knight extends JFrame implements MouseListener {

    //Variables globales.
    private final int DIMENSION = 8;

    //Array que indica las casillas bloqueadas o ocupadas. 
    //-TRUE = Ocupada. 
    //-FALSE = Libre.
    private boolean busySpots[] = new boolean[DIMENSION * DIMENSION];

    //Indica si esta activo el botón de bloquear casillas. 
    private boolean setBlockedBoxes = false;

    //Indica si esta activo el botón de escoger casilla del caballero. 
    private boolean setKnightStartBox = false;

    //Casilla donde se ubica el caballero.
    private int knightBox = -1;

    //Declaraciones de la interfaz gráfica. 
    private JButton jbleft;
    private JButton jbright;
    private JButton jblightBulb;
    private JButton jbblock;
    private JButton jbhelp;
    private JButton jbknight;
    private JLabel box;
    private JPanel board;
    private JPanel menu;

    public Knight() {

        initComponents();
        initControlMenu();
        initBoard();

    }

    public void initComponents() {

        //Obtener resolución de pantalla. 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        this.setSize(700, 600);
        this.setResizable(false);
        this.setTitle("Knight's tour");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        //Establecer icono de ventana. 
        ImageIcon wi = new ImageIcon("IMAGENES/knight.png");
        Image windowIcon = wi.getImage();
        this.setIconImage(windowIcon);

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

        //Inicialización de los botones.
        jbleft = new JButton();
        jbright = new JButton();
        jblightBulb = new JButton();
        jbhelp = new JButton();
        jbblock = new JButton();
        jbknight = new JButton();

        //Descativar casillas por defecto. (Porque aún el caballero no tiene
        //posición asignada). 
        jbleft.setEnabled(false);
        jbright.setEnabled(false);
        jblightBulb.setEnabled(false);

        //Ajustar el tamaño de las imágenes de los botones. 
        ImageIcon i1 = new ImageIcon(new ImageIcon("IMAGENES/left.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i2 = new ImageIcon(new ImageIcon("IMAGENES/right.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i3 = new ImageIcon(new ImageIcon("IMAGENES/light_bulb.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i4 = new ImageIcon(new ImageIcon("IMAGENES/help.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i5 = new ImageIcon(new ImageIcon("IMAGENES/block.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i6 = new ImageIcon(new ImageIcon("IMAGENES/knight.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));

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

        //Agregar escuchadores de eventos.
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

        //Añadir el menú a la izquierda. 
        this.add(menu, BorderLayout.WEST);

    }

    private void jbBlockActionPerformed(ActionEvent evt) {

        if (!setBlockedBoxes) {

            //Elegir imagen del cursor.
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage("IMAGENES/block.png");
            Cursor c = toolkit.createCustomCursor(image, new Point(menu.getX(), menu.getY()), "img");

            //Cambiar el cursor
            menu.setCursor(c);
            board.setCursor(c);

            //Habilitar casillas.
            jbleft.setEnabled(false);
            jbright.setEnabled(false);
            jblightBulb.setEnabled(false);
            jbknight.setEnabled(false);

            setBlockedBoxes = true;

        } else {

            //Poner el cursor por defecto.
            menu.setCursor(Cursor.getDefaultCursor());
            board.setCursor(Cursor.getDefaultCursor());

            //Habilitar las casillas.
            jbknight.setEnabled(true);

            setBlockedBoxes = false;
            
            if (knightBox != -1) {

                //Habilitar botones.
                jbleft.setEnabled(true);
                jbright.setEnabled(true);
                jblightBulb.setEnabled(true);

            }
        }

    }

    private void jbHelpActionPerformed(ActionEvent evt) {

        info();

    }

    private void jbKnightActionPerformed(ActionEvent evt) {

        if (!setKnightStartBox) {

            //Elegir imagen del cursor.
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage("IMAGENES/knight.png");
            Cursor c = toolkit.createCustomCursor(image, new Point(menu.getX(), menu.getY()), "img");

            //Cambiar cursor. 
            menu.setCursor(c);
            board.setCursor(c);

            //Habilitar casillas.
            jbleft.setEnabled(false);
            jbright.setEnabled(false);
            jblightBulb.setEnabled(false);
            jbblock.setEnabled(false);

            setKnightStartBox = true;

        } else {

            //Poner el cursor por defecto.
            menu.setCursor(Cursor.getDefaultCursor());
            board.setCursor(Cursor.getDefaultCursor());

            //Habilitar las casillas. 
            jbblock.setEnabled(true);

            if (knightBox != -1) {

                //Habilitar botones.
                jbleft.setEnabled(true);
                jbright.setEnabled(true);
                jblightBulb.setEnabled(true);

            }

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

        JOptionPane.showMessageDialog(null, "INSTRUCCIONES\n\n"
                + "1. Seleccione las casillas que quiere bloquear a través del botón con el símbolo de prohibido.\n"
                + "2. Seleccione una posición de inicio para el caballero con el botón del caballo.\n"
                + "3. Empiece a realizar movimientos con las flechas izquierda/derecha.\n\n"
                + "Nota I: Puede ver la solución directamente haciendo click en el botón de la bombilla.\n"
                + "Nota II: No podrá iniciar los movimientos hasta que no establezca una posición de salida para el caballero.\n\n", "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean isEmptyBox(JLabel jl) {

        return jl.getIcon() == null;

    }

    /**
     * Encuentra la posición del "label" pasado por parámetro dentro del
     * tablero.
     *
     * @param label "Label" que se desea buscar.
     * @return Posición donde se ubica. -1 si no ha sido encontrado.
     */
    public int findPosition(JLabel label) {

        int position = -1;

        for (int i = 0; i < board.getComponentCount(); i++) {

            if (board.getComponent(i).equals(label)) {

                position = i;

            }

        }

        return position;

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

        if (e.getButton() == MouseEvent.BUTTON1) {

            //Declaraciones
            JLabel label = (JLabel) e.getSource();
            ImageIcon block = new ImageIcon(new ImageIcon("IMAGENES/block.png").getImage().getScaledInstance(label.getWidth() - 20, label.getHeight() - 20, Image.SCALE_DEFAULT));
            ImageIcon knight = new ImageIcon(new ImageIcon("IMAGENES/knight.png").getImage().getScaledInstance(label.getWidth() - 20, label.getHeight() - 20, Image.SCALE_DEFAULT));

            if (setBlockedBoxes) {

                if (isEmptyBox(label)) {
                    label.setIcon(block);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    busySpots[findPosition(label)] = true;

                } else if (busySpots[findPosition(label)] == true) {
                    label.setIcon(null);
                    busySpots[findPosition(label)] = false;
                }
            }

            if (setKnightStartBox) {

                if ((isEmptyBox(label)) && (knightBox == -1)) {

                    label.setIcon(knight);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    knightBox = findPosition(label);

                } else if ((knightBox > -1) && (isEmptyBox(label))) {

                    JLabel auxLabel;
                    auxLabel = (JLabel) board.getComponent(knightBox);
                    auxLabel.setIcon(null);

                    label.setIcon(knight);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    knightBox = findPosition(label);
                } else if (knightBox == findPosition(label)) {

                    label.setIcon(null);
                    knightBox = -1;

                }
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
