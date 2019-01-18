package estudio_algoritmia;

import java.util.Stack;

public class Sort {

    public static void quickSort(int i, int j, int[] array) {
        if (i < j) {
            int m = partition(i, j, array);
            quickSort(i, m - 1, array);
            quickSort(m + 1, j, array);
        }
    }

    private static int partition(int i, int j, int[] array) {

        int index = i - 1;
        int pivot = array[j];
        int tmp;

        for (int k = i; k < j; k++) {

            if (array[k] < pivot) {

                index++;
                tmp = array[index];
                array[index] = array[k];
                array[k] = tmp;

            }

        }

        tmp = array[index + 1];
        array[index + 1] = array[j];
        array[j] = tmp;
        return index + 1;
//        int index = array.length-1;
//        int tmp;
//        for (int k = i; k < j; k++) {
//            if (array[index] < array[k]) {
//                tmp = array[k];
//                array[k] = array[index];
//                array[index] = tmp;
//            }
//        }
//        return 
    }

    public static void iQuickSort(int i, int j, int[] array) {

        if (i >= j) {

            return;

        }

        Stack s = new Stack();
        int[] x = {i, j};
        int[] aux;

        s.push(x);
        int m;

        while (!(s.empty())) {

            aux = (int[]) s.pop();
            //Process
            m = partition(aux[0], aux[1], array);

            //Right
            if (m + 1 < aux[1]) {
                int[] auxR = new int[2];
                auxR[0] = m + 1;
                auxR[1] = aux[1];
                s.push(auxR);

            }

            //Left
            if (aux[0] < m - 1) {
                int[] auxL = new int[2];
                auxL[0] = aux[0];
                auxL[1] = m - 1;
                s.push(auxL);

            }

        }

    }

}
