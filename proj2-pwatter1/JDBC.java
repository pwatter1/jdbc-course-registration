import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleTypes;

public class JDBC 
{
	public static String query = null;
    public static ResultSet rset = null;
	public static Statement stmt = null;
    public static PreparedStatement pstmt = null;
    public static CallableStatement cstmt = null;
    public static final Connection con = createConnection();

    public static void viewTable(int in) throws SQLException
    {
    	rset = null;
        cstmt = null;

        switch (in) 
        {
        	case 1:
        		cstmt = con.prepareCall("{call proj2_procedures.viewStudentsTable(?)}");
        		break;
        	case 2:
        		cstmt = con.prepareCall("{call proj2_procedures.viewCoursesTable(?)}");
        		break;
        	case 3:
        		cstmt = con.prepareCall("{call proj2_procedures.viewPrerequisitesTable(?)}");
        		break;
        	case 4:
        		cstmt = con.prepareCall("{call proj2_procedures.viewStudentsClasses(?)}");
        		break;
        	case 5:
        		cstmt = con.prepareCall("{call proj2_procedures.viewClassesTable(?)}");
        		break;
        	case 6:
        		cstmt = con.prepareCall("{call proj2_procedures.viewLogsTable(?)}");
        		break;
        	default:
        		System.out.println("You've entered an invalid option.");
                viewTable(in);
        }
        cstmt.registerOutParameter(1, OracleTypes.CURSOR); // for result output
		cstmt.execute(); // execute() bc not modifying table
		rset = (ResultSet) cstmt.getObject(1);
        printResultSetColumnNames();
        printResultSetColumnContents();
    }

    public static void addStudent(StudentConfig config) 
    {
    	cstmt = null;
    	cstmt = con.prepareCall("{call proj2_procedures.addStudent(?,?,?,?,?,?,?)}");
    	cstmt.setString(1, config.sid);
    	cstmt.setString(2, config.first);
    	cstmt.setString(3, config.last);
    	cstmt.setString(4, config.status);
    	if (config.gpa == null) { // student has no GPA yet
    		cstmt.setString(5, null);
    	} else { // provided student's current GPA
    		cstmt.setFloat(5, Float.parseFloat(config.gpa));
    	}
    	cstmt.setString(6, config.email);
    	cstmt.registerOutParameter(7, OracleTypes.VARCHAR); // for return message - success / failure
    	cstmt.executeUpdate(); // executeUpdate() bc modifying table

        System.out.println();
        System.out.println(cstmt.getString(7));
        System.out.println("--------------------------------------------------");
    }

    public static void deleteStudent(String sid) throws SQLException
    {
    	cstmt = null;
    	cstmt = con.prepareCall("{call proj2_procedures.deleteStudent(?, ?)}");
    	cstmt.setString(1, sid);
    	cstmt.registerOutParameter(2, OracleTypes.VARCHAR); // for return message
    	cstmt.executeUpdate(); // executeUpdate() bc  modifying table

    	System.out.println(cstmt.getString(2));
		System.out.println("--------------------------------------------------");
    }

    public static void returnAllPrereqs(String deptCode, int course_no) throws SQLException
    {
    	rset = null;
    	stmt = null;
    	cstmt = null;
    	cstmt = con.prepareCall("{call proj2_procedures.returnAllPrereqs(?,?,?)}");
    	cstmt.setString(1, deptCode);
    	cstmt.setInt(2, course_no);
    	cstmt.registerOutParameter(3, OracleTypes.CURSOR); // for result output
    	cstmt.execute(); // execute() bc not modifying table

		rset = (ResultSet) cstmt.getObject(3);
		printResultSetColumnNames();
		printResultSetColumnContents();

		stmt = con.createStatement();
		query = "truncate table prereqResults"; // reset table
	    pstmt = con.prepareStatement(query);
	    boolean result = pstmt.execute();
    }

    public static void dropStudent(String sid, String classid) throws SQLException
    {
    	cstmt = null;
    	cstmt = con.prepareCall("{call proj2_procedures.dropStudent(?,?,?)}");
    	cstmt.setString(1, sid);
    	cstmt.setString(2, classid);
    	cstmt.registerOutParameter(3, OracleTypes.VARCHAR); // for return message - success / failure
    	cstmt.executeUpdate(); // executeUpdate() bc modifying table

    	System.out.println();
    	System.out.println(cstmt.getString(3));
    	System.out.println("--------------------------------------------------");
    }

