package com.streamhead.gae.paypal.ipn;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class IPNMessageParserTest {

	IPNMessageParser parser;
	
	@Test
	public void testUnknownTransactionType() throws Exception {
		final Map<String, String> nvp = new HashMap<String, String>();
		
		nvp.put("txn_type", "unknown");
		parser = new IPNMessageParser(nvp, true);
		
		final IPNMessage message = parser.parse();
		
		assertThat(message.getTransactionType(), nullValue());
	}
	
}
