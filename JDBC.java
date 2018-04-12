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
        stmt = con.createStatement();

        // use a regular statement bc we dont need dynamic params
        // use executeQuery bc we expect it to return result sets

        switch (in) 
        {
        	case 1:
        		rset = stmt.executeQuery("SELECT * FROM students");
        		break;
        	case 2:
        		rset = stmt.executeQuery("SELECT * FROM courses");
        		break;
        	case 3:
        		rset = stmt.executeQuery("SELECT * FROM prerequisites");
        		break;
        	case 4:
        		rset = stmt.executeQuery("SELECT * FROM classes");
        		break;
        	case 5:
        		rset = stmt.executeQuery("SELECT * FROM enrollments"); 
        		break;
        	case 6:
        		rset = stmt.executeQuery("SELECT * FROM logs"); 
        		break;
        	default:
        		System.out.println("You've entered an invalid option.");
                viewTable();
        }

        printResultSetColumnNames();
        printResultSetColumnContents();
    }

    public static viewStudentInformation(String sid) 
    {
    	rset = null;
    	cstmt = null;
    	cstmt = con.prepareCall("{call proj2_procedures.viewStudentInformation(?,?,?)}");
    	cstmt.setString(1, sid);
    	cstmt.registerOutParameter(2, OracleTypes.VARCHAR); // place for error string
    	cstmt.registerOutParameter(3, OracleTypes.CURSOR); // place for records
    	cstmt.execute();

    	if (cstmt.getString(2) == null) { // success
    		printResultSetColumnNames();
    		printResultSetColumnContents();
    	} else { // error
    		System.out.println("ERROR: " + cstmt.getString(2));
    	}    	
    }


    public static void printResultSetColumnContents() 
    {
    	ResultSetMetaData rsmd = rset.getMetaData();

        int num = rsmd.getColumnCount();

    	while (rset.next()) 
    	{
	        for (int i = 1; i <= num; i++) {
	                if (i > 1) { System.out.print("   "); }
	                System.out.print(rset.getString(i));
	        }
	        System.out.println();
        }
        System.out.println();
        System.out.print("--------------------------------------------------");
    }

    public static void printResultSetColumnNames() 
    {
        ResultSetMetaData rsmd = rset.getMetaData();

        int num = rsmd.getColumnCount();

        for (int i = 1; i <= num; i++) 
        {
                if (i > 1) { System.out.print(" | "); }
                System.out.print(rsmd.getColumnName(i));
        }

        System.out.println();
    }

	public static Connection createConnection()
	{
		Connection con = null;
		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		    OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	        ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:ACAD111");
	        con = ds.getConnection("pwatter1", "pineapple1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
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
		if (rs != null) 
		{ 
			try {
				rs.close();
			} catch (Exception e) {
				System.err.println("Error: Exception closing jdbc result set.");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public static void closeStatement() 
	{
		if (st != null) 
		{ 
			try {
				st.close();
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

