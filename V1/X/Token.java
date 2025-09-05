package X;

/**
 * Representa um token, a menor unidade léxica da linguagem.
 * Armazena o tipo do token, o lexema (o texto extraído do código-fonte)
 * a linha onde foi encontrado e um código hash do lexema.
 */
public class Token {
    public final TipoToken tipo;
    public final String lexema;
    public final int linha;
    public final int hash;

    public Token(TipoToken tipo, String lexema, int linha) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linha = linha;
        this.hash = lexema.hashCode(); // Calcula o hash a partir do lexema
    }

    /**
     * Retorna uma representação em String do token no formato <tipo, "lexema", linha, hash>.
     * Este formato é útil para depuração e visualização da saída do analisador léxico.
     */
    @Override
    public String toString() {
        return "<" + tipo + ", \"" + lexema + "\", linha: " + linha + ", hash: " + hash + ">";
    }
}