    public static void enrollStudent(String sid, String classid) throws SQLException
    {
    	cstmt = null;
    	cstmt = con.prepareCall("{call proj2_procedures.enrollStudent(?,?,?)}");
    	cstmt.setString(1, sid);
    	cstmt.setString(2, classid);
    	cstmt.registerOutParameter(3, OracleTypes.VARCHAR); // for return message - success / failure
    	cstmt.executeUpdate(); // executeUpdate() bc modifying table

    	System.out.println();
    	System.out.println(cstmt.getString(3));
    	System.out.println("--------------------------------------------------");
    }

	public static void viewCourseInformationStudentsEnrolled(String classid) throws SQLException
	{
		rset = null;
    	cstmt = null;
    	cstmt = con.prepareCall("{call proj2_procedures.getInfoAndEnrollees(?,?,?)}");
    	cstmt.setString(1, classid);
    	cstmt.registerOutParameter(2, OracleTypes.VARCHAR); // for error message
    	cstmt.registerOutParameter(3, OracleTypes.CURSOR); // for result output
    	cstmt.execute(); // execute() bc not modifying table

    	if (cstmt.getString(2) == null) { // error free
    		rset = (ResultSet) cstmt.getObject(3);
    		printResultSetColumnNames();
    		printResultSetColumnContents();
    	} else {
    		System.out.println("ERROR: " + cstmt.getString(2));
    		System.out.println("--------------------------------------------------");
    	}
	}

    public static void viewStudentInformation(String sid) throws SQLException
    {
    	rset = null;
    	cstmt = null;

        query = "SELECT sid, lastname, status FROM students WHERE sid=?";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, sid);
        rset = pstmt.executeQuery(); 

        if (rset.next()) {
                printResultSetColumnNames();
                //printResultSetColumnContents();
                System.out.println(rset.getString(1) + "   " + rset.getString(2) + "   " + rset.getString(3));
                System.out.println();
        } else { // rset has no value
                System.out.println();
                System.out.println("ERROR: The SID is invalid.");
        } 

    	cstmt = con.prepareCall("{call proj2_procedures.viewStudentsClasses(?,?,?)}");
    	cstmt.setString(1, sid);
    	cstmt.registerOutParameter(2, OracleTypes.VARCHAR); // place for error string
    	cstmt.registerOutParameter(3, OracleTypes.CURSOR); // place for records
    	cstmt.execute(); // execute() bc not modifying table

    	if (cstmt.getString(2) == null) { // success
    		rset = null;
    		rset = (ResultSet) cstmt.getObject(3);
    		printResultSetColumnNames();
    		printResultSetColumnContents();
    	} else { // error
    		System.out.println("ERROR: " + cstmt.getString(2));
    		System.out.println("--------------------------------------------------");
    	}    	
    }


    public static void printResultSetColumnContents() throws SQLException
    {
    	ResultSetMetaData rsmd = rset.getMetaData(); // result set meta info

        int num = rsmd.getColumnCount();

        // rsets are 1 indexed instead of 0 indexed like C arrays

    	while (rset.next()) 
    	{
	        for (int i = 1; i <= num; i++) { 
	                if (i > 1) { System.out.print("   "); }
	                System.out.print(rset.getString(i));
	        }
	        System.out.println();
        }
        System.out.println();
        System.out.println("--------------------------------------------------");
    }

    public static void printResultSetColumnNames() throws SQLException
    {
        ResultSetMetaData rsmd = rset.getMetaData();  // result set meta info

        int num = rsmd.getColumnCount();

        // rsets are 1 indexed instead of 0 indexed like C arrays

        for (int i = 1; i <= num; i++) 
        {
                if (i > 1) { System.out.print(" | "); }
                System.out.print(rsmd.getColumnName(i));
        }

        System.out.println();
    }

	public static Connection createConnection()
	{
		Connection temp = null;
		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		    OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	        ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:ACAD111");
	        temp = ds.getConnection("pwatter1", "pineapple1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	public static void closeConnection() 
	{
		if (con != null) 
		{ 
			try {
				con.close();
			} catch (Exception e) {
				System.err.println("Error: Exception closing jdbc conneciton.");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public static void closeResultSet() 
	{
		if (rset != null) 
		{ 
			try {
				rset.close();
			} catch (Exception e) {
				System.err.println("Error: Exception closing jdbc result set.");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public static void closeStatement() 
	{
		if (stmt != null) 
		{ 
			try {
				stmt.close();
			} catch (Exception e) {
				System.err.println("Error: Exception closing statement.");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public static void closeCallableStatement() 
	{
		if (cstmt != null) 
		{ 
			try {
				cstmt.close();
			} catch (Exception e) {
				System.err.println("Error: Exception closing callable statement.");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public static void closePreparedStatement() 
	{
		if (pstmt != null) 
		{ 
			try {
				pstmt.close();
			} catch (Exception e) {
				System.err.println("Error: Exception closing prepared statement.");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
}