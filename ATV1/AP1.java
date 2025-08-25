package ATV1;
import java.io.IOException;

public class AP1 {
    public static void main(String[] args) throws IOException {
        AnalisadorLexico lex = new AnalisadorLexico("./ATV1/ARQ.gyh");
        Token t = null;
        
        do {
            t = lex.proxToken();
            if (t != null) {
                System.out.println(t.toString());
                
                // Verificar erros léxicos
                if (t.getTipo() == TipoToken.ERRO) {
                    System.err.println("Erro Léxico na linha " + t.getLinha() + 
                                     ", coluna " + t.getColuna() + ": " + t.getLexema());
                    break;
                }
            }
        } while (t != null && t.getTipo() != TipoToken.EOF);
    }
}