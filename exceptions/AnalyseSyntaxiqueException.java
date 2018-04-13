package yal.exceptions;

/**
 * @author Pierre Génard
 */
@SuppressWarnings("serial")
public class AnalyseSyntaxiqueException extends AnalyseException {
 
    public AnalyseSyntaxiqueException(String m) {
        super("ERREUR SYNTAXIQUE :\n\t" + m) ;
    }

}
