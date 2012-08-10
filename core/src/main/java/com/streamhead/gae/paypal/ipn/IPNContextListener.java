package com.streamhead.gae.paypal.ipn;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

public class IPNContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ObjectifyService.register(IPNMessage.class);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
