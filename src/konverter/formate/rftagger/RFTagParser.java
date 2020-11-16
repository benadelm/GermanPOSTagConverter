/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.formate.rftagger;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.regex.Pattern;

import konverter.daten.Item;
import konverter.daten.Genus;
import konverter.daten.Kasus;
import konverter.daten.Modus;
import konverter.daten.Numerus;
import konverter.daten.POS;
import konverter.daten.Person;
import konverter.daten.Steigerungsstufe;
import konverter.daten.Tempus;
import konverter.daten.Itemtyp;
import konverter.exception.Arbeitsschritt;
import konverter.exception.KonverterException;
import konverter.konvertierung.Quelle;

public class RFTagParser implements Quelle {
	
	private static final Pattern DOT_PATTERN = Pattern.compile(Pattern.quote("."));
	
	private final BufferedReader pReader;
	
	private BigInteger pIndex;
	
	public RFTagParser(final BufferedReader reader) {
		pReader = reader;
		
		pIndex = BigInteger.ZERO;
	}
	
	@Override
	public Item next() throws IOException, KonverterException {
		final Item result = new Item();
		
		final String line = pReader.readLine();
		if (line == null) {
			result.typ = Itemtyp.ENDE;
		} else if ("".equals(line)) {
			result.typ = Itemtyp.LEERZEILE;
			pIndex = BigInteger.ZERO;
		} else {
			pIndex = BigInteger.ONE.add(pIndex);
			parsen(line, result);
		}
		
		return result;
	}
	
	private void parsen(final String line, final Item targetToken) {
		final int tabIndex1 = line.indexOf('\t');
		if (tabIndex1 < 0)
			throw new KonverterException(line + " hat kein Tag.", Arbeitsschritt.EINLESEN);
		
		final String tokenString = line.substring(0, tabIndex1);
		final String tagString = extractTagString(line, tabIndex1);
		
		if ("".equals(tagString))
			throw new KonverterException(tokenString + " hat kein Tag.", Arbeitsschritt.EINLESEN);
		
		analysieren(tagString, targetToken);
		
		targetToken.typ = Itemtyp.TOKEN;
		targetToken.index = pIndex;
		targetToken.form = tokenString;
	}
	
	private static String extractTagString(final String line, int tabIndex1) {
		tabIndex1++;
		final int tabIndex2 = line.indexOf('\t', tabIndex1);
		return line.substring(tabIndex1, tabIndex2 < 0 ? line.length() : tabIndex2);
	}
	
