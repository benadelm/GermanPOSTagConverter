/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import konverter.exception.KonverterException;
import konverter.formate.conllx.CoNLLX;
import konverter.formate.rftagger.RFTagger;
import konverter.formate.tsv.TSV;
import konverter.konvertierung.Ausgabe;

public class KonverterMain {
	
	public static void main(final String[] args) {
		final Kommandozeile komm = Kommandozeile.parsen(args);
		if (komm == null)
			return;
		
		final FileSystem fs = FileSystems.getDefault();
		final Path inpath;
		final Path outpath;
		try {
			inpath = fs.getPath(komm.getEingabedatei());
		} catch (final InvalidPathException e) {
			System.err.println(komm.getEingabedatei() + " ist kein gültiger Pfad.");
			return;
		}
		try {
			outpath = fs.getPath(komm.getAusgabedatei());
		} catch (final InvalidPathException e) {
			System.err.println(komm.getAusgabedatei() + " ist kein gültiger Pfad.");
			return;
		}
		
		final String eingabeformat = komm.getEingabeformat();
		final String ausgabeformat = komm.getAusgabeformat();
		
		final Charset eingabeCharset = orDefault(komm.getEingabeCharset(), StandardCharsets.UTF_8);
		final Charset ausgabeCharset = orDefault(komm.getAusgabeCharset(), StandardCharsets.UTF_8);
		
		try {
			convert(inpath, outpath, eingabeformat, ausgabeformat, eingabeCharset, ausgabeCharset);
		} catch (final KonverterException e) {
			System.err.print("Fehler beim ");
			switch (e.getArbeitsschritt()) {
				case EINLESEN:
					System.err.print("Einlesen");
					break;
				case KONVERTIEREN:
					System.err.print("Konvertieren");
					break;
				case AUSGEBEN:
					System.err.print("Ausgeben");
					break;
			}
			System.err.println(": " + e.getMessage());
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	private static void convert(final Path inpath, final Path outpath, final String eingabeformat, final String ausgabeformat, final Charset eingabeCharset, final Charset ausgabeCharset) throws IOException {
		switch (ausgabeformat) {
			case "rftagger":
				convert(inpath, eingabeformat, eingabeCharset, RFTagger.ausgabe(outpath, ausgabeCharset));
				break;
			case "conllx":
				convert(inpath, eingabeformat, eingabeCharset, CoNLLX.ausgabe(outpath, ausgabeCharset));
				break;
			case "tsv":
				convert(inpath, eingabeformat, eingabeCharset, TSV.ausgabe(outpath, ausgabeCharset));
				break;
			default:
				System.err.println("Unbekanntes Ausgabeformat: " + ausgabeformat);
				System.exit(1);
		}
	}
	
	private static void convert(final Path inpath, final String eingabeformat, final Charset eingabeCharset, final Ausgabe ausgabe) throws IOException, KonverterException {
		switch (eingabeformat) {
			case "rftagger":
				ausgabe.konvertierenNach(RFTagger.eingabe(inpath, eingabeCharset));
				break;
			case "conllx":
				ausgabe.konvertierenNach(CoNLLX.eingabe(inpath, eingabeCharset));
				break;
			case "tsv":
				ausgabe.konvertierenNach(TSV.eingabe(inpath, eingabeCharset));
				break;
			default:
				System.err.println("Unbekanntes Eingabeformat: " + eingabeformat);
				System.exit(1);
		}
	}
	
	private static Charset orDefault(final Charset charset, final Charset defaultCharset) {
		if (charset != null)
			return charset;
		return defaultCharset;
	}
	
}
