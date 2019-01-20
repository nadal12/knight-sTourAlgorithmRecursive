package knight;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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

public class Knight extends JFrame implements MouseListener {

    //Variables globales.
    private static final int DIMENSION = 5;

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

    //Indica si el estado actual es Play o Pause
    private boolean play = false;

    //Índice global para moverse con las flechas. 
    private int globalIndex = 0;

    //Array donde se almacena la solución del algoritmo. 
    private int[] sol = new int[DIMENSION * DIMENSION];

    //Número de casillas bloqueadas. 
    private int numBSpots = 0;

    //Declaraciones de la interfaz gráfica. 
    private JButton jbLeft;
    private JButton jbRight;
    private JButton jbLightBulb;
    private JButton jbBlock;
    private JButton jbHelp;
    private JButton jbKnight;
    private JButton jbPlayPause;
    private JButton jbReset;
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
        gl.setRows(4);
        gl.setColumns(2);
        menu.setLayout(gl);

        //Inicialización de los botones.
        jbPlayPause = new JButton();
        jbReset = new JButton();
        jbLeft = new JButton();
        jbRight = new JButton();
        jbLightBulb = new JButton();
        jbHelp = new JButton();
        jbBlock = new JButton();
        jbKnight = new JButton();

        //Descativar casillas por defecto. (Porque aún el caballero no tiene
        //posición asignada). 
        jbLeft.setEnabled(false);
        jbRight.setEnabled(false);
        jbLightBulb.setEnabled(false);
        jbPlayPause.setEnabled(false);
        jbReset.setEnabled(true);

