/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.konvertierung;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import konverter.exception.KonverterException;


public class TextdateiAusgabe implements Ausgabe {
	
	private final Path pFile;
	private final Charset pCharset;
	private final Senkengenerator pSenkengenerator;
	
	public TextdateiAusgabe(final Path file, final Charset charset, final Senkengenerator senkengenerator) {
		pFile = file;
		pCharset = charset;
		pSenkengenerator = senkengenerator;
	}
	
	@Override
	public void konvertierenNach(final Eingabe eingabe) throws IOException, KonverterException {
		try (final BufferedWriter writer = Files.newBufferedWriter(pFile, pCharset, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
			eingabe.konvertierenVon(pSenkengenerator.senke(writer));
			writer.flush();
		}
	}
	
	@FunctionalInterface
	public static interface Senkengenerator {
		
		Senke senke(BufferedWriter writer) throws IOException;
		
	}
	
}
