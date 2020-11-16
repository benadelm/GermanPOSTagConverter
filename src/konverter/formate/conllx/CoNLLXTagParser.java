/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.formate.conllx;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.regex.Pattern;

import konverter.daten.Item;
import konverter.daten.Itemtyp;
import konverter.exception.Arbeitsschritt;
import konverter.exception.KonverterException;
import konverter.formate.conll.CoNLLFeatures;
import konverter.konvertierung.Quelle;
import konverter.tagsets.STTS;

public class CoNLLXTagParser implements Quelle {
	
	private static final Pattern TAB_PATTERN = Pattern.compile(Pattern.quote("\t"));
	private static final Pattern PIPE_PATTERN = Pattern.compile(Pattern.quote("|"));
	
	private final BufferedReader pReader;
	
	public CoNLLXTagParser(final BufferedReader reader) {
		pReader = reader;
	}
	
	@Override
	public Item next() throws IOException, KonverterException {
		final Item result = new Item();
		
		final String line = pReader.readLine();
		if (line == null)
			result.typ = Itemtyp.ENDE;
		else if ("".equals(line))
			result.typ = Itemtyp.LEERZEILE;
		else
			parsen(line, result);
		return result;
	}
	
	private void parsen(final String line, final Item result) {
		final String[] lineParts = TAB_PATTERN.split(line, 7);
		
		if (lineParts.length < 6)
			throw new KonverterException("Zu wenige Bestandteile: " + line, Arbeitsschritt.EINLESEN);
		
		result.index = idEinlesen(lineParts[0]);
		result.form = lineParts[1];
		
		result.pos = STTS.fromSTTS(result.form, lineParts[4]);
		lemmaEinlesen(lineParts[2], result);
		featuresParsen(lineParts[5], result);
		
		result.typ = Itemtyp.TOKEN;
	}
	
	private static BigInteger idEinlesen(final String value) {
		return new BigInteger(value.trim(), 10);
	}
	
	private static void lemmaEinlesen(final String lemma, final Item targetToken) {
		targetToken.lemma = "_".equals(lemma) ? null : lemma;
	}
	
	private static void featuresParsen(final String featuresString, final Item targetToken) {
		if ("_".equals(featuresString))
			return;
		for (final String feature : PIPE_PATTERN.split(featuresString))
			featureParsen(targetToken, feature);
	}
	
	private static void featureParsen(final Item result, final String feature) {
		final int eqIndex = feature.indexOf('=');
		if (eqIndex < 0)
			throw new KonverterException("Kein gültiges Schlüssel-Wert-Paar: " + feature, Arbeitsschritt.EINLESEN);
		featureParsen(result, feature.substring(0, eqIndex), feature.substring(eqIndex + 1));
	}
	
	private static void featureParsen(final Item result, final String key, final String value) {
		switch (key) {
			case "number":
				result.numerus = CoNLLFeatures.numerusParsen(value);
				break;
			case "gender":
				result.genus = CoNLLFeatures.genusParsen(value);
				break;
			case "mood":
				result.modus = CoNLLFeatures.modusParsen(value);
				break;
			case "person":
				result.person = CoNLLFeatures.personParsen(value);
				break;
			case "degree":
				result.grad = CoNLLFeatures.gradParsen(value);
				break;
			case "tense":
				result.tempus = CoNLLFeatures.tempusParsen(value);
				break;
			case "case":
				result.kasus = CoNLLFeatures.kasusParsen(value);
				break;
			default:
				throw new KonverterException("Unbekanntes Feature: " + key, Arbeitsschritt.EINLESEN);
		}
	}
	
}
