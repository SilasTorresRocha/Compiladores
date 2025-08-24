public class Token {
    public String lexema;
    public TipoToken padrao;
    //mais inf

    public Token(String lexema, TipoToken padrao){
        this.lexema=lexema;
        this.padrao=padrao;
    }

    @Override
    public String toString(){
        return"<"+padrao+","+lexema+">";
    }
}
