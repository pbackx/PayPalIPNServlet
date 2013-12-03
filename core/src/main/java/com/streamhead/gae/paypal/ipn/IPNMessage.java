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

import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.streamhead.gae.paypal.variable.PaymentStatus;
import com.streamhead.gae.paypal.variable.TransactionType;

@Entity
public class IPNMessage implements Serializable {

	private static final long serialVersionUID = 3L;

	@Id private Long id;
	@Index private Date date = new Date();
	@Index private boolean validated = false;
	private Text fullMessage;
	@Index private TransactionType transactionType;
	@Index private PaymentStatus paymentStatus;
	@Index private String mcGross;
	@Index private String mcCurrency;
	@Index private String custom;
	@Index private String itemNumber;
	@Index private String txnId;
	@Index private String subscrId;
	@Index private String payerEmail;

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
	
	public void setValidated(boolean validated) {
		this.validated = validated;
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

	public String getCustom() {
		return custom;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public String getTxnId() {
		return txnId;
	}

	public String getSubscrId() {
		return subscrId;
	}

	public String getPayerEmail() {
		return payerEmail;
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
		
		public Builder custom(String value) {
			this.message.custom = value;
			return this;
		}

		public Builder itemNumber(String itemNumber) {
			this.message.itemNumber = itemNumber;
			return this;
		}
		
		public Builder txnId(String txnId) {
			this.message.txnId = txnId;
			return this;
		}
		
		public Builder subscrId(String subscrId) {
			this.message.subscrId = subscrId;
			return this;
		}
		
		public Builder payerEmail(String value) {
			this.message.payerEmail = value;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((custom == null) ? 0 : custom.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((fullMessage == null) ? 0 : fullMessage.hashCode());
		result = prime * result
				+ ((itemNumber == null) ? 0 : itemNumber.hashCode());
		result = prime * result
				+ ((mcCurrency == null) ? 0 : mcCurrency.hashCode());
		result = prime * result + ((mcGross == null) ? 0 : mcGross.hashCode());
		result = prime * result
				+ ((paymentStatus == null) ? 0 : paymentStatus.hashCode());
		result = prime * result
				+ ((subscrId == null) ? 0 : subscrId.hashCode());
		result = prime * result
				+ ((transactionType == null) ? 0 : transactionType.hashCode());
		result = prime * result
				+ ((payerEmail == null) ? 0 : payerEmail.hashCode());
		result = prime * result + ((txnId == null) ? 0 : txnId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IPNMessage other = (IPNMessage) obj;
		if (custom == null) {
			if (other.custom != null)
				return false;
		} else if (!custom.equals(other.custom))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (fullMessage == null) {
			if (other.fullMessage != null)
				return false;
		} else if (!fullMessage.equals(other.fullMessage))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (itemNumber == null) {
			if (other.itemNumber != null)
				return false;
		} else if (!itemNumber.equals(other.itemNumber))
			return false;
		if (mcCurrency == null) {
			if (other.mcCurrency != null)
				return false;
		} else if (!mcCurrency.equals(other.mcCurrency))
			return false;
		if (mcGross == null) {
			if (other.mcGross != null)
				return false;
		} else if (!mcGross.equals(other.mcGross))
			return false;
		if (paymentStatus != other.paymentStatus)
			return false;
		if (subscrId == null) {
			if (other.subscrId != null)
				return false;
		} else if (!subscrId.equals(other.subscrId))
			return false;
		if (transactionType != other.transactionType)
			return false;
		if (txnId == null) {
			if (other.txnId != null)
				return false;
		} else if (!txnId.equals(other.txnId))
			return false;
		if (validated != other.validated)
			return false;
		if (payerEmail == null) {
			if (other.payerEmail != null)
				return false;
		} else if (!payerEmail.equals(other.payerEmail))
			return false;
		return true;
	}
}
