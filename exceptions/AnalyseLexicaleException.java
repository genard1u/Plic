package yal.exceptions;

/**
 * @author Pierre Génard
 */
@SuppressWarnings("serial")
public class AnalyseLexicaleException extends AnalyseException {
 
    public AnalyseLexicaleException(int ligne, int colonne, String m) {
        super("ERREUR LEXICALE :\n\tligne " + ligne + " colonne " + colonne + "\n\t" + m + " : caractère non reconnu") ;
    }

}
