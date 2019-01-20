/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knight;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author nadal
 */
public class Algorithm {

    /*Variables utilizadas para visualizar el número de combinaciones fallidas
    intentadas*/
    private static BigInteger numberOfCombinations = new BigInteger("1");
    private static CombinationsWindow cw;

    /**
     * Metodo que inicializa y visualiza la ventana donde se notifica del numero
     * de combinaciones intentadas antes de conseguir un resultado
     *
     * @param parentFrame frame desde el cual el usuario interactual con el
     * programa
     */
    public static void initCombinationsWindow(JFrame parentFrame) {
        //Generar la ventana de combinaciones con el frame padre.
        cw = new CombinationsWindow(parentFrame);
        cw.setVisible(true);
    }

    /**
     * Enum para diferenciar entre los 8 diferentes tipos de movimientos que
     * puede realizar un caballo dentro de un tablero de ajedrez.
     */
    public enum movements {
        UUR, URR, RRD, RDD, DDL, DLL, LLU, LUU
    }

    /**
     * Función que devuelve un array con la secuencia de casillas que el caballo
     * ha de recorrer para pasar por cada una de las casillas disponibles sin
     * repetir ni dejar una sin visitar.
     *
     * @param board el tablero donde se desarrolla el programa.
     * @param busySpots array de booleanos que indica que casillas no estan dis-
     * ponibles.
     * @param knightBox la posición del caballo dentro del array de componentes
     * de board.
     * @return array con el resultado del algoritmo. Si solution[0] == -1
     * entonces es que no se ha conseguido ninguna combinación que sea solución
     * de la configuración escogida.
     *
     * La función inicializa todas las variables necesarias para realizar la
     * inmersión en la función homónima que realiza un algoritmo de tipo back-
     * tracking para conseguir una solución posible.
     */
    public static int[] KnightsTour(JPanel board, boolean[] busySpots, int knightBox) {
        int[] solution = new int[board.getComponentCount()];
        int index = 1;
        int numBSpots = 0;
        Arrays.fill(solution, -1);
        solution[0] = knightBox;

        //Contar numero de casillas bloqueadas
        for (int i = 0; i < busySpots.length; i++) {
            if (busySpots[i]) {
                numBSpots++;
            }
        }
        numBSpots--; //Quitar la posición inicial de caballo

        //Realizar llamada a algoritmo de backtracking
        if (!KnightsTour(busySpots, solution, index, numBSpots)) {
            /*Si resultado del algoritmo no ha sido una posible solución, 
            indicar a través de la ventana de número de combinaciones*/
            cw.modifyValue(numberOfCombinations, false);
            solution[0] = -1;
        } else {

            //Notificar del número de combinaciones intentadas y devolver solucion
            cw.modifyValue(numberOfCombinations, true);
        }
          numberOfCombinations = BigInteger.ONE;
          return solution;
    }

    /**
     * Función que ejecuta el algoritmo basado en el esquema backtracking para
     * conseguir una solución al problema Knight tour. El algoritmo implementa
     * una función de poda donde cada siguiente salto se realiza hacia la casi-
     * lla desde la cual el caballo tendrá menos saltos posibles.
     *
     * @param busySpots
     * @param solution
     * @param index
     * @param numBSpots
     * @return
     */
    private static boolean KnightsTour(boolean[] busySpots, int[] solution, int index, int numBSpots) {

        //Check si se ha llegado a una solución
        if (index + numBSpots < solution.length) {

            //Obtener array de posibles movimientos desde posición actual
            ArrayList<Integer> possibleMoves = getPossibleMoves(solution, index, busySpots);

            //Recorrido al array de movimientos posibles
            for (int i = 0; i < possibleMoves.size(); i++) {

                /*Realizar movimiento: actualizar solution y busySpots*/
                solution[index] = possibleMoves.get(i);
                busySpots[possibleMoves.get(i)] = true;

                //Incrementar indice para llamada recursiva
                index++;

                /*Realizar llamada recursiva y retornar true si se encontró
                solución*/
                if (KnightsTour(busySpots, solution, index, numBSpots)) {
                    return true;
                }

                /*Decrementar indice para realizar otro intento con el siguien-
                te movimiento disponible, en la misma posición desde donde no 
                se ha encontrado una solución*/
                index--;

                //Incrementar contador de combinaciones
                numberOfCombinations = numberOfCombinations.add(BigInteger.ONE);

                /*Remover información del intento realizado antes de llamada
                recursiva*/
                busySpots[possibleMoves.get(i)] = false;
                solution[index] = -1;
            }
            //Si se comprobado cada movimiento sin resultado positivo
            return false;
        } else {
            //Si se ha conseguido una solución
            return true;
        }
    }

