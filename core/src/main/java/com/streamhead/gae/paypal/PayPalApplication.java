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

import java.util.Date;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.streamhead.gae.paypal.ipn.IPNMessage;
import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

public class PayPalApplication extends Application {

  static {
    ObjectifyService.register(IPNMessage.class);
  }
	
	private static final long serialVersionUID = 1L;
	protected static final PayPalEnvironment environment = PayPalEnvironment.SANDBOX;

	Table ipnTable = new Table("IPN messages"); 
	Label ipnDetail = new Label("Select message for detail", Label.CONTENT_PREFORMATTED);
	
	public Window buildMainLayout() {
		Window mainWindow = new Window("PayPal Test Application");

		mainWindow.addComponent(new Label("Environment: " + environment));

		ipnTable.setSizeFull();
		ipnTable.setSelectable(true);
		ipnTable.setImmediate(true);
		ipnTable.addContainerProperty("Date", Date.class, null);
		ipnTable.addContainerProperty("Validated", Boolean.class, Boolean.FALSE);
		ipnTable.addContainerProperty("Transaction Type", String.class, "");
		ipnTable.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				IPNMessage msg = (IPNMessage)ipnTable.getValue();
				if(msg != null)
					ipnDetail.setValue(msg.getFullMessage().getValue());
			}
		});
		mainWindow.addComponent(ipnTable);
		
		mainWindow.addComponent(ipnDetail);
		
		mainWindow.addComponent(new Button("refresh", new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				loadIPNMessages();
			}
		}));

		return mainWindow;
	}
	
	@Override
	public void init() {
		setMainWindow(buildMainLayout());
		
		loadIPNMessages();
	}
	
	private void loadIPNMessages() {
		ipnTable.removeAllItems();

		Query<IPNMessage> messages = ObjectifyService.begin().query(IPNMessage.class);
		for(IPNMessage m : messages)
		{
			ipnTable.addItem(new Object[] { m.getDate(), m.isValidated(), m.getTransactionType().toString() }, m);
		}
	}

}
