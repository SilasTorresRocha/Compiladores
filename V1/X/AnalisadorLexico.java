package X;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Analisador Léxico para a linguagem GYH.
 * Responsável por ler um arquivo de código-fonte e dividi-lo em uma sequência de tokens.
 */
public class AnalisadorLexico {

    private final BufferedReader leitor; // Leitor de arquivo que lê linha por linha
    private String linhaAtual;           // Armazena a linha que está sendo analisada
    private int numeroLinha;             // Contador para o número da linha atual
    private int coluna;                  // Posição (coluna) atual na linha
    private final Map<String, TipoToken> palavrasChave; // Mapa para identificação rápida de palavras-chave
    private boolean erroOcorreu = false; // flag da vergonha da profissao (tirar isso da versao final)

    /**
     * Construtor do Analisador Léxico.
     * @param nomeArquivo O caminho para o arquivo .gyh a ser analisado.
     * @throws IOException Se ocorrer um erro ao abrir ou ler o arquivo.
     */
    public AnalisadorLexico(String nomeArquivo) throws IOException {
        this.leitor = new BufferedReader(new FileReader(nomeArquivo));
        this.linhaAtual = leitor.readLine(); // Lê a primeira linha do arquivo
        this.numeroLinha = 1;
        this.coluna = 0;
        this.palavrasChave = new HashMap<>();
        inicializarPalavrasChave();
    }

    public boolean getErroOcorreu() {
        return this.erroOcorreu;
    }

    /**
     * Preenche o mapa com todas as palavras-chave da linguagem e seus respectivos tipos de token.
     */
    private void inicializarPalavrasChave() {
        palavrasChave.put("DECLARAR", TipoToken.PCDec);
        palavrasChave.put("PROGRAMA", TipoToken.PCProg);
        palavrasChave.put("INTEGER", TipoToken.PCInt);
        palavrasChave.put("REAL", TipoToken.PCReal);
        palavrasChave.put("LER", TipoToken.PCLer);
        palavrasChave.put("IMPRIMIR", TipoToken.PCImprimir);
        palavrasChave.put("SE", TipoToken.PCSe);
        palavrasChave.put("ENTAO", TipoToken.PCEntao);
        palavrasChave.put("SENAO", TipoToken.PCSenao);
        palavrasChave.put("ENQTO", TipoToken.PCEnqto);
        palavrasChave.put("INICIO", TipoToken.PCIni);
        palavrasChave.put("FINAL", TipoToken.PCFim);
        palavrasChave.put("E", TipoToken.OpBoolE);
        palavrasChave.put("OU", TipoToken.OpBoolOu);
    }

    /**
     * Retorna o próximo token do código-fonte.
     * Este é o método principal que orquestra a análise.
     * @return O próximo token encontrado, ou um token EOF se o final do arquivo for alcançado.
     * @throws IOException Se ocorrer um erro de leitura.
     */
    public Token proximoToken() throws IOException {
        // Loop para ignorar espaços em branco, comentários e avançar para a próxima linha se necessário
        while (linhaAtual != null) {
            // Se chegamos ao fim da linha, lê a próxima
            if (coluna >= linhaAtual.length()) {
                avancarLinha();
                continue; // Volta ao início do loop para processar a nova linha
            }

            char caractere = linhaAtual.charAt(coluna);

            // Ignora espaços em branco
            if (Character.isWhitespace(caractere)) {
                coluna++;
                continue;
            }

            // Ignora comentários (que começam com '#')
            if (caractere == '#') {
                avancarLinha();
                continue;
            }

            // A partir daqui, analisamos os possíveis tokens
            
            // 1. Identificadores (Variáveis) e Palavras-Chave
            if (Character.isLetter(caractere)) {
                return tratarPalavra();
            }

            // 2. Números (Inteiros e Reais)
            if (Character.isDigit(caractere)) {
                return tratarNumero();
            }
            
            // 3. Cadeia de caracteres (delimitada por aspas duplas)
            if (caractere == '"') {
                return tratarCadeia();
            }

            // 4. Operadores e Delimitadores
            return tratarSimbolos();
        }

        // Se linhaAtual for nulo, chegamos ao fim do arquivo
        return new Token(TipoToken.EOF, "EOF", numeroLinha);
    }

    /**
     * Avança para a próxima linha do arquivo e reseta a coluna.
     */
    private void avancarLinha() throws IOException {
        linhaAtual = leitor.readLine();
        numeroLinha++;
        coluna = 0;
    }

