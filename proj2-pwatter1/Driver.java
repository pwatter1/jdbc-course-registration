// Patrick Watters
// CS432 Project 2 

import java.sql.*;
import java.util.*;


public class Driver
{
	public static final Scanner scan = new Scanner(System.in);

	public static void main(String[] args) { Menu(); }

    public static void Menu() 
    {
    	int choice;
    	int course;
    	String dept;
    	String sid;
    	String classid;

    	while (true) // loop for instructions
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
                            		choice = getTableChoice();
                                    JDBC.viewTable(choice);
                                    break;
                            case 2:
                            		StudentConfig config = getStudentConfig();
                                    JDBC.addStudent(config);
                                    break;
                            case 3:
                            		sid = getSID();
                            		JDBC.deleteStudent(sid); 
                                    break;
                            case 4:
                            		sid = getSID();
                                    JDBC.viewStudentInformation(sid);
                                    break;
                            case 5:
                            		dept = getDeptCode();
                            		course = getCourseNo();
                            		JDBC.returnAllPrereqs(dept, course);
                                    break;
                            case 6:
                            		sid = getSID();
                            		classid = getClassid();
                            		JDBC.enrollStudent(sid, classid);
                                    break;
                            case 7:
                            		sid = getSID();
                            		classid = getClassid();
                            		JDBC.dropStudent(sid, classid);
                                    break;
                            case 8:
                            		classid = getClassid();
                            		JDBC.viewCourseInformationStudentsEnrolled(classid);
                            		break;
                            case 9:
                                    System.out.println("--------------------------------------------------");
                                    JDBC.closeConnection();
                                    JDBC.closeStatement();
                                    JDBC.closeResultSet();
                                    JDBC.closePreparedStatement();
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

    public static String getClassid() 
    {
        System.out.println();
        System.out.print("Enter a CLASSID: ");
        String classid = scan.next();
        System.out.println();
        return classid;
    }

    public static int getTableChoice() 
    {
    	System.out.println();
        System.out.println("View a Table: ");
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

    public static String getSID() 
    {
		System.out.println();
        System.out.print("Enter a SID: ");
        String sid = scan.next();
        System.out.println();
        return sid;
    }

    public static StudentConfig getStudentConfig()
    {
    	StudentConfig temp = new StudentConfig();
        System.out.println();
        System.out.print("Enter an SID: ");
        String sid = scan.next();
        System.out.print("Enter First Name: ");
        String first = scan.next();
        System.out.print("Enter Last Name: ");
        String last = scan.next();
        System.out.print("Enter Student's Status: ");
        String status = scan.next();
        String[] validStatuses = new String[] {"freshman", "sophomore", "junior", "senior", "graduate"};

        if (Arrays.asList(validStatuses).contains(status.toLowerCase()) == false) 
        { 
        	System.out.println();
        	System.out.println("ERROR: Not a valid status, please choose from 'freshman, sophomore, junior, senior, or graduate.'");
        	getStudentConfig();
        }

        System.out.print("Enter Student's Email: ");
        String email = scan.next();
        System.out.println("If the student has a GPA, acceptable values fall between 0.0 - 4.0.");
        System.out.println("If the student does not have a GPA at this time, enter null all lowercase.");
        System.out.print("Enter Student's GPA: "); // can be a valid float or null
        String gpa = scan.next();

        temp.sid = sid;
        temp.first = first;
        temp.last = last;
        temp.status = status;
        temp.email = email;

        if (gpa.equals("null")) {
        	temp.gpa = null;
        	return temp;
        } else if (Float.parseFloat(gpa) >= 0.0 || Float.parseFloat(gpa) <= 4.0) { // check range
        	temp.gpa = gpa;
        	return temp;
        } else {
        	System.out.println();
        	System.out.println("ERROR: GPA is out of range, must be between 0.0 - 4.0.");
        	getStudentConfig();
        }
    	return temp;
    }

    public static String getDeptCode()
    {
		System.out.println();
        System.out.print("Enter a DEPT_CODE: ");
        String dept = scan.next();
        return dept;
    }

    public static int getCourseNo() 
    {
        System.out.print("Enter a COURSE_NO: ");
        int course = scan.nextInt();
        System.out.println();
        return course;
    }
}
