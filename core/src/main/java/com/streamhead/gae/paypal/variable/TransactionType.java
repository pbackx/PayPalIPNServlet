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
package com.streamhead.gae.paypal.variable;

public enum TransactionType {

	adjustment, 
	cart,
	express_checkout,
	masspay,
	merch_pmt,
	new_case,
	recurring_payment,
	recurring_payment_profile_created,
	send_money,
	subscr_cancel,
	subscr_eot,
	subscr_failed,
	subscr_modify,
	subscr_payment,
	subscr_signup,
	virtual_terminal,
	web_accept;
	
}
