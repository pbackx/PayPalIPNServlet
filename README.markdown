Introduction
============

This project contains a Google AppEngine project that :

* has a servlet implementing the PayPal IPN "protocol" (Instant Payment Notification)
* persists all IPN messages (and parses them partially)
* uses postback validation
* a minimal Vaadin application to browse the stored IPN messages

Keep in mind that the PayPal sandbox can be pretty slow. So you might get timeouts on validation messaages (GAE has a set 10s max timeout)

Feel free to fork this project and improve it. I'd love to see some cooperation on taming the PayPal beast :)


Getting started
===============

Build and run the example application
-------------------------------------

1. `mvn clean install`
2. `cd war`
3. `mvn appengine:devserver`


History
=======

0.0.2-SNAPSHOT
--------------

Upgrade to Vaadin 7 and AppEngine 1.8. 
*important* The Maven AppEngine plugin is now the one officially supported by Google. The plugin requires Maven 3.1.0 or higher, so you may need to upgrade Maven.


Using the Library in Your Own Projects
======================================

Register the Context Listener
-----------------------------

In our web.xml, make sure you register the IPNContextListener, else you won't be able to persist the messages to the datastore.

	<listener>
	    <listener-class>com.streamhead.gae.paypal.ipn.IPNContextListener</listener-class>
	</listener>


Configure the PayPal environment
--------------------------------

Add in web.xml

	<context-param>
		<param-name>payPalEnvironment</param-name>
		<param-value>SANDBOX</param-value>
	</context-param>
	
Possible values: SANDBOX, LIVE


Configuring the task queue
--------------------------

In your WEB-INF directory, create a queue.xml file to configure the task queue that will handle validation.
Example:

	<?xml version="1.0" encoding="UTF-8"?>
	<queue-entries>
	  <queue>
	    <name>paypal</name>
	    <rate>1/s</rate>
	    <bucket-size>10</bucket-size>
	    <max-concurrent-requests>10</max-concurrent-requests>
		<retry-parameters>
	      <task-retry-limit>10</task-retry-limit>
	      <task-age-limit>1d</task-age-limit>
		  <min-backoff-seconds>10</min-backoff-seconds>
	      <max-backoff-seconds>600</max-backoff-seconds>
	      <max-doublings>2</max-doublings>
	    </retry-parameters>
	  </queue>
	</queue-entries>

Adding the Servlets
-------------------

To process PayPal IPN messages, the task queue is used. This allows for easy and configurable retries. To properly process IPN messages
you will need three servlets. Two are part of this packages, one you will need to implement yourself.

The *IPNServlet* stores incoming IPN messages as is and forwards them to the IPNValidationServlet through a task queue.

	<servlet>
		<servlet-name>IPNServlet</servlet-name>
		<servlet-class>com.streamhead.gae.paypal.ipn.IPNServlet</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>IPNServlet</servlet-name>
		<url-pattern>/ipn</url-pattern>
	</servlet-mapping>
	
The *IPNValidationServlet* tries to validate the message. The number and frequency of retries is configured in the queue. After successful
validation, the servlet starts the processing task.

	<servlet>
		<servlet-name>IPNValidationServlet</servlet-name>
		<servlet-class>com.streamhead.gae.paypal.ipn.IPNValidationServlet</servlet-class>
		<init-param>
			<description>Task to launch after validation</description>
			<param-name>processingTask</param-name>
			<param-value>/ipn/process</param-value>
		</init-param>
	</servlet>
	<servlet-mapping>
		<servlet-name>IPNValidationServlet</servlet-name>
		<url-pattern>/ipn/validate</url-pattern>
	</servlet-mapping>
	
The *processing servlet* should be implemented by you. To get you started, this project contains a dummy servlet.
 
	<servlet>
		<servlet-name>IPNProcessingServlet</servlet-name>
		<servlet-class>com.streamhead.gae.paypal.ipn.IPNProcessingServlet</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>IPNProcessingServlet</servlet-name>
		<url-pattern>/ipn/process</url-pattern>
	</servlet-mapping>
 
 
 Maven Repository
 ================
 
 Currently this project is not released or deployed to a public repository. Please let me know if you'd like that.
 