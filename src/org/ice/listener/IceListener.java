package org.ice.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.ice.Config;

public class IceListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Config.load(sce.getServletContext());
	}

}
