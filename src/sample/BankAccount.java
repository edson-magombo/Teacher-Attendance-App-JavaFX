package sample;

import java.util.Date;

/*
  Created by ITWABI on 4/10/2018.
 */
public class BankAccount {
    Date dateCreated;
    double annualInterestRate;
    private double balance;
    private int id;
    double monthlyInt;
    double newBalance;

    BankAccount(){
    }

    BankAccount(int idi, double bal, double annualInterestRate){
        this.id = idi;
        this.balance = bal;
        this.annualInterestRate = annualInterestRate;
        dateCreated = new Date();
    }

    double getMonthlyInterestRate(double yearlyInterest){
        return (yearlyInterest/12)/100;
    }

    double getMonthlyInterest(double monthlyRate){
        monthlyInt = monthlyRate * balance;
        return monthlyInt;
    }

    double withdraw(double amount){
        balance = this.getBalance() - amount;
        return balance;}

    double deposit(double amount){
        balance = this.getBalance() + amount;
        return balance;
    }

    void setBalance(double bal){balance = bal;}

    double getBalance(){return balance;}

    void setId(int idi){id = idi;}

    int getId(){return id;}

    void setDateCreated(Date mine){dateCreated = mine;}

    Date getDateCreated(){return dateCreated;}

    void setAnnualInterestRate(double rate){annualInterestRate = rate;}

    double getAnnualInterestRate(){return annualInterestRate;}

}

class Account{
    public static void main(String[] args){
        Date date = new Date();
        BankAccount myAccount = new BankAccount();

        myAccount.setAnnualInterestRate(4.5);
        myAccount.setBalance(20000);
        myAccount.setId(1122);
        myAccount.setDateCreated(date);

        double annualIntRate = myAccount.getAnnualInterestRate();
        double myBalance = myAccount.getBalance();
        double monthlyIntRate = myAccount.getMonthlyInterestRate(annualIntRate);
        double monthlyInterest = myAccount.getMonthlyInterest(monthlyIntRate);

        double afterWithdraw = myAccount.withdraw(2500);
        double afterDepoit = myAccount.deposit(3000);

        double newBalance = myAccount.getBalance();

        Date mine = myAccount.getDateCreated();

        System.out.print("Your account balance is: " + newBalance + ". Your monthly interest is: " + monthlyInterest
                + " and this account was created on " + mine );






    }
}


