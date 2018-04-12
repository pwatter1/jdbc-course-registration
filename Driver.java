import java.sql.*;
import java.util.*;

public class Driver
{
	public static final Scanner scan = new Scanner(System.in);

	public static void main(String[] args) { Menu(); }

    public static void Menu() 
    {
    	while (true) 
    	{
            System.out.println();
            System.out.println("--------------- CS432 DB Project 2 ---------------");
            System.out.println("1 View a Table");
            System.out.println("2 Add a Student");
            System.out.println("3 Delete a Student");
            System.out.println("4 View Student's Information");
            System.out.println("5 View Prerequisites for a Course");
            System.out.println("6 Enroll Student to a Specific Course");
            System.out.println("7 Drop Student from a Specific Course");
            System.out.println("8 View Course's Information and Students Enrolled");
            System.out.println("9 Quit");

            int in = scan.nextInt();

            try {
                    switch (in) 
                    {
                            case 1:
                            		int temp = getTableChoice();
                                    JDBC.viewTable(temp);
                                    break;
                            case 2:
                                    JDBC.addStudent();
                                    break;
                            case 3:
                                    break;
                            case 4:
                            		String sid = getSID();
                                    JDBC.viewStudentInfo(sid);
                                    break;
                            case 5:
                                    break;
                            case 6:
                                    break;
                            case 7:
                                    break;
                            case 8:
                                    System.out.println("--------------------------------------------------");
                                    JDBC.closeConnection();
                                    JDBC.closeStatement();
                                    JDBC.closeResultSet();
                                    JDBC.closePreparedStatment();
                                    JDBC.closeCallableStatement(); 
                                    System.exit(0);
                            default:
                                    System.out.println("You've entered an invalid option.");
                    }
            } catch (SQLException e) {
                    e.printStackTrace();
            }
    	}
    }

    public int getTableChoice() 
    {
    	System.out.println();
        System.out.println("View a Table - Enter a number: ");
        System.out.println("1 Students Table");
        System.out.println("2 Courses Table");
        System.out.println("3 Prerequisites Table");
        System.out.println("4 Classes Table");
        System.out.println("5 Enrollments Table");
        System.out.println("6 Logs Table");

        int in = scan.nextInt();
        System.out.println();
        return in;
    }

    public String getSID() 
    {
		System.out.println();
        System.out.print("Viewing a Student - Enter a SID: ");
        String sid = scan.next();
        System.out.println();
        return sid;
    }
}

