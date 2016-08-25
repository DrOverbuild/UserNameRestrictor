package com.drizzard.usernamerestrictor.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLHandler {

	static SettingsManager settings = SettingsManager.getInstance();
	
	public static Connection connection;
	
	public static void setupMySQL() {
		if (settings.getSQLData().getBoolean("MySQL.Enabled")) {
			
			openConnection();
			setupTable();
			
		}
	}
	
	public static void cleanupConnection() {
		try {
			if (connection != null && !connection.isClosed()) connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public synchronized static void openConnection() {
		try {
			
			String ip = settings.getSQLData().getString("MySQL.IP");
			String port = settings.getSQLData().getString("MySQL.Port");
			String database = settings.getSQLData().getString("MySQL.Database");
			String user = settings.getSQLData().getString("MySQL.User");
			String pass = settings.getSQLData().getString("MySQL.Password");
			
			connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database, user, pass);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public synchronized static void setupTable() {
		try {
			PreparedStatement sql = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `knownUsernames` (Username VARCHAR(20));");
		
			sql.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static Boolean usernamesContainsUsername(String username) {
		try {
			PreparedStatement sql = connection.prepareStatement("SELECT * FROM `knownUsernames` WHERE username=?;");
			
			sql.setString(1, username);
			
			ResultSet resultSet = sql.executeQuery();
			
			boolean containsPlayer = resultSet.next();
			
			sql.close();
			resultSet.close();
			
			return containsPlayer;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public synchronized static void addUsername(String username) {
		try {
			PreparedStatement sql = connection.prepareStatement("INSERT INTO `knownUsernames` (`username`) VALUES ('?');");
			
			sql.setString(1, username);
			
			sql.executeUpdate();
			
			sql.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static void removeUsername(String username) {
		try {
			PreparedStatement sql = connection.prepareStatement("DELETE FROM `knownUsernames` WHERE `username`=?;");
			
			sql.setString(1, username);
			
			sql.executeUpdate();
			
			sql.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
