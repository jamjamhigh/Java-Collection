package Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLiteApp {
	public static void main(String[] args) {
		try{
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:C:/SQLiteDB/db.sqlite");
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM table1";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				String one = rs.getString("one");
				int two = rs.getInt("two");
				System.out.println(one + "," + two);
			}
			stmt.close();
			con.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
