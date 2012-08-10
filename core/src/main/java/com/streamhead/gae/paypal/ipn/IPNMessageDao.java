package com.streamhead.gae.paypal.ipn;

import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyOpts;
import com.googlecode.objectify.util.DAOBase;

/**
 * This is a straight copy from
 * https://code.google.com/p/objectify-appengine/wiki
 * /BestPractices#Use_Pythonic_Transactions
 * 
 * @author Peter
 * 
 */
public class IPNMessageDao extends DAOBase {

	private static final Logger log = Logger.getLogger(IPNMessageDao.class.getName());

	/** Alternate interface to Runnable for executing transactions */
	public static interface Transactable {
		void run(IPNMessageDao daot);
	}

	/**
	 * Provides a place to put the result too. Note that the result is only
	 * valid if the transaction completes successfully; otherwise it should be
	 * ignored because it is not necessarily valid.
	 */
	abstract public static class Transact<T> implements Transactable {
		protected T result;

		public T getResult() {
			return this.result;
		}
	}

	/** Create a default DAOT and run the transaction through it */
	public static void runInTransaction(Transactable t) {
		IPNMessageDao daot = new IPNMessageDao();
		daot.doTransaction(t);
	}

	/**
	 * Run this task through transactions until it succeeds without an
	 * optimistic concurrency failure.
	 */
	public static void repeatInTransaction(Transactable t) {
		while (true) {
			try {
				runInTransaction(t);
				break;
			} catch (ConcurrentModificationException ex) {
				log.log(Level.WARNING, "Optimistic concurrency failure for " + t, ex);
			}
		}
	}

	/** Starts out with a transaction and session cache */
	public IPNMessageDao() {
		super(new ObjectifyOpts().setSessionCache(true).setBeginTransaction(true));
	}

	/** Adds transaction to whatever you pass in */
	public IPNMessageDao(ObjectifyOpts opts) {
		super(opts.setBeginTransaction(true));
	}

	/**
	 * Executes the task in the transactional context of this DAO/ofy.
	 */
	public void doTransaction(final Runnable task) {
		this.doTransaction(new Transactable() {
			@Override
			public void run(IPNMessageDao daot) {
				task.run();
			}
		});
	}

	/**
	 * Executes the task in the transactional context of this DAO/ofy.
	 */
	public void doTransaction(Transactable task) {
		try {
			task.run(this);
			ofy().getTxn().commit();
		} finally {
			if (ofy().getTxn().isActive())
				ofy().getTxn().rollback();
		}
	}

}
