/* This Source Code Form is subject to the terms of the hermA Licence.
 * If a copy of the licence was not distributed with this file, You have
 * received this Source Code Form in a manner that does not comply with
 * the terms of the licence.
 */
package konverter.exception;

public class KonverterException extends RuntimeException {
	
	// von Eclipse erzeugt
	private static final long serialVersionUID = -5573306126201273787L;
	
	private final Arbeitsschritt pArbeitsschritt;
	
	public KonverterException(final Arbeitsschritt arbeitsschritt) {
		pArbeitsschritt = arbeitsschritt;
	}
	
	public KonverterException(final String message, final Throwable cause, final Arbeitsschritt arbeitsschritt) {
		super(message, cause);
		pArbeitsschritt = arbeitsschritt;
	}
	
	public KonverterException(final String message, final Arbeitsschritt arbeitsschritt) {
		super(message);
		pArbeitsschritt = arbeitsschritt;
	}
	
	public KonverterException(final Throwable cause, final Arbeitsschritt arbeitsschritt) {
		super(cause);
		pArbeitsschritt = arbeitsschritt;
	}
	
	public Arbeitsschritt getArbeitsschritt() {
		return pArbeitsschritt;
	}
	
}
