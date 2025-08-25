package ATV1;
public class Token {
    private String lexema;
    private TipoToken tipo;
    private int linha;
    private int coluna;
    
    public Token(String lexema, TipoToken tipo, int linha, int coluna) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.linha = linha;
        this.coluna = coluna;
    }
    
    public String getLexema() { return lexema; }
    public TipoToken getTipo() { return tipo; }
    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }
    
    @Override
    public String toString() {
        return "<" + tipo + ", " + lexema + "> (Linha: " + linha + ", Coluna: " + coluna + ")";
    }
}