        //Ajustar el tamaño de las imágenes de los botones. 
        ImageIcon i1 = new ImageIcon(new ImageIcon("IMAGENES/left.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i2 = new ImageIcon(new ImageIcon("IMAGENES/right.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i3 = new ImageIcon(new ImageIcon("IMAGENES/light_bulb.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i4 = new ImageIcon(new ImageIcon("IMAGENES/help.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i5 = new ImageIcon(new ImageIcon("IMAGENES/block.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i6 = new ImageIcon(new ImageIcon("IMAGENES/knight.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i7 = new ImageIcon(new ImageIcon("IMAGENES/play.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i8 = new ImageIcon(new ImageIcon("IMAGENES/pause.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon i9 = new ImageIcon(new ImageIcon("IMAGENES/reset.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));

        //Configurar los botones.
        jbLeft.setIcon(i1);
        jbLeft.setToolTipText("Retroceder un paso");
        jbRight.setIcon(i2);
        jbRight.setToolTipText("Avanzar un paso");
        jbLightBulb.setIcon(i3);
        jbLightBulb.setToolTipText("Ver solución");
        jbLightBulb.setCursor(Cursor.getDefaultCursor());
        jbHelp.setIcon(i4);
        jbHelp.setToolTipText("Ver ayuda");
        jbHelp.setCursor(Cursor.getDefaultCursor());
        jbBlock.setIcon(i5);
        jbBlock.setToolTipText("Bloquear casillas");
        jbKnight.setIcon(i6);
        jbKnight.setToolTipText("Establecer posición de inicio");
        jbPlayPause.setIcon(i7);
        jbPlayPause.setToolTipText("Play/Pause");
        jbReset.setIcon(i9);
        jbReset.setToolTipText("Resetear");

        //Agregar escuchadores de eventos.
        jbPlayPause.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbPlayPauseActionPerformed(evt);
            }

        });

        jbRight.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbRightActionPerformed(evt);
            }

        });

        jbLeft.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbLeftActionPerformed(evt);
            }

        });
        jbReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbResetActionPerformed(evt);
            }

        });

        jbLightBulb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbLightBulbActionPerformed(evt);
            }

        });

        jbHelp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbHelpActionPerformed(evt);
            }

        });

        jbBlock.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbBlockActionPerformed(evt);
            }

        });

        jbKnight.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jbKnightActionPerformed(evt);
            }

        });

        //Añadir los botones al panel del menú.
        menu.add(jbPlayPause);
        menu.add(jbReset);
        menu.add(jbLeft);
        menu.add(jbRight);
        menu.add(jbHelp);
        menu.add(jbLightBulb);
        menu.add(jbBlock);
        menu.add(jbKnight);

        //Añadir el menú a la izquierda. 
        this.add(menu, BorderLayout.WEST);

    }

    private void jbPlayPauseActionPerformed(ActionEvent evt) {

        while (globalIndex < sol.length - numBSpots) {
            moveRight();
            board.paintComponents(board.getGraphics());
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Knight.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void jbRightActionPerformed(ActionEvent evt) {

        moveRight();

    }

    private void jbLeftActionPerformed(ActionEvent evt) {

        moveLeft();

    }

    private void jbResetActionPerformed(ActionEvent evt) {

        reset();

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
            jbLightBulb.setEnabled(false);
            jbKnight.setEnabled(false);

            setBlockedBoxes = true;

        } else {

            //Poner el cursor por defecto.
            menu.setCursor(Cursor.getDefaultCursor());
            board.setCursor(Cursor.getDefaultCursor());

            //Habilitar las casillas.
            jbKnight.setEnabled(true);

            setBlockedBoxes = false;

            if (knightBox != -1) {

                //Habilitar botones.
                jbLightBulb.setEnabled(true);

            }
        }

    }

    private synchronized void jbLightBulbActionPerformed(ActionEvent evt) {

        boolean validSolution = true;

        //Contar el número de casillas bloqueadas. 
        for (int i = 0; i < busySpots.length; i++) {
            if (busySpots[i]) {
                numBSpots++;
            }
        }

        //Desactivar botón. 
        jbLightBulb.setEnabled(false);

        Algorithm.initCombinationsWindow();
//        Thread t = new Thread(new Algorithm(board, busySpots, knightBox));
//        t.start();
        try {
            sol = Algorithm.KnightsTour(board, busySpots, knightBox);
        } catch (Exception ex) {
            validSolution = false;
            Logger.getLogger(Knight.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < sol.length; i++) {
            System.out.print(sol[i] + ", ");
        }

        //Habilitar flechas para que el usuario pueda mover el caballero.
        if (validSolution) {
            jbPlayPause.setEnabled(true);
            jbLeft.setEnabled(true);
            jbRight.setEnabled(true);
        }

        jbBlock.setEnabled(false);
        jbKnight.setEnabled(false);
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
            jbLeft.setEnabled(false);
            jbRight.setEnabled(false);
            jbLightBulb.setEnabled(false);
            jbBlock.setEnabled(false);

            setKnightStartBox = true;

        } else {

            //Poner el cursor por defecto.
            menu.setCursor(Cursor.getDefaultCursor());
            board.setCursor(Cursor.getDefaultCursor());

            //Habilitar las casillas. 
            jbBlock.setEnabled(true);

            if (knightBox != -1) {

                //Habilitar botones.
                jbLightBulb.setEnabled(true);

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

        Knight k = new Knight();
        k.setVisible(true);
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
                + "3. Calcule una posible solución con el botón de la bombilla.\n\n"
                + "Nota: No podrá iniciar los movimientos hasta que no establezca una posición de salida para el caballero y se calcule una solución válida.\n\n", "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
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

    public void moveRight() {

        if (globalIndex < sol.length - numBSpots) {

            JLabel aux;
            aux = (JLabel) board.getComponent(sol[globalIndex]);
            aux.setIcon(null);
            aux.setText(String.valueOf(globalIndex));
            aux.setHorizontalAlignment(JLabel.CENTER);
            aux.setFont(new Font("Serif", Font.ITALIC, 30));
            aux.setForeground(Color.red);

            ImageIcon knight = new ImageIcon(new ImageIcon("IMAGENES/knight.png").getImage().getScaledInstance(aux.getWidth() - 20, aux.getHeight() - 20, Image.SCALE_DEFAULT));
            globalIndex++;
            aux = (JLabel) board.getComponent(sol[globalIndex]);
            aux.setIcon(knight);
            aux.setHorizontalAlignment(JLabel.CENTER);

        }
    }

    public void moveLeft() {

        if (globalIndex > 0) {

            JLabel aux = (JLabel) board.getComponent(sol[globalIndex]);
            aux.setIcon(null);
            ImageIcon knight = new ImageIcon(new ImageIcon("IMAGENES/knight.png").getImage().getScaledInstance(aux.getWidth() - 20, aux.getHeight() - 20, Image.SCALE_DEFAULT));
            globalIndex--;
            aux = (JLabel) board.getComponent(sol[globalIndex]);
            aux.setText(null);
            aux.setIcon(knight);
            aux.setHorizontalAlignment(JLabel.CENTER);

        }

    }

    public void reset() {

        //Resetear array de casillas ocupadas. 
        for (int i = 0; i < busySpots.length; i++) {

            busySpots[i] = false;

        }
        //Reset número de casillas bloqueadas. 
        numBSpots = 0;

        //Quitar el caballero del tablero. 
        knightBox = -1;

        //Resetear globalIndex. 
        globalIndex = 0;

        //Activar/Desactivar botones correspondientes. 
        jbLeft.setEnabled(false);
        jbRight.setEnabled(false);
        jbLightBulb.setEnabled(false);
        jbPlayPause.setEnabled(false);
        jbBlock.setEnabled(true);
        jbKnight.setEnabled(true);

        JLabel aux;

        //Quitar los iconos y números del tablero. 
        for (int i = 0; i < board.getComponentCount(); i++) {

            aux = (JLabel) board.getComponent(i);
            aux.setIcon(null);
            aux.setText(null);

        }

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
                    busySpots[knightBox] = true;

                } else if ((knightBox > -1) && (isEmptyBox(label))) {

                    JLabel auxLabel;
                    auxLabel = (JLabel) board.getComponent(knightBox);
                    auxLabel.setIcon(null);
                    busySpots[knightBox] = false;

                    label.setIcon(knight);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    knightBox = findPosition(label);
                    busySpots[knightBox] = true;

                } else if (knightBox == findPosition(label)) {

                    busySpots[knightBox] = false;
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
