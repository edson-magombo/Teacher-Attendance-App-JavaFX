package sample;

import java.util.Scanner;

/*
 a program that calculates the highest common factor of two numbers
 */
public class HighestCommonFactor {
    public static void main(String [] args){

        //define variables
        int number;

        //define scanner variable
        Scanner input = new Scanner(System.in);

        //get user input on students
        System.out.println("enter the number: ");
        number = input.nextInt();

        int sum = 0;
        int num = 0;

        while (num < number ){
            int y = 1 + num;
            sum += y;
            num++;
            System.out.println("the sum is: " + sum);


        }


    }
}
