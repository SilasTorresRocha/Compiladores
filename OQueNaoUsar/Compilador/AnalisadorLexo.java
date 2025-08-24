import java.io.FileNotFoundException;
import java.io.IOException;

public class AnalisadorLexo {
    public LeitorArq ldat;

    public AnalisadorLexo(String nomeArq) throws FileNotFoundException{
        ldat=new LeitorArq(nomeArq);
    }

    public Token proxToken() throws IOException{
        int caractereLido=-1;
        while ((caractereLido=ldat.lerProxCacter())!=-1) {
            char c = (char) caractereLido;
            switch (c) {
                case '[':
                    return new Token("[",TipoToken.DelimAbre);
                case ']':
                    return new Token("]",TipoToken.DelimFecha);
            }
        }

        return null;
    }
}
