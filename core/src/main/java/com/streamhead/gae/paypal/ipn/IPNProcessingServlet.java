package com.streamhead.gae.paypal.ipn;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class IPNProcessingServlet extends IPNServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(IPNProcessingServlet.class.getName());
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final String idStr  = req.getParameter("id");
		if(idStr == null || "".equals(idStr)) {
			log.severe("no id in validation request");
			resp.setStatus(204); // return in range of 2xx so AppEngine doesn't retry
			return;
		}
		
		try {
			final long id = Long.parseLong(idStr);

			final Objectify ofy = ObjectifyService.begin(); 
			final IPNMessage message = ofy.find(IPNMessage.class, id);
					
			//This is where your code goes to handle the IPN message
			log.fine("now processing message with txn_id " + message.getTxnId());
			
		} catch (NumberFormatException e) {
			log.log(Level.SEVERE, "unparsable id in validation request", e);
			resp.setStatus(204); // return in range of 2xx so AppEngine doesn't retry
			return;
		}
	}
}
