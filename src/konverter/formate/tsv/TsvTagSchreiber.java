/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.formate.tsv;

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

public class TsvTagSchreiber implements Senke {
	
	private final Appendable pTarget;
	
	public TsvTagSchreiber(final Appendable target) {
		pTarget = target;
	}
	
	@Override
	public void next(final Item token) throws IOException, KonverterException {
		if (token.typ == Itemtyp.TOKEN) {
			pTarget.append(token.form);
			pTarget.append('\t');
			pTarget.append(STTS.toSTTS(token.pos));
			pTarget.append('\t');
			spalteAusgeben(token.genus);
			pTarget.append('\t');
			spalteAusgeben(token.kasus);
			pTarget.append('\t');
			spalteAusgeben(token.numerus);
			pTarget.append('\t');
			spalteAusgeben(token.grad);
			pTarget.append('\t');
			spalteAusgeben(token.person);
			pTarget.append('\t');
			spalteAusgeben(token.tempus);
			pTarget.append('\t');
			spalteAusgeben(token.modus);
		}
		
		pTarget.append('\n');
	}
	
	private void spalteAusgeben(final Genus genus) throws IOException, KonverterException {
		if (genus == null)
			return;
		CoNLLFeatures.genusSchreiben(genus, pTarget);
	}
	
	private void spalteAusgeben(final Kasus kasus) throws IOException, KonverterException {
		if (kasus == null)
			return;
		CoNLLFeatures.kasusSchreiben(kasus, pTarget);
	}
	
	private void spalteAusgeben(final Numerus numerus) throws IOException, KonverterException {
		if (numerus == null)
			return;
		CoNLLFeatures.numerusSchreiben(numerus, pTarget);
	}
	
	private void spalteAusgeben(final Steigerungsstufe grad) throws IOException, KonverterException {
		if (grad == null)
			return;
		CoNLLFeatures.gradSchreiben(grad, pTarget);
	}
	
	private void spalteAusgeben(final Person person) throws IOException, KonverterException {
		if (person == null)
			return;
		CoNLLFeatures.personSchreiben(person, pTarget);
	}
	
	private void spalteAusgeben(final Tempus tempus) throws IOException, KonverterException {
		if (tempus == null)
			return;
		CoNLLFeatures.tempusSchreiben(tempus, pTarget);
	}
	
	private void spalteAusgeben(final Modus modus) throws IOException, KonverterException {
		if (modus == null)
			return;
		CoNLLFeatures.modusSchreiben(modus, pTarget);
	}
	
}
