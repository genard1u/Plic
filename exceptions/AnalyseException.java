package yal.exceptions;

/**
 * @author Pierre Génard
 */
@SuppressWarnings("serial")
public abstract class AnalyseException extends RuntimeException {
    
    protected AnalyseException(String m) {
        super(m);
    }

}
