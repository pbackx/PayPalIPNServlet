package com.streamhead.gae.paypal;

import com.vaadin.ui.Label;

public class UnsubscribeButton extends Label {
	private static final long serialVersionUID = 1L;
	
	private static final String HTML = 
		"<A HREF=\"https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_subscr-find&alias=H89YYWGEMFGNQ\">" +
		"<IMG SRC=\"https://www.sandbox.paypal.com/nl_NL/BE/i/btn/btn_unsubscribe_LG.gif\" BORDER=\"0\">" +
		"</A>";

}
