package ATV1;
public enum TipoToken {
    // Palavras-chave
    PCDec, PCProg, PCInt, PCReal, PCLer, PCImprimir, 
    PCSe, PCEntao, PCSenao, PCEnqto, PCIni, PCFim,
    
    // Operadores Aritméticos
    OpAritMult, OpAritDiv, OpAritSoma, OpAritSub,
    
    // Operadores Relacionais
    OpRelMenor, OpRelMenorIgual, OpRelMaior, 
    OpRelMaiorIgual, OpRelIgual, OpRelDif,
    
    // Operadores Booleanos
    OpBoolE, OpBoolOu,
    
    // Delimitadores
    DelimAbre, DelimFecha,
    
    // Atribuição
    Atrib,
    
    // Parênteses
    AbrePar, FechaPar,
    
    // Identificadores e literais
    Var, NumInt, NumReal, Cadeia,
    
    // Fim de arquivo e erro
    EOF, ERRO
}