package ATV1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorArquivo {
    private BufferedReader reader;
    private int linha;
    private int coluna;
    private int marcadorLinha;
    private int marcadorColuna;
    private boolean fimArquivo;
    
    public LeitorArquivo(String nomeArquivo) throws IOException {
        this.reader = new BufferedReader(new FileReader(nomeArquivo));
        this.linha = 1;
        this.coluna = 0;
        this.fimArquivo = false;
    }
    
    public char lerProximoChar() throws IOException {
        reader.mark(1);
        int charCode = reader.read();
        
        if (charCode == -1) {
            fimArquivo = true;
            return '\0';
        }
        
        char c = (char) charCode;
        
        if (c == '\n') {
            linha++;
            coluna = 0;
        } else {
            coluna++;
        }
        
        return c;
    }
    
    public void retroceder() throws IOException {
        reader.reset();
        if (coluna == 0) {
            linha--;
            // Não temos como saber a coluna anterior facilmente, 
            // então usamos uma aproximação
            coluna = 80; // valor arbitrário
        } else {
            coluna--;
        }
    }
    
    public void marcarPosicao() {
        marcadorLinha = linha;
        marcadorColuna = coluna;
    }
    
    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }
    public int getMarcadorLinha() { return marcadorLinha; }
    public int getMarcadorColuna() { return marcadorColuna; }
    public boolean isFimArquivo() { return fimArquivo; }
    
    public void fechar() throws IOException {
        reader.close();
    }
}