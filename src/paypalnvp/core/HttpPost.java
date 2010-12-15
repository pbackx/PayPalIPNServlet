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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for sending request using http post method and returning response.
 *
 * @author Pete Reisinger <p.reisinger@gmail.com>
 */
public final class HttpPost implements Transport {

	private static final long serialVersionUID = 1L;

	/**
	 * Sends request (msg attribute) to the specified url and returns response
	 * as a string
	 *
	 * @param	urlString	url where to send the request
	 * @param	msg			request message to be sent
	 * @return				response message
	 * @throws	MalformedURLException
	 */
	public String getResponse(String urlString, String msg)
			throws MalformedURLException {

        //System.out.println(urlString);
        //System.out.println(msg);

		/* write request */
		URL url = new URL(urlString);
        URLConnection connection;
        StringBuffer response = new StringBuffer();

        try {
            connection = url.openConnection();
            connection.setDoOutput(true);

            /* write request */
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(msg);
            writer.flush();
            writer.close();

            /* read response */
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                //System.out.println(line);
                response.append(line);
            }
            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(HttpPost.class.getName()).log(Level.SEVERE, null, ex);
        }

		/* return response */
		return response.toString();
	}

    @Override
    public String toString() {
        return "instance of HttpPost class";
    }
}
