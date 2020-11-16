/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.formate.rftagger;

import java.io.IOException;

import konverter.daten.Item;
import konverter.daten.Genus;
import konverter.daten.Kasus;
import konverter.daten.Modus;
import konverter.daten.Numerus;
import konverter.daten.Person;
import konverter.daten.Steigerungsstufe;
import konverter.daten.Tempus;
import konverter.daten.Itemtyp;
import konverter.exception.Arbeitsschritt;
import konverter.exception.KonverterException;
import konverter.konvertierung.Senke;

public class RFTagSchreiber implements Senke {

	private final Appendable pTarget;

	public RFTagSchreiber(final Appendable target) {
		pTarget = target;
	}

	@Override
	public void next(final Item token) throws IOException, KonverterException {
		if (token.typ == Itemtyp.TOKEN) {
			pTarget.append(token.form);
			pTarget.append('\t');
			tagSchreiben(token);
		}
		pTarget.append('\n');
	}
	
	private void tagSchreiben(final Item token) throws IOException, KonverterException {
		switch (token.pos) {
			case ADJEKTIV_ATTRIBUTIV:
				pTarget.append("ADJA");
				featureAusgeben(token.grad);
				kngAusgeben(token);
				break;
			case ADJEKTIV_PRAEDIKATIV:
				pTarget.append("ADJD");
				featureAusgeben(token.grad);
				break;
			case ADVERB:
				pTarget.append("ADV");
				break;
			case POSTPOSITION:
				pTarget.append("APPO");
				break;
			case PRAEPOSITION:
				pTarget.append("APPR");
				break;
			case PRAEPOSITION_MIT_ARTIKEL:
				pTarget.append("APPRART");
				kngAusgeben(token);
				break;
			case RECHTER_TEIL_EINER_ZIRKUMPOSITION:
				pTarget.append("APZR");
				break;
			case BESTIMMTER_ARTIKEL:
				pTarget.append("ART.Def");
				kngAusgeben(token);
				break;
			case UNBESTIMMTER_ARTIKEL:
				pTarget.append("ART.Indef");
				kngAusgeben(token);
				break;
			case KARDINALZAHL:
				pTarget.append("CARD");
				break;
			case VERGLEICHSPARTIKEL:
				pTarget.append("CONJ.Comp");
				break;
			case BEIORDNENDE_KONJUNKTION:
				pTarget.append("CONJ.Coord");
				break;
			case KONJUNKTION_MIT_FINITEM_NEBENSATZ:
				pTarget.append("CONJ.SubFin");
				break;
			case KONJUNKTION_MIT_INFINITIV_MIT_ZU:
				pTarget.append("CONJ.SubInf");
				break;
			case FREMDSPRACHLICHES_MATERIAL:
				pTarget.append("FM");
				break;
			case INTERJEKTION:
				pTarget.append("ITJ");
				break;
			case EIGENNAME:
				pTarget.append("N.Name");
				kngAusgeben(token);
				break;
			case SUBSTANTIV:
				pTarget.append("N.Reg");
				kngAusgeben(token);
				break;
			case ANTWORTPARTIKEL:
				pTarget.append("PART.Ans");
				break;
			case GRADPARTIKEL:
				pTarget.append("PART.Deg");
				break;
			case NEGATIONSPARTIKEL:
				pTarget.append("PART.Neg");
				break;
			case VERBPRAEFIX:
				pTarget.append("PART.Verb");
				break;
			case PARTIKEL_ZU:
				pTarget.append("PART.Zu");
				break;
			case ATTRIBUTIVES_DEMONSTRATIVPRONOMEN:
				pTarget.append("PRO.Dem.Attr");
				featureAusgeben(token.person);
				kngAusgeben(token);
				break;
			case SUBSTITUIERENDES_DEMONSTRATIVPRONOMEN:
				pTarget.append("PRO.Dem.Subst");
				featureAusgeben(token.person);
				kngAusgeben(token);
				break;
			case ATTRIBUTIVES_INDEFINITPRONOMEN:
			case ATTRIBUTIVES_INDEFINITPRONOMEN_MIT_ARTIKEL:
				// PIDAT wurde nicht benutzt
				pTarget.append("PRO.Indef.Attr");
				featureAusgeben(token.person);
				kngAusgeben(token);
				break;
			case SUBSTITUIERENDES_INDEFINITPRONOMEN:
				pTarget.append("PRO.Indef.Subst");
				featureAusgeben(token.person);
				kngAusgeben(token);
				break;
			case ATTRIBUTIVES_INTERROGATIVPRONOMEN:
				pTarget.append("PRO.Inter.Attr");
				featureAusgeben(token.person);
				kngAusgeben(token);
				break;
			case SUBSTITUIERENDES_INTERROGATIVPRONOMEN:
				pTarget.append("PRO.Inter.Subst");
				featureAusgeben(token.person);
				kngAusgeben(token);
				break;
			case PERSONALPRONOMEN:
				pTarget.append("PRO.Pers.Subst");
				featureAusgeben(token.person);
				kngAusgeben(token);
				break;
			case ATTRIBUTIVES_POSSESSIVPRONOMEN:
				pTarget.append("PRO.Poss.Attr");
				featureAusgeben(token.person);
				kngAusgeben(token);
				break;
			case SUBSTITUIERENDES_POSSESSIVPRONOMEN:
				pTarget.append("PRO.Poss.Subst");
				featureAusgeben(token.person);
				kngAusgeben(token);
				break;
			case REFLEXIVPRONOMEN:
				pTarget.append("PRO.Refl.Subst");
				featureAusgeben(token.person);
				kngAusgeben(token, false);
				break;
			case ATTRIBUTIVES_RELATIVPRONOMEN:
				pTarget.append("PRO.Rel.Attr");
				featureAusgeben(token.person);
				kngAusgeben(token);
				break;
			case SUBSTITUIERENDES_RELATIVPRONOMEN:
				pTarget.append("PRO.Rel.Subst");
				featureAusgeben(token.person);
				kngAusgeben(token);
				break;
			case PRONOMINALADVERB:
				pTarget.append("PROADV.Dem");
				break;
			case ADVERBIALES_INTERROGATIVPRONOMEN:
				pTarget.append("PROADV.Inter");
				break;
			case STERNCHEN:
				pTarget.append("SYM.Other.Aster");
				break;
			case APOSTROPH:
				pTarget.append("SYM.Pun.Apos");
				break;
			case AUTH:
				pTarget.append("SYM.Other.Auth");
				break;
			case NICHTWORT:
				pTarget.append("SYM.Other.XY");
				break;
			case OEFFNENDE_KLAMMER:
				pTarget.append("SYM.Paren.Left");
				break;
			case SCHLIESSENDE_KLAMMER:
				pTarget.append("SYM.Paren.Right");
				break;
			case DOPPELPUNKT:
				pTarget.append("SYM.Pun.Colon");
				break;
			case KOMMA:
				pTarget.append("SYM.Pun.Comma");
				break;
			case AUSLASSUNGSPUNKTE:
				pTarget.append("SYM.Pun.Cont");
				break;
			case BINDESTRICH:
				pTarget.append("SYM.Pun.Hyph");
				break;
			case SONSTIGE_INTERPUNKTION:
				pTarget.append("SYM.Pun.Other");
				break;
			case SATZENDE:
				pTarget.append("SYM.Pun.Sent");
				break;
			case SLASH:
				pTarget.append("SYM.Pun.Slash");
				break;
			case OEFFNENDES_ANFUEHRUNGSZEICHEN:
				pTarget.append("SYM.Quot.Left");
				break;
			case SCHLIESSENDES_ANFUEHRUNGSZEICHEN:
				pTarget.append("SYM.Quot.Right");
				break;
			case ABGETRENNTES_ADJEKTIVRESTGLIED:
				pTarget.append("TRUNC.Adj");
				break;
			case ABGETRENNTES_SUBSTANTIVRESTGLIED:
				pTarget.append("TRUNC.Noun");
				break;
			case ABGETRENNTES_VERBRESTGLIED:
				pTarget.append("TRUNC.Verb");
				break;
			case ABGETRENNTES_SONSTIGES_RESTGLIED:
				pTarget.append("TRUNC.-");
				break;
			case HILFSVERB_FINIT:
				pTarget.append("VFIN.Aux");
				pntmAusgeben(token);
				break;
			case HILFSVERB_IMPERATIV:
				pTarget.append("VIMP.Aux");
				pnAusgeben(token);
				break;
			case HILFSVERB_INFINITIV:
				pTarget.append("VINF.Aux.-");
				break;
			case HILFSVERB_PARTIZIP:
				pTarget.append("VPP.Aux");
				break;
			case MODALVERB_FINIT:
				pTarget.append("VFIN.Mod");
				pntmAusgeben(token);
				break;
			case MODALVERB_INFINITIV:
				pTarget.append("VINF.Mod.-");
				break;
			case MODALVERB_PARTIZIP:
				pTarget.append("VPP.Mod");
				break;
			case VOLLVERB_FINIT:
				pTarget.append("VFIN.Full");
				pntmAusgeben(token);
				break;
			case VOLLVERB_IMPERATIV:
				pTarget.append("VIMP.Full");
				pnAusgeben(token);
				break;
			case VOLLVERB_INFINITIV:
				pTarget.append("VINF.Full.-");
				break;
			case VOLLVERB_INFINITIV_MIT_ZU:
				pTarget.append("VINF.Full.zu");
				break;
			case VOLLVERB_PARTIZIP:
				pTarget.append("VPP.Full");
				break;
			default:
				// sollte nie eintreten, da alle Konstanten abgedeckt sind
				throw new KonverterException(token.pos.name() + " kann im Tiger-Format nicht dargestellt werden.", Arbeitsschritt.KONVERTIEREN);
		}
	}
	
