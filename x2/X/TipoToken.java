package X;

public enum TipoToken {
    PCDec, PCProg, PCInt, PCReal, PCLer, PCImprimir, PCSe, PCEntao, PCSenao, PCEnqto, PCIni, PCFim,
    OpAritMult, OpAritDiv, OpAritSoma, OpAritSub,
    OpRelMenor, OpRelMenorIgual, OpRelMaior, OpRelMaiorIgual, OpRelIgual, OpRelDif,
    OpBoolE, OpBoolOu,
    DelimAbre, DelimFecha, 
    AbrePar, FechaPar,     
    Delim,                 
    Atrib,                 
    Var,                   
    NumInt,                
    NumReal,               
    Cadeia,                
    EOF,                   
    ERRO       //para erros
}