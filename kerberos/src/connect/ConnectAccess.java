package connect;
import java.sql.*;  

public class ConnectAccess {
	public static void connection(String[] args) throws SQLException { 
		try { 
			System.out.println("�����������ݿ�.....");
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			DriverManager.getConnection("jdbc:odbc:con_DB");
			System.out.println("�Ѿ����ӵ����ݿ�.....");
		} catch (Exception ex) {}  
	} 

}
