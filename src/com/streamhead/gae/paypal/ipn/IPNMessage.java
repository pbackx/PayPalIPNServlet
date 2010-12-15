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

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Id;

import com.google.appengine.api.datastore.Text;
import com.streamhead.gae.paypal.variable.PaymentStatus;
import com.streamhead.gae.paypal.variable.TransactionType;

public class IPNMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private Date date = new Date();
	private boolean validated = false;
	private Text fullMessage;
	private TransactionType transactionType;
	private PaymentStatus paymentStatus;
	private String mcGross;
	private String mcCurrency;

	private IPNMessage() { }
	
	public Date getDate() {
		return date;
	}
	
	public Text getFullMessage() {
		return fullMessage == null ? new Text("") : fullMessage;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}
	
	public boolean isValidated() {
		return validated;
	}
	
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}
	
	public String getMcGross() {
		return mcGross;
	}

	public String getMcCurrency() {
		return mcCurrency;
	}

	public static class Builder {
		private IPNMessage message = new IPNMessage();
		
		public Builder(Map<String, String> nvp) {
			StringBuffer msg = new StringBuffer();
			for(Map.Entry<String, String> entry : nvp.entrySet()) {
				msg.append(entry.getKey());
				msg.append("=");
				msg.append(entry.getValue());
				msg.append("\n");
			}
			message.fullMessage = new Text(msg.toString());
		}
		
		public Builder transactionType(TransactionType transactionType) {
			this.message.transactionType = transactionType;
			return this;
		}
		
		public Builder paymentStatus(PaymentStatus paymentStatus) {
			this.message.paymentStatus = paymentStatus;
			return this;
		}
		
		public Builder mcGross(String mcGross) {
			this.message.mcGross = mcGross;
			return this;
		}
		
		public Builder mcCurrency(String mcCurrency) {
			this.message.mcCurrency = mcCurrency;
			return this;
		}
		
		public Builder validated() {
			this.message.validated = true;
			return this;
		}
		
		public IPNMessage build() {
			return message;
		}
	}

	@Override
	public String toString() {
		return "IPNMessage [transactionType=" + transactionType + "]";
	}

	public Long getId() {
		return id;
	}
}
