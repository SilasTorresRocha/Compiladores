package X;
public class Token {
    public final TipoToken tipo;
    public final String lexema;
    public final int linha;
    public final int hash;

    public Token(TipoToken tipo, String lexema, int linha) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linha = linha;
        this.hash = lexema.hashCode(); 
    }

    @Override
    public String toString() {
        return "<" + tipo + ", \"" + lexema + "\", linha: " + linha + ", hash: " + hash + ">";
    }
}