	private static void analysieren(final String tagString, final Item targetToken) {
		final String[] parts = DOT_PATTERN.split(tagString);
		
		switch (parts[0]) {
			case "ADJA":
				// ADJA.Pos.Dat.Pl.Masc
				strukturtest(parts, 5, tagString);
				targetToken.pos = POS.ADJEKTIV_ATTRIBUTIV;
				targetToken.grad = gradBestimmen(parts[1]);
				targetToken.kasus = kasusBestimmen(parts[2]);
				targetToken.numerus = numerusBestimmen(parts[3]);
				targetToken.genus = genusBestimmen(parts[4]);
				break;
			case "ADJD":
				// ADJD.Pos
				strukturtest(parts, 2, tagString);
				targetToken.pos = POS.ADJEKTIV_PRAEDIKATIV;
				targetToken.grad = gradBestimmen(parts[1]);
				break;
			case "ADV":
				strukturtest(parts, 1, tagString);
				targetToken.pos = POS.ADVERB;
				break;
			case "APPO":
				if ((parts.length != 1) && (parts.length != 2))
					strukturfehler(parts, tagString);
				targetToken.pos = POS.POSTPOSITION;
				break;
			case "APPR":
				if ((parts.length != 1) && (parts.length != 2))
					strukturfehler(parts, tagString);
				targetToken.pos = POS.PRAEPOSITION;
				break;
			case "APPRART":
				// APPRART.Acc.Sg.Neut
				strukturtest(parts, 4, tagString);
				targetToken.pos = POS.PRAEPOSITION_MIT_ARTIKEL;
				targetToken.kasus = kasusBestimmen(parts[1]);
				targetToken.numerus = numerusBestimmen(parts[2]);
				targetToken.genus = genusBestimmen(parts[3]);
				break;
			case "APZR":
				strukturtest(parts, 1, tagString);
				targetToken.pos = POS.RECHTER_TEIL_EINER_ZIRKUMPOSITION;
				break;
			case "ART":
				// ART.Indef.Nom.Sg.Masc
				strukturtest(parts, 5, tagString);
				targetToken.pos = definitheitBestimmen(parts[1]);
				targetToken.kasus = kasusBestimmen(parts[2]);
				targetToken.numerus = numerusBestimmen(parts[3]);
				targetToken.genus = genusBestimmen(parts[4]);
				break;
			case "CARD":
				strukturtest(parts, 1, tagString);
				targetToken.pos = POS.KARDINALZAHL;
				break;
			case "CONJ":
				// CONJ.Coord
				strukturtest(parts, 2, tagString);
				targetToken.pos = konjunktionstypBestimmen(parts[1]);
				break;
			case "FM":
				strukturtest(parts, 1, tagString);
				targetToken.pos = POS.FREMDSPRACHLICHES_MATERIAL;
				break;
			case "ITJ":
				strukturtest(parts, 1, tagString);
				targetToken.pos = POS.INTERJEKTION;
				break;
			case "N":
				// N.Reg.Acc.Sg.Fem
				strukturtest(parts, 5, tagString);
				targetToken.pos = substantivtypBestimmen(parts[1]);
				targetToken.kasus = kasusBestimmen(parts[2]);
				targetToken.numerus = numerusBestimmen(parts[3]);
				targetToken.genus = genusBestimmen(parts[4]);
				break;
			case "PART":
				// PART.Zu
				strukturtest(parts, 2, tagString);
				targetToken.pos = partikeltypBestimmen(parts[1]);
				break;
			case "PRO":
				// PRO.Pers.Subst.3.Nom.Sg.Masc
				strukturtest(parts, 7, tagString);
				targetToken.pos = pronomentypBestimmen(parts[1], parts[2]);
				targetToken.person = personBestimmen(parts[3], true);
				targetToken.kasus = kasusBestimmen(parts[4]);
				targetToken.numerus = numerusBestimmen(parts[5]);
				targetToken.genus = genusBestimmen(parts[6], true);
				break;
			case "PROADV":
				// PROADV.Inter
				strukturtest(parts, 2, tagString);
				targetToken.pos = pronominaladverbtypBestimmen(parts[1]);
				break;
			case "SYM":
				// SYM.Pun.Sent
				strukturtest(parts, 3, tagString);
				targetToken.pos = symboltypBestimmen(parts[1], parts[2]);
				break;
			case "TRUNC":
				// TRUNC.Verb
				strukturtest(parts, 2, tagString);
				targetToken.pos = restgliedtypBestimmen(parts[1]);
				break;
			case "VFIN":
				// VFIN.Full.3.Sg.Pres.Ind
				strukturtest(parts, 6, tagString);
				targetToken.pos = verbtypBestimmen(parts[1], POS.VOLLVERB_FINIT, POS.HILFSVERB_FINIT, POS.MODALVERB_FINIT);
				targetToken.person = personBestimmen(parts[2]);
				targetToken.numerus = numerusBestimmen(parts[3]);
				targetToken.tempus = tempusBestimmen(parts[4]);
				targetToken.modus = modusBestimmen(parts[5]);
				break;
			case "VIMP":
				// VIMP.Full.2.Sg[.Imp]
				if ((parts.length != 4) && (parts.length != 5))
					strukturfehler(parts, tagString);
				targetToken.pos = verbtypBestimmenImperativ(parts[1]);
				targetToken.person = personBestimmen(parts[2]);
				targetToken.numerus = numerusBestimmen(parts[3]);
				break;
			case "VINF":
				// VINF.Full.zu
				strukturtest(parts, 3, tagString);
				targetToken.pos = verbtypBestimmenInfinitiv(parts[1], parts[2]);
				break;
			case "VPP":
				// VPP.Full[.Psp]
				if ((parts.length != 2) && (parts.length != 3))
					strukturfehler(parts, tagString);
				targetToken.pos = verbtypBestimmen(parts[1], POS.VOLLVERB_PARTIZIP, POS.HILFSVERB_PARTIZIP, POS.MODALVERB_PARTIZIP);
				break;
			default:
				throw new KonverterException("Unbekannte Wortart: " + parts[0], Arbeitsschritt.EINLESEN);
		}
	}
	
