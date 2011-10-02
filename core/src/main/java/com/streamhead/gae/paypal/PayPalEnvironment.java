package com.streamhead.gae.paypal;

public enum PayPalEnvironment {

	LIVE(""),
    SANDBOX("sandbox.");

    private final String urlModifier;

    private PayPalEnvironment(String urlModifier) {
    	this.urlModifier = urlModifier;
    }

    public String getUrlModifier() {
    	return urlModifier;
    }
}
