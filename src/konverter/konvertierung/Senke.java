/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.konvertierung;

import java.io.IOException;

import konverter.daten.Item;
import konverter.exception.KonverterException;

public interface Senke {
	
	void next(Item token) throws IOException, KonverterException;
	
}
