import java.sql.*;
import java.util.*;

public class Driver
{
        public static final Connection con = JDBC.createConnection();
        public static final Scanner scan = new Scanner(System.in);
        public static Statement stmt = con.createStatement(); 
        public static ResultSet rset = null;

        public static void main(String[] args) { getOption(); createLogSequence(); }

        public static void createLogSequence() 
        {
        	String query = "CREATE SEQUENCE LOG_SEQ START WITH 100 MAXVALUE 999 MINVALUE 100 INCREMENT BY 1";
   		Boolean retVal = stmt.execute(query);
		if (!retVal) { 
			System.out.println("Error: creating log id sequence.");
			System.exit(-1);
		}
        }
        
        public static void addStudent() throws SQLException 
        {       
        	System.out.println();
                System.out.println("Adding a Student - Enter an SID: ");
                String sid = scan.next();
                System.out.println();
                System.out.print("Enter First Name: ");
                String first = scan.next();
                System.out.println();
                System.out.print("Enter Last Name: ");
                String last = scan.next();
                System.out.println();
                System.out.print("Enter Student's Status: ");
                String status = scan.next();
                System.out.println();
                System.out.print("Enter Student's Email: ");
                String email = scan.next();
                System.out.println();
                System.out.print("Enter Student's GPA: ");
                float gpa = scan.nextFloat();
                scan.nextLine();

        }

        public static void viewTable() throws SQLException
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

                // use a regular statement bc we dont need dynamic params
                // use executeQuery bc we expect it to return result sets

                if (in == 1) {      rset = stmt.executeQuery("SELECT * FROM students"); }
                else if (in == 2) { rset = stmt.executeQuery("SELECT * FROM courses"); }
                else if (in == 3) { rset = stmt.executeQuery("SELECT * FROM prerequisites"); }
                else if (in == 4) { rset = stmt.executeQuery("SELECT * FROM classes"); }
                else if (in == 5) { rset = stmt.executeQuery("SELECT * FROM enrollments"); }
                else if (in == 6) { rset = stmt.executeQuery("SELECT * FROM logs"); }
                else {
                        System.out.println("You've entered an invalid option.");
                        viewTable();
                }

                while (rset.next()) 
                {
                        System.out.print (rset.getString (1)+"  ");
                        System.out.print (rset.getString (2)+"  ");
                        System.out.print (rset.getString (3)+"  ");
                        System.out.print (rset.getString (4)+"  ");
                        System.out.print (rset.getString (5)+"  ");
                        System.out.println (rset.getString (6)+"  ");
                }
                getOption();
        }

        public static void viewStudent()
        {
        	System.out.println();
        	System.out.println("Viewing a Student - Enter an SID: ");
                System.out.print("Enter an SID: ");
                String sid = scan.next();
                System.out.println();


        }

        public static void getOption() 
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
                System.out.println("8 Quit");

                int in = scan.nextInt();

                try {
                        switch (in) 
                        {
                                case 1:
                                        viewTable();
                                        break;
                                case 2:
                                        addStudent();
                                        break;
                                case 3:
                                        break;
                                case 4:
                                        break;
                                case 5:
                                        break;
                                case 6:
                                        break;
                                case 7:
                                        break;
                                case 8:
                                        System.out.println("--------------------------------------------------");
                                        JDBC.closeConnection(con);
                                        JDBC.closeStatement(stmt);
                						JDBC.closeResultSet(rset); 
                						System.exit(0);
                                default:
                                        System.out.println("You've entered an invalid option.");
                                        getOption();
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }
}

