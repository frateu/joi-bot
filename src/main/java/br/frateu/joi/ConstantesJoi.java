package br.frateu.joi;

public class ConstantesJoi {
    // Constantes da Função de Economia
    public static final String CONST_ECONOMIA_STATUS_ESCOLHER = "ES";
    public static final String CONST_ECONOMIA_STATUS_MENU = "MN";
    public static final String CONST_ECONOMIA_ESCOLHA_CONVERSAO = "CV";
    public static final String CONST_ECONOMIA_ESCOLHA_GASTO_FIXO = "GF";

    // Constantes da Sub-Função de conversão de moeda
    public static final String CONST_CONVERSAO_MOEDA_STATUS_INICIAR = "IN";
    public static final String CONST_CONVERSAO_MOEDA_STATUS_CONVERTER = "VC";

    // Constantes da Sub-Função de gasto fixo
    public static final String CONST_GASTO_FIXO_INCLUIR = "GFI";
    public static final String CONST_GASTO_FIXO_CONSULTAR = "GFC";
    public static final String CONST_GASTO_FIXO_DESCRICAO = "GFD";
    public static final String CONST_GASTO_FIXO_VALOR = "GFV";
    public static final String CONST_GASTO_FIXO_USUARIO = "GFU";

    // Constantes de Moedas
    public static final String CONST_MOEDA_IENE = "jpy";
    public static final String CONST_MOEDA_DOLAR = "usd";
    public static final String CONST_MOEDA_REAL = "brl";

    // Constantes de Opções de Menu
    public static final String CONST_OPCOES_MENU = "\n/gastofixo\n/conversaomoeda";
    public static final String CONST_OPCOES_CONVERSAO = "\n/ienereal\n/dolarreal\n/realiene\n/realdolar";
    public static final String CONST_OPCOES_GASTO_FIXO = "\n/incluir\n/pesquisar";
}
