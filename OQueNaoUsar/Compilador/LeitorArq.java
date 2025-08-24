import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class LeitorArq {
    public InputStream is;

    public LeitorArq(String nomeArq) throws FileNotFoundException{
        is= new FileInputStream(nomeArq);
    }
    public int lerProxCacter() throws IOException{
        int caractere= -1;
        caractere=is.read();
        return caractere;
    }
}
