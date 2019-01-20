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
public class Algorithm /*extends Thread */{

    public static BigInteger numberOfCombinations = new BigInteger("1"); 
    private static CombinationsWindow cw; 
    
    //Variables para el hilo. 
    JPanel b;
    boolean [] bool;
    int k;
    
    public static void initCombinationsWindow(JFrame parentFrame) {
    
        cw = new CombinationsWindow(parentFrame);
        cw.setVisible(true);
    
    }
    
    public enum movements {
        UUR, URR, RRD, RDD, DDL, DLL, LLU, LUU
    }

    public static int[] KnightsTour(JPanel board, boolean[] busySpots, int knightBox) throws Exception {
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

        if (!KnightsTour(busySpots, solution, index, numBSpots)) {
            cw.modifyValue(numberOfCombinations, false);
            numberOfCombinations = BigInteger.ONE;
            throw new Exception("No se ha encontrado solución con esa configuración de tablero.");
        }
        
        cw.modifyValue(numberOfCombinations, true);
        numberOfCombinations = BigInteger.ONE;
        return solution;
    }

    private static boolean KnightsTour(boolean[] busySpots, int[] solution, int index, int numBSpots) {
             
        if (index + numBSpots < solution.length) {

            ArrayList<Integer> possibleMoves = getPossibleMoves(solution, index, busySpots);

            for (int i = 0; i < possibleMoves.size(); i++) {

                solution[index] = possibleMoves.get(i);
                busySpots[possibleMoves.get(i)] = true;

                index++;
                if (KnightsTour(busySpots, solution, index, numBSpots)) {
                    return true;
                }

                index--;
                numberOfCombinations = numberOfCombinations.add(BigInteger.ONE);
                busySpots[possibleMoves.get(i)] = false;
                solution[index] = -1;

            }
            return false;
        } else {
            return true;
        }
    }

    private static ArrayList<Integer> getPossibleMoves(int[] solution, int index, boolean[] busySpots) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        ArrayList<Integer> numOfPossibleMoves = new ArrayList<>();

        for (int i = 0; i < movements.values().length; i++) {
            if (possibleToMove(busySpots, i, solution[index - 1], (int) Math.sqrt(solution.length))) {

                int nextMove = nextMovement(i, solution[index - 1], (int) Math.sqrt(solution.length));
                int count = countMovementsIn(nextMove, busySpots, solution);

                possibleMoves.add(nextMove);
                numOfPossibleMoves.add(count);
            }
        }

        return insertionSort(possibleMoves, numOfPossibleMoves);

    }

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

    private static int nextMovement(int i, int knightPos, int dim) {
        switch (movements.values()[i]) {
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
        return -1;
    }

    private static boolean possibleToMove(boolean[] busySpots, int i, int knightPos, int dimension) {
        int knightRow = knightPos / dimension;
        int knightCol = knightPos % dimension;

        switch (movements.values()[i]) {
            case UUR:

                if ((knightRow < 2) || (knightCol >= dimension - 1)
                        || (busySpots[knightPos - dimension * 2 + 1])) {
                    return false;
                }

                break;
            case URR:

                if ((knightRow < 1) || (knightCol >= dimension - 2)
                        || (busySpots[knightPos - dimension + 2])) {
                    return false;
                }

                break;
            case RRD:

                if ((knightRow >= dimension - 1) || (knightCol >= dimension - 2)
                        || (busySpots[knightPos + 2 + dimension])) {
                    return false;
                }

                break;
            case RDD:

                if ((knightRow >= dimension - 2) || (knightCol >= dimension - 1)
                        || (busySpots[knightPos + 1 + dimension * 2])) {
                    return false;
                }
                break;
            case DDL:

                if ((knightRow >= dimension - 2) || (knightCol < 1)
                        || (busySpots[knightPos - 1 + dimension * 2])) {
                    return false;
                }

                break;
            case DLL:

                if ((knightRow >= dimension - 1) || (knightCol < 2)
                        || (busySpots[knightPos - 2 + dimension])) {
                    return false;
                }

                break;
            case LLU:

                if ((knightRow < 1) || (knightCol < 2)
                        || (busySpots[knightPos - 2 - dimension])) {
                    return false;
                }

                break;
            case LUU:

                if ((knightRow < 2) || (knightCol < 1)
                        || busySpots[knightPos - 1 - dimension * 2]) {
                    return false;
                }

                break;
        }
        return true;
    }

    private static void printSolution(int[] solution, boolean[] busySpots) {
        for (int i = 0; i < solution.length; i++) {
            System.out.print(solution[i] + "    ");
        }
    }

    public static BigInteger getCombinationCount() {
        return numberOfCombinations;
    }
}
