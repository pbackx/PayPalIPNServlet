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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.streamhead.gae.paypal.PayPalEnvironment;

public class IPNServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(IPNServlet.class.getName());
	private static final PayPalEnvironment environment = PayPalEnvironment.SANDBOX;
	private static final String verified = "VERIFIED";
	
    private final Objectify ofy;
    
    public IPNServlet() {
    	ObjectifyService.register(IPNMessage.class);
    	ofy = ObjectifyService.begin();
    }
    
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("IPN received");

		final IPNNotificationValidation validation = new IPNNotificationValidation(req);
		IPNMessage message = new IPNMessageParser(req, validate(validation)).parse();
		log.info("parsed message: " + message);
		ofy.put(message);
	}
	
	private boolean validate(IPNNotificationValidation validation) {
        StringBuffer nvpString = new StringBuffer();
        String encoding = "UTF-8";
        try {
        	nvpString.append("cmd=" + validation.getCommand() + "&");
            for(Map.Entry<String, String> e : validation.getNVPRequest().entrySet()) {
                nvpString.append(e.getKey() + "=" + URLEncoder.encode(e.getValue(), encoding));
                nvpString.append("&");
            }
        } catch (UnsupportedEncodingException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        String response = null;
        try {
        	log.fine("Sending request " + nvpString.toString());
        	log.fine("To URL " + validation.getUrl(environment));
            response = getResponse(new URL(validation.getUrl(environment)), nvpString.toString());
        } catch (MalformedURLException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        if (response != null) {
        	log.fine("received response: " + response);
        	
        	return response.equals(verified);
        }
        
        return false;
	}
	
	public String getResponse(URL url, String msg) throws MalformedURLException {
		URLConnection connection;
		StringBuffer response = new StringBuffer();
		try {
			connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);

			/* write request */
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(msg);
			writer.flush();
			writer.close();

			/* read response */
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
		} catch (IOException ex) {
			log.log(Level.SEVERE, null, ex);
		}
		
		/* return response */
		return response.toString();
	}
}