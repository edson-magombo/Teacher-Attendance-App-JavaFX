package sample;

/*
 * Created by ITWABI on 4/2/2018.
 */
import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;


public class SQliteJDBC {

    public static void main( String args[] ) {
        Connection c = null;
        Statement stmt = null;
        Scanner input = new Scanner(System.in);
        String subs;
        int subNo;


        Calendar now = Calendar.getInstance();

        int dd = now.get(Calendar.DATE);

        System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
                + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

        String[] strDays = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thusday",
                "Friday", "Saturday" };

        String day = strDays[now.get(Calendar.DAY_OF_WEEK) - 1];
        // Day_OF_WEEK starts from 1 while array index starts from 0
        System.out.println("Current day is : " + day);

        String[] subjects;

        System.out.println("enter the number of subjects: ");
        subNo = input.nextInt();
        subjects = new String[subNo];



        System.out.println("enter the subject: ");
        String subj = input.next();



        if ((day.equals("Wednesday"))){
            System.out.println("so this could work");

            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:C:\\sqlite\\School.db");
                System.out.println("Opened database successfully");

                stmt = c.createStatement();
                String sql = "SELECT firstname, lastname FROM form4";
                ResultSet rset = stmt.executeQuery(sql);
                while (rset.next()){
                    String firstname = rset.getString("firstname");
                    String lastname = rset.getString("lastname");



                    System.out.println(firstname + " " + lastname);

                    }

                stmt.executeUpdate(sql);
                stmt.close();
                c.close();

                } catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);}
        }else{System.out.println("this did not work");}




    }
}