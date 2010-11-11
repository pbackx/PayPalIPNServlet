package com.streamhead.gae.paypal;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Label;

public class SubscribeButton extends Label {
	private static final long serialVersionUID = 1L;

	private static final String PAYPAL_FORM = 
		"<form action=\"https://www.%spaypal.com/cgi-bin/webscr\" method=\"post\">" +
		"<input type=\"hidden\" name=\"cmd\" value=\"_s-xclick\">" +
		"<input type=\"hidden\" name=\"hosted_button_id\" value=\"P55V7PXR33ADA\">" +
		"<input type=\"hidden\" name=\"custom\" value=\"%s\">" +
		"<table>" +
		"<tr><td><input type=\"hidden\" name=\"on0\" value=\"\"></td></tr><tr><td><select name=\"os0\">" +
		"<option value=\"Basis\">Basis : €5,00EUR - monthly</option>" +
		"<option value=\"Normaal\">Normaal : €10,00EUR - monthly</option>" +
		"<option value=\"Onbeperkt\">Onbeperkt : €25,00EUR - monthly</option>" +
		"</select> </td></tr>" +
		"</table>" +
		"<input type=\"hidden\" name=\"currency_code\" value=\"EUR\">" +
		"<input type=\"image\" src=\"https://www.sandbox.paypal.com/nl_NL/BE/i/btn/btn_subscribe_LG.gif\" border=\"0\" name=\"submit\" alt=\"PayPal, de veilige en complete manier van online betalen.\">" +
		"<img alt=\"\" border=\"0\" src=\"https://www.sandbox.paypal.com/en_US/i/scr/pixel.gif\" width=\"1\" height=\"1\">" +
		"</form>";

	public SubscribeButton(PayPalEnvironment environment, String custom) {
		super(
				String.format(
						PAYPAL_FORM, 
						environment.getUrlModifier(),
						custom
				),
				Label.CONTENT_XHTML
		);
		setHeight(75, Sizeable.UNITS_PIXELS);
	}
}
