import java.sql.DriverManager;
import java.sql.Connection;

public class JDBC 
{
	Connection con = null;

	private String username = "pwatter1";
	private String password = "pineapple";
	private String url = "jdbc:oracle:thin:@bingsuns.cc.binghamton.edu:1521:xe";

	public static Connection createConnection()
	{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); //load jdbc driver
			con = DriverManager.getConnection(url, username, password);
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
}