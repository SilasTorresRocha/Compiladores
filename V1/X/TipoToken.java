package X;
/**
 * Enumeração dos tipos de tokens possíveis na linguagem GYH.
 * Cada tipo corresponde a uma categoria de lexema definida na especificação da linguagem.
 */
public enum TipoToken {
    // Palavras-chave
    PCDec, PCProg, PCInt, PCReal, PCLer, PCImprimir, PCSe, PCEntao, PCSenao, PCEnqto, PCIni, PCFim,

    // Operadores Aritméticos
    OpAritMult, OpAritDiv, OpAritSoma, OpAritSub,

    // Operadores Relacionais
    OpRelMenor, OpRelMenorIgual, OpRelMaior, OpRelMaiorIgual, OpRelIgual, OpRelDif,

    // Operadores Booleanos
    OpBoolE, OpBoolOu,

    // Delimitadores e Pontuação
    DelimAbre, DelimFecha, // [ ]
    AbrePar, FechaPar,     // ( )
    Delim,                 // : (Adicionado para o exemplo "parametro:INTEGER")

    // Outros
    Atrib,                 // :=
    Var,                   // Variáveis
    NumInt,                // Número Inteiro
    NumReal,               // Número Real
    Cadeia,                // Cadeia de caracteres

    // Tokens de Controle
    EOF,                   // Fim de Arquivo (End of File)
    ERRO                   // Token desconhecido ou erro léxico
}