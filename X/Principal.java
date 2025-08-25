package X;

import java.io.IOException;

/**
 * Classe principal para executar o Analisador Léxico.
 * Ela instancia o analisador com um arquivo de código-fonte e imprime
 * a sequência de tokens gerada.
 */
public class Principal {
    public static void main(String[] args) {
        String nomeArquivo = "./X/arq.gyh"; // Nome do arquivo a ser analisado
        try {
            AnalisadorLexico analisador = new AnalisadorLexico(nomeArquivo);
            Token token;

            System.out.println("Iniciando análise léxica do arquivo: " + nomeArquivo);
            System.out.println("--------------------------------------------------");

            // Loop para obter e imprimir todos os tokens do arquivo
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