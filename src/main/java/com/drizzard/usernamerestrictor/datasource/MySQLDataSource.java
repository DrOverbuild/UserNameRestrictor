package com.drizzard.usernamerestrictor.datasource;

import com.drizzard.usernamerestrictor.UsernameRestrictor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

/**
 * Created by jasper on 9/10/16.
 */
public class MySQLDataSource implements DataSource {
	public Connection connection;
	JavaPlugin plugin;

	public MySQLDataSource(JavaPlugin plugin) throws SQLException {
		this.plugin = plugin;

		setupMySQL();
	}

	@Override
	public String getUsernameIgnoreCase(String playerName) {
		String toReturn = null;

		try {
			PreparedStatement sql = connection.prepareStatement("SELECT `username` FROM `players` WHERE username = ?");
			sql.setString(1, playerName);
			ResultSet result = sql.executeQuery();

			while (result.next()) {
				String username = result.getString(1);

				if (playerName.equalsIgnoreCase(username) && !username.equals(playerName)) {
					toReturn = username;
				}

			}

			result.close();
			sql.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return toReturn;
	}

	@Override
	public synchronized void addUsername(String username) {
		try {
			PreparedStatement sql = connection.prepareStatement("INSERT INTO players VALUES (?);");

			sql.setString(1, username);

			sql.executeUpdate();

			sql.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void removeUsername(String username) {
		try {
			PreparedStatement sql = connection.prepareStatement("DELETE FROM `players` WHERE `username`=?;");

			sql.setString(1, username);

			sql.executeUpdate();

			sql.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized boolean containsUsername(String username) {
		try {
			PreparedStatement sql = connection.prepareStatement("SELECT * FROM `players` WHERE username=?;");

			sql.setString(1, username);

			ResultSet resultSet = sql.executeQuery();

			boolean containsPlayer = resultSet.next();

			sql.close();
			resultSet.close();

			return containsPlayer;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void setupMySQL() throws SQLException {
		openConnection();
		setupTable();
	}

	@Override
	public void cleanupConnection() {
		try {
			if (connection != null && !connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public synchronized void openConnection() throws SQLException {
		String host = UsernameRestrictor.getConfigManager().getMySQLHost();
		int port = UsernameRestrictor.getConfigManager().getMySQLPort();
		String database = UsernameRestrictor.getConfigManager().getMySQLDatabase();
		String user = UsernameRestrictor.getConfigManager().getMySQLUsername();
		String pass = UsernameRestrictor.getConfigManager().getMySQLPassword();

		connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, pass);

	}

	public synchronized void setupTable() {
		try {
			PreparedStatement sql = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `players` (username VARCHAR(20));");

			sql.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
