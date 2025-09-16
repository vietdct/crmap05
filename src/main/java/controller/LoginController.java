package controller;

import java.util.List;
import java.io.IOException;
import java.net.CookieHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.util.LimitedInputStream;

import com.mysql.cj.Query;

import config.MySQLconfig;
import entity.User;

@WebServlet (name = "loginController" , urlPatterns = {"/login"})
public class LoginController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = "";
		String password ="";
		
		Cookie[] cookie = req.getCookies();
		for(int i = 0 ; i<cookie.length; i++) {
			String cookieName = cookie[i].getName();
			String cookieValue = cookie[i].getValue();
			if(cookieName.equals("email")) {
				email = cookieName;
			}
			if(cookieName.equals("password")) {
				password = cookieValue;
			}
		}
		req.setAttribute("email",email);
		req.setAttribute("password",password);
		
		req.getRequestDispatcher("login.jsp").forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		boolean rememberCheck = req.getParameter("remember") != null;
		
		System.out.println("Kiem tra :" +email +"-" +password +" - "+rememberCheck);
		
		//Chuẩn bị câu truy vấn Query
		String query ="SELECT *\n"
					+ "FROM users u\n"
					+ "WHERE u.email ='"+ email +"' AND u.password ='"+ password +"'";
		
		//Mở kết nối database
		Connection connection = MySQLconfig.getConnection();
		try {
			//Truyền câu Query đã chuẩn bị vào kết nối
			PreparedStatement statement = connection.prepareStatement(query);
			
			//ResultSet đại diện cho những kết quả Query truy vấn được
			ResultSet resultSet = statement.executeQuery();
			//Tạo listUser chứa list user truy vấn
			List<User> listUser = new ArrayList<User>();
			//Duyệt từng ptử ResultSet truy vấn được trong SQL
			while (resultSet.next()) {
				User user = new User();
				user.setId(resultSet.getInt("id")); //tên phải trùng với cột bên mysql
				user.setEmail(resultSet.getString("email"));
				//user.setPassword(resultSet.getString("password")); không lưu pass bị lộ 
				user.setFullName(resultSet.getString("fullname"));
				user.setPhone(resultSet.getString("phone"));
				user.setFirstName(resultSet.getString("first_name"));
				user.setLastName(resultSet.getString("last_name"));
				user.setIdRole(resultSet.getInt("id_role"));
				
				listUser.add(user);
			}
			if(listUser.size()>0) {
				System.out.println("Login Success");
				Cookie ckRoleCookie = new Cookie("role", listUser.get(0).getIdRole()+"");
				resp.addCookie(ckRoleCookie);
				if(rememberCheck== true) {
					//Tạo 2 ck có tên là email; password
					Cookie ckUsername = new Cookie("email", email);
					Cookie ckPassword = new Cookie("password", password);
					//Set tuổi thọ cho ck
					ckUsername.setMaxAge(60*60*24);
					ckPassword.setMaxAge(60*60*24);
					//Sever tạo 2 cookie và lưu phía client
					resp.addCookie(ckPassword);
					resp.addCookie(ckUsername);

				}
			}else {
				System.out.println("Đăng nhập thất bại");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		}
		
		
}
