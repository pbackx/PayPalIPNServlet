package com.streamhead.gae.paypal.ipn;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.VoidWork;
import com.streamhead.gae.paypal.PayPalEnvironment;

public class IPNValidationServlet extends IPNServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(IPNValidationServlet.class.getName());

	private PayPalEnvironment environment = PayPalEnvironment.SANDBOX;
	private String processingTask;
	
	@Override
	public void init() throws ServletException {
		String ppeStr = getServletContext().getInitParameter("payPalEnvironment");
		if(ppeStr != null) {
			environment = PayPalEnvironment.valueOf(ppeStr);
			log.info("PayPal environment is " + environment);
		}
		
		String processingTask = getServletConfig().getInitParameter("processingTask");
		this.processingTask = processingTask == null ? "/ipn/process" : processingTask;
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		
		final String idStr  = req.getParameter("id");
		if(idStr == null || "".equals(idStr)) {
			log.severe("no id in validation request");
			resp.setStatus(204); // return in range of 2xx so AppEngine doesn't retry
			return;
		}
		
		try {
			final long id = Long.parseLong(idStr);
			ofy().transact(new VoidWork() {
				@Override
				public void vrun() {
					final IPNMessage message = ofy().load().key(Key.create(IPNMessage.class, id)).now();
					
					// Check for duplicate txn_id 
					// We can't do this non-ancestor query inside the transaction, so this isn't 100% safe
					// If you know of a way to fix this, please let me know
					if(message.getTxnId() != null && !"".equals(message.getTxnId())) {
						final int count = ofy().transactionless().load().type(IPNMessage.class).filter("txnId", message.getTxnId()).count();
						if(count > 1) {
							log.severe("duplicate message found with txn_id " + message.getTxnId());
							resp.setStatus(204); // return in range of 2xx so AppEngine doesn't retry
							return;
						}
					}
					
					// validate the message with PayPal
					final IPNNotificationValidation validation = new IPNNotificationValidation(message);
					
					if(validation.validate(environment)) {
						message.setValidated(true);
						ofy().save().entity(message);

						//add to queue for final processing
						Queue queue = QueueFactory.getQueue("paypal");
						queue.add(withUrl(processingTask).param("id", String.valueOf(message.getId())));
					}					
				}
			});
		} catch (NumberFormatException e) {
			log.log(Level.SEVERE, "unparsable id in validation request", e);
			resp.setStatus(204); // return in range of 2xx so AppEngine doesn't retry
			return;
		}
	}
	
}
