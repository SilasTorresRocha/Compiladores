package X;

import java.io.IOException;
public class Principal {
    public static void main(String[] args) {
        String nomeArquivo = "./X/arq.gyh"; 
        try {
            AnalisadorLexico analisador = new AnalisadorLexico(nomeArquivo);
            Token token;

            System.out.println("Iniciando análise léxica do arquivo: " + nomeArquivo);
            System.out.println("--------------------------------------------------");

            do {
                token = analisador.proximoToken();
                System.out.println(token);
            } while (token.tipo != TipoToken.EOF);

            System.out.println("--------------------------------------------------");
            System.out.println("Análise léxica concluída.");

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo '" + nomeArquivo + "': " + e.getMessage());
        }
    }
}