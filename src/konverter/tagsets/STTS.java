/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.tagsets;

import java.util.Locale;

import konverter.daten.POS;
import konverter.exception.Arbeitsschritt;
import konverter.exception.KonverterException;

public class STTS {
	
	public static POS fromSTTS(final String form, final String tag) {
		switch (tag) {
			case "ADJA":
				return POS.ADJEKTIV_ATTRIBUTIV;
			case "ADJD":
				return POS.ADJEKTIV_PRAEDIKATIV;
			case "ADV":
				return POS.ADVERB;
			case "APPO":
				return POS.POSTPOSITION;
			case "APPR":
				return POS.PRAEPOSITION;
			case "APPRART":
			case "APPART":
				return POS.PRAEPOSITION_MIT_ARTIKEL;
			case "APZR":
				return POS.RECHTER_TEIL_EINER_ZIRKUMPOSITION;
			case "ART":
				return definitheitsheuristik(form);
			case "CARD":
				return POS.KARDINALZAHL;
			case "FM":
				return POS.FREMDSPRACHLICHES_MATERIAL;
			case "ITJ":
				return POS.INTERJEKTION;
			case "KOKOM":
				return POS.VERGLEICHSPARTIKEL;
			case "KON":
				return POS.BEIORDNENDE_KONJUNKTION;
			case "KOUI":
				return POS.KONJUNKTION_MIT_INFINITIV_MIT_ZU;
			case "KOUS":
				return POS.KONJUNKTION_MIT_FINITEM_NEBENSATZ;
			case "NE":
				return POS.EIGENNAME;
			case "NN":
				return POS.SUBSTANTIV;
			case "PAV":
			case "PROAV":
				return POS.PRONOMINALADVERB;
			case "PDAT":
				return POS.ATTRIBUTIVES_DEMONSTRATIVPRONOMEN;
			case "PDS":
				return POS.SUBSTITUIERENDES_DEMONSTRATIVPRONOMEN;
			case "PIAT":
				return POS.ATTRIBUTIVES_INDEFINITPRONOMEN;
			case "PIDAT":
				return POS.ATTRIBUTIVES_INDEFINITPRONOMEN_MIT_ARTIKEL;
			case "PIS":
				return POS.SUBSTITUIERENDES_INDEFINITPRONOMEN;
			case "PPER":
				return POS.PERSONALPRONOMEN;
			case "PPOSAT":
				return POS.ATTRIBUTIVES_POSSESSIVPRONOMEN;
			case "PPOSS":
				return POS.SUBSTITUIERENDES_POSSESSIVPRONOMEN;
			case "PRELAT":
				return POS.ATTRIBUTIVES_RELATIVPRONOMEN;
			case "PRELS":
				return POS.SUBSTITUIERENDES_RELATIVPRONOMEN;
			case "PRF":
				return POS.REFLEXIVPRONOMEN;
			case "PTKA":
				return POS.GRADPARTIKEL;
			case "PTKANT":
				return POS.ANTWORTPARTIKEL;
			case "PTKNEG":
				return POS.NEGATIONSPARTIKEL;
			case "PTKVZ":
				return POS.VERBPRAEFIX;
			case "PTKZU":
				return POS.PARTIKEL_ZU;
			case "PWAT":
				return POS.ATTRIBUTIVES_INTERROGATIVPRONOMEN;
			case "PWAV":
				return POS.ADVERBIALES_INTERROGATIVPRONOMEN;
			case "PWS":
				return POS.SUBSTITUIERENDES_INTERROGATIVPRONOMEN;
			case "TRUNC":
				return POS.ABGETRENNTES_SONSTIGES_RESTGLIED;
			case "VAFIN":
				return POS.HILFSVERB_FINIT;
			case "VAIMP":
				return POS.HILFSVERB_IMPERATIV;
			case "VAINF":
				return POS.HILFSVERB_INFINITIV;
			case "VAPP":
				return POS.HILFSVERB_PARTIZIP;
			case "VMFIN":
				return POS.MODALVERB_FINIT;
			case "VMINF":
				return POS.MODALVERB_INFINITIV;
			case "VMPP":
				return POS.MODALVERB_PARTIZIP;
			case "VVFIN":
				return POS.VOLLVERB_FINIT;
			case "VVIMP":
				return POS.VOLLVERB_IMPERATIV;
			case "VVINF":
				return POS.VOLLVERB_INFINITIV;
			case "VVIZU":
				return POS.VOLLVERB_INFINITIV_MIT_ZU;
			case "VVPP":
				return POS.VOLLVERB_PARTIZIP;
			case "XY":
				return nichtwortheuristik(form);
			case "$.":
				return satzendeheuristik(form);
			case "$,":
				return POS.KOMMA;
			case "$(":
			case "$LRB":
				return symbolheuristik(form);
			default:
				throw new KonverterException("Unbekanntes STTS-POS-Tag: " + tag, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS definitheitsheuristik(final String form) {
		return form.toLowerCase(Locale.GERMAN).startsWith("d") ? POS.BESTIMMTER_ARTIKEL : POS.UNBESTIMMTER_ARTIKEL;
	}
	
	private static POS nichtwortheuristik(final String form) {
		if (form.codePoints().allMatch(cp -> Character.isValidCodePoint(cp) && (Character.UnicodeScript.of(cp) == Character.UnicodeScript.LATIN)))
			return POS.AUTH;
		return POS.NICHTWORT;
	}
	
	private static POS satzendeheuristik(final String form) {
		switch (form) {
			case "...":
			case "\u2026":
				return POS.AUSLASSUNGSPUNKTE;
			case ":":
				return POS.DOPPELPUNKT;
			default:
				return POS.SATZENDE;
		}
	}
	
	private static POS symbolheuristik(final String form) {
		switch (form) {
			case "``":
			case "`":
				return POS.OEFFNENDES_ANFUEHRUNGSZEICHEN;
			case "''":
				return POS.SCHLIESSENDES_ANFUEHRUNGSZEICHEN;
			case "'":
				// Auftreten als Anführungszeichen kann nicht unterschieden werden
				return POS.APOSTROPH;
			case "(":
				return POS.OEFFNENDE_KLAMMER;
			case ")":
				return POS.SCHLIESSENDE_KLAMMER;
			case "-":
				return POS.BINDESTRICH;
			case "/":
				return POS.SLASH;
			case "*":
				return POS.STERNCHEN;
			default:
				return POS.SONSTIGE_INTERPUNKTION;
		}
	}
	
	public static String toSTTS(final POS pos) {
		switch (pos) {
			case ADJEKTIV_ATTRIBUTIV:
				return "ADJA";
			case ADJEKTIV_PRAEDIKATIV:
				return "ADJD";
			case ADVERB:
				return "ADV";
			case POSTPOSITION:
				return "APPO";
			case PRAEPOSITION:
				return "APPR";
			case PRAEPOSITION_MIT_ARTIKEL:
				return "APPRART";
			case RECHTER_TEIL_EINER_ZIRKUMPOSITION:
				return "APZR";
			case BESTIMMTER_ARTIKEL:
			case UNBESTIMMTER_ARTIKEL:
				return "ART";
			case KARDINALZAHL:
				return "CARD";
			case FREMDSPRACHLICHES_MATERIAL:
				return "FM";
			case INTERJEKTION:
				return "ITJ";
			case VERGLEICHSPARTIKEL:
				return "KOKOM";
			case BEIORDNENDE_KONJUNKTION:
				return "KON";
			case KONJUNKTION_MIT_INFINITIV_MIT_ZU:
				return "KOUI";
			case KONJUNKTION_MIT_FINITEM_NEBENSATZ:
				return "KOUS";
			case EIGENNAME:
				return "NE";
			case SUBSTANTIV:
				return "NN";
			case PRONOMINALADVERB:
				return "PAV";
			case ATTRIBUTIVES_DEMONSTRATIVPRONOMEN:
				return "PDAT";
			case SUBSTITUIERENDES_DEMONSTRATIVPRONOMEN:
				return "PDS";
			case ATTRIBUTIVES_INDEFINITPRONOMEN:
				return "PIAT";
			case ATTRIBUTIVES_INDEFINITPRONOMEN_MIT_ARTIKEL:
				return "PIDAT";
			case SUBSTITUIERENDES_INDEFINITPRONOMEN:
				return "PIS";
			case PERSONALPRONOMEN:
				return "PPER";
			case ATTRIBUTIVES_POSSESSIVPRONOMEN:
				return "PPOSAT";
			case SUBSTITUIERENDES_POSSESSIVPRONOMEN:
				return "PPOSS";
			case ATTRIBUTIVES_RELATIVPRONOMEN:
				return "PRELAT";
			case SUBSTITUIERENDES_RELATIVPRONOMEN:
				return "PRELS";
			case REFLEXIVPRONOMEN:
				return "PRF";
			case GRADPARTIKEL:
				return "PTKA";
			case ANTWORTPARTIKEL:
				return "PTKANT";
			case NEGATIONSPARTIKEL:
				return "PTKNEG";
			case VERBPRAEFIX:
				return "PTKVZ";
			case PARTIKEL_ZU:
				return "PTKZU";
			case ATTRIBUTIVES_INTERROGATIVPRONOMEN:
				return "PWAT";
			case ADVERBIALES_INTERROGATIVPRONOMEN:
				return "PWAV";
			case SUBSTITUIERENDES_INTERROGATIVPRONOMEN:
				return "PWS";
			case ABGETRENNTES_ADJEKTIVRESTGLIED:
			case ABGETRENNTES_SUBSTANTIVRESTGLIED:
			case ABGETRENNTES_VERBRESTGLIED:
			case ABGETRENNTES_SONSTIGES_RESTGLIED:
				return "TRUNC";
			case HILFSVERB_FINIT:
				return "VAFIN";
			case HILFSVERB_INFINITIV:
				return "VAINF";
			case HILFSVERB_IMPERATIV:
				return "VAIMP";
			case HILFSVERB_PARTIZIP:
				return "VAPP";
			case MODALVERB_FINIT:
				return "VMFIN";
			case MODALVERB_INFINITIV:
				return "VMINF";
			case MODALVERB_PARTIZIP:
				return "VMPP";
			case VOLLVERB_FINIT:
				return "VVFIN";
			case VOLLVERB_INFINITIV:
				return "VVINF";
			case VOLLVERB_IMPERATIV:
				return "VVIMP";
			case VOLLVERB_PARTIZIP:
				return "VVPP";
			case VOLLVERB_INFINITIV_MIT_ZU:
				return "VVIZU";
			case NICHTWORT:
			case AUTH:
				return "XY";
			case SATZENDE:
			case DOPPELPUNKT:
			case AUSLASSUNGSPUNKTE:
				return "$.";
			case KOMMA:
				return "$,";
			case BINDESTRICH:
			case OEFFNENDE_KLAMMER:
			case SCHLIESSENDE_KLAMMER:
			case OEFFNENDES_ANFUEHRUNGSZEICHEN:
			case SCHLIESSENDES_ANFUEHRUNGSZEICHEN:
			case SLASH:
			case STERNCHEN:
			case APOSTROPH:
			case SONSTIGE_INTERPUNKTION:
				return "$(";
			default:
				// sollte nie eintreten, da alle Konstanten abgedeckt sind
				throw new KonverterException(pos.name() + " kann nicht als STTS-POS-Tag dargestellt werden.", Arbeitsschritt.KONVERTIEREN);
		}
	}
	
}
