import java.sql.DriverManager;
import java.sql.Connection;

public class JDBC 
{
	Connection connection = null;

	String username = "";
	String password = "";
	String url = "jdbc:oracle:bingsuns@localhost:1521:xe";

	public static Connection createConnection()
	{
		connection = DriverManager.getConnection("")
	}

	public static void closeConnection() 
	{
		if (connection != null) { connection.close(); } 
	}
}