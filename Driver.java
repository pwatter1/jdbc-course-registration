import java.sql.*;
import java.util.*;

public class Driver
{
        public static ResultSet rset = null;
	public static Statement stmt = null;
        public static PreparedStatement pStmt = null;
        public static CallableStatement cStmt = null;
        public static final Connection con = JDBC.createConnection();
        public static final Scanner scan = new Scanner(System.in);

        public static void main(String[] args) { getOption(); createLogSequence(); }

        public static void createLogSequence()  
        {
                try {
                        String query;
                        stmt = con.createStatement();
                        
                        // check if sequence already created
                        query = "SELECT COUNT(*) AS VAL FROM user_sequences WHERE sequence_name = 'LOG_SEQ'";           
                        rset = stmt.executeQuery(query);
                        
                        if (rset.getInt("VAL") != 1) // not created yet 
                        {
                                query = "CREATE SEQUENCE LOG_SEQ IF NOT EXISTS START WITH 100 MAXVALUE 999 MINVALUE 100 INCREMENT BY 1";
                                Boolean retVal = stmt.execute(query);
                                if (!retVal) { 
                                        System.out.println("Error: Creating Logid Sequence.");
                                        System.exit(-1);
                                }
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }

        public static void addStudent() throws SQLException 
        {       
                System.out.println();
                System.out.print("Adding a Student - Enter an SID: ");
                String sid = scan.next();
                System.out.print("Enter First Name: ");
                String first = scan.next();
                System.out.print("Enter Last Name: ");
                String last = scan.next();
                System.out.print("Enter Student's Status: ");
                String status = scan.next();
                System.out.print("Enter Student's Email: ");
                String email = scan.next();
                System.out.print("Enter Student's GPA: ");
                double gpa = scan.nextDouble();
                scan.nextLine();

		String query = "insert into STUDENTS(sid, firstname, lastname, status, gpa, email) values (?,?,?,?,?,?)";
		pStmt = con.prepareStatement(query);
		pStmt.setString(1, sid);
		pStmt.setString(2, first);
		pStmt.setString(3, last);
		pStmt.setString(4, status);
		pStmt.setDouble(5, gpa);
		pStmt.setString(6, email);

		if (pStmt.executeUpdate() == 1) {
			System.out.println();
			System.out.println("Success: Student Added.");
			getOption();
		} else {
			System.out.println();
			System.out.println("Error: Student unable to be added to Students Table.");
		}                
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
                
                stmt = con.createStatement();

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

                // list sid, last, and status from students table
                // show all courses and their info taken from enrollments, courses, classes

                query = "SELECT sid, lastname, status FROM students WHERE sid=?";
                pStmt = con.prepareStatement(query);
                pStmt.setString(1, sid);
                rset = pStmt.executeQuery();

                if (rset.next()) {
                        System.out.println(rset.getString(1));
                } else {
                        System.out.println();
                        System.out.println("Error: The SID is invalid.");
                        getOption();
                } 

                String classid;
                boolean found = false;
                query = "SELECT classid FROM enrollments WHERE sid=?";
                pStmt = con.prepareStatement(query);
                pStmt.setString(1, sid);
                rset = pStmt.executeQuery();
                
                
                while (rset.next()) {
                        found = true;
                        classid = rset.getString("classid");
                        viewCourseInfo(classid);                        
                }
                if (found == false) {
                        System.out.println();
                        System.out.println("The student has not taken any courses.");
                }
        }

        public static void viewCourseInfo(String classid)
        {
                query = "Select 
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
                                        viewStudentInfo();
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
