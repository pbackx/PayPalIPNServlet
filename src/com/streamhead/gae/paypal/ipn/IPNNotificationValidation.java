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
*/
package com.streamhead.gae.paypal.ipn;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.streamhead.gae.paypal.PayPalEnvironment;

public class IPNNotificationValidation {

	private static final long serialVersionUID = 1L;

	private Map<String, String> nvpReq = new HashMap<String,String>();
	private String cmd = "_notify-validate";
	
	@SuppressWarnings("unchecked")
	public IPNNotificationValidation(HttpServletRequest request) {
		for(Map.Entry<String, String[]> param :  (Set<Map.Entry<String, String[]>>)request.getParameterMap().entrySet()) {
			nvpReq.put(param.getKey(), param.getValue()[0]);
		}
	}

	public String getCommand() {
		return cmd;
	}
	
	public Map<String, String> getNVPRequest() {
		return nvpReq;
	}

	public String getUrl(PayPalEnvironment environment) {
		return "https://www." + environment.getUrlModifier() + "paypal.com/cgi-bin/webscr";
	}
}
