package connect;
import java.sql.*;  

public class ConnectAccess {
	public static void connection(String[] args) throws SQLException { 
		try { 
			System.out.println("正在连接数据库.....");
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			DriverManager.getConnection("jdbc:odbc:con_DB");
			System.out.println("已经连接到数据库.....");
		} catch (Exception ex) {}  
	} 

}
