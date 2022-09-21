package com.perfiosbank.utils;

import java.sql.*;

public class DatabaseUtils {
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Perfios_Bank", "root", "password");

		return connection;
	}
}
