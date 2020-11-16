/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.formate.tsv;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import konverter.daten.Item;
import konverter.daten.Itemtyp;
import konverter.exception.KonverterException;
import konverter.formate.conll.CoNLLFeatures;
import konverter.konvertierung.Quelle;
import konverter.tagsets.STTS;


public class TsvTagParser implements Quelle {
	
	private static final Pattern TAB_PATTERN = Pattern.compile(Pattern.quote("\t"));
	
	private final BufferedReader pReader;
	
	private BigInteger pIndex;
	
	public TsvTagParser(final BufferedReader reader) {
		pReader = reader;
		new ArrayList<>();
		
		pIndex = BigInteger.ZERO;
	}
	
	@Override
	public Item next() throws IOException, KonverterException {
		final Item result = new Item();
		
		final String line = pReader.readLine();
		if (line == null) {
			result.typ = Itemtyp.ENDE;
			return result;
		}
		
		final String[] lineParts = TAB_PATTERN.split(line);
		switch (lineParts.length) {
			default: // > 8 (split(line).length > 0)
				set(result, lineParts, 8, TsvTagParser::modusSetzen);
			case 8:
				set(result, lineParts, 7, TsvTagParser::tempusSetzen);
			case 7:
				set(result, lineParts, 6, TsvTagParser::personSetzen);
			case 6:
				set(result, lineParts, 5, TsvTagParser::gradSetzen);
			case 5:
				set(result, lineParts, 4, TsvTagParser::numerusSetzen);
			case 4:
				set(result, lineParts, 3, TsvTagParser::kasusSetzen);
			case 3:
				set(result, lineParts, 2, TsvTagParser::genusSetzen);
			case 2:
				result.pos = STTS.fromSTTS(result.form, lineParts[1]);
			case 1:
				final String token = lineParts[0];
				if ("".equals(token)) {
					result.typ = Itemtyp.LEERZEILE;
					pIndex = BigInteger.ZERO;
					break;
				}
				result.typ = Itemtyp.TOKEN;
				pIndex = BigInteger.ONE.add(pIndex);
				result.index = pIndex;
				result.lemma = null;
				result.form = token;
		}
		
		return result;
	}
	
	private void set(final Item targetToken, final String[] lineParts, final int index, final BiConsumer<Item, String> setter) {
		final String field = lineParts[index];
		if (!"".equals(field))
			setter.accept(targetToken, field);
	}
	
	private static void genusSetzen(final Item token, final String str) {
		token.genus = CoNLLFeatures.genusParsen(str);
	}
	
	private static void kasusSetzen(final Item token, final String str) {
		token.kasus = CoNLLFeatures.kasusParsen(str);
	}
	
	private static void numerusSetzen(final Item token, final String str) {
		token.numerus = CoNLLFeatures.numerusParsen(str);
	}
	
	private static void gradSetzen(final Item token, final String str) {
		token.grad = CoNLLFeatures.gradParsen(str);
	}
	
	private static void personSetzen(final Item token, final String str) {
		token.person = CoNLLFeatures.personParsen(str);
	}
	
	private static void tempusSetzen(final Item token, final String str) {
		token.tempus = CoNLLFeatures.tempusParsen(str);
	}
	
	private static void modusSetzen(final Item token, final String str) {
		token.modus = CoNLLFeatures.modusParsen(str);
	}
	
}