	private void kngAusgeben(final Item token) throws IOException, KonverterException {
		kngAusgeben(token, true);
	}
	
	private void kngAusgeben(final Item token, final boolean genusErforderlich) throws IOException, KonverterException {
		featureAusgeben(token.kasus);
		featureAusgeben(token.numerus);
		featureAusgeben(token.genus, genusErforderlich);
	}
	
	private void pntmAusgeben(final Item token) throws IOException {
		pnAusgeben(token);
		featureAusgeben(token.tempus);
		featureAusgeben(token.modus);
	}
	
	private void pnAusgeben(final Item token) throws IOException, KonverterException {
		featureAusgeben(token.person);
		featureAusgeben(token.numerus);
	}
	
	private void featureAusgeben(final Steigerungsstufe grad) throws IOException, KonverterException {
		if (grad == null)
			throw new IllegalArgumentException();
		pTarget.append('.');
		switch (grad) {
			case POSITIV:
				pTarget.append("Pos");
				break;
			case KOMPARATIV:
				pTarget.append("Comp");
				break;
			case SUPERLATIV:
				pTarget.append("Sup");
				break;
			default:
				throw unbekannteKonstante(grad, "Grad");
		}
	}
	
	private void featureAusgeben(final Kasus kasus) throws IOException, KonverterException {
		if (kasus == null)
			throw new IllegalArgumentException();
		pTarget.append('.');
		switch (kasus) {
			case NOMINATIV:
				pTarget.append("Nom");
				break;
			case GENITIV:
				pTarget.append("Gen");
				break;
			case DATIV:
				pTarget.append("Dat");
				break;
			case AKKUSATIV:
				pTarget.append("Acc");
				break;
			case UNTERSPEZIFIZIERT:
				pTarget.append('*');
				break;
			default:
				throw unbekannteKonstante(kasus, "Kasus");
		}
	}
	
