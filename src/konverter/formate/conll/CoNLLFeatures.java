/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.formate.conll;

import java.io.IOException;

import konverter.daten.Genus;
import konverter.daten.Kasus;
import konverter.daten.Modus;
import konverter.daten.Numerus;
import konverter.daten.Person;
import konverter.daten.Steigerungsstufe;
import konverter.daten.Tempus;
import konverter.exception.Arbeitsschritt;
import konverter.exception.KonverterException;

public class CoNLLFeatures {
	
	public static Genus genusParsen(final String genus) {
		switch (genus) {
			case "masc":
				return Genus.MASKULINUM;
			case "fem":
				return Genus.FEMININUM;
			case "neut":
				return Genus.NEUTRUM;
			case "*":
				return Genus.UNTERSPEZIFIZIERT;
			default:
				throw new KonverterException("Unbekanntes Genus: " + genus, Arbeitsschritt.EINLESEN);
		}
	}
	
	public static Kasus kasusParsen(final String kasus) {
		switch (kasus) {
			case "nom":
				return Kasus.NOMINATIV;
			case "gen":
				return Kasus.GENITIV;
			case "dat":
				return Kasus.DATIV;
			case "acc":
				return Kasus.AKKUSATIV;
			case "*":
				return Kasus.UNTERSPEZIFIZIERT;
			default:
				throw new KonverterException("Unbekannter Kasus: " + kasus, Arbeitsschritt.EINLESEN);
		}
	}
	
	public static Numerus numerusParsen(final String numerus) {
		switch (numerus) {
			case "sg":
				return Numerus.SINGULAR;
			case "pl":
				return Numerus.PLURAL;
			case "*":
				return Numerus.UNTERSPEZIFIZIERT;
			default:
				throw new KonverterException("Unbekannter Numerus: " + numerus, Arbeitsschritt.EINLESEN);
		}
	}
	
	public static Steigerungsstufe gradParsen(final String grad) {
		switch (grad) {
			case "pos":
				return Steigerungsstufe.POSITIV;
			case "comp":
				return Steigerungsstufe.KOMPARATIV;
			case "sup":
				return Steigerungsstufe.SUPERLATIV;
			default:
				throw new KonverterException("Unbekannte Steigerungsstufe: " + grad, Arbeitsschritt.EINLESEN);
		}
	}
	
	public static Person personParsen(final String person) {
		switch (person) {
			case "1":
				return Person.ERSTE;
			case "2":
				return Person.ZWEITE;
			case "3":
				return Person.DRITTE;
			default:
				throw new KonverterException("Nicht unterstützte Person: " + person, Arbeitsschritt.EINLESEN);
		}
	}
	
	public static Tempus tempusParsen(final String tempus) {
		switch (tempus) {
			case "pres":
				return Tempus.PRAESENS;
			case "past":
				return Tempus.PRAETERITUM;
			default:
				throw new KonverterException("Unbekanntes Tempus: " + tempus, Arbeitsschritt.EINLESEN);
		}
	}
	
	public static Modus modusParsen(final String modus) {
		switch (modus) {
			case "ind":
				return Modus.INDIKATIV;
			case "subj":
				return Modus.KONJUNKTIV;
			default:
				throw new KonverterException("Unbekannter Modus: " + modus, Arbeitsschritt.EINLESEN);
		}
	}
	
	public static void genusSchreiben(final Genus genus, final Appendable appendable) throws IOException, KonverterException {
		switch (genus) {
			case MASKULINUM:
				appendable.append("masc");
				break;
			case FEMININUM:
				appendable.append("fem");
				break;
			case NEUTRUM:
				appendable.append("neut");
				break;
			case UNTERSPEZIFIZIERT:
				appendable.append('*');
				break;
			default:
				// sollte nie eintreten, da alle Konstanten abgedeckt sind
				throw unbekannteKonstante(genus, "Genus");
		}
	}
	
	public static void kasusSchreiben(final Kasus kasus, final Appendable appendable) throws IOException, KonverterException {
		switch (kasus) {
			case NOMINATIV:
				appendable.append("nom");
				break;
			case GENITIV:
				appendable.append("gen");
				break;
			case DATIV:
				appendable.append("dat");
				break;
			case AKKUSATIV:
				appendable.append("acc");
				break;
			case UNTERSPEZIFIZIERT:
				appendable.append('*');
				break;
			default:
				// sollte nie eintreten, da alle Konstanten abgedeckt sind
				throw unbekannteKonstante(kasus, "Kasus");
		}
	}
	
	public static void numerusSchreiben(final Numerus numerus, final Appendable appendable) throws IOException, KonverterException {
		switch (numerus) {
			case SINGULAR:
				appendable.append("sg");
				break;
			case PLURAL:
				appendable.append("pl");
				break;
			case UNTERSPEZIFIZIERT:
				appendable.append('*');
				break;
			default:
				// sollte nie eintreten, da alle Konstanten abgedeckt sind
				throw unbekannteKonstante(numerus, "Numerus");
		}
	}
	
	public static void gradSchreiben(final Steigerungsstufe grad, final Appendable appendable) throws IOException, KonverterException {
		switch (grad) {
			case POSITIV:
				appendable.append("pos");
				break;
			case KOMPARATIV:
				appendable.append("comp");
				break;
			case SUPERLATIV:
				appendable.append("sup");
				break;
			default:
				// sollte nie eintreten, da alle Konstanten abgedeckt sind
				throw unbekannteKonstante(grad, "Steigerungsstufen");
		}
	}
	
	public static void personSchreiben(final Person person, final Appendable appendable) throws IOException, KonverterException {
		switch (person) {
			case ERSTE:
				appendable.append('1');
				break;
			case ZWEITE:
				appendable.append('2');
				break;
			case DRITTE:
				appendable.append('3');
				break;
			default:
				// sollte nie eintreten, da alle Konstanten abgedeckt sind
				throw unbekannteKonstante(person, "Person");
		}
	}
	
	public static void tempusSchreiben(final Tempus tempus, final Appendable appendable) throws IOException, KonverterException {
		switch (tempus) {
			case PRAESENS:
				appendable.append("pres");
				break;
			case PRAETERITUM:
				appendable.append("past");
				break;
			default:
				// sollte nie eintreten, da alle Konstanten abgedeckt sind
				throw unbekannteKonstante(tempus, "Tempus");
		}
	}
	
	public static void modusSchreiben(final Modus modus, final Appendable appendable) throws IOException, KonverterException {
		switch (modus) {
			case INDIKATIV:
				appendable.append("ind");
				break;
			case KONJUNKTIV:
				appendable.append("subj");
				break;
			default:
				// sollte nie eintreten, da alle Konstanten abgedeckt sind
				throw unbekannteKonstante(modus, "Modus");
		}
	}
	
	private static KonverterException unbekannteKonstante(final Enum<?> konstante, final String kontext) throws KonverterException {
		return new KonverterException(kontext + "-Konstante " + konstante.name() + " kann nicht ausgegeben werden.", Arbeitsschritt.AUSGEBEN);
	}
	
}
