/*
 *  Copyright (C) 2010 Pete Reisinger <p.reisinger@gmail.com>.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package paypalnvp.core;

import java.io.Serializable;
import java.net.MalformedURLException;

/**
 * Used for sending request and returning response
 *
 * @author Pete Reisinger <p.reisinger@gmail.com>
 */
public interface Transport extends Serializable {

	/**
	 * Sends request (msg attribute) to the specified url and returns response
	 * as a string
	 *
	 * @param urlString	url where to send the request
	 * @param msg		request message to be sent
	 * @return			response message
	 * @throws MalformedURLException
	 */
	String getResponse(String urlString, String msg)
			throws MalformedURLException;
}