    /**
     * Función que busca todas las posibles casillas a las cuales se puede
     * saltar desde la posición actual del caballo y devuelve un array de saltos
     * posibles ordenado de manera ascendente según el número de saltos posibles
     * desde cada una de las posibles casillas.
     *
     * @param solution array que contendrá la secuencia solución del problema
     * @param index indica la posición actual dentro del array solución
     * @param busySpots indica las casillas no disponibles para saltar.
     * @return
     */
    private static ArrayList<Integer> getPossibleMoves(int[] solution, int index, boolean[] busySpots) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        ArrayList<Integer> numOfPossibleMoves = new ArrayList<>();

        //Recorrido al array de enums
        for (int i = 0; i < movements.values().length; i++) {
            //Ver si es posible realizar el movimiento en la posicíon i
            if (possibleToMove(busySpots, i, solution[index - 1], (int) Math.sqrt(solution.length))) {

                //Buscar la siguiente posible posición
                int nextMove = nextMovement(i, solution[index - 1], (int) Math.sqrt(solution.length));
                //Buscar el número de saltos posibles desde la siguiente casilla
                int count = countMovementsIn(nextMove, busySpots, solution);

                /*Añadir a arrays de manera que casilla y numero de saltos com-
                partan el mismo indice*/
                possibleMoves.add(nextMove);
                numOfPossibleMoves.add(count);
            }
        }
        /*Devolver un array ordenado de forma ascendente según el número de
        disponibles*/
        return insertionSort(possibleMoves, numOfPossibleMoves);
    }

    /**
     * Función que toma los arrays posMov y numPosMov que contienen el número de
     * una casilla y el número de saltos posibles desde esa casilla para ordenar
     * posMov de menor cantidad de saltos posibles a mayor cantidad de saltos
     * posibles.
     *
     * @param posMov array que contiene el número de la casilla.
     * @param numPosMov array que contiene el número de saltos posibles desde
     * cada posible
     * @return un array con el número de las casillas posibles ordenado según el
     * número de saltos posibles desde cada una, en orden ascendente.
     */
    private static ArrayList<Integer> insertionSort(ArrayList<Integer> posMov, ArrayList<Integer> numPosMov) {
        int min, tmp, tmp2;

        for (int i = 0; i < posMov.size() - 1; i++) {
            min = i;
            for (int j = i + 1; j < posMov.size(); j++) {
                if (numPosMov.get(j) < numPosMov.get(min)) {
                    min = j;
                }
            }
            tmp = numPosMov.get(i);
            tmp2 = posMov.get(i);

            numPosMov.set(i, numPosMov.get(min));
            posMov.set(i, posMov.get(min));

            numPosMov.set(min, tmp);
            posMov.set(min, tmp2);
        }

        return posMov;
    }

    /**
     * Función que cuenta cuantos saltos son posibles desde la posicíon hipoté-
     * tica del caball indicada por nextMove
     *
     * @param nextMove indica la casilla de la cual se quiere obtener el numero
     * de saltos
     * @param busySpots indica las casillas no disponibles para saltar.
     * @param solution array que contendrá la posible solución al problema del
     * programa
     * @return el numero de saltos posibles desde la casilla.s
     */
    private static int countMovementsIn(int nextMove, boolean[] busySpots, int[] solution) {
        busySpots[nextMove] = true;
        int count = 0;

        for (int i = 0; i < movements.values().length; i++) {
            if (possibleToMove(busySpots, i, nextMove, (int) Math.sqrt(solution.length))) {
                count++;
            }
        }

        busySpots[nextMove] = false;

        return count;
    }

    /**
     * Función que calcula la siguiente posición del caballo dentro del tablero
     * según el movimiento a realizar indicado por la posición i dentro del
     * array de enums y la posición actual del caballo.
     *
     * @param i indica la posición del movimiento dentro del array de enums
     * @param knightPos indica la posición actual del caballo
     * @param dim indica la dimensión del tablero del programa
     * @return la posición que la siguiente casilla ocupa dentro del array de
     * componentes del tablero del programa.
     */
    private static int nextMovement(int i, int knightPos, int dim) {
        /*Realizar movimiento dependiendo de la posicion actual*/
        switch (movements.values()[i]) {
            /* Todos los casos siguen la siguiente lógica:
                -Saltar a una casilla superior significa restar una vez la
                 dimensión del tablero a la posición actual del caballo.
                -Saltar a una casilla inferior significa sumar una vez la dimen-
                 sión del tablero a la posición actual del caballo.
                -Saltar a una casilla a la izquierda significa restar una unidad
                 a la posicion actual del caballo.
                -Saltar a una casilla a la derecha significa sumar una unidad a 
                 la posición actual del caballo.
             */
            case UUR:
                return knightPos + 1 - dim * 2;

            case URR:
                return knightPos + 2 - dim;

            case RRD:
                return knightPos + 2 + dim;

            case RDD:
                return knightPos + 1 + dim * 2;

            case DDL:
                return knightPos - 1 + dim * 2;

            case DLL:
                return knightPos - 2 + dim;

            case LLU:
                return knightPos - 2 - dim;

            case LUU:
                return knightPos - 1 - dim * 2;
        }
        /*En caso de que i no corresponda con ningún movimiento, devolver -1
        como marca de error*/
        return -1;
    }

    /**
     * Función que verifica que el movimiento indicado por i dentro del array de
     * enums sea válido para la posición actual del caballo, según la fila y
     * columne que este ocupe y la direccion y cantidad de saltos que realice
     * cada movimiento
     *
     * @param busySpots indica las casillas no disponibles para saltar
     * @param i indica la posición del movimiento dentro del array de enums
     * @param knightPos indica la posición actual del caballo
     * @param dimension indica la dimensión del tablero del programa
     * @return true si es posible realizar el salto. False en caso contrario.
     */
    private static boolean possibleToMove(boolean[] busySpots, int i, int knightPos, int dimension) {
        int knightRow = knightPos / dimension;
        int knightCol = knightPos % dimension;

        //Realizar comprobacion dependiendo del movimiento escogido
        switch (movements.values()[i]) {
            case UUR:
                /*El caballo no puede saltar dos veces hacia arriba y uno a la
                derecha si se encuentra en alguna de las dos primeras filas
                de arriba o en la última columna a la derecha*/
                if ((knightRow < 2) || (knightCol >= dimension - 1)
                        || (busySpots[knightPos - dimension * 2 + 1])) {
                    return false;
                }

                break;
            case URR:
                /*El caballo no puede saltar hacia arriba una vez y dos veces
                a la derecha si se encuentra en la primera fila de arriba o en
                una de las dos últimas columnas a la derecha*/
                if ((knightRow < 1) || (knightCol >= dimension - 2)
                        || (busySpots[knightPos - dimension + 2])) {
                    return false;
                }

                break;
            case RRD:
                /*El caballo no puede saltar dos veces a la derecha y una hacia
                abajo si se encuentra en una de las dos ultimas columnas de la
                derecha o en la ultima fila de abajo*/
                if ((knightRow >= dimension - 1) || (knightCol >= dimension - 2)
                        || (busySpots[knightPos + 2 + dimension])) {
                    return false;
                }

                break;
            case RDD:
                /*El caballo no puede saltar una vez hacia la derecha y dos
                hacia abajo si se encuentra en la ultima columna de la derecha
                o en alguna de las dos ultimas filas de abajo*/
                if ((knightRow >= dimension - 2) || (knightCol >= dimension - 1)
                        || (busySpots[knightPos + 1 + dimension * 2])) {
                    return false;
                }
                break;
            case DDL:
                /*El caballo no puede saltar dos veces hacia abajo y una hacia
                la izquierda si se encuentra en una de las dos últimas filas de
                abajo o en la primera columna de la izquierda*/
                if ((knightRow >= dimension - 2) || (knightCol < 1)
                        || (busySpots[knightPos - 1 + dimension * 2])) {
                    return false;
                }

                break;
            case DLL:
                /*El caballo no puede saltar una vez hacia abajo y dos hacia la
                izquierda si se encuentra en la ultima fila de abajo o en una de
                las dos primeras columnas de la izquierda*/
                if ((knightRow >= dimension - 1) || (knightCol < 2)
                        || (busySpots[knightPos - 2 + dimension])) {
                    return false;
                }

                break;
            case LLU:
                /*El caballo no puede saltar dos veces hacia la izquierda y una
                hacia arriba si se encuentra en una de los dos primeras columnas
                de la izquierda o en la primera fila de arriba*/
                if ((knightRow < 1) || (knightCol < 2)
                        || (busySpots[knightPos - 2 - dimension])) {
                    return false;
                }

                break;
            case LUU:
                /*El caballo no puede saltar una vez hacia la izquierda y dos
                hacia arriba si se encuentra en la primera columna de la izqui-
                erda o en una de las dos primeras columnas de arriba*/
                if ((knightRow < 2) || (knightCol < 1)
                        || busySpots[knightPos - 1 - dimension * 2]) {
                    return false;
                }

                break;
        }
        //Si el movimiento es posible desde la posición del caballo, return true
        return true;
    }

    /**
     * Metodo que devuelve el número de combinaciones probadas antes de llegar a
     * un resultado.
     *
     * @return número de combinaciones intentadas.
     */
    public static BigInteger getCombinationCount() {
        return numberOfCombinations;
    }
}
