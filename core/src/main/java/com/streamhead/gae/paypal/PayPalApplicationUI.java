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
package com.streamhead.gae.paypal;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;

import com.streamhead.gae.paypal.ipn.IPNMessage;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class PayPalApplicationUI extends UI {

	private static final long serialVersionUID = 1L;
	protected static final PayPalEnvironment environment = PayPalEnvironment.SANDBOX;

	Table ipnTable = new Table("IPN messages"); 
	Label ipnDetail = new Label("Select message for detail", ContentMode.PREFORMATTED);
	
	public Component buildMainView() {
		VerticalLayout layout = new VerticalLayout();

		layout.addComponent(new Label("Environment: " + environment));

		ipnTable.setSizeFull();
		ipnTable.setSelectable(true);
		ipnTable.setImmediate(true);
		ipnTable.addContainerProperty("Date", Date.class, null);
		ipnTable.addContainerProperty("Validated", Boolean.class, Boolean.FALSE);
		ipnTable.addContainerProperty("Transaction Type", String.class, "");
		ipnTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				IPNMessage msg = (IPNMessage)ipnTable.getValue();
				if(msg != null)
					ipnDetail.setValue(msg.getFullMessage().getValue());
			}
		});
		layout.addComponent(ipnTable);
		
		layout.addComponent(ipnDetail);
		
		layout.addComponent(new Button("refresh", new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				loadIPNMessages();
			}
		}));

		return layout;
	}
	
	@Override
	public void init(VaadinRequest request) {
		setContent(buildMainView());
		
		loadIPNMessages();
	}
	
	private void loadIPNMessages() {
		ipnTable.removeAllItems();

		for(IPNMessage m : ofy().load().type(IPNMessage.class).list())
		{
			ipnTable.addItem(new Object[] { m.getDate(), m.isValidated(), m.getTransactionType().toString() }, m);
		}
	}

}
