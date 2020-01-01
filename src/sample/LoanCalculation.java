package sample;

import java.util.Scanner;

/*
 * Created by ITWABI on 3/20/2018.
 */
public class LoanCalculation {

    public static void main(String []args){
        String fullName;
        double salary, loanAmount, taxedPay, loanRepay, monthlyRepay;

	fullName = GetUserName();
	salary = GettingGrossSalary();
	taxedPay =  NetSalaryTaxed(salary);
	loanAmount = GettingLoanAmount();
	loanRepay = CalcRepay(loanAmount);

	monthlyRepay = loanRepay/12;
	
        DisplayQualification(fullName, taxedPay, loanRepay, monthlyRepay);

    }
    public static String GetUserName(){
        Scanner input = new Scanner(System.in);
        String name;
        //get the users name
        System.out.println("please enter your full name: ");
        name = input.nextLine();
        return name;
    }
    public static double GettingGrossSalary(){
        Scanner input = new Scanner(System.in);
        double pay;
        //get the users net pay
        System.out.println("please enter your net salary: ");
        pay = input.nextDouble();
        return pay;
    }

    public static double GettingLoanAmount(){
        Scanner input = new Scanner(System.in);
        double takeLoan;
        //get the users preferred loan amount to be taken
        System.out.println("enter the loan amount you wish to take :");
        takeLoan = input.nextDouble();
        return takeLoan;
    }
    public static double NetSalaryTaxed(double grossMonthlyPay){
        double taxRate,  tax, netMonthlyPay;

        //CALCULATE THE NET MONTHLY PAY BY REMOVING THE TAX
        taxRate = 30.0/100;

        double first = grossMonthlyPay - 20000;   //first 20,000 goes free.
        double second = 0.15 * 5000;    //second 5,000 has 15 percent taxed off of it.
        double third = taxRate * (first - second);    //the rest is taxed at 30 percent(taxRate)

        tax = second + third;
        netMonthlyPay = grossMonthlyPay - tax;
        return netMonthlyPay;
    }

    public static double CalcRepay(double loanAmount){
        double interestRate, loanAmountToPay;

        //CALCULATING LOAN AMOUNT TO BE PAID PLUS INTEREST
        interestRate = 30.0/100;

        double interest = interestRate * loanAmount;
        loanAmountToPay = loanAmount + interest;

        return loanAmountToPay;

    }

    public static void DisplayQualification(String full_name, double netMonthlyPay, double loanToPay, double monthlyLoanRepay){
        //DETERMINING IF THE USER IS ELIGIBLE FOR A LOAN
        double half = netMonthlyPay * (50.0/100);

        if (monthlyLoanRepay <= half){
            System.out.println(full_name + ", You're eligible for a loan! ");
            Output(loanToPay, monthlyLoanRepay);
        }
        else {
            System.out.println(full_name + ", You're not eligible for a loan. ");
            Output2(monthlyLoanRepay, netMonthlyPay);
        }
    }

    public static void Output(double loanToPay, double monthlyRepay){
        System.out.println("The total loan repayment amount is " +loanToPay +". Your monthly repay amount is " + monthlyRepay);
    }

    public static void Output2( double monthlyToPay, double netPay){
        System.out.println("The monthly repay amount of "+monthlyToPay +" is more than 50% of your monthly salary which is " + netPay);
    }

}
