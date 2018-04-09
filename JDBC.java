import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

public class JDBC 
{
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

        public static void closeConnection(Connection con) 
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

        public static void closeResultSet(ResultSet rs) 
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

        public static void closeStatement(Statement st) 
        {
                if (st != null) 
                { 
                        try {
                                st.close();
                        } catch (Exception e) {
                                System.err.println("Error: Exception closing jdbc statement.");
                                e.printStackTrace();
                                System.exit(-1);
                        }
                }
        }
}

