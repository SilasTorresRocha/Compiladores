// java faz a maldadde de ler por lexema 

import java.io.IOException;

public class AP1 {
    public static void main(String[] args) throws IOException {
        //System.out.println(TipoToken.DelimAbre);     // plagio liberado kkkkk   
        AnalisadorLexo lex = new AnalisadorLexo("arq.gyh");
        Token t = lex.proxToken();
        while (t != null) {
            System.out.println(t.toString());
            t = lex.proxToken();
        }
    }
}
