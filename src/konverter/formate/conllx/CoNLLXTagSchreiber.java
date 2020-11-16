/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.formate.conllx;

import java.io.IOException;

import konverter.daten.Genus;
import konverter.daten.Item;
import konverter.daten.Itemtyp;
import konverter.daten.Kasus;
import konverter.daten.Modus;
import konverter.daten.Numerus;
import konverter.daten.Person;
import konverter.daten.Steigerungsstufe;
import konverter.daten.Tempus;
import konverter.exception.KonverterException;
import konverter.formate.conll.CoNLLFeatures;
import konverter.konvertierung.Senke;
import konverter.tagsets.STTS;

public class CoNLLXTagSchreiber implements Senke {
	
	private final Appendable pTarget;
	
	private boolean pFirstFeature;
	
	public CoNLLXTagSchreiber(final Appendable target) {
		pTarget = target;
	}
	
	@Override
	public void next(final Item token) throws IOException, KonverterException {
		if (token.typ == Itemtyp.TOKEN) {
			pTarget.append(token.index.toString());
			pTarget.append('\t');
			pTarget.append(token.form);
			pTarget.append('\t');
			if (token.lemma == null)
				pTarget.append('_');
			else
				pTarget.append(token.lemma);
			pTarget.append("\t_\t");
			pTarget.append(STTS.toSTTS(token.pos));
			pTarget.append('\t');
			featuresAusgeben(token.kasus, token.numerus, token.genus, token.grad, token.person, token.tempus, token.modus);
			pTarget.append("\t_\t_\t_\t_");
		}
		pTarget.append('\n');
	}
	
	private void featuresAusgeben(final Kasus caseName, final Numerus number, final Genus gender, final Steigerungsstufe degree, final Person person, final Tempus tense, final Modus mood) throws IOException, KonverterException {
		pFirstFeature = true;
		caseAusgeben(caseName);
		numberAusgeben(number);
		genderAusgeben(gender);
		degreeAusgeben(degree);
		personAusgeben(person);
		tenseAusgeben(tense);
		moodAusgeben(mood);
		
		if (pFirstFeature)
			pTarget.append("_");
	}
	
	private void numberAusgeben(final Numerus number) throws IOException, KonverterException {
		if (number == null)
			return;
		startFeature("number=");
		CoNLLFeatures.numerusSchreiben(number, pTarget);
	}
	
	private void genderAusgeben(final Genus gender) throws IOException, KonverterException {
		if (gender == null)
			return;
		startFeature("gender=");
		CoNLLFeatures.genusSchreiben(gender, pTarget);
	}
	
	private void personAusgeben(final Person person) throws IOException, KonverterException {
		if (person == null)
			return;
		startFeature("person=");
		CoNLLFeatures.personSchreiben(person, pTarget);
	}
	
	private void degreeAusgeben(final Steigerungsstufe degree) throws IOException, KonverterException {
		if (degree == null)
			return;
		startFeature("degree=");
		CoNLLFeatures.gradSchreiben(degree, pTarget);
	}
	
	private void tenseAusgeben(final Tempus tense) throws IOException, KonverterException {
		if (tense == null)
			return;
		startFeature("tense=");
		CoNLLFeatures.tempusSchreiben(tense, pTarget);
	}
	
	private void caseAusgeben(final Kasus caseName) throws IOException, KonverterException {
		if (caseName == null)
			return;
		startFeature("case=");
		CoNLLFeatures.kasusSchreiben(caseName, pTarget);
	}
	
	private void moodAusgeben(final Modus mood) throws IOException, KonverterException {
		if (mood == null)
			return;
		startFeature("mood=");
		CoNLLFeatures.modusSchreiben(mood, pTarget);
	}
	
	private void startFeature(final String featurePrefix) throws IOException {
		if (pFirstFeature)
			pFirstFeature = false;
		else
			pTarget.append('|');
		pTarget.append(featurePrefix);
	}
	
}
