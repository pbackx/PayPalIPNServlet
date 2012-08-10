/*
 * Copyright 2010 Peter Backx, streamhead.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * The getResponse code was re-used from http://paypal-nvp.sourceforge.net/
 * Thanks for making this code public
 */
package com.streamhead.gae.paypal.ipn;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

public class IPNServlet extends HttpServlet {

	private static final long serialVersionUID = 2L;
	private static final Logger log = Logger.getLogger(IPNServlet.class.getName());

	protected final IPNMessageDao dao = new IPNMessageDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.severe("GET called");
		resp.setStatus(204);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("IPN received");

		//store the unvalidated message
		final IPNMessage message = new IPNMessageParser(nvp(req),	false).parse();
		dao.doTransaction(new IPNMessageDao.Transactable() {
			@Override
			public void run(IPNMessageDao daot) {
				daot.ofy().put(message);
			}
		});
		
		//add to queue for validation
		Queue queue = QueueFactory.getQueue("paypal");
		queue.add(withUrl("/ipn/validate").param("id", String.valueOf(message.getId())));
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, String> nvp(HttpServletRequest req) {
		Map<String, String[]> params = (Map<String, String[]>) req
				.getParameterMap();
		Map<String, String> nvp = new HashMap<String, String>();
		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			String value = "";
			for (int i = 0; i < entry.getValue().length; i++) {
				value += entry.getValue()[i];
				if (i < entry.getValue().length - 1)
					value += ",";
			}
			nvp.put(entry.getKey(), value);
		}
		return nvp;
	}
}