	private void featureAusgeben(final Numerus numerus) throws IOException, KonverterException {
		if (numerus == null)
			throw new IllegalArgumentException();
		pTarget.append('.');
		switch (numerus) {
			case SINGULAR:
				pTarget.append("Sg");
				break;
			case PLURAL:
				pTarget.append("Pl");
				break;
			case UNTERSPEZIFIZIERT:
				pTarget.append('*');
				break;
			default:
				throw unbekannteKonstante(numerus, "Numerus");
		}
	}
	
	private void featureAusgeben(final Genus genus, final boolean erforderlich) throws IOException, KonverterException {
		if (genus == null) {
			if (erforderlich)
				throw new IllegalArgumentException();
			pTarget.append(".-");
			return;
		}
		pTarget.append('.');
		switch (genus) {
			case MASKULINUM:
				pTarget.append("Masc");
				break;
			case FEMININUM:
				pTarget.append("Fem");
				break;
			case NEUTRUM:
				pTarget.append("Neut");
				break;
			case UNTERSPEZIFIZIERT:
				pTarget.append('*');
				break;
			default:
				throw unbekannteKonstante(genus, "Genus");
		}
	}
	
	private void featureAusgeben(final Person person) throws IOException, KonverterException {
		pTarget.append('.');
		if (person == null) {
			pTarget.append('-');
			return;
		}
		switch (person) {
			case ERSTE:
				pTarget.append('1');
				break;
			case ZWEITE:
				pTarget.append('2');
				break;
			case DRITTE:
				pTarget.append('3');
				break;
			default:
				throw new KonverterException("Person nicht vorgesehen: " + person.name(), Arbeitsschritt.AUSGEBEN);
		}
	}
	
	private void featureAusgeben(final Tempus tempus) throws IOException, KonverterException {
		if (tempus == null)
			throw new IllegalArgumentException();
		pTarget.append('.');
		switch (tempus) {
			case PRAESENS:
				pTarget.append("Pres");
				break;
			case PRAETERITUM:
				pTarget.append("Past");
				break;
			default:
				throw unbekannteKonstante(tempus, "Tempus");
		}
	}
	
	private void featureAusgeben(final Modus modus) throws IOException {
		if (modus == null)
			throw new IllegalArgumentException();
		pTarget.append('.');
		switch (modus) {
			case INDIKATIV:
				pTarget.append("Ind");
				break;
			case KONJUNKTIV:
				pTarget.append("Subj");
				break;
			default:
				throw unbekannteKonstante(modus, "Modus");
		}
	}
	
	private static KonverterException unbekannteKonstante(final Enum<?> konstante, final String kontext) {
		return new KonverterException(kontext + "-Konstante " + konstante.name() + " kann im Tiger-Format nicht ausgegeben werden.", Arbeitsschritt.AUSGEBEN);
	}

}
