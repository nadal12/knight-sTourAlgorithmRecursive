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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Knight extends JFrame implements MouseListener {

    //Variable global para inicializar la ventana
    static Knight k;

    //Variables globales.
    private static int DIMENSION = 8;

    //Array que indica las casillas bloqueadas o ocupadas. 
    //-TRUE = Ocupada. 
    //-FALSE = Libre.
    private boolean busySpots[];

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
    private int[] sol;

    //Número de casillas bloqueadas. 
    private int numBSpots = 0;

    //Tamaño del cursor 
    int cursorWidth = 32;
    int cursorHeight = 32;
        
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
    private JMenuBar jmBar;
    private JMenu jMenu;
    private JMenu jmHelp;
    private JMenuItem jmChSize;
    private JMenuItem jmiInstrucciones;
    private JMenuItem jmiExit;
    private JMenuItem jmiAcercaDe;

    /**
     * Metodo principal donde se ejecuta el programa. Ajusta el lookAndFeel del
     * programa y realiza un llamado al metodo Start().
     *
     * @param args
     */
    public static void main(String[] args) {

        //Heredar estilo de menús del sistema operativo donde se ejecuta. 
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Knight.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Se marca la ventana como objeto visible.    
        Knight.start();
    }

    /**
     * Metodo donde se inicializa la ventana del programa, notifica al usuario
     * de la actual configuración del tablero de DIMENSION x DIMENSION, y
     * consulta al usuario si desea cambiarlo para crear una nueva instancia de
     * nuevas dimensiones escogidas por el usuario.
     */
    public static void start() {
        k = new Knight(DIMENSION);
        k.setVisible(true);
        int newDim = k.changeSizePane();

        if (newDim != DIMENSION) {
            DIMENSION = newDim;
            k.dispose();
            k = null;
            k = new Knight(newDim);
            k.setVisible(true);

        }
    }

    /**
     * Constructor de la ventana que contiene el programa. Llama las funciones
     * necesarias para inicializar una ventana con menú, y un tablero tipo
     * ajedrez de DIMENSION x DIMENSION
     *
     * @param DIMENSION Dimensión que tendrá el tablero.
     */
    public Knight(int DIMENSION) {

        initComponents();
        initControlMenu();
        initBoard(DIMENSION);

    }

    /**
     * Metodo que inicializa todos los componentes de la ventana que no tienen
     * que ver con botones del menú izquierdo ni el tablero. Se da dimensiones a
     * la ventana y se inicializa el menú superior
     */
    public void initComponents() {

        //Inicialización de los arrays implementados en el programa.
        busySpots = new boolean[DIMENSION * DIMENSION];
        sol = new int[DIMENSION * DIMENSION];

        this.setSize(700, 600);
        this.setResizable(false);
        this.setTitle("Knight's tour");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        //Establecer icono de ventana. 
        ImageIcon wi = new ImageIcon("IMAGENES/knight.png");
        Image windowIcon = wi.getImage();
        this.setIconImage(windowIcon);

        //INICIALIZACIÓN DE LA BARRA DE MENU
        jmBar = new JMenuBar();
        jMenu = new JMenu("Tablero");
        jmHelp = new JMenu("Ayuda");
        jmBar.add(jMenu);
        jmBar.add(jmHelp);

        jmChSize = new JMenuItem("Cambiar dimensiones");
        jmChSize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                ActionEvent.CTRL_MASK));
        jmChSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int newDim = k.changeSizePane();
                if (newDim != DIMENSION) {
                    DIMENSION = newDim;

                    k.dispose();
                    k = null;
                    k = new Knight(newDim);
                    k.setVisible(true);
                }
            }
        });
        jMenu.add(jmChSize);

        jmiInstrucciones = new JMenuItem("Instrucciones");
        jmiInstrucciones.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
                ActionEvent.CTRL_MASK));
        jmiInstrucciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                info();
            }
        });

        jmiExit = new JMenuItem("Salir");
        jmiExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
                ActionEvent.ALT_MASK));
        jmiExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        
        jmiAcercaDe = new JMenuItem("Acerca de Knight Tour");
        jmiAcercaDe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,
                ActionEvent.CTRL_MASK));
        jmiAcercaDe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                about();
                
            }
        });

        jmHelp.add(jmiInstrucciones);
        jmHelp.add(jmiAcercaDe);
        jMenu.addSeparator();
        jMenu.add(jmiExit);
        this.setJMenuBar(jmBar);

    }

    /**
     * Metodo que inicializa e imprime en pantalla el tablero del programa.
     * Ajusta el tablero con un número de filas y columnas igual a DIMENSION
     *
     * @param DIMENSION numero de filas y columnas del tablero.
     */
    public void initBoard(int DIMENSION) {

        //Se inicializa el tablero 
        board = new JPanel();

        //Se define un gridLayout.
        GridLayout gl = new GridLayout();
        gl.setRows(DIMENSION);
        gl.setColumns(DIMENSION);
        board.setLayout(gl);

        printBoard(DIMENSION);

        //Se añade el panel a la ventana. 
        this.add(board);

    }

    /**
     * Metodo que inicializa el menú de control del programa donde se alojan los
     * botones: Play/Pause, reset, move back, move forward, info, generar
     * solución, bloquear casillas, colocar caballo.
     */
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
        ImageIcon i9 = new ImageIcon(new ImageIcon("IMAGENES/reset.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));

        //Configurar los botones.
        jbLeft.setIcon(i1);
        jbLeft.setToolTipText("Retroceder un paso");
        jbRight.setIcon(i2);
        jbRight.setToolTipText("Avanzar un paso");
        jbLightBulb.setIcon(i3);
        jbLightBulb.setToolTipText("Calcular solución");
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

    /**
     * Metodo que ejecuta la acción correspondiente al botón play/pause. Genera
     * un nuevo hilo para realizar el recorrido al array solución.
     *
     * @param evt evento de presionar botón.
     */
    private void jbPlayPauseActionPerformed(ActionEvent evt) {
        int iconSize = 30;
        ImageIcon iplay = new ImageIcon(new ImageIcon("IMAGENES/play.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
        ImageIcon ipause = new ImageIcon(new ImageIcon("IMAGENES/pause.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));

        play = !play; //Cambiar el estado de la variable.

        if (!play) {
            /*Cuando el estado de play == false, no se está ejecutando el 
            recorrido al tablero. Poner ícono de play en botón.*/
            jbPlayPause.setIcon(iplay);

            //Habilitar botones al presionar pausa
            jbLeft.setEnabled(true);
            jbRight.setEnabled(true);
            jbHelp.setEnabled(true);
            jbReset.setEnabled(true);
            return;
        }

        /*Cuando play == true, se está ejecutando el recorrido al tablero.
        Poner ícono de pause en botón.*/
        jbPlayPause.setIcon(ipause);

        //Deshabilitar botones mientras se realiza recorrido
        jbLeft.setEnabled(false);
        jbRight.setEnabled(false);
        jbLightBulb.setEnabled(false);
        jbBlock.setEnabled(false);
        jbHelp.setEnabled(false);
        jbKnight.setEnabled(false);
        jbReset.setEnabled(false);


        /*Ejecutar bucle en otro hilo para evitar que la ventana se bloquee
        al ejecutar el recorrido al tablero*/
        Thread t = new Thread() {
            @Override
            public void run() {  // override the run() for the running behaviors
                while (globalIndex < sol.length - numBSpots) {
                    if (!play) {
                        break; //Si se presiona pausa, terminar bucle.
                    }
                    moveForward();
                    board.paintComponents(board.getGraphics()); //Repintado

                    //Dormir por 200ms para ralentizar recorrido al tablero
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Knight.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //Cambiar de vuelta a icono de play al finalizar recorrido
                int iconSize = 30;
                ImageIcon iplay = new ImageIcon(new ImageIcon("IMAGENES/play.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT));
                jbPlayPause.setIcon(iplay);

                //Habilitar botones al finalizar recorrido
                jbHelp.setEnabled(true);
                jbReset.setEnabled(true);
                jbLeft.setEnabled(true);
                jbRight.setEnabled(true);
            }
        };
        t.start();  // call back run()

    }

    /**
     * Metodo que realiza la acción correspondiente al botón mover hacia
     * adelante. Realiza una llamada a moveForward()
     *
     * @param evt
     */
    private void jbRightActionPerformed(ActionEvent evt) {
        moveForward();
    }

    /**
     * Metodo que realiza la acción correspondiente al botón mover hacia atrás.
     * Realiza una llamada a moveBack()
     *
     * @param evt
     */
    private void jbLeftActionPerformed(ActionEvent evt) {
        moveBack();
    }

    /**
     * Metodo que realiza la acción correspondiente al botón reset. Realiza una
     * llamada a reset()
     *
     * @param evt
     */
    private void jbResetActionPerformed(ActionEvent evt) {
        reset();
    }

    /**
     * Metodo que realiza la acción correspondiente al botón bloquear casilla.
     * Modifica el cursor del programa para mostrar un ícono de bloqueo.
     *
     * @param evt
     */
    private void jbBlockActionPerformed(ActionEvent evt) {

        if (!setBlockedBoxes) {
            
            //Elegir imagen del cursor.
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image i;
         
            //Obtener dimensiones relativas a la pantalla para el cursor
            Dimension scrSize = toolkit.getScreenSize();
            cursorHeight = (int) scrSize.getHeight()/15;
            cursorWidth = (int) scrSize.getWidth()/15;
            cursorWidth *= scrSize.getHeight()/scrSize.getWidth();
            
            ImageIcon image = new ImageIcon(new ImageIcon("IMAGENES/block.png").getImage().getScaledInstance(cursorWidth, cursorHeight, Image.SCALE_DEFAULT));
            i = image.getImage();
            
            Cursor c = toolkit.createCustomCursor(i, new Point(menu.getX(), menu.getY()), "img");
            
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

            if (knightBox != -1) {

                jbLightBulb.setEnabled(true);

            }

            setBlockedBoxes = false;
        }

    }

    /**
     * Metodo que realiza la acción correspondiente al botón generar solución.
     * Inicializa las variables necesarias para realizar la llamada al algoritmo
     * que genera una solución y modifica el estado del programa permitiendo al
     * usuario utilizar los botones play/pause, mover hacia atrás, mover hacia
     * adelante si el algoritmo ha generado una solución. En caso contra- rio,
     * no hace nada.
     *
     * @param evt
     */
    private void jbLightBulbActionPerformed(ActionEvent evt) {

        boolean validSolution = true;

        //Contar el número de casillas bloqueadas. 
        for (int i = 0; i < busySpots.length; i++) {
            if (busySpots[i]) {
                numBSpots++;
            }
        }

        //Desactivar botón. 
        jbLightBulb.setEnabled(false);

        //Iniciar ventana que visualiza el resultado del algoritmo
        Algorithm.initCombinationsWindow(this);
        //Bloquear this para forzar cierre de combinationsWindow
        this.setEnabled(false);

        //Ejecución del algoritmo basado en backtracking
        sol = Algorithm.KnightsTour(board, busySpots, knightBox);

        //sol[0] == -1 -> no se ha conseguido solución
        if (sol[0] != -1) {
            //Habilitar flechas para que el usuario pueda mover el caballero.
            if (validSolution) {
                jbPlayPause.setEnabled(true);
                jbLeft.setEnabled(true);
                jbRight.setEnabled(true);
            }

        }

        //Desabilitar botones de bloquear y colocar caballo
        jbBlock.setEnabled(false);
        jbKnight.setEnabled(false);

    }

    /**
     * Metodo que realiza la acción correspondiente al botón información.
     * Realiza una llamada a info()
     *
     * @param evt
     */
    private void jbHelpActionPerformed(ActionEvent evt) {
        info();
    }

    /**
     * Metodo que realiza la acción correspondiente al botón colocar caballo.
     * Modifica el cursor del programa para mostrar el ícono del caballo.
     *
     * @param evt
     */
    private void jbKnightActionPerformed(ActionEvent evt) {

        if (!setKnightStartBox) {

            //Elegir imagen del cursor.
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image i;
         
            //Obtener dimensiones relativas a la pantalla para el cursor
            Dimension scrSize = toolkit.getScreenSize();
            cursorHeight = (int) scrSize.getHeight()/15;
            cursorWidth = (int) scrSize.getWidth()/15;
            cursorWidth *= scrSize.getHeight()/scrSize.getWidth();
            
            ImageIcon image = new ImageIcon(new ImageIcon("IMAGENES/knight.png").getImage().getScaledInstance(cursorWidth, cursorHeight, Image.SCALE_DEFAULT));
            i = image.getImage();
            
            Cursor c = toolkit.createCustomCursor(i, new Point(menu.getX(), menu.getY()), "img");

            //Cambiar cursor. 
            menu.setCursor(c);
            board.setCursor(c);

            //Habilitar casillas.
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

    /**
     * Metodo que imprime en la ventana del programa el tablero de ajedrez, con
     * el formato tradicional y con un número de filas y columnas igual a
     * DIMENSION. FÓRMULA USADA: Posición = casilla(j)+Dimension*Fila(i)
     * 
     * Al finalizar la inicialización del tablero, asigna el tamaño del cursor
     * para cuando se presiona el boton de bloquear o de posicionar caballo
     *
     * @param DIMENSION el número de las filas y columnas
     */
    public void printBoard(int DIMENSION) {

        //Realizar recorrido a filas y columnas para dar forma al tablero
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {

                box = new JLabel();
                box.setOpaque(true);
                box.addMouseListener(this);

                if (DIMENSION % 2 == 0) {

                    /*
                    Código para asegurar que el pintado de las casillas corres-
                    ponda con el de un tablero normal de ajedrez, independiente-
                    mente del número de filas y columnas escogido.
                     */
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
                //Añadir la casilla al tablero
                board.add(box);
            }
        }
    }

    /**
     * Función que notifica al usuario de la actual configuración del tablero de
     * DIMENSION x DIMENSION y consulta al usuario si desea cambiarlo. Llama a
     * la función JOptionPane.showInputDialog() y obtiene el valor de la nueva
     * dimensión a partir de la respuesta.
     *
     * @return la nueva dimensión escogida por el usuario para el tablero.
     */
    public int changeSizePane() {
        //DECLARACIONES
        String title = "Configuración del tablero";
        String message = "El tablero está configurado como un tablero de "
                + "ajedrez de 8x8.\nSi desea cambiarlo, elija una opción de "
                + "la lista.\nSino, presione cancelar.\n";
        String options[] = {"2x2", "3x3", "4x4", "5x5", "6x6",
            "7x7", "8x8", "9x9", "10x10"};
        final int indexOfDefault = DIMENSION - 2; //Opción 8x8 ocupa posición 6 dentro del array
        int option = 0;

        /*Llamada a showInputDialog para crear una ventana emergente simple
        y consultar al usuario*/
        String option_str = (String) JOptionPane.showInputDialog(null, message, title,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[indexOfDefault]);

        /*Si se presiona cancelar, se conserva el número de filas y columnas
        por defecto*/
        if (option_str == null) {
            return indexOfDefault + 2;
        } else {
            //Buscar equivalencia numérica de opción escogida por usuario.
            for (int i = 0; i < options.length; i++) {
                if (option_str.equals(options[i])) {
                    option = i;
                    break;
                }
            }
        }
        return option + 2;
    }

    /**
     * Metodo que llama a jOptionPane.showMessageDialog() para visualizar una
     * ventana informativa con las instrucciones de uso del programa.
     */
    public void info() {
        JOptionPane.showMessageDialog(null, "INSTRUCCIONES\n\n"
                + "1. Seleccione las casillas que quiere bloquear a través del"
                + " botón con el símbolo de prohibido.\n"
                + "2. Seleccione una posición de inicio para el caballero con "
                + "el botón del caballo.\n"
                + "3. Calcule una posible solución con el botón de la bombilla.\n\n"
                + "Nota: No podrá iniciar los movimientos hasta que no establezca"
                + " una posición de salida para el caballero y se calcule una"
                + " solución válida.\n\n", "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * Metodo que llama a jOptionPane.showMessageDialog() para visualizar una
     * ventana con información del programa.
     */
    public void about() {
        
        ImageIcon icon = new ImageIcon("IMAGENES/knight.png");    
        JOptionPane.showMessageDialog(null, "Knight Tour v1.0\n\n"
                + "Desarrollado por Eugenio Doñaque y Nadal Llabrés.\n"
                + "Todos los derechos reservados.\n\n", 
                "Acerca de Knight Tour",JOptionPane.INFORMATION_MESSAGE,icon);
    
    }

    /**
     * Función que devuelve el estado de una de las casillas del tablero.
     *
     * @param jl casilla a consultar estado.
     * @return true si el ícono de la casilla es igual a null. False en caso
     * contrario
     */
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

        /*Recorrido a los componentes del tablero para identificar la casilla
        presionada*/
        for (int i = 0; i < board.getComponentCount(); i++) {
            if (board.getComponent(i).equals(label)) {
                position = i;
            }
        }

        return position;

    }

    /**
     * Metodo que actualiza el tablero moviendo el caballo a la siguiente
     * casilla, correspondiente a la solución obtenida por el algoritmo Knights-
     * tour. Marca con un número indicando la ordinalidad del paso realizado la
     * casi- lla donde el caballo se encontraba antes de moveForward(). Coloca
     * el ícono del caballo en la nueva casilla.
     */
    public void moveForward() {
        //Check si se ha finalizado el recorrido
        if (globalIndex < sol.length - numBSpots) {

            /*Obtener jLabel donde se encuentra caballo actualmente para enume-
            rar el salto actual*/
            JLabel aux;
            aux = (JLabel) board.getComponent(sol[globalIndex]);
            aux.setIcon(null);
            aux.setText(String.valueOf(globalIndex));
            aux.setHorizontalAlignment(JLabel.CENTER);
            aux.setFont(new Font("Serif", Font.ITALIC, 40));
            aux.setForeground(Color.red);

            /*Obtener label donde saltará el caballo para colocar el icono*/
            ImageIcon knight = new ImageIcon(new ImageIcon("IMAGENES/knight.png").getImage().getScaledInstance(aux.getWidth() - 20, aux.getHeight() - 20, Image.SCALE_DEFAULT));
            globalIndex++;
            aux = (JLabel) board.getComponent(sol[globalIndex]);
            aux.setIcon(knight);
            aux.setHorizontalAlignment(JLabel.CENTER);

        }
    }

    /**
     * Metodo que actualiza el tablero moviendo el caballo a la anterior
     * casilla, correspondiente a la solución obtenida por el algoritmo Knights-
     * tour. Coloca el ícono del caballo en la casilla donde se encontraba en el
     * paso anterior reemplazando el número que indicaba el salto realizado
     * antes de moveBack(). Modifica el ícono donde se encontraba el caballo a
     * null.
     */
    public void moveBack() {
        //Check si se puede saltar hacia atrás
        if (globalIndex > 0) {

            /*Obtener jLabel donde se encuentra el caballo para quitar el ícono
            y conseguir el jLabel de la casilla anterior para colocarlo ahí*/
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

    /**
     * Método que reinicia todas las variables del programa a su estado inicial
     * para poder ejecutar una nueva configuración de tablero.
     */
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

    /**
     * Metodo de evento de ratón. No implementado.
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Metodo de evento de ratón. No implementado.
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Metodo que ejecuta el código correspondiente al estado del programa.
     * Permite al usuario colocar el caballo en una casilla y bloquear tantas
     * casillas como quiera el usuario.
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {

        //Check si se ha realizado un click con el botón izquierdo
        if (e.getButton() == MouseEvent.BUTTON1) {

            //Declaraciones
            JLabel label = (JLabel) e.getSource();
            ImageIcon block = new ImageIcon(new ImageIcon("IMAGENES/block.png").getImage().getScaledInstance(label.getWidth() - 20, label.getHeight() - 20, Image.SCALE_DEFAULT));
            ImageIcon knight = new ImageIcon(new ImageIcon("IMAGENES/knight.png").getImage().getScaledInstance(label.getWidth() - 20, label.getHeight() - 20, Image.SCALE_DEFAULT));

            //Si se ha seleccionado bloquear casillas
            if (setBlockedBoxes) {
                //Verificar que la casilla esté vacía
                if (isEmptyBox(label)) {
                    //Colocar ícono
                    label.setIcon(block);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    busySpots[findPosition(label)] = true;

                } else if (busySpots[findPosition(label)] == true) {
                    //Quitar el ícono de la casilla seleccionada.
                    label.setIcon(null);
                    busySpots[findPosition(label)] = false;
                }
            }

            //Si se ha seleccionado colocar caballo
            if (setKnightStartBox) {

                /*Verificar que la casilla esté vacía y el caballo no se ha
                colocado*/
                if ((isEmptyBox(label)) && (knightBox == -1)) {

                    label.setIcon(knight);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    knightBox = findPosition(label);
                    busySpots[knightBox] = true;

                } else if ((knightBox > -1) && (isEmptyBox(label))) {
                    /*Quitar el caballo de donde estaba y colocarlo en nueva
                    casilla*/
                    JLabel auxLabel;
                    auxLabel = (JLabel) board.getComponent(knightBox);
                    auxLabel.setIcon(null);
                    busySpots[knightBox] = false;

                    label.setIcon(knight);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    knightBox = findPosition(label);
                    busySpots[knightBox] = true;

                } else if (knightBox == findPosition(label)) {
                    //Quitar caballo si se ha presionado la misma casilla
                    busySpots[knightBox] = false;
                    label.setIcon(null);
                    knightBox = -1;

                }
            }
        }
    }

    /**
     * Metodo de evento de ratón. No implementado.
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Metodo de evento de ratón. No implementado.
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