	private static void strukturtest(final String[] tagParts, final int expectedLength, final String tagString) {
		if (tagParts.length != expectedLength)
			strukturfehler(tagParts, tagString);
	}
	
	private static void strukturfehler(final String[] tagParts, final String tagString) throws KonverterException {
		throw new KonverterException("Strukturfehler " + tagParts[0] + "-Tag: " + tagString, Arbeitsschritt.EINLESEN);
	}
	
	private static POS definitheitBestimmen(final String definitheitString) {
		switch (definitheitString) {
			case "Def":
				return POS.BESTIMMTER_ARTIKEL;
			case "Indef":
				return POS.UNBESTIMMTER_ARTIKEL;
			default:
				throw new KonverterException("Unbekannte Definitheit: " + definitheitString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS konjunktionstypBestimmen(final String konjunktionstypString) {
		switch (konjunktionstypString) {
			case "Comp":
				return POS.VERGLEICHSPARTIKEL;
			case "Coord":
				return POS.BEIORDNENDE_KONJUNKTION;
			case "SubFin":
				return POS.KONJUNKTION_MIT_FINITEM_NEBENSATZ;
			case "SubInf":
				return POS.KONJUNKTION_MIT_INFINITIV_MIT_ZU;
			default:
				throw new KonverterException("Unbekannter Konjunktionstyp: " + konjunktionstypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS substantivtypBestimmen(final String substantivtypString) {
		switch (substantivtypString) {
			case "Reg":
				return POS.SUBSTANTIV;
			case "Name":
				return POS.EIGENNAME;
			default:
				throw new KonverterException("Unbekannter Substantivtyp: " + substantivtypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS partikeltypBestimmen(final String partikeltypString) {
		switch (partikeltypString) {
			case "Ans":
				return POS.ANTWORTPARTIKEL;
			case "Deg":
				return POS.GRADPARTIKEL;
			case "Neg":
				return POS.NEGATIONSPARTIKEL;
			case "Zu":
				return POS.PARTIKEL_ZU;
			case "Verb":
				return POS.VERBPRAEFIX;
			default:
				throw new KonverterException("Unbekannter Partikeltyp: " + partikeltypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS pronomentypBestimmen(final String pronomentypString, final String verwendungString) {
		switch (pronomentypString) {
			case "Pers":
				checkIsSubst(verwendungString, "Personalpronomen");
				return POS.PERSONALPRONOMEN;
			case "Poss":
				return attrOderSubst(verwendungString, POS.ATTRIBUTIVES_POSSESSIVPRONOMEN, POS.SUBSTITUIERENDES_POSSESSIVPRONOMEN);
			case "Dem":
				return attrOderSubst(verwendungString, POS.ATTRIBUTIVES_DEMONSTRATIVPRONOMEN, POS.SUBSTITUIERENDES_DEMONSTRATIVPRONOMEN);
			case "Indef":
				return attrOderSubst(verwendungString, POS.ATTRIBUTIVES_INDEFINITPRONOMEN, POS.SUBSTITUIERENDES_INDEFINITPRONOMEN);
			case "Inter":
				return attrOderSubst(verwendungString, POS.ATTRIBUTIVES_INTERROGATIVPRONOMEN, POS.SUBSTITUIERENDES_INTERROGATIVPRONOMEN);
			case "Refl":
				checkIsSubst(verwendungString, "Reflexivpronomen");
				return POS.REFLEXIVPRONOMEN;
			case "Rel":
				return attrOderSubst(verwendungString, POS.ATTRIBUTIVES_RELATIVPRONOMEN, POS.SUBSTITUIERENDES_RELATIVPRONOMEN);
			default:
				throw new KonverterException("Unbekannter Pronomentyp: " + pronomentypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS attrOderSubst(final String verwendungString, final POS attr, final POS subst) {
		switch (verwendungString) {
			case "Attr":
				return attr;
			case "Subst":
				return subst;
			default:
				throw new KonverterException("Unbekannte Verwendung: " + verwendungString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static void checkIsSubst(final String verwendungString, final String pronomentyp) {
		if (!"Subst".equals(verwendungString))
			throw new KonverterException("Unerwarteter Pronomentyp für " + pronomentyp + " (nur Subst möglich): " + verwendungString, Arbeitsschritt.EINLESEN);
	}
	
	private static POS pronominaladverbtypBestimmen(final String pronominaladverbtypString) {
		switch (pronominaladverbtypString) {
			case "Dem":
				return POS.PRONOMINALADVERB;
			case "Inter":
				return POS.ADVERBIALES_INTERROGATIVPRONOMEN;
			default:
				throw new KonverterException("Unbekannter Pronominaladverbtyp: " + pronominaladverbtypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS symboltypBestimmen(final String symboltypString, final String symboluntertypString) {
		switch (symboltypString) {
			case "Pun":
				return interpunktionstypBestimmen(symboluntertypString);
			case "Quot":
				return linksRechts(symboluntertypString, POS.OEFFNENDES_ANFUEHRUNGSZEICHEN, POS.SCHLIESSENDES_ANFUEHRUNGSZEICHEN);
			case "Paren":
				return linksRechts(symboluntertypString, POS.OEFFNENDE_KLAMMER, POS.SCHLIESSENDE_KLAMMER);
			case "Other":
				return anderenSymboltypBestimmen(symboluntertypString);
			default:
				throw new KonverterException("Unbekannter Symboltyp: " + symboltypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS interpunktionstypBestimmen(final String symboluntertypString) {
		switch (symboluntertypString) {
			case "Colon":
				return POS.DOPPELPUNKT;
			case "Comma":
				return POS.KOMMA;
			case "Sent":
				return POS.SATZENDE;
			case "Hyph":
				return POS.BINDESTRICH;
			case "Slash":
				return POS.SLASH;
			case "Cont":
				return POS.AUSLASSUNGSPUNKTE;
			case "Apos":
				return POS.APOSTROPH;
			case "Other":
				return POS.SONSTIGE_INTERPUNKTION;
			default:
				throw new KonverterException("Unbekannter Interpunktions-Untertyp: " + symboluntertypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS linksRechts(final String symboluntertypString, final POS links, final POS rechts) {
		switch (symboluntertypString) {
			case "Left":
				return links;
			case "Right":
				return rechts;
			default:
				throw new KonverterException("Nicht Left oder Right: " + symboluntertypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS anderenSymboltypBestimmen(final String symboluntertypString) {
		switch (symboluntertypString) {
			case "Aster":
				return POS.STERNCHEN;
			case "Auth":
				return POS.AUTH;
			case "XY":
				return POS.NICHTWORT;
			default:
				throw new KonverterException("Unbekannter Symboluntertyp für SYM.Other: " + symboluntertypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS restgliedtypBestimmen(final String restgliedtypString) {
		switch (restgliedtypString) {
			case "Adj":
				return POS.ABGETRENNTES_ADJEKTIVRESTGLIED;
			case "Noun":
				return POS.ABGETRENNTES_SUBSTANTIVRESTGLIED;
			case "Verb":
				return POS.ABGETRENNTES_VERBRESTGLIED;
			case "-":
				return POS.ABGETRENNTES_SONSTIGES_RESTGLIED;
			default:
				throw new KonverterException("Unbekannter Restgliedtyp: " + restgliedtypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS verbtypBestimmen(final String verbtypString, final POS full, final POS aux, final POS mod) {
		switch (verbtypString) {
			case "Full":
				return full;
			case "Aux":
				return aux;
			case "Mod":
				return mod;
			default:
				throw new KonverterException("Unbekannter Verbtyp: " + verbtypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS verbtypBestimmenImperativ(final String verbtypString) {
		switch (verbtypString) {
			case "Full":
				return POS.VOLLVERB_IMPERATIV;
			case "Aux":
				return POS.HILFSVERB_IMPERATIV;
			case "Mod":
				throw new KonverterException("Modalverb-Imperativ nicht unterstützt", Arbeitsschritt.EINLESEN);
			default:
				throw new KonverterException("Unbekannter Verbtyp: " + verbtypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS verbtypBestimmenInfinitiv(final String verbtypString, final String infinitivtypString) {
		switch (infinitivtypString) {
			case "zu":
				return verbtypBestimmenZuInfinitiv(verbtypString);
			case "-":
				return verbtypBestimmen(verbtypString, POS.VOLLVERB_INFINITIV, POS.HILFSVERB_INFINITIV, POS.MODALVERB_INFINITIV);
			default:
				throw new KonverterException("Unbekannter Infinitivtyp: " + infinitivtypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static POS verbtypBestimmenZuInfinitiv(final String verbtypString) {
		switch (verbtypString) {
			case "Full":
				return POS.VOLLVERB_INFINITIV_MIT_ZU;
			case "Aux":
			case "Mod":
				throw new KonverterException("zu-Infinitiv für Hilfs- und Modalverben nicht unterstützt", Arbeitsschritt.EINLESEN);
			default:
				throw new KonverterException("Unbekannter Verbtyp: " + verbtypString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static Steigerungsstufe gradBestimmen(final String gradString) {
		switch (gradString) {
			case "Pos":
				return Steigerungsstufe.POSITIV;
			case "Comp":
				return Steigerungsstufe.KOMPARATIV;
			case "Sup":
				return Steigerungsstufe.SUPERLATIV;
			default:
				throw new KonverterException("Unbekannte Steigerungsstufe: " + gradString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static Person personBestimmen(final String personString) {
		return personBestimmen(personString, false);
	}
	
	private static Person personBestimmen(final String personString, final boolean optional) {
		switch (personString) {
			case "1":
				return Person.ERSTE;
			case "2":
				return Person.ZWEITE;
			case "3":
				return Person.DRITTE;
			case "-":
				if (optional)
					return null;
			default:
				throw new KonverterException("Unbekannte Person: " + personString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static Kasus kasusBestimmen(final String kasusString) {
		switch (kasusString) {
			case "Nom":
				return Kasus.NOMINATIV;
			case "Gen":
				return Kasus.GENITIV;
			case "Dat":
				return Kasus.DATIV;
			case "Acc":
				return Kasus.AKKUSATIV;
			case "*":
				return Kasus.UNTERSPEZIFIZIERT;
			default:
				throw new KonverterException("Unbekannter Kasus: " + kasusString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static Numerus numerusBestimmen(final String numerusString) {
		switch (numerusString) {
			case "Sg":
				return Numerus.SINGULAR;
			case "Pl":
				return Numerus.PLURAL;
			case "*":
				return Numerus.UNTERSPEZIFIZIERT;
			default:
				throw new KonverterException("Unbekannter Numerus: " + numerusString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static Genus genusBestimmen(final String genusString) {
		return genusBestimmen(genusString, false);
	}
	
	private static Genus genusBestimmen(final String genusString, final boolean optional) {
		switch (genusString) {
			case "Masc":
				return Genus.MASKULINUM;
			case "Fem":
				return Genus.FEMININUM;
			case "Neut":
				return Genus.NEUTRUM;
			case "*":
				return Genus.UNTERSPEZIFIZIERT;
			case "-":
				if (optional)
					return null;
			default:
				throw new KonverterException("Unbekanntes Genus: " + genusString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static Tempus tempusBestimmen(final String tempusString) {
		switch (tempusString) {
			case "Pres":
				return Tempus.PRAESENS;
			case "Past":
				return Tempus.PRAETERITUM;
			default:
				throw new KonverterException("Unbekanntes Tempus: " + tempusString, Arbeitsschritt.EINLESEN);
		}
	}
	
	private static Modus modusBestimmen(final String modusString) {
		switch (modusString) {
			case "Ind":
				return Modus.INDIKATIV;
			case "Subj":
				return Modus.KONJUNKTIV;
			default:
				throw new KonverterException("Unbekannter finiter Modus: " + modusString, Arbeitsschritt.EINLESEN);
		}
	}
	
}
