/*loan calculation program that calculates loan to be paid and eligibility of taking a loan*/

package sample;

import javax.sql.rowset.spi.SyncProvider;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class ClassWork {
    public static void main(String[] args) {

        //declaring variables
        int studentNo, temp;
        double[] scores;

        //declare scanner object
        Scanner input = new Scanner(System.in);

        //get user input
        System.out.println("enter the number of students: ");
        studentNo = input.nextInt();
        scores = new double[studentNo];


        for(int i = 0; i < studentNo; i++){
            System.out.println("enter the" + i + " student score: ");
            scores[i] = input.nextDouble();
        }

        System.out.println("the sum of your array is: " + Sum(scores));
    }
    public static double Sum(double[] arrayUsed){

        double average;
        double sum = 0;
        for (double value: arrayUsed){
            sum += value;
        }
        average = sum/arrayUsed.length;
        return sum;
    }

}
