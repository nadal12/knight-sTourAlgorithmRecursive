/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estudio_algoritmia;

/**
 *
 * @author nadal
 */
public class Estudio_algoritmia {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int [] array = {8,3,6,5,5,9,1};

        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]+", ");
        }
        System.out.println();
        
        Sort.iQuickSort(0, array.length-1, array);

        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]+", ");
        }
        
    }
    
}
