package X;
//Silas Torres Rocha - 2104644
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// Classe principal do programa
public class Principal {
    public static void main(String[] args) {
        // Abre uma janela para o escolher o arquivo (Nao sei se funciona no Linux/Mac) kkkk 
        String nomeArquivo = null;
        try {
            // Tenta usar o visual padrão do Windows para o JFileChooser
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // Ignora se não conseguir
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione o arquivo .gyh para análise");
        int resultado = fileChooser.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            nomeArquivo = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            System.out.println("Nenhum arquivo selecionado. Encerrando.");
            return;
        }
// Nao sei usar args[] kkkk, traumas do C/C++
        try {
            // Cria o analisador léxico passando o nome do arquivo
            AnalisadorLexico analisador = new AnalisadorLexico(nomeArquivo);
            Token token;

            // Mensagem inicial da análise léxica
            System.out.println("Iniciando analise lexica do arquivo: " + nomeArquivo);
            System.out.println("--------------------------------------------------");

            // Loop para obter todos os tokens até EOF
            do {
                token = analisador.proximoToken();
                System.out.println(token); // Exibe o token encontrado
            } while (token.tipo != TipoToken.EOF);

            System.out.println("--------------------------------------------------");
            System.out.println("Analise lxeica concluida");

            // (tirar isso na versao final)
            // Se ocorreu erro léxico, abre um vídeo motivacional no YouTube
            if (analisador.getErroOcorreu()) {
                try {
                    URI uri = new URI("https://www.youtube.com/watch?v=jjh_r1sgdm4");  //motivacao
                    System.out.println("Erro lexico foi encontrado");
                    Desktop.getDesktop().browse(uri); // Abre o navegador padrão
                } catch (IOException | URISyntaxException e) {
                    System.err.println("Erro ao tentar abrir o video: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            // Caso ocorra erro ao ler o arquivo, exibe mensagem de erro
            System.err.println("Erro ao ler o arquivo '" + nomeArquivo + "': " + e.getMessage());
        }
    }
}