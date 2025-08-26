package X;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnalisadorLexico {

    private final BufferedReader leitor;
    private String linhaAtual;
    private int numeroLinha;
    private int coluna;
    private final Map<String, TipoToken> palavrasChave;

    public AnalisadorLexico(String nomeArquivo) throws IOException {
        this.leitor = new BufferedReader(new FileReader(nomeArquivo));
        this.linhaAtual = leitor.readLine();
        this.numeroLinha = 1;
        this.coluna = 0;
        this.palavrasChave = new HashMap<>();
        inicializarPalavrasChave();
    }

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

    public Token proximoToken() throws IOException {
        while (linhaAtual != null) {
            if (coluna >= linhaAtual.length()) {
                avancarLinha();
                continue;
            }

            char caractere = linhaAtual.charAt(coluna);

            if (Character.isWhitespace(caractere)) {
                coluna++;
                continue;
            }

            if (caractere == '#') {
                avancarLinha();
                continue;
            }

            if (Character.isLetter(caractere)) {
                return tratarPalavra();
            }

            if (Character.isDigit(caractere)) {
                return tratarNumero();
            }

            if (caractere == '"') {
                return tratarCadeia();
            }

            return tratarSimbolos();
        }

        return new Token(TipoToken.EOF, "EOF", numeroLinha);
    }

    private void avancarLinha() throws IOException {
        linhaAtual = leitor.readLine();
        numeroLinha++;
        coluna = 0;
    }

    private Token tratarPalavra() {
        int inicio = coluna;
        while (coluna < linhaAtual.length() && Character.isLetterOrDigit(linhaAtual.charAt(coluna))) {
            coluna++;
        }
        String lexema = linhaAtual.substring(inicio, coluna);

        if (palavrasChave.containsKey(lexema)) {
            return new Token(palavrasChave.get(lexema), lexema, numeroLinha);
        }

        if (Character.isLowerCase(lexema.charAt(0))) {
            return new Token(TipoToken.Var, lexema, numeroLinha);
        }

        System.err.println("Erro Léxico na linha " + numeroLinha + ": Identificador inválido \"" + lexema + "\" (deve começar com letra minúscula).");
        return new Token(TipoToken.ERRO, lexema, numeroLinha);
    }

    private Token tratarNumero() {
        int inicio = coluna;
        boolean temPonto = false;

        while (coluna < linhaAtual.length() && (Character.isDigit(linhaAtual.charAt(coluna)) || linhaAtual.charAt(coluna) == '.')) {
            if (linhaAtual.charAt(coluna) == '.') {
                if (temPonto) break;
                temPonto = true;
            }
            coluna++;
        }
        String lexema = linhaAtual.substring(inicio, coluna);
        TipoToken tipo = temPonto ? TipoToken.NumReal : TipoToken.NumInt;
        return new Token(tipo, lexema, numeroLinha);
    }

    private Token tratarCadeia() {
        int inicio = coluna;
        coluna++;
        while (coluna < linhaAtual.length() && linhaAtual.charAt(coluna) != '"') {
            coluna++;
        }

        if (coluna < linhaAtual.length()) {
            coluna++;
            String lexema = linhaAtual.substring(inicio + 1, coluna - 1);
            return new Token(TipoToken.Cadeia, lexema, numeroLinha);
        } else {
            String lexema = linhaAtual.substring(inicio);
            System.err.println("Erro Léxico na linha " + numeroLinha + ": Cadeia de caracteres não foi fechada.");
            return new Token(TipoToken.ERRO, lexema, numeroLinha);
        }
    }

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

        String lexema = String.valueOf(caractere);
        System.err.println("Erro Léxico na linha " + numeroLinha + ": Símbolo desconhecido \"" + lexema + "\"");
        coluna++;
        return new Token(TipoToken.ERRO, lexema, numeroLinha);
    }
}