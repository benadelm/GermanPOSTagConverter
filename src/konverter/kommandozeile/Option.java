/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.kommandozeile;

import konverter.Kommandozeile;

public interface Option {
	
	String getBeschreibung();
	
	boolean wertUebernehmen(Kommandozeile kommandozeile, String wert);
	
}
