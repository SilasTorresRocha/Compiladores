package ATV1;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnalisadorLexico {
    private LeitorArquivo leitor;
    private Map<String, TipoToken> palavrasChave;
    
    public AnalisadorLexico(String nomeArquivo) throws IOException {
        this.leitor = new LeitorArquivo(nomeArquivo);
        inicializarPalavrasChave();
    }
    
    private void inicializarPalavrasChave() {
        palavrasChave = new HashMap<>();
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
    
    public Token proxToken() throws IOException {
        // Pular espaços em branco, quebras de linha e comentários
        pularEspacosEComentarios();
        
        // Verificar se chegou ao final do arquivo
        if (leitor.isFimArquivo()) {
            return new Token("EOF", TipoToken.EOF, leitor.getLinha(), leitor.getColuna());
        }
        
        // Marcar a posição inicial do token
        leitor.marcarPosicao();
        char c = leitor.lerProximoChar();
        
        // Identificar o tipo de token baseado no primeiro caractere
        if (Character.isLetter(c) && Character.isUpperCase(c)) {
            return processarPalavraChave(c);
        } else if (Character.isLetter(c) && Character.isLowerCase(c)) {
            return processarVariavel(c);
        } else if (Character.isDigit(c)) {
            return processarNumero(c);
        } else if (c == '"') {
            return processarCadeiaCaracteres();
        } else {
            return processarSimbolo(c);
        }
    }
    
    private void pularEspacosEComentarios() throws IOException {
        char c;
        boolean emComentario = false;
        
        while (true) {
            if (leitor.isFimArquivo()) return;
            
            c = leitor.lerProximoChar();
            
            if (emComentario) {
                if (c == '\n') {
                    emComentario = false;
                }
                continue;
            }
            
            if (c == '#') {
                emComentario = true;
                continue;
            }
            
            if (!Character.isWhitespace(c)) {
                leitor.retroceder();
                return;
            }
        }
    }
    
    private Token processarPalavraChave(char primeiroChar) throws IOException {
        StringBuilder lexema = new StringBuilder();
        lexema.append(primeiroChar);
        
        while (true) {
            if (leitor.isFimArquivo()) break;
            
            char c = leitor.lerProximoChar();
            if (Character.isLetter(c) || c == '_') {
                lexema.append(c);
            } else {
                leitor.retroceder();
                break;
            }
        }
        
        String lexemaStr = lexema.toString();
        TipoToken tipo = palavrasChave.get(lexemaStr);
        
        if (tipo != null) {
            return new Token(lexemaStr, tipo, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
        } else {
            return new Token(lexemaStr, TipoToken.ERRO, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
        }
    }
    
    private Token processarVariavel(char primeiroChar) throws IOException {
        StringBuilder lexema = new StringBuilder();
        lexema.append(primeiroChar);
        
        while (true) {
            if (leitor.isFimArquivo()) break;
            
            char c = leitor.lerProximoChar();
            if (Character.isLetterOrDigit(c) || c == '_') {
                lexema.append(c);
            } else {
                leitor.retroceder();
                break;
            }
        }
        
        return new Token(lexema.toString(), TipoToken.Var, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
    }
    
    private Token processarNumero(char primeiroChar) throws IOException {
        StringBuilder lexema = new StringBuilder();
        lexema.append(primeiroChar);
        boolean pontoEncontrado = false;
        boolean erro = false;
        
        while (true) {
            if (leitor.isFimArquivo()) break;
            
            char c = leitor.lerProximoChar();
            if (Character.isDigit(c)) {
                lexema.append(c);
            } else if (c == '.' && !pontoEncontrado) {
                lexema.append(c);
                pontoEncontrado = true;
            } else if (c == '.' && pontoEncontrado) {
                // Dois pontos - erro
                lexema.append(c);
                erro = true;
            } else {
                leitor.retroceder();
                break;
            }
        }
        
        if (erro) {
            return new Token(lexema.toString(), TipoToken.ERRO, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
        } else if (pontoEncontrado) {
            return new Token(lexema.toString(), TipoToken.NumReal, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
        } else {
            return new Token(lexema.toString(), TipoToken.NumInt, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
        }
    }
    
    private Token processarCadeiaCaracteres() throws IOException {
        StringBuilder lexema = new StringBuilder();
        lexema.append('"');
        boolean terminada = false;
        
        while (true) {
            if (leitor.isFimArquivo()) {
                break;
            }
            
            char c = leitor.lerProximoChar();
            lexema.append(c);
            
            if (c == '"') {
                terminada = true;
                break;
            }
            
            if (c == '\n') {
                // Quebra de linha dentro da cadeia - erro
                break;
            }
        }
        
        if (!terminada) {
            return new Token(lexema.toString(), TipoToken.ERRO, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
        } else {
            return new Token(lexema.toString(), TipoToken.Cadeia, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
        }
    }
    
    private Token processarSimbolo(char primeiroChar) throws IOException {
        switch (primeiroChar) {
            case '[':
                return new Token("[", TipoToken.DelimAbre, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                
            case ']':
                return new Token("]", TipoToken.DelimFecha, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                
            case '(':
                return new Token("(", TipoToken.AbrePar, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                
            case ')':
                return new Token(")", TipoToken.FechaPar, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                
            case ':':
                char proximo = leitor.lerProximoChar();
                if (proximo == '=') {
                    return new Token(":=", TipoToken.Atrib, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                } else {
                    leitor.retroceder();
                    return new Token(":", TipoToken.ERRO, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                }
                
            case '+':
                return new Token("+", TipoToken.OpAritSoma, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                
            case '-':
                return new Token("-", TipoToken.OpAritSub, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                
            case '*':
                return new Token("*", TipoToken.OpAritMult, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                
            case '/':
                return new Token("/", TipoToken.OpAritDiv, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                
            case '<':
                proximo = leitor.lerProximoChar();
                if (proximo == '=') {
                    return new Token("<=", TipoToken.OpRelMenorIgual, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                } else {
                    leitor.retroceder();
                    return new Token("<", TipoToken.OpRelMenor, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                }
                
            case '>':
                proximo = leitor.lerProximoChar();
                if (proximo == '=') {
                    return new Token(">=", TipoToken.OpRelMaiorIgual, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                } else {
                    leitor.retroceder();
                    return new Token(">", TipoToken.OpRelMaior, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                }
                
            case '=':
                proximo = leitor.lerProximoChar();
                if (proximo == '=') {
                    return new Token("==", TipoToken.OpRelIgual, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                } else {
                    leitor.retroceder();
                    return new Token("=", TipoToken.ERRO, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                }
                
            case '!':
                proximo = leitor.lerProximoChar();
                if (proximo == '=') {
                    return new Token("!=", TipoToken.OpRelDif, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                } else {
                    leitor.retroceder();
                    return new Token("!", TipoToken.ERRO, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
                }
                
            default:
                return new Token(String.valueOf(primeiroChar), TipoToken.ERRO, leitor.getMarcadorLinha(), leitor.getMarcadorColuna());
        }
    }
    
    public void fechar() throws IOException {
        leitor.fechar();
    }
}