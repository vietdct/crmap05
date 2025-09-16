package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLconfig {
	// tạo hàm mở kết nối tới CSDL 
	public static Connection getConnection() {
		Connection connection = null;
		try {
			String url ="jdbc:mysql://localhost:3307/crmapp";
			String username ="root";
			String password ="admin123";
			//khai báo driver sử dụng là của MySQL (Driver class name tên_driver)
			Class.forName("com.mysql.cj.jdbc.Driver");
			//khai báo thông tin đường dẫn MySQL đường dẫn sẽ kết nối tới Database (url mysql jdbc connection )
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			System.out.println("Lỗi kết nối CSDL"+e.getMessage());;
		}
		return connection;
	}
}
