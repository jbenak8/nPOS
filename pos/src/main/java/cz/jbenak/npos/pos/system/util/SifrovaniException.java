package cz.jbenak.npos.pos.system.util;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-01-29
 */

public class SifrovaniException extends Exception {

    private static final long serialVersionUID = -6444512379134921777L;

    /**
     * Výchozí konstruktor. Pouze zanechá zprávu.
     */
    public SifrovaniException() {
        super("Nastal problém se šifrováním/dešifrováním vstupního řetězce.");
    }

    /**
     * Vyhodí hlášení společně s původní vyjímkou.
     * @param cause původní vyjímka.
     */
    public SifrovaniException(Throwable cause) {
        super("Nastal problém se šifrováním/dešifrováním vstupního řetězce.", cause);
    }

}
