/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.konvertierung;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import konverter.exception.KonverterException;


public class TextdateiEingabe implements Eingabe {
	
	private final Path pFile;
	private final Charset pCharset;
	private final Quellengenerator pQuellengenerator;
	
	public TextdateiEingabe(final Path file, final Charset charset, final Quellengenerator quellengenerator) {
		pFile = file;
		pCharset = charset;
		pQuellengenerator = quellengenerator;
	}
	
	@Override
	public void konvertierenVon(final Senke senke) throws IOException, KonverterException {
		try (final BufferedReader reader = Files.newBufferedReader(pFile, pCharset)) {
			Konvertierung.konvertieren(pQuellengenerator.quelle(reader), senke);
		}
	}
	
	@FunctionalInterface
	public static interface Quellengenerator {
		
		Quelle quelle(BufferedReader reader) throws IOException;
		
	}
	
}
