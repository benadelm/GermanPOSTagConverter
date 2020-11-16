/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.formate.tsv;

import java.nio.charset.Charset;
import java.nio.file.Path;

import konverter.konvertierung.TextdateiAusgabe;
import konverter.konvertierung.TextdateiEingabe;


public class TSV {
	
	public static TextdateiAusgabe ausgabe(final Path file, final Charset charset) {
		return new TextdateiAusgabe(file, charset, TsvTagSchreiber::new);
	}
	
	public static TextdateiEingabe eingabe(final Path file, final Charset charset) {
		return new TextdateiEingabe(file, charset, TsvTagParser::new);
	}
	
}
