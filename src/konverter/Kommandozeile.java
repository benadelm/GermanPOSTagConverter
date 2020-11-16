/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import konverter.kommandozeile.Option;

public class Kommandozeile {
	
	private static final HashMap<String, Option> OPTIONEN;
	
	private String pEingabedatei;
	private String pAusgabedatei;
	
	private Charset pEingabeCharset;
	private Charset pAusgabeCharset;
	
	private String pEingabeformat;
	private String pAusgabeformat;
	
	static {
		OPTIONEN = new HashMap<>();
		
		OPTIONEN.put("input-charset", new CharsetOption(false));
		OPTIONEN.put("output-charset", new CharsetOption(true));
		
		OPTIONEN.put("input-format", new SimpleOption((k, s) -> k.pEingabeformat = s, "Das Format der Eingabe-Datei. Muss angegeben werden. Mögliche Werte: \"rftagger\", \"conllx\" und \"tsv\"."));
		OPTIONEN.put("output-format", new SimpleOption((k, s) -> k.pAusgabeformat = s, "Das Format der Ausgabe-Datei. Muss angegeben werden. Mögliche Werte: \"rftagger\", \"conllx\" und \"tsv\"."));
	}
	
	private Kommandozeile() {}
	
	public String getEingabedatei() {
		return pEingabedatei;
	}
	
	public String getAusgabedatei() {
		return pAusgabedatei;
	}
	
	public Charset getEingabeCharset() {
		return pEingabeCharset;
	}
	
	public Charset getAusgabeCharset() {
		return pAusgabeCharset;
	}
	
	public String getEingabeformat() {
		return pEingabeformat;
	}
	
	public String getAusgabeformat() {
		return pAusgabeformat;
	}
	
	public static Kommandozeile parsen(final String[] args) {
		final Kommandozeile result = new Kommandozeile();
		Option aktuelleOption = null;
		int dateinameIndex = 0;
		for (final String arg : args) {
			if (aktuelleOption == null) {
				if (arg.startsWith("-")) {
					aktuelleOption = OPTIONEN.getOrDefault(arg.substring(1), null);
					if (aktuelleOption == null) {
						verwendungAusgeben();
						return null;
					}
					continue;
				}
			} else {
				if (aktuelleOption.wertUebernehmen(result, arg)) {
					aktuelleOption = null;
					continue;
				}
				verwendungAusgeben();
				return null;
			}
			switch (dateinameIndex) {
				case 0:
					result.pEingabedatei = arg;
					break;
				case 1:
					result.pAusgabedatei = arg;
					break;
				default:
					verwendungAusgeben();
					return null;
			}
			dateinameIndex++;
		}
		if (dateinameIndex < 2) {
			verwendungAusgeben();
			return null;
		}
		return result;
	}
	
	private static void verwendungAusgeben() {
		System.out.println("hermA-Tagsetkonverter");
		System.out.println();
		System.out.println("Kommandozeilenargumente:");
		System.out.println("  [Option1 Wert1 Option2 Wert2 ...] Eingabedatei Ausgabedatei");
		System.out.println();
		System.out.println("Verfügbare Optionen:");
		System.out.println();
		int spalte1breite = 0;
		for (final String option : OPTIONEN.keySet()) {
			final int length = option.length() + 1;
			if (length > spalte1breite)
				spalte1breite = length;
		}
		spalte1breite += 3;
		final ArrayList<Entry<String, Option>> optionenSortiert = new ArrayList<>(OPTIONEN.entrySet());
		Collections.sort(optionenSortiert, (e1, e2) -> e1.getKey().compareToIgnoreCase(e2.getKey()));
		for (final Entry<String, Option> opt : optionenSortiert) {
			final String optname = opt.getKey();
			System.out.print('-');
			System.out.print(optname);
			for (int i = optname.length(); i < spalte1breite; i++)
				System.out.print(' ');
			System.out.println(opt.getValue().getBeschreibung());
		}
	}
	
	private static class SimpleOption implements Option {

		private final String pBeschreibung;
		private final BiConsumer<Kommandozeile, String> pSetter;
		
		public SimpleOption(final BiConsumer<Kommandozeile, String> setter, final String beschreibung) {
			pBeschreibung = beschreibung;
			pSetter = setter;
		}
		
		@Override
		public String getBeschreibung() {
			return pBeschreibung;
		}

		@Override
		public boolean wertUebernehmen(final Kommandozeile kommandozeile, final String wert) {
			pSetter.accept(kommandozeile, wert);
			return true;
		}
		
	}
	
	private static class CharsetOption implements Option {

		private final boolean pAusgabe;
		
		public CharsetOption(final boolean ausgabe) {
			pAusgabe = ausgabe;
		}
		
		@Override
		public String getBeschreibung() {
			return "Name der " + (pAusgabe ? "Ausgabe" : "Eingabe") + "-Zeichenkodierung (zum Beispiel UTF-8).";
		}

		@Override
		public boolean wertUebernehmen(final Kommandozeile kommandozeile, final String wert) {
			final Charset charset;
			try {
				charset = Charset.forName(wert);
			} catch (final IllegalCharsetNameException e) {
				System.err.println(wert + " ist keine gültige Zeichenkodierung.");
				return false;
			} catch (final UnsupportedCharsetException e) {
				System.err.println("Zeichenkodierung " + wert + " wird nicht unterstützt.");
				return false;
			}
			if (pAusgabe)
				kommandozeile.pAusgabeCharset = charset;
			else
				kommandozeile.pEingabeCharset = charset;
			return true;
		}
		
	}
	
}
