package com.smarker.init;

import javax.servlet.ServletContextEvent;

public class init implements javax.servlet.ServletContextListener {
	
	//executes this functions when the server is initialized
	public void contextInitialized(ServletContextEvent arg0) {
		MongoDB.startDB();
		JWT.generateKey();
	}
	
	public void contextDestroyed(ServletContextEvent arg0) {

	}

}