    /**
     * Processa uma sequência de letras que pode ser uma palavra-chave ou uma variável.
     */
    private Token tratarPalavra() {
        int inicio = coluna;
        // Avança enquanto for letra ou dígito (regras para identificadores)
        while (coluna < linhaAtual.length() && Character.isLetterOrDigit(linhaAtual.charAt(coluna))) {
            coluna++;
        }
        String lexema = linhaAtual.substring(inicio, coluna);

        // Verifica se a palavra é uma palavra-chave
        if (palavrasChave.containsKey(lexema)) {
            return new Token(palavrasChave.get(lexema), lexema, numeroLinha);
        }
        
        // Verifica se é uma variável válida (começa com minúscula)
        if (Character.isLowerCase(lexema.charAt(0))) {
            return new Token(TipoToken.Var, lexema, numeroLinha);
        }

        // Se não for palavra-chave e não começar com minúscula, é um erro.
        System.err.println("Erro Lexico na linha " + numeroLinha + ": Identificador invalido \"" + lexema + "\" (deve comecar com letra minuscula).");
        this.erroOcorreu = true;
        return new Token(TipoToken.ERRO, lexema, numeroLinha);
    }

    /**
     * Processa uma sequência de dígitos que pode ser um número inteiro ou real.
     */
    private Token tratarNumero() {
        int inicio = coluna;
        boolean temPonto = false;
        
        while (coluna < linhaAtual.length() && (Character.isDigit(linhaAtual.charAt(coluna)) || linhaAtual.charAt(coluna) == '.')) {
            if (linhaAtual.charAt(coluna) == '.') {
                if (temPonto) break; // Já encontrou um ponto, para de ler
                temPonto = true;
            }
            coluna++;
        }
        String lexema = linhaAtual.substring(inicio, coluna);
        TipoToken tipo = temPonto ? TipoToken.NumReal : TipoToken.NumInt;
        return new Token(tipo, lexema, numeroLinha);
    }
    
    /**
     * Processa uma cadeia de caracteres delimitada por aspas duplas.
     */
    private Token tratarCadeia() {
        int inicio = coluna;
        coluna++; // Pula a aspa inicial
        while (coluna < linhaAtual.length() && linhaAtual.charAt(coluna) != '"') {
            coluna++;
        }
        
        if (coluna < linhaAtual.length()) {
            coluna++; // Pula a aspa final
            String lexema = linhaAtual.substring(inicio + 1, coluna - 1); // Extrai o conteúdo
            return new Token(TipoToken.Cadeia, lexema, numeroLinha);
        } else {
            // Se a linha terminar sem fechar as aspas, é um erro
            String lexema = linhaAtual.substring(inicio);
            System.err.println("Erro Lexico na linha " + numeroLinha + ": Cadeia de caracteres não foi fechada.");
            this.erroOcorreu = true;
            return new Token(TipoToken.ERRO, lexema, numeroLinha);
        }
    }
    
    /**
     * Processa operadores de um ou dois caracteres e delimitadores.
     * Utiliza a técnica de "lookahead" (olhar à frente) para operadores duplos.
     */
    private Token tratarSimbolos() {
        char caractere = linhaAtual.charAt(coluna);
        char proximo = (coluna + 1 < linhaAtual.length()) ? linhaAtual.charAt(coluna + 1) : '\0';

        switch (caractere) {
            case '[': coluna++; return new Token(TipoToken.DelimAbre, "[", numeroLinha);
            case ']': coluna++; return new Token(TipoToken.DelimFecha, "]", numeroLinha);
            case '(': coluna++; return new Token(TipoToken.AbrePar, "(", numeroLinha);
            case ')': coluna++; return new Token(TipoToken.FechaPar, ")", numeroLinha);
            case ':':
                if (proximo == '=') {
                    coluna += 2;
                    return new Token(TipoToken.Atrib, ":=", numeroLinha);
                } else {
                    coluna++;
                    return new Token(TipoToken.Delim, ":", numeroLinha);
                }
            case '*': coluna++; return new Token(TipoToken.OpAritMult, "*", numeroLinha);
            case '/': coluna++; return new Token(TipoToken.OpAritDiv, "/", numeroLinha);
            case '+': coluna++; return new Token(TipoToken.OpAritSoma, "+", numeroLinha);
            case '-': coluna++; return new Token(TipoToken.OpAritSub, "-", numeroLinha);
            case '<':
                if (proximo == '=') {
                    coluna += 2;
                    return new Token(TipoToken.OpRelMenorIgual, "<=", numeroLinha);
                }
                coluna++;
                return new Token(TipoToken.OpRelMenor, "<", numeroLinha);
            case '>':
                if (proximo == '=') {
                    coluna += 2;
                    return new Token(TipoToken.OpRelMaiorIgual, ">=", numeroLinha);
                }
                coluna++;
                return new Token(TipoToken.OpRelMaior, ">", numeroLinha);
            case '=':
                if (proximo == '=') {
                    coluna += 2;
                    return new Token(TipoToken.OpRelIgual, "==", numeroLinha);
                }
                break;
            case '!':
                if (proximo == '=') {
                    coluna += 2;
                    return new Token(TipoToken.OpRelDif, "!=", numeroLinha);
                }
                break;
        }

        // Se chegou aqui, o caractere é desconhecido
        String lexema = String.valueOf(caractere);
        System.err.println("Erro Lexico na linha " + numeroLinha + ": Simbolo desconhecido \"" + lexema + "\"");
        this.erroOcorreu = true;
        coluna++;
        return new Token(TipoToken.ERRO, lexema, numeroLinha);
    }
}