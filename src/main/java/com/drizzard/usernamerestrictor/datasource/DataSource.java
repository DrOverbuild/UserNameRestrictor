package com.drizzard.usernamerestrictor.datasource;

/**
 * Created by jasper on 9/10/16.
 */
public interface DataSource {

	String getUsernameIgnoreCase(String username);

	void addUsername(String username);

	void removeUsername(String username);

	boolean containsUsername(String username);

	void cleanupConnection();
}
